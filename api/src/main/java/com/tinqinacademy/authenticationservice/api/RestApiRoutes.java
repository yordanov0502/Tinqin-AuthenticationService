package com.tinqinacademy.authenticationservice.api;

public class RestApiRoutes {
    public static final String API = "/api/v1";

    public static final String API_AUTH = API+"/auth";


    public static final String LOGIN = API_AUTH+"/login";
    public static final String REGISTER = API_AUTH+"/register";
    public static final String RECOVER_PASSWORD = API_AUTH+"/recover-password";
    public static final String CONFIRM_REGISTRATION = API_AUTH+"/confirm-registration";
    public static final String CHANGE_PASSWORD = API_AUTH+"/change-password";
    public static final String PROMOTE = API_AUTH+"/promote";
    public static final String DENOTE = API_AUTH+"/denote";
}