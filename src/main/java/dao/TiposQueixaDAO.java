/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author JR5
 */



import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.TiposQueixa;
import util.Conexao;

public class TiposQueixaDAO {

    // Inserir novo tipo de queixa
    public boolean inserir(TiposQueixa tipo) {
        String sql = "INSERT INTO tipos_queixa (nome_tipo, descricao, gravidade) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.getNomeTipo());
            stmt.setString(2, tipo.getDescricao());
            stmt.setString(3, tipo.getGravidade());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Atualizar tipo de queixa
    public boolean atualizar(TiposQueixa tipo) {
        String sql = "UPDATE tipos_queixa SET nome_tipo = ?, descricao = ?, gravidade = ? WHERE id_tipo = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
           
            stmt.setString(1, tipo.getNomeTipo());
            stmt.setString(2, tipo.getDescricao());
            stmt.setString(3, tipo.getGravidade());
            stmt.setInt(4, tipo.getIdTipo());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Deletar tipo de queixa
    public boolean deletar(int idTipo) {
        String sql = "DELETE FROM tipos_queixa WHERE id_tipo = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTipo);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Buscar por ID
    public TiposQueixa buscarPorId(int idTipo) {
        String sql = "SELECT * FROM tipos_queixa WHERE id_tipo = ?";
        TiposQueixa tipo = null;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, idTipo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tipo = new TiposQueixa();
                tipo.setIdTipo(rs.getInt("id_tipo"));
                tipo.setNomeTipo(rs.getString("nome_tipo"));
                tipo.setDescricao(rs.getString("descricao"));
                tipo.setGravidade(rs.getString("gravidade"));
                tipo.setDataCadastro(rs.getTimestamp("data_cadastro"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tipo;
    }

    // Listar todos os tipos de queixa
    public List<TiposQueixa> listarTodos() {
        List<TiposQueixa> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipos_queixa";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TiposQueixa tipo = new TiposQueixa();
                tipo.setIdTipo(rs.getInt("id_tipo"));
                tipo.setNomeTipo(rs.getString("nome_tipo"));
                tipo.setDescricao(rs.getString("descricao"));
                tipo.setGravidade(rs.getString("gravidade"));
                tipo.setDataCadastro(rs.getTimestamp("data_cadastro"));
                lista.add(tipo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Buscar por gravidade
public List<TiposQueixa> buscarPorGravidade(String gravidade) {
    List<TiposQueixa> lista = new ArrayList<>();
    String sql = "SELECT * FROM tipos_queixa WHERE gravidade = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, gravidade);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            TiposQueixa tipo = new TiposQueixa();
            tipo.setIdTipo(rs.getInt("id_tipo"));
            tipo.setNomeTipo(rs.getString("nome_tipo"));
            tipo.setDescricao(rs.getString("descricao"));
            tipo.setGravidade(rs.getString("gravidade"));
            tipo.setDataCadastro(rs.getTimestamp("data_cadastro"));
            lista.add(tipo);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}

}
