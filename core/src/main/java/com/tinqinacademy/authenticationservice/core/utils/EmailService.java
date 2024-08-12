package com.tinqinacademy.authenticationservice.core.utils;

import com.tinqinacademy.authenticationservice.api.exceptions.custom.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${env.EMAIL_USERNAME}")
    private String emailSender;
    private final JavaMailSender javaMailSender;

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

    public void sendEmailWithNewPassword(String userFirstName, String toEmail, String newRandomGeneratedPassword) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(emailSender);
            helper.setTo(toEmail);
            helper.setSubject("New password");

            String htmlMsg = "<h2>Dear " + userFirstName + "!</h2>" +
                    "<p>You receive this email, because you have recently sent a request for password recovery.</p>" +
                    "<p>We cannot provide you with information about your current password, because it will violate the GDPR policy.</p>" +
                    "<p>However, we can provide you with a new password, so you can access your account.</p>" +
                    "<p><b>This is your new password: <span style='color:blue;'>" + newRandomGeneratedPassword+ "</span></b></p>" +
                    "<p>We kindly advise you to change your password as soon as you gain access to your account.</p>" +
                    "<p>Yours faithfully,<br>The Hotel Service Team.</p>";
            helper.setText(htmlMsg, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailException("Unexpected error occurred while sending email with new password.");
        }
    }


}
