package br.com.finan.form;

import br.com.finan.entidade.Conta;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.form.principal.CadastroContaForm;

/**
 *
 * @author Wesley Luiz
 */
public class CadastroDespesaForm extends CadastroContaForm<Conta> {

	private static final long serialVersionUID = 1L;

	@Override
	protected void iniciarDados() {
		super.iniciarDados();
		getEntidade().setTipo(TipoConta.DESPESA);
	}
	
	@Override
	protected void iniciarComponentes() {
		setTitle(CadastroContaForm.TITULO_CAD_DESPESA);
		super.iniciarComponentes();
	}
}
