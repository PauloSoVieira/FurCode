package org.mindera.fur.code.aspect.roleauth;

import org.mindera.fur.code.model.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    Role value();
    String shelterIdField() default "";
    int shelterIdParam() default -1;
    boolean isPetOperation() default false;
    int petIdParam() default -1;
}