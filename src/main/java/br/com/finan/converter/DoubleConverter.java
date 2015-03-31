package br.com.finan.converter;

import org.jdesktop.beansbinding.Converter;

import br.com.finan.util.NumberUtil;

public class DoubleConverter extends Converter<Double, String> {

	@Override
	public String convertForward(final Double s) {
		return NumberUtil.obterNumeroFormatado(s);
	}

	@Override
	public Double convertReverse(final String t) {
		if (t != null && !t.isEmpty()) {
			return new Double(t);
		}
		return null;
	}

}
