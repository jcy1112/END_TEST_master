package com.springboot.common;

import java.lang.annotation.*;

/**
 * 自定义注解类1111111111111111111111
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {
}
