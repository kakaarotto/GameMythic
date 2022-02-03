package com.gm.vm;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author pujie
 */
@Data
@NoArgsConstructor
public class ChangePasswordVM {

    @NotNull
    private String oldPassword;

    @NotNull
    @Size(min = 6, max = 16)
    private String password;

}
