package br.com.finan.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.observablecollections.ObservableCollections;

import br.com.finan.annotation.ColunaTabela;
import br.com.finan.annotation.PosSalvar;
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

@SuppressWarnings("unchecked")
public abstract class CadastroForm<T extends Entidade, D extends DTO> extends Formulario {

	private static final long serialVersionUID = 1L;
	private static final int MAX_REGISTROS = 15;

	private final BindingUtil binding;
	private T entidade;
	private List<D> dados;
	private Long idSelecionado;
	private int pagina = 1;
	private Long qntRegistros = 0L;
	private ComponentesCadastro componentes;

	public CadastroForm() {
		super();

		componentes = new ComponentesCadastro();
		binding = BindingUtil.create(new BindingGroup());

		getContentPane().setLayout(new MigLayout());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(getTituloFrame());
		setClosable(true);
		setResizable(true);
		setMaximizable(true);

		iniciarEntidade();
		paginar();
		adicionarAcoes();
		adicionarBindings();
		montarTela();

		pack();
	}

	protected abstract void adicionarRestricoes(CriteriaBuilder builder);

	protected abstract JPanel getPanelCadastro();

	protected abstract String getTituloFrame();

	private void montarTela() {
		componentes.getScroll().setViewportView(componentes.getTabela());

		componentes.getPanelAcoes().setBorder(new EtchedBorder());
		componentes.getPanelAcoes().add(componentes.getBtnNovo());
		componentes.getPanelAcoes().add(componentes.getBtnSalvar());
		componentes.getPanelAcoes().add(componentes.getBtnExcluir());
		componentes.getPanelAcoes().add(componentes.getBtnAtualizar(), "push");
		componentes.getPanelAcoes().add(componentes.getBtnPrimeiro());
		componentes.getPanelAcoes().add(componentes.getBtnAnterior());
		componentes.getPanelAcoes().add(componentes.getLbPaginacao());
		componentes.getPanelAcoes().add(componentes.getBtnProximo());
		componentes.getPanelAcoes().add(componentes.getBtnUltimo());

		componentes.getPnlFiltro().setBorder(new EtchedBorder());

		add(componentes.getPnlFiltro(), "wrap, growx, pushx");
		add(componentes.getScroll(), "wrap, push, grow");
		add(componentes.getPanelAcoes(), "wrap, growx");
	}

	@SuppressWarnings("rawtypes")
	private void adicionarBindings() {
		final ColumnBinding columnBinding = binding.add(componentes.getTabela(), "${selectedElement != null}", componentes.getBtnExcluir(), "enabled").add(componentes.getTabela(), "${selectedElement != null}", componentes.getBtnNovo(), "enabled")
				.add(componentes.getTabela(), "${selectedElement.id}", this, "idSelecionado").addJTableBinding(dados, componentes.getTabela());

		for (final Field f : getCamposTabela().values()) {
			final ColunaTabela ann = f.getAnnotation(ColunaTabela.class);
			final Conversor conversor = ann.conversor();
			Converter converter = null;
			if (conversor.equals(Conversor.DEFAULT)) {
				columnBinding.addColumnBinding(ann.index(), "${".concat(f.getName()).concat("}"), ann.titulo(), ann.tipo());
			} else {
				switch (conversor) {
				case DATE:
					converter = new DateConverter();
					break;
				case BIG_DECIMAL:
					converter = new BigDecimalConverter();
					break;
				case DOUBLE:
					converter = new DoubleConverter();
					break;
				}
				columnBinding.addColumnBinding(ann.index(), "${".concat(f.getName()).concat("}"), ann.titulo(), converter, ann.tipo());
			}
		}
	}

