package br.com.finan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.finan.enumerator.Conversor;

/**
 *
 * @author Wesley Luiz
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColunaTabela {
	int index();
	String titulo();
	Class<?> tipo() default Object.class;
	Conversor conversor() default Conversor.DEFAULT;
}
