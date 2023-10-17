package com.bwongo.core.user_mgt.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.enums.*;
import com.bwongo.core.base.service.AuditService;
import com.bwongo.core.base.utils.EnumValidations;
import com.bwongo.core.core_banking.service.MemberService;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.school_mgt.model.jpa.TSchoolUser;
import com.bwongo.core.school_mgt.repository.SchoolRepository;
import com.bwongo.core.school_mgt.repository.SchoolUserRepository;
import com.bwongo.core.school_mgt.service.SchoolDtoService;
import com.bwongo.core.user_mgt.model.dto.UserRequestDto;
import com.bwongo.core.user_mgt.model.dto.UserResponseDto;
import com.bwongo.core.user_mgt.model.dto.*;

import com.bwongo.core.user_mgt.model.jpa.TUser;
import com.bwongo.core.user_mgt.model.jpa.TUserApproval;
import com.bwongo.core.user_mgt.model.jpa.TUserMeta;
import com.bwongo.core.user_mgt.repository.*;
import com.bwongo.core.base.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

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

    @Transactional
    public UserResponseDto addUser(UserRequestDto userRequestDto) {

        userRequestDto.validate();
        var existingUserUsername = userRepository.findByUsername(userRequestDto.username());
        Validate.isTrue(existingUserUsername.isEmpty(), ExceptionType.BAD_REQUEST,USERNAME_TAKEN, userRequestDto.username());

        var existingUserGroup = userGroupRepository.findById(userRequestDto.userGroupId());
        Validate.isPresent(existingUserGroup, USER_GROUP_DOES_NOT_EXIST, userRequestDto.userGroupId());
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

        var existingUserGroup = userGroupRepository.findById(schoolUserRequestDto.userGroupId());
        Validate.isPresent(existingUserGroup, USER_GROUP_DOES_NOT_EXIST, schoolUserRequestDto.userGroupId());
        final var userGroup = existingUserGroup.get();

        var existingSchool = schoolRepository.findById(schoolUserRequestDto.schoolId());
        Validate.isPresent(existingSchool, SCHOOL_NOT_FOUND, schoolUserRequestDto.schoolId());
        var school= existingSchool.get();

        var user = userDtoService.dtoToTUser(mapSchoolUserRequestDtoToUserRequestDto(schoolUserRequestDto));
        user.setAccountExpired(Boolean.FALSE);
        user.setAccountLocked(Boolean.FALSE);
        user.setApproved(Boolean.FALSE);
        user.setDeleted(Boolean.FALSE);
        user.setInitialPasswordReset(Boolean.FALSE);
        user.setPassword(passwordEncoder.encode(schoolUserRequestDto.password()));
        user.setUserGroup(userGroup);
        user.setUserType(UserTypeEnum.valueOf(schoolUserRequestDto.userType()));

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

    @Transactional
    public SchoolUserResponseDto updateSchoolUser(Long id, SchoolUserRequestDto schoolUserRequestDto){

        schoolUserRequestDto.validate();

        var existingUser = userRepository.findById(id);
        Validate.isPresent(existingUser, USER_DOES_NOT_EXIST, id);
        var user = existingUser.get();

        var existingSchoolUser = schoolUserRepository.findByUser(user);
        Validate.isPresent(existingSchoolUser, SCHOOL_USER_NOT_FOUND, user.getId());
        var schoolUser = existingSchoolUser.get();

        var newSchool = schoolRepository.findById(schoolUserRequestDto.schoolId());
        Validate.isPresent(newSchool, SCHOOL_NOT_FOUND, schoolUserRequestDto.schoolId());
        var existingSchool = newSchool.get();

        var existingUserGroup = userGroupRepository.findById(schoolUserRequestDto.userGroupId());
        Validate.isPresent(existingUserGroup, USER_GROUP_DOES_NOT_EXIST, schoolUserRequestDto.userGroupId());
        final var userGroup = existingUserGroup.get();

        var updatedUser = userDtoService.dtoToTUser(mapSchoolUserRequestDtoToUserRequestDto(schoolUserRequestDto));
        updatedUser.setId(user.getId());
        updatedUser.setAccountExpired(user.isAccountExpired());
        updatedUser.setAccountLocked(user.isAccountLocked());
        updatedUser.setApproved(user.isApproved());
        updatedUser.setDeleted(user.getDeleted());
        updatedUser.setInitialPasswordReset(user.isInitialPasswordReset());
        updatedUser.setPassword(passwordEncoder.encode(schoolUserRequestDto.password()));
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

        return schoolDtoService.tUserToUserSchoolDto(savedUser, existingSchool);
    }


    public List<SchoolUserResponseDto> getSchoolUser(String userType, Long schoolId){

        EnumValidations.isSchoolUserType(userType);
        var userTypeEnum = UserTypeEnum.valueOf(userType);

        var existingSchool = schoolRepository.findById(schoolId);
        Validate.isPresent(existingSchool, SCHOOL_NOT_FOUND, schoolId);
        var school= existingSchool.get();

        var schoolUsers = schoolUserRepository.findAllBySchool(school);

        return schoolUsers.stream()
                .filter(schoolUser -> schoolUser.getUser().getUserType().equals(userTypeEnum))
                .map(schoolUser -> schoolDtoService.tUserToUserSchoolDto(schoolUser.getUser(), schoolUser.getSchool()))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {

        userRequestDto.validate();

        var existingUser = userRepository.findById(userId);
        Validate.isPresent(existingUser, String.format(USER_DOES_NOT_EXIST, userId));

        var existingUserGroup = userGroupRepository.findById(userRequestDto.userGroupId());
        Validate.isPresent(existingUserGroup, USER_GROUP_DOES_NOT_EXIST, userRequestDto.userGroupId());
        final var userGroup = existingUserGroup.get();

        var user = userDtoService.dtoToTUser(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));
        user.setUserGroup(userGroup);

        return userDtoService.tUserToDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto updateByField(Long id, Map<String, Object> fields) {

        Validate.isTrue(!fields.containsKey(PASSWORD), ExceptionType.BAD_REQUEST, PASSWORD_CANT_BE_UPDATED);

        if(fields.containsKey(USERTYPE)){
            Validate.isTrue(isUserType(fields.get(USERTYPE).toString()), ExceptionType.BAD_REQUEST, VALID_USER_TYPE);
            UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(fields.get(USERTYPE).toString());
            fields.put(USERTYPE, userTypeEnum);
        }

        UserDto userDto = new UserDto();
        Validate.doesObjectContainFields(userDto, new ArrayList<>(fields.keySet()));

        var existingUser = userRepository.findById(id);
        Validate.isPresent(existingUser, String.format(USER_DOES_NOT_EXIST, id));
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

        return userDtoService.tUserToDto(userRepository.save(user));
    }

    public UserResponseDto getUserById(Long id) {
        var existingUser = userRepository.findById(id);
        Validate.isPresent(existingUser, USER_DOES_NOT_EXIST, id);

        var user = new TUser();
        if(existingUser.isPresent())
            user = existingUser.get();

        return userDtoService.tUserToDto(user);
    }

    public UserMetaResponseDto getUserByEmail(String email) {

        var existingUser = userMetaRepository.findByEmail(email);
        Validate.isPresent(existingUser, USER_WITH_EMAIL_DOES_NOT_EXIST, email);

        var userMeta = new TUserMeta();
        if(existingUser.isPresent())
            userMeta = existingUser.get();

        return userDtoService.userMetaToDto(userMeta);
    }

    public UserMetaResponseDto getUserByPhoneNumber(String phoneNumber) {
        var existingUser = userMetaRepository.findByPhoneNumber(phoneNumber);
        Validate.isPresent(existingUser, USER_WITH_PHONE_NUMBER_DOES_NOT_EXIST, phoneNumber);

        var userMeta = new TUserMeta();
        if(existingUser.isPresent())
            userMeta = existingUser.get();

        return userDtoService.userMetaToDto(userMeta);
    }

    public UserResponseDto reAssignUserToGroup(Long groupId, Long userId) {

        var existingUser = userRepository.findById(userId);
        Validate.isPresent(existingUser, USER_DOES_NOT_EXIST, userId);
        checkThatUserIsAssignable(existingUser.get());

        var existingUserGroup = userGroupRepository.findById(groupId);
        Validate.isPresent(existingUser, USER_GROUP_DOES_NOT_EXIST, groupId);
        final var userGroup = existingUserGroup.get();

        Validate.isTrue(groupId == userGroup.getId(), ExceptionType.BAD_REQUEST, String.format(USER_ALREADY_ASSIGNED_TO_USER_GROUP, groupId));

        existingUser.get().setUserGroup(existingUserGroup.get());
        auditService.stampLongEntity(existingUser.get());

        return userDtoService.tUserToDto(existingUser.get());

    }

    @Transactional
    public UserMetaResponseDto addUserMetaData(Long userId, UserMetaRequestDto userMetaRequestDto) {

        userMetaRequestDto.validate();

        var existingUser = userRepository.findById(userId);
        Validate.isPresent(existingUser, USER_DOES_NOT_EXIST, userId);
        final var user = existingUser.get();
        if(user.getUserType().equals(UserTypeEnum.ADMIN))
            checkThatUserIsAssignable(user);

        var existingCountry = countryRepository.findById(userMetaRequestDto.countryId());
        Validate.isPresent(existingCountry, COUNTRY_WITH_ID_NOT_FOUND, userMetaRequestDto.countryId());
        final var country = existingCountry.get();

        Validate.isTrue(!userMetaRepository.existsByEmail(userMetaRequestDto.email()), ExceptionType.BAD_REQUEST, EMAIL_ALREADY_TAKEN, userMetaRequestDto.email());
        Validate.isTrue(!userMetaRepository.existsByPhoneNumber(userMetaRequestDto.phoneNumber()), ExceptionType.BAD_REQUEST, PHONE_NUMBER_ALREADY_TAKEN, userMetaRequestDto.phoneNumber());
        Validate.isTrue(!userMetaRepository.existsByPhoneNumber2(userMetaRequestDto.phoneNumber2()), ExceptionType.BAD_REQUEST, SECOND_PHONE_NUMBER_ALREADY_TAKEN, userMetaRequestDto.phoneNumber2());

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
            Validate.isPresent(schoolUser, NOT_ATTACHED_TO_SCHOOL, user.getId());
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
        Validate.isPresent(existingMetaData, USER_META_NOT_FOUND, id);
        var metaData = existingMetaData.get();

        var existingCountry = countryRepository.findById(userMetaRequestDto.countryId());
        Validate.isPresent(existingCountry, COUNTRY_WITH_ID_NOT_FOUND, userMetaRequestDto.countryId());
        final var country = existingCountry.get();

        if(!metaData.getEmail().equals(userMetaRequestDto.email()))
            Validate.isTrue(!userMetaRepository.existsByEmail(userMetaRequestDto.email()), ExceptionType.BAD_REQUEST, EMAIL_ALREADY_TAKEN, userMetaRequestDto.email());

        if(!metaData.getPhoneNumber().equals(userMetaRequestDto.phoneNumber()))
            Validate.isTrue(!userMetaRepository.existsByPhoneNumber(userMetaRequestDto.phoneNumber()), ExceptionType.BAD_REQUEST, PHONE_NUMBER_ALREADY_TAKEN, userMetaRequestDto.phoneNumber());

        if(!metaData.getPhoneNumber2().equals(userMetaRequestDto.phoneNumber2()))
            Validate.isTrue(!userMetaRepository.existsByPhoneNumber2(userMetaRequestDto.phoneNumber2()), ExceptionType.BAD_REQUEST, SECOND_PHONE_NUMBER_ALREADY_TAKEN, userMetaRequestDto.phoneNumber2());

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
        Validate.isPresent(existingUserMeta, USER_DOES_NOT_EXIST, id);

        var userMeta = new TUserMeta();
        if(existingUserMeta.isPresent())
            userMeta = existingUserMeta.get();

        return userDtoService.userMetaToDto(userMeta);
    }

    public Long getNumberOfUsersByType(String userType) {
        Validate.isTrue(isUserType(userType), ExceptionType.BAD_REQUEST, VALID_USER_TYPE);
        UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(userType);
        return userRepository.countByUserType(userTypeEnum);
    }

    public List<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).stream()
                .map(userDtoService::tUserToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto approveUser(UserApprovalRequestDto userApprovalRequestDto) {

        userApprovalRequestDto.validate();
        var existingApproval = userApprovalRepository.findById(userApprovalRequestDto.id());
        Validate.isPresent(existingApproval, USER_APPROVAL_NOT_FOUND, userApprovalRequestDto.id());
        final var userApproval = existingApproval.get();

        ApprovalEnum approvalEnum = ApprovalEnum.valueOf(userApprovalRequestDto.status());
        userApproval.setStatus(approvalEnum);
        auditService.stampAuditedEntity(userApproval);

        userApprovalRepository.save(userApproval);

        var existingUser = userRepository.findById(userApproval.getUser().getId());
        Validate.isPresent(existingUser, USER_DOES_NOT_EXIST, userApproval.getUser().getId());
        final var user = existingUser.get();

        if(userApproval.getStatus().equals(ApprovalEnum.APPROVED)) {
            user.setApproved(Boolean.TRUE);
            user.setAccountExpired(Boolean.FALSE);
            user.setAccountLocked(Boolean.FALSE);
            user.setDeleted(Boolean.FALSE);
        }
        auditService.stampLongEntity(user);
        var approvedUser = userRepository.save(user);

        return userDtoService.tUserToDto(approvedUser);
    }

    public boolean deleteUserAccount(Long userId) {

        var existingUser = userRepository.findById(userId);
        Validate.isPresent(existingUser, USER_DOES_NOT_EXIST, userId);
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
        Validate.isPresent(existingUser, USER_DOES_NOT_EXIST, userId);
        final var user = existingUser.get();

        Validate.isTrue(!user.isAccountLocked(), ExceptionType.BAD_REQUEST, USER_ACCOUNT_IS_ALREADY_LOCKED);

        user.setAccountLocked(Boolean.TRUE);
        auditService.stampLongEntity(user);
        userRepository.save(user);

        return Boolean.TRUE;
    }

    public List<UserApprovalResponseDto> getUserApprovals(String status, Pageable pageable) {

        Validate.isTrue(isApprovalStatus(status), ExceptionType.BAD_REQUEST, INVALID_APPROVAL_STATUS);
        var approvalEnum = ApprovalEnum.valueOf(status);

        return userApprovalRepository.findAllByStatus(approvalEnum, pageable).stream()
                .map(userDtoService::userApprovalToDto)
                .collect(Collectors.toList());
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
                schoolUserRequestDto.identificationNumber(),
                schoolUserRequestDto.pin()
        );

    }
}
