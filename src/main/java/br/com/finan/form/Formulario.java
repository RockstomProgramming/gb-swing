package br.com.finan.form;

import java.awt.Component;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.JTextComponent;

import br.com.finan.annotation.OnGanharFoco;
import br.com.finan.listener.FocoListener;
import br.com.finan.service.CategoriaService;
import br.com.finan.service.ContaBancariaService;
import br.com.finan.service.ContaService;
import br.com.finan.util.FieldUtil;

public abstract class Formulario extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private final ContaService contaService;
	private final CategoriaService categoriaService;
	private final ContaBancariaService contaBancariaService;

	public Formulario() {
		contaService = new ContaService();
		categoriaService = new CategoriaService();
		contaBancariaService = new ContaBancariaService();

		addInternalFrameListener(new FocoListener() {
			@Override
			public void internalFrameActivated(final InternalFrameEvent e) {
				executarMetodos(OnGanharFoco.class);
			}
		});
	}

	@SuppressWarnings("rawtypes")
	protected void limparCampos(final JPanel container) {
		for (final Component comp : container.getComponents()) {
			if (comp instanceof JPanel) {
				limparCampos((JPanel) comp);
			}

			if (comp instanceof JTextComponent) {
				((JTextComponent) comp).setText(null);
			} else if (comp instanceof JComboBox) {
				((JComboBox) comp).setSelectedItem(null);
			}
		}
	}

	protected void executarMetodos(final Class<? extends Annotation> anno) {
		final List<Class<?>> superclasses = FieldUtil.getAllSuperclasses(this.getClass());
		superclasses.add(this.getClass());
		for (final Class<?> c : superclasses) {
			for (final Method method : c.getDeclaredMethods()) {
				if (method.isAnnotationPresent(anno)) {
					try {
						method.invoke(this);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public ContaService getContaService() {
		return contaService;
	}

	public CategoriaService getCategoriaService() {
		return categoriaService;
	}

	public ContaBancariaService getContaBancariaService() {
		return contaBancariaService;
	}
}
