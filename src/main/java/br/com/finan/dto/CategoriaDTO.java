package br.com.finan.dto;

import br.com.finan.annotation.ColunaTabela;

public class CategoriaDTO extends DTO {
	
	@ColunaTabela(index = 1, titulo = "Nome")
	private String nome;

	@ColunaTabela(index = 2, titulo = "Categoria")
	private String supCategoria;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSupCategoria() {
		return supCategoria;
	}

	public void setSupCategoria(String supCategoria) {
		this.supCategoria = supCategoria;
	}
}
