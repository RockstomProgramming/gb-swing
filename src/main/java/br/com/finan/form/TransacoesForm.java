package br.com.finan.form;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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

import br.com.finan.converter.DateConverter;
import br.com.finan.converter.DoubleConverter;
import br.com.finan.dto.TransacaoDTO;
import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.Config;
import br.com.finan.entidade.Conta;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.enumerator.FormaPagamento;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.util.AppUtil;
import br.com.finan.util.BankingUtil;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;

/**
 *
 * @author Wesley Luiz
 */
public class TransacoesForm extends Formulario {

	private static final long serialVersionUID = 1L;
	private static final String TITULO_FRAME = "Transações Bancárias";

	private JComboBox<Categoria> cmbCategoria;
	private JComboBox<ContaBancaria> cmbContaBancaria;
	private JButton btnSalvar;
	private JButton btnAbrir;
	private JButton btnLimpar;
	private JTable tabela;
	private JScrollPane scroll;
	private final List<TransacaoDTO> transacoes = ObservableCollections.observableList(new ArrayList<TransacaoDTO>());

	public TransacoesForm() {
		iniciarComponentes();
	}

	private void iniciarComponentes() {
		btnAbrir = new JButton("Abrir", new ImageIcon(getClass().getResource("/icon/Folder.png")));
		btnSalvar = new JButton("Salvar", new ImageIcon(getClass().getResource("/icon/Save.png")));
		btnLimpar = new JButton("Limpar/Remover", new ImageIcon(getClass().getResource("/icon/Delete.png")));

		cmbCategoria = new JComboBox<Categoria>();
		cmbContaBancaria = new JComboBox<ContaBancaria>();

		tabela = new JTable();
		scroll = new JScrollPane();

		cmbCategoria.setPreferredSize(new Dimension(200, 0));
		cmbContaBancaria.setPreferredSize(new Dimension(200, 0));

		scroll.setViewportView(tabela);
		scroll.setSize(800, 800);

		addAcoes();
		addBinding().bind();

		tabela.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(cmbCategoria));

		final JPanel pnlAcao = new JPanel(new MigLayout());
		pnlAcao.add(btnAbrir);
		pnlAcao.add(btnSalvar);
		pnlAcao.add(btnLimpar);

		final JPanel pnlConta = new JPanel(new MigLayout());
		pnlConta.add(new JLabel("Conta Bancária:"));
		pnlConta.add(cmbContaBancaria);

		final JPanel pnlDados = new JPanel(new MigLayout());
		pnlDados.setBorder(new EtchedBorder());
		pnlDados.add(pnlConta, "growx");

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
		final BindingGroup bindingGroup = new BindingGroup();
		BindingUtil.create(bindingGroup)
		.addJTableBinding(transacoes, tabela)
		.addColumnBinding(0, "${descricao}", "Descrição")
		.addColumnBinding(1, "${data}", "Vencimento", new DateConverter(), String.class)
		.addColumnBinding(2, "${valor}", "Valor", new DoubleConverter(), String.class)
		.addColumnBinding(3, "${categoria}", "Categoria", Categoria.class).close()
		.addJComboBoxBinding(getCategoriaService().obterCategorias(), cmbCategoria)
		.addJComboBoxBinding(getContaBancariaService().obterContasBancarias(), cmbContaBancaria);

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
			public void actionPerformed(final ActionEvent e) {
				final int[] lns = tabela.getSelectedRows();
				if (lns.length > 0) {
					int q = 0;
					for (final int linha : lns) {
						transacoes.remove(linha - q);
						q++;
					}
				} else {
					transacoes.clear();
				}
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
				for (final Transaction transaction : tr) {
					final TransacaoDTO dto = new TransacaoDTO();
					dto.setData(transaction.getDatePosted());
					dto.setDescricao(transaction.getMemo());
					dto.setValor(transaction.getAmount());

					if (transaction.getTransactionType().equals(TransactionType.DEBIT)) {
						dto.setTipo(TipoConta.DESPESA);
					} else if (transaction.getTransactionType().equals(TransactionType.CREDIT)) {
						dto.setTipo(TipoConta.RECEITA);
					}

					transacoes.add(dto);
				}

				conf.setPath(file.getAbsolutePath());
				HibernateUtil.salvarOuAlterar(conf);

			} catch (final FileNotFoundException ex) {
				Logger.getLogger(TransacoesForm.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private void salvar() {
		for (final TransacaoDTO t : transacoes) {
			final Conta conta = new Conta();
			conta.setDataVencimento(t.getData());
			conta.setDescricao(t.getDescricao());
			conta.setTipo(t.getTipo());
			conta.setIsPago(true);
			conta.setValor(new BigDecimal(t.getValor()));
			conta.setContaBancaria((ContaBancaria) cmbContaBancaria.getSelectedItem());
			conta.setFormaPagamento(FormaPagamento.AVISTA);
			conta.setCategoria(t.getCategoria());
			HibernateUtil.salvar(conta);
		}

		transacoes.clear();
		AppUtil.exibirMsgSalvarSucesso(this);
		AppUtil.atualizarSaldoFramePrincipal();
	}

	public List<TransacaoDTO> getTransacoes() {
		return transacoes;
	}
}
