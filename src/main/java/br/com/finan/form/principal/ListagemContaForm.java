package br.com.finan.form.principal;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.beansbinding.BindingGroup;

import br.com.finan.dto.DTO;
import br.com.finan.entidade.Conta;
import br.com.finan.enumerator.Mes;
import br.com.finan.util.BindingUtil;

/**
 *
 * @author Wesley Luiz
 * @param <T>
 */
public abstract class ListagemContaForm<T extends DTO> extends ListagemForm<T> {

	private static final long serialVersionUID = 1L;

	protected static final String MASK_YEAR = "yyyy";

	private javax.swing.JButton btnMesAnterior;
	private javax.swing.JButton btnMesProximo;
	private javax.swing.JTextField txtAno;
	private javax.swing.JComboBox<Mes> cmbMes;

	private Mes mesSelecionado;
	private String ano;

	public ListagemContaForm() {
		mesSelecionado = Mes.JANEIRO;
		ano = new SimpleDateFormat(MASK_YEAR).format(new Date());

		initComponents();
		iniciarDados();
	}

	private void initComponents() {
		btnMesAnterior = new javax.swing.JButton();
		btnMesProximo = new javax.swing.JButton();
		cmbMes = new javax.swing.JComboBox<Mes>();
		txtAno = new javax.swing.JTextField(20);
		
		txtAno.setText(ano);
		btnMesAnterior.setText("<");
		btnMesProximo.setText(">");
		txtAno.setEnabled(false);
		cmbMes.setPreferredSize(new Dimension(150, 0));

		BindingGroup binding = new BindingGroup();
		BindingUtil.create(binding)
			.addJComboBoxBinding(Arrays.asList(Mes.values()), cmbMes)
			.add(this, "${mesSelecionado}", cmbMes, "selectedItem")
			.add(this, "${ano}", txtAno);

		JPanel pnlNav = new JPanel(new MigLayout());
		pnlNav.setBorder(new EtchedBorder());
		pnlNav.add(btnMesAnterior);
		pnlNav.add(cmbMes);
		pnlNav.add(btnMesProximo);
		pnlNav.add(txtAno);

		JPanel panel = new JPanel(new MigLayout());
		panel.add(pnlNav, "wrap, growx");
		panel.add(getPanelPaginacao(), "push, grow");

		add(panel);
		binding.bind();
		addAcoes();
		setClosable(true);
		setMaximizable(true);
		setResizable(true);

		pack();
	}

	private void addAcoes() {
		btnMesAnterior.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				irMesAnterior(cmbMes, txtAno);
			}
		});

		btnMesProximo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				irProximoMes(cmbMes, txtAno);
			}
		});
		
		cmbMes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniciarDados();
			}
		});
	}

	@Override
	protected String getNomeEntidade() {
		return Conta.class.getSimpleName();
	}

	protected void irMesAnterior(final JComboBox<Mes> txtMes, final JTextField txtAno) {
		final int mes = getMesSelecionado().getReferencia() - 1;
		navegar(mes, txtAno, txtMes);
	}

	protected void irProximoMes(final JComboBox<Mes> txtMes, final JTextField txtAno) {
		final int mes = getMesSelecionado().getReferencia() + 1;
		navegar(mes, txtAno, txtMes);
	}

	private void navegar(int mes, final JTextField txtAno, final JComboBox<Mes> txtMes) throws NumberFormatException {
		if (mes < 1) {
			mes = 12;
			final Integer a = Integer.valueOf(getAno()) - 1;
			txtAno.setText(a.toString());
		} else if (mes > 12) {
			mes = 1;
			final Integer a = Integer.valueOf(getAno()) + 1;
			txtAno.setText(a.toString());
		}

		txtMes.setSelectedItem(Mes.getMesPorReferencia(mes));
		buscarDados(1);
		validarBtnPaginacao();
	}

	public Mes getMesSelecionado() {
		return mesSelecionado;
	}

	public void setMesSelecionado(final Mes mesSelecionado) {
		this.mesSelecionado = mesSelecionado;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(final String ano) {
		this.ano = ano;
	}

}
