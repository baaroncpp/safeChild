package com.bwongo.core.user_mgt.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.text.StringUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.dto.PageResponseDto;
import com.bwongo.core.base.model.enums.*;
import com.bwongo.core.base.service.AuditService;
import com.bwongo.core.base.utils.EnumValidations;
import com.bwongo.core.core_banking.service.MemberService;
import com.bwongo.core.school_mgt.model.dto.SchoolResponseDto;
import com.bwongo.core.school_mgt.model.jpa.TSchoolUser;
import com.bwongo.core.school_mgt.repository.SchoolRepository;
import com.bwongo.core.school_mgt.repository.SchoolUserRepository;
import com.bwongo.core.school_mgt.service.SchoolDtoService;
import com.bwongo.core.user_mgt.model.dto.UserRequestDto;
import com.bwongo.core.user_mgt.model.dto.UserResponseDto;
import com.bwongo.core.user_mgt.model.dto.*;
import com.bwongo.core.user_mgt.model.jpa.TPreviousPassword;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import com.bwongo.core.user_mgt.model.jpa.TUserApproval;
import com.bwongo.core.user_mgt.model.jpa.TUserMeta;
import com.bwongo.core.user_mgt.repository.*;
import com.bwongo.core.base.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.bwongo.core.base.utils.BaseUtils.pageToDto;
import static com.bwongo.core.base.utils.BasicMsgConstants.COUNTRY_WITH_ID_NOT_FOUND;
import static com.bwongo.core.base.utils.EnumValidations.isApprovalStatus;
import static com.bwongo.core.base.utils.EnumValidations.isUserType;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.SCHOOL_NOT_FOUND;
import static com.bwongo.core.user_mgt.utils.UserManagementUtils.checkThatUserIsAssignable;
import static com.bwongo.core.user_mgt.utils.UserManagementUtils.checkThatUserIsSchoolUser;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private static final String PASSWORD = "password";
    private static final String USERTYPE = "userType";

    private final TUserRepository userRepository;
    private final TUserGroupRepository userGroupRepository;
    private final TUserMetaRepository userMetaRepository;
    private final TCountryRepository countryRepository;
    private final TUserApprovalRepository userApprovalRepository;
    private final AuditService auditService;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoService userDtoService;
    private final SchoolRepository schoolRepository;
    private final SchoolUserRepository schoolUserRepository;
    private final SchoolDtoService schoolDtoService;
    private final MemberService memberService;
    private final TPreviousPasswordRepository previousPasswordRepository;
    private final TGroupAuthorityRepository groupAuthorityRepository;

    @Transactional
    public boolean changePassword(ChangePasswordRequestDto changePasswordRequestDto){

        changePasswordRequestDto.validate();
        var existingUser = userRepository.findById(auditService.getLoggedInUser().getId());
        var user = existingUser.get();

        var newPassword = changePasswordRequestDto.newPassword();
        var oldPassword = changePasswordRequestDto.oldPassword();
        var oldEncryptedPassword = user.getPassword();

        Validate.isTrue(this, passwordEncoder.matches(oldPassword, oldEncryptedPassword), ExceptionType.BAD_REQUEST, OLD_PASSWORDS_DONT_MATCH);
        Validate.isTrue(this, !newPassword.equals(oldPassword), ExceptionType.BAD_REQUEST, OLD_NEW_SAME_PASSWORD);

        var previousPasswords = previousPasswordRepository.findAllByUser(user);

        previousPasswords.forEach(previousPassword -> Validate.isTrue(this, !(passwordEncoder.matches(newPassword, previousPassword.getPreviousPassword())) ,
                        ExceptionType.BAD_REQUEST,
                        PASSWORD_USED_BEFORE));

        var optionalMaxPasswordCount = previousPasswords.stream().mapToInt(TPreviousPassword::getPasswordChangeCount).max();
        int maxPasswordCount = 0;

        if(optionalMaxPasswordCount.isPresent())
            maxPasswordCount = optionalMaxPasswordCount.getAsInt();

        var previousPassword = userDtoService.dtoToTPreviousPassword(changePasswordRequestDto);
        previousPassword.setPreviousPassword(oldEncryptedPassword);
        previousPassword.setPasswordChangeCount(maxPasswordCount + 1);
        previousPassword.setUser(user);

        user.setPassword(passwordEncoder.encode(newPassword));
        auditService.stampLongEntity(user);
        userRepository.save(user);

        auditService.stampLongEntity(previousPassword);
        previousPasswordRepository.save(previousPassword);

        return Boolean.TRUE;
    }

    @Transactional
    public UserResponseDto addUser(UserRequestDto userRequestDto) {

        userRequestDto.validate();
        Validate.notEmpty(this, userRequestDto.password(), PASSWORD_REQUIRED);
        StringRegExUtil.stringOfStandardPassword(this, userRequestDto.password(), STANDARD_PASSWORD);

        var existingUserUsername = userRepository.findByUsername(userRequestDto.username());
        Validate.isTrue(this, existingUserUsername.isEmpty(), ExceptionType.BAD_REQUEST,USERNAME_TAKEN, userRequestDto.username());

        var existingUserGroup = userGroupRepository.findById(userRequestDto.userGroupId());
        Validate.isPresent(this, existingUserGroup, USER_GROUP_DOES_NOT_EXIST, userRequestDto.userGroupId());
        final var userGroup = existingUserGroup.get();

        var user = userDtoService.dtoToTUser(userRequestDto);
        user.setAccountExpired(Boolean.FALSE);
        user.setAccountLocked(Boolean.FALSE);
        user.setApproved(Boolean.FALSE);
        user.setDeleted(Boolean.FALSE);
        user.setInitialPasswordReset(Boolean.FALSE);
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));
        user.setUserGroup(userGroup);
        user.setUserType(UserTypeEnum.valueOf(userRequestDto.userType()));

        auditService.stampLongEntity(user);
        var savedUser = userRepository.save(user);

        //initiate user approval
        var userApproval = new TUserApproval();
        userApproval.setUser(user);
        userApproval.setStatus(ApprovalEnum.PENDING);
        auditService.stampAuditedEntity(userApproval);

        userApprovalRepository.save(userApproval);

        return userDtoService.tUserToDto(savedUser);
    }

    @Transactional
    public SchoolUserResponseDto addSchoolUser(SchoolUserRequestDto schoolUserRequestDto){

        schoolUserRequestDto.validate();
        Validate.notEmpty(this, schoolUserRequestDto.password(), PASSWORD_REQUIRED);

        //Check if schoolUser is staff or driver
        var userType = UserTypeEnum.valueOf(schoolUserRequestDto.userType());
        if(userType.equals(UserTypeEnum.DRIVER) || userType.equals(UserTypeEnum.SCHOOL_STAFF))
            validateLoggedUserWithSchool(schoolUserRequestDto.schoolId());

        var existingUserGroup = userGroupRepository.findById(schoolUserRequestDto.userGroupId());
        Validate.isPresent(this, existingUserGroup, USER_GROUP_DOES_NOT_EXIST, schoolUserRequestDto.userGroupId());
        final var userGroup = existingUserGroup.get();

        var existingSchool = schoolRepository.findById(schoolUserRequestDto.schoolId());
        Validate.isPresent(this, existingSchool, SCHOOL_NOT_FOUND, schoolUserRequestDto.schoolId());
        var school= existingSchool.get();

        var user = userDtoService.dtoToTUser(mapSchoolUserRequestDtoToUserRequestDto(schoolUserRequestDto));
        user.setAccountExpired(Boolean.FALSE);
        user.setAccountLocked(Boolean.FALSE);
        user.setApproved(Boolean.FALSE);
        user.setDeleted(Boolean.FALSE);
        user.setInitialPasswordReset(Boolean.FALSE);
        user.setPassword(passwordEncoder.encode(schoolUserRequestDto.password()));
        user.setUserGroup(userGroup);
        user.setUserType(userType);

        auditService.stampLongEntity(user);

        var savedUser = userRepository.save(user);

        var schoolUser = new TSchoolUser();
        schoolUser.setSchool(school);
        schoolUser.setUser(savedUser);
        auditService.stampAuditedEntity(schoolUser);

        schoolUserRepository.save(schoolUser);

        //initiate user approval
        var userApproval = new TUserApproval();
        userApproval.setUser(user);
        userApproval.setStatus(ApprovalEnum.PENDING);
        auditService.stampAuditedEntity(userApproval);

        userApprovalRepository.save(userApproval);

        addUserMetaData(savedUser.getId(), schoolUserDtoToUserMetaRequestDto(schoolUserRequestDto));

        return schoolDtoService.tUserToUserSchoolDto(savedUser, school);
    }

    public SchoolUserResponseDto getSchoolUser(Long id){

        var existingUser = userRepository.findById(id);
        Validate.isPresent(this, existingUser, USER_DOES_NOT_EXIST, id);
        var user= existingUser.get();

        var existingSchoolUser = schoolUserRepository.findByUser(user);
        Validate.isPresent(this, existingSchoolUser, SCHOOL_USER_NOT_FOUND, id);
        var schoolUser = existingSchoolUser.get();

        var existingEventUser = userRepository.findById(auditService.getLoggedInUser().getId());
        var eventUser = existingEventUser.get();

        if(!eventUser.getUserType().equals(UserTypeEnum.ADMIN)){
            var existingEventSchoolUser = schoolUserRepository.findByUser(user);
            Validate.isPresent(this, existingEventSchoolUser, SCHOOL_USER_NOT_FOUND, id);
            var eventSchoolUser = existingSchoolUser.get();

            Validate.isTrue(this, Objects.equals(schoolUser.getSchool().getId(), eventSchoolUser.getSchool().getId()), ExceptionType.ACCESS_DENIED, USER_NOT_IN_SIMILAR_SCHOOL);
        }

        return schoolDtoService.tUserToUserSchoolDto(user, schoolUser.getSchool());
    }

    @Transactional
    public SchoolUserResponseDto updateSchoolUser(Long id, SchoolUserRequestDto schoolUserRequestDto){

        schoolUserRequestDto.validate();
        Validate.isTrue(this, schoolUserRequestDto.password() == null, ExceptionType.BAD_REQUEST, CANNOT_UPDATE_PASSWORD);

        var userType = UserTypeEnum.valueOf(schoolUserRequestDto.userType());
        if(userType.equals(UserTypeEnum.DRIVER) || userType.equals(UserTypeEnum.SCHOOL_STAFF))
            validateLoggedUserWithSchool(schoolUserRequestDto.schoolId());

        var existingUser = userRepository.findById(id);
        Validate.isPresent(this, existingUser, USER_DOES_NOT_EXIST, id);
        var user = existingUser.get();

        var existingSchoolUser = schoolUserRepository.findByUser(user);
        Validate.isPresent(this, existingSchoolUser, SCHOOL_USER_NOT_FOUND, user.getId());
        var schoolUser = existingSchoolUser.get();

        var newSchool = schoolRepository.findById(schoolUserRequestDto.schoolId());
        Validate.isPresent(this, newSchool, SCHOOL_NOT_FOUND, schoolUserRequestDto.schoolId());
        var existingSchool = newSchool.get();

        var existingUserGroup = userGroupRepository.findById(schoolUserRequestDto.userGroupId());
        Validate.isPresent(this, existingUserGroup, USER_GROUP_DOES_NOT_EXIST, schoolUserRequestDto.userGroupId());
        final var userGroup = existingUserGroup.get();

        var updatedUser = userDtoService.dtoToTUser(mapSchoolUserRequestDtoToUserRequestDto(schoolUserRequestDto));
        updatedUser.setId(user.getId());
        updatedUser.setAccountExpired(user.isAccountExpired());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setAccountLocked(user.isAccountLocked());
        updatedUser.setApproved(user.isApproved());
        updatedUser.setDeleted(user.getDeleted());
        updatedUser.setInitialPasswordReset(user.isInitialPasswordReset());
        updatedUser.setUserGroup(userGroup);
        updatedUser.setUserMetaId(user.getUserMetaId());
        updatedUser.setApprovedBy(user.getApprovedBy());
        updatedUser.setUserType(UserTypeEnum.valueOf(schoolUserRequestDto.userType()));

        auditService.stampLongEntity(updatedUser);

        var savedUser = userRepository.save(updatedUser);

        var updatedSchoolUser = new TSchoolUser();
        updatedSchoolUser.setId(schoolUser.getId());
        updatedSchoolUser.setSchool(existingSchool);
        updatedSchoolUser.setUser(savedUser);
        auditService.stampAuditedEntity(updatedSchoolUser);

        schoolUserRepository.save(updatedSchoolUser);

        updateUserMetaData(savedUser.getUserMetaId(), schoolUserDtoToUserMetaRequestDto(schoolUserRequestDto));

        userRepository.save(savedUser);

        return schoolDtoService.tUserToUserSchoolDto(savedUser, existingSchool);
    }


    public List<SchoolUserResponseDto> getSchoolUser(String userType, Long schoolId){

        EnumValidations.isSchoolUserType(userType);
        var userTypeEnum = UserTypeEnum.valueOf(userType);

        var existingSchool = schoolRepository.findById(schoolId);
        Validate.isPresent(this, existingSchool, SCHOOL_NOT_FOUND, schoolId);
        var school= existingSchool.get();

        var schoolUsersPage = schoolUserRepository.findAllBySchool(school);

        return schoolUsersPage.stream()
                .filter(schoolUser -> schoolUser.getUser().getUserType().equals(userTypeEnum))
                .map(schoolUser -> schoolDtoService.tUserToUserSchoolDto(schoolUser.getUser(), schoolUser.getSchool()))
                .toList();
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {

        userRequestDto.validate();
        Validate.isTrue(this, userRequestDto.password() == null, ExceptionType.BAD_REQUEST, CANNOT_UPDATE_PASSWORD);

        var existingUser = userRepository.findById(userId);
        Validate.isPresent(this, existingUser, String.format(USER_DOES_NOT_EXIST, userId));

        var existingUserGroup = userGroupRepository.findById(userRequestDto.userGroupId());
        Validate.isPresent(this, existingUserGroup, USER_GROUP_DOES_NOT_EXIST, userRequestDto.userGroupId());
        final var userGroup = existingUserGroup.get();

        var user = userDtoService.dtoToTUser(userRequestDto);
        user.setUserGroup(userGroup);

        return userDtoService.tUserToDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto updateByField(Long id, Map<String, Object> fields) {

        Validate.isTrue(this, !fields.containsKey(PASSWORD), ExceptionType.BAD_REQUEST, PASSWORD_CANT_BE_UPDATED);

        if(fields.containsKey(USERTYPE)){
            Validate.isTrue(this, isUserType(fields.get(USERTYPE).toString()), ExceptionType.BAD_REQUEST, VALID_USER_TYPE);
            UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(fields.get(USERTYPE).toString());
            fields.put(USERTYPE, userTypeEnum);
        }

        UserDto userDto = new UserDto();
        Validate.doesObjectContainFields(this, userDto, new ArrayList<>(fields.keySet()));

        var existingUser = userRepository.findById(id);
        Validate.isPresent(this, existingUser, String.format(USER_DOES_NOT_EXIST, id));
        final var user = existingUser.get();

        fields.forEach(
                (key, value) -> {
                    Field field = ReflectionUtils.findField(TUser.class, key);
                    assert field != null;
                    field.setAccessible(Boolean.TRUE);
                    ReflectionUtils.setField(field, user, value);
                }
        );
        auditService.stampLongEntity(user);
        userRepository.save(user);

        return userDtoService.tUserToDto(userRepository.save(user));
    }

    public UserResponseDto getUserById(Long id) {
        return userDtoService.tUserToDto(getUser(id));
    }

    public UserMetaResponseDto getUserByEmail(String email) {

        var existingUser = userMetaRepository.findByEmail(email);
        Validate.isPresent(this, existingUser, USER_WITH_EMAIL_DOES_NOT_EXIST, email);

        var userMeta = new TUserMeta();
        if(existingUser.isPresent())
            userMeta = existingUser.get();

        return userDtoService.userMetaToDto(userMeta);
    }

    public UserMetaResponseDto getUserByPhoneNumber(String phoneNumber) {
        var existingUser = userMetaRepository.findByPhoneNumber(phoneNumber);
        Validate.isPresent(this, existingUser, USER_WITH_PHONE_NUMBER_DOES_NOT_EXIST, phoneNumber);

        var userMeta = new TUserMeta();
        if(existingUser.isPresent())
            userMeta = existingUser.get();

        return userDtoService.userMetaToDto(userMeta);
    }

    public UserResponseDto reAssignUserToGroup(Long groupId, Long userId) {

        var existingUser = userRepository.findById(userId);
        Validate.isPresent(this, existingUser, USER_DOES_NOT_EXIST, userId);
        checkThatUserIsAssignable(existingUser.get());

        var existingUserGroup = userGroupRepository.findById(groupId);
        Validate.isPresent(this, existingUser, USER_GROUP_DOES_NOT_EXIST, groupId);
        final var userGroup = existingUserGroup.get();

        Validate.isTrue(this, groupId == userGroup.getId(), ExceptionType.BAD_REQUEST, String.format(USER_ALREADY_ASSIGNED_TO_USER_GROUP, groupId));

        existingUser.get().setUserGroup(existingUserGroup.get());
        auditService.stampLongEntity(existingUser.get());

        return userDtoService.tUserToDto(existingUser.get());

    }

    @Transactional
    public UserMetaResponseDto addUserMetaData(Long userId, UserMetaRequestDto userMetaRequestDto) {

        userMetaRequestDto.validate();

        var existingUser = userRepository.findById(userId);
        Validate.isPresent(this, existingUser, USER_DOES_NOT_EXIST, userId);
        final var user = existingUser.get();
        if(user.getUserType().equals(UserTypeEnum.ADMIN))
            checkThatUserIsAssignable(user);

        var existingCountry = countryRepository.findById(userMetaRequestDto.countryId());
        Validate.isPresent(this, existingCountry, COUNTRY_WITH_ID_NOT_FOUND, userMetaRequestDto.countryId());
        final var country = existingCountry.get();

        Validate.isTrue(this, !userMetaRepository.existsByEmail(userMetaRequestDto.email()), ExceptionType.BAD_REQUEST, EMAIL_ALREADY_TAKEN, userMetaRequestDto.email());
        //Validate.isTrue(this, !userMetaRepository.existsByPhoneNumber(userMetaRequestDto.phoneNumber()), ExceptionType.BAD_REQUEST, PHONE_NUMBER_ALREADY_TAKEN, userMetaRequestDto.phoneNumber());

        /*if(!userMetaRequestDto.phoneNumber2().isEmpty())
            Validate.isTrue(this, !userMetaRepository.existsByPhoneNumber2(userMetaRequestDto.phoneNumber2()), ExceptionType.BAD_REQUEST, SECOND_PHONE_NUMBER_ALREADY_TAKEN, userMetaRequestDto.phoneNumber2());
*/
        var userMeta = userDtoService.dtoToUserMeta(userMetaRequestDto);
        userMeta.setCountry(country);
        userMeta.setDisplayName(user.getUsername());
        userMeta.setNonVerifiedEmail(Boolean.FALSE);
        userMeta.setNonVerifiedPhoneNumber(Boolean.TRUE);
        auditService.stampAuditedEntity(userMeta);

        var result = userMetaRepository.save(userMeta);
        user.setUserMetaId(result.getId());
        auditService.stampLongEntity(user);

        if(checkThatUserIsSchoolUser(user)){
            var schoolUser = schoolUserRepository.findByUser(user);
            Validate.isPresent(this, schoolUser, NOT_ATTACHED_TO_SCHOOL, user.getId());
            var school = schoolUser.get().getSchool();

            if(user.getUserType().equals(UserTypeEnum.DRIVER))
                user.setCoreBankingId(memberService.addSchoolDriver(userMeta, school,user.getUsername()));

            if(user.getUserType().equals(UserTypeEnum.SCHOOL_STAFF) || user.getUserType().equals(UserTypeEnum.SCHOOL_ADMIN))
                user.setCoreBankingId(memberService.addSchoolStaff(userMeta, school,user.getUsername()));

        }
        userRepository.save(user);

        return userDtoService.userMetaToDto(result);
    }

    @Transactional
    public UserMetaResponseDto updateUserMetaData(Long id, UserMetaRequestDto userMetaRequestDto) {

        var existingMetaData = userMetaRepository.findById(id);
        Validate.isPresent(this, existingMetaData, USER_META_NOT_FOUND, id);
        var metaData = existingMetaData.get();

        var existingCountry = countryRepository.findById(userMetaRequestDto.countryId());
        Validate.isPresent(this, existingCountry, COUNTRY_WITH_ID_NOT_FOUND, userMetaRequestDto.countryId());
        final var country = existingCountry.get();

        if(!metaData.getEmail().equals(userMetaRequestDto.email()))
            Validate.isTrue(this, !userMetaRepository.existsByEmail(userMetaRequestDto.email()), ExceptionType.BAD_REQUEST, EMAIL_ALREADY_TAKEN, userMetaRequestDto.email());

        /*if(!metaData.getPhoneNumber().equals(userMetaRequestDto.phoneNumber()))
            Validate.isTrue(this, !userMetaRepository.existsByPhoneNumber(userMetaRequestDto.phoneNumber()), ExceptionType.BAD_REQUEST, PHONE_NUMBER_ALREADY_TAKEN, userMetaRequestDto.phoneNumber());

        if(!userMetaRequestDto.phoneNumber2().isEmpty() && !metaData.getPhoneNumber2().equals(userMetaRequestDto.phoneNumber2()))
            Validate.isTrue(this, !userMetaRepository.existsByPhoneNumber2(userMetaRequestDto.phoneNumber2()), ExceptionType.BAD_REQUEST, SECOND_PHONE_NUMBER_ALREADY_TAKEN, userMetaRequestDto.phoneNumber2());
*/
        var userMeta = userDtoService.dtoToUserMeta(userMetaRequestDto);
        userMeta.setId(id);
        userMeta.setCountry(country);
        userMeta.setDisplayName(metaData.getDisplayName());
        userMeta.setNonVerifiedEmail(Boolean.FALSE);
        userMeta.setNonVerifiedPhoneNumber(Boolean.TRUE);
        auditService.stampAuditedEntity(userMeta);

        return userDtoService.userMetaToDto(userMetaRepository.save(userMeta));
    }


    public UserMetaResponseDto getUserMetaData(Long id) {

        var existingUserMeta = userMetaRepository.findById(id);
        Validate.isPresent(this, existingUserMeta, USER_DOES_NOT_EXIST, id);

        var userMeta = new TUserMeta();
        if(existingUserMeta.isPresent())
            userMeta = existingUserMeta.get();

        return userDtoService.userMetaToDto(userMeta);
    }

    public Long getNumberOfUsersByType(String userType) {
        Validate.isTrue(this, isUserType(userType), ExceptionType.BAD_REQUEST, VALID_USER_TYPE);
        UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(userType);
        return userRepository.countByUserType(userTypeEnum);
    }

    public PageResponseDto getAll(Pageable pageable) {
        var usersPage = userRepository.findAll(pageable);
        var users = usersPage.stream()
                .map(userDtoService::tUserToDto)
                .toList();

        return pageToDto(usersPage, users);
    }

    @Transactional
    public UserResponseDto approveUser(UserApprovalRequestDto userApprovalRequestDto) {

        userApprovalRequestDto.validate();
        var existingApproval = userApprovalRepository.findById(userApprovalRequestDto.approvalId());
        Validate.isPresent(this, existingApproval, USER_APPROVAL_NOT_FOUND, userApprovalRequestDto.approvalId());
        final var userApproval = existingApproval.get();

        validateLoggedUserWithSchoolUser(userApproval.getUser());

        ApprovalEnum approvalEnum = ApprovalEnum.valueOf(userApprovalRequestDto.status());
        userApproval.setStatus(approvalEnum);
        auditService.stampAuditedEntity(userApproval);

        userApprovalRepository.save(userApproval);

        var existingUser = userRepository.findById(userApproval.getUser().getId());
        Validate.isPresent(this, existingUser, USER_DOES_NOT_EXIST, userApproval.getUser().getId());
        final var user = existingUser.get();

        if(userApproval.getStatus().equals(ApprovalEnum.APPROVED)) {
            user.setApproved(Boolean.TRUE);
            user.setAccountExpired(Boolean.FALSE);
            user.setAccountLocked(Boolean.FALSE);
            user.setDeleted(Boolean.FALSE);
            user.setApprovedBy(auditService.getLoggedInUser().getId());
        }

        auditService.stampLongEntity(user);

        var approvedUser = userRepository.save(user);

        return userDtoService.tUserToDto(approvedUser);
    }

    public boolean deleteUserAccount(Long userId) {

        var existingUser = userRepository.findById(userId);
        Validate.isPresent(this, existingUser, USER_DOES_NOT_EXIST, userId);
        final var user = existingUser.get();

        user.setAccountExpired(Boolean.TRUE);
        user.setAccountLocked(Boolean.TRUE);
        user.setDeleted(Boolean.TRUE);
        user.setApproved(Boolean.FALSE);

        auditService.stampLongEntity(user);
        userRepository.save(user);

        return Boolean.TRUE;
    }

    public boolean suspendUserAccount(Long userId) {

        var existingUser = userRepository.findById(userId);
        Validate.isPresent(this, existingUser, USER_DOES_NOT_EXIST, userId);
        final var user = existingUser.get();

        Validate.isTrue(this, !user.isAccountLocked(), ExceptionType.BAD_REQUEST, USER_ACCOUNT_IS_ALREADY_LOCKED);

        user.setAccountLocked(Boolean.TRUE);
        auditService.stampLongEntity(user);
        userRepository.save(user);

        return Boolean.TRUE;
    }

    public PageResponseDto getUserApprovals(String status, Pageable pageable) {

        Validate.isTrue(this, isApprovalStatus(status), ExceptionType.BAD_REQUEST, INVALID_APPROVAL_STATUS);
        var approvalEnum = ApprovalEnum.valueOf(status);

        var userApprovalPage = userApprovalRepository.findAllByStatus(approvalEnum, pageable);

        var userApprovals = userApprovalPage.stream()
                .map(approval -> {
                    SchoolResponseDto school = null;
                    if(approval.getUser().getUserType().equals(UserTypeEnum.DRIVER)
                            || approval.getUser().getUserType().equals(UserTypeEnum.SCHOOL_STAFF)
                            || approval.getUser().getUserType().equals(UserTypeEnum.SCHOOL_ADMIN)){
                        school = getUserSchool(approval.getUser());
                    }
                    return userDtoService.userApprovalToDto(approval, school);
                })
                .toList();

        return pageToDto(userApprovalPage, userApprovals);
    }

    public PageResponseDto getSchoolUserApprovals(Long schoolId, String status, PageRequest pageable) {

        var existingSchool = schoolRepository.findById(schoolId);
        Validate.isPresent(this, existingSchool, SCHOOL_NOT_FOUND, schoolId);
        var school = existingSchool.get();

        Validate.isTrue(this, isApprovalStatus(status), ExceptionType.BAD_REQUEST, INVALID_APPROVAL_STATUS);
        var approvalEnum = ApprovalEnum.valueOf(status);

        var userApprovalPage = userApprovalRepository.findAllByStatus(approvalEnum, pageable);

        var userApprovals = userApprovalPage.stream()
                .map(approval -> {
                    SchoolResponseDto tschool = null;
                    if(approval.getUser().getUserType().equals(UserTypeEnum.DRIVER)
                            || approval.getUser().getUserType().equals(UserTypeEnum.SCHOOL_STAFF)){
                        tschool = getUserSchool(approval.getUser());
                    }
                    return userDtoService.userApprovalToDto(approval, tschool);
                }).filter(approvalDto -> {
                    var approvalSchool = approvalDto.school();
                    return approvalSchool != null && (Objects.equals(approvalSchool.id(), school.getId()));
                })
                .toList();

        return pageToDto(userApprovalPage, userApprovals);
    }

    public List<PermissionResponseDto> getPermissions(){
        return getUserPermissionsById(auditService.getLoggedInUser().getId());
    }

    public List<PermissionResponseDto> getUserPermissionsById(Long id){

        var user = getUser(id);
        var groupAuthorities = groupAuthorityRepository.findByUserGroup(user.getUserGroup());

        return groupAuthorities
                .stream()
                .map(groupAuthority -> userDtoService.permissionToDto(groupAuthority.getPermission()))
                .toList();
    }

    private UserRequestDto mapSchoolUserRequestDtoToUserRequestDto(SchoolUserRequestDto schoolUserRequestDto){
        return new UserRequestDto(
                getNonExistingUserUsername(),
                schoolUserRequestDto.password(),
                schoolUserRequestDto.userGroupId(),
                schoolUserRequestDto.approvedBy(),
                schoolUserRequestDto.userType()
        );
    }

    private String getNonExistingUserUsername(){
        var username = "";
        do {
            username = StringUtil.getRandom6DigitString();
        }while(userRepository.existsByUsername(username));

        return username;
    }

    private UserMetaRequestDto schoolUserDtoToUserMetaRequestDto(SchoolUserRequestDto schoolUserRequestDto){

        return new UserMetaRequestDto(
                schoolUserRequestDto.firstName(),
                schoolUserRequestDto.lastName(),
                schoolUserRequestDto.middleName(),
                schoolUserRequestDto.phoneNumber(),
                schoolUserRequestDto.phoneNumber2(),
                schoolUserRequestDto.gender(),
                schoolUserRequestDto.birthDate(),
                schoolUserRequestDto.email(),
                schoolUserRequestDto.countryId(),
                schoolUserRequestDto.identificationType(),
                schoolUserRequestDto.identificationNumber()
        );
    }

    private SchoolResponseDto getUserSchool(TUser user){
        return schoolDtoService.schoolToDto(schoolUserRepository.findByUser(user).get().getSchool());
    }

    private TUser getUser(Long id){
        var existingUser = userRepository.findById(id);
        Validate.isPresent(this, existingUser, USER_DOES_NOT_EXIST, id);

        var user = new TUser();
        if(existingUser.isPresent())
            user = existingUser.get();

        return user;
    }

    private void validateLoggedUserWithSchoolUser(TUser user){

        var schoolUser = schoolUserRepository.findByUser(user);
        Validate.isPresent(this, schoolUser, NOT_ATTACHED_TO_SCHOOL, user.getId());
        var userSchool = schoolUser.get().getSchool();

        var loggedInUserId = auditService.getLoggedInUser().getId();
        var loggedInUser = new TUser();
        loggedInUser.setId(loggedInUserId);

        var loggedInSchoolUser = schoolUserRepository.findByUser(loggedInUser);
        if(loggedInSchoolUser.isPresent()){
            var loggedInUserSchool = loggedInSchoolUser.get().getSchool();
            Validate.isTrue(this, Objects.equals(userSchool.getId(), loggedInUserSchool.getId()), ExceptionType.BAD_REQUEST, USER_NOT_IN_SIMILAR_SCHOOL);
        }
    }

    private void validateLoggedUserWithSchool(Long schoolId){
        var loggedInUserId = auditService.getLoggedInUser().getId();
        var loggedInUser = new TUser();
        loggedInUser.setId(loggedInUserId);

        var loggedInSchoolUser = schoolUserRepository.findByUser(loggedInUser);
        if(loggedInSchoolUser.isPresent()){
            var loggedInUserSchool = loggedInSchoolUser.get().getSchool();
            Validate.isTrue(this, Objects.equals(schoolId, loggedInUserSchool.getId()), ExceptionType.BAD_REQUEST, USER_NOT_IN_SIMILAR_SCHOOL);
        }
    }

}
