package br.com.finan.form;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.criterion.Projections;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.observablecollections.ObservableCollections;

import br.com.finan.annotation.OnGanharFoco;
import br.com.finan.annotation.PostLoadTable;
import br.com.finan.component.JMoneyField;
import br.com.finan.converter.BigDecimalConverter;
import br.com.finan.converter.DateConverter;
import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.ContaDTO;
import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.Conta;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.enumerator.FormaPagamento;
import br.com.finan.enumerator.Frequencia;
import br.com.finan.enumerator.Mes;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.util.AppUtil;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.CalcularRecorrencia;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.NumberUtil;
import br.com.finan.util.ObjetoUtil;
import br.com.finan.validator.IntegerValidator;
import br.com.finan.validator.MaxLengthValidator;

public abstract class CadastroContaForm<T extends Conta, D extends ContaDTO> extends CadastroForm<T, D> {

	private static final long serialVersionUID = 1L;

	public static final String TITULO_CAD_RECEITA = "Cadastro de Receita";
	public static final String TITULO_CAD_DESPESA = "Cadastro de Despesa";

	private JTextField txtDescricao;
	private JFormattedTextField txtVencimento;
	private JFormattedTextField txtPagamento;
	private JMoneyField txtValor;
	private JComboBox<Categoria> cmbCategoria;
	private JComboBox<FormaPagamento> cmbFormaPagamento;
	private JComboBox<Frequencia> cmbRecorrencia;
	private JTextField txtMaximo;
	private final JPanel pnlCad;
	private JCheckBox txtPago;
	private JTextArea txtObservacoes;
	private final PainelFiltro pnlNavegacao;

	private final List<Categoria> categorias;
	private final List<ContaBancaria> contasBancarias;

	private int limite = 1;
	private Frequencia recorrencia = Frequencia.MENSAL;

