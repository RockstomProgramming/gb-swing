package br.com.finan.enumerator;

import br.com.finan.entidade.Entidade;

/**
 * Este <code>Enumerator</code> é responsável por fornecer um status à uma
 * {@link Entidade}, <i>Ativo</i> e <i>Inativo</i>
 * que é referente a um atributo booleano na tabela referente a entidade em
 * questão.
 *
 * @author Wesley Luiz
 * @version 1.0.0
 */
public enum EnumStatus {

	INATIVO(false), ATIVO(true);

	private boolean status;

	/**
	 * Responsável pela criação de novas instâncias desta classe.
	 *
	 * @param status
	 */
	private EnumStatus(final boolean status) {
		this.status = status;
	}

	/**
	 * Retorna o valor do atributo <code>status</code>
	 *
	 * @return <code>boolean</code>
	 */
	public boolean getStatus() {
		return status;
	}
}
