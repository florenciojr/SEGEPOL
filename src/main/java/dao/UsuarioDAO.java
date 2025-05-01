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
import java.util.Scanner;
import java.nio.file.*;
import java.io.File;
import java.io.IOException;
import model.Usuario;
import model.Conexao;

public class UsuarioDAO {
    private static final String[] CARGOS_DISPONIVEIS = {
        "Agente", "Investigador", "Comandante", "Administrador"
    };
    
    private static final String[] ESTADOS_DISPONIVEIS = {
        "Ativo", "Inativo", "Suspenso", "Férias"
    };
    
    private static final String[] PERFIS_DISPONIVEIS = {
        "Operacional", "Tático", "Estratégico", "Administrativo"
    };
    
    private static final String UPLOAD_DIR = "uploads/usuarios/";
    private static final int MAX_FOTO_SIZE_KB = 2048; // 2MB
    private static final String[] EXTENSOES_PERMITIDAS = {".jpg", ".jpeg", ".png", ".gif"};

    // Método para inserir um novo usuário com tratamento completo de erros
    public boolean inserirUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha, cargo, telefone, status, perfil, estado, numero_identificacao, foto_perfil, data_cadastro) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false); // Inicia transação
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Validações antes de inserir
            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                throw new SQLException("O nome do usuário é obrigatório.");
            }
            
            if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
                throw new SQLException("Email inválido.");
            }
            
           if (buscarUsuarioPorEmail(usuario.getEmail())) {
    throw new SQLException("Já existe um usuário com este email.");
}
            
            if (buscarUsuarioPorNumeroIdentificacao(usuario.getNumero_identificacao()) != null) {
                throw new SQLException("Número de identificação já está em uso.");
            }

            // Setar parâmetros
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getCargo());
            stmt.setString(5, usuario.getContacto());
            stmt.setString(6, usuario.getStatus());
            stmt.setString(7, usuario.getPerfil());
            stmt.setString(8, usuario.getEstado());
            stmt.setString(9, usuario.getNumero_identificacao());
            stmt.setString(10, usuario.getFoto_perfil());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar usuário, nenhuma linha afetada.");
            }

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                usuario.setId_usuario(generatedKeys.getInt(1));
                conn.commit(); // Confirma transação
                return true;
            } else {
                throw new SQLException("Falha ao criar usuário, nenhum ID obtido.");
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback em caso de erro
            }
            throw e; // Re-lança a exceção para tratamento específico
        } finally {
            // Fecha recursos em ordem inversa de abertura
            if (generatedKeys != null) generatedKeys.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Método para listar todos os usuários com paginação
    public List<Usuario> listarUsuarios(int pagina, int itensPorPagina) throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nome LIMIT ? OFFSET ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itensPorPagina);
            stmt.setInt(2, (pagina - 1) * itensPorPagina);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearUsuario(rs));
                }
            }
        }
        return lista;
    }
    
    // Método para contar o total de usuários
    public int contarUsuarios() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM usuarios";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }
    
    // Método auxiliar para mapear ResultSet para objeto Usuario
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
    Usuario usuario = new Usuario();
    usuario.setId_usuario(rs.getInt("id_usuario"));
        usuario.setId_usuario(rs.getInt("id_usuario"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setCargo(rs.getString("cargo"));
        usuario.setContacto(rs.getString("telefone"));
        usuario.setStatus(rs.getString("status"));
        usuario.setPerfil(rs.getString("perfil"));
        usuario.setEstado(rs.getString("estado"));
        usuario.setNumero_identificacao(rs.getString("numero_identificacao"));
        usuario.setFoto_perfil(rs.getString("foto_perfil"));
        usuario.setData_cadastro(rs.getTimestamp("data_cadastro"));
       usuario.setData_atualizacao(rs.getTimestamp("data_atualizacao"));
    return usuario;
      
    }
    
    // Método para buscar usuário por número de identificação
    public Usuario buscarUsuarioPorNumeroIdentificacao(String numeroIdentificacao) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE numero_identificacao = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroIdentificacao);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
                return null;
            }
        }
    }
    
    // Método para verificar se email já existe
  public boolean buscarUsuarioPorEmail(String email) throws SQLException {
    String sql = "SELECT 1 FROM usuarios WHERE email = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, email);
        
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next(); // Retorna true se encontrou um usuário com este email
        }
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

    // Método para atualizar um usuário com tratamento completo de erros
    public boolean atualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, cargo = ?, telefone = ?, " +
                     "status = ?, perfil = ?, estado = ?, numero_identificacao = ?, foto_perfil = ?, " +
                     "data_atualizacao = CURRENT_TIMESTAMP WHERE id_usuario = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false); // Inicia transação
            
            // Verifica se o novo email já está em uso por outro usuário
            if (emailEmUsoPorOutroUsuario(usuario.getEmail(), usuario.getId_usuario())) {
                throw new SQLException("Este email já está sendo usado por outro usuário.");
            }
            
            // Verifica se o novo número de identificação já está em uso
            if (numeroIdentificacaoEmUso(usuario.getNumero_identificacao(), usuario.getId_usuario())) {
                throw new SQLException("Este número de identificação já está em uso por outro usuário.");
            }

            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getCargo());
            stmt.setString(5, usuario.getContacto());
            stmt.setString(6, usuario.getStatus());
            stmt.setString(7, usuario.getPerfil());
            stmt.setString(8, usuario.getEstado());
            stmt.setString(9, usuario.getNumero_identificacao());
            stmt.setString(10, usuario.getFoto_perfil());
            stmt.setInt(11, usuario.getId_usuario());

            int affectedRows = stmt.executeUpdate();
            conn.commit(); // Confirma transação
            
            return affectedRows > 0;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback em caso de erro
            }
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
    
    // Verifica se email está em uso por outro usuário
    private boolean emailEmUsoPorOutroUsuario(String email, int idUsuario) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE email = ? AND id_usuario != ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setInt(2, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    // Verifica se número de identificação está em uso por outro usuário
    private boolean numeroIdentificacaoEmUso(String numeroIdentificacao, int idUsuario) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE numero_identificacao = ? AND id_usuario != ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroIdentificacao);
            stmt.setInt(2, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Método para desativar um usuário (inativação em vez de exclusão física)
    public boolean desativarUsuario(int id) throws SQLException {
        String sql = "UPDATE usuarios SET status = 'Inativo', data_atualizacao = CURRENT_TIMESTAMP WHERE id_usuario = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Método para reativar um usuário
    public boolean reativarUsuario(int id) throws SQLException {
        String sql = "UPDATE usuarios SET status = 'Ativo', data_atualizacao = CURRENT_TIMESTAMP WHERE id_usuario = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Método para listar cargos disponíveis
    public static void listarCargosDisponiveis() {
        System.out.println("\nCargos disponíveis:");
        for (int i = 0; i < CARGOS_DISPONIVEIS.length; i++) {
            System.out.println((i + 1) + ". " + CARGOS_DISPONIVEIS[i]);
        }
    }
    
    // Método para listar estados disponíveis
    public static void listarEstadosDisponiveis() {
        System.out.println("\nEstados disponíveis:");
        for (int i = 0; i < ESTADOS_DISPONIVEIS.length; i++) {
            System.out.println((i + 1) + ". " + ESTADOS_DISPONIVEIS[i]);
        }
    }
    
    // Método para listar perfis disponíveis
    public static void listarPerfisDisponiveis() {
        System.out.println("\nPerfis disponíveis:");
        for (int i = 0; i < PERFIS_DISPONIVEIS.length; i++) {
            System.out.println((i + 1) + ". " + PERFIS_DISPONIVEIS[i]);
        }
    }
    
    // Método para selecionar cargo
    public static String selecionarCargo(Scanner sc) {
        listarCargosDisponiveis();
        System.out.print("Escolha o cargo (1-" + CARGOS_DISPONIVEIS.length + "): ");
        try {
            int escolha = Integer.parseInt(sc.nextLine());
            if (escolha > 0 && escolha <= CARGOS_DISPONIVEIS.length) {
                return CARGOS_DISPONIVEIS[escolha - 1];
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida. Usando cargo padrão (Agente).");
        }
        return "Agente"; // Default
    }
    
    // Método para selecionar estado
    public static String selecionarEstado(Scanner sc) {
        listarEstadosDisponiveis();
        System.out.print("Escolha o estado (1-" + ESTADOS_DISPONIVEIS.length + "): ");
        try {
            int escolha = Integer.parseInt(sc.nextLine());
            if (escolha > 0 && escolha <= ESTADOS_DISPONIVEIS.length) {
                return ESTADOS_DISPONIVEIS[escolha - 1];
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida. Usando estado padrão (Ativo).");
        }
        return "Ativo"; // Default
    }
    
    // Método para selecionar perfil
    public static String selecionarPerfil(Scanner sc) {
        listarPerfisDisponiveis();
        System.out.print("Escolha o perfil (1-" + PERFIS_DISPONIVEIS.length + "): ");
        try {
            int escolha = Integer.parseInt(sc.nextLine());
            if (escolha > 0 && escolha <= PERFIS_DISPONIVEIS.length) {
                return PERFIS_DISPONIVEIS[escolha - 1];
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida. Usando perfil padrão (Operacional).");
        }
        return "Operacional"; // Default
    }

    // Método para upload de foto de perfil com validações
    public static String uploadFotoPerfil(Scanner sc, String nomeUsuario) {
        System.out.print("\nCaminho completo da foto de perfil (deixe em branco para pular): ");
        String caminhoFoto = sc.nextLine().trim();
        
        if (caminhoFoto.isEmpty()) {
            return null;
        }
        
        try {
            // Verifica se o arquivo existe
            Path sourcePath = Paths.get(caminhoFoto);
            if (!Files.exists(sourcePath)) {
                throw new IOException("Arquivo não encontrado.");
            }
            
            // Verifica a extensão do arquivo
            String extensao = caminhoFoto.substring(caminhoFoto.lastIndexOf(".")).toLowerCase();
            boolean extensaoValida = false;
            for (String ext : EXTENSOES_PERMITIDAS) {
                if (ext.equals(extensao)) {
                    extensaoValida = true;
                    break;
                }
            }
            if (!extensaoValida) {
                throw new IOException("Extensão de arquivo não permitida. Use: " + String.join(", ", EXTENSOES_PERMITIDAS));
            }
            
            // Verifica o tamanho do arquivo
            long fileSize = Files.size(sourcePath);
            if (fileSize > MAX_FOTO_SIZE_KB * 1024) {
                throw new IOException("Tamanho do arquivo excede o limite de " + MAX_FOTO_SIZE_KB + "KB.");
            }
            
            // Cria o diretório de uploads se não existir
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Gera um nome único para o arquivo
            String nomeArquivo = nomeUsuario.replaceAll("\\s+", "_").toLowerCase() + "_" + System.currentTimeMillis() + extensao;
            String caminhoDestino = UPLOAD_DIR + nomeArquivo;
            
            // Copia o arquivo para o diretório de uploads
            Files.copy(sourcePath, Paths.get(caminhoDestino), StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("Foto carregada com sucesso: " + caminhoDestino);
            return caminhoDestino;
        } catch (Exception e) {
            System.err.println("Erro ao fazer upload da foto: " + e.getMessage());
            return null;
        }
    }
    
    // Método para remover foto de perfil antiga
    private static void removerFotoAntiga(String caminhoFoto) {
        if (caminhoFoto != null && !caminhoFoto.isEmpty()) {
            try {
                Files.deleteIfExists(Paths.get(caminhoFoto));
            } catch (IOException e) {
                System.err.println("Aviso: Não foi possível remover a foto antiga: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();
        Scanner sc = new Scanner(System.in);
        int opcao;
        int itensPorPagina = 5; // Itens por página na listagem

        do {
            System.out.println("\n=== MENU USUÁRIO ===");
            System.out.println("1. Inserir usuário");
            System.out.println("2. Listar usuários");
            System.out.println("3. Buscar usuário por ID");
            System.out.println("4. Atualizar usuário");
            System.out.println("5. Desativar usuário");
            System.out.println("6. Reativar usuário");
            System.out.println("7. Buscar usuário por número de identificação");
            System.out.println("8. Listar usuários inativos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            
            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            try {
                switch (opcao) {
                    case 1:
                        inserirUsuarioMenu(dao, sc);
                        break;

                    case 2:
                        listarUsuariosMenu(dao, sc, itensPorPagina);
                        break;

                    case 3:
                        buscarUsuarioPorIdMenu(dao, sc);
                        break;

                    case 4:
                        atualizarUsuarioMenu(dao, sc);
                        break;

                    case 5:
                        desativarUsuarioMenu(dao, sc);
                        break;
                        
                    case 6:
                        reativarUsuarioMenu(dao, sc);
                        break;

                    case 7:
                        buscarUsuarioPorNumeroIdentificacaoMenu(dao, sc);
                        break;
                        
                    case 8:
                        listarUsuariosInativosMenu(dao, sc, itensPorPagina);
                        break;

                    case 0:
                        System.out.println("Saindo...");
                        break;

                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } catch (SQLException e) {
                System.err.println("\nErro de banco de dados: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("\nErro inesperado: " + e.getMessage());
            }

        } while (opcao != 0);

        sc.close();
    }
    
    // Métodos auxiliares para o menu
    private static void inserirUsuarioMenu(UsuarioDAO dao, Scanner sc) throws SQLException {
        Usuario novoUsuario = new Usuario();
        
        System.out.println("\n=== NOVO USUÁRIO ===");
        
        // Número de Identificação
        while (true) {
            System.out.print("Número de Identificação: ");
            String numId = sc.nextLine().trim();
            if (!numId.isEmpty()) {
                if (dao.buscarUsuarioPorNumeroIdentificacao(numId) == null) {
                    novoUsuario.setNumero_identificacao(numId);
                    break;
                } else {
                    System.out.println("Este número de identificação já está em uso!");
                }
            } else {
                System.out.println("Número de identificação é obrigatório!");
            }
        }
        
        // Nome
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        while (nome.isEmpty()) {
            System.out.println("Nome é obrigatório!");
            System.out.print("Nome: ");
            nome = sc.nextLine().trim();
        }
        novoUsuario.setNome(nome);
        
        // Email
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        while (email.isEmpty() || !email.contains("@")) {
            System.out.println("Email inválido!");
            System.out.print("Email: ");
            email = sc.nextLine().trim();
        }
        novoUsuario.setEmail(email);
        
        // Senha
        System.out.print("Senha: ");
        String senha = sc.nextLine().trim();
        while (senha.isEmpty() || senha.length() < 6) {
            System.out.println("Senha deve ter pelo menos 6 caracteres!");
            System.out.print("Senha: ");
            senha = sc.nextLine().trim();
        }
        novoUsuario.setSenha(senha);
        
        // Cargo
        novoUsuario.setCargo(selecionarCargo(sc));
        
        // Telefone
        System.out.print("Telefone: ");
        novoUsuario.setContacto(sc.nextLine().trim());
        
        // Status
        novoUsuario.setStatus(selecionarEstado(sc));
        
        // Perfil
        novoUsuario.setPerfil(selecionarPerfil(sc));
        
        // Estado (UF)
        System.out.print("Estado (UF): ");
        novoUsuario.setEstado(sc.nextLine().trim().toUpperCase());
        
        // Foto de perfil
        String caminhoFoto = uploadFotoPerfil(sc, nome);
        novoUsuario.setFoto_perfil(caminhoFoto);

        try {
            if (dao.inserirUsuario(novoUsuario)) {
                System.out.println("\nUsuário cadastrado com sucesso! ID: " + novoUsuario.getId_usuario());
            }
        } catch (SQLException e) {
            // Remove a foto se foi enviada mas houve erro no banco
            if (caminhoFoto != null) {
                removerFotoAntiga(caminhoFoto);
            }
            throw e;
        }
    }
    
    private static void listarUsuariosMenu(UsuarioDAO dao, Scanner sc, int itensPorPagina) throws SQLException {
        int totalUsuarios = dao.contarUsuarios();
        int totalPaginas = (int) Math.ceil((double) totalUsuarios / itensPorPagina);
        int paginaAtual = 1;
        
        if (totalUsuarios == 0) {
            System.out.println("\nNenhum usuário cadastrado.");
            return;
        }
        
        do {
            System.out.printf("\n=== LISTA DE USUÁRIOS (Página %d de %d) ===\n", paginaAtual, totalPaginas);
            
            List<Usuario> usuarios = dao.listarUsuarios(paginaAtual, itensPorPagina);
            for (Usuario u : usuarios) {
                exibirResumoUsuario(u);
            }
            
            if (totalPaginas > 1) {
                System.out.println("\nNavegação: [P]róxima, [A]nterior, [V]oltar");
                System.out.print("Opção: ");
                String opcao = sc.nextLine().trim().toLowerCase();
                
                if (opcao.equals("p") && paginaAtual < totalPaginas) {
                    paginaAtual++;
                } else if (opcao.equals("a") && paginaAtual > 1) {
                    paginaAtual--;
                } else if (opcao.equals("v")) {
                    break;
                }
            } else {
                break;
            }
        } while (true);
    }
    
    private static void listarUsuariosInativosMenu(UsuarioDAO dao, Scanner sc, int itensPorPagina) throws SQLException {
        // Implementação similar à listarUsuariosMenu mas filtrando por status 'Inativo'
        // Pode ser adaptado conforme necessidade
        System.out.println("\n=== LISTA DE USUÁRIOS INATIVOS ===");
        // ...
    }
    
    private static void buscarUsuarioPorIdMenu(UsuarioDAO dao, Scanner sc) throws SQLException {
        System.out.print("\nID do usuário: ");
        try {
            int idBusca = Integer.parseInt(sc.nextLine());
            Usuario usuarioEncontrado = dao.buscarUsuarioPorId(idBusca);
            if (usuarioEncontrado != null) {
                exibirDetalhesUsuario(usuarioEncontrado);
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }
    
    private static void buscarUsuarioPorNumeroIdentificacaoMenu(UsuarioDAO dao, Scanner sc) throws SQLException {
        System.out.print("\nNúmero de Identificação do usuário: ");
        String numeroIdentificacaoBusca = sc.nextLine().trim();
        Usuario usuarioPorNumero = dao.buscarUsuarioPorNumeroIdentificacao(numeroIdentificacaoBusca);
        if (usuarioPorNumero != null) {
            exibirDetalhesUsuario(usuarioPorNumero);
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }
    
    private static void atualizarUsuarioMenu(UsuarioDAO dao, Scanner sc) throws SQLException {
        System.out.print("\nID do usuário para atualizar: ");
        try {
            int idAtualizar = Integer.parseInt(sc.nextLine());
            Usuario usuarioAtualizar = dao.buscarUsuarioPorId(idAtualizar);
            
            if (usuarioAtualizar != null) {
                System.out.println("\nDados atuais do usuário:");
                exibirDetalhesUsuario(usuarioAtualizar);
                
                System.out.println("\nDigite os novos dados (deixe em branco para manter o valor atual):");
                
                // Nome
                System.out.print("Nome (" + usuarioAtualizar.getNome() + "): ");
                String nome = sc.nextLine();
                if (!nome.isEmpty()) usuarioAtualizar.setNome(nome);
                
                // Número de Identificação
                while (true) {
                    System.out.print("Número de Identificação (" + usuarioAtualizar.getNumero_identificacao() + "): ");
                    String numId = sc.nextLine();
                    if (numId.isEmpty()) {
                        break; // Mantém o atual
                    }
                    if (dao.numeroIdentificacaoEmUso(numId, usuarioAtualizar.getId_usuario())) {
                        System.out.println("Este número de identificação já está em uso por outro usuário!");
                    } else {
                        usuarioAtualizar.setNumero_identificacao(numId);
                        break;
                    }
                }
                
                // Email
                while (true) {
                    System.out.print("Email (" + usuarioAtualizar.getEmail() + "): ");
                    String email = sc.nextLine();
                    if (email.isEmpty()) {
                        break; // Mantém o atual
                    }
                    if (!email.contains("@")) {
                        System.out.println("Email inválido!");
                    } else if (dao.emailEmUsoPorOutroUsuario(email, usuarioAtualizar.getId_usuario())) {
                        System.out.println("Este email já está em uso por outro usuário!");
                    } else {
                        usuarioAtualizar.setEmail(email);
                        break;
                    }
                }
                
                // Senha
                System.out.print("Nova senha (deixe em branco para manter): ");
                String senha = sc.nextLine();
                if (!senha.isEmpty()) usuarioAtualizar.setSenha(senha);
                
                // Cargo
                System.out.println("Cargo atual: " + usuarioAtualizar.getCargo());
                System.out.print("Deseja alterar o cargo? (s/n): ");
                if (sc.nextLine().equalsIgnoreCase("s")) {
                    usuarioAtualizar.setCargo(selecionarCargo(sc));
                }
                
                // Telefone
                System.out.print("Telefone (" + usuarioAtualizar.getContacto() + "): ");
                String telefone = sc.nextLine();
                if (!telefone.isEmpty()) usuarioAtualizar.setContacto(telefone);
                
                // Status
                System.out.println("Status atual: " + usuarioAtualizar.getStatus());
                System.out.print("Deseja alterar o status? (s/n): ");
                if (sc.nextLine().equalsIgnoreCase("s")) {
                    usuarioAtualizar.setStatus(selecionarEstado(sc));
                }
                
                // Perfil
                System.out.println("Perfil atual: " + usuarioAtualizar.getPerfil());
                System.out.print("Deseja alterar o perfil? (s/n): ");
                if (sc.nextLine().equalsIgnoreCase("s")) {
                    usuarioAtualizar.setPerfil(selecionarPerfil(sc));
                }
                
                // Estado (UF)
                System.out.print("Estado (UF) (" + usuarioAtualizar.getEstado() + "): ");
                String estado = sc.nextLine();
                if (!estado.isEmpty()) usuarioAtualizar.setEstado(estado.toUpperCase());
                
                // Foto de perfil
                String fotoAntiga = usuarioAtualizar.getFoto_perfil();
                System.out.println("Foto atual: " + (fotoAntiga != null ? fotoAntiga : "Nenhuma foto cadastrada"));
                System.out.print("Deseja alterar a foto? (s/n): ");
                if (sc.nextLine().equalsIgnoreCase("s")) {
                    String novaFoto = uploadFotoPerfil(sc, usuarioAtualizar.getNome());
                    if (novaFoto != null) {
                        usuarioAtualizar.setFoto_perfil(novaFoto);
                        // Remove a foto antiga se existir
                        removerFotoAntiga(fotoAntiga);
                    }
                }

                try {
                    if (dao.atualizarUsuario(usuarioAtualizar)) {
                        System.out.println("\nUsuário atualizado com sucesso!");
                    } else {
                        System.out.println("\nFalha ao atualizar usuário.");
                    }
                } catch (SQLException e) {
                    // Se houve erro, reverte a foto (se foi alterada)
                    if (usuarioAtualizar.getFoto_perfil() != null && 
                        !usuarioAtualizar.getFoto_perfil().equals(fotoAntiga)) {
                        removerFotoAntiga(usuarioAtualizar.getFoto_perfil());
                        usuarioAtualizar.setFoto_perfil(fotoAntiga);
                    }
                    throw e;
                }
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }
    
    private static void desativarUsuarioMenu(UsuarioDAO dao, Scanner sc) throws SQLException {
        System.out.print("\nID do usuário para desativar: ");
        try {
            int idDesativar = Integer.parseInt(sc.nextLine());
            Usuario usuario = dao.buscarUsuarioPorId(idDesativar);
            
            if (usuario != null) {
                if (usuario.getStatus().equalsIgnoreCase("Inativo")) {
                    System.out.println("Este usuário já está inativo.");
                } else {
                    if (dao.desativarUsuario(idDesativar)) {
                        System.out.println("Usuário desativado com sucesso.");
                    } else {
                        System.out.println("Falha ao desativar usuário.");
                    }
                }
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }
    
    private static void reativarUsuarioMenu(UsuarioDAO dao, Scanner sc) throws SQLException {
        System.out.print("\nID do usuário para reativar: ");
        try {
            int idReativar = Integer.parseInt(sc.nextLine());
            Usuario usuario = dao.buscarUsuarioPorId(idReativar);
            
            if (usuario != null) {
                if (!usuario.getStatus().equalsIgnoreCase("Inativo")) {
                    System.out.println("Este usuário não está inativo.");
                } else {
                    if (dao.reativarUsuario(idReativar)) {
                        System.out.println("Usuário reativado com sucesso.");
                    } else {
                        System.out.println("Falha ao reativar usuário.");
                    }
                }
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }
    
    private static void exibirResumoUsuario(Usuario usuario) {
        System.out.println("\nID: " + usuario.getId_usuario());
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Cargo: " + usuario.getCargo());
        System.out.println("Status: " + usuario.getStatus());
        System.out.println("Nº Identificação: " + usuario.getNumero_identificacao());
        System.out.println("-----------------------------------");
    }
    
    private static void exibirDetalhesUsuario(Usuario usuario) {
        System.out.println("\n=== DETALHES DO USUÁRIO ===");
        System.out.println("ID: " + usuario.getId_usuario());
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Cargo: " + usuario.getCargo());
        System.out.println("Status: " + usuario.getStatus());
        System.out.println("Telefone: " + usuario.getContacto());
        System.out.println("Nº Identificação: " + usuario.getNumero_identificacao());
        System.out.println("Perfil: " + usuario.getPerfil());
        System.out.println("Estado: " + usuario.getEstado());
        System.out.println("Foto de Perfil: " + (usuario.getFoto_perfil() != null ? usuario.getFoto_perfil() : "Nenhuma foto cadastrada"));
        System.out.println("Data Cadastro: " + usuario.getData_cadastro());
        System.out.println("Última Atualização: " + usuario.getData_atualizacao());
    }
}

// USAR NO FINAL OS MAIN PARA CRIAR OS SERVLETS