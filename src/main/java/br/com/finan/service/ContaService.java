package br.com.finan.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.ContaDTO;
import br.com.finan.entidade.Categoria;
import br.com.finan.entidade.Conta;
import br.com.finan.entidade.ContaBancaria;
import br.com.finan.enumerator.TipoConta;
import br.com.finan.form.principal.PrincipalForm;
import br.com.finan.util.CriterionInfo;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.NumberUtil;
import br.com.finan.util.ObjetoUtil;

public class ContaService {

	private static final String REGEX_DATE = "dd/MM/yyyy";

	@SuppressWarnings("unchecked")
	public List<String> obterContaPorDescricaoAgrupado(final String dataInicio, final String dataFim, final TipoConta tipo) {
		final CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder(Conta.class);
		addRestricoesBasicas(dataInicio, dataFim, tipo, builder);
		builder.getCriteria().setProjection(Projections.groupProperty("descricao").as("descricao"));
		return builder.list();
	}

	public BigDecimal obterSomaPorDescricao(final String descricao, final String dataInicio, final String dataFim, final TipoConta tipo) {
		final CriteriaBuilder builder = obterBuilderSomaPorTipo(dataInicio, dataFim, tipo);
		builder.eq("descricao", descricao);
		return getResultadoValido(builder);
	}

	public BigDecimal obterSomaPorContaBancaria(final Long idConta, final TipoConta tipo, final String dataInicio, final String dataFim) {
		final CriteriaBuilder builder = obterBuilderSomaPorTipo(dataInicio, dataFim, tipo);
		builder.eq("contaBancaria.id", idConta);
		return getResultadoValido(builder);
	}

	public BigDecimal obterSomaPorCategoria(final Long idCategoria, final TipoConta tipo, final String dataInicio, final String dataFim) {
		final CriteriaBuilder builder = obterBuilderSomaPorTipo(dataInicio, dataFim, tipo);
		builder.eq("categoria.id", idCategoria);
		return getResultadoValido(builder);
	}

	public CriteriaBuilder obterBuilderSomaPorTipo(final String dataInicio, final String dataFim, final TipoConta tipo) {
		final CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder(Conta.class);
		addRestricoesBasicas(dataInicio, dataFim, tipo, builder);
		builder.getCriteria().setProjection(Projections.sum("valor"));
		return builder;
	}

	@SuppressWarnings("unchecked")
	public List<ContaDTO> obterExtratoPorPeriodo(final String dataInicio, final String dataFim, final Categoria categoria, final ContaBancaria contaBancaria) {
		final CriteriaBuilder builder = CriterionInfo.getInstance(Conta.class, ContaDTO.class).eqStatusAtivo().eq("isPago", true);

		if (!isDataVazia(dataInicio, dataFim)) {
			try {
				builder.between("dataVencimento", new SimpleDateFormat(REGEX_DATE).parse(dataInicio), new SimpleDateFormat(REGEX_DATE).parse(dataFim));
			} catch (final ParseException e) {
				e.printStackTrace();
			}
		}

		if (ObjetoUtil.isReferencia(categoria)) {
			builder.eq("categoria.id", categoria.getId());
		}

		if (ObjetoUtil.isReferencia(contaBancaria)) {
			builder.eq("contaBancaria.id", contaBancaria.getId());
		}

		return builder.addOrdenacao(Order.asc("dataVencimento")).list();
	}

	public boolean isDataVazia(final String... datas) {
		for (final String d : datas) {
			if (!ObjetoUtil.isReferencia(d) || d.trim().equals("/  /")) {
				return true;
			}
		}
		return false;
	}

	public void atualizarSaldoFramePrincipal() {
		final String saldo = getSaldoAtual();
		PrincipalForm.lbSaldo.setText("Saldo: ".concat(saldo));
	}
	
	public String getSaldoAtual(final Long idConta) {
		final BigDecimal despesa = getSomaValores((BigDecimal) getBuilderSaldoPorConta(idConta).eq("tipo", TipoConta.DESPESA).uniqueResult());
		final BigDecimal receita = getSomaValores((BigDecimal) getBuilderSaldoPorConta(idConta).eq("tipo", TipoConta.RECEITA).uniqueResult());
		final String saldo = NumberUtil.obterNumeroFormatado(receita.subtract(despesa, MathContext.UNLIMITED));
		return saldo;
	}
	
	public String getSaldoAtual() {
		return getSaldoAtual(null);
	}

	public String getSaldoAtual(final BigDecimal despesa, final BigDecimal receita) {
		final String saldo = NumberUtil.obterNumeroFormatado(receita.subtract(despesa, MathContext.UNLIMITED));
		return saldo;
	}

	public BigDecimal getSomaReceita() {
		return getSomaValores((BigDecimal) getBuilderSaldo().eq("tipo", TipoConta.RECEITA).uniqueResult());
	}

	public BigDecimal getSomaDespesa() {
		return getSomaValores((BigDecimal) getBuilderSaldo().eq("tipo", TipoConta.DESPESA).uniqueResult());
	}

	private BigDecimal getResultadoValido(final CriteriaBuilder builder) {
		final BigDecimal res = (BigDecimal) builder.uniqueResult();
		return ObjetoUtil.isReferencia(res) ? res : new BigDecimal(0);
	}

	private CriteriaBuilder getBuilderSaldoPorConta(final Long idConta) {
		CriteriaBuilder builderSaldo = getBuilderSaldo();
		if (ObjetoUtil.isReferencia(idConta)) {
			builderSaldo.eq("contaBancaria.id", idConta);
		}
		return builderSaldo;
	}

	private void addRestricoesBasicas(final String dataInicio, final String dataFim, final TipoConta tipo, final CriteriaBuilder builder) {
		builder.eqStatusAtivo().eq("isPago", true).eq("tipo", tipo);

		if (!isDataVazia(dataInicio, dataFim)) {
			builder.between("dataVencimento", getData(dataInicio), getData(dataFim));
		}
	}

	private Date getData(final String dataInicio) {
		Date dtInicio = null;
		try {
			dtInicio = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicio);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dtInicio;
	}

	private BigDecimal getSomaValores(final BigDecimal valor) {
		if (ObjetoUtil.isReferencia(valor)) {
			return valor;
		}

		return NumberUtil.obterNumeroFormatado("0,00");
	}

	private CriteriaBuilder getBuilderSaldo() {
		final CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder(Conta.class).eqStatusAtivo().eq("isPago", true);
		builder.getCriteria().setProjection(Projections.sum("valor"));
		return builder;
	}
}
