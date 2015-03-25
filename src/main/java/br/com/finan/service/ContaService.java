package br.com.finan.service;

import java.math.BigDecimal;
import java.math.MathContext;

import org.hibernate.criterion.Projections;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.entidade.Conta;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.form.principal.PrincipalForm;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.NumberUtil;
import br.com.finan.util.ObjetoUtil;

public class ContaService {

	public void atualizarSaldoFramePrincipal() {
		String saldo = getSaldoAtual();
		PrincipalForm.lbSaldo.setText("Saldo: ".concat(saldo));
	}

	public String getSaldoAtual() {
		BigDecimal despesa = getSomaDespesa();
		BigDecimal receita = getSomaReceita();
		String saldo = NumberUtil.obterNumeroFormatado(receita.subtract(despesa, MathContext.UNLIMITED));
		return saldo;
	}

	public String getSaldoAtual(BigDecimal despesa, BigDecimal receita) {
		String saldo = NumberUtil.obterNumeroFormatado(receita.subtract(despesa, MathContext.UNLIMITED));
		return saldo;
	}

	public BigDecimal getSomaReceita() {
		return getSomaValores((BigDecimal) getBuilderSaldo().eq("tipo", TipoConta.RECEITA).uniqueResult());
	}

	public BigDecimal getSomaDespesa() {
		return getSomaValores((BigDecimal) getBuilderSaldo().eq("tipo", TipoConta.DESPESA).uniqueResult());
	}

	private BigDecimal getSomaValores(BigDecimal valor) {
		if (ObjetoUtil.isReferencia(valor)) {
			return valor;
		}

		return NumberUtil.obterNumeroFormatado("0,00");
	}

	private CriteriaBuilder getBuilderSaldo() {
		CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder(Conta.class).eqStatusAtivo().eq("isPago", true);
		builder.getCriteria().setProjection(Projections.sum("valor"));
		return builder;
	}
}
