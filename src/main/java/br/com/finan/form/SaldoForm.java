package br.com.finan.form;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import br.com.finan.entidade.ContaBancaria;

public class SaldoForm extends Formulario {

	private static final String TITULO_FRAME = "Saldo";
	private static final long serialVersionUID = 1L;

	public SaldoForm() {
		final JPanel panel = new JPanel(new MigLayout());
		for (final ContaBancaria c : getContaBancariaService().obterContasBancarias()) {
			final String saldo = getContaService().getSaldoAtual(c.getId());

			final JLabel lb = new JLabel(c.getDescricao().concat(":"));
			lb.setFont(new Font(null, Font.BOLD, 12));
			panel.add(lb);
			panel.add(new JLabel(saldo), "wrap");
		}

		add(panel);
		setClosable(true);
		setTitle(TITULO_FRAME);
		pack();
	}
}
