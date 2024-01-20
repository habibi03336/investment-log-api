package com.habibi.stockstoryapi.service;

import com.habibi.stockstoryapi.dto.UserDetailsDto;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    UserDetailsDto loadUserByUsername(String email);
}
