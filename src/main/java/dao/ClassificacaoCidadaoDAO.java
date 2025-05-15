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
import java.util.Map;
import java.util.HashMap;
import model.ClassificacaoCidadao;
import model.Conexao;

public class ClassificacaoCidadaoDAO {

    public ClassificacaoCidadaoDAO() {
        // Construtor padrão sem parâmetros
    }

    // CREATE
    public boolean create(ClassificacaoCidadao classificacao) {
        String sql = "INSERT INTO classificacao_cidadao (id_cidadao, classificacao, data_classificacao, observacoes) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (!existeCidadao(conn, classificacao.getIdCidadao())) {
                System.out.println("Erro: O cidadão informado não existe.");
                return false;
            }

            stmt.setInt(1, classificacao.getIdCidadao());
            stmt.setString(2, classificacao.getClassificacao());
            stmt.setTimestamp(3, classificacao.getDataClassificacao());
            stmt.setString(4, classificacao.getObservacoes());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        classificacao.setIdClassificacao(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ
    public ClassificacaoCidadao read(int id) {
        String sql = "SELECT * FROM classificacao_cidadao WHERE id_classificacao = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ClassificacaoCidadao(
                    rs.getInt("id_classificacao"),
                    rs.getInt("id_cidadao"),
                    rs.getString("classificacao"),
                    rs.getTimestamp("data_classificacao"),
                    rs.getString("observacoes")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public boolean update(ClassificacaoCidadao classificacao) {
        String sql = "UPDATE classificacao_cidadao SET id_cidadao = ?, classificacao = ?, data_classificacao = ?, observacoes = ? WHERE id_classificacao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, classificacao.getIdCidadao());
            stmt.setString(2, classificacao.getClassificacao());
            stmt.setTimestamp(3, classificacao.getDataClassificacao());
            stmt.setString(4, classificacao.getObservacoes());
            stmt.setInt(5, classificacao.getIdClassificacao());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean delete(int id) {
        String sql = "DELETE FROM classificacao_cidadao WHERE id_classificacao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // LIST ALL
    public List<ClassificacaoCidadao> listAll() {
        List<ClassificacaoCidadao> classificacoes = new ArrayList<>();
        String sql = "SELECT * FROM classificacao_cidadao";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                classificacoes.add(new ClassificacaoCidadao(
                    rs.getInt("id_classificacao"),
                    rs.getInt("id_cidadao"),
                    rs.getString("classificacao"),
                    rs.getTimestamp("data_classificacao"),
                    rs.getString("observacoes")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classificacoes;
    }

    // LIST BY CIDADAO
    public List<ClassificacaoCidadao> findByCidadao(int idCidadao) {
        List<ClassificacaoCidadao> classificacoes = new ArrayList<>();
        String sql = "SELECT * FROM classificacao_cidadao WHERE id_cidadao = ? ORDER BY data_classificacao DESC";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCidadao);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                classificacoes.add(new ClassificacaoCidadao(
                    rs.getInt("id_classificacao"),
                    rs.getInt("id_cidadao"),
                    rs.getString("classificacao"),
                    rs.getTimestamp("data_classificacao"),
                    rs.getString("observacoes")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classificacoes;
    }

    // DROPDOWN METHODS
    public List<Map<String, String>> listarCidadaosParaDropdown() throws SQLException {
        List<Map<String, String>> cidadaos = new ArrayList<>();
        String sql = "SELECT id_cidadao, nome FROM cidadaos ORDER BY nome";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, String> cidadao = new HashMap<>();
                cidadao.put("key", String.valueOf(rs.getInt("id_cidadao")));
                cidadao.put("value", rs.getString("nome"));
                cidadaos.add(cidadao);
            }
        }
        return cidadaos;
    }

    // VALIDATION METHODS
    private boolean existeCidadao(Connection conn, int idCidadao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cidadaos WHERE id_cidadao = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCidadao);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
