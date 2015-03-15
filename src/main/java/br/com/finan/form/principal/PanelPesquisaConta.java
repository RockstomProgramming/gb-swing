package br.com.finan.form.principal;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.beansbinding.BindingGroup;

import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.util.BindingUtil;

public class PanelPesquisaConta extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField txtDescricao;
	private JComboBox<Categoria> cmbCategoria;
	private JComboBox<ContaBancaria> cmbContaBancaria;
	private JButton btnCancelar;
	private JButton btnSelecionar;
	private PesquisaDTO pesquisaDTO;

	public PanelPesquisaConta() {
		setLayout(new MigLayout("wrap 2"));

		txtDescricao = new JTextField(20);
		cmbCategoria = new JComboBox<Categoria>();
		cmbContaBancaria = new JComboBox<ContaBancaria>();
		pesquisaDTO = new PesquisaDTO();

		add(new JLabel("Descrição:"));
		add(txtDescricao, "growx");
		add(new JLabel("Categoria:"));
		add(cmbCategoria, "growx");
		add(new JLabel("Conta Bancária:"));
		add(cmbContaBancaria, "growx");

		btnSelecionar = new JButton("Selecionar");
		btnCancelar = new JButton("Cancelar");

		JPanel pnlAcao = new JPanel(new MigLayout());
		pnlAcao.setBorder(new EtchedBorder());
		pnlAcao.add(btnSelecionar);
		pnlAcao.add(btnCancelar);

		BindingGroup bindingGroup = new BindingGroup();
		BindingUtil.create(bindingGroup)
			.add(this, "${pesquisaDTO.descricao}", txtDescricao)
			.add(this, "${pesquisaDTO.categoria}", cmbCategoria, "selectedItem")
			.add(this, "${pesquisaDTO.contaBancaria}", cmbContaBancaria, "selectedItem");

		bindingGroup.bind();

		add(pnlAcao, "growx, spanx 2");
	}

	class PesquisaDTO {
		private String descricao;
		private Categoria categoria;
		private ContaBancaria contaBancaria;

		private final PropertyChangeSupport property = new PropertyChangeSupport(
				this);

		public void addPropertyChangeListener(
				final PropertyChangeListener listener) {
			property.addPropertyChangeListener(listener);
		}

		public void removePropertyChangeListener(
				final PropertyChangeListener listener) {
			property.removePropertyChangeListener(listener);
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		public Categoria getCategoria() {
			return categoria;
		}

		public void setCategoria(Categoria categoria) {
			this.categoria = categoria;
		}

		public ContaBancaria getContaBancaria() {
			return contaBancaria;
		}

		public void setContaBancaria(ContaBancaria contaBancaria) {
			this.contaBancaria = contaBancaria;
		}
	}

	public JTextField getTxtDescricao() {
		return txtDescricao;
	}

	public JComboBox<Categoria> getCmbCategoria() {
		return cmbCategoria;
	}

	public JComboBox<ContaBancaria> getCmbContaBancaria() {
		return cmbContaBancaria;
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}

	public JButton getBtnSelecionar() {
		return btnSelecionar;
	}

	public PesquisaDTO getPesquisaDTO() {
		return pesquisaDTO;
	}

	public void setPesquisaDTO(PesquisaDTO pesquisaDTO) {
		this.pesquisaDTO = pesquisaDTO;
	}
}
