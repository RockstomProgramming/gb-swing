package br.com.finan.dto;

import java.math.BigDecimal;
import java.util.Date;
import br.com.finan.entidade.annotation.ColunaTabela;

/**
 *
 * @author Wesley Luiz
 */
public class ContaDTO extends DTO {

	@ColunaTabela(index = 2, titulo = "Descrição")
	private String descricao;

	@ColunaTabela(index = 3, titulo = "Categoria")
	private String categoria;

	@ColunaTabela(index = 5, titulo = "Valor")
	private BigDecimal valor;

	@ColunaTabela(index = 4, titulo = "Vencimento")
	private Date vencimento;

	@ColunaTabela(index = 1, titulo = "")
	private boolean selecionado;

	private Integer parcela;

	private Integer totalParcela;

	private boolean editado;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(final BigDecimal valor) {
		this.valor = valor;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(final Date vencimento) {
		this.vencimento = vencimento;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(final boolean selecionado) {
		this.selecionado = selecionado;
	}

	public boolean isEditado() {
		return editado;
	}

	public void setEditado(final boolean editado) {
		this.editado = editado;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(final String categoria) {
		this.categoria = categoria;
	}

	public Integer getParcela() {
		return parcela;
	}

	public void setParcela(final Integer parcela) {
		this.parcela = parcela;
	}

	public Integer getTotalParcela() {
		return totalParcela;
	}

	public void setTotalParcela(final Integer totalParcela) {
		this.totalParcela = totalParcela;
	}
}
