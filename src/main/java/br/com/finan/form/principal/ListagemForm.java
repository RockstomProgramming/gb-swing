package br.com.finan.form.principal;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.criterion.Projections;

import br.com.finan.annotation.ColunaTabela;
import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dao.Criterion;
import br.com.finan.dto.DTO;
import br.com.finan.dto.FiltroDTO;
import br.com.finan.entidade.Entidade;
import br.com.finan.util.FieldUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.NumberUtil;
import br.com.finan.util.ObjetoUtil;

/**
 *
 * @author Wesley Luiz
 * @param <T>
 */
public abstract class ListagemForm<T extends DTO> extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private List<T> dados;
	private int pagina = 1;
	private Long qntRegistros = 0L;
	private final ListagemTableModel model;
	protected final int MAX_REGISTROS = 15;

	protected JButton btnAnterior;
	protected JButton btnExcluir;
	protected JButton btnPrimeiro;
	protected JButton btnProximo;
	protected JButton btnSalvar;
	protected JButton btnUltimo;
	protected JScrollPane scroll;
	protected JLabel lbPaginacao;
	protected JPanel pnlPaginacao;
	protected JTable tabela;
	protected JButton btnAtualizar;
	protected JButton btnEditar;
	protected JButton btnPesquisar;
	protected DialogPesquisa<? extends FiltroDTO> modalPesquisa;

	public ListagemForm() {
		scroll = new JScrollPane();
		tabela = new JTable();
		btnSalvar = new JButton();
		btnExcluir = new JButton();
		pnlPaginacao = new JPanel();
		lbPaginacao = new JLabel();
		btnAnterior = new JButton();
		btnProximo = new JButton();
		btnUltimo = new JButton();
		btnPrimeiro = new JButton();
		btnAtualizar = new JButton();
		btnEditar = new JButton();
		btnPesquisar = new JButton();
		
		scroll.setViewportView(tabela);
		scroll.setPreferredSize(new Dimension(800, 280));
		
		btnSalvar.setText("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {}
		});

		btnExcluir.setIcon(new ImageIcon(getClass().getResource("/icon/Delete.png")));
		btnExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				inativarDados(getEntidade().getSimpleName());
			}
		});

		btnPrimeiro.setIcon(new ImageIcon(getClass().getResource("/icon/Symbol_Rewind.png")));
		btnPrimeiro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irPrimeiraPagina();
			}
		});

		btnAnterior.setIcon(new ImageIcon(getClass().getResource("/icon/Symbol_Play_Reversed.png")));
		btnAnterior.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irPaginaAnterior();
			}
		});

		btnProximo.setIcon(new ImageIcon(getClass().getResource("/icon/Symbol_Play.png")));
		btnProximo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irProximaPagina();
			}
		});

		btnUltimo.setIcon(new ImageIcon(getClass().getResource("/icon/Symbol_FastForward.png")));
		btnUltimo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irUltimaPagina();
			}
		});
		
		btnAtualizar.setIcon(new ImageIcon(getClass().getResource("/icon/refresh.png")));
		btnAtualizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setPagina(1);
				iniciarDados();
			}
		});
		
		btnEditar.setIcon(new ImageIcon(getClass().getResource("/icon/Edit-Document-icon.png")));
		btnEditar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirFormularioEdicao();
			}
		});
		
		btnPesquisar.setIcon(new ImageIcon(getClass().getResource("/icon/Search.png")));
		btnPesquisar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					modalPesquisa = getModalPesquisa().newInstance();
					modalPesquisa.btnSelecionar.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							buscarDados(0);
							validarBtnPaginacao();
							modalPesquisa.setVisible(false);
						}
					});
					
					modalPesquisa.setVisible(true);
				} catch (InstantiationException | IllegalAccessException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		final Map<Integer, Field> campos = getCamposTabela();
		final List<String> titulos = new ArrayList<String>();

		for (final Field f : campos.values()) {
			titulos.add(f.getAnnotation(ColunaTabela.class).titulo());
		}

		model = new ListagemTableModel(new Object[][] {}, titulos.toArray());
		model.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				boolean selecionado = isObjetoSelecionadoNaTabela();
				btnExcluir.setEnabled(selecionado);
				btnEditar.setEnabled(selecionado);
				
				if (tabela.getRowCount() > 0) {
					int selectedRow = tabela.getSelectedRow();
					if (selectedRow > -1) {
						model.getValueAt(selectedRow).setSelecionado((boolean) tabela.getValueAt(selectedRow, 0));
					}
				}
			}
		});
		
		tabela.setModel(model);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(5);
		
		setTitle(getTituloFrame());
	}
	
	protected void filtrarDados() {
		
	}
	
	protected Class<? extends DialogPesquisa<? extends FiltroDTO>> getModalPesquisa() {
		return null;
	}
	
	protected Map<String, Object> getMapaFiltros() {
		Map<String, Object> mp = new java.util.HashMap<>();
		return mp;
	}

	protected void abrirFormularioEdicao() {
		Class<? extends CadastroForm<? extends Entidade>> formCadastro = getFormCadastro();
		if (ObjetoUtil.isReferencia(formCadastro)) {
			for (T d : getDados()) {
				if (d.isSelecionado()) {
					try {
						CadastroForm<? extends Entidade> form = formCadastro.newInstance();
						form.setEntidade(HibernateUtil.getCriteriaBuilder(getEntidade()).eqId(d.getId()).uniqueResult());
						PropertyUtils.setProperty(form, "txtDescricao.text", "Teste");
						PrincipalForm.desktop.add(form);
						form.show();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}
	
	public void iniciarDados() {
		buscarDados(0);
		validarBtnPaginacao();
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
		validarBtnPaginacao();
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

	protected void inativarDados(final String nomeClasse) {
		if (tabela.getRowCount() > 0) {
			int result = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esse(s) dado(s)?");
			if (result == JOptionPane.YES_OPTION) {
				for (int i = 0; i < tabela.getRowCount(); i++) {
					T dto = model.getValueAt(i);
					if (dto.isSelecionado()) {
						HibernateUtil.inativar(dto.getId(), nomeClasse);
					}
				}
			}
	
			iniciarDados();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void buscarDados(final int primResultado, final CriteriaBuilder builderListagem, final CriteriaBuilder builderQntDados) {
		getModel().limparTabela();
		setDados(builderListagem.getCriteria().setFirstResult(primResultado).setMaxResults(MAX_REGISTROS).list());
		setQntRegistros((Long) builderQntDados.getCriteria().setProjection(Projections.rowCount()).uniqueResult());

		final Map<Integer, Field> campos = getCamposTabela();

		for (final T d : getDados()) {
			final List<Object> valores = new ArrayList<>();
			for (final Field f : campos.values()) {
				try {
					Object property = BeanUtils.getProperty(d, f.getName());
					
					if (ObjetoUtil.isReferencia(property)) {
						String str = property.toString();
						if (str.equalsIgnoreCase(Boolean.TRUE.toString()) ||
								str.equalsIgnoreCase(Boolean.FALSE.toString())) {
							property = new Boolean(str);
						} else if (str.matches("\\d{4}-\\d{2}-\\d{2}")) {
							Date data = new SimpleDateFormat("yyyy-MM-dd").parse(str);
							property = new SimpleDateFormat("dd/MM/yyyy").format(data);
						} else if (str.matches("^[+-]?[0-9]{1,3}(?:[0-9]{3})*(\\.[0-9]{2})$")) {
							property = NumberUtil.obterNumeroFormatado(new BigDecimal(str));
						}
					}
					
					valores.add(property);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
					Logger.getLogger(ListagemForm.class.getName()).log(Level.SEVERE, null, ex);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			getModel().addRow(valores.toArray());
		}
	}

	protected void buscarDados(final int primResultado) {
		CriteriaBuilder builderListagem = getBuilderListagem();
		CriteriaBuilder builderQntDados = getBuilderQntRegistros();
		
		montarRestricaoFiltro(builderListagem);
		montarRestricaoFiltro(builderQntDados);
		
		buscarDados(primResultado, builderListagem, builderQntDados);
	}

	protected final void montarRestricaoFiltro(CriteriaBuilder builder) {
		if (ObjetoUtil.isReferencia(modalPesquisa)) {
			Map<String, Object> mp = getMapaFiltros();
			for (Entry<String, Object> entry :  mp.entrySet()) {
				Object value = entry.getValue();
				if (ObjetoUtil.isReferencia(value)) {
					if (value instanceof String)
						builder.ilike(entry.getKey(), value.toString(), Criterion.ANYWHERE);
					else if (value instanceof Entidade)
						builder.eq(entry.getKey(), ((Entidade) value).getId());
					else
						builder.eq(entry.getKey(), value);
				}
			}
		}
	}
	
	private Map<Integer, Field> getCamposTabela() throws SecurityException {
		final Map<Integer, Field> campos = new TreeMap<Integer, Field>();
		final Class<T> clazz = obterTipoDaClasse();
		for (final Field field : FieldUtil.getAllFields(clazz)) {
			if (field.isAnnotationPresent(ColunaTabela.class)) {
				campos.put(field.getAnnotation(ColunaTabela.class).index(), field);
			}
		}
		return campos;
	}
	
	private boolean isObjetoSelecionadoNaTabela() {
		for (int i = 0; i < tabela.getRowCount(); i++) {
			if ((boolean) tabela.getValueAt(i, 0)) {
				return true;
			}
		}
		
		return false;
	}

	@SuppressWarnings({ "unchecked", "restriction" })
	protected Class<T> obterTipoDaClasse() {
		return (Class<T>) ((sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public JPanel getPanelPaginacao() {
		JPanel panelPaginacao = new JPanel(new MigLayout());
		panelPaginacao.setBorder(new EtchedBorder());
		panelPaginacao.add(btnPrimeiro);
		panelPaginacao.add(btnAnterior);
		panelPaginacao.add(lbPaginacao);
		panelPaginacao.add(btnProximo);
		panelPaginacao.add(btnUltimo, "pushx 1");
		panelPaginacao.add(btnEditar);
		panelPaginacao.add(btnExcluir);
		panelPaginacao.add(btnAtualizar);

		JPanel panel = new JPanel(new MigLayout());
		panel.add(scroll, "wrap, grow, push");
		panel.add(panelPaginacao, "growx");
		
		return panel;
	}
	
	protected abstract CriteriaBuilder getBuilderListagem();

	protected abstract CriteriaBuilder getBuilderQntRegistros();

	protected abstract Class<?> getEntidade();

	protected abstract String getTituloFrame();
	
	protected abstract Class<? extends CadastroForm<? extends Entidade>> getFormCadastro();

	class ListagemTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 1L;
		
		public ListagemTableModel(Object[][] objects, Object[] array) {
			super(objects, array);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return Boolean.class;
			}
			
			return Object.class;
		}
		
		public T getValueAt(int row) {
			return getDados().get(row);
		}
		
		public void limparTabela() {
			while (getRowCount() > 0) {
				removeRow(0);
			}
		}
	}
	
	public List<T> getDados() {
		return dados;
	}

	public void setDados(final List<T> dados) {
		this.dados = dados;
	}

	public int getPagina() {
		return pagina;
	}

	public void setPagina(final int pagina) {
		this.pagina = pagina;
	}

	public Long getQntRegistros() {
		return qntRegistros;
	}

	public void setQntRegistros(final Long qntRegistros) {
		this.qntRegistros = qntRegistros;
	}

	public ListagemTableModel getModel() {
		return model;
	}
	
}
