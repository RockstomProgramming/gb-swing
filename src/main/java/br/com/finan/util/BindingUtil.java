package br.com.finan.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.Validator;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import br.com.finan.converter.BigDecimalConverter;
import br.com.finan.converter.DateConverter;
import br.com.finan.validator.MaxLengthValidator;

@SuppressWarnings({ "unchecked", "rawtypes"})
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

	public ColumnBinding addJTableBinding(List<?> list, JTable jtable) {
		JTableBinding tableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE, list, jtable);
		ColumnBinding columnBinding = new ColumnBinding(this, tableBinding);
		
		bindingGroup.addBinding(tableBinding);
		
		return columnBinding;
	}
	
	public class ColumnBinding {
		
		private JTableBinding tableBinding;
		private BindingUtil bindingUtil;
		
		public ColumnBinding(BindingUtil bindingUtil, JTableBinding tableBinding) {
			this.tableBinding = tableBinding;
			this.bindingUtil = bindingUtil;
		}

		public ColumnBinding addColumnBinding(int index, String expression, String nameColumn) {
			tableBinding.addColumnBinding(index, ELProperty.create(expression)).setColumnName(nameColumn);
			return this;
		}

		public ColumnBinding addColumnBinding(int index, String expression, String nameColumn, Class<?> columnClass) {
			JTableBinding.ColumnBinding columnBinding = tableBinding.addColumnBinding(index, ELProperty.create(expression)).setColumnName(nameColumn).setColumnClass(Object.class);
			if (columnClass.equals(Date.class)) {
				columnBinding.setConverter(new DateConverter());
			} else if (columnClass.equals(BigDecimal.class)) {
				columnBinding.setConverter(new BigDecimalConverter());
			}
			return this;
		}
		
		public BindingUtil close() {
			return bindingUtil;
		}
	}
	
	public BindingUtil add(final Object source, final String sourceEl, final Object target) {
		return add(source, sourceEl, target, "text", null, null);
	}

	public BindingUtil add(final Object source, final String sourceEl, final Object target, final String targetEl) {
		return add(source, sourceEl, target, targetEl, null, null);
	}

	public BindingUtil add(final Object source, final String sourceEl, final Object target, final Converter converter) {
		return add(source, sourceEl, target, "text", converter, null);
	}

	public BindingUtil add(final Object source, final String sourceEl, final Object target, final Validator validator) {
		return add(source, sourceEl, target, "text", null, validator);
	}

	public BindingUtil add(final Object source, final String sourceEl, final Object target, String targetEl, final Converter converter, final Validator validator) {
		final Binding b = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, source, ELProperty.create(sourceEl), target, BeanProperty.create(targetEl));
		
		if (ObjetoUtil.isReferencia(converter)) {
			b.setConverter(converter);
		}

		if (ObjetoUtil.isReferencia(validator)) {
			if (validator instanceof MaxLengthValidator) {
				((MaxLengthValidator) validator).setComp((JTextComponent) target);
			}
			b.setValidator(validator);
		}
		
		bindingGroup.addBinding(b);
		return this;
	}
	
	public BindingGroup getBindingGroup() {
		return bindingGroup;
	}
}
