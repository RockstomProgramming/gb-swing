package br.com.finan.entidade;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_config")
public class Config extends Entidade {

	private static final long serialVersionUID = 1L;

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
