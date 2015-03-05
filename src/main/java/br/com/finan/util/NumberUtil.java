package br.com.finan.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitária responsável por fornecer funções de formatação de objetos
 * numérios.<br/>
 * Arquivo: NumberUtil.java <br/>
 * Criado em: 10/09/2014
 *
 * @author Wesley Luiz
 * @version 1.0.0
 */
public final class NumberUtil {

    private static final NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));
    public static final int DECIMAL_DIGITS = 2;

    private NumberUtil() {
        super();
    }

    /**
     * Método responsável por transformar um objeto numérico em uma
     * <code>String</code> formatada na moeda local.
     *
     * @author Wesley Luiz
     * @param value
     * @return
     */
    public static String obterNumeroFormatado(final Object value) {
        configurarFormato();
        return format.format(value);
    }

    /**
     * Método responsável por transformar uma <code>String</code> em um
     * <code>BigDecimal</code>.
     *
     * @author Wesley Luiz
     * @param value
     * @return
     */
    public static BigDecimal obterNumeroFormatado(final String value) {
        try {
            return new BigDecimal(format.parse(value).toString()).setScale(DECIMAL_DIGITS, RoundingMode.CEILING);
        } catch (final ParseException ex) {
            Logger.getLogger(NumberUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Método responsável por configurar o formato da saída do objeto numério
     * obtido nos métodos desta classe.
     *
     * @author Wesley Luiz
     */
    private static void configurarFormato() {
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }
}
