/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author JR5
 */


import model.Intimacao;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import util.Conexao;

public class IntimacaoDAO {
    private static final String[] STATUS_PERMITIDOS = {
        "Pendente", "Compareceu", "Não Compareceu", "Adiado"
    };
    
    public Map<Integer, String> listarCidadaosRecentes() throws SQLException {
        Map<Integer, String> cidadaos = new LinkedHashMap<>();
        String sql = "SELECT id_cidadao, nome FROM cidadaos ORDER BY data_registro DESC LIMIT 20";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("id_cidadao");
                String nome = rs.getString("nome");
                
                if (nome != null) {
                    try {
                        if (!java.nio.charset.StandardCharsets.UTF_8.newEncoder().canEncode(nome)) {
                            nome = new String(nome.getBytes("ISO-8859-1"), "UTF-8");
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao converter nome do cidadão ID " + id);
                    }
                }
                
                cidadaos.put(id, nome != null ? nome : "Nome não disponível");
            }
        }
        return cidadaos;
    }

    public Map<Integer, String> listarQueixasRecentes() throws SQLException {
        Map<Integer, String> queixas = new LinkedHashMap<>();
        String sql = "SELECT id_queixa, titulo FROM queixas ORDER BY data_registro DESC LIMIT 20";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("id_queixa");
                String titulo = rs.getString("titulo");
                queixas.put(id, titulo != null ? titulo : "Queixa #" + id);
            }
        }
        return queixas;
    }

    public boolean inserirIntimacao(Intimacao intimacao) {
        if (intimacao.getMotivo() == null || intimacao.getMotivo().trim().isEmpty()) {
            System.err.println("Erro: Motivo da intimação é obrigatório");
            return false;
        }
        
        if (!isStatusValido(intimacao.getStatus())) {
            System.err.println("Erro: Status inválido");
            return false;
        }
        
        if (!existeRegistro("cidadaos", "id_cidadao", intimacao.getIdCidadao())) {
            System.err.println("Erro: Cidadão não encontrado");
            return false;
        }
        
        if (!existeRegistro("queixas", "id_queixa", intimacao.getIdQueixa())) {
            System.err.println("Erro: Queixa não encontrada");
            return false;
        }
        
        if (!existeRegistro("usuarios", "id_usuario", intimacao.getIdUsuario())) {
            System.err.println("Erro: Usuário não encontrado");
            return false;
        }

        String sql = "INSERT INTO intimacoes (id_cidadao, id_usuario, id_queixa, motivo, " +
                     "data_emissao, data_comparecimento, local_comparecimento, status, observacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, intimacao.getIdCidadao());
            stmt.setInt(2, intimacao.getIdUsuario());
            stmt.setInt(3, intimacao.getIdQueixa());
            stmt.setString(4, intimacao.getMotivo());
            stmt.setDate(5, Date.valueOf(intimacao.getDataEmissao()));
            stmt.setDate(6, Date.valueOf(intimacao.getDataComparecimento()));
            stmt.setString(7, intimacao.getLocalComparecimento());
            stmt.setString(8, intimacao.getStatus());
            stmt.setString(9, intimacao.getObservacoes());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    intimacao.setIdIntimacao(rs.getInt(1));
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir intimação: " + e.getMessage());
            return false;
        }
    }

