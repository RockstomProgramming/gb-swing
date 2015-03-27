package br.com.finan.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.sql.JoinType;

import br.com.finan.annotation.Aliases;
import br.com.finan.annotation.ColunaTabela;
import br.com.finan.annotation.CreateAlias;
import br.com.finan.annotation.ProjectionEntityProperty;
import br.com.finan.enumerator.Conversor;
import br.com.finan.enumerator.TipoConta;

/**
 *
 * @author Wesley Luiz
 */
@Aliases({ 
	@CreateAlias(alias = "categoria", associationPath = "categoria", joinType = JoinType.LEFT_OUTER_JOIN),
	@CreateAlias(alias = "contaBancaria", associationPath = "contaBancaria", joinType = JoinType.LEFT_OUTER_JOIN) 
})
public class ContaDTO extends DTO {
	
	@ProjectionEntityProperty("descricao")
	@ColunaTabela(index = 0, titulo = "Descrição")
	private String descricao;

	@ProjectionEntityProperty("categoria.nome")
	@ColunaTabela(index = 1, titulo = "Categoria")
	private String categoria;
	
	@ProjectionEntityProperty("contaBancaria.descricao")
	@ColunaTabela(index = 2, titulo = "Conta Bancária")
	private String contaBancaria;

	@ProjectionEntityProperty("dataVencimento")
	@ColunaTabela(index = 3, titulo = "Vencimento", tipo = Date.class)
	private Date vencimento;

	@ProjectionEntityProperty("valor")
	@ColunaTabela(index = 4, titulo = "Valor", tipo = String.class, conversor = Conversor.BIG_DECIMAL)
	private BigDecimal valor;
	
	@ProjectionEntityProperty("dataPagamento")
	private Date pagamento;

	@ProjectionEntityProperty("parcela")
	private Integer parcela;

	@ProjectionEntityProperty("totalParcelas")
	private Integer totalParcela;
	
	@ProjectionEntityProperty("tipo")
	private TipoConta tipo;

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

	public String getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(String contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

	public Date getPagamento() {
		return pagamento;
	}

	public void setPagamento(Date pagamento) {
		this.pagamento = pagamento;
	}

	public TipoConta getTipo() {
		return tipo;
	}

	public void setTipo(TipoConta tipo) {
		this.tipo = tipo;
	}
}
