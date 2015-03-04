package br.com.finan.entidade;

import br.com.finan.entidade.enumerator.FormaPagamento;
import br.com.finan.entidade.enumerator.TipoConta;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_conta")
@Inheritance(strategy = InheritanceType.JOINED)
public class Conta extends Entidade {

    private static final long serialVersionUID = 1L;

    @Column
    private String descricao;

    @Column(precision = 8, scale = 2)
    private BigDecimal valor;

    @Column
    private String observacoes;

    @Column
    private Integer parcela;

    @Column
    private Integer totalParcelas;

    @Column
    private boolean isPago;

    @Temporal(TemporalType.DATE)
    @Column
    private Date dataVencimento;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEmisao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPagamento;

    @ManyToOne
    @JoinColumn(name = "idCategoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "idContaBancaria")
    private ContaBancaria contaBancaria;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private FormaPagamento formaPagamento;
    
    @Column
    @Enumerated(EnumType.ORDINAL)
    private TipoConta tipo;

    @Override
    public void preSalvar() {
        setDataEmisao(new Date());
        super.preSalvar();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Integer getParcela() {
        return parcela;
    }

    public void setParcela(Integer parcela) {
        this.parcela = parcela;
    }

    public Integer getTotalParcelas() {
        return totalParcelas;
    }

    public void setTotalParcelas(Integer totalParcelas) {
        this.totalParcelas = totalParcelas;
    }

    public boolean isIsPago() {
        return isPago;
    }

    public void setIsPago(boolean isPago) {
        this.isPago = isPago;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Date getDataEmisao() {
        return dataEmisao;
    }

    public void setDataEmisao(Date dataEmisao) {
        this.dataEmisao = dataEmisao;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }

}
