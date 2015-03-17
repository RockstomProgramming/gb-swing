package br.com.arq.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.observablecollections.ObservableCollections;

import br.com.finan.annotation.ColunaTabela;
import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.DTO;
import br.com.finan.entidade.Entidade;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.BindingUtil.ColumnBinding;
import br.com.finan.util.FieldUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;

public abstract class CadastroForm<T extends Entidade, D extends DTO> extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private BindingUtil binding;
	private JPanel panelAcoes;
	private T entidade;
	private List<D> dados;
	private Long idSelecionado;

	protected JScrollPane scroll;
	protected JTable tabela;
	protected JButton btnSalvar;
	protected JButton btnNovo;
	protected JButton btnExcluir;

	public CadastroForm() {
		getContentPane().setLayout(new MigLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(getTituloFrame());
		setClosable(true);

		BindingGroup bindingGroup = new BindingGroup();
		binding = BindingUtil.create(bindingGroup);

		iniciarDados();

		btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HibernateUtil.inativar(idSelecionado, obterTipoDaClasse(0).getSimpleName());
				iniciarDados();
			}
		});

		btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniciarDados();
			}
		});

		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				entidade.setId(getIdSelecionado());
				HibernateUtil.salvarOuAlterar(entidade);
				iniciarDados();
			}

		});
		
		tabela = new JTable();
		scroll = new JScrollPane();
		scroll.setViewportView(tabela);

		ColumnBinding columnBinding = 
				binding.add(tabela, "${selectedElement != null}", btnExcluir, "enabled")
					.add(tabela, "${selectedElement.id}", this, "idSelecionado")
					.addJTableBinding(dados, tabela);
		
		for (Field f : getCamposTabela().values()) {
			ColunaTabela ann = f.getAnnotation(ColunaTabela.class);
			columnBinding.addColumnBinding(ann.index(), "${".concat(f.getName()).concat("}"), ann.titulo(), ann.tipo());
		}
		
		panelAcoes = new JPanel(new MigLayout());
		panelAcoes.add(btnNovo);
		panelAcoes.add(btnSalvar);
		panelAcoes.add(btnExcluir);

		add(scroll, "wrap, push");
		add(panelAcoes, "wrap");

		pack();
	}
	
	private Map<Integer, Field> getCamposTabela() {
		final Map<Integer, Field> campos = new TreeMap<Integer, Field>();
		for (final Field field : FieldUtil.getAllFields(obterTipoDaClasse(1))) {
			if (field.isAnnotationPresent(ColunaTabela.class)) {
				campos.put(field.getAnnotation(ColunaTabela.class).index(), field);
			}
		}
		return campos;
	}

	@SuppressWarnings("unchecked")
	private void iniciarDados() {
		setIdSelecionado(null);
		
		try {
			entidade = obterTipoDaClasse(0).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		if (ObjetoUtil.isReferencia(dados)) {
			dados.clear();
			dados.addAll(getBuilderListagem().list());
		} else {
			dados = ObservableCollections.observableList(getBuilderListagem().list());
		}
		
		limparCampos();
	}
	
	protected void limparCampos() {
		JPanel panelCadastro = getPanelCadastro();
		if (ObjetoUtil.isReferencia(panelCadastro)) {
			limparCampos(panelCadastro);
		}
	}

	protected void limparCampos(final JPanel container) {
		for (final Component comp : container.getComponents()) {
			if (comp instanceof JPanel) {
				limparCampos((JPanel) comp);
			}
			
			if (comp instanceof JTextComponent) {
				((JTextComponent) comp).setText(null);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "restriction" })
	protected Class<T> obterTipoDaClasse(int index) {
		return (Class<T>) ((sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl) getClass().getGenericSuperclass()).getActualTypeArguments()[index];
	}
	
	protected abstract String getTituloFrame();

	protected abstract CriteriaBuilder getBuilderListagem();
	
	protected abstract JPanel getPanelCadastro();
	
	public BindingUtil getBinding() {
		return binding;
	}

	public Long getIdSelecionado() {
		return idSelecionado;
	}

	@SuppressWarnings("unchecked")
	public void setIdSelecionado(Long idSelecionado) {
		setEntidade((T) HibernateUtil.getCriteriaBuilder(obterTipoDaClasse(0)).eqId(idSelecionado).uniqueResult());
		this.idSelecionado = idSelecionado;
	}

	public T getEntidade() {
		return entidade;
	}

	public void setEntidade(T entidade) {
		this.entidade = entidade;
	}
}
