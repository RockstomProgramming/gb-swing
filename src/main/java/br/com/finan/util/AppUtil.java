package br.com.finan.util;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import br.com.finan.entidade.ContaBancaria;
import br.com.finan.form.principal.PrincipalForm;
import br.com.finan.service.ContaBancariaService;
import br.com.finan.service.ContaService;

/**
 *
 * @author Wesley Luiz
 */
public final class AppUtil {

	public static final String MSG_ALTERAR_SUCESSO = "Os dados foram alterados com sucesso!";
	public static final String MSG_SALVAR_SUCESSO = "Os dados foram salvos com sucesso!";
	public static final String MODAL_INFO = "Informação";

	public static void exibirMsgSalvarSucesso(final JComponent comp) {
		JOptionPane.showMessageDialog(comp, MSG_SALVAR_SUCESSO, MODAL_INFO, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void exibirMsgAlterarSucesso(final JComponent comp) {
		JOptionPane.showMessageDialog(comp, MSG_ALTERAR_SUCESSO, MODAL_INFO, JOptionPane.INFORMATION_MESSAGE);
	}

	public static boolean exibirMensagemConfirmacaoInativacao(final Component comp) {
		final int result = JOptionPane.showConfirmDialog(comp, "Confirma a exclusão do item selecionado?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
		return result == JOptionPane.YES_OPTION ? true : false;
	}

	public static void atualizarContas() {
		PrincipalForm.contasBancarias.clear();
		PrincipalForm.contasBancarias.addAll(new ContaBancariaService().obterContasBancarias());
	}

	public static ContaBancaria getContaSelecionada() {
		return (ContaBancaria) PrincipalForm.cmbConta.getSelectedItem();
	}

	public static void atualizarSaldoFramePrincipal() {
		String saldo = "0,00";
		final ContaBancaria conta = getContaSelecionada();
		if (ObjetoUtil.isReferencia(conta)) {
			saldo = new ContaService().getSaldoAtual(conta.getId());
		}
		PrincipalForm.lbSaldo.setText("Saldo: ".concat(saldo));
	}
}
