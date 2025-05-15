/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author JR5
 */



import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import model.Queixa;
import model.TiposQueixa;
import util.Conexao;

public class QueixaDAO {
    private static final String[] STATUS_PERMITIDOS = {
        "Registrada", "Em Investigação", "Atribuída", "Arquivada", "Resolvida", "Cancelada"
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
            
            // Correção definitiva para problemas de encoding
            if (nome != null) {
                try {
                    // Verifica se a string está em ISO-8859-1 e converte para UTF-8
                    if (!Charset.forName("UTF-8").newEncoder().canEncode(nome)) {
                        nome = new String(nome.getBytes("ISO-8859-1"), "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    System.err.println("Erro ao converter nome do cidadão ID " + id);
                }
            }
            
            cidadaos.put(id, nome != null ? nome : "Nome não disponível");
        }
    }
    return cidadaos;
}

    // Método para obter tipos de queixa para dropdown
    public List<TiposQueixa> listarTiposQueixa() {
        List<TiposQueixa> tipos = new ArrayList<>();
        String sql = "SELECT id_tipo, nome_tipo, gravidade FROM tipos_queixa ORDER BY nome_tipo";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                TiposQueixa tipo = new TiposQueixa();
                tipo.setIdTipo(rs.getInt("id_tipo"));
                tipo.setNomeTipo(rs.getString("nome_tipo"));
                tipo.setGravidade(rs.getString("gravidade"));
                
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar tipos de queixa: " + e.getMessage());
        }
        return tipos;
    }


    // Método simplificado para inserção via web
    public boolean inserirQueixaWeb(Queixa queixa, int idUsuarioLogado) {
        // Validações básicas
        if (queixa.getTitulo() == null || queixa.getTitulo().trim().isEmpty()) {
            return false;
        }
        
        // Define valores padrão
        queixa.setStatus("Registrada");
        queixa.setIdUsuario(idUsuarioLogado);
        queixa.setDataRegistro(LocalDate.now());
        
        return inserirQueixa(queixa);
    }


// No método listarQueixasPaginado()
public List<Queixa> listarQueixasPaginado(int pagina, int itensPorPagina) {
    List<Queixa> queixas = new ArrayList<>();
    String sql = "SELECT q.*, c.nome AS nome_cidadao, t.nome_tipo AS nome_tipo_queixa, u.nome AS nome_usuario " +
                 "FROM queixas q " +
                 "LEFT JOIN cidadaos c ON q.id_cidadao = c.id_cidadao " +
                 "LEFT JOIN tipos_queixa t ON q.id_tipo = t.id_tipo " +
                 "LEFT JOIN usuarios u ON q.id_usuario = u.id_usuario " +
                 "ORDER BY q.data_registro DESC LIMIT ?, ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, (pagina - 1) * itensPorPagina);
        stmt.setInt(2, itensPorPagina);
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            queixas.add(mapearQueixa(rs));
        }
    } catch (SQLException e) {
        System.err.println("Erro ao listar queixas paginadas: " + e.getMessage());
    }
    return queixas;
}

