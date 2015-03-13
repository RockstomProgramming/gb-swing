package br.com.finan.form.principal;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import org.hibernate.criterion.Projections;

import br.com.finan.annotation.ColunaTabela;
import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.DTO;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.NumberUtil;
import br.com.finan.util.StringUtil;

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
	private final DefaultTableModel model;
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
		
		scroll.setViewportView(tabela);
		scroll.setPreferredSize(new Dimension(800, 280));
		
		setTitle(getTituloFrame());

		btnSalvar.setText("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {}
		});

		btnExcluir.setText("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				inativarDados(getNomeEntidade());
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
		

		final Map<Integer, Field> campos = getCamposTabela();
		final List<String> titulos = new ArrayList<String>();

		for (final Field f : campos.values()) {
			titulos.add(f.getAnnotation(ColunaTabela.class).titulo());
		}

		model = new DefaultTableModel(new Object[][] {}, titulos.toArray());
		
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabela.setModel(model);
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
		for (int i = 0; i < tabela.getRowCount(); i++) {
			final boolean b = (boolean) tabela.getValueAt(i, 0);
			if (b) {
				final T dto = getDados().get(i);
				HibernateUtil.inativar(dto.getId(), nomeClasse);
			}
		}

		iniciarDados();
	}

	@SuppressWarnings("unchecked")
	protected void buscarDados(final int primResultado) {
		limparTabela();
		setDados(getBuilderListagem().getCriteria().setFirstResult(primResultado).setMaxResults(MAX_REGISTROS).list());
		setQntRegistros((Long) getBuilderQntRegistros().getCriteria().setProjection(Projections.rowCount()).uniqueResult());

		final Map<Integer, Field> campos = getCamposTabela();

		for (final T d : getDados()) {
			final List<Object> valores = new ArrayList<>();
			for (final Field f : campos.values()) {
				try {
					final String ini = f.getType().getName().equals("boolean") ? "is" : "get";
					final Method m = d.getClass().getDeclaredMethod(ini + StringUtil.toPrimeiraLetraMaiuscula(f.getName()));
					final Object v = m.invoke(d);
					valores.add(formatarTipos(v));
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
					Logger.getLogger(ListagemForm.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			getModel().addRow(valores.toArray());
		}
	}

	private Map<Integer, Field> getCamposTabela() throws SecurityException {
		final Map<Integer, Field> campos = new TreeMap<Integer, Field>();
		final Class<T> clazz = obterTipoDaClasse();
		for (final Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(ColunaTabela.class)) {
				campos.put(field.getAnnotation(ColunaTabela.class).index(), field);
			}
		}
		return campos;
	}

	protected void limparTabela() {
		while (getModel().getRowCount() > 0) {
			getModel().removeRow(0);
		}
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
		panelPaginacao.add(btnAtualizar);

		JPanel panel = new JPanel(new MigLayout());
		panel.add(scroll, "wrap, grow, push");
		panel.add(panelPaginacao, "growx");
		
		return panel;
	}
	
	private Object formatarTipos(final Object v) {
		if (v instanceof BigDecimal) {
			return NumberUtil.obterNumeroFormatado(v);
		} else if (v instanceof Date) {
			return new SimpleDateFormat("dd/MM/yyyy").format(v);
		}

		return v;
	}

	protected abstract CriteriaBuilder getBuilderListagem();

	protected abstract CriteriaBuilder getBuilderQntRegistros();

	protected abstract String getNomeEntidade();

	protected abstract String getTituloFrame();

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

	public DefaultTableModel getModel() {
		return model;
	}
}
