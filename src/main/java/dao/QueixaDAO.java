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
import model.Queixa;
import model.Conexao;
import java.time.LocalDate;
import java.util.Scanner;


public class QueixaDAO {

    public void inserirQueixa(Queixa queixa) {
        String sql = "INSERT INTO queixas (titulo, descricao, data_registro, status, id_cidadao, id_tipo, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, queixa.getTitulo());
            stmt.setString(2, queixa.getDescricao());
            stmt.setDate(3, Date.valueOf(queixa.getDataRegistro()));
            stmt.setString(4, queixa.getStatus());
            stmt.setInt(5, queixa.getIdCidadao());
            stmt.setInt(6, queixa.getIdTipo());
            stmt.setInt(7, queixa.getIdUsuario());

            stmt.executeUpdate();
            System.out.println("Queixa registrada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Queixa> listarQueixas() {
        List<Queixa> queixas = new ArrayList<>();
        String sql = "SELECT * FROM queixas";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Queixa queixa = new Queixa();
                queixa.setIdQueixa(rs.getInt("id_queixa"));
                queixa.setTitulo(rs.getString("titulo"));
                queixa.setDescricao(rs.getString("descricao"));
                queixa.setDataRegistro(rs.getDate("data_registro").toLocalDate());
                queixa.setStatus(rs.getString("status"));
                queixa.setIdCidadao(rs.getInt("id_cidadao"));
                queixa.setIdTipo(rs.getInt("id_tipo"));
                queixa.setIdUsuario(rs.getInt("id_usuario"));
                queixas.add(queixa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queixas;
    }

    public Queixa buscarQueixaPorId(int id) {
        String sql = "SELECT * FROM queixas WHERE id_queixa = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Queixa queixa = new Queixa();
                queixa.setIdQueixa(rs.getInt("id_queixa"));
                queixa.setTitulo(rs.getString("titulo"));
                queixa.setDescricao(rs.getString("descricao"));
                queixa.setDataRegistro(rs.getDate("data_registro").toLocalDate());
                queixa.setStatus(rs.getString("status"));
                queixa.setIdCidadao(rs.getInt("id_cidadao"));
                queixa.setIdTipo(rs.getInt("id_tipo"));
                queixa.setIdUsuario(rs.getInt("id_usuario"));
                return queixa;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void atualizarQueixa(Queixa queixa) {
        String sql = "UPDATE queixas SET titulo = ?, descricao = ?, data_registro = ?, status = ?, id_cidadao = ?, id_tipo = ?, id_usuario = ? WHERE id_queixa = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, queixa.getTitulo());
            stmt.setString(2, queixa.getDescricao());
            stmt.setDate(3, Date.valueOf(queixa.getDataRegistro()));
            stmt.setString(4, queixa.getStatus());
            stmt.setInt(5, queixa.getIdCidadao());
            stmt.setInt(6, queixa.getIdTipo());
            stmt.setInt(7, queixa.getIdUsuario());
            stmt.setInt(8, queixa.getIdQueixa());

            stmt.executeUpdate();
            System.out.println("Queixa atualizada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarQueixa(int id) {
        String sql = "DELETE FROM queixas WHERE id_queixa = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Queixa deletada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        QueixaDAO queixaDAO = new QueixaDAO();

        while (true) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1. Inserir nova queixa");
            System.out.println("2. Atualizar queixa");
            System.out.println("3. Deletar queixa");
            System.out.println("4. Listar todas as queixas");
            System.out.println("5. Buscar queixa por ID");
            System.out.println("6. Sair");
            int opcao = scanner.nextInt();
            scanner.nextLine();  // Consumir o newline restante

            switch (opcao) {
                case 1:
                    // Inserir nova queixa
                    System.out.println("\nDigite o título da queixa:");
                    String tituloInserir = scanner.nextLine();
                    System.out.println("Digite a descrição da queixa:");
                    String descricaoInserir = scanner.nextLine();
                    System.out.println("Digite a data de registro (YYYY-MM-DD):");
                    String dataInserir = scanner.nextLine();
                    System.out.println("Digite o status da queixa:");
                    String statusInserir = scanner.nextLine();
                    System.out.println("Digite o ID do cidadão:");
                    int idCidadaoInserir = scanner.nextInt();
                    System.out.println("Digite o ID do tipo de queixa:");
                    int idTipoInserir = scanner.nextInt();
                    System.out.println("Digite o ID do usuário:");
                    int idUsuarioInserir = scanner.nextInt();
                    scanner.nextLine(); // Consumir o newline restante

                    Queixa queixaInserir = new Queixa();
                    queixaInserir.setTitulo(tituloInserir);
                    queixaInserir.setDescricao(descricaoInserir);
                    queixaInserir.setDataRegistro(LocalDate.parse(dataInserir));
                    queixaInserir.setStatus(statusInserir);
                    queixaInserir.setIdCidadao(idCidadaoInserir);
                    queixaInserir.setIdTipo(idTipoInserir);
                    queixaInserir.setIdUsuario(idUsuarioInserir);

                    queixaDAO.inserirQueixa(queixaInserir);
                    break;

                case 2:
                    // Atualizar queixa
                    System.out.println("\nDigite o ID da queixa que deseja atualizar:");
                    int idAtualizar = scanner.nextInt();
                    scanner.nextLine();  // Consumir o newline restante
                    System.out.println("Digite o novo título da queixa:");
                    String tituloAtualizar = scanner.nextLine();
                    System.out.println("Digite a nova descrição da queixa:");
                    String descricaoAtualizar = scanner.nextLine();
                    System.out.println("Digite a nova data de registro (YYYY-MM-DD):");
                    String dataAtualizar = scanner.nextLine();
                    System.out.println("Digite o novo status da queixa:");
                    String statusAtualizar = scanner.nextLine();
                    System.out.println("Digite o novo ID do cidadão:");
                    int idCidadaoAtualizar = scanner.nextInt();
                    System.out.println("Digite o novo ID do tipo de queixa:");
                    int idTipoAtualizar = scanner.nextInt();
                    System.out.println("Digite o novo ID do usuário:");
                    int idUsuarioAtualizar = scanner.nextInt();
                    scanner.nextLine(); // Consumir o newline restante

                    Queixa queixaAtualizar = new Queixa();
                    queixaAtualizar.setIdQueixa(idAtualizar);
                    queixaAtualizar.setTitulo(tituloAtualizar);
                    queixaAtualizar.setDescricao(descricaoAtualizar);
                    queixaAtualizar.setDataRegistro(LocalDate.parse(dataAtualizar));
                    queixaAtualizar.setStatus(statusAtualizar);
                    queixaAtualizar.setIdCidadao(idCidadaoAtualizar);
                    queixaAtualizar.setIdTipo(idTipoAtualizar);
                    queixaAtualizar.setIdUsuario(idUsuarioAtualizar);

                    queixaDAO.atualizarQueixa(queixaAtualizar);
                    break;

                case 3:
                    // Deletar queixa
                    System.out.println("\nDigite o ID da queixa que deseja deletar:");
                    int idDeletar = scanner.nextInt();
                    scanner.nextLine();  // Consumir o newline restante

                    queixaDAO.deletarQueixa(idDeletar);
                    break;

                case 4:
                    // Listar todas as queixas
                    List<Queixa> queixas = queixaDAO.listarQueixas();
                    if (queixas.isEmpty()) {
                        System.out.println("Não há queixas registradas.");
                    } else {
                        System.out.println("\nLista de Queixas:");
                        for (Queixa q : queixas) {
                            System.out.println(q);
                        }
                    }
                    break;

                case 5:
                    // Buscar queixa por ID
                    System.out.println("\nDigite o ID da queixa para buscar:");
                    int idBuscar = scanner.nextInt();
                    scanner.nextLine();  // Consumir o newline restante

                    Queixa queixaBuscada = queixaDAO.buscarQueixaPorId(idBuscar);
                    if (queixaBuscada != null) {
                        System.out.println("Queixa encontrada: " + queixaBuscada);
                    } else {
                        System.out.println("Queixa não encontrada.");
                    }
                    break;

                case 6:
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

