package br.com.finan.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe responsável por agrupar funções comuns de classes genéricas,
 * fornecendo métodos que trabalhem com <i>reflexão</i>.
 *
 * @author Wesley Luiz
 * @version 1.0.0
 */
public final class FieldUtil {

	/**
	 * Método responsável por verificar se um campo possui uma determinada
	 * anotação.
	 *
	 * @author Wesley Luiz
	 * @param field - Campo a ser analisado.
	 * @param anotacao - Anotação do campo.
	 * @return Retorna <code>true</code> caso possua anotação ou
	 * <code>false</code> caso não tenha.
	 */
	public static boolean isPossuiAnotacao(final Field field, final Class<? extends Annotation> anotacao) {
		return field.isAnnotationPresent(anotacao);
	}

	/**
	 * Return a list of all fields (whatever access status, and on whatever
	 * superclass they were defined) that can be found on this class. This is
	 * like a union of {@link Class#getDeclaredFields()} which ignores and
	 * super-classes, and {@link Class#getFields()} which ignored non-public
	 * fields
	 *
	 * @param clazz The class to introspect
	 * @return The complete list of fields
	 */
	public static Field[] getAllFields(final Class<?> clazz) {
		final List<Class<?>> classes = getAllSuperclasses(clazz);
		classes.add(clazz);
		return getAllFields(classes);
	}

	/**
	 * As {@link #getAllFields(Class)} but acts on a list of {@link Class}s and
	 * uses only {@link Class#getDeclaredFields()}.
	 *
	 * @param classes The list of classes to reflect on
	 * @return The complete list of fields
	 */
	private static Field[] getAllFields(final List<Class<?>> classes) {
		final Set<Field> fields = new HashSet<Field>();
		for (final Class<?> clazz : classes) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		}

		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 * Return a List of super-classes for the given class.
	 *
	 * @param clazz the class to look up
	 * @return the List of super-classes in order going up from this one
	 */
	public static List<Class<?>> getAllSuperclasses(final Class<?> clazz) {
		final List<Class<?>> classes = new ArrayList<Class<?>>();

		Class<?> superclass = clazz.getSuperclass();
		while (superclass != null) {
			classes.add(superclass);
			superclass = superclass.getSuperclass();
		}

		return classes;
	}
}