private Queixa mapearQueixa(ResultSet rs) throws SQLException {
    Queixa queixa = new Queixa();
    queixa.setIdQueixa(rs.getInt("id_queixa"));
    queixa.setTitulo(rs.getString("titulo"));
    queixa.setDescricao(rs.getString("descricao"));
    
    // Conversão correta para LocalDate
    Timestamp timestampIncidente = rs.getTimestamp("data_incidente");
    if (timestampIncidente != null) {
        queixa.setDataIncidente(timestampIncidente.toLocalDateTime().toLocalDate());
    }
    
    Timestamp timestampRegistro = rs.getTimestamp("data_registro");
    if (timestampRegistro != null) {
        queixa.setDataRegistro(timestampRegistro.toLocalDateTime().toLocalDate());
    }
    
    queixa.setLocalIncidente(rs.getString("local_incidente"));
    queixa.setCoordenadas(rs.getString("coordenadas"));
    queixa.setStatus(rs.getString("status"));
    queixa.setIdCidadao(rs.getInt("id_cidadao"));
    queixa.setIdTipo(rs.getInt("id_tipo"));
    queixa.setIdUsuario(rs.getInt("id_usuario"));
    
    try {
        queixa.setNomeCidadao(rs.getString("nome_cidadao"));
        queixa.setNomeTipoQueixa(rs.getString("nome_tipo"));
        queixa.setNomeUsuario(rs.getString("nome_usuario"));
    } catch (SQLException e) {
        // Campos podem não estar presentes em todas as consultas
    }
    
    return queixa;
}

    // Método para verificar se um registro existe
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

    // Método para validar o status
    private boolean isStatusValido(String status) {
        if (status == null) return false;
        for (String s : STATUS_PERMITIDOS) {
            if (s.equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }

    // Inserir queixa com validações
    public boolean inserirQueixa(Queixa queixa) {
        // Validações
        if (queixa.getTitulo() == null || queixa.getTitulo().trim().isEmpty()) {
            System.err.println("Erro: Título da queixa é obrigatório");
            return false;
        }
        
        if (!isStatusValido(queixa.getStatus())) {
            System.err.println("Erro: Status inválido");
            return false;
        }
        
        if (!existeRegistro("cidadaos", "id_cidadao", queixa.getIdCidadao())) {
            System.err.println("Erro: Cidadão não encontrado");
            return false;
        }
        
        if (!existeRegistro("tipos_queixa", "id_tipo", queixa.getIdTipo())) {
            System.err.println("Erro: Tipo de queixa não encontrado");
            return false;
        }
        
        if (!existeRegistro("usuarios", "id_usuario", queixa.getIdUsuario())) {
            System.err.println("Erro: Usuário não encontrado");
            return false;
        }

        String sql = "INSERT INTO queixas (titulo, descricao, data_incidente, local_incidente, coordenadas, status, id_cidadao, id_tipo, id_usuario) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, queixa.getTitulo());
            stmt.setString(2, queixa.getDescricao());
            
            if (queixa.getDataIncidente() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(queixa.getDataIncidente().atStartOfDay()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }
            
            stmt.setString(4, queixa.getLocalIncidente());
            stmt.setString(5, queixa.getCoordenadas());
            stmt.setString(6, queixa.getStatus());
            stmt.setInt(7, queixa.getIdCidadao());
            stmt.setInt(8, queixa.getIdTipo());
            stmt.setInt(9, queixa.getIdUsuario());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                System.err.println("Erro: Nenhuma linha afetada");
                return false;
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    queixa.setIdQueixa(rs.getInt(1));
                    System.out.println("Queixa registrada com ID: " + queixa.getIdQueixa());
                    return true;
                }
            }
            System.err.println("Erro: Não foi possível obter o ID gerado");
            return false;
        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir queixa: " + e.getMessage());
            return false;
        }
    }

    // Listar todas as queixas
public List<Queixa> listarQueixas() throws SQLException {
    List<Queixa> queixas = new ArrayList<>();
    String sql = "SELECT q.*, c.nome AS nome_cidadao, t.nome_tipo AS nome_tipo_queixa, u.nome AS nome_usuario " +
                 "FROM queixas q " +
                 "LEFT JOIN cidadaos c ON q.id_cidadao = c.id_cidadao " +
                 "LEFT JOIN tipos_queixa t ON q.id_tipo = t.id_tipo " +
                 "LEFT JOIN usuarios u ON q.id_usuario = u.id_usuario " +
                 "ORDER BY q.data_registro DESC";

    try (Connection conn = Conexao.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            queixas.add(this.mapearQueixa(rs)); // Chamada explícita
        }
    }
    return queixas;
}



    // Buscar queixa por ID
