package br.com.finan.form.principal;

import javax.persistence.Persistence;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import br.com.finan.entidade.Config;
import br.com.finan.service.ContaService;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;

public class Main {
	
	public static void main(final String args[]) {
		HibernateUtil.factory = Persistence.createEntityManagerFactory("finan-unit");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception ex) {
			java.util.logging.Logger.getLogger(PrincipalForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Config config = (Config) HibernateUtil.getCriteriaBuilder(Config.class).uniqueResult();
				boolean isBloquear = ObjetoUtil.isReferencia(config) && config.isBloquear();
				JPasswordField txtSenha = new JPasswordField(20);

				if (isBloquear) {
					JPanel panel = new JPanel(new MigLayout());
					panel.add(new JLabel("Insira a senha:"));
					panel.add(txtSenha);
					
					String[] options = new String[] {"Entrar", "Cancelar"};
						
					int res = JOptionPane.showOptionDialog(null, panel, "Senha", JOptionPane.NO_OPTION, 
							JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
					
					if (res == 1) {
						return;
					}
				}
				
				if (!isBloquear || new String(txtSenha.getPassword()).equals(config.getSenha())) {
					new PrincipalForm().setVisible(true);
					new ContaService().atualizarSaldoFramePrincipal();
				}
			}
		});
	}
}
