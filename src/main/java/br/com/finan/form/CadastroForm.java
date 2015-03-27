package br.com.finan.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.hibernate.criterion.Projections;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.observablecollections.ObservableCollections;

import br.com.finan.annotation.ColunaTabela;
import br.com.finan.annotation.PostLoadTable;
import br.com.finan.converter.BigDecimalConverter;
import br.com.finan.converter.DateConverter;
import br.com.finan.converter.DoubleConverter;
import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.DTO;
import br.com.finan.entidade.Entidade;
import br.com.finan.enumerator.Conversor;
import br.com.finan.util.AppUtil;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.BindingUtil.ColumnBinding;
import br.com.finan.util.CriterionInfo;
import br.com.finan.util.FieldUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;

public abstract class CadastroForm<T extends Entidade, D extends DTO> extends Formulario {

	private static final long serialVersionUID = 1L;
	private BindingUtil binding;
	private JPanel panelAcoes;
	private T entidade;
	private List<D> dados;
	private Long idSelecionado;
	
	private int pagina = 1;
	private Long qntRegistros = 0L;
	protected final int MAX_REGISTROS = 15;

	protected JScrollPane scroll;
	protected JTable tabela;
	protected JButton btnSalvar;
	protected JButton btnNovo;
	protected JButton btnExcluir;
	protected JButton btnAtualizar;
	
	protected JButton btnAnterior;
	protected JButton btnPrimeiro;
	protected JButton btnProximo;
	protected JButton btnUltimo;
	protected JLabel lbPaginacao;
	protected JPanel pnlPaginacao;
	protected JPanel pnlFiltro;

