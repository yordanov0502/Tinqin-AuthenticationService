package com.tinqinacademy.authenticationservice.api.operations.demote;

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
public class DemoteInput implements OperationInput {
    @NotBlank
    @UUID
    private String userId;
}
