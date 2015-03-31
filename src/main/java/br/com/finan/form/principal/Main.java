package br.com.finan.form.principal;

import javax.persistence.Persistence;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;
import br.com.finan.entidade.Config;
import br.com.finan.util.AppUtil;
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
				final Config config = (Config) HibernateUtil.getCriteriaBuilder(Config.class).uniqueResult();
				final boolean isBloquear = ObjetoUtil.isReferencia(config) && config.isBloquear();
				final JPasswordField txtSenha = new JPasswordField(20);

				if (isBloquear) {
					final JPanel panel = new JPanel(new MigLayout());
					panel.add(new JLabel("Insira a senha:"));
					panel.add(txtSenha);

					final String[] options = new String[] {"Entrar", "Cancelar"};

					final int res = JOptionPane.showOptionDialog(null, panel, "Senha", JOptionPane.NO_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

					if (res == 1) {
						return;
					}
				}

				if (!isBloquear || new String(txtSenha.getPassword()).equals(config.getSenha())) {
					new PrincipalForm().setVisible(true);
					AppUtil.atualizarSaldoFramePrincipal();
				}
			}
		});
	}
}
