/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.finan.form.principal;

import br.com.finan.dto.DTO;
import br.com.finan.entidade.enumerator.Mes;

/**
 *
 * @author fabrica
 * @param <T>
 */
public abstract class ListagemContaForm<T extends DTO> extends ListagemForm<T> {

    private javax.swing.JTextField txtAno;
    private javax.swing.JComboBox txtMes;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private Mes mesSelecionado;
    private String ano;

    protected void definirLayout() {
        javax.swing.GroupLayout pnlPaginacaoLayout = new javax.swing.GroupLayout(getPnlPaginacao());
        getPnlPaginacao().setLayout(pnlPaginacaoLayout);
        pnlPaginacaoLayout.setHorizontalGroup(
                pnlPaginacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPaginacaoLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(getLbPaginacao(), javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(getBtnPrimeiro())
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(getBtnAnterior())
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(getBtnProximo())
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(getBtnUltimo())
                        .addContainerGap())
        );
        pnlPaginacaoLayout.setVerticalGroup(
                pnlPaginacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPaginacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(getBtnUltimo())
                        .addComponent(getBtnProximo())
                        .addComponent(getBtnAnterior())
                        .addComponent(getBtnPrimeiro()))
                .addComponent(getLbPaginacao(), javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(getjScrollPane1(), javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(getBtnMesAnterior())
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(getTxtMes(), javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(getBtnMesProximo())
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(getTxtAno(), javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(getBtnSalvar(), javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(getBtnExcluir())))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(getPnlPaginacao(), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{getBtnExcluir(), getBtnSalvar()});

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(getBtnMesAnterior())
                                .addComponent(getBtnMesProximo())
                                .addComponent(getTxtMes(), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(getTxtAno(), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(getjScrollPane1(), javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getBtnSalvar(), javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(getBtnExcluir(), javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(getPnlPaginacao(), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
        );
    }

    public javax.swing.JTextField getTxtAno() {
        return txtAno;
    }

    public void setTxtAno(javax.swing.JTextField txtAno) {
        this.txtAno = txtAno;
    }

    public javax.swing.JComboBox getTxtMes() {
        return txtMes;
    }

    public void setTxtMes(javax.swing.JComboBox txtMes) {
        this.txtMes = txtMes;
    }

    public org.jdesktop.beansbinding.BindingGroup getBindingGroup() {
        return bindingGroup;
    }

    public void setBindingGroup(org.jdesktop.beansbinding.BindingGroup bindingGroup) {
        this.bindingGroup = bindingGroup;
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
