package br.com.finan.form;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;
import net.sf.ofx4j.domain.data.common.Transaction;
import net.sf.ofx4j.domain.data.common.TransactionType;

import org.jdesktop.beansbinding.BindingGroup;

import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.Config;
import br.com.finan.entidade.Conta;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.util.AppUtil;
import br.com.finan.util.BankingUtil;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.NumberUtil;
import br.com.finan.util.ObjetoUtil;

/**
 *
 * @author Wesley Luiz
 */
public class TransacoesForm extends JInternalFrame {

	/** Atributo serialVersionUID. */
	private static final long serialVersionUID = 1L;
	private static final String TITULO_FRAME = "Transações Bancárias";
	
	private JComboBox<Categoria> cmbCategoria;
	private JComboBox<ContaBancaria> cmbContaBancaria;
	private JButton btnSalvar;
	private JButton btnAbrir;
	private JTable tabela;
	private JScrollPane scroll;
	private TransacaoTableModel<Transaction> model;
	
	public TransacoesForm() {
		iniciarComponentes();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void iniciarComponentes() {
		btnAbrir = new JButton("Abrir", new ImageIcon(getClass().getResource("/icon/Folder.png")));
		btnSalvar = new JButton("Salvar", new ImageIcon(getClass().getResource("/icon/Save.png")));
		cmbCategoria = new JComboBox<Categoria>();
		cmbContaBancaria = new JComboBox<ContaBancaria>();
		model = new TransacaoTableModel(null, new String[] { "Descrição", "Data", "Valor" });
		tabela = new JTable(model);
		scroll = new JScrollPane();

		cmbCategoria.setPreferredSize(new Dimension(200, 0));
		cmbContaBancaria.setPreferredSize(new Dimension(200, 0));
		
		scroll.setViewportView(tabela);
		scroll.setSize(800, 800);

		addAcoes(model);
		addBinding().bind();
		
		final JPanel pnlAcao = new JPanel(new MigLayout());
		pnlAcao.add(btnAbrir);
		pnlAcao.add(btnSalvar);
		
		JPanel pnlDados_1 = new JPanel(new MigLayout());
		pnlDados_1.add(new JLabel("Categoria:"));
		pnlDados_1.add(cmbCategoria);
		
		JPanel pnlDados_2 = new JPanel(new MigLayout());
		pnlDados_2.add(new JLabel("Conta Bancária:"));
		pnlDados_2.add(cmbContaBancaria);
		
		JPanel pnlDados = new JPanel(new MigLayout());
		pnlDados.setBorder(new EtchedBorder());
		pnlDados.add(pnlDados_1, "growx");
		pnlDados.add(pnlDados_2, "growx");

		final JPanel pnl = new JPanel(new MigLayout("wrap 1"));
		pnl.add(pnlAcao);
		pnl.add(pnlDados, "growx");
		pnl.add(scroll, "grow, push");

		add(pnl);
		setMaximizable(true);
		setClosable(true);
		setResizable(true);
		setTitle(TITULO_FRAME);
		pack();
	}

	private BindingGroup addBinding() {
		BindingGroup bindingGroup = new BindingGroup();
			BindingUtil.create(bindingGroup)
				.addJComboBoxBinding(HibernateUtil.getCriteriaBuilder(Categoria.class).eqStatusAtivo().list(), cmbCategoria)
				.addJComboBoxBinding(HibernateUtil.getCriteriaBuilder(ContaBancaria.class).eqStatusAtivo().list(), cmbContaBancaria);
		return bindingGroup;
	}

	private void addAcoes(final TransacaoTableModel<Transaction> model) {
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				salvar(model);
			}
		});

		btnAbrir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirSelecionadorDeArquivos(model);
			}
		});
	}

	private void abrirSelecionadorDeArquivos(final TransacaoTableModel<Transaction> model) {
		final JFileChooser fc = new JFileChooser();
		Config conf = (Config) HibernateUtil.getCriteriaBuilder(Config.class).uniqueResult();
		
		if (!ObjetoUtil.isReferencia(conf)) {
			conf = new Config();
		}
		
		if (ObjetoUtil.isReferencia(conf.getPath()) && !conf.getPath().isEmpty()) {
			fc.setCurrentDirectory(new File(conf.getPath()));
		}
		
		fc.addChoosableFileFilter(new FileNameExtensionFilter("ofx", "ofx"));
		final int result = fc.showOpenDialog(this);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			final File file = fc.getSelectedFile();
			try {
				final FileInputStream in = new FileInputStream(file);
				final List<Transaction> tr = BankingUtil.obterTransacoesArquivoOfx(in);
				model.clear();
				model.setDados(tr);
				for (final Transaction t : tr) {
					Double amount = t.getAmount();
					if (amount < 0) {
						t.setAmount(amount * (-1));
					}
					model.addRow(new Object[] { t.getMemo(), new SimpleDateFormat("dd/MM/yyyy").format(t.getDatePosted()), NumberUtil.obterNumeroFormatado(t.getAmount()) });
				}
				
				conf.setPath(file.getAbsolutePath());
				HibernateUtil.salvarOuAlterar(conf);
				
			} catch (final FileNotFoundException ex) {
				Logger.getLogger(TransacoesForm.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	private void salvar(final TransacaoTableModel<Transaction> model) {
		for (final Transaction t : model.getDados()) {
			final Conta conta = new Conta();
			conta.setDataVencimento(t.getDatePosted());
			conta.setDescricao(t.getMemo());
			conta.setIsPago(true);
			conta.setValor(new BigDecimal(t.getAmount()));
			conta.setTipo(t.getTransactionType().equals(TransactionType.DEBIT) ? TipoConta.DESPESA : TipoConta.RECEITA);
			conta.setCategoria((Categoria) cmbCategoria.getSelectedItem());
			conta.setContaBancaria((ContaBancaria) cmbContaBancaria.getSelectedItem());
			HibernateUtil.salvar(conta);
		}

		model.clear();
		AppUtil.exibirMsgSalvarSucesso(this);
	}

	class TransacaoTableModel<T> extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		private List<T> dados;

		public TransacaoTableModel(final Object[][] data, final Object[] columnNames) {
			super(data, columnNames);
		}

		public T getValueAt(final int row) {
			if (ObjetoUtil.isReferencia(dados) && !dados.isEmpty()) {
				return dados.get(row);
			}
			return null;
		}

		public void clear() {
			if (ObjetoUtil.isReferencia(dados)) {
				for (int i = 0; i < dados.size(); i++) {
					this.removeRow(0);
				}
			}
		}

		public void setDados(final List<T> dados) {
			this.dados = dados;
		}

		public List<T> getDados() {
			return dados;
		}

	}
}
