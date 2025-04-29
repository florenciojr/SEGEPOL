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
import model.Vitima;
import model.Conexao; // Importando a classe Conexao

public class VitimaDAO {

public VitimaDAO() {
    // Construtor padrão sem parâmetros
}



    // CREATE
    public boolean inserirVitima(Vitima vitima) {
        String sql = "INSERT INTO vitimas (id_queixa, nome, descricao, data_nascimento, genero, id_cidadao) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (!existeQueixa(conn, vitima.getIdQueixa())) {
                System.out.println("Erro: A queixa informada não existe.");
                return false;
            }

            if (!existeCidadao(conn, vitima.getIdCidadao())) {
                System.out.println("Erro: O cidadão informado não existe.");
                return false;
            }

            stmt.setInt(1, vitima.getIdQueixa());
            stmt.setString(2, vitima.getNome());
            stmt.setString(3, vitima.getDescricao());
            stmt.setString(4, vitima.getDataNascimento());
            stmt.setString(5, vitima.getGenero());
            stmt.setInt(6, vitima.getIdCidadao());

            stmt.executeUpdate();
            System.out.println("Vítima inserida com sucesso.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ - Listar todas as vítimas
    public List<Vitima> listarVitimas() {
        List<Vitima> vitimas = new ArrayList<>();
        String sql = "SELECT * FROM vitimas";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vitima vitima = new Vitima();
                vitima.setIdVitima(rs.getInt("id_vitima"));
                vitima.setIdQueixa(rs.getInt("id_queixa"));
                vitima.setNome(rs.getString("nome"));
                vitima.setDescricao(rs.getString("descricao"));
                vitima.setDataNascimento(rs.getString("data_nascimento"));
                vitima.setGenero(rs.getString("genero"));
                vitima.setIdCidadao(rs.getInt("id_cidadao"));
                vitimas.add(vitima);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vitimas;
    }

    // READ - Buscar vítima por ID
    public Vitima buscarVitimaPorId(int id) {
        String sql = "SELECT * FROM vitimas WHERE id_vitima = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Vitima vitima = new Vitima();
                vitima.setIdVitima(rs.getInt("id_vitima"));
                vitima.setIdQueixa(rs.getInt("id_queixa"));
                vitima.setNome(rs.getString("nome"));
                vitima.setDescricao(rs.getString("descricao"));
                vitima.setDataNascimento(rs.getString("data_nascimento"));
                vitima.setGenero(rs.getString("genero"));
                vitima.setIdCidadao(rs.getInt("id_cidadao"));
                return vitima;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public void atualizarVitima(Vitima vitima) {
        String sql = "UPDATE vitimas SET id_queixa = ?, nome = ?, descricao = ?, data_nascimento = ?, genero = ?, id_cidadao = ? WHERE id_vitima = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vitima.getIdQueixa());
            stmt.setString(2, vitima.getNome());
            stmt.setString(3, vitima.getDescricao());
            stmt.setString(4, vitima.getDataNascimento());
            stmt.setString(5, vitima.getGenero());
            stmt.setInt(6, vitima.getIdCidadao());
            stmt.setInt(7, vitima.getIdVitima());

            stmt.executeUpdate();
            System.out.println("Vítima atualizada com sucesso.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletarVitima(int id) {
        String sql = "DELETE FROM vitimas WHERE id_vitima = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Vítima deletada com sucesso.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Verificar se a queixa existe
    private boolean existeQueixa(Connection conn, int idQueixa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM queixas WHERE id_queixa = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idQueixa);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    // Verificar se o cidadão existe
    private boolean existeCidadao(Connection conn, int idCidadao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cidadaos WHERE id_cidadao = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idCidadao);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    // Main interativo
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VitimaDAO vitimaDAO = new VitimaDAO(); // CERTO

        while (true) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1. Inserir nova vítima");
            System.out.println("2. Listar todas as vítimas");
            System.out.println("3. Buscar vítima por ID");
            System.out.println("4. Atualizar vítima");
            System.out.println("5. Deletar vítima");
            System.out.println("6. Sair");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir newline

            switch (opcao) {
                case 1:
                    System.out.print("ID da queixa: ");
                    int idQueixa = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nome da vítima: ");
                    String nome = scanner.nextLine();
                    System.out.print("Descrição: ");
                    String descricao = scanner.nextLine();
                    System.out.print("Data de nascimento (YYYY-MM-DD): ");
                    String dataNascimento = scanner.nextLine();
                    System.out.print("Gênero: ");
                    String genero = scanner.nextLine();
                    System.out.print("ID do cidadão: ");
                    int idCidadao = scanner.nextInt();

                    Vitima novaVitima = new Vitima(idQueixa, nome, descricao, dataNascimento, genero, idCidadao);
                    vitimaDAO.inserirVitima(novaVitima);
                    break;

                case 2:
                    List<Vitima> vitimas = vitimaDAO.listarVitimas();
                    for (Vitima v : vitimas) {
                        System.out.println(v);
                    }
                    break;

                case 3:
                    System.out.print("ID da vítima: ");
                    int idBuscar = scanner.nextInt();
                    Vitima vitima = vitimaDAO.buscarVitimaPorId(idBuscar);
                    if (vitima != null) {
                        System.out.println(vitima);
                    } else {
                        System.out.println("Vítima não encontrada.");
                    }
                    break;

                case 4:
                    System.out.print("ID da vítima para atualizar: ");
                    int idAtualizar = scanner.nextInt();
                    scanner.nextLine();
                    Vitima vitimaAtualizar = vitimaDAO.buscarVitimaPorId(idAtualizar);

                    if (vitimaAtualizar != null) {
                        System.out.print("Novo nome (deixe vazio para não alterar): ");
                        String novoNome = scanner.nextLine();
                        if (!novoNome.isEmpty()) vitimaAtualizar.setNome(novoNome);

                        System.out.print("Nova descrição (deixe vazio para não alterar): ");
                        String novaDescricao = scanner.nextLine();
                        if (!novaDescricao.isEmpty()) vitimaAtualizar.setDescricao(novaDescricao);

                        System.out.print("Nova data de nascimento (deixe vazio para não alterar): ");
                        String novaData = scanner.nextLine();
                        if (!novaData.isEmpty()) vitimaAtualizar.setDataNascimento(novaData);

                        System.out.print("Novo gênero (deixe vazio para não alterar): ");
                        String novoGenero = scanner.nextLine();
                        if (!novoGenero.isEmpty()) vitimaAtualizar.setGenero(novoGenero);

                        vitimaDAO.atualizarVitima(vitimaAtualizar);
                    } else {
                        System.out.println("Vítima não encontrada.");
                    }
                    break;

                case 5:
                    System.out.print("ID da vítima para deletar: ");
                    int idDeletar = scanner.nextInt();
                    vitimaDAO.deletarVitima(idDeletar);
                    break;

                case 6:
                    System.out.println("Saindo...");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}
