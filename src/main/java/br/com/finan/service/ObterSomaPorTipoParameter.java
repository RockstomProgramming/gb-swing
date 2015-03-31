package br.com.finan.service;

import br.com.finan.enumerator.TipoConta;

public class ObterSomaPorTipoParameter {
	public String dataInicio;
	public String dataFim;
	public TipoConta tipo;

	public ObterSomaPorTipoParameter(final String dataInicio, final String dataFim, final TipoConta tipo) {
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.tipo = tipo;
	}
}