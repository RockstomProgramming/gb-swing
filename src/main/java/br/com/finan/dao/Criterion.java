package br.com.finan.dao;

import br.com.finan.entidade.enumerator.EnumStatus;
import java.util.List;
import java.util.Map;
import javax.swing.SortOrder;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.sql.JoinType;

/**
 * Arquivo: Criterion.java <br/>
 * @since 16/09/2014
 * @author Wesley Luiz
 * @version 1.0.0
 */
@SuppressWarnings("rawtypes")
public interface Criterion {
	
	public static JoinType FULL_JOIN = JoinType.FULL_JOIN;
	public static JoinType INNER_JOIN = JoinType.INNER_JOIN;
	public static JoinType LEFT_JOIN = JoinType.LEFT_OUTER_JOIN;
	public static JoinType RIGHT_JOIN = JoinType.RIGHT_OUTER_JOIN;
	
	public static SortOrder ASCENDING = SortOrder.ASCENDING;
	public static SortOrder DESCENDING = SortOrder.DESCENDING;
	public static SortOrder UNSORTED = SortOrder.UNSORTED;
	
	public static MatchMode ANYWHERE = MatchMode.ANYWHERE;
	public static MatchMode END = MatchMode.END;
	public static MatchMode EXACT = MatchMode.EXACT;
	public static MatchMode START = MatchMode.START;
	
	
	CriteriaBuilder addAliases(String associationPath, String alias);
	CriteriaBuilder addAliases(String associationPath, String alias, JoinType joinType);
	CriteriaBuilder addAliases(final JoinType joinType, final String... aliases);
	
	CriteriaBuilder eq(String propertyName, Object value);
	CriteriaBuilder lt(String propertyName, Object value);
	CriteriaBuilder le(String propertyName, Object value);
	CriteriaBuilder ge(String propertyName, Object value);
	CriteriaBuilder gt(String propertyName, Object value);
	CriteriaBuilder ne(String propertyName, Object value);
	CriteriaBuilder in(String propertyName, Object[] values);
	CriteriaBuilder isNull(String propertyName);
	CriteriaBuilder isNotNull(String propertyName);
	CriteriaBuilder eqId(Number id);
	CriteriaBuilder between(String propertyName, Object v0, Object v1);

	CriteriaBuilder eqStatusAtivo();
	CriteriaBuilder eqStatus(EnumStatus status);

	CriteriaBuilder ilike(String propertyName, Object value);
	CriteriaBuilder ilike(String propertyName, String value, MatchMode mode);
	
	CriteriaBuilder addProjection(String propertyName);
	CriteriaBuilder addProjection(String propertyName, String anotherProperty);
	CriteriaBuilder addProjection(String... propertyName);
	
	CriteriaBuilder addPaginacao(int first, int pageSize);
	CriteriaBuilder addOrdenacao(String sortField, String sortOrder);
	CriteriaBuilder addOrdenacao(Order order);
	CriteriaBuilder addFiltro(Map<String, Object> filters);
	
	List list();
	Object uniqueResult();
	Long getRowCount();
}
