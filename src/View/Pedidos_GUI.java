/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Cliente_DAO;
import Controller.PedidoItem_DAO;
import Controller.Pedido_DAO;
import Controller.Produto_DAO;
import Model.Cliente;
import Model.Pedido;
import Model.PedidoItem;
import Model.Produto;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javax.swing.RowFilter;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Murylo
 */
public class Pedidos_GUI extends javax.swing.JFrame {

    private List<Cliente> clientes = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();
    private List<Pedido> pedidos = new ArrayList<>();
    private final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Pedidos_GUI() {
        initComponents();
        customizeMenuBar(barrinha);

        carregarCombos();
        readJTable();
        limpar();
    }

    private void carregarCombos() {
        cliente_cmb.removeAllItems();
        clientes = new Cliente_DAO().read();
        for (Cliente c : clientes) {
            cliente_cmb.addItem(c.getNome());
        }

        produto_cmb.removeAllItems();
        produtos = new Produto_DAO().read();
        for (Produto p : produtos) {
            produto_cmb.addItem(p.getNome());
        }
    }

    private String nomeCliente(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c.getNome();
            }
        }
        return "";
    }

    private String nomeProduto(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                return p.getNome();
            }
        }
        return "";
    }

    private Pedido buscarPedido(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void readJTable() {
        DefaultTableModel modelo = (DefaultTableModel) pedidos_table.getModel();
        modelo.setNumRows(0);

        pedidos = new Pedido_DAO().read();

        for (Pedido p : pedidos) {
            modelo.addRow(new Object[]{
                p.getId(),
                nomeCliente(p.getClienteId()),
                p.getDataEntrega().format(BR),
                p.getSituacao(),
                String.format("R$ %.2f", p.getValorPago())
            });
        }
    }

    public void readItens(int pedidoId) {
        DefaultTableModel modelo = (DefaultTableModel) itens_table.getModel();
        modelo.setNumRows(0);

        PedidoItem_DAO dao = new PedidoItem_DAO();

        for (PedidoItem i : dao.read(pedidoId)) {
            modelo.addRow(new Object[]{
                i.getId(),
                nomeProduto(i.getProdutoId()),
                String.format("%.0f", i.getQuantidade()),
                String.format("R$ %.2f", i.getPrecoPraticado()),
                String.format("R$ %.2f", i.getQuantidade() * i.getPrecoPraticado())
            });
        }
        total_lbl.setText(String.format("R$ %.2f", dao.totalizar(pedidoId)));
    }

    private void limpar() {
        id_txt.setText("");
        entrega_txt.setText("");
        pago_txt.setText("0");
        qtd_txt.setText("1");
        situacao_cmb.setSelectedIndex(0);
        if (cliente_cmb.getItemCount() > 0) {
            cliente_cmb.setSelectedIndex(0);
        }
        pedidos_table.clearSelection();
        ((DefaultTableModel) itens_table.getModel()).setNumRows(0);
        total_lbl.setText("R$ 0,00");
    }

    private double numero(String texto) {
        return Double.parseDouble(texto.trim().replace(",", "."));
    }

    private LocalDate lerEntrega() {
        try {
            return LocalDate.parse(entrega_txt.getText().trim(), BR);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Informe a data de entrega no formato dd/mm/aaaa.");
            return null;
        }
    }


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
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        id_txt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cliente_cmb = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        entrega_txt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        pago_txt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        situacao_cmb = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        busca_nome_txt = new javax.swing.JTextField();
        add_BTN = new javax.swing.JButton();
        editar_BTN = new javax.swing.JButton();
        excluir_BTN = new javax.swing.JButton();
        confirmar_BTN = new javax.swing.JButton();
        limpar_BTN = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        pedidos_table = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        produto_cmb = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        qtd_txt = new javax.swing.JTextField();
        additem_BTN = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        itens_table = new javax.swing.JTable();
        removeitem_BTN = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        total_lbl = new javax.swing.JLabel();
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
        jLabel3.setText("Pedidos");
        panelImage1.add(jLabel3);
        jLabel3.setBounds(0, 10, 170, 40);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do pedido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Inter", 0, 15), new java.awt.Color(248, 236, 220))); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Id");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, 50, 30));

        id_txt.setEditable(false);
        jPanel2.add(id_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 55, 60, 30));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Cliente");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 25, 100, 30));

        jPanel2.add(cliente_cmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 55, 300, 30));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Entrega (dd/mm/aaaa)");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 25, 160, 30));
        jPanel2.add(entrega_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 55, 140, 30));

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Valor pago");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 25, 100, 30));
        jPanel2.add(pago_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 55, 120, 30));

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Situação");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 25, 100, 30));

        situacao_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ORCAMENTO", "CONFIRMADO", "EM_PRODUCAO", "PRONTO", "ENTREGUE", "CANCELADO" }));
        jPanel2.add(situacao_cmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 55, 230, 30));

        panelImage1.add(jPanel2);
        jPanel2.setBounds(30, 55, 920, 100);

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Busca por cliente");
        panelImage1.add(jLabel10);
        jLabel10.setBounds(30, 160, 150, 25);

        busca_nome_txt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                busca_nome_txtKeyReleased(evt);
            }
        });
        panelImage1.add(busca_nome_txt);
        busca_nome_txt.setBounds(30, 185, 200, 30);

        add_BTN.setBackground(new java.awt.Color(37, 36, 36));
        add_BTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/adicionar (2).png"))); // NOI18N
        add_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_BTNActionPerformed(evt);
            }
        });
        panelImage1.add(add_BTN);
        add_BTN.setBounds(245, 165, 50, 50);

        editar_BTN.setBackground(new java.awt.Color(37, 36, 36));
        editar_BTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/editar (1).png"))); // NOI18N
        editar_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editar_BTNActionPerformed(evt);
            }
        });
        panelImage1.add(editar_BTN);
        editar_BTN.setBounds(300, 165, 50, 50);

        excluir_BTN.setBackground(new java.awt.Color(37, 36, 36));
        excluir_BTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/excluir (2).png"))); // NOI18N
        excluir_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluir_BTNActionPerformed(evt);
            }
        });
        panelImage1.add(excluir_BTN);
        excluir_BTN.setBounds(355, 165, 50, 50);

        confirmar_BTN.setBackground(new java.awt.Color(37, 36, 36));
        confirmar_BTN.setForeground(new java.awt.Color(255, 255, 255));
        confirmar_BTN.setText("Confirmar");
        confirmar_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmar_BTNActionPerformed(evt);
            }
        });
        panelImage1.add(confirmar_BTN);
        confirmar_BTN.setBounds(410, 165, 95, 50);

        limpar_BTN.setBackground(new java.awt.Color(37, 36, 36));
        limpar_BTN.setForeground(new java.awt.Color(255, 255, 255));
        limpar_BTN.setText("Limpar");
        limpar_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpar_BTNActionPerformed(evt);
            }
        });
        panelImage1.add(limpar_BTN);
        limpar_BTN.setBounds(510, 165, 80, 50);

        pedidos_table.setAutoCreateRowSorter(true);
        pedidos_table.setBackground(new java.awt.Color(248, 236, 220));
        pedidos_table.setForeground(new java.awt.Color(43, 26, 17));
        pedidos_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Cliente", "Entrega", "Situação", "Pago"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
        jScrollPane1.setBounds(30, 225, 560, 325);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Itens do pedido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Inter", 0, 15), new java.awt.Color(248, 236, 220))); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Produto");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, 100, 25));

        jPanel3.add(produto_cmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 190, 30));

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Qtd");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 25, 50, 25));
        jPanel3.add(qtd_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 55, 30));

        additem_BTN.setBackground(new java.awt.Color(37, 36, 36));
        additem_BTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/adicionar (2).png"))); // NOI18N
        additem_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                additem_BTNActionPerformed(evt);
            }
        });
        jPanel3.add(additem_BTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 40, 50, 45));

        itens_table.setAutoCreateRowSorter(true);
        itens_table.setBackground(new java.awt.Color(248, 236, 220));
        itens_table.setForeground(new java.awt.Color(43, 26, 17));
        itens_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Produto", "Qtd", "Preço", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itens_table.setAutoscrolls(false);
        itens_table.setSelectionBackground(new java.awt.Color(138, 75, 42));
        itens_table.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(itens_table);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 95, 320, 240));

        removeitem_BTN.setBackground(new java.awt.Color(37, 36, 36));
        removeitem_BTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/excluir (2).png"))); // NOI18N
        removeitem_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeitem_BTNActionPerformed(evt);
            }
        });
        jPanel3.add(removeitem_BTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 50, 40));

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Total do pedido:");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 345, 120, 30));

        total_lbl.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        total_lbl.setForeground(new java.awt.Color(248, 236, 220));
        total_lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total_lbl.setText("R$ 0,00");
        jPanel3.add(total_lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 345, 125, 30));

        panelImage1.add(jPanel3);
        jPanel3.setBounds(610, 160, 340, 390);

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
            Pedido p = buscarPedido(Integer.parseInt(modelo.getValueAt(linha, 0).toString()));
            if (p == null) {
                return;
            }
            id_txt.setText(String.valueOf(p.getId()));
            cliente_cmb.setSelectedItem(nomeCliente(p.getClienteId()));
            entrega_txt.setText(p.getDataEntrega().format(BR));
            pago_txt.setText(String.format("%.2f", p.getValorPago()));
            situacao_cmb.setSelectedItem(p.getSituacao());
            readItens(p.getId());
        }
    }//GEN-LAST:event_pedidos_tableMouseClicked

    private void busca_nome_txtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_busca_nome_txtKeyReleased
        // TODO add your handling code here:
        DefaultTableModel modelo = (DefaultTableModel) pedidos_table.getModel();
        TableRowSorter<DefaultTableModel> filtro = new TableRowSorter<>(modelo);
        pedidos_table.setRowSorter(filtro);

        String texto = busca_nome_txt.getText().trim();

        if (texto.isEmpty()) {
            filtro.setRowFilter(null);
        } else {
            filtro.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1));
        }
    }//GEN-LAST:event_busca_nome_txtKeyReleased

    private void add_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_BTNActionPerformed
        // TODO add your handling code here:
        if (cliente_cmb.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Cadastre um cliente primeiro.");
            return;
        }
        LocalDate entrega = lerEntrega();
        if (entrega == null) {
            return;
        }
        Pedido p = new Pedido(
                clientes.get(cliente_cmb.getSelectedIndex()).getId(),
                LocalDate.now(),
                entrega,
                "ORCAMENTO",
                numero(pago_txt.getText()));
        new Pedido_DAO().create(p);
        readJTable();
        limpar();
    }//GEN-LAST:event_add_BTNActionPerformed

    private void editar_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editar_BTNActionPerformed
        // TODO add your handling code here:
        if (id_txt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um pedido na tabela.");
            return;
        }
        Pedido p = buscarPedido(Integer.parseInt(id_txt.getText().trim()));
        LocalDate entrega = lerEntrega();
        if (p == null || entrega == null) {
            return;
        }
        String nova = situacao_cmb.getSelectedItem().toString();
        if (p.getSituacao().equals("ORCAMENTO") && nova.equals("CONFIRMADO")) {
            JOptionPane.showMessageDialog(this, "Para confirmar o pedido use o botão Confirmar.");
            return;
        }
        p.setDataEntrega(entrega);
        p.setValorPago(numero(pago_txt.getText()));
        p.setSituacao(nova);
        new Pedido_DAO().update(p);
        readJTable();
        limpar();
    }//GEN-LAST:event_editar_BTNActionPerformed

    private void excluir_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluir_BTNActionPerformed
        // TODO add your handling code here:
        if (id_txt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um pedido na tabela.");
            return;
        }
        int op = JOptionPane.showConfirmDialog(this,
                "Excluir o pedido " + id_txt.getText() + " de " + cliente_cmb.getSelectedItem() + "?", "Excluir",
                JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            Pedido p = new Pedido();
            p.setId(Integer.parseInt(id_txt.getText().trim()));
            new Pedido_DAO().delete(p);
            readJTable();
            limpar();
        }
    }//GEN-LAST:event_excluir_BTNActionPerformed

    private void confirmar_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmar_BTNActionPerformed
        // TODO add your handling code here:
        if (id_txt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um pedido na tabela.");
            return;
        }
        Pedido p = buscarPedido(Integer.parseInt(id_txt.getText().trim()));
        if (p == null) {
            return;
        }
        if (itens_table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um item antes de confirmar.");
            return;
        }
        new Pedido_DAO().confirmar(p);
        readJTable();
        limpar();
    }//GEN-LAST:event_confirmar_BTNActionPerformed

    private void limpar_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpar_BTNActionPerformed
        // TODO add your handling code here:
        limpar();
    }//GEN-LAST:event_limpar_BTNActionPerformed

    private void additem_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_additem_BTNActionPerformed
        // TODO add your handling code here:
        if (id_txt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um pedido na tabela.");
            return;
        }
        if (produto_cmb.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Cadastre um produto primeiro.");
            return;
        }
        double qtd;
        try {
            qtd = numero(qtd_txt.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.");
            return;
        }
        int pedidoId = Integer.parseInt(id_txt.getText().trim());
        PedidoItem item = new PedidoItem(pedidoId, produtos.get(produto_cmb.getSelectedIndex()).getId(), qtd, 0);
        new PedidoItem_DAO().create(item);
        readItens(pedidoId);
    }//GEN-LAST:event_additem_BTNActionPerformed

    private void removeitem_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeitem_BTNActionPerformed
        // TODO add your handling code here:
        int linhaTela = itens_table.getSelectedRow();
        if (linhaTela == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela de itens.");
            return;
        }
        Pedido p = buscarPedido(Integer.parseInt(id_txt.getText().trim()));
        if (p != null && !p.getSituacao().equals("ORCAMENTO")) {
            JOptionPane.showMessageDialog(this, "Só é possível remover itens de pedidos que ainda são orçamento.");
            return;
        }
        int linha = itens_table.convertRowIndexToModel(linhaTela);
        DefaultTableModel modelo = (DefaultTableModel) itens_table.getModel();
        int op = JOptionPane.showConfirmDialog(this,
                "Remover " + modelo.getValueAt(linha, 1) + " do pedido?", "Remover",
                JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            PedidoItem item = new PedidoItem();
            item.setId(Integer.parseInt(modelo.getValueAt(linha, 0).toString()));
            new PedidoItem_DAO().delete(item);
            readItens(Integer.parseInt(id_txt.getText().trim()));
        }
    }//GEN-LAST:event_removeitem_BTNActionPerformed


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
            java.util.logging.Logger.getLogger(Pedidos_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pedidos_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pedidos_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pedidos_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new Pedidos_GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_BTN;
    private javax.swing.JButton additem_BTN;
    public static javax.swing.JMenuBar barrinha;
    private javax.swing.JTextField busca_nome_txt;
    private javax.swing.JComboBox<String> cliente_cmb;
    private javax.swing.JButton confirmar_BTN;
    private javax.swing.JButton editar_BTN;
    private javax.swing.JTextField entrega_txt;
    private javax.swing.JButton excluir_BTN;
    private javax.swing.JTextField id_txt;
    private javax.swing.JTable itens_table;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton limpar_BTN;
    private javax.swing.JTextField pago_txt;
    private org.edisoncor.gui.panel.PanelImage panelImage1;
    private javax.swing.JTable pedidos_table;
    private javax.swing.JComboBox<String> produto_cmb;
    private javax.swing.JTextField qtd_txt;
    private javax.swing.JButton removeitem_BTN;
    private javax.swing.JComboBox<String> situacao_cmb;
    private javax.swing.JLabel total_lbl;
    // End of variables declaration//GEN-END:variables
}
