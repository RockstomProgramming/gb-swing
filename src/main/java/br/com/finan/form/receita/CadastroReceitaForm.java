package br.com.finan.form.receita;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Wesley Luiz
 */
public class CadastroReceitaForm extends JFrame {

    public CadastroReceitaForm() {
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JTable tabela = new JTable();
        tabela.setBorder(new LineBorder(Color.BLACK));
        tabela.setGridColor(Color.BLACK);
        tabela.setShowGrid(true);
        tabela.setSize(450, 800);
        tabela.setVisible(true);

        JScrollPane scroll = new JScrollPane();
        scroll.getViewport().setBorder(null);
        scroll.getViewport().add(tabela);
        scroll.setSize(450, 450);

        JPanel panel = new JPanel();
        panel.setBorder(new BevelBorder(1));
        panel.add(new JButton("<"));
        panel.add(new JComboBox());
        panel.add(new JButton(">"));

        add(panel);
        add(scroll);

        pack();
        setSize(800, 600);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CadastroReceitaForm().setVisible(true);
            }
        });
    }

}
