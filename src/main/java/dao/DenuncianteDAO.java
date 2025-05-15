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
import model.Denunciante;
import model.Conexao;

public class DenuncianteDAO {

    public DenuncianteDAO() {
        // Construtor padrão sem parâmetros
    }

    // CREATE
  public boolean create(Denunciante denunciante) throws SQLException {
    String sql = "INSERT INTO denunciantes (id_cidadao, id_queixa, modo_denuncia, data_denuncia) VALUES (?, ?, ?, ?)";

    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        if (!existeQueixa(conn, denunciante.getIdQueixa())) {
            throw new SQLException("A queixa informada não existe");
        }

        if (!existeCidadao(conn, denunciante.getIdCidadao())) {
            throw new SQLException("O cidadão informado não existe");
        }

        stmt.setInt(1, denunciante.getIdCidadao());  // Corrigido aqui
        stmt.setInt(2, denunciante.getIdQueixa());
        stmt.setString(3, denunciante.getModoDenuncia());
        stmt.setTimestamp(4, denunciante.getDataDenuncia());

        int affectedRows = stmt.executeUpdate();
        
        if (affectedRows > 0) {
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    denunciante.setIdDenunciante(rs.getInt(1));
                }
            }
            return true;
        }
        return false;
    }
}

    // READ
    public Denunciante read(int id) throws SQLException {
        String sql = "SELECT * FROM denunciantes WHERE id_denunciante = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Denunciante(
                        rs.getInt("id_denunciante"),
                        rs.getInt("id_cidadao"),
                        rs.getInt("id_queixa"),
                        rs.getString("modo_denuncia"),
                        rs.getTimestamp("data_denuncia")
                    );
                }
            }
        }
        return null;
    }

    // UPDATE
    public boolean update(Denunciante denunciante) throws SQLException {
        String sql = "UPDATE denunciantes SET id_cidadao = ?, id_queixa = ?, modo_denuncia = ?, data_denuncia = ? WHERE id_denunciante = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, denunciante.getIdCidadao());
            stmt.setInt(2, denunciante.getIdQueixa());
            stmt.setString(3, denunciante.getModoDenuncia());
            stmt.setTimestamp(4, denunciante.getDataDenuncia());
            stmt.setInt(5, denunciante.getIdDenunciante());

            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM denunciantes WHERE id_denunciante = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // LIST ALL
    public List<Denunciante> listAll() throws SQLException {
        List<Denunciante> denunciantes = new ArrayList<>();
        String sql = "SELECT * FROM denunciantes";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                denunciantes.add(new Denunciante(
                    rs.getInt("id_denunciante"),
                    rs.getInt("id_cidadao"),
                    rs.getInt("id_queixa"),
                    rs.getString("modo_denuncia"),
                    rs.getTimestamp("data_denuncia")
                ));
            }
        }
        return denunciantes;
    }

    // LIST BY QUEIXA
    public List<Denunciante> findByQueixa(int idQueixa) throws SQLException {
        List<Denunciante> denunciantes = new ArrayList<>();
        String sql = "SELECT * FROM denunciantes WHERE id_queixa = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idQueixa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    denunciantes.add(new Denunciante(
                        rs.getInt("id_denunciante"),
                        rs.getInt("id_cidadao"),
                        rs.getInt("id_queixa"),
                        rs.getString("modo_denuncia"),
                        rs.getTimestamp("data_denuncia")
                    ));
                }
            }
        }
        return denunciantes;
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

    public List<Map<String, String>> listarQueixasParaDropdown() throws SQLException {
        List<Map<String, String>> queixas = new ArrayList<>();
        String sql = "SELECT id_queixa, descricao FROM queixas ORDER BY data_registro DESC";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, String> queixa = new HashMap<>();
                queixa.put("key", String.valueOf(rs.getInt("id_queixa")));
                queixa.put("value", rs.getString("descricao"));
                queixas.add(queixa);
            }
        }
        return queixas;
    }

    // VALIDATION METHODS
    private boolean existeQueixa(Connection conn, int idQueixa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM queixas WHERE id_queixa = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idQueixa);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean existeCidadao(Connection conn, int idCidadao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cidadaos WHERE id_cidadao = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCidadao);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
