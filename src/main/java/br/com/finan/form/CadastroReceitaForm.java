package br.com.finan.form;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.ContaDTO;
import br.com.finan.entidade.Conta;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.util.CriterionInfo;
import br.com.finan.util.HibernateUtil;

public class CadastroReceitaForm extends CadastroContaForm<Conta, ContaDTO> {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected String getTituloFrame() {
		return CadastroDespesaForm.TITULO_CAD_RECEITA;
	}

	@Override
	protected CriteriaBuilder getBuilderListagem() {
		return CriterionInfo.getInstance(getBuilderQntDados(), ContaDTO.class);
	}

	@Override
	protected CriteriaBuilder getBuilderQntDados() {
		return HibernateUtil.getCriteriaBuilder(Conta.class).eq("tipo", TipoConta.RECEITA).eqStatusAtivo();
	}
	
	@Override
	protected void iniciarDados() {
		super.iniciarDados();
		getEntidade().setTipo(TipoConta.RECEITA);
	}
}
