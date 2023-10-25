package com.app.marketplace.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleAuthorizationAspect {
    private final JwtUtil jwtUtil;
    @Around("@annotation(requiresRole)")
    public Object checkAuthorization(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Set<String> roles = jwtUtil.getRolesFromRequest(request);
        List<String> requiredRoles = Arrays.asList(requiresRole.value());

        if (Collections.disjoint(roles, requiredRoles)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return joinPoint.proceed();
    }
}


