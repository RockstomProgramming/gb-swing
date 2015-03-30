package br.com.finan.form;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.text.MaskFormatter;

import org.jdesktop.beansbinding.BindingGroup;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.view.JasperViewer;
import br.com.finan.dto.RelatorioGraficoDTO;
import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.JasperUtil;

public class GraficoContaForm extends Formulario {

	private static final String GRAFICO_JASPER = "grafico.jasper";

	private static final long serialVersionUID = 1L;
	
	private JRadioButton rdCategoria;
	private JRadioButton rdDescricao;
	private JRadioButton rdConta;
	private JRadioButton rdReceita;
	private JRadioButton rdDespesa;
	private JFormattedTextField txtDataInicio;
	private JFormattedTextField txtDataFim;
	
	private List<RelatorioGraficoDTO> dados;
	
	private String dataInicio;
	private String dataFim;
	
	public GraficoContaForm() throws ParseException {
		getContentPane().setLayout(new MigLayout());
		
		setClosable(true);
		setTitle("Gráficos");
		
		txtDataInicio = new JFormattedTextField(new MaskFormatter("##/##/####"));
		txtDataFim = new JFormattedTextField(new MaskFormatter("##/##/####"));
		rdCategoria = new JRadioButton("Categoria", true);
		rdDescricao = new JRadioButton("Descrição");
		rdConta = new JRadioButton("Conta");
		rdReceita = new JRadioButton("Receita", true);
		rdDespesa = new JRadioButton("Despesa");
		dados = new ArrayList<>();
		
		txtDataFim.setColumns(10);
		txtDataInicio.setColumns(10);
		
		ButtonGroup grupo = new ButtonGroup();
		ButtonGroup tipoGrupo = new ButtonGroup();
		JButton btnGerar = new JButton("Gerar");
		
		grupo.add(rdConta);
		grupo.add(rdDescricao);
		grupo.add(rdCategoria);
		
		tipoGrupo.add(rdReceita);
		tipoGrupo.add(rdDespesa);
		
		btnGerar.setPreferredSize(new Dimension(100, 0));
		btnGerar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dados.clear();
				
				HashMap<String, Object> params = new HashMap<String, Object>();
				TipoConta tipoConta = rdReceita.isSelected() ? TipoConta.RECEITA : TipoConta.DESPESA;
				String titulo = tipoConta.equals(TipoConta.RECEITA) ? "Receita" : "Despesa";
				
				if (rdCategoria.isSelected()) {
					for (Categoria cat : getCategoriaService().obterCategorias()) {
						adicionarDados(cat.getNome(), getContaService().obterSomaPorCategoria(cat.getId(), tipoConta, dataInicio, dataFim));
					}
					params.put("TITULO", "Gráfico de ".concat(titulo).concat(" por Categoria"));
				} else if (rdConta.isSelected()) {
					for (ContaBancaria conta : getContaBancariaService().obterContasBancarias()) {
						adicionarDados(conta.getDescricao(), getContaService().obterSomaPorContaBancaria(conta.getId(), tipoConta, dataInicio, dataFim));
					}
					params.put("TITULO", "Gráfico de ".concat(titulo).concat(" por Conta Bancária"));
				} else {
					for (String desc : getContaService().obterContaPorDescricaoAgrupado(dataInicio, dataFim, tipoConta)) {
						adicionarDados(desc, getContaService().obterSomaPorDescricao(desc, dataInicio, dataFim, tipoConta));
					}
					params.put("TITULO", "Gráfico de ".concat(titulo).concat(" por Descrição"));
				}
				
				BigDecimal total = new BigDecimal(0);
				
				for (RelatorioGraficoDTO dto : dados) {
					total = total.add((BigDecimal) dto.getValor());
				}
				
				params.put("TOTAL", total);
				
				Collections.sort(dados, new Comparator<RelatorioGraficoDTO>() {
					@Override
					public int compare(RelatorioGraficoDTO o1, RelatorioGraficoDTO o2) {
						double v1 = o1.getValor().doubleValue();
						double v2 = o2.getValor().doubleValue();
						
						if (v1 > v2) {
							return 1;
						} else if (v1 < v2) {
							return -1;
						}
						
						return 0;
					}
				});
				
				JasperViewer frame = JasperUtil.gerarRelatorio(GRAFICO_JASPER, dados, params);
				frame.setVisible(true);
			}

			private void adicionarDados(String chave, Number valor) {
				RelatorioGraficoDTO dto = new RelatorioGraficoDTO();
				dto.setChave(chave);
				dto.setValor(valor);
				dados.add(dto);
			}
		});
		
		JPanel pnlPor = new JPanel(new MigLayout());
		pnlPor.add(rdCategoria);
		pnlPor.add(rdConta);
		pnlPor.add(rdDescricao, "wrap");

		JPanel pnlTipo = new JPanel(new MigLayout());
		pnlTipo.add(rdReceita);
		pnlTipo.add(rdDespesa);
		
		JPanel panel = new JPanel(new MigLayout());
		panel.add(new JLabel("Data Iníco:"));
		panel.add(txtDataInicio);
		panel.add(new JLabel("Data Fim:"));
		panel.add(txtDataFim, "wrap");
		panel.add(new JLabel("Tipo de gráfico:"), "wrap, spanx2");
		panel.add(pnlTipo, "wrap, spanx4");
		panel.add(new JLabel("Gerar gráfico por:"), "wrap, spanx2");
		panel.add(pnlPor, "wrap, spanx4");
		panel.add(btnGerar, "spanx2");
		
		BindingUtil.create(new BindingGroup())
			.add(this, "#{dataInicio}", txtDataInicio)
			.add(this, "#{dataFim}", txtDataFim)
			.getBindingGroup().bind();
		
		add(panel);
		
		pack();
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

}
