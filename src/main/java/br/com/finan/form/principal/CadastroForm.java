package br.com.finan.form.principal;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

import net.miginfocom.swing.MigLayout;
import br.com.finan.entidade.Entidade;
import br.com.finan.util.AppUtil;
import br.com.finan.util.HibernateUtil;
import br.com.finan.util.ObjetoUtil;

/**
 *
 * @author Wesley Luiz
 * @param <T> - Entidade principal.
 */
public abstract class CadastroForm<T extends Entidade> extends javax.swing.JInternalFrame {

	/** Atributo serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private T entidade;
	
	private JButton btnSalvar;
	private JPanel pnlAcao;

	public CadastroForm() {
		btnSalvar = new JButton("Salvar");
		btnSalvar.setIcon(new ImageIcon(getClass().getResource("/icon/Save.png")));
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				salvar();
			}
		});
		
		pnlAcao = new JPanel(new MigLayout());
		pnlAcao.setBorder(new EtchedBorder());
		pnlAcao.add(btnSalvar);
		
		iniciarEntidade();
		iniciarComponentes();
	}

	protected void salvar() {
		HibernateUtil.salvar(entidade);
		iniciarDados();
		AppUtil.exibirMsgSalvarSucesso(this);
	}

	protected void alterar() {

	}

	@SuppressWarnings("unchecked")
	protected T obterPorId(final Long id) {
		return (T) HibernateUtil.getCriteriaBuilder(entidade.getClass()).eqId(id).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	protected List<T> listarTudo() {
		return HibernateUtil.getCriteriaBuilder(entidade.getClass()).list();
	}

	protected void limparCampos() {
		limparCampos(getFrame(), getContainerCadastro());
	}

	protected void limparCampos(final JInternalFrame frame, final JPanel container) {
		for (final Component comp : container.getComponents()) {
			if (comp instanceof JTextComponent) {
				((JTextComponent) comp).setText(null);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "restriction" })
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

	protected boolean isReferencia(final Object... valores) {
		return ObjetoUtil.isReferencia(valores);
	}

	protected void iniciarDados() {
		limparCampos();
		iniciarEntidade();
	}

	protected JPanel getPanelAcao() {
		return pnlAcao;
	}
	
	protected abstract JInternalFrame getFrame();

	protected abstract JPanel getContainerCadastro();

	protected abstract void iniciarComponentes();
	
	public T getEntidade() {
		return entidade;
	}

	public void setEntidade(final T entidade) {
		this.entidade = entidade;
	}

	@SuppressWarnings("unchecked")
	public void setEntidade(Object entidade) {
		this.entidade = (T) entidade;
	}
}
