package br.com.finan.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.view.JasperViewer;
import br.com.finan.dto.RelatorioGraficoDTO;
import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.util.JasperUtil;

public class GraficoContaForm extends Formulario {

	private static final String GRAFICO_JASPER = "grafico.jasper";

	private static final long serialVersionUID = 1L;
	
	private JRadioButton rdCategoria;
	private JRadioButton rdDescricao;
	private JRadioButton rdConta;
	
	private List<RelatorioGraficoDTO> dados;
	
	public GraficoContaForm() {
		getContentPane().setLayout(new MigLayout());
		
		setClosable(true);
		setMaximizable(true);
		setResizable(true);
		setTitle("");
		
		rdCategoria = new JRadioButton("Categoria", true);
		rdDescricao = new JRadioButton("Descrição");
		rdConta = new JRadioButton("Conta");
		dados = new ArrayList<>();
		
		ButtonGroup bg = new ButtonGroup();
		JButton btnGerar = new JButton("Gerar");
		
		bg.add(rdConta);
		bg.add(rdDescricao);
		bg.add(rdCategoria);
		
		btnGerar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dados.clear();
				
				if (rdCategoria.isSelected()) {
					for (Categoria cat : getCategoriaService().obterCategorias()) {
						adicionarDados(cat.getNome(), getContaService().obterSomaPorCategoria(cat.getId(), TipoConta.DESPESA, null, null));
					}
				} else if (rdConta.isSelected()) {
					for (ContaBancaria conta : getContaBancariaService().obterContasBancarias()) {
						adicionarDados(conta.getDescricao(), getContaService().obterSomaPorContaBancaria(conta.getId(), TipoConta.RECEITA, null, null));
					}
				} else {
					
				}
				
				JasperViewer frame = JasperUtil.gerarRelatorio(GRAFICO_JASPER, dados, new HashMap<String, Object>());
				frame.setVisible(true);
			}

			private void adicionarDados(String chave, Number valor) {
				RelatorioGraficoDTO dto = new RelatorioGraficoDTO();
				dto.setChave(chave);
				dto.setValor(valor);
				dados.add(dto);
			}
		});
		
		JPanel panel = new JPanel(new MigLayout());
		panel.add(new JLabel("Gerar gráfico por:"), "wrap");
		panel.add(rdCategoria);
		panel.add(rdConta);
		panel.add(rdDescricao, "wrap");
		panel.add(btnGerar);
		
		add(panel);
		
		pack();
		
	}

}
