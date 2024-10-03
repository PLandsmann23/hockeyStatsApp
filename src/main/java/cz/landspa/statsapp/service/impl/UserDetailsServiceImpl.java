package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Uživatel nebyl nalezen"));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
                grantedAuthorities.add( new SimpleGrantedAuthority(user.getRole().toString()));


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),

                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                grantedAuthorities
        );
    }
}