	public CadastroContaForm() {
		pnlNavegacao = new PainelFiltro();
		categorias = ObservableCollections.observableList(new ArrayList<Categoria>());
		contasBancarias = ObservableCollections.observableList(new ArrayList<ContaBancaria>());

		try {
			txtDescricao = new JTextField(20);
			txtVencimento = new JFormattedTextField(new MaskFormatter("##/##/####"));
			txtPagamento = new JFormattedTextField(new MaskFormatter("##/##/####"));
			txtValor = new JMoneyField();
			cmbCategoria = new JComboBox<Categoria>();
			cmbFormaPagamento = new JComboBox<FormaPagamento>();
			cmbRecorrencia = new JComboBox<Frequencia>();
			txtMaximo = new JTextField(10);
			txtPago = new JCheckBox("Sim");
			txtObservacoes = new JTextArea(5, 15);

			txtVencimento.setColumns(10);
			txtPagamento.setColumns(10);
			txtValor.setColumns(10);
		} catch (final ParseException ex) {
			Logger.getLogger(CadastroContaForm.class.getName()).log(Level.SEVERE, null, ex);
		}

		final JPanel pnlRecorrencia = new JPanel(new MigLayout());
		pnlRecorrencia.setBorder(new TitledBorder(new EtchedBorder(), "Repetir lançamento"));
		pnlRecorrencia.add(new JLabel("Recorrência:"));
		pnlRecorrencia.add(cmbRecorrencia, "wrap, grow");
		pnlRecorrencia.add(new JLabel("Limite:"));
		pnlRecorrencia.add(txtMaximo);

		pnlCad = new JPanel(new MigLayout());
		pnlCad.setBorder(new EtchedBorder());
		pnlCad.add(new JLabel("Descrição:"));
		pnlCad.add(txtDescricao);
		pnlCad.add(pnlRecorrencia, "wrap, spany4");
		pnlCad.add(new JLabel("Vencimento:"));
		pnlCad.add(txtVencimento, "wrap");
		pnlCad.add(new JLabel("Valor (R$):"));
		pnlCad.add(txtValor, "wrap");
		pnlCad.add(new JLabel("Categoria:"));
		pnlCad.add(cmbCategoria, "wrap");
		pnlCad.add(new JLabel("Pago:"));
		pnlCad.add(txtPago, "wrap");
		pnlCad.add(new JLabel("Forma Pagamento:"));
		pnlCad.add(cmbFormaPagamento, "wrap");
		pnlCad.add(new JLabel("Pagamento:"));
		pnlCad.add(txtPagamento, "wrap");
		pnlCad.add(new JLabel("Observações:"));
		pnlCad.add(txtObservacoes, "growx");

		add(pnlCad, "growx");

		getBinding().addJComboBoxBinding(Arrays.asList(Frequencia.values()), cmbRecorrencia)
		.add(getComponentes().getTabela(), "${selectedElement == null}", pnlRecorrencia, "enabled")
		.add(getComponentes().getTabela(), "${selectedElement == null}", cmbRecorrencia, "enabled")
		.add(getComponentes().getTabela(), "${selectedElement == null}", txtMaximo, "enabled")
		.addJComboBoxBinding(Arrays.asList(FormaPagamento.values()), cmbFormaPagamento)
		.addJComboBoxBinding(categorias, cmbCategoria)
		.add(this, "${entidade.categoria}", cmbCategoria, "selectedItem")
		.add(this, "${entidade.formaPagamento}", cmbFormaPagamento, "selectedItem")
		.add(this, "${entidade.descricao}", txtDescricao, new MaxLengthValidator(50))
		.add(this, "${entidade.dataVencimento}", txtVencimento, new DateConverter())
		.add(this, "${entidade.dataPagamento}", txtPagamento, new DateConverter())
		.add(this, "${entidade.valor}", txtValor, new BigDecimalConverter())
		.add(this, "${entidade.isPago}", txtPago, "selected")
		.add(this, "${entidade.observacoes}", txtObservacoes, new MaxLengthValidator(225))
		.add(this, "${recorrencia}", cmbRecorrencia, "selectedItem")
		.add(this, "${limite}", txtMaximo, new IntegerValidator(4))
		.add(txtPago, "${selected}", txtPagamento, "enabled")
		.add(txtPago, "${selected}", cmbFormaPagamento, "enabled")
		.add(getComponentes().getTabela(), "${selectedElement.descricao}", txtDescricao)
		.add(getComponentes().getTabela(), "${selectedElement.observacoes}", txtObservacoes)
		.add(getComponentes().getTabela(), "${selectedElement.valor}", txtValor)
		.add(getComponentes().getTabela(), "${selectedElement.isPago}", txtPago, "selected")
		.add(getComponentes().getTabela(), "${selectedElement.vencimento}", txtVencimento, new DateConverter())
		.add(getComponentes().getTabela(), "${selectedElement.pagamento}", txtPagamento, new DateConverter())
		.getBindingGroup().bind();

	}

	@OnGanharFoco
	public void iniciarListas() {
		this.categorias.clear();
		this.categorias.add(null);
		this.categorias.addAll(getCategoriaService().obterCategorias());

		this.contasBancarias.clear();
		this.contasBancarias.addAll(getContaBancariaService().obterContasBancarias());
	}

	@Override
	protected void popularInterface(final Long idSelecionado) {
		super.popularInterface(idSelecionado);
		if (ObjetoUtil.isReferencia(getEntidade())) {
			txtPago.setSelected(getEntidade().isIsPago());
			txtObservacoes.setText(getEntidade().getObservacoes());
			cmbCategoria.setSelectedItem(getEntidade().getCategoria());
			cmbFormaPagamento.setSelectedItem(getEntidade().getFormaPagamento());
		}
	}

	protected String getAnoSelecionado() {
		return ObjetoUtil.isReferencia(getPnlNavegacao()) ? getPnlNavegacao().getAno() : new SimpleDateFormat("yyyy").format(new Date());
	}

	protected Integer getMesSelecionado() {
		return ObjetoUtil.isReferencia(getPnlNavegacao()) ? getPnlNavegacao().getMesSelecionado().getReferencia() : getMesAtual().getReferencia();
	}

