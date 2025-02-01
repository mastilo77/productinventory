package com.pt.productinventory.validator;

import com.pt.productinventory.error.exceptions.ObjectNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class ValidatorService {
    private final String className = this.getClass().getSimpleName();
    private final Validator validator;

    public ValidatorService(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T t) {
        log.debug("calling validate method in {}", className);

        if (Objects.isNull(t)) {
            log.error("Error: Object can't be null!");
            throw new ObjectNotFoundException("Object can't be null!");
        }

        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            log.error("Error: Validator Constraint Validation Exception occurred! Error message: {}", violations);
            throw new ConstraintViolationException(violations);
        }
    }
}
