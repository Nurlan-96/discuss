package com.discuss.user.validation;

import com.discuss.user.dto.UserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, UserRequest> {

    @Override
    public boolean isValid(UserRequest dto, ConstraintValidatorContext context) {

        if (dto == null) {
            return true;
        }

        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();

        if (password == null || confirmPassword == null) {
            return true;
        }

        return password.equals(confirmPassword);
    }
}
