package br.com.finan.util;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.MaskFormatter;

import br.com.finan.form.despesa.CadastroDespesaForm;

/**
 *
 * @author Wesley Luiz
 */
public class Formatters {

	private MaskFormatter dateFormatter;

	public Formatters() {
		try {
			dateFormatter = new MaskFormatter("##/##/####");
		} catch (final ParseException ex) {
			Logger.getLogger(CadastroDespesaForm.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public MaskFormatter getDateFormatter() {
		return dateFormatter;
	}

}
