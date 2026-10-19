package Controller;

import Connection.ConnectionFactory;
import Model.Pedido;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Pedido_DAO {

    private static final int CAPACIDADE_DIA = 5;

    public void create(Pedido p) {

        if (p.getDataEntrega().isBefore(p.getDataPedido())) {
            JOptionPane.showMessageDialog(null, "A data de entrega não pode ser antes da data do pedido.");
            return;
        }

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("INSERT INTO pedido (cliente_id, data_pedido, data_entrega, situacao, valor_pago) VALUES(?, ?, ?, ?, ?)");
            stmt.setInt(1, p.getClienteId());
            stmt.setDate(2, Date.valueOf(p.getDataPedido()));
            stmt.setDate(3, Date.valueOf(p.getDataEntrega()));
            stmt.setString(4, "ORCAMENTO");
            stmt.setDouble(5, p.getValorPago());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Pedido Criado");
        } catch (SQLException ex) {
            Logger.getLogger(Pedido_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Salvar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

    public List<Pedido> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Pedido> pedidos = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM pedido ORDER BY data_entrega");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Pedido pdd = new Pedido();
                pdd.setId(rs.getInt("id"));
                pdd.setClienteId(rs.getInt("cliente_id"));
                pdd.setDataPedido(rs.getDate("data_pedido").toLocalDate());
                pdd.setDataEntrega(rs.getDate("data_entrega").toLocalDate());
                pdd.setSituacao(rs.getString("situacao"));
                pdd.setValorPago(rs.getDouble("valor_pago"));
                pedidos.add(pdd);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Pedido_DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return pedidos;

    }

    public void confirmar(Pedido p) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con.setAutoCommit(false);

            stmt = con.prepareStatement("SELECT situacao FROM pedido WHERE id = ?");
            stmt.setInt(1, p.getId());
            rs = stmt.executeQuery();

            if (rs.next() && !rs.getString("situacao").equals("ORCAMENTO")) {
                JOptionPane.showMessageDialog(null, "Este pedido já foi confirmado ou encerrado.");
                return;
            }
            rs.close();
            stmt.close();

            stmt = con.prepareStatement("SELECT COUNT(*) AS total FROM pedido WHERE data_entrega = ? AND situacao IN ('CONFIRMADO', 'EM_PRODUCAO', 'PRONTO')");
            stmt.setDate(1, Date.valueOf(p.getDataEntrega()));
            rs = stmt.executeQuery();

            if (rs.next() && rs.getInt("total") >= CAPACIDADE_DIA) {
                JOptionPane.showMessageDialog(null, "Não há mais vagas de produção nesta data.");
                return;
            }
            rs.close();
            stmt.close();

            stmt = con.prepareStatement(
                    "UPDATE insumo i "
                    + "JOIN (SELECT e.insumo_id, SUM(e.quantidade * pi.quantidade) AS consumo "
                    + "      FROM pedido_item pi "
                    + "      JOIN especificacao e ON e.produto_id = pi.produto_id "
                    + "      WHERE pi.pedido_id = ? "
                    + "      GROUP BY e.insumo_id) c ON c.insumo_id = i.id "
                    + "SET i.qtd_estoque = i.qtd_estoque - c.consumo"
            );
            stmt.setInt(1, p.getId());
            stmt.executeUpdate();
            stmt.close();

            stmt = con.prepareStatement("UPDATE pedido SET situacao = 'CONFIRMADO' WHERE id = ?");
            stmt.setInt(1, p.getId());
            stmt.executeUpdate();

            con.commit();
            JOptionPane.showMessageDialog(null, "Pedido Confirmado");

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                Logger.getLogger(Pedido_DAO.class.getName()).log(Level.SEVERE, null, e);
            }
            Logger.getLogger(Pedido_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Confirmar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

    }

    public void update(Pedido p) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE pedido SET data_entrega = ?, situacao = ?, valor_pago = ? WHERE id = ?");
            stmt.setDate(1, Date.valueOf(p.getDataEntrega()));
            stmt.setString(2, p.getSituacao());
            stmt.setDouble(3, p.getValorPago());
            stmt.setInt(4, p.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Pedido Atualizado");
        } catch (SQLException ex) {
            Logger.getLogger(Pedido_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Atualizar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

    public void delete(Pedido p) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM pedido_item WHERE pedido_id = ?");
            stmt.setInt(1, p.getId());
            stmt.executeUpdate();
            stmt.close();

            stmt = con.prepareStatement("DELETE FROM pedido WHERE id = ?");
            stmt.setInt(1, p.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Pedido Deletado");
        } catch (SQLException ex) {
            Logger.getLogger(Pedido_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Excluir" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

}
