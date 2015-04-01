package br.com.finan.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.beansbinding.Converter;

import br.com.finan.util.NumberUtil;

/**
 * 
 * @author Wesley Luiz
 */
public class BigDecimalConverter extends Converter<BigDecimal, String> {

	private static final NumberFormat FORMATO = NumberFormat
			.getInstance(new Locale("pt", "BR"));

	public BigDecimalConverter() {
		super();
		FORMATO.setMaximumFractionDigits(2);
		FORMATO.setMinimumFractionDigits(2);
	}

	@Override
	public String convertForward(final BigDecimal numero) {
		return NumberUtil.obterNumeroFormatado(numero);
	}

	@Override
	public BigDecimal convertReverse(final String numero) {
		BigDecimal res = null;
		try {
			if (numero != null && !numero.isEmpty()) {
				res = new BigDecimal(FORMATO.parse(numero).toString()).setScale(2, RoundingMode.CEILING);
			}
		} catch (final ParseException ex) {
			Logger.getLogger(BigDecimalConverter.class.getName()).log(Level.SEVERE, null, ex);
		}
		return res;
	}
}
