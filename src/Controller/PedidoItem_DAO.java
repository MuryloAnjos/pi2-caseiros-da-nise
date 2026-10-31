package Controller;

import Connection.ConnectionFactory;
import Model.PedidoItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class PedidoItem_DAO {

    public void create(PedidoItem pi) {

        if (pi.getQuantidade() <= 0) {
            JOptionPane.showMessageDialog(null, "A quantidade deve ser maior que zero.");
            return;
        }

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            stmt = con.prepareStatement("SELECT situacao FROM pedido WHERE id = ?");
            stmt.setInt(1, pi.getPedidoId());
            rs = stmt.executeQuery();

            if (rs.next() && !rs.getString("situacao").equals("ORCAMENTO")) {
                JOptionPane.showMessageDialog(null, "Só é possível adicionar itens em pedidos que ainda são orçamento.");
                return;
            }
            rs.close();
            stmt.close();

            stmt = con.prepareStatement("SELECT preco_venda FROM produto WHERE id = ?");
            stmt.setInt(1, pi.getProdutoId());
            rs = stmt.executeQuery();

            if (rs.next()) {
                pi.setPrecoPraticado(rs.getDouble("preco_venda"));
            }
            rs.close();
            stmt.close();

            stmt = con.prepareStatement("INSERT INTO pedido_item (pedido_id, produto_id, quantidade, preco_praticado) VALUES(?, ?, ?, ?)");
            stmt.setInt(1, pi.getPedidoId());
            stmt.setInt(2, pi.getProdutoId());
            stmt.setDouble(3, pi.getQuantidade());
            stmt.setDouble(4, pi.getPrecoPraticado());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Item Adicionado");

        } catch (SQLException ex) {
            Logger.getLogger(PedidoItem_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Salvar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

    }

    public List<PedidoItem> read(int pedidoId) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<PedidoItem> itens = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM pedido_item WHERE pedido_id = ?");
            stmt.setInt(1, pedidoId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                PedidoItem item = new PedidoItem();
                item.setId(rs.getInt("id"));
                item.setPedidoId(rs.getInt("pedido_id"));
                item.setProdutoId(rs.getInt("produto_id"));
                item.setQuantidade(rs.getDouble("quantidade"));
                item.setPrecoPraticado(rs.getDouble("preco_praticado"));
                itens.add(item);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PedidoItem_DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return itens;

    }

    public double totalizar(int pedidoId) {

        double total = 0;

        for (PedidoItem item : read(pedidoId)) {
            total = total + (item.getQuantidade() * item.getPrecoPraticado());
        }

        return total;
    }

    public void update(PedidoItem pi) {

        if (pi.getQuantidade() <= 0) {
            JOptionPane.showMessageDialog(null, "A quantidade deve ser maior que zero.");
            return;
        }

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE pedido_item SET quantidade = ? WHERE id = ?");
            stmt.setDouble(1, pi.getQuantidade());
            stmt.setInt(2, pi.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Item Atualizado");
        } catch (SQLException ex) {
            Logger.getLogger(PedidoItem_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Atualizar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

    public void delete(PedidoItem pi) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM pedido_item WHERE id = ?");
            stmt.setInt(1, pi.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Item Removido");
        } catch (SQLException ex) {
            Logger.getLogger(PedidoItem_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Excluir" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

}
