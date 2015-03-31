package br.com.finan.form;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.view.JasperViewer;

import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.observablecollections.ObservableCollections;

import br.com.finan.annotation.OnGanharFoco;
import br.com.finan.dto.ContaDTO;
import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.service.ContaService;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.JasperUtil;

public class RelatorioContaForm extends Formulario {

	private static final String TITULO_RELATORIO = "Movimentações financeiras";
	private static final String TITULO_FRAME = "Extrato";
	private static final String EXTRATO_JASPER = "extrato.jasper";
	private static final long serialVersionUID = 1L;

	private final JComboBox<Categoria> cmbCategoria;
	private final JComboBox<ContaBancaria> cmbContaBancaria;

	private final List<Categoria> categorias;
	private final List<ContaBancaria> contasBancarias;

	private final ContaService contaService;
	private ContaBancaria contaBancaria;
	private Categoria categoria;

	private final JButton btnGerar;
	private final JFormattedTextField txtDataInicio;
	private final JFormattedTextField txtDataFim;

	private String dataInicio;
	private String dataFim;
	private final JPanel painel;
	private final JButton btnLimpar;

	public RelatorioContaForm() throws ParseException {
		contaService = new ContaService();
		cmbCategoria = new JComboBox<Categoria>();
		cmbContaBancaria = new JComboBox<ContaBancaria>();
		txtDataInicio = new JFormattedTextField(new MaskFormatter("##/##/####"));
		txtDataFim = new JFormattedTextField(new MaskFormatter("##/##/####"));
		categorias = ObservableCollections.observableList(new ArrayList<Categoria>());
		contasBancarias = ObservableCollections.observableList(new ArrayList<ContaBancaria>());

		setClosable(true);
		setTitle(TITULO_FRAME);
		getContentPane().setLayout(new MigLayout());

		txtDataFim.setColumns(10);
		txtDataInicio.setColumns(10);

		cmbCategoria.setPreferredSize(new Dimension(200, 0));
		cmbContaBancaria.setPreferredSize(new Dimension(200, 0));

		btnGerar = new JButton("Gerar");
		btnGerar.setPreferredSize(new Dimension(100, 0));
		btnGerar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				gerarRelatorio();
			}
		});

		btnLimpar = new JButton("Limpar");
		btnLimpar.setPreferredSize(new Dimension(100, 0));
		btnLimpar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				limparCampos(painel);
			}
		});

		BindingUtil.create(new BindingGroup())
		.add(this, "${categoria}", cmbCategoria, "selectedItem")
		.add(this, "${contaBancaria}", cmbContaBancaria, "selectedItem")
		.add(this, "#{dataInicio}", txtDataInicio)
		.add(this, "#{dataFim}", txtDataFim)
		.addJComboBoxBinding(categorias, cmbCategoria)
		.addJComboBoxBinding(contasBancarias, cmbContaBancaria)
		.getBindingGroup().bind();

		final JPanel pnlAcao = new JPanel(new MigLayout());
		pnlAcao.add(btnLimpar, "");
		pnlAcao.add(btnGerar, "");

		painel = new JPanel(new MigLayout());
		painel.add(new JLabel("Data Início:"));
		painel.add(txtDataInicio);
		painel.add(new JLabel("Data Fim:"));
		painel.add(txtDataFim, "wrap");
		painel.add(new JLabel("Categoria:"));
		painel.add(cmbCategoria, "spanx2, growx, wrap");
		painel.add(new JLabel("Conta:"));
		painel.add(cmbContaBancaria, "wrap, growx, spanx2");
		painel.add(pnlAcao, "spanx4");

		add(painel);
		pack();
	}

	@OnGanharFoco
	public void iniciarListas() {
		categorias.add(null);
		categorias.addAll(getCategoriaService().obterCategorias());

		contasBancarias.add(null);
		contasBancarias.addAll(getContaBancariaService().obterContasBancarias());
	}

	public void gerarRelatorio() {
		final Map<String, Object> params = new HashMap<String, Object>();
		final BigDecimal totalDespesa = getContaService().getSomaDespesa();
		final BigDecimal totalReceita = getContaService().getSomaReceita();

		final List<ContaDTO> dados = getContaService().obterExtratoPorPeriodo(dataInicio, dataFim, getCategoria(), getContaBancaria());

		BigDecimal despesa = new BigDecimal(0);
		BigDecimal receita = new BigDecimal(0);

		for (final ContaDTO c : dados) {
			if (c.getTipo().equals(TipoConta.DESPESA)) {
				despesa = despesa.add(c.getValor());
			} else {
				receita = receita.add(c.getValor());
			}
		}

		params.put("SALDO", getContaService().getSaldoAtual(totalDespesa, totalReceita));
		params.put("DESPESA", despesa);
		params.put("RECEITA", receita);
		params.put("TOTAL_REC", totalReceita);
		params.put("TOTAL_DESP", totalDespesa);

		final JasperViewer frame = JasperUtil.gerarRelatorio(EXTRATO_JASPER, dados, params);
		frame.setTitle(TITULO_RELATORIO);
		frame.setVisible(true);
	}

	@Override
	public ContaService getContaService() {
		return contaService;
	}

	public ContaBancaria getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(final ContaBancaria contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(final Categoria categoria) {
		this.categoria = categoria;
	}

	public String getDataFim() {
		return dataFim;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataFim(final String dataFim) {
		this.dataFim = dataFim;
	}

	public void setDataInicio(final String dataInicio) {
		this.dataInicio = dataInicio;
	}
}
