package br.com.finan.validator;

import javax.swing.text.JTextComponent;

import org.jdesktop.beansbinding.Validator;

/**
 *
 * @author Wesley Luiz
 */
public class MaxLengthValidator extends Validator<Object> {

	private final int max;
	private JTextComponent comp;

	public MaxLengthValidator(final int max) {
		super();
		this.max = max;
	}

	@Override
	public Result validate(final Object t) {
		if (t.toString().length() >= max) {
			comp.setText(comp.getText().substring(0, max));
			return new Result(null, "Quatidade de caracteres excedida");
		}
		return null;
	}

	public void setComp(final JTextComponent comp) {
		this.comp = comp;
	}

	public JTextComponent getComp() {
		return comp;
	}

}
