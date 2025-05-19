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
import util.Conexao;

public class ClassificacaoCidadaoDAO {
    private static final String TABLE_NAME = "classificacoes_cidadao";
    private static final String CIDADAOS_TABLE = "cidadaos";

    public ClassificacaoCidadaoDAO() {
        // Construtor padrão
    }

    // CREATE - Apenas inserção de novas classificações (histórico)
    public boolean create(ClassificacaoCidadao classificacao) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (id_cidadao, classificacao, data_classificacao, observacoes) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Valida se o cidadão existe
            if (!existeCidadao(conn, classificacao.getIdCidadao())) {
                throw new SQLException("O cidadão informado não existe");
            }

            // Configura os parâmetros
            stmt.setInt(1, classificacao.getIdCidadao());
            stmt.setString(2, classificacao.getClassificacao());
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // Data atual
            stmt.setString(4, classificacao.getObservacoes());

            // Executa a inserção
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Obtém o ID gerado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        classificacao.setIdClassificacao(rs.getInt(1));
                    }
                }
                // Atualiza a classificação ATUAL na tabela de cidadãos
                atualizarClassificacaoAtual(conn, classificacao.getIdCidadao(), classificacao.getClassificacao());
                return true;
            }
            return false;
        }
    }

    // READ - Consulta de registros históricos
    public ClassificacaoCidadao read(int id) throws SQLException {
        String sql = "SELECT cc.*, c.nome as nomeCidadao FROM " + TABLE_NAME + " cc " +
                     "JOIN " + CIDADAOS_TABLE + " c ON cc.id_cidadao = c.id_cidadao " +
                     "WHERE cc.id_classificacao = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarClassificacaoFromResultSet(rs);
                }
            }
        }
        return null;
    }

    // REMOVI os métodos UPDATE e DELETE pois não fazem sentido para um histórico

    // LIST ALL - Listagem do histórico completo
    public List<ClassificacaoCidadao> listAll() throws SQLException {
        List<ClassificacaoCidadao> classificacoes = new ArrayList<>();
        String sql = "SELECT cc.*, c.nome as nomeCidadao FROM " + TABLE_NAME + " cc " +
                     "JOIN " + CIDADAOS_TABLE + " c ON cc.id_cidadao = c.id_cidadao " +
                     "ORDER BY cc.data_classificacao DESC";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                classificacoes.add(criarClassificacaoFromResultSet(rs));
            }
        }
        return classificacoes;
    }

    // LIST BY CIDADAO - Histórico de classificações de um cidadão específico
    public List<ClassificacaoCidadao> findByCidadao(int idCidadao) throws SQLException {
        List<ClassificacaoCidadao> classificacoes = new ArrayList<>();
        String sql = "SELECT cc.*, c.nome as nomeCidadao FROM " + TABLE_NAME + " cc " +
                     "JOIN " + CIDADAOS_TABLE + " c ON cc.id_cidadao = c.id_cidadao " +
                     "WHERE cc.id_cidadao = ? ORDER BY cc.data_classificacao DESC";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCidadao);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                classificacoes.add(criarClassificacaoFromResultSet(rs));
            }
        }
        return classificacoes;
    }

    // DROPDOWN METHODS - Para seleção de cidadãos
    public List<Map<String, String>> listarCidadaosParaDropdown() throws SQLException {
        List<Map<String, String>> cidadaos = new ArrayList<>();
        String sql = "SELECT id_cidadao, nome FROM " + CIDADAOS_TABLE + " ORDER BY nome";
        
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
        String sql = "SELECT COUNT(*) FROM " + CIDADAOS_TABLE + " WHERE id_cidadao = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCidadao);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    
    // Atualiza apenas a classificação ATUAL na tabela de cidadãos
    private void atualizarClassificacaoAtual(Connection conn, int idCidadao, String classificacao) throws SQLException {
        String sql = "UPDATE " + CIDADAOS_TABLE + " SET classificacao = ? WHERE id_cidadao = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, classificacao);
            stmt.setInt(2, idCidadao);
            stmt.executeUpdate();
        }
    }

    // Obtém o nome do cidadão
    public String getNomeCidadao(int idCidadao) throws SQLException {
        String sql = "SELECT nome FROM " + CIDADAOS_TABLE + " WHERE id_cidadao = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCidadao);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("nome") : "Cidadão Desconhecido";
        }
    }

    // Método auxiliar para criar objeto ClassificacaoCidadao
    private ClassificacaoCidadao criarClassificacaoFromResultSet(ResultSet rs) throws SQLException {
        ClassificacaoCidadao cc = new ClassificacaoCidadao();
        cc.setIdClassificacao(rs.getInt("id_classificacao"));
        cc.setIdCidadao(rs.getInt("id_cidadao"));
        cc.setClassificacao(rs.getString("classificacao"));
        cc.setObservacoes(rs.getString("observacoes"));
        cc.setDataClassificacao(rs.getTimestamp("data_classificacao"));
        cc.setNomeCidadao(rs.getString("nomeCidadao"));
        return cc;
    }
}
