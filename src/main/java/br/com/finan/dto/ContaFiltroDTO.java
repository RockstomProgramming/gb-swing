package br.com.finan.dto;

import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.ContaBancaria;

public class ContaFiltroDTO extends FiltroDTO {

	private static final long serialVersionUID = 1L;

	private String descricao;
	private ContaBancaria conta;
	private Categoria categoria;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ContaBancaria getConta() {
		return conta;
	}

	public void setConta(ContaBancaria conta) {
		this.conta = conta;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}
