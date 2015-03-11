package br.com.finan.validator;

/**
 *
 * @author Wesley Luiz
 */
public class IntegerValidator extends MaxLengthValidator {

	public IntegerValidator(final int max) {
		super(max);
	}

	@Override
	public Result validate(final Object t) {
		final String s = t.toString();
		try {
			Integer.valueOf(s);
			return super.validate(t);
		} catch (final NumberFormatException ex) {
			getComp().setText(s.substring(0, s.length() - 1));
			return null;
		}
	}
}
