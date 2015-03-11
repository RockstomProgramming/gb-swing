package br.com.finan.entidade.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Arquivo: Unique.java <br/>
 *
 * @author Wesley Luiz
 * @version 2.0
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Documented
public @interface Unique {

	String message() default "";
}
