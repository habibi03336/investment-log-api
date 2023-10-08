package com.habibi.stockstoryapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "userId")
@Entity
public class UserEntity {
    @Id @GeneratedValue
    private int userId;
    private String email;
    private String password;
    private String name;
}
