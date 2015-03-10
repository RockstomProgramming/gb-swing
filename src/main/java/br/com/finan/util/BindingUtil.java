package br.com.finan.util;

import br.com.finan.entidade.Categoria;
import br.com.finan.form.receita.CadastroReceitaForm;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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

    public BindingUtil addJComboBoxBinding(List list, JComboBox combobox) {
        bindingGroup.addBinding(SwingBindings.createJComboBoxBinding(AutoBinding.UpdateStrategy.READ, list, combobox));
        return this;
    }

    public BindingUtil add(Object source, String el, JComponent component) {
        return add(source, el, component, "text", null);
    }

    public BindingUtil add(Object source, String el, JComponent component, String bean) {
        return add(source, el, component, bean, null);
    }

    public BindingUtil add(Object source, String el, JComponent component, Converter converter) {
        return add(source, el, component, "text", converter);
    }

    public BindingUtil add(Object source, String el, JComponent component, String bean, Converter converter) {
        final Binding b = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, source, ELProperty.create(el), component, BeanProperty.create(bean));
        if (ObjetoUtil.isReferencia(converter)) {
            b.setConverter(converter);
        }
        bindingGroup.addBinding(b);
        return this;
    }
}
