package br.com.finan.form.categoria;

import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.beansbinding.BindingGroup;

import br.com.finan.entidade.Categoria;
import br.com.finan.form.principal.CadastroForm;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.HibernateUtil;

public class CadastroCategoriaForm extends CadastroForm<Categoria> {

	private static final String TITULO_FRAME = "Cadastro de Categorias";
	private static final long serialVersionUID = 1L;
	private JTextField txtNome;
	private JComboBox<Categoria> cmbSupCategoria;
	private JPanel pnlCadastro;
	private ListagemCategoriaForm lstCategoriaForm;
	
	@Override
	protected void iniciarComponentes() {
		txtNome = new JTextField(20);
		cmbSupCategoria = new JComboBox<Categoria>();
		lstCategoriaForm = new ListagemCategoriaForm();

		JPanel panel = new JPanel(new MigLayout("wrap 1"));
		panel.setPreferredSize(new Dimension(800, 400));
		
		pnlCadastro = new JPanel(new MigLayout());
		pnlCadastro.add(new JLabel("Nome:"));
		pnlCadastro.add(txtNome, "wrap");
		pnlCadastro.add(new JLabel("Categoria:"));
		pnlCadastro.add(cmbSupCategoria, "wrap, growx");
		
		panel.add(pnlCadastro, "growx");
		panel.add(getPanelAcao(), "growx");

		add(panel);
		addBinding().bind();
		setTitle(TITULO_FRAME);
		setClosable(true);
		pack();
		
		panel.add(lstCategoriaForm.getPanelPaginacao());
		pack();
	}
	
	@Override
	protected void salvar() {
		super.salvar();
		lstCategoriaForm.iniciarDados();
	}

	private BindingGroup addBinding() {
		BindingGroup bindingGroup = new BindingGroup();
		BindingUtil.create(bindingGroup)
			.addJComboBoxBinding(HibernateUtil.getCriteriaBuilder(Categoria.class).eqStatusAtivo().list(), cmbSupCategoria)
			.add(this, "${entidade.superCategoria}", cmbSupCategoria, "selectedItem")
			.add(this, "${entidade.nome}", txtNome);
		return bindingGroup;
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
