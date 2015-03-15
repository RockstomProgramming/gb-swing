package br.com.finan.form.principal;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import br.com.finan.form.principal.PanelPesquisaConta.PesquisaDTO;

import net.miginfocom.swing.MigLayout;

public class DialogPesquisaConta extends JDialog {

	private static final long serialVersionUID = 1L;
	private PanelPesquisaConta pnlPesquisa;

	public DialogPesquisaConta() {
		pnlPesquisa = new PanelPesquisaConta();

		pnlPesquisa.getBtnCancelar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		pnlPesquisa.getBtnSelecionar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PesquisaDTO dto = pnlPesquisa.getPesquisaDTO();
			}
		});

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - getWidth()) / 2;
		final int y = (screenSize.height - getHeight()) / 2;
		setLocation(x, y);

		add(pnlPesquisa);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new MigLayout());
		setTitle("Pesquisa");
		setModal(true);
		pack();
	}
}
