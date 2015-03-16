package br.com.finan.dto;

import br.com.finan.annotation.ColunaTabela;

/**
 *
 * @author Wesley Luiz
 */
public class DTO {

	private Long id;
	
	@ColunaTabela(index = 0, titulo = "")
	private boolean selecionado;
	
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}
}
