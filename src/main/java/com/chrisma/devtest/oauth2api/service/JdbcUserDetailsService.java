package com.chrisma.devtest.oauth2api.service;

import com.chrisma.devtest.oauth2api.entity.Oauth2ApiUser;
import com.chrisma.devtest.oauth2api.entity.Scope;
import com.chrisma.devtest.oauth2api.repository.ScopeRepository;
import com.chrisma.devtest.oauth2api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JdbcUserDetailsService implements UserDetailsService {
    private UserRepository userRepo;
    private ScopeRepository scopeRepo;

    public JdbcUserDetailsService(UserRepository userRepo, ScopeRepository scopeRepo)
    {
        this.userRepo = userRepo;
        this.scopeRepo = scopeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Oauth2ApiUser user = userRepo.getById(username);
        log.info("role: {}", user.getRole());

        return User.withUsername(username)
                    .authorities(appendScopes(scopeRepo.findByRole(user.getRole())))
                    .password(user.getPassword())
                    .build();
    }

    private String appendScopes(List<Scope> scopes) {
        return scopes.stream().map(scope -> scope.getScope()).collect(Collectors.joining(" "));
    }

    public String getScopesByUsername(String username) {
        Oauth2ApiUser user = userRepo.getById(username);
        List<Scope> scopes = scopeRepo.findByRole(user.getRole());
        return appendScopes(scopes);
    }
}
