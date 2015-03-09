package br.com.finan.form.receita;

import br.com.finan.component.JMoneyField;
import br.com.finan.converter.BigDecimalConverter;
import br.com.finan.converter.DateConverter;
import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.Conta;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.entidade.enumerator.FormaPagamento;
import br.com.finan.entidade.enumerator.Frequencia;
import br.com.finan.entidade.enumerator.TipoConta;
import br.com.finan.form.principal.CadastroForm;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.HibernateUtil;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.beansbinding.BindingGroup;

/**
 * Constraints do mig layout (span, wrap, grow, gap, align, dock)
 *
 * @author Wesley Luiz
 */
public class CadastroReceitaForm extends CadastroForm<Conta> {

    public CadastroReceitaForm() {
        iniciarComponentes();
        iniciarDados();
    }

    JTextField txtDescricao;
    JFormattedTextField txtVencimento;
    JMoneyField txtValor;
    JComboBox<Categoria> txtCategoria;
    JComboBox<ContaBancaria> txtContaBancaria;
    JComboBox<FormaPagamento> txtFormaPagamento;
    JComboBox<Frequencia> txtRecorrencia;
    JTextField txtMaximo;
    JButton btnSalvar;

    private void iniciarComponentes() {

        try {
            txtDescricao = new JTextField(20);
            txtVencimento = new JFormattedTextField(new MaskFormatter("##/##/####"));
            txtValor = new JMoneyField();
            txtCategoria = new JComboBox<Categoria>();
            txtContaBancaria = new JComboBox<ContaBancaria>();
            txtFormaPagamento = new JComboBox<FormaPagamento>();
            txtRecorrencia = new JComboBox<Frequencia>();
            txtMaximo = new JTextField(10);
            btnSalvar = new JButton("Salvar");
            
            final BindingGroup bindingGroup = new BindingGroup();
            BindingUtil.create(bindingGroup)
                    .addJComboBoxBinding(Arrays.asList(Frequencia.values()), txtRecorrencia)
                    .add(this, "${entidade.descricao}", txtDescricao)
                    .add(this, "${entidade.dataVencimento}", txtVencimento, new DateConverter())
                    .add(this, "${entidade.valor}", txtValor, new BigDecimalConverter()) //                .add(this, "${entidade.categoria}", txtCategoria)
                    //                .add(this, "${entidade.contaBancaria}", txtContaBancaria)
                    //                .add(this, "${}", txtFormaPagamento)
                    //                .add(this, "${}", txtRecorrencia)
                    //                .add(this, "${}", txtMaximo)
                    ;
            
            btnSalvar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.print(getEntidade().getDescricao());
                }
            });

            setClosable(true);
            setMaximizable(true);

            JPanel pnlRecorrencia = new JPanel(new MigLayout());
            pnlRecorrencia.setBorder(new LineBorder(Color.black));
            pnlRecorrencia.add(new JLabel("Recorrência:"));
            pnlRecorrencia.add(txtRecorrencia, "wrap, grow");
            pnlRecorrencia.add(new JLabel("Limite:"));
            pnlRecorrencia.add(txtMaximo);

            JPanel pnlCad = new JPanel(new MigLayout("wrap 2"));
            pnlCad.add(new JLabel("Descrição:"));
            pnlCad.add(txtDescricao);
            pnlCad.add(new JLabel("Vencimento:"));
            pnlCad.add(txtVencimento, "grow");
            pnlCad.add(new JLabel("Valor (R$):"));
            pnlCad.add(txtValor, "grow");
            pnlCad.add(new JLabel("Categoria:"));
            pnlCad.add(txtCategoria, "grow");
            pnlCad.add(new JLabel("Conta Bancária:"));
            pnlCad.add(txtContaBancaria, "grow");
            pnlCad.add(new JLabel("Forma Pagamento:"));
            pnlCad.add(txtFormaPagamento, "grow");
            pnlCad.add(pnlRecorrencia);
            pnlCad.add(btnSalvar);

            add(pnlCad);

            bindingGroup.bind();
            pack();
            setSize(800, 600);

        } catch (ParseException ex) {
            Logger.getLogger(CadastroReceitaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void iniciarDados() {
        popularComboCategoria();
//        popularComboRecorrencia();
        popularComboFormaPagamento();
        getEntidade().setTipo(TipoConta.RECEITA);
    }

    private void popularComboFormaPagamento() {
        txtFormaPagamento.removeAllItems();
        for (FormaPagamento pag : Arrays.asList(FormaPagamento.values())) {
            txtFormaPagamento.addItem(pag);
        }
    }

    private void popularComboRecorrencia() {
        txtRecorrencia.removeAllItems();
        for (Frequencia freq : Arrays.asList(Frequencia.values())) {
            txtRecorrencia.addItem(freq);
        }
    }

    private void popularComboCategoria() {
        List<Categoria> categorias = HibernateUtil.getCriteriaBuilder(Categoria.class)
                .eqStatusAtivo().addProjection("id", "status", "nome").addAliasToBean(Categoria.class).list();

        txtCategoria.removeAllItems();

        for (Categoria cat : categorias) {
            txtCategoria.addItem(cat);
        }
    }

    @Override
    protected JInternalFrame getFrame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected JPanel getContainerCadastro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
