package br.com.finan.form.principal;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.DespesaDTO;
import br.com.finan.entidade.annotation.ColunaTabela;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Wesley Luiz
 * @param <T>
 */
public abstract class ListagemForm<T> extends javax.swing.JInternalFrame {

    private DefaultTableModel model;
    private List<T> dados;

    protected void iniciarDados() {
        setModel((DefaultTableModel) getTable().getModel());
        getTable().setModel(getModel());
        buscarDados();
    }

    protected void buscarDados() {
        setDados(getBuilderListagem().list());
        List<Field> campos = new ArrayList<>();
        Class<T> clazz = obterTipoDaClasse();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ColunaTabela.class)) {
                campos.add(field);
            }
        }

        for (T d : getDados()) {
            List<Object> valores = new ArrayList();
            for (Field f : campos) {
                try {
                    String ini = d instanceof Boolean ? "is" : "get";
                    Method m = d.getClass().getDeclaredMethod(ini + f.getName());
                    Object v = m.invoke(d);
                    valores.add(v);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(ListagemForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            getModel().addRow(valores.toArray());
        }
    }

    protected Class<T> obterTipoDaClasse() {
        return (Class<T>) ((sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected abstract JTable getTable();

    protected abstract CriteriaBuilder getBuilderListagem();

    public DefaultTableModel getModel() {
        return model;
    }

    public void setModel(DefaultTableModel model) {
        this.model = model;
    }

    public List<T> getDados() {
        return dados;
    }

    public void setDados(List<T> dados) {
        this.dados = dados;
    }
}
