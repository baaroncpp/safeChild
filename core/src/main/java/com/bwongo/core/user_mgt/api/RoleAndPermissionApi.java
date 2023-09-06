package com.bwongo.core.user_mgt.api;

import com.bwongo.core.user_mgt.model.dto.*;
import com.bwongo.core.user_mgt.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/16/23
 **/
@Tag(name = "Roles", description = "Manage user access permissions in system")
@RolesAllowed({"ROLE_ADMIN.WRITE", "ROLE_PERMISSION.WRITE"})
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class RoleAndPermissionApi {
    private final RoleService roleService;

    @RolesAllowed("ROLE_ADMIN.WRITE")
    @PostMapping(path = "role", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public RoleResponseDto addRole(@RequestBody RoleRequestDto role){
        return roleService.addRole(role);
    }

    @RolesAllowed("ROLE_ADMIN.READ")
    @GetMapping(path = "permission/{id}", produces = APPLICATION_JSON)
    public PermissionResponseDto getPermissionById(@PathVariable("id") Long id){
        return roleService.getPermissionById(id);
    }

    @RolesAllowed("ROLE_ADMIN.READ")
    @GetMapping(path = "permissions/{roleName}", produces = APPLICATION_JSON)
    public List<PermissionResponseDto> getPermissionByRoleName(@PathVariable("roleName") String roleName){
        return roleService.getAllPermissionsByRoleName(roleName);
    }

    @RolesAllowed("ROLE_ADMIN.UPDATE")
    @PutMapping(path = "permission/activate/{id}", produces = APPLICATION_JSON)
    public PermissionResponseDto activatePermission(@PathVariable("id") Long id){
        return roleService.activatePermission(id);
    }

    @RolesAllowed("ROLE_ADMIN.UPDATE")
    @PutMapping(path = "permission/de-activate/{id}", produces = APPLICATION_JSON)
    public PermissionResponseDto deactivatePermission(@PathVariable("id") Long id){
        return roleService.deactivatePermission(id);
    }

    @RolesAllowed("ROLE_ADMIN.READ")
    @GetMapping(path = "assignable/permissions", produces = APPLICATION_JSON)
    public List<PermissionResponseDto> getAllAssignablePermissions(){
        return roleService.getAllPermissionsAssignable();
    }

    @RolesAllowed("ROLE_ADMIN.READ")
    @GetMapping(path = "group/authorities", produces = APPLICATION_JSON)
    public List<GroupAuthorityResponseDto> getAllGroupAuthorities(){
        return roleService.getAllGroups();
    }

    @RolesAllowed("ROLE_ADMIN.READ")
    @GetMapping(path = "group/authorities/{userGroupId}", produces = APPLICATION_JSON)
    public List<GroupAuthorityResponseDto> getUserGroupAuthorities(@PathVariable("userGroupId") Long userGroupId){
        return roleService.getUserGroupAuthorities(userGroupId);
    }

    @RolesAllowed("ROLE_ADMIN.UPDATE")
    @PutMapping(path = "permission/assign/{permissionName}/{userGroupId}", produces = APPLICATION_JSON)
    public GroupAuthorityResponseDto assignPermissionToUserGroup(@PathVariable("permissionName") String permissionName,
                                                         @PathVariable("userGroupId") Long userGroupId){
        return roleService.assignPermissionToUserGroup(permissionName, userGroupId);
    }

    @RolesAllowed("ROLE_ADMIN.UPDATE")
    @PutMapping(path = "permission/un-assign/{permissionName}/{userGroupId}", produces = APPLICATION_JSON)
    public void unAssignPermissionFromUserGroup(@PathVariable("permissionName") String permissionName,
                                                @PathVariable("userGroupId") Long userGroupId){
        roleService.unAssignPermissionFromUserGroup(permissionName, userGroupId);
    }

    @RolesAllowed("ROLE_ADMIN.READ")
    @GetMapping(path = "groups", produces = APPLICATION_JSON)
    public List<UserGroupResponseDto> getAllUserGroups(){
        return roleService.getAllUserGroups();
    }

    @RolesAllowed("ROLE_ADMIN.WRITE")
    @PostMapping(path = "group")
    public UserGroupResponseDto addUserGroup(@RequestBody UserGroupRequestDto userGroupDto){
        return roleService.addUserGroup(userGroupDto);
    }
}
