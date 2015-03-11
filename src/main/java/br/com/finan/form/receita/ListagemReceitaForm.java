package br.com.finan.form.receita;

import org.hibernate.criterion.Order;
import org.hibernate.sql.JoinType;
import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.ContaDTO;
import br.com.finan.entidade.Conta;
import br.com.finan.entidade.enumerator.TipoConta;
import br.com.finan.form.principal.ListagemContaForm;
import br.com.finan.util.HibernateUtil;

/**
 *
 * @author Wesley Luiz
 */
public class ListagemReceitaForm extends ListagemContaForm<ContaDTO> {

	/** Atributo serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected String getTituloFrame() {
		return "Cadastro de Receitas";
	}

	@Override
	protected CriteriaBuilder getBuilderListagem() {
		return HibernateUtil.getCriteriaBuilder(Conta.class).eqStatusAtivo().eq("tipo", TipoConta.RECEITA).sqlRestrictions("MONTH(dataVencimento) = " + getMesSelecionado().getReferencia()).sqlRestrictions("YEAR(dataVencimento) = " + getAno())
				.addAliases("categoria", "categoria", JoinType.LEFT_OUTER_JOIN).addProjection("id", "id").addProjection("descricao", "descricao").addProjection("valor", "valor").addProjection("dataVencimento", "vencimento").addProjection("parcela", "parcela")
				.addProjection("totalParcelas", "totalParcela").addProjection("categoria.nome", "categoria").addAliasToBean(ContaDTO.class).close().addOrdenacao(Order.asc("dataVencimento"));
	}

	@Override
	protected CriteriaBuilder getBuilderQntRegistros() {
		return HibernateUtil.getCriteriaBuilder(Conta.class).eqStatusAtivo().eq("tipo", TipoConta.RECEITA).sqlRestrictions("MONTH(dataVencimento) = " + getMesSelecionado().getReferencia()).sqlRestrictions("YEAR(dataVencimento) = " + getAno());
	}

}
