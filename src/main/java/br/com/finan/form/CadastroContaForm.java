package br.com.finan.form;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import br.com.arq.form.CadastroForm;
import br.com.finan.component.JMoneyField;
import br.com.finan.converter.BigDecimalConverter;
import br.com.finan.converter.DateConverter;
import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dao.Criterion;
import br.com.finan.dto.ContaDTO;
import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.Conta;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.enumerator.FormaPagamento;
import br.com.finan.enumerator.Frequencia;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.util.AppUtil;
import br.com.finan.util.CalcularRecorrencia;
import br.com.finan.util.HibernateUtil;
import br.com.finan.validator.IntegerValidator;
import br.com.finan.validator.MaxLengthValidator;

public class CadastroContaForm extends CadastroForm<Conta, ContaDTO> {

	private static final long serialVersionUID = 1L;
	private boolean isDespesa;
	
	public static final String TITULO_CAD_RECEITA = "Cadastro de Receita";
	public static final String TITULO_CAD_DESPESA = "Cadastro de Despesa";

	private JTextField txtDescricao;
	private JFormattedTextField txtVencimento;
	private JFormattedTextField txtPagamento;
	private JMoneyField txtValor;
	private JComboBox<Categoria> txtCategoria;
	private JComboBox<ContaBancaria> txtContaBancaria;
	private JComboBox<FormaPagamento> txtFormaPagamento;
	private JComboBox<Frequencia> txtRecorrencia;
	private JTextField txtMaximo;
	private JPanel pnlCad;
	private JCheckBox txtPago;
	private JTextArea txtObservacoes;

	private int limite = 1;
	private Frequencia recorrencia = Frequencia.MENSAL;
	
	public CadastroContaForm(boolean isDespesa) {
		this.isDespesa = isDespesa;
		
		try {
			txtDescricao = new JTextField(20);
			txtVencimento = new JFormattedTextField(new MaskFormatter("##/##/####"));
			txtPagamento = new JFormattedTextField(new MaskFormatter("##/##/####"));
			txtValor = new JMoneyField();
			txtCategoria = new JComboBox<Categoria>();
			txtContaBancaria = new JComboBox<ContaBancaria>();
			txtFormaPagamento = new JComboBox<FormaPagamento>();
			txtRecorrencia = new JComboBox<Frequencia>();
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
		pnlRecorrencia.add(txtRecorrencia, "wrap, grow");
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
		pnlCad.add(txtCategoria, "grow");
		pnlCad.add(new JLabel("Conta Bancária:"));
		pnlCad.add(txtContaBancaria, "grow, wrap");
		pnlCad.add(new JLabel("Pago:"));
		pnlCad.add(txtPago);
		pnlCad.add(new JLabel("Forma Pagamento:"));
		pnlCad.add(txtFormaPagamento, "grow, wrap");
		pnlCad.add(new JLabel("Pagamento:"));
		pnlCad.add(txtPagamento);
		pnlCad.add(pnlRecorrencia, "wrap, growx, span 2 2");
		pnlCad.add(new JLabel("Observações:"));
		pnlCad.add(txtObservacoes, "wrap");
//		pnlCad.add(getPanelAcao(), "growx, spanx 4, gapbottom 10");

		add(pnlCad);
		
		getBinding().addJComboBoxBinding(Arrays.asList(Frequencia.values()), txtRecorrencia)
			.addJComboBoxBinding(Arrays.asList(FormaPagamento.values()), txtFormaPagamento)
			.addJComboBoxBinding(HibernateUtil.getCriteriaBuilder(Categoria.class).eqStatusAtivo().list(), txtCategoria)
			.addJComboBoxBinding(HibernateUtil.getCriteriaBuilder(ContaBancaria.class).eqStatusAtivo().list(), txtContaBancaria)
			.add(this, "${entidade.categoria}", txtCategoria, "selectedItem")
			.add(this, "${entidade.contaBancaria}", txtContaBancaria, "selectedItem")
			.add(this, "${entidade.formaPagamento}", txtFormaPagamento, "selectedItem")
			.add(this, "${entidade.descricao}", txtDescricao, new MaxLengthValidator(50))
			.add(this, "${entidade.dataVencimento}", txtVencimento, new DateConverter())
			.add(this, "${entidade.dataPagamento}", txtPagamento, new DateConverter())
			.add(this, "${entidade.valor}", txtValor, new BigDecimalConverter())
			.add(this, "${entidade.isPago}", txtPago, "selected")
			.add(this, "${entidade.observacoes}", txtObservacoes, new MaxLengthValidator(225))
			.add(this, "${recorrencia}", txtRecorrencia, "selectedItem")
			.add(this, "${limite}", txtMaximo, new IntegerValidator(4))
			.add(txtPago, "${selected}", txtPagamento, "enabled")
			.add(txtPago, "${selected}", txtFormaPagamento, "enabled")
			.getBindingGroup().bind();
		
		setPreferredSize(new Dimension(800, 0));
	}
	
	protected void salvar() {
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

		iniciarDados();
		AppUtil.exibirMsgSalvarSucesso(this);
	}
	
	@Override
	protected void iniciarDados() {
		super.iniciarDados();
		getEntidade().setTipo(isDespesa ? TipoConta.DESPESA : TipoConta.RECEITA);
	}
	
	@Override
	protected String getTituloFrame() {
		return isDespesa ? TITULO_CAD_DESPESA : TITULO_CAD_RECEITA;
	}

	@Override
	protected CriteriaBuilder getBuilderListagem() {
		return getBuilderQntDados().addProjection("descricao", "descricao").addProjection("valor", "valor")
				.addProjection("categoria.nome", "categoria").addProjection("dataVencimento", "vencimento")
				.addAliases("categoria", "categoria", Criterion.LEFT_JOIN).addAliasToBean(ContaDTO.class).close();
	}

	@Override
	protected CriteriaBuilder getBuilderQntDados() {
		return HibernateUtil.getCriteriaBuilder(Conta.class)
				.eq("tipo", isDespesa ? TipoConta.DESPESA : TipoConta.RECEITA).eqStatusAtivo();
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

}
