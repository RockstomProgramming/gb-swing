package br.com.finan.converter;

import br.com.finan.entidade.Categoria;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author Wesley Luiz
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
