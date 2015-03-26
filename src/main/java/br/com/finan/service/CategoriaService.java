package br.com.finan.service;

import java.util.List;

import br.com.finan.entidade.Categoria;
import br.com.finan.util.HibernateUtil;

public class CategoriaService {
	
	@SuppressWarnings("unchecked")
	public List<Categoria> obterCategorias() {
		return HibernateUtil.getCriteriaBuilder(Categoria.class).eqStatusAtivo().list();
	}
}
