package com.lamnd.annotation.validator;


import com.lamnd.annotation.UniqueUsername;
import com.lamnd.repository.UserRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepo userRepo;

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        return !userRepo.existsByUsername(userName); // return true if email does not exist in the database
    }
}
