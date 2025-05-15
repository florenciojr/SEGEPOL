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
import model.Testemunha;
import util.Conexao;

public class TestemunhaDAO {

    // Nomes das colunas baseados na estrutura da tabela
    private static final String COL_ID = "id_testemunha";
    private static final String COL_CIDADAO = "id_cidadao";
    private static final String COL_QUEIXA = "id_queixa";
    private static final String COL_TIPO = "tipo_testemunha";
    private static final String COL_DESCRICAO = "descricao";
    private static final String COL_DATA = "data_registro";

    public TestemunhaDAO() {
        // Construtor padrão
    }

    // CREATE
    public boolean create(Testemunha testemunha) throws SQLException {
        String sql = String.format(
            "INSERT INTO testemunhas (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
            COL_CIDADAO, COL_QUEIXA, COL_TIPO, COL_DESCRICAO, COL_DATA
        );

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (!existeCidadao(conn, testemunha.getIdCidadao())) {
                throw new SQLException("O cidadão informado não existe");
            }

            if (!existeQueixa(conn, testemunha.getIdQueixa())) {
                throw new SQLException("A queixa informada não existe");
            }

            stmt.setInt(1, testemunha.getIdCidadao());
            stmt.setInt(2, testemunha.getIdQueixa());
            stmt.setString(3, testemunha.getTipoTestemunha());
            stmt.setString(4, testemunha.getDescricao());
            stmt.setTimestamp(5, testemunha.getDataRegistro());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        testemunha.setIdTestemunha(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    // READ
    public Testemunha read(int id) throws SQLException {
        String sql = String.format("SELECT * FROM testemunhas WHERE %s = ?", COL_ID);
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractTestemunhaFromResultSet(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public boolean update(Testemunha testemunha) throws SQLException {
        String sql = String.format(
            "UPDATE testemunhas SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            COL_CIDADAO, COL_QUEIXA, COL_TIPO, COL_DESCRICAO, COL_DATA, COL_ID
        );

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, testemunha.getIdCidadao());
            stmt.setInt(2, testemunha.getIdQueixa());
            stmt.setString(3, testemunha.getTipoTestemunha());
            stmt.setString(4, testemunha.getDescricao());
            stmt.setTimestamp(5, testemunha.getDataRegistro());
            stmt.setInt(6, testemunha.getIdTestemunha());

            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean delete(int id) throws SQLException {
        String sql = String.format("DELETE FROM testemunhas WHERE %s = ?", COL_ID);

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // LIST ALL
    public List<Testemunha> listAll() throws SQLException {
        List<Testemunha> testemunhas = new ArrayList<>();
        String sql = String.format("SELECT * FROM testemunhas ORDER BY %s DESC", COL_DATA);

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                testemunhas.add(extractTestemunhaFromResultSet(rs));
            }
        }
        return testemunhas;
    }

    // LIST BY QUEIXA
    public List<Testemunha> findByQueixa(int idQueixa) throws SQLException {
        List<Testemunha> testemunhas = new ArrayList<>();
        String sql = String.format("SELECT * FROM testemunhas WHERE %s = ? ORDER BY %s DESC", 
                                 COL_QUEIXA, COL_DATA);

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idQueixa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    testemunhas.add(extractTestemunhaFromResultSet(rs));
                }
            }
        }
        return testemunhas;
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

    // Método auxiliar para extrair Testemunha do ResultSet
    private Testemunha extractTestemunhaFromResultSet(ResultSet rs) throws SQLException {
        Testemunha testemunha = new Testemunha();
        testemunha.setIdTestemunha(rs.getInt(COL_ID));
        testemunha.setIdCidadao(rs.getInt(COL_CIDADAO));
        testemunha.setIdQueixa(rs.getInt(COL_QUEIXA));
        testemunha.setTipoTestemunha(rs.getString(COL_TIPO));
        testemunha.setDescricao(rs.getString(COL_DESCRICAO));
        testemunha.setDataRegistro(rs.getTimestamp(COL_DATA));
        return testemunha;
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
