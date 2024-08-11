package com.tinqinacademy.authenticationservice.api.operations.promote;

import com.tinqinacademy.authenticationservice.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PromoteInput implements OperationInput {
    @NotBlank
    @UUID
    private String userId;
}
