package com.lamnd.annotation.validator;

import com.lamnd.annotation.UniqueEmail;
import com.lamnd.repository.UserRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepo userRepo;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userRepo.existsByEmail(email); // return true if email does not exist in the database
    }
}
