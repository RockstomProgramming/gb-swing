package br.com.finan.form.principal;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.entidade.annotation.ColunaTabela;
import br.com.finan.util.NumberUtil;
import br.com.finan.util.StringUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.hibernate.criterion.Projections;

/**
 *
 * @author Wesley Luiz
 * @param <T>
 */
public abstract class ListagemForm<T> extends javax.swing.JInternalFrame {

    private DefaultTableModel model;
    private List<T> dados;
    private int pagina = 1;
    private Long qntRegistros = 0L;
    protected final int MAX_REGISTROS = 15;

    protected void iniciarDados() {
        setModel((DefaultTableModel) getTable().getModel());
        getTable().setModel(getModel());
        buscarDados(0);
        validarBtnPaginacao();
    }

    protected void irProximaPagina() {
        setPagina(getPagina() + 1);
        paginar();
    }

    protected void irPaginaAnterior() {
        setPagina(getPagina() == 0 ? 0 : getPagina() - 1);
        paginar();
    }

    protected void irPrimeiraPagina() {
        setPagina(1);
        paginar();
    }

    protected void irUltimaPagina() {
        setPagina(getQntPagina());
        paginar();
    }

    private void paginar() {
        final int max = getPagina() * MAX_REGISTROS;
        final int min = max - MAX_REGISTROS;
        buscarDados(min);
        validarBtnPaginacao();
    }

    protected int getQntPagina() {
        int p = (int) (getQntRegistros() / MAX_REGISTROS);
        if (getQntRegistros() % MAX_REGISTROS > 0) {
            p++;
        }
        return p;
    }

    protected boolean isUltimaPagina() {
        return pagina >= getQntPagina();
    }
    
    protected void validarBtnPaginacao() {
        
    }

    protected void buscarDados(int primResultado) {
        limparTabela();
        setDados(getBuilderListagem().getCriteria().setFirstResult(primResultado).setMaxResults(MAX_REGISTROS).list());

        if (getQntRegistros().equals(0L)) {
            setQntRegistros((Long) getBuilderQntRegistros().getCriteria().setProjection(Projections.rowCount()).uniqueResult());
        }

        Map<Integer, Field> campos = new TreeMap<Integer, Field>();
        Class<T> clazz = obterTipoDaClasse();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ColunaTabela.class)) {
                campos.put(field.getAnnotation(ColunaTabela.class).value(), field);
            }
        }

        for (T d : getDados()) {
            List<Object> valores = new ArrayList();
            for (Field f : campos.values()) {
                try {
                    String ini = f.getType().getName().equals("boolean") ? "is" : "get";
                    Method m = d.getClass().getDeclaredMethod(ini + StringUtil.toPrimeiraLetraMaiuscula(f.getName()));
                    Object v = m.invoke(d);
                    valores.add(formatarTipos(v));
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(ListagemForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            getModel().addRow(valores.toArray());
        }
    }

    protected void limparTabela() {
        while (getModel().getRowCount() > 0) {
            getModel().removeRow(0);
        }
    }

    protected Class<T> obterTipoDaClasse() {
        return (Class<T>) ((sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private Object formatarTipos(Object v) {
        if (v instanceof BigDecimal) {
            return NumberUtil.obterNumeroFormatado(v);
        } else if (v instanceof Date) {
            return new SimpleDateFormat("dd/MM/yyyy").format(v);
        }

        return v;
    }

    protected abstract JTable getTable();

    protected abstract CriteriaBuilder getBuilderListagem();

    protected abstract CriteriaBuilder getBuilderQntRegistros();

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

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public Long getQntRegistros() {
        return qntRegistros;
    }

    public void setQntRegistros(Long qntRegistros) {
        this.qntRegistros = qntRegistros;
    }
}
