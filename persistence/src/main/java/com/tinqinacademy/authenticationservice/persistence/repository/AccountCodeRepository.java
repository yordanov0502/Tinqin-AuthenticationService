package com.tinqinacademy.authenticationservice.persistence.repository;

import com.tinqinacademy.authenticationservice.persistence.model.entity.AccountCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountCodeRepository extends JpaRepository<AccountCode, UUID> {
    boolean existsByCode(String code);
    void deleteByEmail(String email);
    Optional<AccountCode> findByCode(String confirmationCode);
}
