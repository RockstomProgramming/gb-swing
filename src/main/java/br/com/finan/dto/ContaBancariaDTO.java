package br.com.finan.dto;

import br.com.finan.annotation.ColunaTabela;
import br.com.finan.annotation.ProjectionEntityProperty;

public class ContaBancariaDTO extends DTO {

	@ProjectionEntityProperty("descricao")
	@ColunaTabela(index = 0, titulo = "Descrição")
	private String descricao;

	@ProjectionEntityProperty("numero")
	@ColunaTabela(index = 1, titulo = "Conta")
	private String numero;

	@ProjectionEntityProperty("agencia")
	@ColunaTabela(index = 2, titulo = "Agência")
	private String agencia;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(final String numero) {
		this.numero = numero;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(final String agencia) {
		this.agencia = agencia;
	}
}
