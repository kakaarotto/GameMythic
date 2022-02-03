package com.gm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO representing a user, with his roles.
 */
@Data
@NoArgsConstructor
public class UserDTO {

    private Integer userId;

    private LocalDateTime addTime;

    private LocalDateTime updateTime;

    private String username;

    private String email;

    private String mobile;

    private String avatar;

    private Boolean notificationEnabled;
}
