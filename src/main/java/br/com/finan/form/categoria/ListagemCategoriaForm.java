package br.com.finan.form.categoria;

import javax.swing.JPanel;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dao.Criterion;
import br.com.finan.dto.CategoriaDTO;
import br.com.finan.entidade.Categoria;
import br.com.finan.form.principal.ListagemForm;
import br.com.finan.util.HibernateUtil;

public class ListagemCategoriaForm extends ListagemForm<CategoriaDTO> {
	
	private static final long serialVersionUID = 1L;
	
	public ListagemCategoriaForm() {
		iniciarDados();
	}
	
	@Override
	public JPanel getPanelPaginacao() {
		return super.getPanelPaginacao();
	}
	
	@Override
	protected CriteriaBuilder getBuilderListagem() {
		return getBuilder()
				.addAliases("superCategoria", "superCategoria", Criterion.LEFT_JOIN)
				.addProjection("id").addProjection("nome").addProjection("superCategoria.nome", "supCategoria")
				.addAliasToBean(CategoriaDTO.class).close();
	}

	@Override
	protected CriteriaBuilder getBuilderQntRegistros() {
		return getBuilder();
	}
	
	private CriteriaBuilder getBuilder() {
		return HibernateUtil.getCriteriaBuilder(Categoria.class).eqStatusAtivo();
	}

	@Override
	protected String getNomeEntidade() {
		return Categoria.class.getSimpleName();
	}

	@Override
	protected String getTituloFrame() {
		return "";
	}
}
