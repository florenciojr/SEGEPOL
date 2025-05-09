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
import java.util.Arrays;
import model.Usuario;
import model.Conexao;

public class UsuarioDAO {

 public Usuario autenticar(String identificacao, String senha, String remoteAddr) {
    String sql = "SELECT * FROM usuarios WHERE (email = ? OR numero_identificacao = ?) "
               + "AND senha = ? AND status = ? LIMIT 1";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, identificacao);
        stmt.setString(2, identificacao);
        stmt.setString(3, senha);
        stmt.setString(4, StatusUsuario.ATIVO.getDescricao());
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Usuario usuario = mapearUsuario(rs);
                registrarLogin(usuario.getId_usuario(), remoteAddr);
                return usuario;
            }
            return null;
        }
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao autenticar usuário: " + e.getMessage(), e);
    }
}

public boolean ativarUsuario(int id) {
    String sql = "UPDATE usuarios SET status = ? WHERE id_usuario = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, StatusUsuario.ATIVO.getDescricao());
        stmt.setInt(2, id);
        
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao ativar usuário: " + e.getMessage(), e);
    }
}

public boolean desativarUsuario(int id) {
    String sql = "UPDATE usuarios SET status = ? WHERE id_usuario = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, StatusUsuario.INATIVO.getDescricao());
        stmt.setInt(2, id);
        
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao desativar usuário: " + e.getMessage(), e);
    }
}
    
    
    // Enums mantidos da versão original
    public enum Cargo {
        COMANDANTE("Comandante"),
        CHEFE_OPERACOES("Chefe das operações"),
        CHEFE_ETICA("Chefe da ética"),
        COMANDANTE_COMPANHIA("Comandante da companhia"),
        COMANDANTE_PELOTAO("Comandante do pelotão"),
        CHEFE_SECCAO("Chefe de secção"),
        ADJUNTO_SECCAO("Adjunto de secção"),
        OFICIAL_PERMANENCIA("Oficial de permanência"),
        ADJUNTO_OFICIAL("Adjunto de oficial de permanência"),
        CHEFE_PESSOAL("Chefe de gestor de pessoal"),
        CHEFE_SECRETARIA("Chefe da Secretaria"),
        ADJUNTO_SECRETARIA("Adjunto de secretaria"),
        ADMINISTRADOR("Administrador");

        private final String descricao;

        Cargo(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        public static class OUTRO {

            public static String name() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            public OUTRO() {
            }
        }
    }
    

    public enum Perfil {
        COMANDO("Comando"),
        OPERACIONAL("Operacional"),
        ADMINISTRATIVO("Administrativo"),
        ETICA("Ética"),
        PLANTAO("Plantão"),
        SECRETARIA("Secretaria"),
        GESTAO_PESSOAL("Gestão de Pessoal"),
        SUPER_ADMIN("Super Admin");

        private final String descricao;

        Perfil(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        public static class PADRAO {

            public static String name() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            public PADRAO() {
            }
        }
    }

    public enum StatusUsuario {
        ATIVO("Ativo"),
        INATIVO("Inativo"),
        SUSPENSO("Suspenso"),
        LICENCA("Licença");

        private final String descricao;

        StatusUsuario(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    // Método para inserir usuário
    public boolean inserirUsuario(Usuario usuario) throws SQLException {
        validarUsuario(usuario);
        
        String sql = "INSERT INTO usuarios (nome, email, senha, cargo, telefone, status, perfil, "
                   + "numero_identificacao, foto_perfil, data_cadastro, ip_ultimo_login) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getCargo());
            stmt.setString(5, usuario.getTelefone());
            stmt.setString(6, usuario.getStatus());
            stmt.setString(7, usuario.getPerfil());
            stmt.setString(8, usuario.getNumero_identificacao());
            stmt.setString(9, usuario.getFoto_perfil());
            stmt.setString(10, usuario.getIp_ultimo_login());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId_usuario(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    
    
    

    // Método para atualizar usuário
    public boolean atualizarUsuario(Usuario usuario) throws SQLException {
        validarUsuario(usuario);
        
        String sql = "UPDATE usuarios SET nome = ?, email = ?, cargo = ?, telefone = ?, "
                   + "status = ?, perfil = ?, numero_identificacao = ?, foto_perfil = ?, "
                   + "data_atualizacao = CURRENT_TIMESTAMP, ip_ultimo_login = ? "
                   + "WHERE id_usuario = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getCargo());
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getStatus());
            stmt.setString(6, usuario.getPerfil());
            stmt.setString(7, usuario.getNumero_identificacao());
            stmt.setString(8, usuario.getFoto_perfil());
            stmt.setString(9, usuario.getIp_ultimo_login());
            stmt.setInt(10, usuario.getId_usuario());
            
            return stmt.executeUpdate() > 0;
        }
    }

    // Método para buscar usuário por ID
    public Usuario buscarUsuarioPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
                return null;
            }
        }
    }

    // Método para listar todos os usuários
    public List<Usuario> listarTodosUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }
        return usuarios;
    }

    // Método para autenticar usuário
    public Usuario autenticarUsuario(String identificacao, String senha, String ip) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE (email = ? OR numero_identificacao = ?) "
                   + "AND senha = ? AND status = 'Ativo' LIMIT 1";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, identificacao);
            stmt.setString(2, identificacao);
            stmt.setString(3, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = mapearUsuario(rs);
                    registrarLogin(usuario.getId_usuario(), ip);
                    return usuario;
                }
                return null;
            }
        }
    }

    // Método para deletar usuário
    public boolean deletarUsuario(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Método para contar usuários
    public int contarUsuarios() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    // Método para buscar usuários por termo
    public List<Usuario> buscarUsuariosPorTermo(String termo) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE nome LIKE ? OR email LIKE ? OR numero_identificacao LIKE ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String termoBusca = "%" + termo + "%";
            stmt.setString(1, termoBusca);
            stmt.setString(2, termoBusca);
            stmt.setString(3, termoBusca);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }
        }
        return usuarios;
    }

    // Métodos auxiliares
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(rs.getInt("id_usuario"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setCargo(rs.getString("cargo"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setStatus(rs.getString("status"));
        usuario.setPerfil(rs.getString("perfil"));
        usuario.setNumero_identificacao(rs.getString("numero_identificacao"));
        usuario.setFoto_perfil(rs.getString("foto_perfil"));
        usuario.setData_cadastro(rs.getTimestamp("data_cadastro"));
        usuario.setData_atualizacao(rs.getTimestamp("data_atualizacao"));
        usuario.setUltimo_login(rs.getTimestamp("ultimo_login"));
        usuario.setIp_ultimo_login(rs.getString("ip_ultimo_login"));
        return usuario;
    }

    private boolean registrarLogin(int idUsuario, String ip) throws SQLException {
        String sql = "UPDATE usuarios SET ultimo_login = CURRENT_TIMESTAMP, ip_ultimo_login = ? "
                   + "WHERE id_usuario = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, ip);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;
        }
    }

    private void validarUsuario(Usuario usuario) throws SQLException {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do usuário é obrigatório");
        }
        
        if (usuario.getEmail() == null || !usuario.getEmail().matches("^[^@]+@[^@]+\\.[^@]+$")) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        if (usuario.getNumero_identificacao() == null || usuario.getNumero_identificacao().trim().isEmpty()) {
            throw new IllegalArgumentException("Número de identificação é obrigatório");
        }
        
        // Verifica se email ou número de identificação já estão em uso
        if (isCampoEmUso("email", usuario.getEmail(), usuario.getId_usuario())) {
            throw new SQLException("Email já está em uso");
        }
        
        if (isCampoEmUso("numero_identificacao", usuario.getNumero_identificacao(), usuario.getId_usuario())) {
            throw new SQLException("Número de identificação já está em uso");
        }
    }

    private boolean isCampoEmUso(String campo, String valor, int idUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE " + campo + " = ? AND id_usuario != ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, valor);
            stmt.setInt(2, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Métodos para dados de teste (opcional)
    public void popularDadosTeste() throws SQLException {
        Usuario admin = new Usuario();
        admin.setNome("Administrador Teste");
        admin.setEmail("admin@teste.com");
        admin.setSenha("123456");
        admin.setCargo(Cargo.ADMINISTRADOR.getDescricao());
        admin.setPerfil(Perfil.SUPER_ADMIN.getDescricao());
        admin.setStatus(StatusUsuario.ATIVO.getDescricao());
        admin.setNumero_identificacao("ADM-001");
        inserirUsuario(admin);
        
        Usuario operador = new Usuario();
        operador.setNome("Operador Teste");
        operador.setEmail("operador@teste.com");
        operador.setSenha("123456");
        operador.setCargo(Cargo.OFICIAL_PERMANENCIA.getDescricao());
        operador.setPerfil(Perfil.OPERACIONAL.getDescricao());
        operador.setStatus(StatusUsuario.ATIVO.getDescricao());
        operador.setNumero_identificacao("OPE-001");
        inserirUsuario(operador);
    }

    public void limparDadosTeste() throws SQLException {
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM usuarios WHERE email LIKE '%@teste.com'");
        }
    }
    
    
    
}
