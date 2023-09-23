package com.habibi.stockstoryapi.domain;

import lombok.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "userId")
public class UserEntity {
    private long userId;
    private String email;
    private String password;
    private String name;
}
