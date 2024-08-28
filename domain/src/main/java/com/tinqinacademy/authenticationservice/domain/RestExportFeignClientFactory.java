package com.tinqinacademy.authenticationservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.emailservice.restexport.EmailRestExport;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RestExportFeignClientFactory {

    @Value("${env.EMAIL_SERVICE_URL}")
    private String EMAIL_SERVICE_URL;
    private final ApplicationContext applicationContext;

    @Bean
    public EmailRestExport buildEmailFeignClient() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder(applicationContext.getBean(ObjectMapper.class)))
                .decoder(new JacksonDecoder(applicationContext.getBean(ObjectMapper.class)))
                .target(EmailRestExport.class,EMAIL_SERVICE_URL);
    }
}