public List<Intimacao> listarIntimacoes() throws SQLException {
    String sql = "SELECT i.*, c.nome as nome_cidadao, q.titulo as titulo_queixa, u.nome as nome_usuario " +
                 "FROM intimacoes i " +
                 "LEFT JOIN cidadaos c ON i.id_cidadao = c.id_cidadao " +
                 "LEFT JOIN queixas q ON i.id_queixa = q.id_queixa " +
                 "LEFT JOIN usuarios u ON i.id_usuario = u.id_usuario " +
                 "ORDER BY i.data_comparecimento DESC";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        List<Intimacao> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(mapearIntimacao(rs));
        }
        return lista;
    }
}

    public Intimacao buscarIntimacaoPorId(int id) throws SQLException {
        String sql = "SELECT i.*, c.nome AS nome_cidadao, q.titulo AS titulo_queixa, u.nome AS nome_usuario " +
                     "FROM intimacoes i " +
                     "LEFT JOIN cidadaos c ON i.id_cidadao = c.id_cidadao " +
                     "LEFT JOIN queixas q ON i.id_queixa = q.id_queixa " +
                     "LEFT JOIN usuarios u ON i.id_usuario = u.id_usuario " +
                     "WHERE i.id_intimacao = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearIntimacao(rs);
            }
        }
        return null;
    }

    public boolean atualizarIntimacao(Intimacao intimacao) throws SQLException {
        if (buscarIntimacaoPorId(intimacao.getIdIntimacao()) == null) {
            System.err.println("Erro: Intimação não encontrada");
            return false;
        }
        
        if (!isStatusValido(intimacao.getStatus())) {
            System.err.println("Erro: Status inválido");
            return false;
        }

        String sql = "UPDATE intimacoes SET id_cidadao = ?, id_usuario = ?, id_queixa = ?, " +
                     "motivo = ?, data_emissao = ?, data_comparecimento = ?, " +
                     "local_comparecimento = ?, status = ?, observacoes = ? " +
                     "WHERE id_intimacao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, intimacao.getIdCidadao());
            stmt.setInt(2, intimacao.getIdUsuario());
            stmt.setInt(3, intimacao.getIdQueixa());
            stmt.setString(4, intimacao.getMotivo());
            stmt.setDate(5, Date.valueOf(intimacao.getDataEmissao()));
            stmt.setDate(6, Date.valueOf(intimacao.getDataComparecimento()));
            stmt.setString(7, intimacao.getLocalComparecimento());
            stmt.setString(8, intimacao.getStatus());
            stmt.setString(9, intimacao.getObservacoes());
            stmt.setInt(10, intimacao.getIdIntimacao());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar intimação: " + e.getMessage());
            return false;
        }
    }

    public boolean deletarIntimacao(int id) {
        String sql = "DELETE FROM intimacoes WHERE id_intimacao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar intimação: " + e.getMessage());
            return false;
        }
    }

private Intimacao mapearIntimacao(ResultSet rs) throws SQLException {
    Intimacao intimacao = new Intimacao();
    try {
        // Mapeamento dos campos básicos
        intimacao.setIdIntimacao(rs.getInt("id_intimacao"));
        intimacao.setIdCidadao(rs.getInt("id_cidadao"));
        intimacao.setIdUsuario(rs.getInt("id_usuario"));
        intimacao.setIdQueixa(rs.getInt("id_queixa"));
        intimacao.setMotivo(rs.getString("motivo"));
        
        // Tratamento robusto para datas
        Date dataEmissao = rs.getDate("data_emissao");
        intimacao.setDataEmissao(dataEmissao != null && !rs.wasNull() ? dataEmissao.toLocalDate() : null);
        
        Date dataComparecimento = rs.getDate("data_comparecimento");
        intimacao.setDataComparecimento(dataComparecimento != null && !rs.wasNull() ? dataComparecimento.toLocalDate() : null);
        
        // Tratamento para strings com encoding
        intimacao.setLocalComparecimento(rs.getString("local_comparecimento"));
        intimacao.setStatus(fixEncoding(rs.getString("status")));
        intimacao.setObservacoes(rs.getString("observacoes"));
        
        // Campos adicionais com tratamento de encoding
        try {
            intimacao.setNomeCidadao(fixEncoding(rs.getString("nome_cidadao")));
        } catch (SQLException e) {
            intimacao.setNomeCidadao(null);
        }
        
        try {
            intimacao.setTituloQueixa(fixEncoding(rs.getString("titulo_queixa")));
        } catch (SQLException e) {
            intimacao.setTituloQueixa(null);
        }
        
    } catch (SQLException e) {
        System.err.println("Erro ao mapear intimação: " + e.getMessage());
        throw e;
    }
    return intimacao;
}

// Método auxiliar para corrigir problemas de encoding
private String fixEncoding(String input) {
    if (input == null) return null;
    try {
        // Tenta detectar e corrigir encoding mal interpretado
        if (!java.nio.charset.StandardCharsets.UTF_8.newEncoder().canEncode(input)) {
            return new String(input.getBytes("ISO-8859-1"), "UTF-8");
        }
        return input;
    } catch (Exception e) {
        System.err.println("Erro ao corrigir encoding: " + e.getMessage());
        return input;
    }
}

    private boolean existeRegistro(String tabela, String campo, int id) {
        String sql = "SELECT 1 FROM " + tabela + " WHERE " + campo + " = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar existência em " + tabela + ": " + e.getMessage());
            return false;
        }
    }

    private boolean isStatusValido(String status) {
        if (status == null) return false;
        for (String s : STATUS_PERMITIDOS) {
            if (s.equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }

    public String[] getStatusPermitidos() {
        return STATUS_PERMITIDOS.clone();
    }
}
