package br.com.finan.dto;

import org.hibernate.sql.JoinType;

import br.com.finan.annotation.Aliases;
import br.com.finan.annotation.ColunaTabela;
import br.com.finan.annotation.CreateAlias;
import br.com.finan.annotation.ProjectionEntityProperty;

@Aliases({ @CreateAlias(alias = "superCategoria", associationPath = "superCategoria", joinType = JoinType.LEFT_OUTER_JOIN) })
public class CategoriaDTO extends DTO {
	
	@ProjectionEntityProperty("nome")
	@ColunaTabela(index = 0, titulo = "Nome")
	private String nome;

	@ProjectionEntityProperty("superCategoria.nome")
	@ColunaTabela(index = 1, titulo = "Categoria")
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
