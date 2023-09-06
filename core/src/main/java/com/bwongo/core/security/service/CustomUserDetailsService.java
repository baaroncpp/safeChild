package com.bwongo.core.security.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.security.models.LoginUser;
import com.bwongo.core.user_mgt.model.jpa.TGroupAuthority;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import com.bwongo.core.user_mgt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TUserRepository userRepository;
    private final TGroupAuthorityRepository groupAuthorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return mapUserDetails(userRepository.findByUsername(username).get()).orElseThrow(
                () -> new UsernameNotFoundException(String.format("Username %s not found", username))
        );
    }

    private Optional<LoginUser> mapUserDetails(TUser user){

        var userGroup = user.getUserGroup();
        Validate.notNull(userGroup, ExceptionType.BAD_CREDENTIALS,"User group is NULL");

        List<TGroupAuthority> groupAuthorities = groupAuthorityRepository.findByUserGroup(userGroup);
        Validate.notNull(groupAuthorities, ExceptionType.BAD_CREDENTIALS, String.format("user %s has no permissions, service access denied", user.getUsername()));

        Set<SimpleGrantedAuthority> permissions = groupAuthorities
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission().getName()))
                .collect(Collectors.toSet());

        var logInUser = new LoginUser();

        logInUser.setUsername(user.getUsername());
        logInUser.setPassword(user.getPassword());
        logInUser.setAccountNonExpired(!user.isAccountExpired());
        logInUser.setAccountNonLocked(!user.isAccountLocked());
        logInUser.setEnabled(user.isApproved());
        logInUser.setCredentialsNonExpired(!user.isCredentialExpired());
        logInUser.setId(user.getId());
        logInUser.setGrantedAuthorities(permissions);

        return Optional.of(logInUser);
    }
}
