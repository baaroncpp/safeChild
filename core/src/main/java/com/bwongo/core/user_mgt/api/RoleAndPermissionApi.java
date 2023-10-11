package com.bwongo.core.user_mgt.api;

import com.bwongo.core.user_mgt.model.dto.*;
import com.bwongo.core.user_mgt.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/16/23
 **/
@Tag(name = "Roles", description = "Manage user access permissions in SafeChild")
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class RoleAndPermissionApi {
    private final RoleService roleService;

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "roles", produces = APPLICATION_JSON)
    public List<RoleResponseDto> getAllRoles(){
        return roleService.getAllRoles();
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(path = "role", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public RoleResponseDto addRole(@RequestBody RoleRequestDto role){
        return roleService.addRole(role);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "permission/{id}", produces = APPLICATION_JSON)
    public PermissionResponseDto getPermissionById(@PathVariable("id") Long id){
        return roleService.getPermissionById(id);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "permissions/{roleName}", produces = APPLICATION_JSON)
    public List<PermissionResponseDto> getPermissionByRoleName(@PathVariable("roleName") String roleName){
        return roleService.getAllPermissionsByRoleName(roleName);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.UPDATE','ADMIN_ROLE.UPDATE')")
    @PutMapping(path = "permission/activate/{id}", produces = APPLICATION_JSON)
    public PermissionResponseDto activatePermission(@PathVariable("id") Long id){
        return roleService.activatePermission(id);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.UPDATE','ADMIN_ROLE.UPDATE')")
    @PutMapping(path = "permission/de-activate/{id}", produces = APPLICATION_JSON)
    public PermissionResponseDto deactivatePermission(@PathVariable("id") Long id){
        return roleService.deactivatePermission(id);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "assignable/permissions", produces = APPLICATION_JSON)
    public List<PermissionResponseDto> getAllAssignablePermissions(){
        return roleService.getAllPermissionsAssignable();
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "group/authorities", produces = APPLICATION_JSON)
    public List<GroupAuthorityResponseDto> getAllGroupAuthorities(){
        return roleService.getAllGroups();
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "group/authorities/{userGroupId}", produces = APPLICATION_JSON)
    public List<GroupAuthorityResponseDto> getUserGroupAuthorities(@PathVariable("userGroupId") Long userGroupId){
        return roleService.getUserGroupAuthorities(userGroupId);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.UPDATE','ADMIN_ROLE.UPDATE')")
    @PutMapping(path = "permission/assign-to-group", produces = APPLICATION_JSON)
    public GroupAuthorityResponseDto assignPermissionToUserGroup(@RequestParam("permissionName") String permissionName,
                                                                 @RequestParam("userGroupId") Long userGroupId){
        return roleService.assignPermissionToUserGroup(permissionName, userGroupId);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasAnyAuthority('USER_ROLE.UPDATE','ADMIN_ROLE.UPDATE')")
    @PutMapping(path = "permission/un-assign-to-group", produces = APPLICATION_JSON)
    public boolean unAssignPermissionFromUserGroup(@RequestParam("permissionName") String permissionName,
                                                   @RequestParam("userGroupId") Long userGroupId){
        return roleService.unAssignPermissionFromUserGroup(permissionName, userGroupId);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "groups", produces = APPLICATION_JSON)
    public List<UserGroupResponseDto> getAllUserGroups(){
        return roleService.getAllUserGroups();
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(path = "group")
    public UserGroupResponseDto addUserGroup(@RequestBody UserGroupRequestDto userGroupDto){
        return roleService.addUserGroup(userGroupDto);
    }
}
