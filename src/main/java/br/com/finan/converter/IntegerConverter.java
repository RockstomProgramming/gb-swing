package br.com.finan.converter;

import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author Wesley Luiz
 */
public class IntegerConverter extends Converter<Integer, String> {

	@Override
	public String convertForward(final Integer s) {
		throw new UnsupportedOperationException("Not supported yet."); // To
		// change
		// body
		// of
		// generated
		// methods,
		// choose
		// Tools
		// |
		// Templates.
	}

	@Override
	public Integer convertReverse(final String t) {
		try {
			return new Integer(t);
		} catch (final NumberFormatException e) {
			return null;
		}
	}

}
