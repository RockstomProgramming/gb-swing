package br.com.finan.form.principal;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.hibernate.criterion.Projections;
import org.jdesktop.beansbinding.BindingGroup;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.ContaFiltroDTO;
import br.com.finan.dto.DTO;
import br.com.finan.entidade.Conta;
import br.com.finan.enumerator.Mes;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.form.despesa.ListagemDespesaForm;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.NumberUtil;

/**
 *
 * @author Wesley Luiz
 * @param <T>
 */
public abstract class ListagemContaForm<T extends DTO> extends ListagemForm<T> {

	private static final long serialVersionUID = 1L;

	protected static final String MASK_YEAR = "yyyy";

	private JButton btnMesAnterior;
	private JButton btnMesProximo;
	private JTextField txtAno;
	private JComboBox<Mes> cmbMes;
	private JLabel lbQntAberto;
	private JLabel lbQntPago;
	private JLabel lbTotal;

	private Mes mesSelecionado;
	private String ano;

	public ListagemContaForm() {
		mesSelecionado = Mes.JANEIRO;
		ano = new SimpleDateFormat(MASK_YEAR).format(new Date());

		initComponents();
		iniciarDados();
	}

	private void initComponents() {
		btnMesAnterior = new JButton();
		btnMesProximo = new JButton();
		cmbMes = new JComboBox<Mes>();
		txtAno = new JTextField(20);
		lbQntAberto = new JLabel();
		lbQntPago = new JLabel();
		lbTotal = new JLabel();
		
		txtAno.setText(ano);
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
		pnlNav.add(txtAno, "pushx");
		pnlNav.add(btnPesquisar);

		JPanel pnlRel = new JPanel(new MigLayout());
		pnlRel.setBorder(new EtchedBorder());
		pnlRel.add(addBoldLabel(new JLabel("Em Aberto:")));
		pnlRel.add(lbQntAberto, "gapright 20");
		pnlRel.add(addBoldLabel(new JLabel("Pago:")));
		pnlRel.add(lbQntPago, "gapright 20");
		pnlRel.add(addBoldLabel(new JLabel("Total:")));
		pnlRel.add(lbTotal);

		JPanel panel = new JPanel(new MigLayout());
		panel.add(pnlNav, "wrap, growx");
		panel.add(pnlRel, "wrap, growx");
		panel.add(getPanelPaginacao(), "push, grow");

		add(panel);
		binding.bind();
		addAcoes();
		setClosable(true);
		setMaximizable(true);
		setResizable(true);

		pack();
	}

	private JLabel addBoldLabel(JLabel label) {
		label.setFont(new Font(null, Font.BOLD, 12));
		return label;
	}

	private void addAcoes() {
		btnMesAnterior.setIcon(new ImageIcon(getClass().getResource("/icon/Symbol_Play_Reversed.png")));
		btnMesAnterior.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				irMesAnterior(cmbMes, txtAno);
			}
		});

		btnMesProximo.setIcon(new ImageIcon(getClass().getResource("/icon/Symbol_Play.png")));
		btnMesProximo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				irProximoMes(cmbMes, txtAno);
			}
		});
		
		cmbMes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				iniciarDados();
			}
		});
	}
	
	@Override
	protected Map<String, Object> getMapaFiltros() {
		ContaFiltroDTO filtro = (ContaFiltroDTO) modalPesquisa.getFiltro();
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("descricao", filtro.getDescricao());
		mp.put("categoria.id", filtro.getCategoria());
		mp.put("contaBancaria.id", filtro.getConta());
		return mp;
	}
	
	@Override
	protected Class<DialogPesquisaConta> getModalPesquisa() {
		return DialogPesquisaConta.class;
	}

	@Override
	protected Class<Conta> getEntidade() {
		return Conta.class;
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
	
	@Override
	protected void buscarDados(int primResultado) {
		super.buscarDados(primResultado);
		
		BigDecimal qntAberto = (BigDecimal) getBuilderRel().eq("isPago", false)
				.getCriteria().setProjection(Projections.sum("valor")).uniqueResult();

		BigDecimal qntPago = (BigDecimal) getBuilderRel().eq("isPago", true)
				.getCriteria().setProjection(Projections.sum("valor")).uniqueResult();
		
		BigDecimal qntTotal = (BigDecimal) getBuilderRel()
				.getCriteria().setProjection(Projections.sum("valor")).uniqueResult();
		
		lbQntAberto.setText(NumberUtil.obterNumeroFormatado(qntAberto));
		lbQntPago.setText(NumberUtil.obterNumeroFormatado(qntPago));
		lbTotal.setText(NumberUtil.obterNumeroFormatado(qntTotal));
	}

	private CriteriaBuilder getBuilderRel() {
		CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder(Conta.class).eqStatusAtivo()
				.eq("tipo", getClass().getSimpleName().equals(ListagemDespesaForm.class.getSimpleName()) ? TipoConta.DESPESA : TipoConta.RECEITA);
		montarRestricaoFiltro(builder);
		return builder;
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
