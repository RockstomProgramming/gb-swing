package br.com.finan.util;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author Wesley Luiz
 */
public final class AppUtil {

	public static final String MODAL_INFO = "Informação";
	public static final String MSG_SALVAR_SUCESSO = "Os dados foram salvos com sucesso!";

	public static void exibirMsgSalvarSucesso(JComponent comp) {
		JOptionPane.showMessageDialog(comp, AppUtil.MSG_SALVAR_SUCESSO, AppUtil.MODAL_INFO, JOptionPane.INFORMATION_MESSAGE);
	}
}
