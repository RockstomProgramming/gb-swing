package br.com.finan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import br.com.finan.entidade.enumerator.EnumStatus;

/**
 * Arquivo: CriteriaBuilder.java <br/>
 *
 * @since 16/09/2014
 * @author Wesley Luiz
 * @version 1.0.0
 */
public class CriteriaBuilder implements Criterion {

	private Criteria criteria;
	private ProjectionList proj;
	private Class<?> aliasToBean;
	private PropertiesBuilderCriteria propertiesBuilder;
	private static final String EQ = "=";
	private static final String LE = "<=";
	private static final String GE = ">=";
	private static final String QUOTES = "'";
	private static final String FUNCTION_DATE = "date(dataEmissao)";
	private static final String DATE_PATTERN = "yyyy-MM-dd";

	public CriteriaBuilder(final Criteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public CriteriaBuilder addAliases(final String associationPath, final String alias) {
		this.criteria.createAlias(associationPath, alias, Criterion.INNER_JOIN);
		return this;
	}

	@Override
	public CriteriaBuilder addAliases(final String associationPath, final String alias, final JoinType joinType) {
		this.criteria.createAlias(associationPath, alias, joinType);
		return this;
	}

	@Override
	public CriteriaBuilder addAliases(final JoinType joinType, final String... aliases) {
		for (final String alias : aliases) {
			addAliases(alias, alias, joinType);
		}
		return this;
	}

	@Override
	public CriteriaBuilder eq(final String propertyName, final Object value) {
		this.criteria.add(Restrictions.eq(propertyName, value));
		return this;
	}

	@Override
	public CriteriaBuilder ge(final String propertyName, final Object value) {
		this.criteria.add(Restrictions.ge(propertyName, value));
		return this;
	}

	@Override
	public CriteriaBuilder gt(final String propertyName, final Object value) {
		this.criteria.add(Restrictions.gt(propertyName, value));
		return this;
	}

	@Override
	public CriteriaBuilder le(final String propertyName, final Object value) {
		this.criteria.add(Restrictions.le(propertyName, value));
		return this;
	}

	@Override
	public CriteriaBuilder lt(final String propertyName, final Object value) {
		this.criteria.add(Restrictions.lt(propertyName, value));
		return this;
	}

	@Override
	public CriteriaBuilder ne(final String propertyName, final Object value) {
		this.criteria.add(Restrictions.ne(propertyName, value));
		return this;
	}

	@Override
	public CriteriaBuilder in(final String propertyName, final Object[] values) {
		this.criteria.add(Restrictions.in(propertyName, values));
		return this;
	}

	@Override
	public CriteriaBuilder isNull(final String propertyName) {
		this.criteria.add(Restrictions.isNull(propertyName));
		return this;
	}

	@Override
	public CriteriaBuilder isNotNull(final String propertyName) {
		this.criteria.add(Restrictions.isNotNull(propertyName));
		return this;
	}

	@Override
	public CriteriaBuilder eqId(final Number id) {
		this.criteria.add(Restrictions.eq("id", id));
		return this;
	}

	@Override
	public CriteriaBuilder ilike(final String propertyName, final Object value) {
		getCriteria().add(Restrictions.ilike(propertyName, value));
		return this;
	}

	@Override
	public CriteriaBuilder ilike(final String propertyName, final String value, final MatchMode mode) {
		getCriteria().add(Restrictions.ilike(propertyName, value.toString(), mode));
		return this;
	}

	@Override
	public CriteriaBuilder between(final String propertyName, final Object v0, final Object v1) {
		getCriteria().add(Restrictions.between(propertyName, v0, v1));
		return this;
	}

	@Override
	public CriteriaBuilder sqlRestrictions(final String sql) {
		getCriteria().add(Restrictions.sqlRestriction(sql));
		return this;
	}

	@Override
	public CriteriaBuilder addFiltro(final Map<String, Object> filters) {
		if (filters != null) {
			for (final Map.Entry<String, Object> entry : filters.entrySet()) {
				criteria.add(Restrictions.like(entry.getKey(), entry.getValue().toString(), ANYWHERE));
			}
		}
		return this;
	}

	@Override
	public CriteriaBuilder addOrdenacao(final String sortField, final String sortOrder) {
		if (sortField != null && sortOrder != null) {
			if (sortOrder.equals(ASCENDING.toString())) {
				criteria.addOrder(Order.asc(sortField));
			} else if (sortOrder.equals(DESCENDING.toString())) {
				criteria.addOrder(Order.desc(sortField));
			}
		}
		return this;
	}

	@Override
	public CriteriaBuilder addOrdenacao(final Order order) {
		getCriteria().addOrder(order);
		return this;
	}

	@Override
	public CriteriaBuilder addPaginacao(final int first, final int pageSize) {
		criteria.setFirstResult(first);
		criteria.setMaxResults(pageSize);
		return this;
	}

	@Override
	public CriteriaBuilder eqStatus(final EnumStatus status) {
		eq("status", status);
		return this;
	}

	@Override
	public CriteriaBuilder eqStatusAtivo() {
		eqStatus(EnumStatus.ATIVO);
		return this;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List list() {
		return getCriteria().list();
	}

	@Override
	public Object uniqueResult() {
		return getCriteria().uniqueResult();
	}

	@Override
	public CriteriaBuilder addProjection(final String propertyName) {
		addProjection(propertyName, propertyName);
		return this;
	}

	@Override
	public CriteriaBuilder addProjection(final String propertyName, final String anotherProperty) {
		projectionInstance().add(Projections.property(propertyName), anotherProperty);
		return this;
	}

	@Override
	public CriteriaBuilder addProjection(final String... properties) {
		for (final String p : properties) {
			addProjection(p);
		}
		return this;
	}

	@Override
	public Long getRowCount() {
		return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	public CriteriaBuilder addAliasToBean(final Class<?> classe) {
		this.aliasToBean = classe;
		return this;
	}

	public CriteriaBuilder close() {
		getCriteria().setProjection(proj).setResultTransformer(Transformers.aliasToBean(this.aliasToBean));
		return this;
	}

	private ProjectionList projectionInstance() {
		if (this.proj == null) {
			this.proj = Projections.projectionList();
		}

		return proj;
	}

	public PropertiesBuilderCriteria getPropertiesBuilder() {
		if (propertiesBuilder == null) {
			propertiesBuilder = new PropertiesBuilderCriteria();
		}
		return propertiesBuilder;
	}

	public final class PropertiesBuilderCriteria {

		private Map<String, String> propertiesMap;

		public PropertiesBuilderCriteria add(final String property, final String anotherProperty) {
			propertyInstance().put(property, anotherProperty);
			return this;
		}

		public PropertiesBuilderCriteria add(final String property) {
			add(property, property);
			return this;
		}

		public void generateProjection() {
			for (final Map.Entry<String, String> projection : propertiesMap.entrySet()) {
				addProjection(projection.getKey(), projection.getValue());
			}
		}

		private Map<String, String> propertyInstance() {
			if (propertiesMap == null) {
				propertiesMap = new HashMap<String, String>();
			}
			return propertiesMap;
		}
	}

	public void setCriteria(final Criteria criteria) {
		this.criteria = criteria;
	}

	public Criteria getCriteria() {
		return criteria;
	}
}
