package com.gm.vm;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Password Recovery View Model
 * @author pujie
 */
@Data
@NoArgsConstructor
public class PasswordVM {

    @NotNull
    private String code;

    @NotNull
    @Size(min = 6, max = 16)
    private String password;

}
