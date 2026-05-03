package com.lamnd.annotation.validator;


import com.lamnd.annotation.UniquePhone;
import com.lamnd.repository.UserRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniquePhoneValidator implements ConstraintValidator<UniquePhone, String> {

    private final UserRepo userRepo;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null) {
            return true; // consider null as valid, use @NotNull for null checks
        }
        return !userRepo.existsByPhoneNumber(phone); // return true if phone does not exist in the database
    }
}
