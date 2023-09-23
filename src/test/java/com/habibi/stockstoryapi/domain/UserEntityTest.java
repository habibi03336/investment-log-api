package com.habibi.stockstoryapi.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @Test
    public void builder(){
        UserEntity userEntity = UserEntity.builder().build();
        assertThat(userEntity).isNotNull();
    }

    @Test
    public void javaBean(){
        // Given
        String email = "habibi@example.com";
        String name = "habibi";

        // When
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setName(name);

        // Then
        assertThat(userEntity.getEmail()).isEqualTo(email);
        assertThat(userEntity.getName()).isEqualTo(name);
    }
}