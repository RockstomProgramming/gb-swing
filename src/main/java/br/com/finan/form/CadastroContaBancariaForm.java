package br.com.finan.form;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import br.com.arq.form.CadastroForm;
import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.ContaBancariaDTO;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.util.HibernateUtil;

public class CadastroContaBancariaForm extends CadastroForm<ContaBancaria, ContaBancariaDTO> {

	private static final String TITULO_FRAME = "Cadastro de Contas Bancárias";
	private static final long serialVersionUID = 1L;
	private JPanel pnlCadastro;
	
	public CadastroContaBancariaForm() {
		JTextField txtDescricao = new JTextField(20);
		JTextField txtNumero = new JTextField(10);
		JTextField txtAgencia = new JTextField(5);

		pnlCadastro = new JPanel(new MigLayout());
		pnlCadastro.add(new JLabel("Descrição:"));
		pnlCadastro.add(txtDescricao, "wrap, spanx 3");
		pnlCadastro.add(new JLabel("Conta:"));
		pnlCadastro.add(txtNumero);
		pnlCadastro.add(new JLabel("Agência:"));
		pnlCadastro.add(txtAgencia);

		JPanel panel = new JPanel(new MigLayout("wrap 1"));
		panel.setPreferredSize(new Dimension(500, 400));
		panel.add(pnlCadastro, "growx");

		add(panel);

		getBinding()
			.add(tabela, "${selectedElement.descricao}", txtDescricao)
			.add(tabela, "${selectedElement.agencia}", txtAgencia)
			.add(tabela, "${selectedElement.numero}", txtNumero)
			.add(this, "${entidade.descricao}", txtDescricao)
			.add(this, "${entidade.agencia}", txtAgencia)
			.add(this, "${entidade.numero}", txtNumero).getBindingGroup().bind();
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
	protected CriteriaBuilder getBuilderListagem() {
		return getBuilderQntDados().addProjection("id", "descricao", "numero", "agencia").addAliasToBean(ContaBancariaDTO.class).close();
	}
	
	@Override
	protected CriteriaBuilder getBuilderQntDados() {
		return HibernateUtil.getCriteriaBuilder(ContaBancaria.class).eqStatusAtivo();
	}

}
