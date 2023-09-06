package com.bwongo.core.user_mgt.service;

import com.bwongo.core.base.model.enums.GenderEnum;
import com.bwongo.core.base.model.enums.IdentificationType;
import com.bwongo.core.base.model.jpa.TCountry;
import com.bwongo.core.base.service.BaseDtoService;
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

        return new UserGroupResponseDto(
                userGroup.getId(),
                userGroup.getCreatedOn(),
                userGroup.getModifiedOn(),
                userGroup.getName(),
                userGroup.getNote()
        );
    }

    public TUser dtoToTUser(UserRequestDto userRequestDto){

        var userGroup = new TUserGroup();
        userGroup.setId(userRequestDto.userGroupId());

        var user = new TUser();
        user.setUsername(userRequestDto.username());
        user.setPassword(userRequestDto.password());
        user.setUserGroup(userGroup);

        return user;
    }

    public UserResponseDto tUserToDto(TUser user){

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

        return userMeta;
    }

    public UserMetaResponseDto userMetaToDto(TUserMeta userMeta){

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

    public UserApprovalResponseDto userApprovalToDto(TUserApproval userApproval){

        return new UserApprovalResponseDto(
                userApproval.getId(),
                userApproval.getCreatedOn(),
                userApproval.getModifiedOn(),
                tUserToDto(userApproval.getCreatedBy()),
                tUserToDto(userApproval.getModifiedBy()),
                userApproval.getUserId(),
                userApproval.getStatus()
        );
    }

    public RoleResponseDto roleToDto(TRole role){
        return new RoleResponseDto(
                role.getId(),
                role.getCreatedOn(),
                role.getModifiedOn(),
                role.getName(),
                role.getNote()
        );
    }

    public TRole dtoToTRole(RoleRequestDto roleRequestDto){

        var role = new TRole();
        role.setName(roleRequestDto.name());
        role.setNote(role.getNote());

        return role;
    }

    public PermissionResponseDto permissionToDto(TPermission permission){

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
        return new GroupAuthorityResponseDto(
                groupAuthority.getId(),
                groupAuthority.getCreatedOn(),
                groupAuthority.getModifiedOn(),
                userGroupToDto(groupAuthority.getUserGroup()),
                permissionToDto(groupAuthority.getPermission())
        );
    }

    public TGroupAuthority dtoToGroupAuthority(GroupAuthorityRequestDto groupAuthorityRequestDto){
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
        return new UserGroupResponseDto(
                userGroup.getId(),
                userGroup.getCreatedOn(),
                userGroup.getModifiedOn(),
                userGroup.getName(),
                userGroup.getNote()
        );
    }

    public TUserGroup dtoToTUserGroup(UserGroupRequestDto userGroupRequestDto){
        var userGroup = new TUserGroup();
        userGroup.setName(userGroupRequestDto.name());
        userGroup.setNote(userGroupRequestDto.note());

        return userGroup;
    }
}
