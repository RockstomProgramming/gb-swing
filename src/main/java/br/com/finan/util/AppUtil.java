package br.com.finan.util;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import br.com.finan.form.principal.PrincipalForm;
import br.com.finan.service.ContaBancariaService;

/**
 *
 * @author Wesley Luiz
 */
public final class AppUtil {

	public static final String MSG_ALTERAR_SUCESSO = "Os dados foram alterados com sucesso!";
	public static final String MSG_SALVAR_SUCESSO = "Os dados foram salvos com sucesso!";
	public static final String PROP_CONF_FILECHOOSER = "prop.conf.filechooser";
	public static final String MODAL_INFO = "Informação";

	public static void exibirMsgSalvarSucesso(JComponent comp) {
		JOptionPane.showMessageDialog(comp, MSG_SALVAR_SUCESSO, MODAL_INFO, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void exibirMsgAlterarSucesso(JComponent comp) {
		JOptionPane.showMessageDialog(comp, MSG_ALTERAR_SUCESSO, MODAL_INFO, JOptionPane.INFORMATION_MESSAGE);
	}

	public static boolean exibirMensagemConfirmacaoInativacao(Component comp) {
		int result = JOptionPane.showConfirmDialog(comp, "Confirma a exclusão do item selecionado?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
		return result == JOptionPane.YES_OPTION ? true : false;
	}
	
	public static void atualizarContas() {
		PrincipalForm.contasBancarias.clear();
		PrincipalForm.contasBancarias.addAll(new ContaBancariaService().obterContasBancarias());
	}
}
