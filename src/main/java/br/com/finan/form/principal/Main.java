package br.com.finan.form.principal;

import javax.persistence.Persistence;
import javax.swing.UIManager;

import br.com.finan.util.HibernateUtil;

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
				new PrincipalForm().setVisible(true);
			}
		});
	}
}
