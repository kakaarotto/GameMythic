package com.gm.vm;

import com.gm.config.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 * @author pujie
 */
@NoArgsConstructor
@Data
public class UserVM {

    public static final int PASSWORD_MIN_LENGTH = 6;

    public static final int PASSWORD_MAX_LENGTH = 16;

    @NotNull
    @Pattern(regexp = Constants.USERNAME_REGEX)
    @Size(min = 1, max = 50)
    private String username;

    private String email;

    private String avatar;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public String roleName;

    private Boolean notificationEnabled;
}
