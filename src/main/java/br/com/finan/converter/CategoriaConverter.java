/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.finan.converter;

import br.com.finan.entidade.Categoria;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;
import org.hibernate.Hibernate;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author fabrica
 */
public class CategoriaConverter extends Converter<Categoria, String> {

    @Override
    public String convertForward(Categoria s) {
        return null;
    }

    @Override
    public Categoria convertReverse(String t) {
        Categoria categoria = (Categoria) HibernateUtil.getCriteriaBuilder(Categoria.class).eq("nome", t)
                .eqStatusAtivo().uniqueResult();
        
        if (ObjetoUtil.isReferencia(categoria)) {
            return categoria;
        }
        
        return new Categoria(t);
    }

}
