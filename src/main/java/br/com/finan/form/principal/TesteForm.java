package br.com.finan.form.principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.observablecollections.ObservableCollections;

import br.com.finan.entidade.Categoria;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;

@SuppressWarnings({"unchecked"})
public class TesteForm extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JTable tabela;
	private JTextField txtDescricao;
	private JPanel panel;
	private JScrollPane scroll;
	private Categoria entidade;
	private List<Categoria> lista;
	private Long idEntidadeSelecionada;
	
	private void iniciarDados() {
		setEntidade(new Categoria());
		setIdEntidadeSelecionada(null);
		
		if (ObjetoUtil.isReferencia(lista)) {
			lista.clear();
			lista.addAll(HibernateUtil.getCriteriaBuilder(Categoria.class).eqStatusAtivo().list());
		} else {
			lista = ObservableCollections.observableList(HibernateUtil.getCriteriaBuilder(Categoria.class).eqStatusAtivo().list());
		}
	}
	
	public TesteForm() {
		getContentPane().setLayout(new MigLayout());
		
		iniciarDados();
		
		tabela = new JTable();
		scroll = new JScrollPane();
		txtDescricao = new JTextField(20);
		
		scroll.setViewportView(tabela);
		
		JButton btnLimpar = new JButton("Limpar");
		JButton btnSalvar = new JButton("Salvar");
		JButton btnNovo = new JButton("Novo");
		JButton btnExcluir = new JButton("Excluir");

		panel = new JPanel(new MigLayout());
		panel.add(btnNovo);
		panel.add(btnSalvar);
		panel.add(btnExcluir);
		panel.add(btnLimpar);
		
		btnExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HibernateUtil.inativar(getIdEntidadeSelecionada(), Categoria.class.getSimpleName());
				iniciarDados();
			}
		});
		
		btnNovo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HibernateUtil.salvar(entidade);
				iniciarDados();
			}

		});
		
		btnLimpar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lista.clear();
			}
		});
		
		BindingGroup bindingGroup = new BindingGroup();
		
		BindingUtil.create(bindingGroup)
			.addJTableBinding(lista, tabela)
				.addColumnBinding(0, "${id}", "Código")
				.addColumnBinding(1, "${nome}", "Descrição").close()
			.add(tabela, "${selectedElement.nome}", txtDescricao)
			.add(tabela, "${selectedElement != null}", btnExcluir, "enabled")
			.add(tabela, "${selectedElement.id}", this, "idEntidadeSelecionada")
			.add(this, "${entidade.nome}", txtDescricao);
		
		bindingGroup.bind();
		
		add(scroll, "wrap");
		add(txtDescricao, "wrap");
		add(panel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	public Categoria getEntidade() {
		return entidade;
	}

	public void setEntidade(Categoria entidade) {
		this.entidade = entidade;
	}

	public Long getIdEntidadeSelecionada() {
		return idEntidadeSelecionada;
	}

	public void setIdEntidadeSelecionada(Long idEntidadeSelecionada) {
		this.idEntidadeSelecionada = idEntidadeSelecionada;
	}
}
