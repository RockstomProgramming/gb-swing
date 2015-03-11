package br.com.finan.form.despesa;

import br.com.finan.entidade.Conta;
import br.com.finan.entidade.enumerator.TipoConta;
import br.com.finan.form.principal.CadastroContaForm;

/**
 *
 * @author Wesley Luiz
 */
public class CadastroDespesaForm extends CadastroContaForm<Conta> {

	private static final long serialVersionUID = 1L;

	@Override
	protected void iniciarDados() {
		getEntidade().setTipo(TipoConta.DESPESA);
		super.iniciarDados();
	}
	
	@Override
	protected void iniciarComponentes() {
		setTitle(CadastroContaForm.TITULO_CAD_DESPESA);
		super.iniciarComponentes();
	}
}
