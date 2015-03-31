package br.com.finan.dto;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public class FiltroDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private final PropertyChangeSupport property = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		property.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		property.removePropertyChangeListener(listener);
	}
}
