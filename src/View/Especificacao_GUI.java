/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Especificacao_DAO;
import Controller.Insumo_DAO;
import Controller.Produto_DAO;
import Model.Especificacao;
import Model.Insumo;
import Model.Produto;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Murylo
 */
public class Especificacao_GUI extends javax.swing.JFrame {

    private List<Produto> produtos = new ArrayList<>();
    private List<Insumo> insumos = new ArrayList<>();
    private List<Especificacao> receita = new ArrayList<>();
    private boolean carregando = false;

    private void customizeMenuBar(JMenuBar menuBar) {

        menuBar.setUI(new BasicMenuBarUI() {

            @Override
            public void paint(Graphics g, JComponent c) {
                g.setColor(new Color(43, 26, 17));          // marrom chocolate escuro
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }

        });

        MenuElement[] menus = menuBar.getSubElements();

        for (MenuElement menuElement : menus) {

            JMenu menu = (JMenu) menuElement.getComponent();
            changeComponentColors(menu);
            menu.setOpaque(true);

            MenuElement[] menuElements = menu.getSubElements();

            for (MenuElement popupMenuElement : menuElements) {

                JPopupMenu popupMenu = (JPopupMenu) popupMenuElement.getComponent();
                popupMenu.setBorder(BorderFactory.createLineBorder(new Color(138, 75, 42)));

                MenuElement[] menuItens = popupMenuElement.getSubElements();

                for (MenuElement menuItemElement : menuItens) {
                    if (menuItemElement.getComponent() instanceof JMenuItem) {
                        JMenuItem menuItem = (JMenuItem) menuItemElement.getComponent();
                        changeComponentColors(menuItem);
                        menuItem.setOpaque(true);
                    }
                }
            }
        }
    }

    private void changeComponentColors(Component comp) {
        comp.setBackground(new Color(43, 26, 17));        // fundo marrom escuro
        comp.setForeground(new Color(248, 236, 220));     // texto creme
    }

    public Especificacao_GUI() {
        initComponents();
        customizeMenuBar(barrinha);

        carregarCombos();
        produtoSelecionado();
        insumoSelecionado();
    }

    private void carregarCombos() {
        carregando = true;

        produto_cmb.removeAllItems();
        produtos = new Produto_DAO().read();
        for (Produto p : produtos) {
            produto_cmb.addItem(p.getNome());
        }

        insumo_cmb.removeAllItems();
        insumos = new Insumo_DAO().read();
        for (Insumo i : insumos) {
            insumo_cmb.addItem(i.getNome());
        }

        carregando = false;
    }

    private Produto produtoAtual() {
        int i = produto_cmb.getSelectedIndex();
        if (i == -1) {
            return null;
        }
        return produtos.get(i);
    }

    private Insumo insumoAtual() {
        int i = insumo_cmb.getSelectedIndex();
        if (i == -1) {
            return null;
        }
        return insumos.get(i);
    }

