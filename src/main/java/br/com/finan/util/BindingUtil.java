package br.com.finan.util;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingbinding.SwingBindings;

public class BindingUtil {

    private final BindingGroup bindingGroup;

    private BindingUtil(BindingGroup bindingGroup) {
        this.bindingGroup = bindingGroup;
    }

    public static BindingUtil create(BindingGroup bindingGroup) {
        return new BindingUtil(bindingGroup);
    }

    public BindingUtil add(Object source, String el, JTextField field) {
        return add(source, el, field, "text", null);
    }
    public BindingUtil add(Object source, String el, JTextField field, Converter converter) {
        return add(source, el, field, "text", converter);
    }
    
    public BindingUtil addJComboBoxBinding(List list, JComboBox combobox) {
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(AutoBinding.UpdateStrategy.READ, list, combobox));
        return this;
    }

    public BindingUtil add(Object source, String el, JTextField field, String bean, Converter converter) {
        final Binding b = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, source, ELProperty.create(el), field, BeanProperty.create(bean));
        if (ObjetoUtil.isReferencia(converter)) {
            b.setConverter(converter);
        }
        bindingGroup.addBinding(b);
        return this;
    }
}
