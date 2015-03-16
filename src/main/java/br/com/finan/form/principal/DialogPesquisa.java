package br.com.finan.form.principal;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import br.com.finan.dto.FiltroDTO;

import net.miginfocom.swing.MigLayout;

public abstract class DialogPesquisa<T extends FiltroDTO> extends JDialog {

	private static final long serialVersionUID = 1L;
	
	protected JButton btnCancelar;
	protected JButton btnSelecionar;
	protected JPanel pnlAcao;
	
	private T filtro;
	
	public DialogPesquisa() {
		getContentPane().setLayout(new MigLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Pesquisa");
		setModal(true);
		
		try {
			filtro = obterTipoDaClasse().newInstance();
		} catch (InstantiationException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
		
		btnSelecionar = new JButton("Selecionar");
		btnCancelar = new JButton("Cancelar");
		
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		pnlAcao = new JPanel(new MigLayout());
		pnlAcao.setBorder(new EtchedBorder());
		pnlAcao.add(btnSelecionar);
		pnlAcao.add(btnCancelar);
		
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - getWidth()) / 2;
		final int y = (screenSize.height - getHeight()) / 2;
		setLocation(x, y);
	}
	
	@SuppressWarnings({ "unchecked", "restriction" })
	protected Class<T> obterTipoDaClasse() {
		return (Class<T>) ((sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public JPanel getPanelAcao() {
		return pnlAcao;
	}
	
	public T getFiltro() {
		return filtro;
	}

}
