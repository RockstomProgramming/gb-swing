package br.com.finan.form.principal;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
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

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import org.hibernate.criterion.Projections;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.DTO;
import br.com.finan.entidade.annotation.ColunaTabela;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.NumberUtil;
import br.com.finan.util.StringUtil;

/**
 *
 * @author Wesley Luiz
 * @param <T>
 */
public abstract class ListagemForm<T extends DTO> extends javax.swing.JInternalFrame {

	/** Atributo serialVersionUID. */
	private static final long serialVersionUID = 1L;
	private List<T> dados;
	private int pagina = 1;
	private Long qntRegistros = 0L;
	private final DefaultTableModel model;
	protected final int MAX_REGISTROS = 15;

	protected javax.swing.JButton btnAnterior;
	protected javax.swing.JButton btnExcluir;
	protected javax.swing.JButton btnPrimeiro;
	protected javax.swing.JButton btnProximo;
	protected javax.swing.JButton btnSalvar;
	protected javax.swing.JButton btnUltimo;
	protected javax.swing.JScrollPane scroll;
	protected javax.swing.JLabel lbPaginacao;
	protected javax.swing.JPanel pnlPaginacao;
	protected javax.swing.JTable tabela;

	public ListagemForm() {
		scroll = new javax.swing.JScrollPane();
		tabela = new javax.swing.JTable();
		btnSalvar = new javax.swing.JButton();
		btnExcluir = new javax.swing.JButton();
		pnlPaginacao = new javax.swing.JPanel();
		lbPaginacao = new javax.swing.JLabel();
		btnAnterior = new javax.swing.JButton();
		btnProximo = new javax.swing.JButton();
		btnUltimo = new javax.swing.JButton();
		btnPrimeiro = new javax.swing.JButton();
		
		scroll.setViewportView(tabela);
		scroll.setPreferredSize(new Dimension(800, 280));
		
		setTitle(getTituloFrame());

		btnSalvar.setText("Salvar");
		btnSalvar.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {}
		});

		btnExcluir.setText("Excluir");
		btnExcluir.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				inativarDados(getNomeEntidade());
			}
		});

		btnPrimeiro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/first.png")));
		btnPrimeiro.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irPrimeiraPagina();
			}
		});

		btnAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/rewind.png")));
		btnAnterior.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irPaginaAnterior();
			}
		});

		btnProximo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/forward.png")));
		btnProximo.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irProximaPagina();
			}
		});

		btnUltimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/last.png")));
		btnUltimo.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				irUltimaPagina();
			}
		});

		final Map<Integer, Field> campos = getCamposTabela();
		final List<String> titulos = new ArrayList<String>();

		for (final Field f : campos.values()) {
			titulos.add(f.getAnnotation(ColunaTabela.class).titulo());
		}

		model = new DefaultTableModel(new Object[][] {}, titulos.toArray());

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
		lbPaginacao.setText("Exibindo " + getPagina() * MAX_REGISTROS + " de " + getQntRegistros() + " registros");
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
		panelPaginacao.add(lbPaginacao);
		panelPaginacao.add(btnPrimeiro);
		panelPaginacao.add(btnAnterior);
		panelPaginacao.add(btnProximo);
		panelPaginacao.add(btnUltimo);

		JPanel panel = new JPanel(new MigLayout());
		panel.add(scroll, "wrap, growx");
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
