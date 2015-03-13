package br.com.finan.entidade;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import br.com.finan.enumerator.EnumStatus;

/**
 *
 * @author Wesley Luiz
 */
@MappedSuperclass
public class Entidade implements Serializable {

	/** Atributo serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private EnumStatus status;

	private transient final PropertyChangeSupport property = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		property.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		property.removePropertyChangeListener(listener);
	}

	@PrePersist
	public void preSalvar() {
		status = EnumStatus.ATIVO;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public EnumStatus getStatus() {
		return status;
	}

	public void setStatus(final EnumStatus status) {
		this.status = status;
	}

	public PropertyChangeSupport getProperty() {
		return property;
	}

}
