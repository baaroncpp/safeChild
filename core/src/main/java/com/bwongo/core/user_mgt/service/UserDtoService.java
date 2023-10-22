package com.bwongo.core.user_mgt.service;

import com.bwongo.core.base.model.enums.GenderEnum;
import com.bwongo.core.base.model.enums.IdentificationType;
import com.bwongo.core.base.model.jpa.TCountry;
import com.bwongo.core.base.service.BaseDtoService;
import com.bwongo.core.school_mgt.model.dto.SchoolResponseDto;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.school_mgt.service.SchoolDtoService;
import com.bwongo.core.user_mgt.model.dto.*;
import com.bwongo.core.user_mgt.model.jpa.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
@Service
@RequiredArgsConstructor
public class UserDtoService {

    private final BaseDtoService baseDtoService;

    public UserGroupResponseDto tUserGroupToDto(TUserGroup userGroup){

        if(userGroup == null){
            return null;
        }

        return new UserGroupResponseDto(
                userGroup.getId(),
                userGroup.getCreatedOn(),
                userGroup.getModifiedOn(),
                userGroup.getName(),
                userGroup.getNote()
        );
    }

    public TUser dtoToTUser(UserRequestDto userRequestDto){

        if(userRequestDto == null){
            return null;
        }

        var userGroup = new TUserGroup();
        userGroup.setId(userRequestDto.userGroupId());

        var user = new TUser();
        user.setUsername(userRequestDto.username());
        user.setPassword(userRequestDto.password());
        user.setUserGroup(userGroup);

        return user;
    }

    public UserResponseDto tUserToDto(TUser user){

        if(user == null){
            return null;
        }

        return new UserResponseDto(
                user.getId(),
                user.getCreatedOn(),
                user.getModifiedOn(),
                user.getUsername(),
                user.isAccountLocked(),
                user.isAccountExpired(),
                user.isCredentialExpired(),
                user.isApproved(),
                tUserGroupToDto(user.getUserGroup()),
                user.getDeleted(),
                user.getApprovedBy(),
                user.getUserType(),
                user.getUserMetaId()
        );
    }

    public TUserMeta dtoToUserMeta(UserMetaRequestDto userMetaRequestDto){

        if(userMetaRequestDto == null){
            return null;
        }

        var country = new TCountry();
        country.setId(userMetaRequestDto.countryId());

        var userMeta = new TUserMeta();
        userMeta.setFirstName(userMetaRequestDto.firstName());
        userMeta.setLastName(userMetaRequestDto.lastName());
        userMeta.setMiddleName(userMeta.getMiddleName());
        userMeta.setPhoneNumber(userMetaRequestDto.phoneNumber());
        userMeta.setPhoneNumber2(userMetaRequestDto.phoneNumber2());
        userMeta.setGender(GenderEnum.valueOf(userMetaRequestDto.gender()));
        userMeta.setBirthDate(userMetaRequestDto.birthDate());
        userMeta.setEmail(userMetaRequestDto.email());
        userMeta.setCountry(country);
        userMeta.setIdentificationType(IdentificationType.valueOf(userMetaRequestDto.identificationType()));
        userMeta.setIdentificationNumber(userMetaRequestDto.identificationNumber());
        userMeta.setPin(userMetaRequestDto.pin());

        return userMeta;
    }

    public UserMetaResponseDto userMetaToDto(TUserMeta userMeta){

        if(userMeta == null){
            return null;
        }

        return new UserMetaResponseDto(
                userMeta.getId(),
                userMeta.getCreatedOn(),
                userMeta.getModifiedOn(),
                tUserToDto(userMeta.getCreatedBy()),
                tUserToDto(userMeta.getModifiedBy()),
                userMeta.getFirstName(),
                userMeta.getLastName(),
                userMeta.getMiddleName(),
                userMeta.getPhoneNumber(),
                userMeta.getPhoneNumber2(),
                userMeta.getImagePath(),
                userMeta.getDisplayName(),
                userMeta.getGender(),
                userMeta.getBirthDate(),
                userMeta.getEmail(),
                baseDtoService.countryToDto(userMeta.getCountry()),
                userMeta.getIdentificationType(),
                userMeta.getIdentificationNumber(),
                userMeta.getIdentificationPath(),
                userMeta.isNonVerifiedEmail(),
                userMeta.isNonVerifiedPhoneNumber()
        );
    }

    public UserApprovalResponseDto userApprovalToDto(TUserApproval userApproval, SchoolResponseDto school){

        if(userApproval == null){
            return null;
        }

        return new UserApprovalResponseDto(
                userApproval.getId(),
                userApproval.getCreatedOn(),
                userApproval.getModifiedOn(),
                tUserToDto(userApproval.getCreatedBy()),
                tUserToDto(userApproval.getModifiedBy()),
                tUserToDto(userApproval.getUser()),
                userApproval.getStatus(),
                school
        );
    }

    public RoleResponseDto roleToDto(TRole role){

        if(role == null){
            return null;
        }

        return new RoleResponseDto(
                role.getId(),
                role.getCreatedOn(),
                role.getModifiedOn(),
                role.getName(),
                role.getNote()
        );
    }

    public TRole dtoToTRole(RoleRequestDto roleRequestDto){

        if(roleRequestDto == null){
            return null;
        }

        var role = new TRole();
        role.setName(roleRequestDto.name());
        role.setNote(role.getNote());

        return role;
    }

    public PermissionResponseDto permissionToDto(TPermission permission){

        if(permission == null){
            return null;
        }

        return new PermissionResponseDto(
                permission.getId(),
                permission.getCreatedOn(),
                permission.getModifiedOn(),
                roleToDto(permission.getRole()),
                permission.getName(),
                permission.getAssignable()
        );
    }

    public GroupAuthorityResponseDto groupAuthorityToDto(TGroupAuthority groupAuthority){

        if(groupAuthority == null){
            return null;
        }

        return new GroupAuthorityResponseDto(
                groupAuthority.getId(),
                groupAuthority.getCreatedOn(),
                groupAuthority.getModifiedOn(),
                userGroupToDto(groupAuthority.getUserGroup()),
                permissionToDto(groupAuthority.getPermission())
        );
    }

    public TGroupAuthority dtoToGroupAuthority(GroupAuthorityRequestDto groupAuthorityRequestDto){

        if(groupAuthorityRequestDto == null){
            return null;
        }

        var permission = new TPermission();
        permission.setId(groupAuthorityRequestDto.permissionId());

        var userGroup = new TUserGroup();
        userGroup.setId(groupAuthorityRequestDto.userGroupId());

        var groupAuthority = new TGroupAuthority();
        groupAuthority.setPermission(permission);
        groupAuthority.setUserGroup(userGroup);

        return groupAuthority;
    }

    public UserGroupResponseDto userGroupToDto(TUserGroup userGroup){

        if(userGroup == null){
            return null;
        }

        return new UserGroupResponseDto(
                userGroup.getId(),
                userGroup.getCreatedOn(),
                userGroup.getModifiedOn(),
                userGroup.getName(),
                userGroup.getNote()
        );
    }

    public TUserGroup dtoToTUserGroup(UserGroupRequestDto userGroupRequestDto){

        if(userGroupRequestDto == null){
            return null;
        }

        var userGroup = new TUserGroup();
        userGroup.setName(userGroupRequestDto.name());
        userGroup.setNote(userGroupRequestDto.note());

        return userGroup;
    }
}