public Queixa buscarQueixaPorId(int id) throws SQLException {
    String sql = "SELECT q.*, c.nome AS nome_cidadao, t.nome_tipo AS nome_tipo_queixa, u.nome AS nome_usuario " +
                 "FROM queixas q " +
                 "LEFT JOIN cidadaos c ON q.id_cidadao = c.id_cidadao " +
                 "LEFT JOIN tipos_queixa t ON q.id_tipo = t.id_tipo " +
                 "LEFT JOIN usuarios u ON q.id_usuario = u.id_usuario " +
                 "WHERE q.id_queixa = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return this.mapearQueixa(rs); // Chamada explícita
        }
    }
    return null;
}

    // Atualizar queixa
    public boolean atualizarQueixa(Queixa queixa) throws SQLException {
        if (buscarQueixaPorId(queixa.getIdQueixa()) == null) {
            System.err.println("Erro: Queixa não encontrada");
            return false;
        }
        
        if (!isStatusValido(queixa.getStatus())) {
            System.err.println("Erro: Status inválido");
            return false;
        }

        String sql = "UPDATE queixas SET titulo = ?, descricao = ?, data_incidente = ?, " +
                     "local_incidente = ?, coordenadas = ?, status = ?, id_cidadao = ?, " +
                     "id_tipo = ?, id_usuario = ? WHERE id_queixa = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, queixa.getTitulo());
            stmt.setString(2, queixa.getDescricao());
            
            if (queixa.getDataIncidente() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(queixa.getDataIncidente().atStartOfDay()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }
            
            stmt.setString(4, queixa.getLocalIncidente());
            stmt.setString(5, queixa.getCoordenadas());
            stmt.setString(6, queixa.getStatus());
            stmt.setInt(7, queixa.getIdCidadao());
            stmt.setInt(8, queixa.getIdTipo());
            stmt.setInt(9, queixa.getIdUsuario());
            stmt.setInt(10, queixa.getIdQueixa());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar queixa: " + e.getMessage());
            return false;
        }
    }

    // Deletar queixa
    public boolean deletarQueixa(int id) {
        String sql = "DELETE FROM queixas WHERE id_queixa = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar queixa: " + e.getMessage());
            return false;
        }
    }

    // Métodos utilitários para o menu (opcional - só necessário se for usar em aplicação console)
    private static LocalDate obterDataValida(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String input = scanner.nextLine().trim();
            
            if (input.matches("\\d{8}")) {
                input = input.substring(0, 4) + "-" + input.substring(4, 6) + "-" + input.substring(6, 8);
            }
            
            try {
                return LocalDate.parse(input);
            } catch (Exception e) {
                System.err.println("Formato inválido. Use YYYY-MM-DD ou YYYYMMDD");
            }
        }
    }

// Método para obter os status permitidos
    public String[] getStatusPermitidos() {
        return STATUS_PERMITIDOS.clone();
    }

  public Map<Integer, String> listarUsuariosRecentes() throws SQLException {
    Map<Integer, String> usuarios = new LinkedHashMap<>();
    String sql = "SELECT id_usuario, nome FROM usuarios ORDER BY id_usuario DESC LIMIT 20";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            int id = rs.getInt("id_usuario");
            String nome = rs.getString("nome");
            
            // Tratamento para nomes nulos ou vazios
            if (nome == null || nome.trim().isEmpty()) {
                nome = "Usuário " + id;
            }
            
            usuarios.put(id, nome);
        }
    } catch (SQLException e) {
        System.err.println("Erro ao listar usuários recentes: " + e.getMessage());
        throw e; // Re-lança a exceção para ser tratada no servlet
    }
    
    return usuarios;
}
  
 public Queixa buscarQueixaComRelacionamentos(int id) throws SQLException {
    String sql = "SELECT q.*, c.nome AS nome_cidadao, t.nome_tipo, u.nome AS nome_usuario " +
                 "FROM queixas q " +
                 "LEFT JOIN cidadaos c ON q.id_cidadao = c.id_cidadao " +
                 "LEFT JOIN tipos_queixa t ON q.id_tipo = t.id_tipo " +
                 "LEFT JOIN usuarios u ON q.id_usuario = u.id_usuario " +
                 "WHERE q.id_queixa = ?";

    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return mapearQueixa(rs);
        }
    }
    return null;
}
}
