package br.com.finan.form.principal;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.DTO;
import br.com.finan.entidade.annotation.ColunaTabela;
import br.com.finan.util.HibernateUtil;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.hibernate.criterion.Projections;

/**
 *
 * @author Wesley Luiz
 * @param <T>
 */
public abstract class ListagemForm<T extends DTO> extends javax.swing.JInternalFrame {

    private DefaultTableModel model;
    private List<T> dados;
    private int pagina = 1;
    private Long qntRegistros = 0L;
    protected final int MAX_REGISTROS = 15;

    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnMesAnterior;
    private javax.swing.JButton btnMesProximo;
    private javax.swing.JButton btnPrimeiro;
    private javax.swing.JButton btnProximo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnUltimo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbPaginacao;
    private javax.swing.JPanel pnlPaginacao;
    private javax.swing.JTable tabela;

    protected void iniciarDados() {
        setModel((DefaultTableModel) getTabela().getModel());
        getTabela().setModel(getModel());
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
        return getPagina() >= getQntPagina();
    }

    protected void validarBtnPaginacao() {
//        getPanelPaginacao().getComponent(1).setEnabled(getPagina() != 1);
//        getPanelPaginacao().getComponent(2).setEnabled(getPanelPaginacao().getComponent(1).isEnabled());
//        getPanelPaginacao().getComponent(3).setEnabled(!isUltimaPagina());
//        getPanelPaginacao().getComponent(4).setEnabled(!isUltimaPagina());
//        ((JLabel) getPanelPaginacao().getComponent(0)).setText("Exibindo " + getPagina() * MAX_REGISTROS + " de " + getQntRegistros() + " registros");
    }

    protected void inativarDados(String nomeClasse) {
//        for (int i = 0; i < getTable().getRowCount(); i++) {
//            boolean b = (boolean) getTable().getValueAt(i, 0);
//            if (b) {
//                T dto = getDados().get(i);
//                HibernateUtil.inativar(dto.getId(), nomeClasse);
//            }
//        }
//
//        iniciarDados();
    }

    protected void buscarDados(int primResultado) {
        limparTabela();
        setDados(getBuilderListagem().getCriteria().setFirstResult(primResultado).setMaxResults(MAX_REGISTROS).list());
        setQntRegistros((Long) getBuilderQntRegistros().getCriteria().setProjection(Projections.rowCount()).uniqueResult());

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

//    protected abstract JTable getTable();
    protected abstract CriteriaBuilder getBuilderListagem();

    protected abstract CriteriaBuilder getBuilderQntRegistros();

//    protected abstract JPanel getPanelPaginacao();
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

    public javax.swing.JButton getBtnAnterior() {
        return btnAnterior;
    }

    public void setBtnAnterior(javax.swing.JButton btnAnterior) {
        this.btnAnterior = btnAnterior;
    }

    public javax.swing.JButton getBtnExcluir() {
        return btnExcluir;
    }

    public void setBtnExcluir(javax.swing.JButton btnExcluir) {
        this.btnExcluir = btnExcluir;
    }

    public javax.swing.JButton getBtnMesAnterior() {
        return btnMesAnterior;
    }

    public void setBtnMesAnterior(javax.swing.JButton btnMesAnterior) {
        this.btnMesAnterior = btnMesAnterior;
    }

    public javax.swing.JButton getBtnMesProximo() {
        return btnMesProximo;
    }

    public void setBtnMesProximo(javax.swing.JButton btnMesProximo) {
        this.btnMesProximo = btnMesProximo;
    }

    public javax.swing.JButton getBtnPrimeiro() {
        return btnPrimeiro;
    }

    public void setBtnPrimeiro(javax.swing.JButton btnPrimeiro) {
        this.btnPrimeiro = btnPrimeiro;
    }

    public javax.swing.JButton getBtnProximo() {
        return btnProximo;
    }

    public void setBtnProximo(javax.swing.JButton btnProximo) {
        this.btnProximo = btnProximo;
    }

    public javax.swing.JButton getBtnSalvar() {
        return btnSalvar;
    }

    public void setBtnSalvar(javax.swing.JButton btnSalvar) {
        this.btnSalvar = btnSalvar;
    }

    public javax.swing.JButton getBtnUltimo() {
        return btnUltimo;
    }

    public void setBtnUltimo(javax.swing.JButton btnUltimo) {
        this.btnUltimo = btnUltimo;
    }

    public javax.swing.JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public void setjScrollPane1(javax.swing.JScrollPane jScrollPane1) {
        this.jScrollPane1 = jScrollPane1;
    }

    public javax.swing.JLabel getLbPaginacao() {
        return lbPaginacao;
    }

    public void setLbPaginacao(javax.swing.JLabel lbPaginacao) {
        this.lbPaginacao = lbPaginacao;
    }

    public javax.swing.JPanel getPnlPaginacao() {
        return pnlPaginacao;
    }

    public void setPnlPaginacao(javax.swing.JPanel pnlPaginacao) {
        this.pnlPaginacao = pnlPaginacao;
    }

    public javax.swing.JTable getTabela() {
        return tabela;
    }

    public void setTabela(javax.swing.JTable tabela) {
        this.tabela = tabela;
    }
}
