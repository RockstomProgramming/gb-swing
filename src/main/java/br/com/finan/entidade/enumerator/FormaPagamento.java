package br.com.finan.entidade.enumerator;

public enum FormaPagamento {

	AVISTA("A vista", 1), APRAZO("A prazo", -1), CARTAO_DEBITO("Cartão de Débito", 1), CARTAO_CREDITO("Cartão de Crédito", -1), DINHEIRO("Dinheiro", 1), BOLETO_BANCARIO("Boleto Bancário", 1), TRANSFERENCIA_BANCARIA("Transferência Bancária", 1), DEBITO_AUTOMATICO("Débito Automático", 1), CHEQUE(
			"Cheque", -1);

	private final String descricao;
	private final Integer status;

	@Override
	public String toString() {
		return getDescricao();
	}

	private FormaPagamento(final String descricao, final Integer status) {
		this.descricao = descricao;
		this.status = status;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getStatus() {
		return status;
	}
}
