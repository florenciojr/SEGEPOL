/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author JR5
 */



import model.Patrulha;
import util.Conexao;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import model.Usuario;

public class PatrulhaDAO {
    
    // Método para criar a tabela com todas as colunas necessárias
    public void criarTabela() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS patrulhas (" +
                     "id_patrulha INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                     "nome VARCHAR(50) NOT NULL, " +
                     "responsavel_id INTEGER NOT NULL, " +
                     "membros TEXT, " +  // Armazenará IDs dos membros separados por vírgula
                     "zona_atuacao VARCHAR(255), " +
                     "data DATE NOT NULL, " +
                     "hora_inicio TIME NOT NULL, " +
                     "hora_fim TIME, " +
                     "tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('Ronda', 'Operação', 'Especial', 'Preventiva')), " +
                     "observacoes TEXT, " +
                     "status VARCHAR(20) NOT NULL CHECK (status IN ('Planejada', 'Em Andamento', 'Concluída', 'Cancelada')), " +
                     "criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    // Método para inserir uma nova patrulha
    public int inserir(Patrulha patrulha) throws SQLException {
        String sql = "INSERT INTO patrulhas (nome, responsavel_id, membros, data, hora_inicio, " +
                     "hora_fim, tipo, zona_atuacao, observacoes, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, patrulha.getNome());
            stmt.setInt(2, patrulha.getResponsavelId());
            stmt.setString(3, patrulha.getMembrosAsString());
            stmt.setDate(4, Date.valueOf(patrulha.getData()));
            stmt.setTime(5, Time.valueOf(patrulha.getHoraInicio()));
            stmt.setTime(6, patrulha.getHoraFim() != null ? Time.valueOf(patrulha.getHoraFim()) : null);
            stmt.setString(7, patrulha.getTipo());
            stmt.setString(8, patrulha.getZonaAtuacao());  // Novo campo
            stmt.setString(9, patrulha.getObservacoes());
            stmt.setString(10, patrulha.getStatus());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    // Método para atualizar uma patrulha
public boolean atualizar(Patrulha patrulha) throws SQLException {
    String sql = "UPDATE patrulhas SET nome = ?, responsavel_id = ?, membros = ?, data = ?, " +
                 "hora_inicio = ?, hora_fim = ?, tipo = ?, zona_atuacao = ?, observacoes = ?, status = ? " +
                 "WHERE id_patrulha = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, patrulha.getNome());
        stmt.setInt(2, patrulha.getResponsavelId());
        stmt.setString(3, patrulha.getMembrosAsString());
        stmt.setDate(4, Date.valueOf(patrulha.getData()));
        stmt.setTime(5, Time.valueOf(patrulha.getHoraInicio()));
        stmt.setTime(6, patrulha.getHoraFim() != null ? Time.valueOf(patrulha.getHoraFim()) : null);
        stmt.setString(7, patrulha.getTipo());
        stmt.setString(8, patrulha.getZonaAtuacao());  // Parâmetro 8 - zona_atuacao
        stmt.setString(9, patrulha.getObservacoes());
        stmt.setString(10, patrulha.getStatus());
        stmt.setInt(11, patrulha.getIdPatrulha());  // Parâmetro 11 - id_patrulha
        
        return stmt.executeUpdate() > 0;
    }
}
    
    // Método para listar todas as patrulhas
    public List<Patrulha> listarTodas() throws SQLException {
        List<Patrulha> patrulhas = new ArrayList<>();
        String sql = "SELECT * FROM patrulhas ORDER BY data, hora_inicio";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                patrulhas.add(criarPatrulhaFromResultSet(rs));
            }
        }
        return patrulhas;
    }
    
    // Método para buscar patrulha por ID
    public Patrulha buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM patrulhas WHERE id_patrulha = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarPatrulhaFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    // Método para listar patrulhas por status
    public List<Patrulha> listarPorStatus(String status) throws SQLException {
        List<Patrulha> patrulhas = new ArrayList<>();
        String sql = "SELECT * FROM patrulhas WHERE status = ? ORDER BY data, hora_inicio";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patrulhas.add(criarPatrulhaFromResultSet(rs));
                }
            }
        }
        return patrulhas;
    }
    
    // Método para listar patrulhas por data
    public List<Patrulha> listarPorData(LocalDate data) throws SQLException {
        List<Patrulha> patrulhas = new ArrayList<>();
        String sql = "SELECT * FROM patrulhas WHERE data = ? ORDER BY hora_inicio";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(data));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patrulhas.add(criarPatrulhaFromResultSet(rs));
                }
            }
        }
        return patrulhas;
    }
    
    // Método para deletar uma patrulha
    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM patrulhas WHERE id_patrulha = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Método para iniciar uma patrulha (muda status para "Em Andamento")
    public boolean iniciar(int idPatrulha) throws SQLException {
        String sql = "UPDATE patrulhas SET status = 'Em Andamento' WHERE id_patrulha = ? AND status = 'Planejada'";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPatrulha);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Método para finalizar uma patrulha (muda status para "Concluída")
    public boolean finalizar(int idPatrulha, LocalTime horaFim, String observacoes) throws SQLException {
        String sql = "UPDATE patrulhas SET status = 'Concluída', hora_fim = ?, observacoes = ? " +
                     "WHERE id_patrulha = ? AND status = 'Em Andamento'";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTime(1, Time.valueOf(horaFim));
            stmt.setString(2, observacoes);
            stmt.setInt(3, idPatrulha);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Método para cancelar uma patrulha (muda status para "Cancelada")
    public boolean cancelar(int idPatrulha, String motivo) throws SQLException {
        String sql = "UPDATE patrulhas SET status = 'Cancelada', observacoes = ? " +
                     "WHERE id_patrulha = ? AND status IN ('Planejada', 'Em Andamento')";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, motivo);
            stmt.setInt(2, idPatrulha);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Método auxiliar para criar objeto Patrulha a partir de ResultSet
private Patrulha criarPatrulhaFromResultSet(ResultSet rs) throws SQLException {
    Patrulha patrulha = new Patrulha();
    patrulha.setIdPatrulha(rs.getInt("id_patrulha"));
    patrulha.setNome(rs.getString("nome"));
    patrulha.setResponsavelId(rs.getInt("responsavel_id"));
    
    // Converter string de membros para lista
    String membrosStr = rs.getString("membros");
    if (membrosStr != null && !membrosStr.isEmpty()) {
        patrulha.setMembrosFromString(membrosStr);
    }
    
    patrulha.setData(rs.getDate("data").toLocalDate());
    
    // Usar o novo método que não valida hora no passado
    patrulha.setHoraInicioFromDB(rs.getTime("hora_inicio").toLocalTime());
    
    Time horaFim = rs.getTime("hora_fim");
    if (horaFim != null) {
        patrulha.setHoraFim(horaFim.toLocalTime());
    }
    
    patrulha.setTipo(rs.getString("tipo"));
    patrulha.setZonaAtuacao(rs.getString("zona_atuacao")); 
    patrulha.setObservacoes(rs.getString("observacoes"));
    patrulha.setStatus(rs.getString("status"));
    
    Timestamp criadoEm = rs.getTimestamp("criado_em");
    if (criadoEm != null) {
        patrulha.setCriadoEm(criadoEm.toLocalDateTime());
    }
    
    Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
    if (atualizadoEm != null) {
        patrulha.setAtualizadoEm(atualizadoEm.toLocalDateTime());
    }
    
    return patrulha;
}
    
   public Map<Integer, String> buscarNomesUsuarios(Set<Integer> idsUsuarios) throws SQLException {
    if (idsUsuarios == null || idsUsuarios.isEmpty()) {
        return Collections.emptyMap();
    }
    
    // Cria uma string com os IDs separados por vírgulas
    String idsStr = idsUsuarios.stream()
                             .map(String::valueOf)
                             .collect(Collectors.joining(","));
    
    String sql = "SELECT id_usuario, nome FROM usuarios WHERE id_usuario IN (" + idsStr + ")";
    Map<Integer, String> nomesUsuarios = new HashMap<>();
    
    try (Connection conn = Conexao.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            nomesUsuarios.put(rs.getInt("id_usuario"), rs.getString("nome"));
        }
    }
    
    return nomesUsuarios;
}
   
