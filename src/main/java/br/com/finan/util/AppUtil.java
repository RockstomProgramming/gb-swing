package br.com.finan.util;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author Wesley Luiz
 */
public final class AppUtil {

	public static final String MSG_SALVAR_SUCESSO = "Os dados foram salvos com sucesso!";
	public static final String PROP_CONF_FILECHOOSER = "prop.conf.filechooser";
	public static final String MODAL_INFO = "Informação";
	
	public static void exibirMsgSalvarSucesso(JComponent comp) {
		JOptionPane.showMessageDialog(comp, AppUtil.MSG_SALVAR_SUCESSO, AppUtil.MODAL_INFO, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static boolean exibirMensagemConfirmacaoInativacao(Component comp) {
		int result = JOptionPane.showConfirmDialog(comp, "Confirma a exclusão do item selecionado?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
		return result == JOptionPane.YES_OPTION ? true : false;
	}
}