	@PostLoadTable
	public void calcularContas() {
		if (ObjetoUtil.isReferencia(getPnlNavegacao())) {
			final BigDecimal qntAberto = (BigDecimal) getBuilderRel().eq("isPago", false)
					.getCriteria().setProjection(Projections.sum("valor")).uniqueResult();

			final BigDecimal qntPago = (BigDecimal) getBuilderRel().eq("isPago", true)
					.getCriteria().setProjection(Projections.sum("valor")).uniqueResult();

			final BigDecimal qntTotal = (BigDecimal) getBuilderRel()
					.getCriteria().setProjection(Projections.sum("valor")).uniqueResult();

			getPnlNavegacao().getLbQntAberto().setText(NumberUtil.obterNumeroFormatado(qntAberto));
			getPnlNavegacao().getLbQntPago().setText(NumberUtil.obterNumeroFormatado(qntPago));
			getPnlNavegacao().getLbTotal().setText(NumberUtil.obterNumeroFormatado(qntTotal));
		}
	}

	private CriteriaBuilder getBuilderRel() {
		final CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder(Conta.class).eqStatusAtivo()
				.eq("tipo", getClass().getSimpleName().equals(CadastroDespesaForm.class.getSimpleName()) ? TipoConta.DESPESA : TipoConta.RECEITA)
				.sqlRestrictions("MONTH(dataVencimento) = " + getMesSelecionado())
				.sqlRestrictions("YEAR(dataVencimento) = " + getAnoSelecionado());
		return builder;
	}

	private Mes getMesAtual() {
		return Mes.getMesPorReferencia(Integer.valueOf(new SimpleDateFormat("MM").format(new Date())));
	}

