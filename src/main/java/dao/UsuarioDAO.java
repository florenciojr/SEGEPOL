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

    // Método para inserir um novo usuário
    public void inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha, perfil, estado, telefone) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPerfil());
            stmt.setString(5, usuario.getEstado());
            stmt.setString(6, usuario.getContacto());

            stmt.executeUpdate();
            System.out.println("Usuário inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
        }
    }

    // Método para listar todos os usuários
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPerfil(rs.getString("perfil"));
                usuario.setEstado(rs.getString("estado"));
                usuario.setContacto(rs.getString("telefone"));
                lista.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }

        return lista;
    }

    // Método para buscar um usuário por ID
    public Usuario buscarUsuarioPorId(int id) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setPerfil(rs.getString("perfil"));
                    usuario.setEstado(rs.getString("estado"));
                    usuario.setContacto(rs.getString("telefone"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return usuario;
    }

    // Método para atualizar um usuário
    public void atualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, perfil = ?, estado = ?, telefone = ? WHERE id_usuario = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPerfil());
            stmt.setString(5, usuario.getEstado());
            stmt.setString(6, usuario.getContacto());
            stmt.setInt(7, usuario.getId_usuario());

            stmt.executeUpdate();
            System.out.println("Usuário atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    // Método para deletar um usuário
    public void deletarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
            System.out.println("Usuário deletado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao deletar usuário: " + e.getMessage());
        }
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
        System.out.println("5. Deletar usuário");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
        opcao = sc.nextInt();
        sc.nextLine(); // Limpar o buffer

        switch (opcao) {
            case 1:
                Usuario novoUsuario = new Usuario();
                System.out.print("Nome: ");
                novoUsuario.setNome(sc.nextLine());
                System.out.print("Email: ");
                novoUsuario.setEmail(sc.nextLine());
                System.out.print("Senha: ");
                novoUsuario.setSenha(sc.nextLine());
                System.out.print("Perfil: ");
                novoUsuario.setPerfil(sc.nextLine());
                System.out.print("Estado: ");
                novoUsuario.setEstado(sc.nextLine());
                System.out.print("Contacto: ");
                novoUsuario.setContacto(sc.nextLine());
                dao.inserirUsuario(novoUsuario);
                break;

            case 2:
                List<Usuario> usuarios = dao.listarUsuarios();
                for (Usuario u : usuarios) {
                    System.out.println("ID: " + u.getId_usuario());
                    System.out.println("Nome: " + u.getNome());
                    System.out.println("Email: " + u.getEmail());
                    System.out.println("Perfil: " + u.getPerfil());
                    System.out.println("Estado: " + u.getEstado());
                    System.out.println("Contacto: " + u.getContacto());
                    System.out.println("-----------------------------------");
                }
                break;

            case 3:
                System.out.print("ID do usuário: ");
                int idBusca = sc.nextInt();
                Usuario usuarioEncontrado = dao.buscarUsuarioPorId(idBusca);
                if (usuarioEncontrado != null) {
                    System.out.println("Nome: " + usuarioEncontrado.getNome());
                    System.out.println("Email: " + usuarioEncontrado.getEmail());
                    System.out.println("Perfil: " + usuarioEncontrado.getPerfil());
                    System.out.println("Estado: " + usuarioEncontrado.getEstado());
                    System.out.println("Contacto: " + usuarioEncontrado.getContacto());
                } else {
                    System.out.println("Usuário não encontrado.");
                }
                break;

            case 4:
                System.out.print("ID do usuário para atualizar: ");
                int idAtualizar = sc.nextInt();
                sc.nextLine(); // Limpar o buffer

                Usuario usuarioAtualizar = dao.buscarUsuarioPorId(idAtualizar);
                if (usuarioAtualizar != null) {
                    System.out.print("Novo nome (" + usuarioAtualizar.getNome() + "): ");
                    usuarioAtualizar.setNome(sc.nextLine());
                    System.out.print("Novo email (" + usuarioAtualizar.getEmail() + "): ");
                    usuarioAtualizar.setEmail(sc.nextLine());
                    System.out.print("Nova senha: ");
                    usuarioAtualizar.setSenha(sc.nextLine());
                    System.out.print("Novo perfil (" + usuarioAtualizar.getPerfil() + "): ");
                    usuarioAtualizar.setPerfil(sc.nextLine());
                    System.out.print("Novo estado (" + usuarioAtualizar.getEstado() + "): ");
                    usuarioAtualizar.setEstado(sc.nextLine());
                    System.out.print("Novo contacto (" + usuarioAtualizar.getContacto() + "): ");
                    usuarioAtualizar.setContacto(sc.nextLine());

                    dao.atualizarUsuario(usuarioAtualizar);
                } else {
                    System.out.println("Usuário não encontrado.");
                }
                break;

            case 5:
                System.out.print("ID do usuário para deletar: ");
                int idDeletar = sc.nextInt();
                dao.deletarUsuario(idDeletar);
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

}
