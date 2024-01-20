package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.domain.UserEntity;
import com.habibi.stockstoryapi.dto.UserDetailsDto;
import com.habibi.stockstoryapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsDto loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user =  userRepository.findByEmail(email);
        return new UserDetailsDto(user.getUserId(), user.getEmail(), user.getPassword());
    }
}