	@Override
	protected void salvar() {

		if (ObjetoUtil.isReferencia(getEntidade().getId())) {

			HibernateUtil.alterar(getEntidade());
			AppUtil.exibirMsgAlterarSucesso(this);

		} else {

			final List<Date> vencimentos = new CalcularRecorrencia(getRecorrencia(), ((Conta) getEntidade()).getDataVencimento(), getLimite()).obterVencimentos();

			for (int i = 0; i < getLimite(); i++) {
				try {
					final Conta conta = new Conta();
					BeanUtils.copyProperties(conta, getEntidade());
					conta.setParcela(i + 1);
					conta.setTotalParcelas(getLimite());
					conta.setDataVencimento(vencimentos.get(i));
					conta.setContaBancaria(AppUtil.getContaSelecionada());
					HibernateUtil.salvar(conta);
				} catch (IllegalAccessException | InvocationTargetException ex) {
					Logger.getLogger(CadastroContaForm.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

			AppUtil.exibirMsgSalvarSucesso(this);
		}

		iniciarDados();
		AppUtil.atualizarSaldoFramePrincipal();
	}

	public class PainelFiltro {

		protected static final String MASK_YEAR = "yyyy";

		private final JButton btnMesAnterior;
		private final JButton btnMesProximo;
		private final JTextField txtAno;
		private final JComboBox<Mes> cmbMes;
		private JLabel lbQntAberto;
		private JLabel lbQntPago;
		private JLabel lbTotal;

		private Mes mesSelecionado;
		private String ano;

		public PainelFiltro() {
			mesSelecionado = getMesAtual();
			ano = new SimpleDateFormat(MASK_YEAR).format(new Date());

			btnMesAnterior = new JButton();
			btnMesProximo = new JButton();
			cmbMes = new JComboBox<Mes>();
			txtAno = new JTextField(10);
			lbQntAberto = new JLabel();
			lbQntPago = new JLabel();
			lbTotal = new JLabel();

			txtAno.setText(ano);
			txtAno.setEnabled(false);
			cmbMes.setPreferredSize(new Dimension(150, 0));

			final BindingGroup binding = new BindingGroup();
			BindingUtil.create(binding)
			.addJComboBoxBinding(Arrays.asList(Mes.values()), cmbMes)
			.add(this, "${mesSelecionado}", cmbMes, "selectedItem")
			.add(this, "${ano}", txtAno);

			final JPanel pnlNav = new JPanel(new MigLayout());
			pnlNav.add(btnMesAnterior);
			pnlNav.add(cmbMes);
			pnlNav.add(btnMesProximo);
			pnlNav.add(txtAno, "pushx");

			final JPanel pnlRel = new JPanel(new MigLayout());
			pnlRel.add(addBoldLabel(new JLabel("Em Aberto:")));
			pnlRel.add(lbQntAberto, "gapright 20");
			pnlRel.add(addBoldLabel(new JLabel("Pago:")));
			pnlRel.add(lbQntPago, "gapright 20");
			pnlRel.add(addBoldLabel(new JLabel("Total:")));
			pnlRel.add(lbTotal);

			getPnlFiltro().add(pnlNav, "wrap, growx");
			getPnlFiltro().add(pnlRel, "wrap, growx");

			binding.bind();
			addAcoes();
		}

		private JLabel addBoldLabel(final JLabel label) {
			label.setFont(new Font(null, Font.BOLD, 12));
			return label;
		}

		private void addAcoes() {
			btnMesAnterior.setIcon(new ImageIcon(getClass().getResource("/icon/Symbol_Play_Reversed.png")));
			btnMesAnterior.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent evt) {
					irMesAnterior(cmbMes, txtAno);
				}
			});

			btnMesProximo.setIcon(new ImageIcon(getClass().getResource("/icon/Symbol_Play.png")));
			btnMesProximo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent evt) {
					irProximoMes(cmbMes, txtAno);
				}
			});

			cmbMes.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					//					iniciarDados();
				}
			});
		}

		protected void irMesAnterior(final JComboBox<Mes> txtMes, final JTextField txtAno) {
			final int mes = getMesSelecionado().getReferencia() - 1;
			navegar(mes, txtAno, txtMes);
		}

		protected void irProximoMes(final JComboBox<Mes> txtMes, final JTextField txtAno) {
			final int mes = getMesSelecionado().getReferencia() + 1;
			navegar(mes, txtAno, txtMes);
		}

		private void navegar(final int mes, final JTextField txtAno, final JComboBox<Mes> txtMes) throws NumberFormatException {
			Mes ref;
			if (mes < 1) {
				ref = Mes.getMesPorReferencia(12);
				final Integer a = Integer.valueOf(getAno()) - 1;
				txtAno.setText(a.toString());
			} else if (mes > 12) {
				ref = Mes.getMesPorReferencia(1);
				final Integer a = Integer.valueOf(getAno()) + 1;
				txtAno.setText(a.toString());
			} else {
				ref = Mes.getMesPorReferencia(mes);
			}

			txtMes.setSelectedItem(ref);
			setPagina(1);
			buscarDados(0);
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

		public JLabel getLbQntAberto() {
			return lbQntAberto;
		}

		public void setLbQntAberto(final JLabel lbQntAberto) {
			this.lbQntAberto = lbQntAberto;
		}

		public JLabel getLbQntPago() {
			return lbQntPago;
		}

		public void setLbQntPago(final JLabel lbQntPago) {
			this.lbQntPago = lbQntPago;
		}

		public JLabel getLbTotal() {
			return lbTotal;
		}

		public void setLbTotal(final JLabel lbTotal) {
			this.lbTotal = lbTotal;
		}
	}

	@Override
	protected JPanel getPanelCadastro() {
		return pnlCad;
	}

	public int getLimite() {
		return limite;
	}

	public void setLimite(final int limite) {
		this.limite = limite;
	}

	public Frequencia getRecorrencia() {
		return recorrencia;
	}

	public void setRecorrencia(final Frequencia recorrencia) {
		this.recorrencia = recorrencia;
	}

	public PainelFiltro getPnlNavegacao() {
		return pnlNavegacao;
	}
}
