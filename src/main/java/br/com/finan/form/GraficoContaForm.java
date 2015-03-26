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

	private JRadioButton rdReceita;

	private JRadioButton rdDespesa;
	
	public GraficoContaForm() {
		getContentPane().setLayout(new MigLayout());
		
		setClosable(true);
		setMaximizable(true);
		setResizable(true);
		setTitle("");
		
		rdCategoria = new JRadioButton("Categoria", true);
		rdDescricao = new JRadioButton("Descrição");
		rdConta = new JRadioButton("Conta");
		rdReceita = new JRadioButton("Receita", true);
		rdDespesa = new JRadioButton("Despesa");
		dados = new ArrayList<>();
		
		ButtonGroup grupo = new ButtonGroup();
		ButtonGroup tipoGrupo = new ButtonGroup();
		JButton btnGerar = new JButton("Gerar");
		
		grupo.add(rdConta);
		grupo.add(rdDescricao);
		grupo.add(rdCategoria);
		
		tipoGrupo.add(rdReceita);
		tipoGrupo.add(rdDespesa);
		
		btnGerar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dados.clear();
				
				TipoConta tipoConta = rdReceita.isSelected() ? TipoConta.RECEITA : TipoConta.DESPESA;
				
				if (rdCategoria.isSelected()) {
					for (Categoria cat : getCategoriaService().obterCategorias()) {
						adicionarDados(cat.getNome(), getContaService().obterSomaPorCategoria(cat.getId(), tipoConta, null, null));
					}
				} else if (rdConta.isSelected()) {
					for (ContaBancaria conta : getContaBancariaService().obterContasBancarias()) {
						adicionarDados(conta.getDescricao(), getContaService().obterSomaPorContaBancaria(conta.getId(), tipoConta, null, null));
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
		panel.add(new JLabel("Tipo de gráfico:"), "wrap, spanx2");
		panel.add(rdReceita);
		panel.add(rdDespesa, "wrap");
		panel.add(new JLabel("Gerar gráfico por:"), "wrap, spanx2");
		panel.add(rdCategoria);
		panel.add(rdConta);
		panel.add(rdDescricao, "wrap");
		panel.add(btnGerar);
		
		add(panel);
		
		pack();
		
	}

}
