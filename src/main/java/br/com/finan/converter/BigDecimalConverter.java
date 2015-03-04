/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.finan.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author fabrica
 */
public class BigDecimalConverter extends Converter<BigDecimal, String> {

    NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));

    public BigDecimalConverter() {
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    @Override
    public String convertForward(BigDecimal s) {
        return s.toString();
    }

    @Override
    public BigDecimal convertReverse(String t) {
        if (t != null && !t.isEmpty()) {
            try {
                return new BigDecimal(format.parse(t).toString()).setScale(2, RoundingMode.CEILING);
            } catch (ParseException ex) {
                Logger.getLogger(BigDecimalConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
