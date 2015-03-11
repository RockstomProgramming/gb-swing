package br.com.finan.entidade;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_categoria")
public class Categoria extends Entidade {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String nome;

	@ManyToOne
	@JoinColumn(name = "idSuperCategoria")
	private Categoria superCategoria;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Categoria.class, mappedBy = "superCategoria")
	private Collection<Categoria> subCategorias;

	public Categoria() {
		super();
	}

	public Categoria(final String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return this.nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public Categoria getSuperCategoria() {
		return superCategoria;
	}

	public void setSuperCategoria(final Categoria superCategoria) {
		this.superCategoria = superCategoria;
	}

	public Collection<Categoria> getSubCategorias() {
		return subCategorias;
	}

	public void setSubCategorias(final Collection<Categoria> subCategorias) {
		this.subCategorias = subCategorias;
	}
}
