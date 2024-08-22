package com.tinqinacademy.authenticationservice.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.authenticationservice.api.RestApiRoutes;
import com.tinqinacademy.authenticationservice.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.authenticationservice.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authenticationservice.api.operations.demote.DemoteInput;
import com.tinqinacademy.authenticationservice.api.operations.login.LoginInput;
import com.tinqinacademy.authenticationservice.api.operations.promote.PromoteInput;
import com.tinqinacademy.authenticationservice.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.authenticationservice.api.operations.register.RegisterInput;
import com.tinqinacademy.authenticationservice.api.operations.resetpassword.ResetPasswordInput;
import com.tinqinacademy.authenticationservice.core.security.JwtService;
import com.tinqinacademy.authenticationservice.persistence.model.entity.User;
import com.tinqinacademy.authenticationservice.persistence.model.enums.Role;
import com.tinqinacademy.authenticationservice.persistence.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .role(Role.ADMIN)
                .username("yordanov4.0")
                .email("yordanovtodor281@gmail.com")
                .password(passwordEncoder.encode("1234ABCDefgh****"))
                .firstName("Тодор")
                .lastName("Йорданов")
                .phoneNumber("0882960111")
                .dateOfBirth(LocalDate.of(2001,6,30))
                .isConfirmed(true)
                .build();

        userRepository.save(user);

        User user1 = User.builder()
                .role(Role.USER)
                .username("emo4.0")
                .email("natoto01@abv.bg")
                .password(passwordEncoder.encode("1234ABCDefgh****"))
                .firstName("Емил")
                .lastName("Ефтимов")
                .phoneNumber("0882960222")
                .dateOfBirth(LocalDate.of(2001,6,30))
                .isConfirmed(true)
                .build();

        userRepository.save(user1);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void loginOk() throws Exception {
        LoginInput input = LoginInput.builder()
                .username("yordanov4.0")
                .password("1234ABCDefgh****")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    void loginBadRequest() throws Exception {
        LoginInput input = LoginInput.builder()
                .username("0")
                .password("1234ABCDefgh****")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginNotFound() throws Exception {
        LoginInput input = LoginInput.builder()
                .username("0")
                .password("1234ABCDefgh****")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.LOGIN+"/wrong")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound());
    }



    @Test
    void registerCreated() throws Exception {
        RegisterInput input = RegisterInput.builder()
                .username("emo2.0")
                .password("1234ABCDefgh****")
                .firstName("Емил")
                .lastName("Ефтимов")
                .email("oblakanatosho1@gmail.com")
                .phoneNumber("0897652431")
                .dateOfBirth(LocalDate.of(2001,9,7))
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated());
    }

    @Test
    void registerBadRequest() throws Exception {
        RegisterInput input = RegisterInput.builder()
                .username("emo3.0")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerNotFound() throws Exception {
        RegisterInput input = RegisterInput.builder()
                .username("emo3.0")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.REGISTER+"/wrong")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    void recoverPasswordOk() throws Exception {
        RecoverPasswordInput input = RecoverPasswordInput.builder()
                .email("natoto01@abv.bg")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.RECOVER_PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    void recoverPasswordBadRequest() throws Exception {
        RecoverPasswordInput input = RecoverPasswordInput.builder()
                .email("bg")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.RECOVER_PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recoverPasswordNotFound() throws Exception {
        RecoverPasswordInput input = RecoverPasswordInput.builder()
                .email("bg")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.RECOVER_PASSWORD+"/wrong")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    void resetPasswordBadRequest() throws Exception {
        ResetPasswordInput input = ResetPasswordInput.builder()
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.RESET_PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void resetPasswordNotFound() throws Exception {
        ResetPasswordInput input = ResetPasswordInput.builder()
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.RESET_PASSWORD+"/wrong")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    void confirmRegistrationBadRequest() throws Exception {
        ConfirmRegistrationInput input = ConfirmRegistrationInput.builder()
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.CONFIRM_REGISTRATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void confirmRegistrationNotFound() throws Exception {
        ConfirmRegistrationInput input = ConfirmRegistrationInput.builder()
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.CONFIRM_REGISTRATION+"/wrong")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    void changePasswordOk() throws Exception {
        User testUser = userRepository.findByEmail("yordanovtodor281@gmail.com").get();
        String authHeader = "Bearer " + jwtService.generateToken(testUser);

        ChangePasswordInput input = ChangePasswordInput.builder()
                .oldPassword("1234ABCDefgh****")
                .newPassword("4321ABCDefgh****")
                .email("yordanovtodor281@gmail.com")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.CHANGE_PASSWORD)
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    void changePasswordUnauthorized() throws Exception {
        ChangePasswordInput input = ChangePasswordInput.builder()
                .oldPassword("1234ABCDefgh****")
                .newPassword("4321ABCDefgh****")
                .email("yordanovtodor281@gmail.com")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.CHANGE_PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void promoteUserToAdminOk() throws Exception {
        User testUser = userRepository.findByEmail("yordanovtodor281@gmail.com").get();
        String authHeader = "Bearer " + jwtService.generateToken(testUser);
        String user1Id = userRepository.findAll().get(1).getId().toString();

        PromoteInput input = PromoteInput.builder()
                .userId(user1Id)
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.PROMOTE)
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    void promoteUserToAdminUnauthorized() throws Exception {
        String user1Id = userRepository.findAll().get(1).getId().toString();

        PromoteInput input = PromoteInput.builder()
                .userId(user1Id)
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.PROMOTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void demoteAdminToUserCreated() throws Exception {
        User user = User.builder()
                .role(Role.ADMIN)
                .username("ivan716")
                .email("zit960003@gmail.com")
                .password(passwordEncoder.encode("1234ABCDefgh****"))
                .firstName("Иван")
                .lastName("Петров")
                .phoneNumber("0882960212")
                .dateOfBirth(LocalDate.of(2001,11,12))
                .isConfirmed(true)
                .build();

        User savedUser = userRepository.save(user);

        User testUser = userRepository.findByEmail("yordanovtodor281@gmail.com").get();
        String authHeader = "Bearer " + jwtService.generateToken(testUser);

        DemoteInput input = DemoteInput.builder()
                .userId(savedUser.getId().toString())
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.DEMOTE)
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    void demoteAdminToUserUnauthorized() throws Exception {
        User user = User.builder()
                .role(Role.ADMIN)
                .username("ivan716")
                .email("zit960003@gmail.com")
                .password(passwordEncoder.encode("1234ABCDefgh****"))
                .firstName("Иван")
                .lastName("Петров")
                .phoneNumber("0882960212")
                .dateOfBirth(LocalDate.of(2001,11,12))
                .isConfirmed(true)
                .build();

        User savedUser = userRepository.save(user);

        DemoteInput input = DemoteInput.builder()
                .userId(savedUser.getId().toString())
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.DEMOTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutOk() throws Exception {
        User testUser = userRepository.findByEmail("yordanovtodor281@gmail.com").get();
        String authHeader = "Bearer " + jwtService.generateToken(testUser);

        mvc.perform(post(RestApiRoutes.LOGOUT)
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    void logoutUnauthorized() throws Exception {
        mvc.perform(post(RestApiRoutes.LOGOUT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isUnauthorized());
    }
}