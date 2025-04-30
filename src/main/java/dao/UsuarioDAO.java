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
import model.Usuario;
import model.Conexao;

public class UsuarioDAO {
    private static final String[] CARGOS_DISPONIVEIS = {
        "Agente", "Investigador", "Comandante", "Administrador"
    };
    
    private static final String[] ESTADOS_DISPONIVEIS = {
        "Ativo", "Inativo"
    };

    // Método para inserir um novo usuário
    public boolean inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha, cargo, telefone, status, perfil, estado, numero_identificacao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getCargo());
            stmt.setString(5, usuario.getContacto());
            stmt.setString(6, usuario.getStatus());
            stmt.setString(7, usuario.getPerfil());
            stmt.setString(8, usuario.getEstado());
            stmt.setString(9, usuario.getNumero_identificacao());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar usuário, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId_usuario(generatedKeys.getInt(1));
                    return true;
                } else {
                    throw new SQLException("Falha ao criar usuário, nenhum ID obtido.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
            return false;
        }
    }

    // Método para listar todos os usuários
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nome";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }
        return lista;
    }
    
    // Método auxiliar para mapear ResultSet para objeto Usuario
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
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
usuario.setData_cadastro(rs.getTimestamp("data_cadastro"));
        return usuario;
    }
    
    // Método para buscar usuário por número de identificação
    public Usuario buscarUsuarioPorNumeroIdentificacao(String numeroIdentificacao) {
        String sql = "SELECT * FROM usuarios WHERE numero_identificacao = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroIdentificacao);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por número de identificação: " + e.getMessage());
        }
        return null;
    }

    // Método para buscar usuário por ID
    public Usuario buscarUsuarioPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
        }
        return null;
    }

    // Método para atualizar um usuário
    public boolean atualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, cargo = ?, telefone = ?, " +
                     "status = ?, perfil = ?, estado = ?, numero_identificacao = ? WHERE id_usuario = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getCargo());
            stmt.setString(5, usuario.getContacto());
            stmt.setString(6, usuario.getStatus());
            stmt.setString(7, usuario.getPerfil());
            stmt.setString(8, usuario.getEstado());
            stmt.setString(9, usuario.getNumero_identificacao());
            stmt.setInt(10, usuario.getId_usuario());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    // Método para deletar um usuário (inativação em vez de exclusão física)
    public boolean desativarUsuario(int id) {
        String sql = "UPDATE usuarios SET status = 'Inativo' WHERE id_usuario = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao desativar usuário: " + e.getMessage());
            return false;
        }
    }
    
    // Método para listar cargos disponíveis
    public static void listarCargosDisponiveis() {
        System.out.println("\nCargos disponíveis:");
        for (int i = 0; i < CARGOS_DISPONIVEIS.length; i++) {
            System.out.println((i + 1) + ". " + CARGOS_DISPONIVEIS[i]);
        }
    }
    
    // Método para selecionar cargo
    public static String selecionarCargo(Scanner sc) {
        listarCargosDisponiveis();
        System.out.print("Escolha o cargo (1-" + CARGOS_DISPONIVEIS.length + "): ");
        int escolha = sc.nextInt();
        sc.nextLine(); // Limpar buffer
        
        if (escolha > 0 && escolha <= CARGOS_DISPONIVEIS.length) {
            return CARGOS_DISPONIVEIS[escolha - 1];
        }
        return "Agente"; // Default
    }
    
    // Método para selecionar estado
    public static String selecionarEstado(Scanner sc) {
        System.out.println("\nEstados disponíveis:");
        for (int i = 0; i < ESTADOS_DISPONIVEIS.length; i++) {
            System.out.println((i + 1) + ". " + ESTADOS_DISPONIVEIS[i]);
        }
        
        System.out.print("Escolha o estado (1-" + ESTADOS_DISPONIVEIS.length + "): ");
        int escolha = sc.nextInt();
        sc.nextLine(); // Limpar buffer
        
        if (escolha > 0 && escolha <= ESTADOS_DISPONIVEIS.length) {
            return ESTADOS_DISPONIVEIS[escolha - 1];
        }
        return "Ativo"; // Default
    }

    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=== MENU USUÁRIO ===");
            System.out.println("1. Inserir usuário");
            System.out.println("2. Listar usuários");
            System.out.println("3. Buscar usuário por ID");
            System.out.println("4. Atualizar usuário");
            System.out.println("5. Desativar usuário");
            System.out.println("6. Buscar usuário por número de identificação");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            
            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    Usuario novoUsuario = new Usuario();
                    System.out.print("\nNúmero de Identificação: ");
                    novoUsuario.setNumero_identificacao(sc.nextLine());

                    System.out.print("Nome: ");
                    novoUsuario.setNome(sc.nextLine());
                    
                    System.out.print("Email: ");
                    novoUsuario.setEmail(sc.nextLine());
                    
                    System.out.print("Senha: ");
                    novoUsuario.setSenha(sc.nextLine());
                    
                    novoUsuario.setCargo(selecionarCargo(sc));
                    
                    System.out.print("Telefone: ");
                    novoUsuario.setContacto(sc.nextLine());
                    
                    novoUsuario.setStatus(selecionarEstado(sc));
                    
                    System.out.print("Perfil: ");
                    novoUsuario.setPerfil(sc.nextLine());
                    
                    System.out.print("Estado (UF): ");
                    novoUsuario.setEstado(sc.nextLine().toUpperCase());

                    if (dao.inserirUsuario(novoUsuario)) {
                        System.out.println("\nUsuário cadastrado com sucesso! ID: " + novoUsuario.getId_usuario());
                    } else {
                        System.out.println("\nFalha ao cadastrar usuário.");
                    }
                    break;

                case 2:
                    List<Usuario> usuarios = dao.listarUsuarios();
                    if (usuarios.isEmpty()) {
                        System.out.println("\nNenhum usuário cadastrado.");
                    } else {
                        System.out.println("\n=== LISTA DE USUÁRIOS ===");
                        for (Usuario u : usuarios) {
                            System.out.println("\nID: " + u.getId_usuario());
                            System.out.println("Nome: " + u.getNome());
                            System.out.println("Email: " + u.getEmail());
                            System.out.println("Cargo: " + u.getCargo());
                            System.out.println("Status: " + u.getStatus());
                            System.out.println("Telefone: " + u.getContacto());
                            System.out.println("Nº Identificação: " + u.getNumero_identificacao());
                            System.out.println("Data Cadastro: " + u.getData_cadastro());
                            System.out.println("-----------------------------------");
                        }
                    }
                    break;

                case 3:
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
                    break;

                case 4:
                    System.out.print("\nID do usuário para atualizar: ");
                    try {
                        int idAtualizar = Integer.parseInt(sc.nextLine());
                        Usuario usuarioAtualizar = dao.buscarUsuarioPorId(idAtualizar);
                        
                        if (usuarioAtualizar != null) {
                            System.out.println("\nDados atuais do usuário:");
                            exibirDetalhesUsuario(usuarioAtualizar);
                            
                            System.out.println("\nDigite os novos dados (deixe em branco para manter o valor atual):");
                            
                            System.out.print("Nome (" + usuarioAtualizar.getNome() + "): ");
                            String nome = sc.nextLine();
                            if (!nome.isEmpty()) usuarioAtualizar.setNome(nome);
                            
                            System.out.print("Número de Identificação (" + usuarioAtualizar.getNumero_identificacao()+ "): ");
                            String numId = sc.nextLine();
                            if (!numId.isEmpty()) usuarioAtualizar.setNumero_identificacao(numId);
                            
                            System.out.print("Email (" + usuarioAtualizar.getEmail() + "): ");
                            String email = sc.nextLine();
                            if (!email.isEmpty()) usuarioAtualizar.setEmail(email);
                            
                            System.out.print("Nova senha (deixe em branco para manter): ");
                            String senha = sc.nextLine();
                            if (!senha.isEmpty()) usuarioAtualizar.setSenha(senha);
                            
                            System.out.println("Cargo atual: " + usuarioAtualizar.getCargo());
                            System.out.print("Deseja alterar o cargo? (s/n): ");
                            if (sc.nextLine().equalsIgnoreCase("s")) {
                                usuarioAtualizar.setCargo(selecionarCargo(sc));
                            }
                            
                            System.out.print("Telefone (" + usuarioAtualizar.getContacto() + "): ");
                            String telefone = sc.nextLine();
                            if (!telefone.isEmpty()) usuarioAtualizar.setContacto(telefone);
                            
                            System.out.println("Status atual: " + usuarioAtualizar.getStatus());
                            System.out.print("Deseja alterar o status? (s/n): ");
                            if (sc.nextLine().equalsIgnoreCase("s")) {
                                usuarioAtualizar.setStatus(selecionarEstado(sc));
                            }
                            
                            System.out.print("Perfil (" + usuarioAtualizar.getPerfil() + "): ");
                            String perfil = sc.nextLine();
                            if (!perfil.isEmpty()) usuarioAtualizar.setPerfil(perfil);
                            
                            System.out.print("Estado (UF) (" + usuarioAtualizar.getEstado() + "): ");
                            String estado = sc.nextLine();
                            if (!estado.isEmpty()) usuarioAtualizar.setEstado(estado.toUpperCase());

                            if (dao.atualizarUsuario(usuarioAtualizar)) {
                                System.out.println("\nUsuário atualizado com sucesso!");
                            } else {
                                System.out.println("\nFalha ao atualizar usuário.");
                            }
                        } else {
                            System.out.println("Usuário não encontrado.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                    }
                    break;
                    
                case 5:
                    System.out.print("\nID do usuário para desativar: ");
                    try {
                        int idDesativar = Integer.parseInt(sc.nextLine());
                        if (dao.desativarUsuario(idDesativar)) {
                            System.out.println("Usuário desativado com sucesso.");
                        } else {
                            System.out.println("Falha ao desativar usuário ou usuário não encontrado.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                    }
                    break;
                    
                case 6:
                    System.out.print("\nNúmero de Identificação do usuário: ");
                    String numeroIdentificacaoBusca = sc.nextLine();
                    Usuario usuarioPorNumero = dao.buscarUsuarioPorNumeroIdentificacao(numeroIdentificacaoBusca);
                    if (usuarioPorNumero != null) {
                        exibirDetalhesUsuario(usuarioPorNumero);
                    } else {
                        System.out.println("Usuário não encontrado.");
                    }
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);

        sc.close();
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
        System.out.println("Data Cadastro: " + usuario.getData_cadastro());
    }
}

// USAR NO FINAL OS MAIN PARA CRIAR OS SERVLETS