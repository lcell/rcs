package com.liguang.rcs.admin.web.profile;

import com.liguang.rcs.admin.util.ResponseUtil;
import com.liguang.rcs.admin.util.bcrypt.BCryptPasswordEncoder;
import com.liguang.rcs.db.domain.RcsUser;
import com.liguang.rcs.db.service.RcsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.liguang.rcs.admin.util.ResponseCode.USER_INVALID_ACCOUNT;

//@RestController
@RequestMapping("/admin/profile")
@Validated
@Api(tags = "用户设置API")
public class ProfileController {
    private static final Log LOG = LogFactory.getLog(ProfileController.class);

    @Autowired
    private RcsUserService userService;

    @RequiresAuthentication
    @PostMapping("/password")
    @ApiOperation(value = "修改用户密码")
    public Object changePwd(@RequestBody ChangePwdInput input) {
        if (StringUtils.isEmpty(input.getOldPassword())) {
            return ResponseUtil.badArgument();
        }
        if (StringUtils.isEmpty(input.getNewPassword())) {
            return ResponseUtil.badArgument();
        }

        Subject currentUser = SecurityUtils.getSubject();
        RcsUser admin = (RcsUser) currentUser.getPrincipal();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(input.getOldPassword(), admin.getPassword())) {
            return ResponseUtil.fail(USER_INVALID_ACCOUNT, "账号密码不对");
        }
        String encodedNewPassword = encoder.encode(input.getNewPassword());
        admin.setPassword(encodedNewPassword);
        userService.updateById(admin);
        return ResponseUtil.ok();
    }

}
