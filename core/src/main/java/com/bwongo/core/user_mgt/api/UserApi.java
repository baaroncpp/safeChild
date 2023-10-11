package com.bwongo.core.user_mgt.api;

import com.bwongo.core.user_mgt.model.dto.*;
import com.bwongo.core.user_mgt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/15/23
 **/
@Tag(name = "Users",description = "Manage users on SafeChild")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto get(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "meta-data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserMetaResponseDto getMetaData(@PathVariable Long id){
        return userService.getUserMetaData(id);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserMetaResponseDto getByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "phone-number/{phoneNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserMetaResponseDto getByPhoneNumber(@PathVariable String phoneNumber){
        return userService.getUserByPhoneNumber(phoneNumber);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto add(@RequestBody UserRequestDto userDto) {
        return userService.addUser(userDto);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(path = "school",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public SchoolUserResponseDto addSchoolUser(@RequestBody SchoolUserRequestDto userDto) {
        return userService.addSchoolUser(userDto);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(path = "meta-data/user-id/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserMetaResponseDto addUserMeta(@RequestBody UserMetaRequestDto userMetaDto,
                                           @PathVariable("id") Long userId) {
        return userService.addUserMetaData(userId, userMetaDto);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.UPDATE','ADMIN_ROLE.UPDATE')")
    @PutMapping(path = "user-id/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto update(@RequestBody UserRequestDto userRequestDto,
                                  @PathVariable("id") Long id) {
        return userService.updateUser(id, userRequestDto);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.UPDATE','ADMIN_ROLE.UPDATE')")
    @PatchMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto update(@PathVariable("id") Long id,
                          @RequestBody Map<String, Object> fields) {
        return userService.updateByField(id, fields);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.DELETE','ADMIN_ROLE.DELETE')")
    @DeleteMapping(path="/delete", produces = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean delete(Long id) {
        return userService.deleteUserAccount(id);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path="pageable", produces = APPLICATION_JSON)
    public List<UserResponseDto> getAllUsers(@RequestParam(name = "page", required = true) int page,
                                     @RequestParam(name = "size", required = true) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return userService.getAll(pageable);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path="/count/{user_type}", produces = APPLICATION_JSON)
    public Long getNumberOfUsersByType(@PathVariable("user_type") String userType) {
        return userService.getNumberOfUsersByType(userType);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.UPDATE','ADMIN_ROLE.UPDATE')")
    @PatchMapping(path = "re_assign/group", produces = APPLICATION_JSON)
    public UserResponseDto reAssignUserToGroup(@RequestParam(name = "groupId", required = true) Long groupId,
                                       @RequestParam(name = "userId", required = true) Long userId){
        return userService.reAssignUserToGroup(groupId, userId);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(path = "approve", produces = APPLICATION_JSON, consumes = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponseDto approveUser(@RequestBody UserApprovalRequestDto userApprovalDto){
        return userService.approveUser(userApprovalDto);
    }

    @PreAuthorize("hasAnyAuthority('USER_ROLE.UPDATE','ADMIN_ROLE.UPDATE')")
    @PatchMapping(path = "suspend/{id}")
    public boolean suspendUserAccount(@PathVariable("id") Long id){
        return userService.suspendUserAccount(id);
    }

    @GetMapping(path = "approvals", produces = APPLICATION_JSON)
    @PreAuthorize("hasAnyAuthority('USER_ROLE.READ','ADMIN_ROLE.READ')")
    public List<UserApprovalResponseDto> getUserApprovals(@RequestParam(name = "page", required = true) int page,
                                                          @RequestParam(name = "size", required = true) int size,
                                                          @RequestParam(name = "status", required = true) String status){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return userService.getUserApprovals(status, pageable);
    }

}
