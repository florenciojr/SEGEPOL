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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Queixa;
import model.Conexao;

public class QueixaDAO {
    private static final String[] STATUS_PERMITIDOS = {
        "Aberta", "Em Investigação", "Concluída", "Cancelada"
    };

    // Método para verificar se um registro existe
    private boolean existeRegistro(String tabela, String campo, int id) {
        String sql = "SELECT 1 FROM " + tabela + " WHERE " + campo + " = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar existência em " + tabela + ": " + e.getMessage());
            return false;
        }
    }

    // Método para validar o status
    private boolean isStatusValido(String status) {
        if (status == null) return false;
        for (String s : STATUS_PERMITIDOS) {
            if (s.equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }

    // Mostrar status disponíveis
    private void mostrarStatusDisponiveis() {
        System.out.println("\nStatus disponíveis:");
        for (int i = 0; i < STATUS_PERMITIDOS.length; i++) {
            System.out.println((i+1) + " - " + STATUS_PERMITIDOS[i]);
        }
    }

    // Obter status válido do usuário
    private String obterStatusValido(Scanner scanner) {
        mostrarStatusDisponiveis();
        while (true) {
            System.out.print("Escolha o status (número ou nome): ");
            String input = scanner.nextLine().trim();
            
            try {
                int opcao = Integer.parseInt(input);
                if (opcao >= 1 && opcao <= STATUS_PERMITIDOS.length) {
                    return STATUS_PERMITIDOS[opcao-1];
                }
            } catch (NumberFormatException e) {
                // Não é número, verifica se é nome válido
                for (String s : STATUS_PERMITIDOS) {
                    if (s.equalsIgnoreCase(input)) {
                        return s;
                    }
                }
            }
            System.err.println("Status inválido. Escolha uma das opções listadas.");
            mostrarStatusDisponiveis();
        }
    }

    // Inserir queixa com validações
    public boolean inserirQueixa(Queixa queixa) {
        // Validações
        if (queixa.getTitulo() == null || queixa.getTitulo().trim().isEmpty()) {
            System.err.println("Erro: Título da queixa é obrigatório");
            return false;
        }
        
        if (!isStatusValido(queixa.getStatus())) {
            System.err.println("Erro: Status inválido");
            return false;
        }
        
        if (!existeRegistro("cidadaos", "id_cidadao", queixa.getIdCidadao())) {
            System.err.println("Erro: Cidadão não encontrado");
            return false;
        }
        
        if (!existeRegistro("tipos_queixa", "id_tipo", queixa.getIdTipo())) {
            System.err.println("Erro: Tipo de queixa não encontrado");
            return false;
        }
        
        if (!existeRegistro("usuarios", "id_usuario", queixa.getIdUsuario())) {
            System.err.println("Erro: Usuário não encontrado");
            return false;
        }

        String sql = "INSERT INTO queixas (titulo, descricao, data_registro, status, id_cidadao, id_tipo, id_usuario) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, queixa.getTitulo());
            stmt.setString(2, queixa.getDescricao());
            stmt.setDate(3, Date.valueOf(queixa.getDataRegistro()));
            stmt.setString(4, queixa.getStatus());
            stmt.setInt(5, queixa.getIdCidadao());
            stmt.setInt(6, queixa.getIdTipo());
            stmt.setInt(7, queixa.getIdUsuario());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                System.err.println("Erro: Nenhuma linha afetada");
                return false;
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    queixa.setIdQueixa(rs.getInt(1));
                    System.out.println("Queixa registrada com ID: " + queixa.getIdQueixa());
                    return true;
                }
            }
            System.err.println("Erro: Não foi possível obter o ID gerado");
            return false;
        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir queixa: " + e.getMessage());
            return false;
        }
    }

    // Listar todas as queixas
    public List<Queixa> listarQueixas() {
        List<Queixa> queixas = new ArrayList<>();
        String sql = "SELECT * FROM queixas ORDER BY data_registro DESC";

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
            System.err.println("Erro ao listar queixas: " + e.getMessage());
        }
        return queixas;
    }

    // Buscar queixa por ID
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
            System.err.println("Erro ao buscar queixa: " + e.getMessage());
        }
        return null;
    }

    // Atualizar queixa
    public boolean atualizarQueixa(Queixa queixa) {
        if (buscarQueixaPorId(queixa.getIdQueixa()) == null) {
            System.err.println("Erro: Queixa não encontrada");
            return false;
        }
        
        if (!isStatusValido(queixa.getStatus())) {
            System.err.println("Erro: Status inválido");
            return false;
        }

        String sql = "UPDATE queixas SET titulo = ?, descricao = ?, data_registro = ?, " +
                     "status = ?, id_cidadao = ?, id_tipo = ?, id_usuario = ? " +
                     "WHERE id_queixa = ?";

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

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar queixa: " + e.getMessage());
            return false;
        }
    }

    // Deletar queixa
    public boolean deletarQueixa(int id) {
        String sql = "DELETE FROM queixas WHERE id_queixa = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar queixa: " + e.getMessage());
            return false;
        }
    }

    // Métodos utilitários para o menu
    private static LocalDate obterDataValida(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String input = scanner.nextLine().trim();
            
            // Permite digitar sem hífen (YYYYMMDD)
            if (input.matches("\\d{8}")) {
                input = input.substring(0, 4) + "-" + input.substring(4, 6) + "-" + input.substring(6, 8);
            }
            
            try {
                return LocalDate.parse(input);
            } catch (Exception e) {
                System.err.println("Formato inválido. Use YYYY-MM-DD ou YYYYMMDD");
            }
        }
    }

    private static int obterIdValido(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                int id = Integer.parseInt(scanner.nextLine());
                if (id > 0) {
                    return id;
                }
                System.err.println("ID deve ser maior que zero");
            } catch (NumberFormatException e) {
                System.err.println("Digite um número válido");
            }
        }
    }

    // Main interativo
    public static void main(String[] args) {
        QueixaDAO dao = new QueixaDAO();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== MENU QUEIXAS ===");
            System.out.println("1. Inserir nova queixa");
            System.out.println("2. Atualizar queixa");
            System.out.println("3. Deletar queixa");
            System.out.println("4. Listar todas as queixas");
            System.out.println("5. Buscar queixa por ID");
            System.out.println("6. Sair");
            System.out.print("Escolha: ");

            try {
                String input = scanner.nextLine();
                int opcao = Integer.parseInt(input);

                switch (opcao) {
                    case 1:
                        System.out.println("\nNOVA QUEIXA");
                        
                        System.out.print("Título: ");
                        String titulo = scanner.nextLine();
                        
                        System.out.print("Descrição: ");
                        String descricao = scanner.nextLine();
                        
                        LocalDate dataRegistro = obterDataValida(scanner, "Data de registro (YYYY-MM-DD ou YYYYMMDD): ");
                        
                        String status = dao.obterStatusValido(scanner);
                        
                        int idCidadao = obterIdValido(scanner, "ID do Cidadão: ");
                        int idTipo = obterIdValido(scanner, "ID do Tipo de Queixa: ");
                        int idUsuario = obterIdValido(scanner, "ID do Usuário: ");
                        
                        Queixa novaQueixa = new Queixa();
                        novaQueixa.setTitulo(titulo);
                        novaQueixa.setDescricao(descricao);
                        novaQueixa.setDataRegistro(dataRegistro);
                        novaQueixa.setStatus(status);
                        novaQueixa.setIdCidadao(idCidadao);
                        novaQueixa.setIdTipo(idTipo);
                        novaQueixa.setIdUsuario(idUsuario);
                        
                        if (dao.inserirQueixa(novaQueixa)) {
                            System.out.println("Queixa cadastrada com sucesso!");
                        }
                        break;
                        
                    case 2:
                        System.out.println("\nATUALIZAR QUEIXA");
                        int idAtualizar = obterIdValido(scanner, "ID da Queixa: ");
                        
                        Queixa queixaAtual = dao.buscarQueixaPorId(idAtualizar);
                        if (queixaAtual == null) {
                            break;
                        }
                        
                        System.out.println("\nDados atuais:");
                        System.out.println(queixaAtual);
                        
                        System.out.print("\nNovo Título (Enter para manter): ");
                        String novoTitulo = scanner.nextLine();
                        if (!novoTitulo.isEmpty()) {
                            queixaAtual.setTitulo(novoTitulo);
                        }
                        
                        System.out.print("Nova Descrição (Enter para manter): ");
                        String novaDescricao = scanner.nextLine();
                        if (!novaDescricao.isEmpty()) {
                            queixaAtual.setDescricao(novaDescricao);
                        }
                        
                        System.out.print("Nova Data (YYYY-MM-DD ou Enter para manter): ");
                        String novaDataStr = scanner.nextLine();
                        if (!novaDataStr.isEmpty()) {
                            try {
                                queixaAtual.setDataRegistro(LocalDate.parse(novaDataStr));
                            } catch (Exception e) {
                                System.err.println("Formato inválido. Mantendo data anterior.");
                            }
                        }
                        
                        System.out.print("Deseja alterar o status? (S/N): ");
                        if (scanner.nextLine().equalsIgnoreCase("S")) {
                            queixaAtual.setStatus(dao.obterStatusValido(scanner));
                        }
                        
                        System.out.print("Novo ID do Cidadão (Enter para manter): ");
                        String novoCidadao = scanner.nextLine();
                        if (!novoCidadao.isEmpty()) {
                            try {
                                queixaAtual.setIdCidadao(Integer.parseInt(novoCidadao));
                            } catch (NumberFormatException e) {
                                System.err.println("ID inválido. Mantendo valor anterior.");
                            }
                        }
                        
                        if (dao.atualizarQueixa(queixaAtual)) {
                            System.out.println("Queixa atualizada com sucesso!");
                        }
                        break;
                        
                    case 3:
                        System.out.println("\nDELETAR QUEIXA");
                        int idDeletar = obterIdValido(scanner, "ID da Queixa: ");
                        
                        System.out.print("Confirmar exclusão? (S/N): ");
                        if (scanner.nextLine().equalsIgnoreCase("S")) {
                            if (dao.deletarQueixa(idDeletar)) {
                                System.out.println("Queixa deletada com sucesso");
                            }
                        } else {
                            System.out.println("Operação cancelada");
                        }
                        break;
                        
                    case 4:
                        System.out.println("\nLISTA DE QUEIXAS");
                        List<Queixa> queixas = dao.listarQueixas();
                        if (queixas.isEmpty()) {
                            System.out.println("Nenhuma queixa encontrada");
                        } else {
                            System.out.println("Total: " + queixas.size() + " queixa(s)");
                            System.out.println("--------------------------------");
                            for (Queixa q : queixas) {
                                System.out.println(q);
                                System.out.println("--------------------------------");
                            }
                        }
                        break;
                        
                    case 5:
                        System.out.println("\nBUSCAR QUEIXA");
                        int idBuscar = obterIdValido(scanner, "ID da Queixa: ");
                        
                        Queixa queixa = dao.buscarQueixaPorId(idBuscar);
                        if (queixa != null) {
                            System.out.println("\nQueixa encontrada:");
                            System.out.println(queixa);
                        } else {
                            System.out.println("Queixa não encontrada");
                        }
                        break;
                        
                    case 6:
                        System.out.println("Saindo...");
                        scanner.close();
                        System.exit(0);
                        break;
                        
                    default:
                        System.out.println("Opção inválida! Digite de 1 a 6");
                }
            } catch (NumberFormatException e) {
                System.err.println("Erro: Digite apenas números de 1 a 6");
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
            }
        }
    }
}