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
import model.Suspeito;
import model.Conexao;

public class SuspeitoDAO {

    private Connection conexao;

    public SuspeitoDAO() {
        conexao = Conexao.conectar();
    }
    
    public boolean existeIdQueixa(int id) {
    String sql = "SELECT 1 FROM queixas WHERE id_queixa = ?";
    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    
    
    public boolean existePorId(int id) {
    String sql = "SELECT 1 FROM suspeitos WHERE id_suspeito = ?";
    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        return rs.next(); // Retorna true se encontrou o ID
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


public void inserir(Suspeito suspeito) {
    // Verificar se o ID da queixa existe
    String verificarSQL = "SELECT 1 FROM queixas WHERE id_queixa = ?";
    try (PreparedStatement checkStmt = conexao.prepareStatement(verificarSQL)) {
        checkStmt.setInt(1, suspeito.getIdQueixa());
        try (ResultSet rs = checkStmt.executeQuery()) {
            if (!rs.next()) {
                System.out.println("Erro: A queixa com ID " + suspeito.getIdQueixa() + " não existe.");
                return;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return;
    }

    // Inserir o suspeito
    String sql = "INSERT INTO suspeitos (id_queixa, nome, descricao, genero, data_nascimento) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        stmt.setInt(1, suspeito.getIdQueixa());
        stmt.setString(2, suspeito.getNome());
        stmt.setString(3, suspeito.getDescricao());
        stmt.setString(4, suspeito.getGenero());
        stmt.setString(5, suspeito.getDataNascimento());
        stmt.executeUpdate();
        System.out.println("Suspeito inserido com sucesso.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public List<Suspeito> listarTodos() {
        List<Suspeito> lista = new ArrayList<>();
        String sql = "SELECT * FROM suspeitos";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Suspeito s = new Suspeito();
                s.setIdSuspeito(rs.getInt("id_suspeito"));
                s.setIdQueixa(rs.getInt("id_queixa"));
                s.setNome(rs.getString("nome"));
                s.setDescricao(rs.getString("descricao"));
                s.setGenero(rs.getString("genero"));
                s.setDataNascimento(rs.getString("data_nascimento"));
                lista.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Suspeito buscarPorId(int id) {
        String sql = "SELECT * FROM suspeitos WHERE id_suspeito = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Suspeito s = new Suspeito();
                    s.setIdSuspeito(rs.getInt("id_suspeito"));
                    s.setIdQueixa(rs.getInt("id_queixa"));
                    s.setNome(rs.getString("nome"));
                    s.setDescricao(rs.getString("descricao"));
                    s.setGenero(rs.getString("genero"));
                    s.setDataNascimento(rs.getString("data_nascimento"));
                    return s;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

   public void atualizar(Suspeito suspeito) {
    if (!existePorId(suspeito.getIdSuspeito())) {
        System.out.println("Erro: Suspeito com ID " + suspeito.getIdSuspeito() + " não encontrado.");
        return;   
    }
    if (!existeIdQueixa(suspeito.getIdQueixa())) {
    System.out.println("Erro: Queixa com ID " + suspeito.getIdQueixa() + " não encontrada.");
    return;
}


    String sql = "UPDATE suspeitos SET id_queixa = ?, nome = ?, descricao = ?, genero = ?, data_nascimento = ? WHERE id_suspeito = ?";
    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        stmt.setInt(1, suspeito.getIdQueixa());
        stmt.setString(2, suspeito.getNome());
        stmt.setString(3, suspeito.getDescricao());
        stmt.setString(4, suspeito.getGenero());
        stmt.setString(5, suspeito.getDataNascimento());
        stmt.setInt(6, suspeito.getIdSuspeito());
        int linhasAfetadas = stmt.executeUpdate();
        
        if (linhasAfetadas > 0) {
            System.out.println("Suspeito atualizado com sucesso.");
        } else {
            System.out.println("Erro ao atualizar suspeito. Nenhuma linha foi afetada.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public void deletar(int id) {
        String sql = "DELETE FROM suspeitos WHERE id_suspeito = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Suspeito deletado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SuspeitoDAO dao = new SuspeitoDAO();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1. Inserir novo suspeito");
            System.out.println("2. Listar todos os suspeitos");
            System.out.println("3. Buscar suspeito por ID");
            System.out.println("4. Atualizar suspeito");
            System.out.println("5. Deletar suspeito");
            System.out.println("6. Sair");

            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    Suspeito novo = new Suspeito();
                    System.out.print("ID da queixa: ");
                    novo.setIdQueixa(Integer.parseInt(scanner.nextLine()));
                    System.out.print("Nome: ");
                    novo.setNome(scanner.nextLine());
                    System.out.print("Descrição: ");
                    novo.setDescricao(scanner.nextLine());
                    System.out.print("Gênero: ");
                    novo.setGenero(scanner.nextLine());
                    System.out.print("Data de nascimento (YYYY-MM-DD): ");
                    novo.setDataNascimento(scanner.nextLine());
                    dao.inserir(novo);
                    break;

                case 2:
                    List<Suspeito> lista = dao.listarTodos();
                    for (Suspeito s : lista) {
                        System.out.println(s);
                    }
                    break;

                case 3:
                    System.out.print("Digite o ID do suspeito: ");
                    int idBusca = Integer.parseInt(scanner.nextLine());
                    Suspeito encontrado = dao.buscarPorId(idBusca);
                    if (encontrado != null) {
                        System.out.println(encontrado);
                    } else {
                        System.out.println("Suspeito não encontrado.");
                    }
                    break;

                case 4:
                    Suspeito atualizar = new Suspeito();
                    System.out.print("ID do suspeito a atualizar: ");
                    atualizar.setIdSuspeito(Integer.parseInt(scanner.nextLine()));
                    System.out.print("Novo ID da queixa: ");
                    atualizar.setIdQueixa(Integer.parseInt(scanner.nextLine()));
                    System.out.print("Novo nome: ");
                    atualizar.setNome(scanner.nextLine());
                    System.out.print("Nova descrição: ");
                    atualizar.setDescricao(scanner.nextLine());
                    System.out.print("Novo gênero: ");
                    atualizar.setGenero(scanner.nextLine());
                    System.out.print("Nova data de nascimento (YYYY-MM-DD): ");
                    atualizar.setDataNascimento(scanner.nextLine());
                    dao.atualizar(atualizar);
                    break;

                case 5:
                    System.out.print("ID do suspeito a deletar: ");
                    int idDel = Integer.parseInt(scanner.nextLine());
                    dao.deletar(idDel);
                    break;

                case 6:
                    System.out.println("Saindo...");
                    return;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

   
}
