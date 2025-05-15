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
import model.Informante;
import util.Conexao;

public class InformanteDAO {

    // Nomes das colunas baseados na estrutura da tabela
    private static final String COL_ID = "id_informante";
    private static final String COL_CIDADAO = "id_cidadao";
    private static final String COL_QUEIXA = "id_queixa";
    private static final String COL_RELATO = "relato";
    private static final String COL_CONFIABILIDADE = "confiabilidade";
    private static final String COL_ANONIMATO = "anonimato";
    private static final String COL_DATA = "data_registro";

    public InformanteDAO() {
        // Construtor padrão
    }

    // CREATE
    public boolean create(Informante informante) throws SQLException {
        String sql = String.format(
            "INSERT INTO informantes (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
            COL_CIDADAO, COL_QUEIXA, COL_RELATO, COL_CONFIABILIDADE, COL_ANONIMATO, COL_DATA
        );

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (!existeCidadao(conn, informante.getIdCidadao())) {
                throw new SQLException("O cidadão informado não existe");
            }

            if (!existeQueixa(conn, informante.getIdQueixa())) {
                throw new SQLException("A queixa informada não existe");
            }

            stmt.setInt(1, informante.getIdCidadao());
            stmt.setInt(2, informante.getIdQueixa());
            stmt.setString(3, informante.getRelato());
            stmt.setString(4, informante.getConfiabilidade());
            stmt.setBoolean(5, informante.isAnonimato());
            stmt.setTimestamp(6, informante.getDataRegistro());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        informante.setIdInformante(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    // READ
    public Informante read(int id) throws SQLException {
        String sql = String.format("SELECT * FROM informantes WHERE %s = ?", COL_ID);
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractInformanteFromResultSet(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public boolean update(Informante informante) throws SQLException {
        String sql = String.format(
            "UPDATE informantes SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            COL_CIDADAO, COL_QUEIXA, COL_RELATO, COL_CONFIABILIDADE, COL_ANONIMATO, COL_DATA, COL_ID
        );

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, informante.getIdCidadao());
            stmt.setInt(2, informante.getIdQueixa());
            stmt.setString(3, informante.getRelato());
            stmt.setString(4, informante.getConfiabilidade());
            stmt.setBoolean(5, informante.isAnonimato());
            stmt.setTimestamp(6, informante.getDataRegistro());
            stmt.setInt(7, informante.getIdInformante());

            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean delete(int id) throws SQLException {
        String sql = String.format("DELETE FROM informantes WHERE %s = ?", COL_ID);

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // LIST ALL
    public List<Informante> listAll() throws SQLException {
        List<Informante> informantes = new ArrayList<>();
        String sql = String.format("SELECT * FROM informantes ORDER BY %s DESC", COL_DATA);

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                informantes.add(extractInformanteFromResultSet(rs));
            }
        }
        return informantes;
    }

    // LIST BY QUEIXA
    public List<Informante> findByQueixa(int idQueixa) throws SQLException {
        List<Informante> informantes = new ArrayList<>();
        String sql = String.format("SELECT * FROM informantes WHERE %s = ? ORDER BY %s DESC", 
                                 COL_QUEIXA, COL_DATA);

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idQueixa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    informantes.add(extractInformanteFromResultSet(rs));
                }
            }
        }
        return informantes;
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

    // Método auxiliar para extrair Informante do ResultSet
    private Informante extractInformanteFromResultSet(ResultSet rs) throws SQLException {
        Informante informante = new Informante();
        informante.setIdInformante(rs.getInt(COL_ID));
        informante.setIdCidadao(rs.getInt(COL_CIDADAO));
        informante.setIdQueixa(rs.getInt(COL_QUEIXA));
        informante.setRelato(rs.getString(COL_RELATO));
        informante.setConfiabilidade(rs.getString(COL_CONFIABILIDADE));
        informante.setAnonimato(rs.getBoolean(COL_ANONIMATO));
        informante.setDataRegistro(rs.getTimestamp(COL_DATA));
        return informante;
    }

    // VALIDATION METHODS
    private boolean existeCidadao(Connection conn, int idCidadao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cidadaos WHERE id_cidadao = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCidadao);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean existeQueixa(Connection conn, int idQueixa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM queixas WHERE id_queixa = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idQueixa);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
