package br.com.finan.form.principal;

import br.com.finan.form.despesa.CadastroDespesaForm;
import br.com.finan.form.despesa.ListagemDespesaForm;
import br.com.finan.form.receita.CadastroReceitaForm;
import br.com.finan.form.receita.ListagemReceitaForm;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;

/**
 *
 * @author Wesley Luiz
 */
public class PrincipalForm extends javax.swing.JFrame {

    /**
     * Creates new form Main
     */
    public PrincipalForm() {
        initComponents();
        iniciarDados();
        abrirFrame(CadastroReceitaForm.class, NomeFrame.CADASTRO_RECEITA_FRAME.toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktop = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuCadastro = new javax.swing.JMenu();
        menuCadReceita = new javax.swing.JMenuItem();
        menuCadDespsea = new javax.swing.JMenuItem();
        menuListagens = new javax.swing.JMenu();
        menuListReceita = new javax.swing.JMenuItem();
        menuListDespesa = new javax.swing.JMenuItem();
        menuSair = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        desktop.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        menuCadastro.setText("Cadastros");

        menuCadReceita.setText("Receitas");
        menuCadastro.add(menuCadReceita);

        menuCadDespsea.setText("Despesas");
        menuCadastro.add(menuCadDespsea);

        jMenuBar1.add(menuCadastro);

        menuListagens.setText("Listagens");

        menuListReceita.setText("Receitas");
        menuListagens.add(menuListReceita);

        menuListDespesa.setText("Despesas");
        menuListagens.add(menuListDespesa);

        jMenuBar1.add(menuListagens);

        menuSair.setText("Sair");
        jMenuBar1.add(menuSair);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 946, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem menuCadDespsea;
    private javax.swing.JMenuItem menuCadReceita;
    private javax.swing.JMenu menuCadastro;
    private javax.swing.JMenuItem menuListDespesa;
    private javax.swing.JMenuItem menuListReceita;
    private javax.swing.JMenu menuListagens;
    private javax.swing.JMenu menuSair;
    // End of variables declaration//GEN-END:variables

    private void iniciarDados() {
        menuListDespesa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFrame(ListagemDespesaForm.class, NomeFrame.LISTAGEM_DESPESA_FRAME.toString());
            }
        });

        menuListReceita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFrame(ListagemReceitaForm.class, NomeFrame.LISTAGEM_RECEITA_FRAME.toString());
            }
        });

        menuCadDespsea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFrame(CadastroDespesaForm.class, NomeFrame.CADASTRO_DESPESA_FRAME.toString());
            }
        });

        menuCadReceita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFrame(CadastroReceitaForm.class, NomeFrame.CADASTRO_RECEITA_FRAME.toString());
            }
        });

        menuSair.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
    }

    private void abrirFrame(Class<? extends JInternalFrame> clazz, String nome) {
        if (!contemFrame(nome)) {
            try {
                JInternalFrame frame = clazz.newInstance();
                frame.setName(nome);
                desktop.add(frame);
                frame.show();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(PrincipalForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean contemFrame(String nomeFrame) {
        Component[] components = desktop.getComponents();
        for (Component comp : components) {
            if (comp.getName().equals(nomeFrame)) {
                return true;
            }
        }

        return false;
    }

    enum NomeFrame {

        CADASTRO_DESPESA_FRAME,
        CADASTRO_RECEITA_FRAME,
        LISTAGEM_DESPESA_FRAME,
        LISTAGEM_RECEITA_FRAME;
    }
}
