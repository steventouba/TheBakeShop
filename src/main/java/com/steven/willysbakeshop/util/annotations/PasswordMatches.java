package com.steven.willysbakeshop.util.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
@Deprecated
public @interface PasswordMatches {
    String message() default  "Passwords don't match";
    Class<?>[] groups() default {};
    Class <? extends Payload>[] payload() default {};
}
