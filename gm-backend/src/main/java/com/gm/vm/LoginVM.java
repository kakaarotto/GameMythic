package com.gm.vm;

import com.gm.config.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 * @author pujie
 */
@Data
@NoArgsConstructor
public class LoginVM {

    @Pattern(regexp = Constants.USERNAME_REGEX)
    @NotNull
    @Size(min = 1, max = 50)
    private String username;

//    @Size(min = 6, max = 16)
    private String password;

    private boolean rememberMe = false;

    private String code;

}
