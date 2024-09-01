package com.tinqinacademy.authenticationservice.kafka.model;

import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private String to;
    private String email;
    private String content;
}
