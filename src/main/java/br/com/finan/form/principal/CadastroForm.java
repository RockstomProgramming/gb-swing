package br.com.finan.form.principal;

import br.com.finan.util.Formatters;
import br.com.finan.entidade.Entidade;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;
import java.awt.Component;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Wesley Luiz
 * @param <T> - Entidade principal.
 */
public abstract class CadastroForm<T extends Entidade> extends javax.swing.JInternalFrame {

    private T entidade;

    private final Formatters formatters;

    public CadastroForm() {
        iniciarEntidade();
        formatters = new Formatters();
    }

    protected void salvar() {
//        HibernateUtil.salvar(entidade);
        limparCampos();
        iniciarDados();
        JOptionPane.showMessageDialog(getFrame(), "Dados Salvos com sucesso");
    }

    protected void alterar() {

    }

    protected T obterPorId(Long id) {
        return (T) HibernateUtil.getCriteriaBuilder(entidade.getClass()).eqId(id).uniqueResult();
    }

    protected List<T> listarTudo() {
        return HibernateUtil.getCriteriaBuilder(entidade.getClass()).list();
    }

    protected void limparCampos() {
        limparCampos(getFrame(), getContainerCadastro());
    }

    protected void limparCampos(JInternalFrame frame, JPanel container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextComponent) {
                ((JTextComponent) comp).setText(null);
            }
        }
    }

    protected Class<T> obterTipoDaClasse() {
        return (Class<T>) ((sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private void iniciarEntidade() {
        try {
            setEntidade(obterTipoDaClasse().newInstance());
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(CadastroForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected boolean isReferencia(Object... valores) {
        return ObjetoUtil.isReferencia(valores);
    }

    protected abstract void iniciarDados();

    protected abstract JInternalFrame getFrame();

    protected abstract JPanel getContainerCadastro();

    public T getEntidade() {
        return entidade;
    }

    public void setEntidade(T entidade) {
        this.entidade = entidade;
    }

    public Formatters getFormatters() {
        return formatters;
    }

}
