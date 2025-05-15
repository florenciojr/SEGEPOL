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
import model.TiposQueixa;
import util.Conexao;
import dao.TiposQueixaDAO;
import java.util.Scanner;

public class TiposQueixaDAO {

    // Inserir novo tipo de queixa
    public boolean inserir(TiposQueixa tipo) {
        String sql = "INSERT INTO tipos_queixa (nome_tipo, descricao) VALUES (?, ?)";

       try (Connection conn = Conexao.conectar(); // Usando a conexão fornecida pela classe Conexao
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.getNomeTipo());
            stmt.setString(2, tipo.getDescricao());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Atualizar tipo de queixa
    public boolean atualizar(TiposQueixa tipo) {
        String sql = "UPDATE tipos_queixa SET nome_tipo = ?, descricao = ? WHERE id_tipo = ?";

       try (Connection conn = Conexao.conectar(); // Usando a conexão fornecida pela classe Conexao
             PreparedStatement stmt = conn.prepareStatement(sql)) {
           
            stmt.setString(1, tipo.getNomeTipo());
            stmt.setString(2, tipo.getDescricao());
            stmt.setInt(3, tipo.getIdTipo());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Deletar tipo de queixa
    public boolean deletar(int idTipo) {
        String sql = "DELETE FROM tipos_queixa WHERE id_tipo = ?";

        try (Connection conn = Conexao.conectar(); // Usando a conexão fornecida pela classe Conexao
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTipo);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Buscar por ID
    public TiposQueixa buscarPorId(int idTipo) {
        String sql = "SELECT * FROM tipos_queixa WHERE id_tipo = ?";
        TiposQueixa tipo = null;

         try (Connection conn = Conexao.conectar(); // Usando a conexão fornecida pela classe Conexao
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, idTipo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tipo = new TiposQueixa();
                tipo.setIdTipo(rs.getInt("id_tipo"));
                tipo.setNomeTipo(rs.getString("nome_tipo"));
                tipo.setDescricao(rs.getString("descricao"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tipo;
    }

    // Listar todos os tipos de queixa
    public List<TiposQueixa> listarTodos() {
        List<TiposQueixa> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipos_queixa";

       try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TiposQueixa tipo = new TiposQueixa();
                tipo.setIdTipo(rs.getInt("id_tipo"));
                tipo.setNomeTipo(rs.getString("nome_tipo"));
                tipo.setDescricao(rs.getString("descricao"));
                lista.add(tipo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }





    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TiposQueixaDAO tiposQueixaDAO = new TiposQueixaDAO();

        while (true) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1. Inserir novo tipo de queixa");
            System.out.println("2. Atualizar tipo de queixa");
            System.out.println("3. Deletar tipo de queixa");
            System.out.println("4. Listar todos os tipos de queixa");
            System.out.println("5. Sair");
            int opcao = scanner.nextInt();
            scanner.nextLine();  // Consumir o newline restante

            switch (opcao) {
                case 1:
                    // Inserir novo tipo de queixa
                    System.out.println("\nDigite o nome do tipo de queixa:");
                    String nomeInserir = scanner.nextLine();
                    System.out.println("Digite a descrição do tipo de queixa:");
                    String descricaoInserir = scanner.nextLine();

                    TiposQueixa tipoInserir = new TiposQueixa();
                    tipoInserir.setNomeTipo(nomeInserir);
                    tipoInserir.setDescricao(descricaoInserir);

                    if (tiposQueixaDAO.inserir(tipoInserir)) {
                        System.out.println("Tipo de queixa inserido com sucesso!");
                    } else {
                        System.out.println("Erro ao inserir tipo de queixa.");
                    }
                    break;

                case 2:
                    // Atualizar tipo de queixa
                    System.out.println("\nDigite o ID do tipo de queixa que deseja atualizar:");
                    int idAtualizar = scanner.nextInt();
                    scanner.nextLine();  // Consumir o newline restante
                    System.out.println("Digite o novo nome do tipo de queixa:");
                    String nomeAtualizar = scanner.nextLine();
                    System.out.println("Digite a nova descrição do tipo de queixa:");
                    String descricaoAtualizar = scanner.nextLine();

                    TiposQueixa tipoAtualizar = new TiposQueixa();
                    tipoAtualizar.setIdTipo(idAtualizar);
                    tipoAtualizar.setNomeTipo(nomeAtualizar);
                    tipoAtualizar.setDescricao(descricaoAtualizar);

                    if (tiposQueixaDAO.atualizar(tipoAtualizar)) {
                        System.out.println("Tipo de queixa atualizado com sucesso!");
                    } else {
                        System.out.println("Erro ao atualizar tipo de queixa.");
                    }
                    break;

                case 3:
                    // Deletar tipo de queixa
                    System.out.println("\nDigite o ID do tipo de queixa que deseja deletar:");
                    int idDeletar = scanner.nextInt();
                    scanner.nextLine();  // Consumir o newline restante

                    if (tiposQueixaDAO.deletar(idDeletar)) {
                        System.out.println("Tipo de queixa deletado com sucesso!");
                    } else {
                        System.out.println("Erro ao deletar tipo de queixa.");
                    }
                    break;

                case 4:
                    // Listar todos os tipos de queixa
                    List<TiposQueixa> tiposQueixa = tiposQueixaDAO.listarTodos();
                    if (tiposQueixa.isEmpty()) {
                        System.out.println("Não há tipos de queixa cadastrados.");
                    } else {
                        System.out.println("\nTipos de Queixa:");
                        for (TiposQueixa tipo : tiposQueixa) {
                            System.out.println("ID: " + tipo.getIdTipo() + " | Nome: " + tipo.getNomeTipo() + " | Descrição: " + tipo.getDescricao());
                        }
                    }
                    break;

                case 5:
                    // Sair
                    System.out.println("Saindo...");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }
}