	private void adicionarAcoes() {
		componentes.getBtnExcluir().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				inativar();
			}
		});

		componentes.getBtnNovo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				iniciarDados();
			}
		});

		componentes.getBtnSalvar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				salvar();
			}
		});

		componentes.getBtnPrimeiro().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irPrimeiraPagina();
			}
		});

		componentes.getBtnAnterior().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irPaginaAnterior();
			}
		});

		componentes.getBtnProximo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irProximaPagina();
			}
		});

		componentes.getBtnUltimo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irUltimaPagina();
			}
		});

		componentes.getBtnAtualizar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				paginar();
			}
		});
	}

	protected void buscarDados(final int primeiroResultado) {
		final Class<D> dto = (Class<D>) obterTipoDaClasse(1);
		final CriteriaBuilder builder = CriterionInfo.getInstance(getBuilder(), dto);
		final Criteria criteria = builder.getCriteria();
		criteria.setFirstResult(primeiroResultado);
		criteria.setMaxResults(MAX_REGISTROS);

		final List<D> lista = criteria.list();

		if (ObjetoUtil.isReferencia(dados)) {
			dados.clear();
			dados.addAll(lista);
		} else {
			dados = ObservableCollections.observableList(lista);
		}

		qntRegistros = (Long) getBuilder().getCriteria().setProjection(Projections.rowCount()).uniqueResult();

		validarBtnPaginacao();
		executarMetodos(PostLoadTable.class);
	}

	protected void excluir() {
		if (AppUtil.exibirMensagemConfirmacaoInativacao(this)) {
			HibernateUtil.excluir(idSelecionado, obterTipoDaClasse(0).getSimpleName());
			iniciarDados();
		}
	}

	private CriteriaBuilder getBuilder() {
		final Class<T> entidade = (Class<T>) obterTipoDaClasse(0);
		final CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder(entidade);
		builder.eqStatusAtivo();

		adicionarRestricoes(builder);

		return builder;
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

	public JPanel getPnlFiltro() {
		return componentes.getPnlFiltro();
	}

	public JPanel getPnlPaginacao() {
		return componentes.getPnlPaginacao();
	}

	protected int getQntPagina() {
		int p = (int) (getQntRegistros() / MAX_REGISTROS);
		if (getQntRegistros() % MAX_REGISTROS > 0) {
			p++;
		}
		return p;
	}

	protected void inativar() {
		if (AppUtil.exibirMensagemConfirmacaoInativacao(this)) {
			HibernateUtil.inativar(idSelecionado, obterTipoDaClasse(0).getSimpleName());
			iniciarDados();
		}
	}

	protected void iniciarDados() {
		setIdSelecionado(null);

		iniciarEntidade();
		paginar();
		limparCampos();
	}

	private void iniciarEntidade() {
		try {
			entidade = (T) obterTipoDaClasse(0).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.log(Level.SEVERE, null, e);
		}
	}

	protected void irPaginaAnterior() {
		setPagina(getPagina() == 0 ? 0 : getPagina() - 1);
		paginar();
	}

	protected void irPrimeiraPagina() {
		setPagina(1);
		paginar();
	}

	protected void irProximaPagina() {
		setPagina(getPagina() + 1);
		paginar();
	}

	protected void irUltimaPagina() {
		setPagina(getQntPagina());
		paginar();
	}

	protected boolean isUltimaPagina() {
		return getPagina() >= getQntPagina();
	}

	protected void limparCampos() {
		final JPanel panelCadastro = getPanelCadastro();
		if (ObjetoUtil.isReferencia(panelCadastro)) {
			limparCampos(panelCadastro);
		}
	}

	@SuppressWarnings({ "restriction" })
	protected Class<?> obterTipoDaClasse(final int index) {
		return (Class<?>) ((sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl) getClass().getGenericSuperclass()).getActualTypeArguments()[index];
	}

	private void paginar() {
		final int max = pagina * MAX_REGISTROS;
		final int min = max - MAX_REGISTROS;
		buscarDados(min);
	}

	protected void popularInterface(final Long idSelecionado) {
		setEntidade((T) HibernateUtil.getCriteriaBuilder(obterTipoDaClasse(0)).eqId(idSelecionado).uniqueResult());
	}

	protected void salvar() {
		entidade.setId(getIdSelecionado());
		HibernateUtil.salvarOuAlterar(entidade);
		AppUtil.exibirMsgSalvarSucesso(this);
		executarMetodos(PosSalvar.class);
		iniciarDados();
	}

	protected void validarBtnPaginacao() {
		componentes.getBtnPrimeiro().setEnabled(getPagina() != 1);
		componentes.getBtnAnterior().setEnabled(componentes.getBtnPrimeiro().isEnabled());
		componentes.getBtnProximo().setEnabled(!isUltimaPagina());
		componentes.getBtnUltimo().setEnabled(!isUltimaPagina());
		componentes.getLbPaginacao().setText(getPagina() + " de " + getQntPagina());
	}

	public List<D> getDados() {
		return dados;
	}

	public T getEntidade() {
		return entidade;
	}

	public Long getIdSelecionado() {
		return idSelecionado;
	}

	public int getPagina() {
		return pagina;
	}

	public Long getQntRegistros() {
		return qntRegistros;
	}

	public void setEntidade(final T entidade) {
		this.entidade = entidade;
	}

	public void setIdSelecionado(final Long idSelecionado) {
		popularInterface(idSelecionado);
		this.idSelecionado = idSelecionado;
	}

	public void setPagina(final int pagina) {
		this.pagina = pagina;
	}

	public BindingUtil getBinding() {
		return binding;
	}

	public ComponentesCadastro getComponentes() {
		return componentes;
	}

	public void setComponentes(final ComponentesCadastro componentes) {
		this.componentes = componentes;
	}

}
