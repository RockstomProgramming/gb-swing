package br.com.finan.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.sf.ofx4j.domain.data.MessageSetType;
import net.sf.ofx4j.domain.data.ResponseEnvelope;
import net.sf.ofx4j.domain.data.ResponseMessageSet;
import net.sf.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import net.sf.ofx4j.domain.data.banking.BankingResponseMessageSet;
import net.sf.ofx4j.domain.data.common.Transaction;
import net.sf.ofx4j.io.AggregateUnmarshaller;

/**
 * Arquivo: BankingUtil.java <br/>
 *
 * @since 03/11/2014
 * @author Wesley Luiz
 * @version 1.0.0
 */
public final class BankingUtil {

	private static final String MSG_SEM_TRANSACOES = "msg.sem.transacoes";
	private static final String MSG_ARQUIVO_OFX_INVALIDO = "msg.arquivo.ofx.invalido";
	private static final String REGEX_EXTENSAO_OFX = ".*ofx$";

	/**
	 * Método responsável por obter a lista de transações de um arquivo com a
	 * extensão <i>ofx</i> informado.
	 *
	 * @author Wesley Luiz
	 * @since 03/11/2014
	 * @param arquivo
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Transaction> obterTransacoesArquivoOfx(final InputStream arquivo) {
		// validarArquivoOfx(arquivo);

		final List<Transaction> transactions = new ArrayList<Transaction>();
		try {
			final AggregateUnmarshaller a = new AggregateUnmarshaller(ResponseEnvelope.class);
			final ResponseEnvelope re = (ResponseEnvelope) a.unmarshal(arquivo);
			final MessageSetType type = MessageSetType.banking;
			final ResponseMessageSet message = re.getMessageSet(type);
			List<BankStatementResponseTransaction> bank;

			if (ObjetoUtil.isReferencia(message)) {
				bank = ((BankingResponseMessageSet) message).getStatementResponses();

				for (final BankStatementResponseTransaction b : bank) {
					if (!ObjetoUtil.isReferencia(b.getMessage().getTransactionList(), b.getMessage().getTransactionList().getTransactions())) {
						// throw new ArquivoUploadException(MSG_SEM_TRANSACOES);
					}

					for (final Transaction transaction : b.getMessage().getTransactionList().getTransactions()) {
						transactions.add(transaction);
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return transactions;
	}

	/**
	 * Método responsável por validar o tipo de arquivo contendo as transações
	 * bancárias.
	 *
	 * @author Wesley Luiz
	 * @since 29/09/2014
	 * @param arquivo
	 * @throws ArquivoUploadException
	 */
	// public static void validarArquivoOfx(final Part arquivo) throws
	// ArquivoUploadException {
	// if (!AnexoUtil.getNomeArquivo(arquivo).matches(REGEX_EXTENSAO_OFX)) {
	// throw new ArquivoUploadException(MSG_ARQUIVO_OFX_INVALIDO);
	// }
	// }
}
