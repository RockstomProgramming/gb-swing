package br.com.finan.form;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

public class ComponentesCadastro {

	private JPanel panelAcoes;
	private JScrollPane scroll;
	private JTable tabela;
	private JButton btnSalvar;
	private JButton btnNovo;
	private JButton btnExcluir;
	private JButton btnAtualizar;
	private JButton btnAnterior;
	private JButton btnPrimeiro;
	private JButton btnProximo;
	private JButton btnUltimo;
	private JLabel lbPaginacao;
	private JPanel pnlPaginacao;
	private JPanel pnlFiltro;

	public ComponentesCadastro() {
		setBtnExcluir(new JButton("Excluir", new ImageIcon(getClass().getResource("/icon/Delete.png"))));
		setBtnNovo(new JButton("Novo", new ImageIcon(getClass().getResource("/icon/Add.png"))));
		setBtnSalvar(new JButton("Salvar", new ImageIcon(getClass().getResource("/icon/Save.png"))));
		setBtnPrimeiro(new JButton(new ImageIcon(getClass().getResource("/icon/Symbol_Rewind.png"))));
		setBtnAnterior(new JButton(new ImageIcon(getClass().getResource("/icon/Symbol_Play_Reversed.png"))));
		setBtnProximo(new JButton(new ImageIcon(getClass().getResource("/icon/Symbol_Play.png"))));
		setBtnUltimo(new JButton(new ImageIcon(getClass().getResource("/icon/Symbol_FastForward.png"))));
		setBtnAtualizar(new JButton("Atualizar", new ImageIcon(getClass().getResource("/icon/refresh.png"))));
		setPnlPaginacao(new JPanel());
		setLbPaginacao(new JLabel());
		setTabela(new JTable());
		setScroll(new JScrollPane());
		setPanelAcoes(new JPanel(new MigLayout()));
		setPnlFiltro(new JPanel(new MigLayout()));
	}

	public JPanel getPanelAcoes() {
		return panelAcoes;
	}

	public void setPanelAcoes(final JPanel panelAcoes) {
		this.panelAcoes = panelAcoes;
	}

	public JScrollPane getScroll() {
		return scroll;
	}

	public void setScroll(final JScrollPane scroll) {
		this.scroll = scroll;
	}

	public JTable getTabela() {
		return tabela;
	}

	public void setTabela(final JTable tabela) {
		this.tabela = tabela;
	}

	public JButton getBtnSalvar() {
		return btnSalvar;
	}

	public void setBtnSalvar(final JButton btnSalvar) {
		this.btnSalvar = btnSalvar;
	}

	public JButton getBtnNovo() {
		return btnNovo;
	}

	public void setBtnNovo(final JButton btnNovo) {
		this.btnNovo = btnNovo;
	}

	public JButton getBtnExcluir() {
		return btnExcluir;
	}

	public void setBtnExcluir(final JButton btnExcluir) {
		this.btnExcluir = btnExcluir;
	}

	public JButton getBtnAtualizar() {
		return btnAtualizar;
	}

	public void setBtnAtualizar(final JButton btnAtualizar) {
		this.btnAtualizar = btnAtualizar;
	}

	public JButton getBtnAnterior() {
		return btnAnterior;
	}

	public void setBtnAnterior(final JButton btnAnterior) {
		this.btnAnterior = btnAnterior;
	}

	public JButton getBtnPrimeiro() {
		return btnPrimeiro;
	}

	public void setBtnPrimeiro(final JButton btnPrimeiro) {
		this.btnPrimeiro = btnPrimeiro;
	}

	public JButton getBtnProximo() {
		return btnProximo;
	}

	public void setBtnProximo(final JButton btnProximo) {
		this.btnProximo = btnProximo;
	}

	public JButton getBtnUltimo() {
		return btnUltimo;
	}

	public void setBtnUltimo(final JButton btnUltimo) {
		this.btnUltimo = btnUltimo;
	}

	public JLabel getLbPaginacao() {
		return lbPaginacao;
	}

	public void setLbPaginacao(final JLabel lbPaginacao) {
		this.lbPaginacao = lbPaginacao;
	}

	public JPanel getPnlPaginacao() {
		return pnlPaginacao;
	}

	public void setPnlPaginacao(final JPanel pnlPaginacao) {
		this.pnlPaginacao = pnlPaginacao;
	}

	public JPanel getPnlFiltro() {
		return pnlFiltro;
	}

	public void setPnlFiltro(final JPanel pnlFiltro) {
		this.pnlFiltro = pnlFiltro;
	}
}