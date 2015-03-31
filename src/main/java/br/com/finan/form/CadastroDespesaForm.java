package br.com.finan.form;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.ContaDTO;
import br.com.finan.entidade.Conta;
import br.com.finan.enumerator.TipoConta;

public class CadastroDespesaForm extends CadastroContaForm<Conta, ContaDTO> {

	private static final long serialVersionUID = 1L;

	//	public CadastroDespesaForm() {
	//		buscarDados(0);
	//	}

	@Override
	protected String getTituloFrame() {
		return CadastroContaForm.TITULO_CAD_DESPESA;
	}

	@Override
	protected void iniciarDados() {
		super.iniciarDados();
		getEntidade().setTipo(TipoConta.DESPESA);
	}

	@Override
	protected void adicionarRestricoes(final CriteriaBuilder builder) {
		builder.sqlRestrictions("MONTH(dataVencimento) = " + getMesSelecionado())
		.sqlRestrictions("YEAR(dataVencimento) = " + getAnoSelecionado()).eq("tipo", TipoConta.DESPESA);
	}

}
