package br.com.finan.form;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.jdesktop.observablecollections.ObservableCollections;

import net.miginfocom.swing.MigLayout;

import br.com.finan.dto.ContaDTO;
import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.Conta;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.service.ContaService;
import br.com.finan.util.CriterionInfo;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.JasperUtil;
import br.com.finan.util.NumberUtil;

@SuppressWarnings("unchecked")
public class RelatorioContaForm extends JInternalFrame {

	private static final String TITULO_FRAME = "Extrato";
	private static final String EXTRATO_JASPER = "extrato.jasper";
	private static final long serialVersionUID = 1L;
	
	private JComboBox<Categoria> cmbCategoria;
	private JComboBox<ContaBancaria> cmbContaBancaria;
	
	private List<Categoria> categorias;
	private List<ContaBancaria> contasBancarias;
	
	private ContaService contaService;
	
	private JButton btnGerar;
	
	public RelatorioContaForm() {
		contaService = new ContaService();
		cmbCategoria = new JComboBox<Categoria>();
		cmbContaBancaria = new JComboBox<ContaBancaria>();
		categorias = ObservableCollections.observableList(new ArrayList<Categoria>());
		contasBancarias = ObservableCollections.observableList(new ArrayList<ContaBancaria>());
		
		setClosable(true);
		setMaximizable(true);
		setTitle(TITULO_FRAME);
		getContentPane().setLayout(new MigLayout());
		
		setPreferredSize(new Dimension(800, 600));
		
		btnGerar = new JButton("Gerar");
		btnGerar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gerarRelatorio();
			}
		});
		
		JPanel pnl = new JPanel(new MigLayout());
		pnl.add(btnGerar);
		
		add(pnl);
		pack();
	}

	public void gerarRelatorio() {
		Map<String, Object> params = new HashMap<String, Object>();
		BigDecimal despesa = getContaService().getSomaDespesa();
		BigDecimal receita = getContaService().getSomaReceita();
		
		params.put("SALDO", getContaService().getSaldoAtual(despesa, receita));
		params.put("DESPESA", NumberUtil.obterNumeroFormatado(despesa));
		params.put("RECEITA", NumberUtil.obterNumeroFormatado(receita));
		
		List<ContaDTO> dados = CriterionInfo.getInstance(Conta.class, ContaDTO.class).eqStatusAtivo().list();

		JasperUtil.gerarRelatorio(EXTRATO_JASPER, dados, params).setVisible(true);
	}

	public static void main(String[] args) {
		new RelatorioContaForm();
	}

	public ContaService getContaService() {
		return contaService;
	}

}
