package br.com.finan.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.beansbinding.BindingGroup;

import br.com.finan.entidade.Config;
import br.com.finan.form.principal.PrincipalForm;
import br.com.finan.util.AppUtil;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;

public class AutenticadorForm extends Formulario {

	private static final String TITULO_FRAME = "Configuração de Bloqueio";
	private static final long serialVersionUID = 1L;

	private Config config;

	public AutenticadorForm() {
		super();
		
		config = (Config) HibernateUtil.getCriteriaBuilder(Config.class).uniqueResult();

		if (!ObjetoUtil.isReferencia(config)) {
			config = new Config();
		}

		final JPasswordField txtSenha = new JPasswordField(20);
		final JCheckBox ckSenha = new JCheckBox("Bloquear com senha");

		final JButton btnSalvar = new JButton("Salvar", new ImageIcon(getClass().getResource("/icon/Save.png")));
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				salvar();
			}
		});

		final JPanel panel = new JPanel(new MigLayout());
		panel.add(new JLabel("Senha:"));
		panel.add(txtSenha, "wrap");
		panel.add(ckSenha, "spanx2, wrap");
		panel.add(btnSalvar, "spanx2");

		BindingUtil.create(new BindingGroup())
		.add(this, "${config.senha}", txtSenha)
		.add(this, "${config.bloquear}", ckSenha, "selected")
		.add(ckSenha, "${selected}", txtSenha, "enabled")
		.getBindingGroup().bind();

		setClosable(true);
		setTitle(TITULO_FRAME);
		add(panel);
		pack();
	}

	private void salvar() {
		HibernateUtil.salvarOuAlterar(config);
		AppUtil.exibirMsgSalvarSucesso(this);
		hide();
		PrincipalForm.desktop.remove(this);
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(final Config config) {
		this.config = config;
	}
}
