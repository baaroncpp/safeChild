package com.bwongo.core.user_mgt.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.service.AuditService;
import com.bwongo.core.user_mgt.model.dto.RoleRequestDto;
import com.bwongo.core.user_mgt.model.dto.*;
import com.bwongo.core.user_mgt.model.jpa.TGroupAuthority;
import com.bwongo.core.user_mgt.repository.TGroupAuthorityRepository;
import com.bwongo.core.user_mgt.repository.TRoleRepository;
import com.bwongo.core.user_mgt.repository.TUserGroupRepository;
import com.bwongo.core.user_mgt.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.bwongo.core.base.utils.EnumValidations.isRolePermissions;
import static com.bwongo.core.user_mgt.utils.UserManagementUtils.checkThatPermissionRoleIsAssignable;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/16/23
 **/
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RoleService {
    private final TGroupAuthorityRepository groupAuthorityRepository;
    private final TUserRepository userRepository;
    private final TRoleRepository roleRepository;
    private final AuditService auditService;
    private final TUserGroupRepository userGroupRepository;
    private final TPermissionRepository permissionRepository;
    private final UserDtoService userDtoService;
    private static final String USER_GROUP_SUFFIX = "_GROUP";


    @Transactional
    public RoleResponseDto addRole(RoleRequestDto roleRequestDto) {

        roleRequestDto.validate();

        var existing = roleRepository.findByName(roleRequestDto.name());
        Validate.isTrue(this, existing.isEmpty(), ExceptionType.BAD_REQUEST, ROLE_EXISTS, roleRequestDto.name());

        var role = userDtoService.dtoToTRole(roleRequestDto);
        auditService.stampLongEntity(role);

        var result = roleRepository.save(role);

        var permissions = isRolePermissions(result);
        permissions.forEach(
                auditService::stampLongEntity
        );

        permissionRepository.saveAll(permissions);

        return userDtoService.roleToDto(result);
    }

    public RoleResponseDto updateRole(Long roleId, RoleRequestDto roleRequestDto) {

        roleRequestDto.validate();
        var existing = roleRepository.findById(roleId);
        Validate.isPresent(this, existing, ROLE_DOES_NOT_EXIST, roleId);

        var updatedRole = userDtoService.dtoToTRole(roleRequestDto);
        auditService.stampLongEntity(updatedRole);

        return userDtoService.roleToDto(roleRepository.save(updatedRole));
    }

    public List<RoleResponseDto> getAllRoles(){
        return roleRepository.findAll().stream()
                .map(userDtoService::roleToDto)
                .collect(Collectors.toList());
    }

    public PermissionResponseDto getPermissionById(Long id) {
        var result = permissionRepository.findById(id);
        Validate.isPresent(this, result, PERMISSION_DOES_NOT_EXIST);
        return userDtoService.permissionToDto(result.get());
    }

    public PermissionResponseDto deactivatePermission(Long id) {
        var existing = permissionRepository.findById(id);
        Validate.isPresent(this, existing, PERMISSION_DOES_NOT_EXIST, id);
        Validate.isTrue(this, existing.get().getAssignable(), ExceptionType.BAD_REQUEST, PERMISSION_IS_ALREADY_IN_ACTIVE, id);

        var permission = existing.get();
        permission.setAssignable(Boolean.FALSE);
        var result = permissionRepository.save(permission);

        return userDtoService.permissionToDto(result);
    }

    public PermissionResponseDto activatePermission(Long id) {

        var existing = permissionRepository.findById(id);
        Validate.isPresent(this, existing, PERMISSION_DOES_NOT_EXIST, id);
        Validate.isTrue(this, !existing.get().getAssignable(), ExceptionType.BAD_REQUEST, PERMISSION_IS_ALREADY_ACTIVE, id);

        var permission = existing.get();
        permission.setAssignable(Boolean.TRUE);
        var result = permissionRepository.save(permission);

        return userDtoService.permissionToDto(result);
    }

    public void deletePermission(Long id) {
        var existing = permissionRepository.findById(id);
        Validate.isPresent(this, existing, PERMISSION_DOES_NOT_EXIST, id);

    }

    public List<PermissionResponseDto> getAllPermissionsByRoleName(String roleName) {

        var existingRole = roleRepository.findByName(roleName);
        Validate.isPresent(this, existingRole, ROLE_NAME_DOES_NOT_EXIST, roleName);

        return permissionRepository.findAllByRole(existingRole.get()).stream()
                .map(userDtoService::permissionToDto)
                .collect(Collectors.toList());
    }

    public List<PermissionResponseDto> getAllPermissionsAssignable() {
        return permissionRepository.findByAssignableEquals(Boolean.TRUE).stream()
                .map(userDtoService::permissionToDto)
                .collect(Collectors.toList());
    }

    public boolean unAssignPermissionFromUserGroup(String permissionName, Long userGroupId) {

        var existingUserGroup = userGroupRepository.findById(userGroupId);
        Validate.isPresent(this, existingUserGroup, USER_GROUP_DOES_NOT_EXIST, userGroupId);

        var existingPermission = permissionRepository.findByName(permissionName);
        Validate.isPresent(this, existingPermission, PERMISSION_NAME_DOES_NOT_EXIST, permissionName);

        var existingGroupAuthority = groupAuthorityRepository.findByUserGroupAndPermission(existingUserGroup.get(), existingPermission.get());
        Validate.isTrue(this, existingGroupAuthority.isPresent(), ExceptionType.BAD_REQUEST, PERMISSION_NOT_ASSIGNED_TO_USER_GROUP, permissionName, existingUserGroup.get().getName());

        groupAuthorityRepository.delete(existingGroupAuthority.get());
        return Boolean.TRUE;
    }

    public GroupAuthorityResponseDto assignPermissionToUserGroup(String permissionName, Long userGroupId) {

        var existingUserGroup = userGroupRepository.findById(userGroupId);
        Validate.isPresent(this, existingUserGroup, USER_GROUP_DOES_NOT_EXIST, userGroupId);

        var existingPermission = permissionRepository.findByName(permissionName);
        Validate.isPresent(this, existingPermission, PERMISSION_NAME_DOES_NOT_EXIST, permissionName);
        checkThatPermissionRoleIsAssignable(existingPermission.get());

        var existingGroupAuthority = groupAuthorityRepository.findByUserGroupAndPermission(existingUserGroup.get(), existingPermission.get());
        Validate.isTrue(this, existingGroupAuthority.isEmpty(), ExceptionType.BAD_REQUEST, PERMISSION_ALREADY_ASSIGNED_TO_USER_GROUP, permissionName, existingUserGroup.get().getName());

        var groupAuthority = new TGroupAuthority();
        groupAuthority.setUserGroup(existingUserGroup.get());
        groupAuthority.setPermission(existingPermission.get());

        auditService.stampLongEntity(groupAuthority);

        return userDtoService.groupAuthorityToDto(groupAuthorityRepository.save(groupAuthority));
    }

    public List<GroupAuthorityResponseDto> getUserGroupAuthorities(Long userGroupId) {

        var existingUserGroup = userGroupRepository.findById(userGroupId);
        Validate.isPresent(this, existingUserGroup, USER_GROUP_DOES_NOT_EXIST, userGroupId);

        return groupAuthorityRepository.findByUserGroup(existingUserGroup.get()).stream()
                .map(userDtoService::groupAuthorityToDto)
                .collect(Collectors.toList());
    }

    public List<GroupAuthorityResponseDto> getAllGroups() {
        return groupAuthorityRepository.findAll().stream()
                .map(userDtoService::groupAuthorityToDto)
                .collect(Collectors.toList());
    }

    public List<UserGroupResponseDto> getAllUserGroups() {
        return userGroupRepository.findAll().stream()
                .map(userDtoService::tUserGroupToDto)
                .collect(Collectors.toList());
    }

    public UserGroupResponseDto addUserGroup(UserGroupRequestDto userGroupDto) {

        userGroupDto.validate();
        var existingUserGroup = userGroupRepository.findTUserGroupByName(userGroupDto.name()+USER_GROUP_SUFFIX);
        Validate.isTrue(this, existingUserGroup.isEmpty(), ExceptionType.BAD_REQUEST, USER_GROUP_ALREADY_EXISTS, userGroupDto.name()+USER_GROUP_SUFFIX);

        var userGroup = userDtoService.dtoToTUserGroup(userGroupDto);
        userGroup.setName(userGroupDto.name()+USER_GROUP_SUFFIX);

        auditService.stampLongEntity(userGroup);

        return userDtoService.userGroupToDto(userGroupRepository.save(userGroup));
    }
}
