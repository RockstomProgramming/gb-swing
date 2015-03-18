package br.com.finan.form;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.beansbinding.BindingGroup;

import net.miginfocom.swing.MigLayout;

import br.com.finan.entidade.ContaBancaria;
import br.com.finan.form.principal.CadastroForm;
import br.com.finan.util.BindingUtil;
import br.com.finan.validator.MaxLengthValidator;

public class CadastroContaBancariaForm extends CadastroForm<ContaBancaria> {

	private static final String TITULO_FRAME = "Cadastro de Contas Bancárias";
	private static final long serialVersionUID = 1L;
	private JPanel pnlCadastro;
	private JTextField txtDescricao;
	private JTextField txtNumero;
	private JTextField txtAgencia;
	private ListagemContaBancariaForm lstContaForm;
	
	@Override
	protected void iniciarComponentes() {
		txtDescricao = new JTextField(20);
		txtNumero = new JTextField(10);
		txtAgencia = new JTextField(5);
		lstContaForm = new ListagemContaBancariaForm();
		
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
		panel.add(getPanelAcao(), "growx");
		
		BindingGroup bindingGroup = new BindingGroup();
		BindingUtil.create(bindingGroup)
			.add(this, "${entidade.descricao}", txtDescricao, new MaxLengthValidator(20))
			.add(this, "${entidade.numero}", txtNumero, new MaxLengthValidator(20))
			.add(this, "${entidade.agencia}", txtAgencia, new MaxLengthValidator(10));
		
		bindingGroup.bind();
		add(panel);
		setClosable(true);
		setTitle(TITULO_FRAME);
		pack();
		
		panel.add(lstContaForm.getPanelPaginacao(), "growx");
		pack();
	}
	
	@Override
	protected void salvar() {
		super.salvar();
		lstContaForm.iniciarDados();
	}

	@Override
	protected JInternalFrame getFrame() {
		return this;
	}

	@Override
	protected JPanel getContainerCadastro() {
		return pnlCadastro;
	}

}
