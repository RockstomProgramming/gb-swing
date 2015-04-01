package br.com.finan.form;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import br.com.finan.annotation.PosSalvar;
import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.ContaBancariaDTO;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.util.AppUtil;

public class CadastroContaBancariaForm extends CadastroForm<ContaBancaria, ContaBancariaDTO> {

	private static final String TITULO_FRAME = "Cadastro de Contas Bancárias";
	private static final long serialVersionUID = 1L;
	private final JPanel pnlCadastro;

	public CadastroContaBancariaForm() {
		final JTextField txtDescricao = new JTextField(20);
		final JTextField txtNumero = new JTextField(10);
		final JTextField txtAgencia = new JTextField(5);

		pnlCadastro = new JPanel(new MigLayout());
		pnlCadastro.add(new JLabel("Descrição:"));
		pnlCadastro.add(txtDescricao, "wrap, spanx 3");
		pnlCadastro.add(new JLabel("Conta:"));
		pnlCadastro.add(txtNumero);
		pnlCadastro.add(new JLabel("Agência:"));
		pnlCadastro.add(txtAgencia);

		add(pnlCadastro, "growx");

		getBinding()
		.add(getComponentes().getTabela(), "${selectedElement.descricao}", txtDescricao)
		.add(getComponentes().getTabela(), "${selectedElement.agencia}", txtAgencia)
		.add(getComponentes().getTabela(), "${selectedElement.numero}", txtNumero)
		.add(this, "${entidade.descricao}", txtDescricao)
		.add(this, "${entidade.agencia}", txtAgencia)
		.add(this, "${entidade.numero}", txtNumero).getBindingGroup().bind();

		remove(getPnlFiltro());
	}

	@PosSalvar
	public void atualizarContasTelaPrincipal() {
		AppUtil.atualizarContas();
	}

	@Override
	protected JPanel getPanelCadastro() {
		return pnlCadastro;
	}

	@Override
	protected String getTituloFrame() {
		return TITULO_FRAME;
	}

	@Override
	protected void adicionarRestricoes(final CriteriaBuilder builder) {
		// TODO Auto-generated method stub

	}
}
