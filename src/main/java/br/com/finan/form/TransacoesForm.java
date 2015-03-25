package br.com.finan.form;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import net.miginfocom.swing.MigLayout;
import net.sf.ofx4j.domain.data.common.Transaction;
import net.sf.ofx4j.domain.data.common.TransactionType;

import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.observablecollections.ObservableCollections;

import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.Config;
import br.com.finan.entidade.Conta;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.enumerator.FormaPagamento;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.service.ContaService;
import br.com.finan.util.AppUtil;
import br.com.finan.util.BankingUtil;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;

/**
 *
 * @author Wesley Luiz
 */
public class TransacoesForm extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private static final String TITULO_FRAME = "Transações Bancárias";

	private JComboBox<Categoria> cmbCategoria;
	private JComboBox<ContaBancaria> cmbContaBancaria;
	private JButton btnSalvar;
	private JButton btnAbrir;
	private JButton btnLimpar;
	private JTable tabela;
	private JScrollPane scroll;
	private List<Transaction> transacoes = ObservableCollections.observableList(new ArrayList<Transaction>());
	
	private ContaService contaService;

	public TransacoesForm() {
		iniciarComponentes();
	}
	
	public void iniciarComponentes() {
		btnAbrir = new JButton("Abrir", new ImageIcon(getClass().getResource("/icon/Folder.png")));
		btnSalvar = new JButton("Salvar", new ImageIcon(getClass().getResource("/icon/Save.png")));
		btnLimpar = new JButton("Limpar", new ImageIcon(getClass().getResource("/icon/Delete.png")));
		
		cmbCategoria = new JComboBox<Categoria>();
		cmbContaBancaria = new JComboBox<ContaBancaria>();
		
		tabela = new JTable();
		scroll = new JScrollPane();
		contaService = new ContaService();

		cmbCategoria.setPreferredSize(new Dimension(200, 0));
		cmbContaBancaria.setPreferredSize(new Dimension(200, 0));

		scroll.setViewportView(tabela);
		scroll.setSize(800, 800);

		addAcoes();
		addBinding().bind();

		final JPanel pnlAcao = new JPanel(new MigLayout());
		pnlAcao.add(btnAbrir);
		pnlAcao.add(btnSalvar);
		pnlAcao.add(btnLimpar);

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
			.addJTableBinding(transacoes, tabela)
				.addColumnBinding(0, "${memo}", "Descrição")
				.addColumnBinding(1, "${datePosted}", "Vencimento", Date.class)
				.addColumnBinding(2, "${amount}", "Valor", Double.class).close()
			.addJComboBoxBinding(HibernateUtil.getCriteriaBuilder(Categoria.class).eqStatusAtivo().list(), cmbCategoria)
			.addJComboBoxBinding(HibernateUtil.getCriteriaBuilder(ContaBancaria.class).eqStatusAtivo().list(), cmbContaBancaria);
		return bindingGroup;
	}

	private void addAcoes() {
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				salvar();
			}
		});

		btnAbrir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirSelecionadorDeArquivos();
			}
		});
		
		btnLimpar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				transacoes.clear();
			}
		});
	}

	private void abrirSelecionadorDeArquivos() {
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
				transacoes.addAll(tr);

				conf.setPath(file.getAbsolutePath());
				HibernateUtil.salvarOuAlterar(conf);

			} catch (final FileNotFoundException ex) {
				Logger.getLogger(TransacoesForm.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private void salvar() {
		for (final Transaction t : transacoes) {
			final Conta conta = new Conta();
			conta.setDataVencimento(t.getDatePosted());
			conta.setDescricao(t.getMemo());
			conta.setIsPago(true);
			conta.setValor(new BigDecimal(t.getAmount()));
			if (t.getTransactionType().equals(TransactionType.DEBIT)) {
				conta.setTipo(TipoConta.DESPESA);
			} else if (t.getTransactionType().equals(TransactionType.CREDIT)) {
				conta.setTipo(TipoConta.RECEITA);
			}
			conta.setCategoria((Categoria) cmbCategoria.getSelectedItem());
			conta.setContaBancaria((ContaBancaria) cmbContaBancaria.getSelectedItem());
			conta.setFormaPagamento(FormaPagamento.AVISTA);
			HibernateUtil.salvar(conta);
		}

		transacoes.clear();
		AppUtil.exibirMsgSalvarSucesso(this);
		getContaService().atualizarSaldoFramePrincipal();
	}

	public ContaService getContaService() {
		return contaService;
	}
	

	public List<Transaction> getTransacoes() {
		return transacoes;
	}
}
