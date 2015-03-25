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
	private JComboBox<ContaBancaria> cmbContaBancaria;
	private JComboBox<FormaPagamento> cmbFormaPagamento;
	private JComboBox<Frequencia> cmbRecorrencia;
	private JTextField txtMaximo;
	private JPanel pnlCad;
	private JCheckBox txtPago;
	private JTextArea txtObservacoes;
	private PainelFiltro pnlNavegacao;
	
	private List<Categoria> categorias;
	private List<ContaBancaria> contasBancarias;

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
			cmbContaBancaria = new JComboBox<ContaBancaria>();
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
		pnlCad.add(new JLabel("Descrição:"));
		pnlCad.add(txtDescricao, "wrap, spanx2");
		pnlCad.add(new JLabel("Vencimento:"));
		pnlCad.add(txtVencimento);
		pnlCad.add(new JLabel("Valor (R$):"));
		pnlCad.add(txtValor, "wrap");
		pnlCad.add(new JLabel("Categoria:"));
		pnlCad.add(cmbCategoria, "grow");
		pnlCad.add(new JLabel("Conta Bancária:"));
		pnlCad.add(cmbContaBancaria, "grow, wrap");
		pnlCad.add(new JLabel("Pago:"));
		pnlCad.add(txtPago);
		pnlCad.add(new JLabel("Forma Pagamento:"));
		pnlCad.add(cmbFormaPagamento, "grow, wrap");
		pnlCad.add(new JLabel("Pagamento:"));
		pnlCad.add(txtPagamento);
		pnlCad.add(pnlRecorrencia, "wrap, growx, span 2 2");
		pnlCad.add(new JLabel("Observações:"));
		pnlCad.add(txtObservacoes, "wrap");

		add(pnlCad, "wrap");
		
		getBinding().addJComboBoxBinding(Arrays.asList(Frequencia.values()), cmbRecorrencia)
			.add(tabela, "${selectedElement == null}", pnlRecorrencia, "enabled")
			.add(tabela, "${selectedElement == null}", cmbRecorrencia, "enabled")
			.add(tabela, "${selectedElement == null}", txtMaximo, "enabled")
			.addJComboBoxBinding(Arrays.asList(FormaPagamento.values()), cmbFormaPagamento)
			.addJComboBoxBinding(categorias, cmbCategoria)
			.addJComboBoxBinding(contasBancarias, cmbContaBancaria)
			.add(this, "${entidade.categoria}", cmbCategoria, "selectedItem")
			.add(this, "${entidade.contaBancaria}", cmbContaBancaria, "selectedItem")
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
			
			.add(tabela, "${selectedElement.descricao}", txtDescricao)
			.add(tabela, "${selectedElement.observacoes}", txtObservacoes)
			.add(tabela, "${selectedElement.valor}", txtValor)
			.add(tabela, "${selectedElement.isPago}", txtPago, "selected")
			.add(tabela, "${selectedElement.vencimento}", txtVencimento, new DateConverter())
			.add(tabela, "${selectedElement.pagamento}", txtPagamento, new DateConverter())
			.getBindingGroup().bind();
		
		calcularContas();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void onGanharFoco() {
		this.categorias.clear();
		this.categorias.addAll(HibernateUtil.getCriteriaBuilder(Categoria.class).eqStatusAtivo().list());
	
		this.contasBancarias.clear();
		this.contasBancarias.addAll(HibernateUtil.getCriteriaBuilder(ContaBancaria.class).eqStatusAtivo().list());
	}
	
	@Override
	protected void popularInterface(Long idSelecionado) {
		super.popularInterface(idSelecionado);
		if (ObjetoUtil.isReferencia(getEntidade())) {
			txtPago.setSelected(getEntidade().isIsPago());
			txtObservacoes.setText(getEntidade().getObservacoes());
			cmbCategoria.setSelectedItem(getEntidade().getCategoria());
			cmbContaBancaria.setSelectedItem(getEntidade().getContaBancaria());
			cmbFormaPagamento.setSelectedItem(getEntidade().getFormaPagamento());
		}
	}
	
	protected String getAnoSelecionado() {
		return ObjetoUtil.isReferencia(getPnlNavegacao()) ? getPnlNavegacao().getAno() : new SimpleDateFormat("yyyy").format(new Date());
	}

	protected Integer getMesSelecionado() {
		return ObjetoUtil.isReferencia(getPnlNavegacao()) ? getPnlNavegacao().getMesSelecionado().getReferencia() : 1;
	}
	
	@PostLoadTable
	public void calcularContas() {
		if (ObjetoUtil.isReferencia(getPnlNavegacao())) {
			BigDecimal qntAberto = (BigDecimal) getBuilderRel().eq("isPago", false)
					.getCriteria().setProjection(Projections.sum("valor")).uniqueResult();
	
			BigDecimal qntPago = (BigDecimal) getBuilderRel().eq("isPago", true)
					.getCriteria().setProjection(Projections.sum("valor")).uniqueResult();
			
			BigDecimal qntTotal = (BigDecimal) getBuilderRel()
					.getCriteria().setProjection(Projections.sum("valor")).uniqueResult();
			
			getPnlNavegacao().getLbQntAberto().setText(NumberUtil.obterNumeroFormatado(qntAberto));
			getPnlNavegacao().getLbQntPago().setText(NumberUtil.obterNumeroFormatado(qntPago));
			getPnlNavegacao().getLbTotal().setText(NumberUtil.obterNumeroFormatado(qntTotal));
		}
	}
	
	private CriteriaBuilder getBuilderRel() {
		CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder(Conta.class).eqStatusAtivo()
				.eq("tipo", getClass().getSimpleName().equals(CadastroDespesaForm.class.getSimpleName()) ? TipoConta.DESPESA : TipoConta.RECEITA);
		return builder;
	}
	
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
					HibernateUtil.salvar(conta);
				} catch (IllegalAccessException | InvocationTargetException ex) {
					Logger.getLogger(CadastroContaForm.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
	
			AppUtil.exibirMsgSalvarSucesso(this);
		}
		
		iniciarDados();
		getContaService().atualizarSaldoFramePrincipal();
	}
	
	public class PainelFiltro {

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
		
		public PainelFiltro() {
			mesSelecionado = Mes.JANEIRO;
			ano = new SimpleDateFormat(MASK_YEAR).format(new Date());

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
			pnlNav.add(btnMesAnterior);
			pnlNav.add(cmbMes);
			pnlNav.add(btnMesProximo);
			pnlNav.add(txtAno, "pushx");
			
			JPanel pnlRel = new JPanel(new MigLayout());
			pnlRel.add(addBoldLabel(new JLabel("Em Aberto:")));
			pnlRel.add(lbQntAberto, "gapright 20");
			pnlRel.add(addBoldLabel(new JLabel("Pago:")));
			pnlRel.add(lbQntPago, "gapright 20");
			pnlRel.add(addBoldLabel(new JLabel("Total:")));
			pnlRel.add(lbTotal);
			
			pnlFiltro.add(pnlNav, "wrap, growx");
			pnlFiltro.add(pnlRel, "wrap, growx");
			
			binding.bind();
			addAcoes();
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
			setPagina(1);
			buscarDados(0);
		}

		public Mes getMesSelecionado() {
			return mesSelecionado;
		}

		public void setMesSelecionado(Mes mesSelecionado) {
			this.mesSelecionado = mesSelecionado;
		}

		public String getAno() {
			return ano;
		}

		public void setAno(String ano) {
			this.ano = ano;
		}
		
		public JLabel getLbQntAberto() {
			return lbQntAberto;
		}
		
		public void setLbQntAberto(JLabel lbQntAberto) {
			this.lbQntAberto = lbQntAberto;
		}

		public JLabel getLbQntPago() {
			return lbQntPago;
		}

		public void setLbQntPago(JLabel lbQntPago) {
			this.lbQntPago = lbQntPago;
		}
		
		public JLabel getLbTotal() {
			return lbTotal;
		}

		public void setLbTotal(JLabel lbTotal) {
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

	public void setLimite(int limite) {
		this.limite = limite;
	}

	public Frequencia getRecorrencia() {
		return recorrencia;
	}

	public void setRecorrencia(Frequencia recorrencia) {
		this.recorrencia = recorrencia;
	}

	public PainelFiltro getPnlNavegacao() {
		return pnlNavegacao;
	}
}
