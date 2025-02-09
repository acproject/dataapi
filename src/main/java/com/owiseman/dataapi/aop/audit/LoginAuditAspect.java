package com.owiseman.dataapi.aop.audit;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginAuditAspect {
    private static final Logger log = LoggerFactory.getLogger(LoginAuditAspect.class);
    @AfterReturning(pointcut = "execution(* org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider.authenticate(..))",
                    returning = "result")
    public void auditLoginSucess(Object result) {
        log.info("用户登录成功: {}", result);
    }

     @AfterThrowing(value = "execution(* org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider.authenticate(..))",
     throwing = "ex")
    public void auditLoginFailure(Throwable ex) {
        log.error("用户登录失败: ",  ex);
    }
}
