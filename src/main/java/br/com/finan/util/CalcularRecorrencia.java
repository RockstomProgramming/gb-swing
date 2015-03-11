package br.com.finan.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import br.com.finan.entidade.enumerator.Frequencia;

/**
 * Arquivo: CalcularRecorrencia.java Criado em: 21/08/2014
 *
 * @author Wesley Luiz
 * @version 1.0.0
 */
public final class CalcularRecorrencia {

	private final Frequencia frequencia;
	private final Date database;
	private final Integer limite;
	private List<Date> datas;

	/**
	 * Responsável pela criação de novas instâncias desta classe.
	 *
	 * @param frequencia - Frequência da recorrência.
	 * @param database - Data de início.
	 * @param limite - Delimitador para que não gere mais datas.
	 */
	public CalcularRecorrencia(final Frequencia frequencia, final Date database, final Integer limite) {
		this.frequencia = frequencia;
		this.database = database;
		this.limite = limite;
	}

	public CalcularRecorrencia() {
		throw new UnsupportedOperationException("Not supported yet."); // To
		// change
		// body
		// of
		// generated
		// methods,
		// choose
		// Tools
		// |
		// Templates.
	}

	/**
	 * Método responsável por obter as datas de vencimento a partir dos dados
	 * informados na contrução dessa classe.
	 *
	 * @author Wesley Luiz
	 * @return - Lista de datas contendo os vencimentos.
	 */
	public List<Date> obterVencimentos() {
		datas = new ArrayList<Date>();

		if (ObjetoUtil.isReferencia(database, limite, frequencia)) {
			final DateTime dateTime = new DateTime(database);
			int diaTemp = 0;

			for (int i = 0; i < limite; i++) {
				switch (frequencia) {

				case DIARIO:
					adicionarData(new DateTime(dateTime.plusDays(i)), i);
					break;

				case SEMANAL:
					adicionarData(new DateTime(dateTime.plusWeeks(i)), i);
					break;

				case MENSAL:
					adicionarData(new DateTime(dateTime.plusMonths(i)), i);
					break;

				case BIMESTRAL:
					diaTemp = i * 2;
					adicionarData(new DateTime(dateTime.plusMonths(diaTemp)), i);
					break;

				case TRIMESTRAL:
					diaTemp = i * 3;
					adicionarData(new DateTime(dateTime.plusMonths(diaTemp)), i);
					break;

				case SEMESTRAL:
					diaTemp = i * 6;
					adicionarData(new DateTime(dateTime.plusMonths(diaTemp)), i);
					break;

				case ANUAL:
					adicionarData(new DateTime(dateTime.plusYears(i)), i);
					break;
				}
			}

			return datas;
		}

		return null;
	}

	/**
	 * Método responsável por montar a lista de datas, com a nova data
	 * calculada.
	 *
	 * @author Wesley Luiz
	 * @param data - Data calculada.
	 * @param i - Valor da iteração atual.
	 */
	private void adicionarData(final DateTime data, final int i) {
		if (i > 0) {
			datas.add(data.toDate());
		} else {
			datas.add(database);
		}
	}
}
