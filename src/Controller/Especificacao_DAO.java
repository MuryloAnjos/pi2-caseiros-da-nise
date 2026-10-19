package Controller;

import Connection.ConnectionFactory;
import Model.Especificacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Especificacao_DAO {

    public void create(Especificacao e) {

        if (e.getQuantidade() <= 0) {
            JOptionPane.showMessageDialog(null, "A quantidade deve ser maior que zero.");
            return;
        }

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            stmt = con.prepareStatement("SELECT id FROM especificacao WHERE produto_id = ? AND insumo_id = ?");
            stmt.setInt(1, e.getProdutoId());
            stmt.setInt(2, e.getInsumoId());
            rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Este insumo já está na receita do produto.");
                return;
            }
            rs.close();
            stmt.close();

            stmt = con.prepareStatement("INSERT INTO especificacao (produto_id, insumo_id, quantidade) VALUES(?, ?, ?)");
            stmt.setInt(1, e.getProdutoId());
            stmt.setInt(2, e.getInsumoId());
            stmt.setDouble(3, e.getQuantidade());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Insumo Adicionado na Receita");

        } catch (SQLException ex) {
            Logger.getLogger(Especificacao_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Salvar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

    }

    public List<Especificacao> read(int produtoId) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Especificacao> receita = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM especificacao WHERE produto_id = ?");
            stmt.setInt(1, produtoId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Especificacao e = new Especificacao();
                e.setId(rs.getInt("id"));
                e.setProdutoId(rs.getInt("produto_id"));
                e.setInsumoId(rs.getInt("insumo_id"));
                e.setQuantidade(rs.getDouble("quantidade"));
                receita.add(e);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Especificacao_DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return receita;

    }

    public double calcularCusto(int produtoId) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        double custo = 0;

        try {
            stmt = con.prepareStatement(
                    "SELECT SUM(e.quantidade * i.preco) AS custo "
                    + "FROM especificacao e "
                    + "JOIN insumo i ON e.insumo_id = i.id "
                    + "WHERE e.produto_id = ?"
            );
            stmt.setInt(1, produtoId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                custo = rs.getDouble("custo");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Especificacao_DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return custo;

    }

    public double calcularPrecoSugerido(int produtoId, double margem) {
        double custo = calcularCusto(produtoId);
        return custo / (1 - (margem / 100));
    }

    public void update(Especificacao e) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE especificacao SET quantidade = ? WHERE id = ?");
            stmt.setDouble(1, e.getQuantidade());
            stmt.setInt(2, e.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Receita Atualizada");
        } catch (SQLException ex) {
            Logger.getLogger(Especificacao_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Atualizar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

    public void delete(Especificacao e) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM especificacao WHERE id = ?");
            stmt.setInt(1, e.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Insumo Removido da Receita");
        } catch (SQLException ex) {
            Logger.getLogger(Especificacao_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Excluir" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

}
