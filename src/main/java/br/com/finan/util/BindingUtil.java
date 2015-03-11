package br.com.finan.util;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.Validator;
import org.jdesktop.swingbinding.SwingBindings;
import br.com.finan.validator.MaxLengthValidator;

public class BindingUtil {

	private final BindingGroup bindingGroup;

	private BindingUtil(final BindingGroup bindingGroup) {
		this.bindingGroup = bindingGroup;
	}

	public static BindingUtil create(final BindingGroup bindingGroup) {
		return new BindingUtil(bindingGroup);
	}

	public BindingUtil addJComboBoxBinding(final List list, final JComboBox combobox) {
		bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(AutoBinding.UpdateStrategy.READ, list, combobox));
		return this;
	}

	public BindingUtil add(final Object source, final String el, final JComponent component) {
		return add(source, el, component, "text", null, null);
	}

	public BindingUtil add(final Object source, final String el, final JComponent component, final String bean) {
		return add(source, el, component, bean, null, null);
	}

	public BindingUtil add(final Object source, final String el, final JComponent component, final Converter converter) {
		return add(source, el, component, "text", converter, null);
	}

	public BindingUtil add(final Object source, final String el, final JComponent component, final Validator validator) {
		return add(source, el, component, "text", null, validator);
	}

	public BindingUtil add(final Object source, final String el, final JComponent component, final String bean, final Converter converter, final Validator validator) {
		final Binding b = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, source, ELProperty.create(el), component, BeanProperty.create(bean));
		if (ObjetoUtil.isReferencia(converter)) {
			b.setConverter(converter);
		}

		if (ObjetoUtil.isReferencia(validator)) {
			if (validator instanceof MaxLengthValidator) {
				((MaxLengthValidator) validator).setComp((JTextComponent) component);
			}
			b.setValidator(validator);
		}
		bindingGroup.addBinding(b);
		return this;
	}
}
