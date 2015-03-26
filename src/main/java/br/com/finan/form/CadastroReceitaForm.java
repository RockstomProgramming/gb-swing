package br.com.finan.form;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.ContaDTO;
import br.com.finan.entidade.Conta;
import br.com.finan.enumerator.TipoConta;

public class CadastroReceitaForm extends CadastroContaForm<Conta, ContaDTO> {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected String getTituloFrame() {
		return CadastroDespesaForm.TITULO_CAD_RECEITA;
	}

	@Override
	protected void iniciarDados() {
		super.iniciarDados();
		getEntidade().setTipo(TipoConta.RECEITA);
	}
	
	@Override
	protected void adicionarRestricoes(CriteriaBuilder builder) {
		builder.sqlRestrictions("MONTH(dataVencimento) = " + getMesSelecionado())
			.sqlRestrictions("YEAR(dataVencimento) = " + getAnoSelecionado()).eq("tipo", TipoConta.RECEITA);
	}
}
