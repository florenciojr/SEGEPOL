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
import model.AndamentoQueixa;
import model.Conexao;

public class AndamentoQueixaDAO {
    private Connection conexao;

    public AndamentoQueixaDAO() {
        this.conexao = Conexao.conectar();
    }

    // Método auxiliar para verificar existência
    private boolean existeRegistro(String tabela, String campo, int id) {
        String sql = "SELECT 1 FROM " + tabela + " WHERE " + campo + " = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar registro: " + e.getMessage());
            return false;
        }
    }

    // INSERIR
    public void inserir(AndamentoQueixa andamento) {
        if (!existeRegistro("queixas", "id_queixa", andamento.getIdQueixa())) {
            System.err.println("Erro: Queixa não encontrada!");
            return;
        }
        
        if (!existeRegistro("usuarios", "id_usuario", andamento.getIdUsuario())) {
            System.err.println("Erro: Usuário não encontrado!");
            return;
        }

        String sql = "INSERT INTO andamento_queixa (id_queixa, descricao, data_andamento, id_usuario) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, andamento.getIdQueixa());
            stmt.setString(2, andamento.getDescricao());
            stmt.setTimestamp(3, Timestamp.valueOf(andamento.getDataAndamento()));
            stmt.setInt(4, andamento.getIdUsuario());
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        andamento.setIdAndamento(rs.getInt(1));
                        System.out.println("Andamento registrado com ID: " + andamento.getIdAndamento());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir: " + e.getMessage());
        }
    }

    // LISTAR TODOS
    public List<AndamentoQueixa> listarTodos() {
        List<AndamentoQueixa> lista = new ArrayList<>();
        String sql = "SELECT * FROM andamento_queixa ORDER BY data_andamento DESC";
        
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                AndamentoQueixa aq = new AndamentoQueixa(
                    rs.getInt("id_queixa"),
                    rs.getString("descricao"),
                    rs.getInt("id_usuario")
                );
                aq.setIdAndamento(rs.getInt("id_andamento"));
                aq.setDataAndamento(rs.getTimestamp("data_andamento").toLocalDateTime());
                lista.add(aq);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar: " + e.getMessage());
        }
        return lista;
    }

    // BUSCAR POR QUEIXA
    public List<AndamentoQueixa> buscarPorQueixa(int idQueixa) {
        List<AndamentoQueixa> lista = new ArrayList<>();
        String sql = "SELECT * FROM andamento_queixa WHERE id_queixa = ? ORDER BY data_andamento DESC";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idQueixa);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                AndamentoQueixa aq = new AndamentoQueixa(
                    rs.getInt("id_queixa"),
                    rs.getString("descricao"),
                    rs.getInt("id_usuario")
                );
                aq.setIdAndamento(rs.getInt("id_andamento"));
                aq.setDataAndamento(rs.getTimestamp("data_andamento").toLocalDateTime());
                lista.add(aq);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar: " + e.getMessage());
        }
        return lista;
    }

    // MAIN PARA TESTES
    public static void main(String[] args) {
        AndamentoQueixaDAO dao = new AndamentoQueixaDAO();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== MENU ANDAMENTO QUEIXA ===");
            System.out.println("1. Adicionar andamento");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por queixa");
            System.out.println("4. Sair");
            System.out.print("Escolha: ");
            
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 1:
                    System.out.print("ID da Queixa: ");
                    int idQueixa = Integer.parseInt(scanner.nextLine());
                    
                    System.out.print("ID do Usuário: ");
                    int idUsuario = Integer.parseInt(scanner.nextLine());
                    
                    System.out.print("Descrição: ");
                    String descricao = scanner.nextLine();
                    
                    AndamentoQueixa novo = new AndamentoQueixa(idQueixa, descricao, idUsuario);
                    dao.inserir(novo);
                    break;
                    
case 2:
    System.out.println("\nTODOS OS ANDAMENTOS:");
    List<AndamentoQueixa> todosAndamentos = dao.listarTodos();
    for (AndamentoQueixa andamento : todosAndamentos) {
        System.out.println(andamento);
    }
    break;
                    
               case 3:
    System.out.print("ID da Queixa: ");
    int idQ = Integer.parseInt(scanner.nextLine());
    
    System.out.println("\nANDAMENTOS DA QUEIXA #" + idQ + ":");
    List<AndamentoQueixa> andamentosQueixa = dao.buscarPorQueixa(idQ);
    for (AndamentoQueixa andamento : andamentosQueixa) {
        System.out.println(andamento);
    }
    break;
                    
                case 4:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                    
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}