public List<Usuario> listarUsuariosParaPatrulha() throws SQLException {
    String sql = "SELECT id_usuario, nome, cargo, perfil FROM usuarios WHERE status = 'Ativo' ORDER BY nome";
    List<Usuario> usuarios = new ArrayList<>();
    
    try (Connection conn = Conexao.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            Usuario usuario = new Usuario();
            usuario.setId_usuario(rs.getInt("id_usuario"));
            usuario.setNome(rs.getString("nome"));
            usuario.setCargo(rs.getString("cargo"));
            usuario.setPerfil(rs.getString("perfil"));
            usuarios.add(usuario);
        }
    }
    return usuarios;
}
       
       
       
        public Map<Integer, Usuario> buscarUsuariosCompletos(Set<Integer> idsUsuarios) throws SQLException {
        if (idsUsuarios == null || idsUsuarios.isEmpty()) {
            return Collections.emptyMap();
        }
        
        String idsStr = idsUsuarios.stream()
                                 .map(String::valueOf)
                                 .collect(Collectors.joining(","));
        
        String sql = "SELECT id_usuario, nome, cargo, perfil FROM usuarios WHERE id_usuario IN (" + idsStr + ")";
        Map<Integer, Usuario> usuarios = new HashMap<>();
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setCargo(rs.getString("cargo"));
                usuario.setPerfil(rs.getString("perfil"));
                usuarios.put(usuario.getId_usuario(), usuario);
            }
        }
        return usuarios;
    }
        
        
        
        public boolean existePatrulhaSimilar(String nome, LocalDate data, LocalTime horaInicio) throws SQLException {
    String sql = "SELECT COUNT(*) FROM patrulhas WHERE nome = ? AND data = ? AND hora_inicio = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
         
        stmt.setString(1, nome);
        stmt.setDate(2, Date.valueOf(data));
        stmt.setTime(3, Time.valueOf(horaInicio));
        
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
        
}

