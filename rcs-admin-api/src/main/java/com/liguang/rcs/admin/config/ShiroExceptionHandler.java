package com.liguang.rcs.admin.config;

import com.liguang.rcs.admin.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order( value = Ordered.HIGHEST_PRECEDENCE )
@Slf4j
public class ShiroExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public Object unauthenticatedHandler(AuthenticationException e) {
        log.error("[Auth] unauthenticatedHandler Fail, Exception:", e);
        return ResponseUtil.unlogin();
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public Object unauthorizedHandler(AuthorizationException e) {
        log.error("[Auth] unauthorizedHandler Fail, Exception:", e);
        return ResponseUtil.unauthz();
    }

}
