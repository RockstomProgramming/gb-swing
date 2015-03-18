package br.com.finan.form;

import javax.swing.JPanel;

import br.com.arq.form.CadastroForm;
import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.ContaDTO;
import br.com.finan.entidade.Conta;

public class CadastroContaForm extends CadastroForm<Conta, ContaDTO> {

	private static final long serialVersionUID = 1L;
	private boolean isDespesa;
	
	public CadastroContaForm(boolean isDespesa) {
		this.isDespesa = isDespesa;
	}

	@Override
	protected String getTituloFrame() {
		return isDespesa ? "Cadastro de Despesas" : "Cadastro de Receitas";
	}

	@Override
	protected CriteriaBuilder getBuilderListagem() {
		return null;
	}

	@Override
	protected CriteriaBuilder getBuilderQntDados() {
		return null;
	}

	@Override
	protected JPanel getPanelCadastro() {
		return null;
	}

}
