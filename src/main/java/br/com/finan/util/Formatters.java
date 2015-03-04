/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.finan.util;

import br.com.finan.form.despesa.ManterDespesaForm;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author fabrica
 */
public class Formatters {
    
    private MaskFormatter dateFormatter;

    public Formatters() {
        try {
            dateFormatter = new MaskFormatter("##/##/####");
        } catch (ParseException ex) {
            Logger.getLogger(ManterDespesaForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public MaskFormatter getDateFormatter() {
        return dateFormatter;
    }
    
    
}
