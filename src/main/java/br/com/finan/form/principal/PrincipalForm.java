package br.com.finan.form.principal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.observablecollections.ObservableCollections;

import br.com.finan.entidade.ContaBancaria;
import br.com.finan.form.AutenticadorForm;
import br.com.finan.form.CadastroCategoriaForm;
import br.com.finan.form.CadastroContaBancariaForm;
import br.com.finan.form.CadastroDespesaForm;
import br.com.finan.form.CadastroReceitaForm;
import br.com.finan.form.GraficoContaForm;
import br.com.finan.form.RelatorioContaForm;
import br.com.finan.form.TransacoesForm;
import br.com.finan.util.AppUtil;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.ObjetoUtil;

/**
 *
 * @author Wesley Luiz
 */
public class PrincipalForm extends JFrame {

	private static final String TITULO_FRAME = "Gerenciador Financeiro";
	private static final long serialVersionUID = 1L;

	private JButton btnImportar;
	private JMenuBar jMenuBar1;
	private JMenuItem menuCadDespsea;
	private JMenuItem menuCadReceita;
	private JMenu menuCadastro;
	private JMenu menuSair;
	private JMenuItem menuCadCategoria;
	private JMenuItem menuCadContaBancaria;

	public static JDesktopPane desktop;
	public static JLabel lbSaldo;
	public static List<ContaBancaria> contasBancarias;
	public static JComboBox<ContaBancaria> cmbConta;
	private JMenu menuRelatorio;
	private JMenuItem menuExtrato;
	private JMenuItem menuGrafico;
	private JMenuItem menuSenha;

	/**
	 * Creates new form Main
	 */
	public PrincipalForm() {
		initComponents();
		iniciarDados();
	}

	private void initComponents() {
		desktop = new JDesktopPane();
		btnImportar = new JButton();
		jMenuBar1 = new JMenuBar();
		menuCadastro = new JMenu();
		menuCadReceita = new JMenuItem();
		menuCadDespsea = new JMenuItem();
		menuSair = new JMenu();
		lbSaldo = new JLabel();

		desktop.setBorder(BorderFactory.createEtchedBorder());
		desktop.setBackground(Color.WHITE);

		menuCadastro.setText("Cadastros");

		menuCadReceita.setText("Receitas");
		menuCadastro.add(menuCadReceita);

		menuCadDespsea.setText("Despesas");
		menuCadastro.add(menuCadDespsea);

		jMenuBar1.add(menuCadastro);

		menuCadastro.add(new JSeparator());

		menuCadCategoria = new JMenuItem("Categoria");
		menuCadCategoria.setIcon(new ImageIcon(getClass().getResource("/icon/Paste.png")));
		menuCadastro.add(menuCadCategoria);

		menuCadContaBancaria = new JMenuItem("Conta Bancaria");
		menuCadContaBancaria.setIcon(new ImageIcon(getClass().getResource("/icon/User.png")));
		menuCadastro.add(menuCadContaBancaria);

		menuRelatorio = new JMenu("Relatórios");
		jMenuBar1.add(menuRelatorio);

		menuExtrato = new JMenuItem("Extrato");
		menuRelatorio.add(menuExtrato);

		menuGrafico = new JMenuItem("Gráfico");
		menuRelatorio.add(menuGrafico);

		final JMenu menuConfiguracoes = new JMenu("Configurações");
		jMenuBar1.add(menuConfiguracoes);

		menuSenha = new JMenuItem("Bloquear com senha");
		menuConfiguracoes.add(menuSenha);

		menuSair.setText("Sair");
		jMenuBar1.add(menuSair);

		btnImportar.setText("Importar (*.ofx)");
		btnImportar.setIcon(new ImageIcon(getClass().getResource("/icon/Arrow_Down.png")));

		cmbConta = new JComboBox<ContaBancaria>();
		cmbConta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				AppUtil.atualizarSaldoFramePrincipal();
			}
		});

		contasBancarias = ObservableCollections.observableList(new ArrayList<ContaBancaria>());

		BindingUtil.create(new BindingGroup())
		.addJComboBoxBinding(contasBancarias, cmbConta)
		.getBindingGroup().bind();

		AppUtil.atualizarContas();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setTitle(TITULO_FRAME);

		final JPanel pnlAtalhos = new JPanel(new MigLayout());
		pnlAtalhos.setBorder(new EtchedBorder());
		pnlAtalhos.add(btnImportar, "pushx");
		pnlAtalhos.add(cmbConta);
		pnlAtalhos.add(lbSaldo);

		setJMenuBar(jMenuBar1);
		getContentPane().setLayout(new MigLayout());
		getContentPane().add(pnlAtalhos, "growx, wrap");
		getContentPane().add(desktop, "push, grow, wrap");
		getContentPane().setPreferredSize(new Dimension(800, 600));

		pack();
	}

	private void iniciarDados() {
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

		menuCadCategoria.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirFrame(CadastroCategoriaForm.class, NomeFrame.CADASTRO_CATEGORIA_FRAME.toString());
			}
		});

		menuCadContaBancaria.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirFrame(CadastroContaBancariaForm.class, NomeFrame.CADASTRO_CONTA_BANCARIA_FRAME.toString());
			}
		});

		menuExtrato.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirFrame(RelatorioContaForm.class, NomeFrame.RELATORIO_CONTA_FRAME.toString());
			}
		});

		menuGrafico.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirFrame(GraficoContaForm.class, NomeFrame.GRAFICO_CONTA_FRAME.toString());
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

		menuSenha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				abrirFrame(AutenticadorForm.class, NomeFrame.AUTENTICADOR_FRAME.toString());
			}
		});
	}

	private void abrirFrame(final Class<? extends JInternalFrame> clazz, final String nome, final Object... args) {
		if (!ObjetoUtil.isReferencia(getFrameDesktop(nome))) {
			try {
				JInternalFrame frame = null;
				if (args.length > 0) {
					final List<Class<?>> tipos = new ArrayList<>();
					for (final Object arg : args) {
						tipos.add(arg.getClass());
					}
					try {
						frame = clazz.getDeclaredConstructor(tipos.toArray(new Class<?>[tipos.size()])).newInstance(args);
					} catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				} else {
					frame = clazz.newInstance();
				}
				frame.setFrameIcon(new ImageIcon(getClass().getResource("/icon/Stats.png")));
				frame.setName(nome);
				desktop.add(frame);
				frame.show();
			} catch (InstantiationException | IllegalAccessException ex) {
				ex.printStackTrace();
			}
		} else {
			final JInternalFrame frame = (JInternalFrame) getFrameDesktop(nome);
			try {
				frame.setSelected(true);
			} catch (final PropertyVetoException e) {
				e.printStackTrace();
			}
		}
	}

	public Component getFrameDesktop(final String nomeFrame) {
		final Component[] components = desktop.getComponents();
		for (final Component comp : components) {
			if (ObjetoUtil.isReferencia(comp.getName()) && comp.getName().equals(nomeFrame)) {
				return comp;
			}
		}

		return null;
	}

	enum NomeFrame {
		CADASTRO_DESPESA_FRAME,
		CADASTRO_RECEITA_FRAME,
		CADASTRO_CATEGORIA_FRAME,
		CADASTRO_CONTA_BANCARIA_FRAME,
		TRANSACOES_FRAME,
		RELATORIO_CONTA_FRAME,
		GRAFICO_CONTA_FRAME,
		AUTENTICADOR_FRAME,
		SALDO_FRAME;
	}
}