    private Insumo buscarInsumo(int id) {
        for (Insumo i : insumos) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    private void produtoSelecionado() {
        readJTable();
        atualizarPreco();
    }

    private void insumoSelecionado() {
        Insumo i = insumoAtual();
        unidade_lbl.setText(i == null ? "" : i.getUnidade());
    }

    public void readJTable() {
        DefaultTableModel modelo = (DefaultTableModel) pedidos_table.getModel();
        modelo.setNumRows(0);

        Produto p = produtoAtual();
        if (p == null) {
            receita.clear();
            return;
        }

        receita = new Especificacao_DAO().read(p.getId());

        for (Especificacao e : receita) {
            Insumo i = buscarInsumo(e.getInsumoId());
            if (i == null) {
                continue;
            }
            modelo.addRow(new Object[]{
                e.getId(),
                i.getNome(),
                String.format("%.2f", e.getQuantidade()),
                i.getUnidade(),
                String.format("R$ %.4f", i.getPreco()),
                String.format("R$ %.2f", e.getQuantidade() * i.getPreco())
            });
        }
    }

    private void atualizarPreco() {
        Produto p = produtoAtual();

        if (p == null) {
            custo_lbl.setText("R$ 0,00");
            margem_lbl.setText("0%");
            sugerido_lbl.setText("R$ 0,00");
            praticado_lbl.setText("R$ 0,00");
            aviso_lbl.setText("Cadastre um produto primeiro.");
            aviso_lbl.setForeground(new Color(248, 236, 220));
            return;
        }

        Especificacao_DAO dao = new Especificacao_DAO();
        double custo = dao.calcularCusto(p.getId());

        custo_lbl.setText(String.format("R$ %.2f", custo));
        margem_lbl.setText(String.format("%.0f%%", p.getMargem()));
        praticado_lbl.setText(String.format("R$ %.2f", p.getPrecoVenda()));

        if (p.getMargem() >= 100) {
            sugerido_lbl.setText("-");
            aviso_lbl.setText("A margem precisa ser menor que 100%.");
            aviso_lbl.setForeground(new Color(255, 140, 120));
            return;
        }

        double sugerido = dao.calcularPrecoSugerido(p.getId(), p.getMargem());
        sugerido_lbl.setText(String.format("R$ %.2f", sugerido));

        if (receita.isEmpty()) {
            aviso_lbl.setText("Receita sem ingredientes.");
            aviso_lbl.setForeground(new Color(248, 236, 220));
        } else if (p.getPrecoVenda() < sugerido) {
            aviso_lbl.setText("Preço abaixo do sugerido!");
            aviso_lbl.setForeground(new Color(255, 140, 120));
        } else {
            aviso_lbl.setText("Preço cobre o custo e a margem.");
            aviso_lbl.setForeground(new Color(150, 220, 140));
        }
    }

    private double numero(String texto) {
        return Double.parseDouble(texto.trim().replace(",", "."));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        panelImage1 = new org.edisoncor.gui.panel.PanelImage();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pedidos_table = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        produto_cmb = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        insumo_cmb = new javax.swing.JComboBox<>();
        qtd_txt = new javax.swing.JTextField();
        add_BTN = new javax.swing.JButton();
        excluir_BTN = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        unidade_lbl = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        custo_lbl = new javax.swing.JLabel();
        margem_lbl = new javax.swing.JLabel();
        sugerido_lbl = new javax.swing.JLabel();
        praticado_lbl = new javax.swing.JLabel();
        aviso_lbl = new javax.swing.JLabel();
        barrinha = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 32)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Início");

        jMenuItem3.setText("jMenuItem3");

        jMenuItem6.setText("jMenuItem6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(54, 51, 51));
        jPanel1.setLayout(null);

        panelImage1.setBackground(new java.awt.Color(138, 75, 42));
        panelImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/fundo_nise.jpg"))); // NOI18N
        panelImage1.setPreferredSize(new java.awt.Dimension(973, 530));
        panelImage1.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 32)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Especificação");
        panelImage1.add(jLabel3);
        jLabel3.setBounds(30, 10, 300, 40);

        pedidos_table.setAutoCreateRowSorter(true);
        pedidos_table.setBackground(new java.awt.Color(248, 236, 220));
        pedidos_table.setForeground(new java.awt.Color(43, 26, 17));
        pedidos_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Insumo", "Quantidade", "Unidade", "Preço Unit.", "Custo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pedidos_table.setAutoscrolls(false);
        pedidos_table.setSelectionBackground(new java.awt.Color(138, 75, 42));
        pedidos_table.setSelectionForeground(new java.awt.Color(255, 255, 255));
        pedidos_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pedidos_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(pedidos_table);

        panelImage1.add(jScrollPane1);
        jScrollPane1.setBounds(30, 180, 560, 370);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Receita do Produto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Inter", 0, 15), new java.awt.Color(248, 236, 220))); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Produto");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 70, 30));

        produto_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                produto_cmbActionPerformed(evt);
            }
        });
        jPanel2.add(produto_cmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 300, 30));

        panelImage1.add(jPanel2);
        jPanel2.setBounds(30, 60, 340, 110);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Adicionar Ingrediente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Inter", 0, 15), new java.awt.Color(248, 236, 220))); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Insumo");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 70, 30));

        insumo_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insumo_cmbActionPerformed(evt);
            }
        });
        jPanel3.add(insumo_cmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 260, 30));

        jPanel3.add(qtd_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 90, 30));

        add_BTN.setBackground(new java.awt.Color(37, 36, 36));
        add_BTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/adicionar (2).png"))); // NOI18N
        add_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_BTNActionPerformed(evt);
            }
        });
        jPanel3.add(add_BTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 60, 60, 30));

        excluir_BTN.setBackground(new java.awt.Color(37, 36, 36));
        excluir_BTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/excluir (2).png"))); // NOI18N
        excluir_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluir_BTNActionPerformed(evt);
            }
        });
        jPanel3.add(excluir_BTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, 60, 30));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Quantidade");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 110, 30));
        unidade_lbl.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.add(unidade_lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 60, 30, 30));

        panelImage1.add(jPanel3);
        jPanel3.setBounds(390, 60, 570, 110);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Preço", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Inter", 0, 15), new java.awt.Color(248, 236, 220))); // NOI18N
        jPanel4.setOpaque(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Preço Praticado");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 120, 30));

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Custo dos Ingredientes");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 180, 30));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Margem");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 70, 30));

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Preço Sugerido");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 120, 30));

        custo_lbl.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        custo_lbl.setForeground(new java.awt.Color(248, 236, 220));
        custo_lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        custo_lbl.setText("R$ 0,00");
        jPanel4.add(custo_lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 130, 30));

        margem_lbl.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        margem_lbl.setForeground(new java.awt.Color(248, 236, 220));
        margem_lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        margem_lbl.setText("0%");
        jPanel4.add(margem_lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 130, 30));

        sugerido_lbl.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        sugerido_lbl.setForeground(new java.awt.Color(248, 236, 220));
        sugerido_lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        sugerido_lbl.setText("R$ 0,00");
        jPanel4.add(sugerido_lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 150, 130, 30));

        praticado_lbl.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        praticado_lbl.setForeground(new java.awt.Color(248, 236, 220));
        praticado_lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        praticado_lbl.setText("R$ 0,00");
        jPanel4.add(praticado_lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 130, 30));

        panelImage1.add(jPanel4);
        jPanel4.setBounds(620, 180, 320, 270);

        aviso_lbl.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        aviso_lbl.setForeground(new java.awt.Color(248, 236, 220));
        aviso_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelImage1.add(aviso_lbl);
        aviso_lbl.setBounds(620, 460, 320, 30);

        jPanel1.add(panelImage1);
        panelImage1.setBounds(0, 0, 980, 580);

        barrinha.setBackground(new java.awt.Color(0, 0, 0));

        jMenu1.setBackground(new java.awt.Color(0, 0, 0));
        jMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("Início");

        jMenuItem2.setText("Tela Inicial");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        barrinha.add(jMenu1);

        jMenu2.setBackground(new java.awt.Color(0, 0, 0));
        jMenu2.setForeground(new java.awt.Color(255, 255, 255));
        jMenu2.setLabel("Cadastros");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem4.setText("Clientes");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem8.setText("Insumos");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem9.setText("Produtos");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        barrinha.add(jMenu2);

        jMenu3.setBackground(new java.awt.Color(0, 0, 0));
        jMenu3.setForeground(new java.awt.Color(255, 255, 255));
        jMenu3.setText("Especificação");

        jMenuItem5.setText("Receitas");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        barrinha.add(jMenu3);

        jMenu4.setBackground(new java.awt.Color(0, 0, 0));
        jMenu4.setForeground(new java.awt.Color(255, 255, 255));
        jMenu4.setText("Pedidos");
        jMenu4.setToolTipText("");

        jMenuItem7.setText("Pedidos");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        barrinha.add(jMenu4);

        jMenu8.setBackground(new java.awt.Color(0, 0, 0));
        jMenu8.setForeground(new java.awt.Color(255, 255, 255));
        jMenu8.setText("Sair");
        jMenu8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu8ActionPerformed(evt);
            }
        });

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        jMenuItem1.setText("Fechar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem1);

        barrinha.add(jMenu8);

        setJMenuBar(barrinha);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(976, 600));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:

        String x = JOptionPane.showInputDialog(null, "Deseja realmente fechar ?"
                + " \n1 - Sim"
                + " \n2 - Não "
        );
        int op = Integer.parseInt(x);

        if (op == 1) {
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenu8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu8ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenu8ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        new Inicio_GUI().setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        new Clientes_GUI().setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        new Especificacao_GUI().setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        new Pedidos_GUI().setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        new Insumos_GUI().setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        new Produtos_GUI().setVisible(true);
        dispose();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void pedidos_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pedidos_tableMouseClicked
        // TODO add your handling code here:
        int linhaTela = pedidos_table.getSelectedRow();
        if (linhaTela != -1) {
            int linha = pedidos_table.convertRowIndexToModel(linhaTela);
            DefaultTableModel modelo = (DefaultTableModel) pedidos_table.getModel();
            insumo_cmb.setSelectedItem(modelo.getValueAt(linha, 1).toString());
            qtd_txt.setText(modelo.getValueAt(linha, 2).toString());
        }
    }//GEN-LAST:event_pedidos_tableMouseClicked

    private void add_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_BTNActionPerformed
        // TODO add your handling code here:
        Produto p = produtoAtual();
        Insumo i = insumoAtual();

        if (p == null || i == null) {
            JOptionPane.showMessageDialog(this, "Selecione o produto e o insumo.");
            return;
        }
        if (qtd_txt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a quantidade.");
            return;
        }

        double qtd;
        try {
            qtd = numero(qtd_txt.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.");
            return;
        }

        Especificacao existente = null;
        for (Especificacao e : receita) {
            if (e.getInsumoId() == i.getId()) {
                existente = e;
            }
        }

        if (existente != null) {
            int op = JOptionPane.showConfirmDialog(this,
                    i.getNome() + " já está na receita. Atualizar a quantidade para " + qtd + " " + i.getUnidade() + "?",
                    "Atualizar", JOptionPane.YES_NO_OPTION);
            if (op != JOptionPane.YES_OPTION) {
                return;
            }
            if (qtd <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.");
                return;
            }
            existente.setQuantidade(qtd);
            new Especificacao_DAO().update(existente);
        } else {
            new Especificacao_DAO().create(new Especificacao(p.getId(), i.getId(), qtd));
        }

        qtd_txt.setText("");
        produtoSelecionado();
    }//GEN-LAST:event_add_BTNActionPerformed

    private void excluir_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluir_BTNActionPerformed
        // TODO add your handling code here:
        int linhaTela = pedidos_table.getSelectedRow();
        if (linhaTela == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um ingrediente na tabela.");
            return;
        }
        int linha = pedidos_table.convertRowIndexToModel(linhaTela);
        DefaultTableModel modelo = (DefaultTableModel) pedidos_table.getModel();

        int op = JOptionPane.showConfirmDialog(this,
                "Remover " + modelo.getValueAt(linha, 1) + " da receita?", "Excluir",
                JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            Especificacao e = new Especificacao();
            e.setId(Integer.parseInt(modelo.getValueAt(linha, 0).toString()));
            new Especificacao_DAO().delete(e);
            qtd_txt.setText("");
            produtoSelecionado();
        }
    }//GEN-LAST:event_excluir_BTNActionPerformed

    private void produto_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_produto_cmbActionPerformed
        // TODO add your handling code here:
        if (!carregando) {
            produtoSelecionado();
        }
    }//GEN-LAST:event_produto_cmbActionPerformed

    private void insumo_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insumo_cmbActionPerformed
        // TODO add your handling code here:
        if (!carregando) {
            insumoSelecionado();
        }
    }//GEN-LAST:event_insumo_cmbActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Especificacao_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Especificacao_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Especificacao_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Especificacao_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Especificacao_GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_BTN;
    private javax.swing.JLabel aviso_lbl;
    public static javax.swing.JMenuBar barrinha;
    private javax.swing.JLabel custo_lbl;
    private javax.swing.JButton excluir_BTN;
    private javax.swing.JComboBox<String> insumo_cmb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel margem_lbl;
    private org.edisoncor.gui.panel.PanelImage panelImage1;
    private javax.swing.JTable pedidos_table;
    private javax.swing.JLabel praticado_lbl;
    private javax.swing.JComboBox<String> produto_cmb;
    private javax.swing.JTextField qtd_txt;
    private javax.swing.JLabel sugerido_lbl;
    private javax.swing.JLabel unidade_lbl;
    // End of variables declaration//GEN-END:variables
}
