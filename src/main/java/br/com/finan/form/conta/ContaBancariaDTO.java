package br.com.finan.form.conta;

import br.com.finan.dto.DTO;
import br.com.finan.entidade.annotation.ColunaTabela;

public class ContaBancariaDTO extends DTO {
	
	@ColunaTabela(index = 0, titulo = "Descrição")
	private String descricao;

	@ColunaTabela(index = 1, titulo = "Conta")
	private String numero;
	
	@ColunaTabela(index = 2, titulo = "Agência")
	private String agencia;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
}
