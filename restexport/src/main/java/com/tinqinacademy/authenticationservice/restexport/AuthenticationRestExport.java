package com.tinqinacademy.authenticationservice.restexport;

import com.tinqinacademy.authenticationservice.api.RestApiRoutes;
import com.tinqinacademy.authenticationservice.api.operations.auth.AuthOutput;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json"})
public interface AuthenticationRestExport {

    @RequestLine("POST " + RestApiRoutes.VALIDATE_JWT)
    @Headers({"Authorization: {authorizationHeader}"})
    AuthOutput isJwtValid(@Param("authorizationHeader") String authorizationHeader);
}