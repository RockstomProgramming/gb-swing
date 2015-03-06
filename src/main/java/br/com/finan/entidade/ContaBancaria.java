package br.com.finan.entidade;

import br.com.finan.entidade.annotation.Unique;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_conta_bancaria")
public class ContaBancaria extends Entidade {

    private static final long serialVersionUID = 1L;

    @Unique
    @Column(nullable = false)
    private String descricao;

    @Unique
    @Column
    private String numero;

    @Column
    private String agencia;

    @Override
    public String toString() {
        return getDescricao();
    }

    public ContaBancaria() {
        super();
    }

    public ContaBancaria(final String descricao) {
        this.descricao = descricao;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(final String numero) {
        this.numero = numero;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(final String agencia) {
        this.agencia = agencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(final String descricao) {
        this.descricao = descricao;
    }
}
