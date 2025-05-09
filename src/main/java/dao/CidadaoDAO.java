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
import model.Cidadao;
import model.Conexao;
import java.util.Scanner;



public class CidadaoDAO {

    public void inserirCidadao(Cidadao cidadao) {
        String sql = "INSERT INTO cidadaos (nome, genero, data_nascimento, documento_Identificacao, telefone, email, naturalidade, rua, bairro, cidade, provincia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar(); // Usando a conexão fornecida pela classe Conexao
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cidadao.getNome());
            stmt.setString(2, cidadao.getGenero());
            stmt.setDate(3, Date.valueOf(cidadao.getDataNascimento()));
            stmt.setString(4, cidadao.getDocumentoIdentificacao());
            stmt.setString(5, cidadao.getTelefone());
            stmt.setString(6, cidadao.getEmail());
            stmt.setString(7, cidadao.getNaturalidade());
            stmt.setString(8, cidadao.getRua());
            stmt.setString(9, cidadao.getBairro());
            stmt.setString(10, cidadao.getCidade());
            stmt.setString(11, cidadao.getProvincia());

            stmt.executeUpdate();
            System.out.println("Cidadão inserido com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cidadao> listarCidadaos() {
        List<Cidadao> cidadaos = new ArrayList<>();
        String sql = "SELECT * FROM cidadaos";

        try (Connection conn = Conexao.conectar(); // Usando a conexão fornecida pela classe Conexao
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cidadao cidadao = new Cidadao();
                cidadao.setIdCidadao(rs.getInt("id_Cidadao"));
                cidadao.setNome(rs.getString("nome"));
                cidadao.setGenero(rs.getString("genero"));
                cidadao.setDataNascimento(rs.getDate("data_Nascimento").toLocalDate());
                cidadao.setDocumentoIdentificacao(rs.getString("documento_Identificacao"));
                cidadao.setTelefone(rs.getString("telefone"));
                cidadao.setEmail(rs.getString("email"));
                cidadao.setNaturalidade(rs.getString("naturalidade"));
                cidadao.setRua(rs.getString("rua"));
                cidadao.setBairro(rs.getString("bairro"));
                cidadao.setCidade(rs.getString("cidade"));
                cidadao.setProvincia(rs.getString("provincia"));
                cidadaos.add(cidadao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cidadaos;
    }

    public Cidadao buscarCidadaoPorId(int id) {
        String sql = "SELECT * FROM cidadaos WHERE id_cidadao = ?";
        
        
        try (Connection conn = Conexao.conectar(); // Usando a conexão fornecida pela classe Conexao
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cidadao cidadao = new Cidadao();
                cidadao.setIdCidadao(rs.getInt("id_cidadao"));
                cidadao.setNome(rs.getString("nome"));
                cidadao.setGenero(rs.getString("genero"));
                cidadao.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                cidadao.setDocumentoIdentificacao(rs.getString("documento_identificacao"));
                cidadao.setTelefone(rs.getString("telefone"));
                cidadao.setEmail(rs.getString("email"));
                cidadao.setNaturalidade(rs.getString("naturalidade"));
                cidadao.setRua(rs.getString("rua"));
                cidadao.setBairro(rs.getString("bairro"));
                cidadao.setCidade(rs.getString("cidade"));
                cidadao.setProvincia(rs.getString("provincia"));
                return cidadao;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Cidadao buscarCidadaoPorDocumento(String documentoIdentificacao) {
    String sql = "SELECT * FROM cidadaos WHERE documento_identificacao = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, documentoIdentificacao);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Cidadao cidadao = new Cidadao();
            cidadao.setIdCidadao(rs.getInt("id_cidadao"));
            cidadao.setNome(rs.getString("nome"));
            cidadao.setGenero(rs.getString("genero"));
            cidadao.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
            cidadao.setDocumentoIdentificacao(rs.getString("documento_identificacao"));
            cidadao.setTelefone(rs.getString("telefone"));
            cidadao.setEmail(rs.getString("email"));
            cidadao.setNaturalidade(rs.getString("naturalidade"));
            cidadao.setRua(rs.getString("rua"));
            cidadao.setBairro(rs.getString("bairro"));
            cidadao.setCidade(rs.getString("cidade"));
            cidadao.setProvincia(rs.getString("provincia"));
            return cidadao;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    public void atualizarCidadao(Cidadao cidadao) {
      String sql = "UPDATE cidadaos SET nome = ?, genero = ?, data_nascimento = ?, documento_identificacao = ?, telefone = ?, email = ?, naturalidade = ?, rua = ?, bairro = ?, cidade = ?, provincia = ? WHERE id_cidadao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cidadao.getNome());
            stmt.setString(2, cidadao.getGenero());
            stmt.setDate(3, Date.valueOf(cidadao.getDataNascimento()));
            stmt.setString(4, cidadao.getDocumentoIdentificacao());
            stmt.setString(5, cidadao.getTelefone());
            stmt.setString(6, cidadao.getEmail());
            stmt.setString(7, cidadao.getNaturalidade());
            stmt.setString(8, cidadao.getRua());
            stmt.setString(9, cidadao.getBairro());
            stmt.setString(10, cidadao.getCidade());
            stmt.setString(11, cidadao.getProvincia());
            stmt.setInt(12, cidadao.getIdCidadao());

            stmt.executeUpdate();
            System.out.println("Cidadão atualizado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarCidadao(int id) {
        String sql = "DELETE FROM cidadaos WHERE id_cidadao = ?";

        try (Connection conn = Conexao.conectar(); // Usando a conexão fornecida pela classe Conexao
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Cidadão deletado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    


 public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CidadaoDAO cidadaoDAO = new CidadaoDAO();

        while (true) {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Inserir Cidadão");
            System.out.println("2. Listar Cidadãos");
            System.out.println("3. Buscar Cidadão por ID");
            System.out.println("4. Atualizar Cidadão");
            System.out.println("5. Deletar Cidadão");
            System.out.println("6. Sair");
            System.out.println("7. Buscar Cidadão por BI");

            System.out.print("Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine();  // Limpar o buffer

            switch (opcao) {
                case 1:
                    // Inserir Cidadão
                    Cidadao cidadao = new Cidadao();

                    System.out.print("Nome: ");
                    cidadao.setNome(scanner.nextLine());

                    System.out.print("Gênero: ");
                    cidadao.setGenero(scanner.nextLine());

                    System.out.print("Data de Nascimento (yyyy-MM-dd): ");
                    cidadao.setDataNascimento(java.time.LocalDate.parse(scanner.nextLine()));

                    System.out.print("Documento de Identificação: ");
                    cidadao.setDocumentoIdentificacao(scanner.nextLine());

                    System.out.print("Telefone: ");
                    cidadao.setTelefone(scanner.nextLine());

                    System.out.print("Email: ");
                    cidadao.setEmail(scanner.nextLine());

                    System.out.print("Naturalidade: ");
                    cidadao.setNaturalidade(scanner.nextLine());

                    System.out.print("Rua: ");
                    cidadao.setRua(scanner.nextLine());

                    System.out.print("Bairro: ");
                    cidadao.setBairro(scanner.nextLine());

                    System.out.print("Cidade: ");
                    cidadao.setCidade(scanner.nextLine());

                    System.out.print("Província: ");
                    cidadao.setProvincia(scanner.nextLine());

                    cidadaoDAO.inserirCidadao(cidadao);
                    break;

                case 2:
                    // Listar Cidadãos
                    List<Cidadao> cidadaos = cidadaoDAO.listarCidadaos();
                    System.out.println("\n=== Cidadãos Cadastrados ===");
                    for (Cidadao c : cidadaos) {
                        System.out.println("ID: " + c.getIdCidadao() + " | Nome: " + c.getNome() + " | Gênero: " + c.getGenero());
                    }
                    break;
                    
                    
                   case 7:
    // Buscar Cidadão por BI
    System.out.print("Digite o número do BI do cidadão: ");
    String documentoIdentificacao = scanner.nextLine();

    Cidadao cidadaoPorBI = cidadaoDAO.buscarCidadaoPorDocumento(documentoIdentificacao);
    if (cidadaoPorBI != null) {
        System.out.println("\n=== Cidadão Encontrado ===");
        System.out.println("ID: " + cidadaoPorBI.getIdCidadao());
        System.out.println("Nome: " + cidadaoPorBI.getNome());
        System.out.println("Gênero: " + cidadaoPorBI.getGenero());
        System.out.println("Data de Nascimento: " + cidadaoPorBI.getDataNascimento());
        System.out.println("Documento de Identificação: " + cidadaoPorBI.getDocumentoIdentificacao());
        System.out.println("Telefone: " + cidadaoPorBI.getTelefone());
        System.out.println("Email: " + cidadaoPorBI.getEmail());
        System.out.println("Naturalidade: " + cidadaoPorBI.getNaturalidade());
        System.out.println("Rua: " + cidadaoPorBI.getRua());
        System.out.println("Bairro: " + cidadaoPorBI.getBairro());
        System.out.println("Cidade: " + cidadaoPorBI.getCidade());
        System.out.println("Província: " + cidadaoPorBI.getProvincia());
    } else {
        System.out.println("Cidadão não encontrado pelo BI informado.");
    }
    break;

                    

                case 3:
                    // Buscar Cidadão por ID
                    System.out.print("Digite o ID do cidadão: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();  // Limpar o buffer

                    Cidadao cidadaoBuscado = cidadaoDAO.buscarCidadaoPorId(id);
                    if (cidadaoBuscado != null) {
                        System.out.println("\n=== Cidadão Encontrado ===");
                        System.out.println("ID: " + cidadaoBuscado.getIdCidadao());
                        System.out.println("Nome: " + cidadaoBuscado.getNome());
                        System.out.println("Gênero: " + cidadaoBuscado.getGenero());
                        System.out.println("Data de Nascimento: " + cidadaoBuscado.getDataNascimento());
                        System.out.println("Documento de Identificação: " + cidadaoBuscado.getDocumentoIdentificacao());
                        System.out.println("Telefone: " + cidadaoBuscado.getTelefone());
                        System.out.println("Email: " + cidadaoBuscado.getEmail());
                        System.out.println("Naturalidade: " + cidadaoBuscado.getNaturalidade());
                        System.out.println("Rua: " + cidadaoBuscado.getRua());
                        System.out.println("Bairro: " + cidadaoBuscado.getBairro());
                        System.out.println("Cidade: " + cidadaoBuscado.getCidade());
                        System.out.println("Província: " + cidadaoBuscado.getProvincia());
                    } else {
                        System.out.println("Cidadão não encontrado.");
                    }
                    break;

                case 4:
                    // Atualizar Cidadão
                    System.out.print("Digite o ID do cidadão para atualizar: ");
                    int idAtualizar = scanner.nextInt();
                    scanner.nextLine();  // Limpar o buffer

                    Cidadao cidadaoAtualizado = cidadaoDAO.buscarCidadaoPorId(idAtualizar);

                    if (cidadaoAtualizado != null) {
                        System.out.println("Atualizando informações de: " + cidadaoAtualizado.getNome());

                        System.out.print("Novo Nome: ");
                        cidadaoAtualizado.setNome(scanner.nextLine());

                        System.out.print("Novo Gênero: ");
                        cidadaoAtualizado.setGenero(scanner.nextLine());

                        System.out.print("Nova Data de Nascimento (yyyy-MM-dd): ");
                        cidadaoAtualizado.setDataNascimento(java.time.LocalDate.parse(scanner.nextLine()));

                        System.out.print("Novo Documento de Identificação: ");
                        cidadaoAtualizado.setDocumentoIdentificacao(scanner.nextLine());

                        System.out.print("Novo Telefone: ");
                        cidadaoAtualizado.setTelefone(scanner.nextLine());

                        System.out.print("Novo Email: ");
                        cidadaoAtualizado.setEmail(scanner.nextLine());

                        System.out.print("Nova Naturalidade: ");
                        cidadaoAtualizado.setNaturalidade(scanner.nextLine());

                        System.out.print("Nova Rua: ");
                        cidadaoAtualizado.setRua(scanner.nextLine());

                        System.out.print("Novo Bairro: ");
                        cidadaoAtualizado.setBairro(scanner.nextLine());

                        System.out.print("Nova Cidade: ");
                        cidadaoAtualizado.setCidade(scanner.nextLine());

                        System.out.print("Nova Província: ");
                        cidadaoAtualizado.setProvincia(scanner.nextLine());

                        cidadaoDAO.atualizarCidadao(cidadaoAtualizado);
                    } else {
                        System.out.println("Cidadão não encontrado para atualizar.");
                    }
                    break;

                case 5:
                    // Deletar Cidadão
                    System.out.print("Digite o ID do cidadão para deletar: ");
                    int idDeletar = scanner.nextInt();
                    scanner.nextLine();  // Limpar o buffer

                    cidadaoDAO.deletarCidadao(idDeletar);
                    break;

                case 6:
                    // Sair
                    System.out.println("Saindo...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }
}









// USAR NO FINAL OS MAIN PARA CRIAR OS SERVLETS

