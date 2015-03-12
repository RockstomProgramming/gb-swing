package br.com.finan.form.principal;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JInternalFrame;

import br.com.finan.form.categoria.CadastroCategoriaForm;
import br.com.finan.form.conta.CadastroContaBancariaForm;
import br.com.finan.form.despesa.CadastroDespesaForm;
import br.com.finan.form.despesa.ListagemDespesaForm;
import br.com.finan.form.receita.CadastroReceitaForm;
import br.com.finan.form.receita.ListagemReceitaForm;
import br.com.finan.form.transacao.TransacoesForm;

/**
 *
 * @author Wesley Luiz
 */
public class PrincipalForm extends javax.swing.JFrame {

	/** Atributo serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new form Main
	 */
	public PrincipalForm() {
		initComponents();
		iniciarDados();
		abrirFrame(CadastroContaBancariaForm.class, NomeFrame.CADASTRO_CONTA_BANCARIA_FRAME.toString());
	}

	private void initComponents() {

		desktop = new javax.swing.JDesktopPane();
		btnImportar = new javax.swing.JButton();
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

		btnImportar.setText("Importar");

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

		final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 946, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(btnImportar).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addGap(8, 8, 8).addComponent(btnImportar).addGap(18, 18, 18).addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)));

		pack();
	}
	
	private javax.swing.JButton btnImportar;
	private javax.swing.JDesktopPane desktop;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem menuCadDespsea;
	private javax.swing.JMenuItem menuCadReceita;
	private javax.swing.JMenu menuCadastro;
	private javax.swing.JMenuItem menuListDespesa;
	private javax.swing.JMenuItem menuListReceita;
	private javax.swing.JMenu menuListagens;
	private javax.swing.JMenu menuSair;

	private void iniciarDados() {
		menuListDespesa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirFrame(ListagemDespesaForm.class, NomeFrame.LISTAGEM_DESPESA_FRAME.toString());
			}
		});

		menuListReceita.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirFrame(ListagemReceitaForm.class, NomeFrame.LISTAGEM_RECEITA_FRAME.toString());
			}
		});

		menuCadDespsea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirFrame(CadastroDespesaForm.class, NomeFrame.CADASTRO_DESPESA_FRAME.toString());
			}
		});

		menuCadReceita.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirFrame(CadastroReceitaForm.class, NomeFrame.CADASTRO_RECEITA_FRAME.toString());
			}
		});

		btnImportar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirFrame(TransacoesForm.class, NomeFrame.TRANSACOES_FRAME.toString());
			}
		});

		menuSair.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				System.exit(0);
			}
		});
	}

	private void abrirFrame(final Class<? extends JInternalFrame> clazz, final String nome) {
		if (!contemFrame(nome)) {
			try {
				final JInternalFrame frame = clazz.newInstance();
				frame.setName(nome);
				desktop.add(frame);
				frame.show();
			} catch (InstantiationException | IllegalAccessException ex) {
				Logger.getLogger(PrincipalForm.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public boolean contemFrame(final String nomeFrame) {
		final Component[] components = desktop.getComponents();
		for (final Component comp : components) {
			if (comp.getName().equals(nomeFrame)) {
				return true;
			}
		}

		return false;
	}

	enum NomeFrame {
		CADASTRO_DESPESA_FRAME, 
		CADASTRO_RECEITA_FRAME,
		CADASTRO_CATEGORIA_FRAME,
		CADASTRO_CONTA_BANCARIA_FRAME,
		LISTAGEM_DESPESA_FRAME, 
		LISTAGEM_RECEITA_FRAME, 
		TRANSACOES_FRAME;
	}
}
