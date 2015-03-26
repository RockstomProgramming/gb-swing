package br.com.finan.form;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.JTextComponent;

import br.com.finan.listener.FocoListener;
import br.com.finan.service.CategoriaService;
import br.com.finan.service.ContaBancariaService;
import br.com.finan.service.ContaService;

public abstract class Formulario extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private ContaService contaService;
	private CategoriaService categoriaService;
	private ContaBancariaService contaBancariaService;
	
	public Formulario() {
		contaService = new ContaService();
		categoriaService = new CategoriaService();
		contaBancariaService = new ContaBancariaService();
		
		addInternalFrameListener(new FocoListener() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				onGanharFoco();
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	protected void limparCampos(final JPanel container) {
		for (final Component comp : container.getComponents()) {
			if (comp instanceof JPanel) {
				limparCampos((JPanel) comp);
			}
			
			if (comp instanceof JTextComponent) {
				((JTextComponent) comp).setText(null);
			} else if (comp instanceof JComboBox) {
				((JComboBox) comp).setSelectedItem(null);
			}
		}
	}
	
	protected void onGanharFoco() {
		// TODO Auto-generated method stub
	}

	public ContaService getContaService() {
		return contaService;
	}

	public CategoriaService getCategoriaService() {
		return categoriaService;
	}

	public ContaBancariaService getContaBancariaService() {
		return contaBancariaService;
	}
}
