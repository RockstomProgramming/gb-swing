package br.com.finan.service;

import java.util.List;

import br.com.finan.entidade.ContaBancaria;
import br.com.finan.util.HibernateUtil;

public class ContaBancariaService {

	@SuppressWarnings("unchecked")
	public List<ContaBancaria> obterContasBancarias() {
		return HibernateUtil.getCriteriaBuilder(ContaBancaria.class).eqStatusAtivo().list();
	}
}
