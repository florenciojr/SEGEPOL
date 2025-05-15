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
import model.Detencao;
import model.Conexao;

public class DetencaoDAO {

    // Nomes das colunas baseados na estrutura da tabela
    private static final String COL_ID = "id_detencao";
    private static final String COL_CIDADAO = "id_cidadao";
    private static final String COL_MOTIVO = "motivo";
    private static final String COL_LOCAL = "local_detencao";
    private static final String COL_STATUS = "status";
    private static final String COL_USUARIO = "id_usuario_responsavel";
    private static final String COL_DATA = "data_detencao";

    public DetencaoDAO() {
        // Construtor padrão
    }

    // CREATE
    public boolean create(Detencao detencao) throws SQLException {
        String sql = String.format(
            "INSERT INTO detencoes (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
            COL_CIDADAO, COL_MOTIVO, COL_LOCAL, COL_STATUS, COL_USUARIO, COL_DATA
        );

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (!existeCidadao(conn, detencao.getIdCidadao())) {
                throw new SQLException("O cidadão informado não existe");
            }

            if (detencao.getIdUsuarioResponsavel() > 0 && !existeUsuario(conn, detencao.getIdUsuarioResponsavel())) {
                throw new SQLException("O usuário responsável informado não existe");
            }

            stmt.setInt(1, detencao.getIdCidadao());
            stmt.setString(2, detencao.getMotivo());
            stmt.setString(3, detencao.getLocalDetencao());
            stmt.setString(4, detencao.getStatus());
            stmt.setInt(5, detencao.getIdUsuarioResponsavel());
            stmt.setTimestamp(6, detencao.getDataDetencao());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        detencao.setIdDetencao(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    // READ
    public Detencao read(int id) throws SQLException {
        String sql = String.format("SELECT * FROM detencoes WHERE %s = ?", COL_ID);
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractDetencaoFromResultSet(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public boolean update(Detencao detencao) throws SQLException {
        String sql = String.format(
            "UPDATE detencoes SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            COL_CIDADAO, COL_MOTIVO, COL_LOCAL, COL_STATUS, COL_USUARIO, COL_DATA, COL_ID
        );

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, detencao.getIdCidadao());
            stmt.setString(2, detencao.getMotivo());
            stmt.setString(3, detencao.getLocalDetencao());
            stmt.setString(4, detencao.getStatus());
            stmt.setInt(5, detencao.getIdUsuarioResponsavel());
            stmt.setTimestamp(6, detencao.getDataDetencao());
            stmt.setInt(7, detencao.getIdDetencao());

            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean delete(int id) throws SQLException {
        String sql = String.format("DELETE FROM detencoes WHERE %s = ?", COL_ID);

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // LIST ALL
    public List<Detencao> listAll() throws SQLException {
        List<Detencao> detencoes = new ArrayList<>();
        String sql = String.format("SELECT * FROM detencoes ORDER BY %s DESC", COL_DATA);

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                detencoes.add(extractDetencaoFromResultSet(rs));
            }
        }
        return detencoes;
    }

    // LIST BY CIDADAO
    public List<Detencao> findByCidadao(int idCidadao) throws SQLException {
        List<Detencao> detencoes = new ArrayList<>();
        String sql = String.format("SELECT * FROM detencoes WHERE %s = ? ORDER BY %s DESC", 
                                 COL_CIDADAO, COL_DATA);

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCidadao);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    detencoes.add(extractDetencaoFromResultSet(rs));
                }
            }
        }
        return detencoes;
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

    public List<Map<String, String>> listarUsuariosParaDropdown() throws SQLException {
        List<Map<String, String>> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, nome FROM usuarios ORDER BY nome";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, String> usuario = new HashMap<>();
                usuario.put("key", String.valueOf(rs.getInt("id_usuario")));
                usuario.put("value", rs.getString("nome"));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    // Método auxiliar para extrair Detencao do ResultSet
    private Detencao extractDetencaoFromResultSet(ResultSet rs) throws SQLException {
        Detencao detencao = new Detencao();
        detencao.setIdDetencao(rs.getInt(COL_ID));
        detencao.setIdCidadao(rs.getInt(COL_CIDADAO));
        detencao.setMotivo(rs.getString(COL_MOTIVO));
        detencao.setLocalDetencao(rs.getString(COL_LOCAL));
        
        // Tratamento especial para status (ENUM)
        String status = rs.getString(COL_STATUS);
        if (status == null) {
            status = "Detido"; // Valor padrão conforme sua tabela
        }
        detencao.setStatus(status);
        
        detencao.setIdUsuarioResponsavel(rs.getInt(COL_USUARIO));
        detencao.setDataDetencao(rs.getTimestamp(COL_DATA));
        return detencao;
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

    private boolean existeUsuario(Connection conn, int idUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
