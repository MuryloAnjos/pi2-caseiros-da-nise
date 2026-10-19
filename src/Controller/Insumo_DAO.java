package Controller;

import Connection.ConnectionFactory;
import Model.Insumo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Insumo_DAO {

    public void create(Insumo i) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            stmt = con.prepareStatement("SELECT id FROM insumo WHERE nome = ?");
            stmt.setString(1, i.getNome());
            rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Já existe um insumo cadastrado com este nome.");
                return;
            }
            rs.close();
            stmt.close();

            stmt = con.prepareStatement("INSERT INTO insumo (nome, unidade, preco, qtd_estoque, qtd_minima) VALUES(?, ?, ?, ?, ?)");
            stmt.setString(1, i.getNome());
            stmt.setString(2, i.getUnidade());
            stmt.setDouble(3, i.getPreco());
            stmt.setDouble(4, i.getQtdEstoque());
            stmt.setDouble(5, i.getQtdMinima());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Insumo Cadastrado");

        } catch (SQLException ex) {
            Logger.getLogger(Insumo_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Salvar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

    }

    public List<Insumo> read() {
        return listar("SELECT * FROM insumo ORDER BY nome");
    }

    public List<Insumo> readAbaixoDoMinimo() {
        return listar("SELECT * FROM insumo WHERE qtd_estoque < qtd_minima ORDER BY nome");
    }

    private List<Insumo> listar(String sql) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Insumo> insumos = new ArrayList<>();

        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Insumo insumo = new Insumo();
                insumo.setId(rs.getInt("id"));
                insumo.setNome(rs.getString("nome"));
                insumo.setUnidade(rs.getString("unidade"));
                insumo.setPreco(rs.getDouble("preco"));
                insumo.setQtdEstoque(rs.getDouble("qtd_estoque"));
                insumo.setQtdMinima(rs.getDouble("qtd_minima"));
                insumos.add(insumo);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Insumo_DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return insumos;

    }

    public double calcularPrecoUnitario(double qtdComprada, double fator, double precoPago) {
        return precoPago / (qtdComprada * fator);
    }

    public void update(Insumo i) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE insumo SET nome = ?, unidade = ?, preco = ?, qtd_estoque = ?, qtd_minima = ? WHERE id = ?");
            stmt.setString(1, i.getNome());
            stmt.setString(2, i.getUnidade());
            stmt.setDouble(3, i.getPreco());
            stmt.setDouble(4, i.getQtdEstoque());
            stmt.setDouble(5, i.getQtdMinima());
            stmt.setInt(6, i.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Insumo Atualizado");
        } catch (SQLException ex) {
            Logger.getLogger(Insumo_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Atualizar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

    public void delete(Insumo i) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM insumo WHERE id = ?");
            stmt.setInt(1, i.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Insumo Deletado");
        } catch (SQLException ex) {
            Logger.getLogger(Insumo_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Não é possível excluir: o insumo está em uma receita.");
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

}
