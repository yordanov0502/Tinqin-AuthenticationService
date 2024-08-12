package com.tinqinacademy.authenticationservice.rest.interceptors;

import com.tinqinacademy.authenticationservice.api.RestApiRoutes;
import com.tinqinacademy.authenticationservice.core.security.JwtService;
import com.tinqinacademy.authenticationservice.persistence.model.context.UserContext;
import com.tinqinacademy.authenticationservice.persistence.model.entity.User;
import com.tinqinacademy.authenticationservice.persistence.model.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccessInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;
    private final UserContext userContext;
    @Value("${env.REDIRECT_TO_LOGIN_PAGE_RESPONSE_HEADER}")
    private String REDIRECT_TO_LOGIN_PAGE_RESPONSE_HEADER;
    @Value("${env.REDIRECT_TO_LOGIN_PAGE_RESPONSE_HEADER_VALUE}")
    private String REDIRECT_TO_LOGIN_PAGE_RESPONSE_HEADER_VALUE;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws IOException {

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized.");
            return false;
        }

        String jwt = authorizationHeader.substring(7);
        Optional<User> currUser = jwtService.validateJwtAndReturnUser(jwt);
        if(currUser.isEmpty()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized.");

            if (request.getRequestURI().equals(RestApiRoutes.LOGOUT)){
                response.setHeader(REDIRECT_TO_LOGIN_PAGE_RESPONSE_HEADER,REDIRECT_TO_LOGIN_PAGE_RESPONSE_HEADER_VALUE);
            }
            return false;
        }

        if(request.getRequestURI().equals(RestApiRoutes.PROMOTE) || request.getRequestURI().equals(RestApiRoutes.DEMOTE)){
            if(!currUser.get().getRole().toString().equals(Role.ADMIN.toString())){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Forbidden.");
                return false;
            }
        }

        userContext.setCurrAuthorizedUser(currUser.get());
        userContext.setJwt(jwt);
        return true;
    }

}