package Controller;

import Connection.ConnectionFactory;
import Model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Produto_DAO {

    public void create(Produto p) {

        if (p.getPrecoVenda() <= 0) {
            JOptionPane.showMessageDialog(null, "O preço de venda deve ser maior que zero.");
            return;
        }

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("INSERT INTO produto (nome, categoria, tempo_preparo_min, margem, preco_venda) VALUES(?, ?, ?, ?, ?)");
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getCategoria());
            stmt.setInt(3, p.getTempoPreparoMin());
            stmt.setDouble(4, p.getMargem());
            stmt.setDouble(5, p.getPrecoVenda());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produto Adicionado");
        } catch (SQLException ex) {
            Logger.getLogger(Produto_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Salvar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

    public List<Produto> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Produto> produtos = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM produto ORDER BY nome");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setCategoria(rs.getString("categoria"));
                produto.setTempoPreparoMin(rs.getInt("tempo_preparo_min"));
                produto.setMargem(rs.getDouble("margem"));
                produto.setPrecoVenda(rs.getDouble("preco_venda"));
                produtos.add(produto);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Produto_DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return produtos;

    }

    public Produto consul(int id) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Produto produto = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM produto WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setCategoria(rs.getString("categoria"));
                produto.setTempoPreparoMin(rs.getInt("tempo_preparo_min"));
                produto.setMargem(rs.getDouble("margem"));
                produto.setPrecoVenda(rs.getDouble("preco_venda"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Produto_DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return produto;

    }

    public void update(Produto p) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE produto SET nome = ?, categoria = ?, tempo_preparo_min = ?, margem = ?, preco_venda = ? WHERE id = ?");
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getCategoria());
            stmt.setInt(3, p.getTempoPreparoMin());
            stmt.setDouble(4, p.getMargem());
            stmt.setDouble(5, p.getPrecoVenda());
            stmt.setInt(6, p.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produto Atualizado");
        } catch (SQLException ex) {
            Logger.getLogger(Produto_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao Atualizar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

    public void delete(Produto p) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM produto WHERE id = ?");
            stmt.setInt(1, p.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produto Deletado");
        } catch (SQLException ex) {
            Logger.getLogger(Produto_DAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Não é possível excluir: o produto tem receita ou já foi vendido.");
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

}
