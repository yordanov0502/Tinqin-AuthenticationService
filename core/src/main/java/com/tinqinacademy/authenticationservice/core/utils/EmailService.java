package com.tinqinacademy.authenticationservice.core.utils;

import com.tinqinacademy.authenticationservice.api.exceptions.custom.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${env.EMAIL_USERNAME}")
    private String emailSender;
    private final JavaMailSender javaMailSender;

    @Async
    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000)
    )
    public void sendEmailForAccountActivation(String userFirstName, String toEmail, String randomGeneratedCode) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(emailSender);
            helper.setTo(toEmail);
            helper.setSubject("Registration confirmation");

            String htmlMsg = "<h2>Dear " + userFirstName + "!</h2>" +
                    "<p>You receive this email, because you have recently made a registration.</p>" +
                    "<p>In order to login successfully, you have to confirm your email address.</p>" +
                    "<p><b>To do so, you must send us the following code: <span style='color:blue;'>" + randomGeneratedCode + "</span></b></p>" +
                    "<p>By doing this, you will activate your user account.</p>" +
                    "<p>Yours faithfully,<br>The Hotel Service Team.</p>";
            helper.setText(htmlMsg, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailException("Unexpected error occurred while sending email with account confirmation code.");
        }
    }

    @Async
    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000)
    )
    public void sendEmailWithPasswordRecoveryCode(String userFirstName, String toEmail, String passwordRecoveryCode) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(emailSender);
            helper.setTo(toEmail);
            helper.setSubject("Password recovery code");

            String htmlMsg = "<h2>Dear " + userFirstName + "!</h2>" +
                    "<p>You receive this email, because you have recently sent a request for password recovery code.</p>" +
                    "<p><b>This is your password recovery code: <span style='color:blue;'>" + passwordRecoveryCode + "</span></b></p>" +
                    "<p>Yours faithfully,<br>The Hotel Service Team.</p>";
            helper.setText(htmlMsg, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailException("Unexpected error occurred while sending email with password recovery code.");
        }
    }

    @Recover
    private void recover(Exception exception, String userFirstName, String toEmail, String passwordRecoveryCode) {
        log.error(String.format("Error occurred while sending email to %s. Exception: %s",toEmail,exception));
    }

}
