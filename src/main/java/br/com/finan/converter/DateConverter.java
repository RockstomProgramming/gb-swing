package br.com.finan.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author Wesley Luiz
 */
public class DateConverter extends Converter<Date, String> {

    @Override
    public String convertForward(Date s) {
        return null;
    }

    @Override
    public Date convertReverse(String t) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(t);
        } catch (ParseException ex) {
            Logger.getLogger(DateConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
