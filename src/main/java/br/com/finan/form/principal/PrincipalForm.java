package br.com.finan.form.principal;

import br.com.finan.form.despesa.CadastroDespesaForm;
import br.com.finan.form.despesa.ListagemDespesaForm;
import java.awt.Component;
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
        abrirFrame(new ListagemDespesaForm(), NomeFrame.LISTAGEM_DESPESA_FRAME.toString());
//        abrirFrame(new CadastroReceitaForm(), NomeFrame.CADASTRO_RECEITA_FRAME.toString());
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
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        desktop.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setText("Abrir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktop)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(881, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        abrirFrame(new CadastroDespesaForm(), NomeFrame.CADASTRO_DESPESA_FRAME.toString());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void abrirFrame(JInternalFrame frame, String nome) {
        frame.setName(nome);
        if (!contemFrame(frame)) {
            desktop.add(frame);
            frame.show();
        }
    }

    public boolean contemFrame(JInternalFrame frame) {
        Component[] components = desktop.getComponents();
        for (Component comp : components) {
            if (comp.getName().equals(frame.getName())) {
                return true;
            }
        }

        return false;
    }

    enum NomeFrame {

        CADASTRO_DESPESA_FRAME,
        CADASTRO_RECEITA_FRAME,
        LISTAGEM_DESPESA_FRAME;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables
}
