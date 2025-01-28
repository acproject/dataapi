package com.owiseman.dataapi.audit;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginAuditAspect {
    @AfterReturning(pointcut = "execution(* org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider.authenticate(..))")
    public void auditLoginSucess() {
        // todo 记录登录成功日志
    }

     @AfterThrowing("execution(* org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider.authenticate(..))")
    public void auditLoginFailure() {
        // todo 记录登录失败日志
    }
}
