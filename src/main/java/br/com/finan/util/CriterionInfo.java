package br.com.finan.util;

import java.lang.reflect.Field;
import java.util.List;

import br.com.finan.annotation.Aliases;
import br.com.finan.annotation.CreateAlias;
import br.com.finan.annotation.ProjectionEntityProperty;
import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.DTO;

/**
 * Arquivo: CriterionInfo.java <br/>
 * @since 13/10/2014
 * @author Wesley Luiz
 * @version 1.0.0
 */
public final class CriterionInfo {

	private CriterionInfo() {
	}

	public static CriteriaBuilder getInstance(final Class<?> entidade, final Class<? extends DTO> dto) {
		final CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder(entidade);
		return gerarProjecoes(builder, dto);
	}

	public static CriteriaBuilder getInstance(final CriteriaBuilder builder, final Class<? extends DTO> dto) {
		return gerarProjecoes(builder, dto);
	}

	private static CriteriaBuilder gerarProjecoes(final CriteriaBuilder builder, final Class<? extends DTO> dto) {

		final List<Class<?>> superclasses = FieldUtil.getAllSuperclasses(dto);
		superclasses.add(dto);

		for (final Class<?> clazz : superclasses) {
			if (clazz.isAnnotationPresent(Aliases.class)) {
				final CreateAlias[] aliasesCreate = clazz.getAnnotation(Aliases.class).value();
				for (final CreateAlias createAlias : aliasesCreate) {
					builder.addAliases(createAlias.associationPath(), createAlias.alias(), createAlias.joinType());
				}
			}
		}

		for (final Field field : FieldUtil.getAllFields(dto)) {
			if (field.isAnnotationPresent(ProjectionEntityProperty.class)) {
				builder.addProjection(field.getAnnotation(ProjectionEntityProperty.class).value(), field.getName());
			}
		}

		return builder.addAliasToBean(dto).close();
	}
}
