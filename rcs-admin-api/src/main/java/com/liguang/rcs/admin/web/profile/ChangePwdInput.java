package com.liguang.rcs.admin.web.profile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChangePwdInput {
    private String oldPassword;
    private String newPassword;

}