	@SuppressWarnings("rawtypes")
	public CadastroForm() {
		getContentPane().setLayout(new MigLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(getTituloFrame());
		setClosable(true);
		setResizable(true);
		setMaximizable(true);
		
		BindingGroup bindingGroup = new BindingGroup();
		binding = BindingUtil.create(bindingGroup);

		btnExcluir = new JButton("Excluir", new ImageIcon(getClass().getResource("/icon/Delete.png")));
		btnExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inativar();
			}
		});

		btnNovo = new JButton("Novo", new ImageIcon(getClass().getResource("/icon/Add.png")));
		btnNovo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniciarDados();
			}
		});

		btnSalvar = new JButton("Salvar", new ImageIcon(getClass().getResource("/icon/Save.png")));
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				salvar();
			}
		});
		
		pnlPaginacao = new JPanel();
		lbPaginacao = new JLabel();
		
		btnPrimeiro = new JButton(new ImageIcon(getClass().getResource("/icon/Symbol_Rewind.png")));
		btnPrimeiro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irPrimeiraPagina();
			}
		});

		btnAnterior = new JButton();
		btnAnterior.setIcon(new ImageIcon(getClass().getResource("/icon/Symbol_Play_Reversed.png")));
		btnAnterior.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irPaginaAnterior();
			}
		});

		btnProximo = new JButton();
		btnProximo.setIcon(new ImageIcon(getClass().getResource("/icon/Symbol_Play.png")));
		btnProximo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irProximaPagina();
			}
		});

		btnUltimo = new JButton();
		btnUltimo.setIcon(new ImageIcon(getClass().getResource("/icon/Symbol_FastForward.png")));
		btnUltimo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irUltimaPagina();
			}
		});

		btnAtualizar = new JButton("Atualizar");
		btnAtualizar.setIcon(new ImageIcon(getClass().getResource("/icon/refresh.png")));
		btnAtualizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				paginar();
			}
		});

		iniciarDados();
		
		tabela = new JTable();
		scroll = new JScrollPane();
		scroll.setViewportView(tabela);

		ColumnBinding columnBinding = 
				binding.add(tabela, "${selectedElement != null}", btnExcluir, "enabled")
					.add(tabela, "${selectedElement != null}", btnNovo, "enabled")
					.add(tabela, "${selectedElement.id}", this, "idSelecionado")
					.addJTableBinding(dados, tabela);
		
		for (Field f : getCamposTabela().values()) {
			ColunaTabela ann = f.getAnnotation(ColunaTabela.class);
			Conversor conversor = ann.conversor();
			Converter converter = null;
			if (!conversor.equals(Conversor.DEFAULT)) {
				switch (conversor) {
					case DATE: converter = new DateConverter(); break;
					case BIG_DECIMAL: converter = new BigDecimalConverter(); break;
					case DOUBLE : converter = new DoubleConverter(); break;
				}
				columnBinding.addColumnBinding(ann.index(), "${".concat(f.getName()).concat("}"), ann.titulo(), converter, ann.tipo());
			} else {
				columnBinding.addColumnBinding(ann.index(), "${".concat(f.getName()).concat("}"), ann.titulo(), ann.tipo());
			}
		}
		
		panelAcoes = new JPanel(new MigLayout());
		panelAcoes.setBorder(new EtchedBorder());
		panelAcoes.add(btnNovo);
		panelAcoes.add(btnSalvar);
		panelAcoes.add(btnExcluir);
		panelAcoes.add(btnAtualizar, "push");
		panelAcoes.add(btnPrimeiro);
		panelAcoes.add(btnAnterior);
		panelAcoes.add(lbPaginacao);
		panelAcoes.add(btnProximo);
		panelAcoes.add(btnUltimo);
		
		pnlFiltro = new JPanel(new MigLayout());
		pnlFiltro.setBorder(new EtchedBorder());
		
		add(pnlFiltro, "wrap, growx, pushx");
		add(scroll, "wrap, push, grow");
		add(panelAcoes, "wrap, growx");
		
		pack();
	}
	
	protected void irProximaPagina() {
		setPagina(getPagina() + 1);
		paginar();
	}

	protected void irPaginaAnterior() {
		setPagina(getPagina() == 0 ? 0 : getPagina() - 1);
		paginar();
	}

	protected void irPrimeiraPagina() {
		setPagina(1);
		paginar();
	}

	protected void irUltimaPagina() {
		setPagina(getQntPagina());
		paginar();
	}

	private void paginar() {
		final int max = getPagina() * MAX_REGISTROS;
		final int min = max - MAX_REGISTROS;
		buscarDados(min);
	}

	protected int getQntPagina() {
		int p = (int) (getQntRegistros() / MAX_REGISTROS);
		if (getQntRegistros() % MAX_REGISTROS > 0) {
			p++;
		}
		return p;
	}

	protected boolean isUltimaPagina() {
		return getPagina() >= getQntPagina();
	}

	protected void validarBtnPaginacao() {
		btnPrimeiro.setEnabled(getPagina() != 1);
		btnAnterior.setEnabled(btnPrimeiro.isEnabled());
		btnProximo.setEnabled(!isUltimaPagina());
		btnUltimo.setEnabled(!isUltimaPagina());
		lbPaginacao.setText(getPagina() + " de " + getQntPagina());
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

	protected void iniciarDados() {
		setIdSelecionado(null);
		
		iniciarEntidade();
		paginar();
		limparCampos();
	}

	@SuppressWarnings("unchecked")
	private void iniciarEntidade() {
		try {
			entidade = (T) obterTipoDaClasse(0).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	protected void buscarDados(int primeiroResultado) {
		Class<D> dto = (Class<D>) obterTipoDaClasse(1);
		List<D> lista = CriterionInfo.getInstance(getBuilder(), dto).getCriteria().setFirstResult(primeiroResultado).setMaxResults(MAX_REGISTROS).list();
		
		if (!ObjetoUtil.isReferencia(dados)) {
			dados = ObservableCollections.observableList(lista);
		} else {
			dados.clear();
			dados.addAll(lista);
		}
		
		qntRegistros = (Long) getBuilder().getCriteria().setProjection(Projections.rowCount()).uniqueResult();
		
		validarBtnPaginacao();
		executarMetodosPosCarregamento();
	}
	
	@SuppressWarnings("unchecked")
	private CriteriaBuilder getBuilder() {
		Class<T> entidade = (Class<T>) obterTipoDaClasse(0);
		CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder(entidade);
		builder.eqStatusAtivo();
		
		adicionarRestricoes(builder);
		
		return builder;
	}

	private void executarMetodosPosCarregamento() {
		List<Class<?>> superclasses = FieldUtil.getAllSuperclasses(this.getClass());
		superclasses.add(this.getClass());
		for (Class<?> c : superclasses) {
			for (Method method : c.getDeclaredMethods()) {
				if (method.isAnnotationPresent(PostLoadTable.class)) {
					try {
						method.invoke(this);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	protected void limparCampos() {
		JPanel panelCadastro = getPanelCadastro();
		if (ObjetoUtil.isReferencia(panelCadastro)) {
			limparCampos(panelCadastro);
		}
	}

	protected void salvar() {
		entidade.setId(getIdSelecionado());
		HibernateUtil.salvarOuAlterar(entidade);
		AppUtil.exibirMsgSalvarSucesso(this);
		iniciarDados();
	}

	protected void inativar() {
		if (AppUtil.exibirMensagemConfirmacaoInativacao(this)) {
			HibernateUtil.inativar(idSelecionado, obterTipoDaClasse(0).getSimpleName());
			iniciarDados();
		}
	}
	
	@SuppressWarnings({ "restriction" })
	protected Class<?> obterTipoDaClasse(int index) {
		return (Class<?>) ((sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl) getClass().getGenericSuperclass()).getActualTypeArguments()[index];
	}
	
	protected abstract String getTituloFrame();
	
	protected abstract void adicionarRestricoes(CriteriaBuilder builder);
	
	protected abstract JPanel getPanelCadastro();
	
	public BindingUtil getBinding() {
		return binding;
	}

	public Long getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(Long idSelecionado) {
		popularInterface(idSelecionado);
		this.idSelecionado = idSelecionado;
	}

	@SuppressWarnings("unchecked")
	protected void popularInterface(Long idSelecionado) {
		setEntidade((T) HibernateUtil.getCriteriaBuilder(obterTipoDaClasse(0)).eqId(idSelecionado).uniqueResult());
	}

	public T getEntidade() {
		return entidade;
	}

	public void setEntidade(T entidade) {
		this.entidade = entidade;
	}

	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	public Long getQntRegistros() {
		return qntRegistros;
	}
}
