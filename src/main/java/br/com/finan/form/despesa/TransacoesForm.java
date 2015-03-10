package br.com.finan.form.despesa;

import br.com.finan.entidade.Conta;
import br.com.finan.entidade.enumerator.TipoConta;
import br.com.finan.util.AppUtil;
import br.com.finan.util.BankingUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.NumberUtil;
import br.com.finan.util.ObjetoUtil;
import java.awt.HeadlessException;
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
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import net.sf.ofx4j.domain.data.common.Transaction;
import net.sf.ofx4j.domain.data.common.TransactionType;

/**
 *
 * @author Wesley Luiz
 */
public class TransacoesForm extends JInternalFrame {
    
    private static final String TITULO_FRAME = "Transações Bancárias";
    
    public TransacoesForm() {
        iniciarComponentes();
    }

    public void iniciarComponentes() {
        JButton btnAbrir = new JButton("Abrir");
        JButton btnSalvar = new JButton("Salvar");

        final TransacaoTableModel<Transaction> model = new TransacaoTableModel(null, new String[]{"Descrição", "Data", "Valor"}) {
            Class[] types = new Class[]{String.class, String.class, Double.class};
        };

        JTable tabela = new JTable(model);

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(tabela);
        scroll.setSize(800, 600);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Transaction t : model.getDados()) {
                    Conta conta = new Conta();
                    conta.setDataVencimento(t.getDatePosted());
                    conta.setDescricao(t.getMemo());
                    conta.setIsPago(true);
                    conta.setValor(new BigDecimal(t.getAmount()));
                    conta.setTipo(t.getTransactionType().equals(TransactionType.DEBIT) ? TipoConta.DESPESA : TipoConta.RECEITA);
                    HibernateUtil.salvar(conta);
                }
                
                model.clear();
                AppUtil.exibirMsgSalvarSucesso();
            }
        });

        btnAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.addChoosableFileFilter(new FileNameExtensionFilter("ofx", "ofx"));
                int result = fc.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        FileInputStream in = new FileInputStream(file);
                        List<Transaction> tr = BankingUtil.obterTransacoesArquivoOfx(in);
                        model.setDados(tr);
                        for (Transaction t : tr) {
                            Double amount = t.getAmount();
                            if (amount < 0) {
                                amount = amount * (-1);
                            }
                            model.addRow(new Object[]{t.getMemo(), new SimpleDateFormat("dd/MM/yyyy").format(t.getDatePosted()), NumberUtil.obterNumeroFormatado(amount)});
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(TransacoesForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        JPanel pnlAcao = new JPanel(new MigLayout("wrap 2"));
        pnlAcao.add(btnAbrir);
        pnlAcao.add(btnSalvar);

        JPanel pnl = new JPanel(new MigLayout("wrap 1"));
        pnl.add(pnlAcao);
        pnl.add(scroll);

        add(pnl);
        setMaximizable(true);
        setClosable(true);
        setResizable(true);
        setTitle(TITULO_FRAME);
        pack();
    }

    class TransacaoTableModel<T> extends DefaultTableModel {

        private List<T> dados;

        public TransacaoTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        public T getValueAt(int row) {
            if (ObjetoUtil.isReferencia(dados) && !dados.isEmpty()) {
                return dados.get(row);
            }
            return null;
        }
        
        public void clear() {
            for (T t : dados) {
                this.removeRow(0);
            }
        }

        public void setDados(List<T> dados) {
            this.dados = dados;
        }

        public List<T> getDados() {
            return dados;
        }

    }
}
