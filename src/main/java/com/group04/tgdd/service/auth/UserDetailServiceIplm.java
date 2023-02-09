package com.group04.tgdd.service.auth;

import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.model.Users;
import com.group04.tgdd.repository.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailServiceIplm implements UserDetailsService {

    private final UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepo.findUsersByEmail(username);
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user!=null)
            return new UserDetailIplm(user);
        else{
            user = usersRepo.findUsersByPhone(username);
            authorities = new HashSet<>();
            if (user!=null)
                return new UserDetailIplm(user);
            else{
                throw new BadCredentialsException(null);
            }
        }

    }

}
