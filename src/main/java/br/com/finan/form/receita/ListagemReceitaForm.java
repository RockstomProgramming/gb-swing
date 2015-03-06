package br.com.finan.form.receita;

import br.com.finan.dao.CriteriaBuilder;
import br.com.finan.dto.DespesaDTO;
import br.com.finan.entidade.Conta;
import br.com.finan.entidade.enumerator.TipoConta;
import br.com.finan.form.principal.ListagemContaForm;
import br.com.finan.util.HibernateUtil;
import org.hibernate.criterion.Order;
import org.hibernate.sql.JoinType;

/**
 *
 * @author Wesley Luiz
 */
public class ListagemReceitaForm extends ListagemContaForm<DespesaDTO> {

    public ListagemReceitaForm() {
        initComponents();
        iniciarDados();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setBindingGroup(new org.jdesktop.beansbinding.BindingGroup());

        setjScrollPane1(new javax.swing.JScrollPane());
        setTabela(new javax.swing.JTable());
        setBtnSalvar(new javax.swing.JButton());
        setBtnExcluir(new javax.swing.JButton());
        setBtnMesAnterior(new javax.swing.JButton());
        setBtnMesProximo(new javax.swing.JButton());
        setTxtMes(new javax.swing.JComboBox());
        setTxtAno(new javax.swing.JTextField());
        setPnlPaginacao(new javax.swing.JPanel());
        setLbPaginacao(new javax.swing.JLabel());
        setBtnAnterior(new javax.swing.JButton());
        setBtnProximo(new javax.swing.JButton());
        setBtnUltimo(new javax.swing.JButton());
        setBtnPrimeiro(new javax.swing.JButton());

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        getTabela().setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "", "Descrição", "Categoria", "Vencimento", "Valor"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        getjScrollPane1().setViewportView(getTabela());

        getBtnSalvar().setText("Salvar");
        getBtnSalvar().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                btnSalvarActionPerformed(evt);
            }
        });

        getBtnExcluir().setText("Excluir");
        getBtnExcluir().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inativarDados(Conta.class.getSimpleName());
            }
        });

        getBtnMesAnterior().setText("<");
        getBtnMesAnterior().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                irMesAnterior(getTxtMes(), getTxtAno());
            }
        });

        getBtnMesProximo().setText(">");
        getBtnMesProximo().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                irProximoMes(getTxtMes(), getTxtAno());
            }
        });

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${meses}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, getTxtMes());
        getBindingGroup().addBinding(jComboBoxBinding);
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${mesSelecionado}"), getTxtMes(), org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        getBindingGroup().addBinding(binding);

        getTxtAno().setEnabled(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${ano}"), getTxtAno(), org.jdesktop.beansbinding.BeanProperty.create("text"));
        getBindingGroup().addBinding(binding);

        getPnlPaginacao().setBorder(javax.swing.BorderFactory.createEtchedBorder());

        getLbPaginacao().setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        getLbPaginacao().setText("jLabel1");

        getBtnAnterior().setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/rewind.png"))); // NOI18N
        getBtnAnterior().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irPaginaAnterior();
            }
        });

        getBtnProximo().setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/forward.png"))); // NOI18N
        getBtnProximo().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irProximaPagina();
            }
        });

        getBtnUltimo().setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/last.png"))); // NOI18N
        getBtnUltimo().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irUltimaPagina();
            }
        });

        getBtnPrimeiro().setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/first.png"))); // NOI18N
        getBtnPrimeiro().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irPrimeiraPagina();
            }
        });

        definirLayout();

        getBindingGroup().bind();

        pack();
    }

    @Override
    protected CriteriaBuilder getBuilderListagem() {
        return HibernateUtil.getCriteriaBuilder(Conta.class)
                .eqStatusAtivo()
                .eq("tipo", TipoConta.DESPESA)
                .sqlRestrictions("MONTH(dataVencimento) = " + getMesSelecionado().getReferencia())
                .sqlRestrictions("YEAR(dataVencimento) = " + getAno())
                .addAliases("categoria", "categoria", JoinType.LEFT_OUTER_JOIN)
                .addProjection("id", "id")
                .addProjection("descricao", "descricao")
                .addProjection("valor", "valor")
                .addProjection("dataVencimento", "vencimento")
                .addProjection("parcela", "parcela")
                .addProjection("totalParcelas", "totalParcela")
                .addProjection("categoria.nome", "categoria")
                .addAliasToBean(DespesaDTO.class).close()
                .addOrdenacao(Order.asc("dataVencimento"));
    }

    @Override
    protected CriteriaBuilder getBuilderQntRegistros() {
        return HibernateUtil.getCriteriaBuilder(Conta.class)
                .eqStatusAtivo()
                .eq("tipo", TipoConta.DESPESA)
                .sqlRestrictions("MONTH(dataVencimento) = " + getMesSelecionado().getReferencia())
                .sqlRestrictions("YEAR(dataVencimento) = " + getAno());
    }
}
