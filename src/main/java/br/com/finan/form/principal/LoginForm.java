package br.com.finan.form.principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.beansbinding.BindingGroup;

import br.com.finan.entidade.Config;
import br.com.finan.form.Formulario;
import br.com.finan.util.BindingUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;

public class LoginForm extends Formulario {

	private static final long serialVersionUID = 1L;
	
	private String senha;
	private boolean autenticado;
	
	public LoginForm() {
		JPasswordField txtSenha = new JPasswordField(20);
		
		JButton btnEntrar = new JButton();
		btnEntrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				entrar();
			}
		});
		
		JPanel panel = new JPanel(new MigLayout());
		panel.add(new JLabel("Digite a senha:"));
		panel.add(txtSenha);
		
		add(panel);
		
		BindingUtil.create(new BindingGroup())
			.add(txtSenha, "${senha}", txtSenha)
			.getBindingGroup().bind();
	}

	private void entrar() {
		Config config = (Config) HibernateUtil.getCriteriaBuilder(Config.class).eq("senha", getSenha()).uniqueResult();
		setAutenticado(ObjetoUtil.isReferencia(config));
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isAutenticado() {
		return autenticado;
	}

	public void setAutenticado(boolean autenticado) {
		this.autenticado = autenticado;
	}

}
