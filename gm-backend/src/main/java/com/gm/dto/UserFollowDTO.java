package com.gm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserFollowDTO {

    private Integer userId;

    private LocalDateTime followTime;

    private String username;

    private String email;

    private String avatar;

    // Check followed
    private Boolean following;
}
