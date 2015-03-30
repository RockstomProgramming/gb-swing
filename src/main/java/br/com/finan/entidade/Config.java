package br.com.finan.entidade;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_config")
public class Config extends Entidade {

	private static final long serialVersionUID = 1L;

	private String path;
	private boolean bloquear;
	private String senha;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isBloquear() {
		return bloquear;
	}

	public void setBloquear(boolean bloquear) {
		this.bloquear = bloquear;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
