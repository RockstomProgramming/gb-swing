package br.com.finan.dto;

import java.util.Date;

import br.com.finan.entidade.Categoria;
import br.com.finan.enumerator.TipoConta;

public class TransacaoDTO {

	private String descricao;
	private Double valor;
	private Date data;
	private Categoria categoria;
	private TipoConta tipo;

	@Override
	public String toString() {
		return getDescricao().concat(" - ").concat(valor.toString());
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(final Double valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(final Date data) {
		this.data = data;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(final Categoria categoria) {
		this.categoria = categoria;
	}

	public TipoConta getTipo() {
		return tipo;
	}

	public void setTipo(final TipoConta tipo) {
		this.tipo = tipo;
	}
}
