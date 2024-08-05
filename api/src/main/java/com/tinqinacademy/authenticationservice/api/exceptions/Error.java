package com.tinqinacademy.authenticationservice.api.exceptions;

import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Error {
    private String field;
    private String errMsg;
}
