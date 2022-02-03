package com.gm.dto;

import com.gm.config.Constants;
import com.gm.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * A DTO representing a user
 * @author pujie
 */
@Data
@NoArgsConstructor
public class UserSimpleDTO {

    private Integer userId;

    private LocalDateTime addTime;

    private LocalDateTime updateTime;

    @NotBlank
    @Pattern(regexp = Constants.USERNAME_REGEX)
    @Size(min = 1, max = 50)
    private String username;

    private String avatar = Constants.USER_DEFAULT_PHOTO;

    public UserSimpleDTO(User user) {
        this.userId = user.getId();
        this.addTime = user.getAddTime();
        this.updateTime = user.getUpdateTime();
        this.username = user.getUsername();
        this.avatar = user.getAvatar();
    }
}
