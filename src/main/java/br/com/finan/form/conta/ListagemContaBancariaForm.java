package br.com.finan.form.conta;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.form.principal.ListagemForm;
import br.com.finan.util.HibernateUtil;

public class ListagemContaBancariaForm extends ListagemForm<ContaBancariaDTO> {

	private static final long serialVersionUID = 1L;
	
	public ListagemContaBancariaForm() {
		iniciarDados();
	}
	
	@Override
	protected CriteriaBuilder getBuilderListagem() {
		return getBuilder().addProjection("id", "descricao", "numero", "agencia").addAliasToBean(ContaBancariaDTO.class).close();
	}

	@Override
	protected CriteriaBuilder getBuilderQntRegistros() {
		return getBuilder();
	}

	private CriteriaBuilder getBuilder() {
		return HibernateUtil.getCriteriaBuilder(ContaBancaria.class).eqStatusAtivo();
	}

	@Override
	protected String getNomeEntidade() {
		return ContaBancaria.class.getSimpleName();
	}

	@Override
	protected String getTituloFrame() {
		return "";
	}

}
