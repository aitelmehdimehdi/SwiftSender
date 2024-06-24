package com.PFE.etc.PasswordChanging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword {
    private String password;
    private String new_pswd;
    private String new_pswd2;
}
