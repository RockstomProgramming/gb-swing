package br.com.finan.form.principal;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.beansbinding.BindingGroup;

import br.com.finan.dto.ContaFiltroDTO;
import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.HibernateUtil;

public class DialogPesquisaConta extends DialogPesquisa<ContaFiltroDTO> {

	private static final long serialVersionUID = 1L;

	private final JTextField txtDescricao;
	private final JComboBox<Categoria> cmbCategoria;
	private final JComboBox<ContaBancaria> cmbContaBancaria;

	public DialogPesquisaConta() {
		txtDescricao = new JTextField(20);
		cmbCategoria = new JComboBox<Categoria>();
		cmbContaBancaria = new JComboBox<ContaBancaria>();

		final JPanel pnlFiltro = new JPanel(new MigLayout("wrap 2"));
		pnlFiltro.add(new JLabel("Descrição:"));
		pnlFiltro.add(txtDescricao, "growx");
		pnlFiltro.add(new JLabel("Categoria:"));
		pnlFiltro.add(cmbCategoria, "growx");
		pnlFiltro.add(new JLabel("Conta Bancária:"));
		pnlFiltro.add(cmbContaBancaria, "growx");

		final BindingGroup bindingGroup = new BindingGroup();
		BindingUtil.create(bindingGroup)
		.addJComboBoxBinding(HibernateUtil.getCriteriaBuilder(Categoria.class).eqStatusAtivo().list(), cmbCategoria)
		.addJComboBoxBinding(HibernateUtil.getCriteriaBuilder(ContaBancaria.class).eqStatusAtivo().list(), cmbContaBancaria)
		.add(this, "${filtro.descricao}", txtDescricao)
		.add(this, "${filtro.categoria}", cmbCategoria, "selectedItem")
		.add(this, "${filtro.conta}", cmbContaBancaria, "selectedItem");

		bindingGroup.bind();

		add(pnlFiltro, "wrap");
		add(pnlAcao, "growx");

		pack();
	}
}
