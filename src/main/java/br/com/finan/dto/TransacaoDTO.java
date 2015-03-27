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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public TipoConta getTipo() {
		return tipo;
	}

	public void setTipo(TipoConta tipo) {
		this.tipo = tipo;
	}
}
