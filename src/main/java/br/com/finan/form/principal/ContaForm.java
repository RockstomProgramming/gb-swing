package br.com.finan.form.principal;

import br.com.finan.dto.DTO;
import br.com.finan.entidade.enumerator.Mes;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author Wesley Luiz
 * @param <T>
 */
public abstract class ContaForm<T extends DTO> extends ListagemForm<T> {

    protected static final String MASK_YEAR = "yyyy";

    private List<Mes> meses;
    private Mes mesSelecionado;
    private String ano;

    public ContaForm() {
        meses = Arrays.asList(Mes.values());
        mesSelecionado = Mes.JANEIRO;
        ano = new SimpleDateFormat(MASK_YEAR).format(new Date());
    }

    protected void irMesAnterior(JComboBox txtMes, JTextField txtAno) {
        int mes = getMesSelecionado().getReferencia() - 1;
        navegar(mes, txtAno, txtMes);
    }

    protected void irProximoMes(JComboBox txtMes, JTextField txtAno) {
        int mes = getMesSelecionado().getReferencia() + 1;
        navegar(mes, txtAno, txtMes);
    }

    private void navegar(int mes, JTextField txtAno, JComboBox txtMes) throws NumberFormatException {
        if (mes < 1) {
            mes = 12;
            final Integer a = Integer.valueOf(getAno()) - 1;
            txtAno.setText(a.toString());
        } else if (mes > 12) {
            mes = 1;
            final Integer a = Integer.valueOf(getAno()) + 1;
            txtAno.setText(a.toString());
        }

        txtMes.setSelectedItem(Mes.getMesPorReferencia(mes));
        buscarDados(1);
        validarBtnPaginacao();
    }

    public List<Mes> getMeses() {
        return meses;
    }

    public void setMeses(List<Mes> meses) {
        this.meses = meses;
    }

    public Mes getMesSelecionado() {
        return mesSelecionado;
    }

    public void setMesSelecionado(Mes mesSelecionado) {
        this.mesSelecionado = mesSelecionado;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }
}
