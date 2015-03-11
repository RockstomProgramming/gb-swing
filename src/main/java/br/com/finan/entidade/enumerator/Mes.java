package br.com.finan.entidade.enumerator;

/**
 * Arquivo: Meses.java <br/>
 *
 * @since 30/10/2014
 * @author Wesley Luiz
 * @version 1.0.0
 */
public enum Mes {

	JANEIRO("Janeiro", 1), FEVEREIRO("Fevereiro", 2), MARCO("Março", 3), ABRIL("Abril", 4), MAIO("Maio", 5), JUNHO("Junho", 6), JULHO("Julho", 7), AGOSTO("Agosto", 8), SETEMBRO("Setembro", 9), OUTUBRO("Outubro", 10), NOVEMBRO("Novembro", 11), DEZEMBRO("Dezembro", 12);

	private final String descricao;
	private final Integer referencia;

	@Override
	public String toString() {
		return getDescricao();
	}

	private Mes(final String descricao, final Integer referencia) {
		this.descricao = descricao;
		this.referencia = referencia;
	}

	public static Mes getMesPorDescricao(final String descricao) {
		for (final Mes mes : Mes.values()) {
			if (mes.getDescricao().equals(descricao)) {
				return mes;
			}
		}
		return null;
	}

	public static Mes getMesPorReferencia(final Integer referencia) {
		for (final Mes mes : Mes.values()) {
			if (mes.getReferencia().equals(referencia)) {
				return mes;
			}
		}
		return null;
	}

	public Integer getReferencia() {
		return referencia;
	}

	public String getDescricao() {
		return descricao;
	}
}
