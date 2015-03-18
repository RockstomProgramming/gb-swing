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
import javax.persistence.Transient;

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
	
	@Transient
	private transient final PropertyChangeSupport property = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		property.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		property.removePropertyChangeListener(listener);
	}
	
	public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		property.addPropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		property.removePropertyChangeListener(propertyName, listener);
	}
	
	protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
		property.firePropertyChange(propertyName, oldValue, newValue);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entidade other = (Entidade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
