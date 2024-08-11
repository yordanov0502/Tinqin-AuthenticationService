package com.tinqinacademy.authenticationservice.rest.config;

import com.tinqinacademy.authenticationservice.api.RestApiRoutes;
import com.tinqinacademy.authenticationservice.rest.interceptors.AccessInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor).addPathPatterns(RestApiRoutes.CHANGE_PASSWORD,RestApiRoutes.PROMOTE,RestApiRoutes.DEMOTE);
    }
}
