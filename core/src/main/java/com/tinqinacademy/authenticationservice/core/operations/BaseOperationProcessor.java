package com.tinqinacademy.authenticationservice.core.operations;

import com.tinqinacademy.authenticationservice.api.base.OperationInput;
import com.tinqinacademy.authenticationservice.api.exceptions.Error;
import com.tinqinacademy.authenticationservice.core.exceptions.ExceptionService;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.ViolationsException;
import com.tinqinacademy.authenticationservice.core.utils.LoggingUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public abstract class BaseOperationProcessor {

    protected final ConversionService conversionService;
    protected final ExceptionService exceptionService;
    private final Validator validator;

    protected <I extends OperationInput> void validate(I input){
        Set<ConstraintViolation<I>> violationSet = validator.validate(input);

        if(!violationSet.isEmpty()){
        List<Error> errorList = violationSet
                .stream()
                .map(violation -> Error.builder()
                        .field(violation.getPropertyPath().toString())
                        .errMsg(violation.getMessage())
                        .build())
                .collect(Collectors.toList());
        throw new ViolationsException("Violation exception occurred.",errorList);
        }
    }

//    protected void logStart(Object... args) {
//        log.info(String.format("Start %s %s input: %s",
//                this.getClass().getSimpleName(),
//                LoggingUtils.getMethodName(),
//                formatArgs(args)
//        ));
//    }
//
//    protected void logEnd(Object... args) {
//        log.info(String.format("End %s %s output: %s",
//                this.getClass().getSimpleName(),
//                LoggingUtils.getMethodName(),
//                formatArgs(args)
//        ));
//    }
//
//    private String formatArgs(Object... args) {
//        StringBuilder sb = new StringBuilder();
//        for (Object arg : args) {
//            sb.append(arg).append(", ");
//        }
//
//        if (sb.length() > 0) {
//            sb.setLength(sb.length() - 2);
//        }
//        return sb.toString();
//    }
}