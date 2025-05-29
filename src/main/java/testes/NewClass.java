///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package testes;
//
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//import model.MandadoPrisao;
//
///**
// *
// * @author JR5
// */
//public class NewClass 
//        
//    
// public static void main(String[] args) {
//        try (Scanner scanner = new Scanner(System.in)) {
//            MandadoPrisaoDAO dao = new MandadoPrisaoDAO();
//            
//            while (true) {
//                System.out.println("\n=== MENU MANDADOS PRISÃO ===");
//                System.out.println("1. Inserir Mandado");
//                System.out.println("2. Listar Todos");
//                System.out.println("3. Buscar por ID");
//                System.out.println("4. Atualizar");
//                System.out.println("5. Remover");
//                System.out.println("6. Listar por Status");
//                System.out.println("0. Sair");
//                System.out.print("Opção: ");
//                
//                try {
//                    int opcao = scanner.nextInt();
//                    scanner.nextLine();
//                    
//                    switch (opcao) {
//                        case 1:
//                            inserirMandado(dao, scanner);
//                            break;
//                        case 2:
//                            listarMandados(dao);
//                            break;
//                        case 3:
//                            buscarMandado(dao, scanner);
//                            break;
//                        case 4:
//                            atualizarMandado(dao, scanner);
//                            break;
//                        case 5:
//                            removerMandado(dao, scanner);
//                            break;
//                        case 6:
//                            listarPorStatus(dao, scanner);
//                            break;
//                        case 0:
//                            System.out.println("Saindo do sistema...");
//                            return;
//                        default:
//                            System.out.println("Opção inválida!");
//                    }
//                } catch (Exception e) {
//                    System.out.println("Erro: " + e.getMessage());
//                    scanner.nextLine();
//                }
//            }
//        }
//    }
//
//    private static void inserirMandado(MandadoPrisaoDAO dao, Scanner scanner) {
//        System.out.println("\n--- NOVO MANDADO DE PRISÃO ---");
//        
//        try {
//            System.out.print("ID do Suspeito: ");
//            int idSuspeito = scanner.nextInt();
//            scanner.nextLine();
//            
//            System.out.print("Número do Mandado: ");
//            String numeroMandado = scanner.nextLine();
//            
//            System.out.print("Data de Emissão (AAAA-MM-DD): ");
//            String dataStr = scanner.nextLine();
//            Date dataEmissao = Date.valueOf(dataStr);
//            
//            System.out.println("Status:");
//            exibirOpcoesStatus();
//            System.out.print("Escolha (1-3): ");
//            int statusOpcao = scanner.nextInt();
//            scanner.nextLine();
//            String status = obterStatusPorOpcao(statusOpcao);
//            
//            MandadoPrisao novo = new MandadoPrisao();
//            novo.setIdSuspeito(idSuspeito);
//            novo.setNumeroMandado(numeroMandado);
//            novo.setDataEmissao(dataEmissao);
//            novo.setStatus(status);
//            
//            if (dao.inserir(novo)) {
//                System.out.println("Mandado cadastrado com sucesso! ID: " + novo.getIdMandado());
//            } else {
//                System.out.println("Falha ao cadastrar mandado.");
//            }
//        } catch (Exception e) {
//            System.out.println("Erro: " + e.getMessage());
//        }
//    }
//
//    private static void listarMandados(MandadoPrisaoDAO dao) {
//        System.out.println("\n--- LISTA DE MANDADOS DE PRISÃO ---");
//        List<MandadoPrisao> mandados = dao.listarTodos();
//        
//        if (mandados.isEmpty()) {
//            System.out.println("Nenhum mandado cadastrado.");
//            return;
//        }
//        
//        System.out.printf("%-5s %-10s %-20s %-15s %-15s%n", 
//            "ID", "Suspeito", "Número Mandado", "Data Emissão", "Status");
//        System.out.println("------------------------------------------------------------");
//        
//        for (MandadoPrisao m : mandados) {
//            System.out.printf("%-5d %-10d %-20s %-15s %-15s%n",
//                m.getIdMandado(),
//                m.getIdSuspeito(),
//                m.getNumeroMandado(),
//                m.getDataEmissao(),
//                m.getStatus());
//        }
//    }
//
//    private static void buscarMandado(MandadoPrisaoDAO dao, Scanner scanner) {
//        System.out.println("\n--- BUSCAR MANDADO ---");
//        System.out.print("ID do Mandado: ");
//        int id = scanner.nextInt();
//        scanner.nextLine();
//        
//        MandadoPrisao m = dao.buscarPorId(id);
//        if (m != null) {
//            System.out.println("\nDetalhes do Mandado:");
//            System.out.println("ID: " + m.getIdMandado());
//            System.out.println("ID Suspeito: " + m.getIdSuspeito());
//            System.out.println("Número Mandado: " + m.getNumeroMandado());
//            System.out.println("Data Emissão: " + m.getDataEmissao());
//            System.out.println("Status: " + m.getStatus());
//        } else {
//            System.out.println("Mandado não encontrado!");
//        }
//    }
//
//    private static void atualizarMandado(MandadoPrisaoDAO dao, Scanner scanner) {
//        System.out.println("\n--- ATUALIZAR MANDADO ---");
//        System.out.print("ID do Mandado: ");
//        int id = scanner.nextInt();
//        scanner.nextLine();
//        
//        MandadoPrisao m = dao.buscarPorId(id);
//        if (m == null) {
//            System.out.println("Mandado não encontrado!");
//            return;
//        }
//        
//        System.out.println("\nDados atuais:");
//        System.out.println(m);
//        
//        System.out.print("\nNovo ID do Suspeito (" + m.getIdSuspeito() + "): ");
//        String novoIdSuspeito = scanner.nextLine();
//        if (!novoIdSuspeito.isEmpty()) {
//            m.setIdSuspeito(Integer.parseInt(novoIdSuspeito));
//        }
//        
//        System.out.print("Novo Número do Mandado (" + m.getNumeroMandado() + "): ");
//        String novoNumero = scanner.nextLine();
//        if (!novoNumero.isEmpty()) {
//            m.setNumeroMandado(novoNumero);
//        }
//        
//        System.out.print("Nova Data de Emissão (" + m.getDataEmissao() + ") [deixe em branco para manter]: ");
//        String novaDataStr = scanner.nextLine();
//        if (!novaDataStr.isEmpty()) {
//            try {
//                m.setDataEmissao(Date.valueOf(novaDataStr));
//            } catch (Exception e) {
//                System.out.println("Formato de data inválido! Mantendo data atual.");
//            }
//        }
//        
//        System.out.println("Novo Status (" + m.getStatus() + "):");
//        exibirOpcoesStatus();
//        System.out.print("Escolha (1-3, 0 para manter): ");
//        int statusOpcao = scanner.nextInt();
//        scanner.nextLine();
//        if (statusOpcao > 0) {
//            m.setStatus(obterStatusPorOpcao(statusOpcao));
//        }
//        
//        if (dao.atualizar(m)) {
//            System.out.println("Mandado atualizado com sucesso!");
//        } else {
//            System.out.println("Falha ao atualizar mandado!");
//        }
//    }
//
//    private static void removerMandado(MandadoPrisaoDAO dao, Scanner scanner) {
//        System.out.println("\n--- REMOVER MANDADO ---");
//        System.out.print("ID do Mandado: ");
//        int id = scanner.nextInt();
//        scanner.nextLine();
//        
//        MandadoPrisao m = dao.buscarPorId(id);
//        if (m == null) {
//            System.out.println("Mandado não encontrado!");
//            return;
//        }
//        
//        System.out.println("\nDados do mandado a ser removido:");
//        System.out.println(m);
//        
//        System.out.print("Tem certeza que deseja remover este mandado? (S/N): ");
//        if (scanner.nextLine().equalsIgnoreCase("S")) {
//            if (dao.remover(id)) {
//                System.out.println("Mandado removido com sucesso!");
//            } else {
//                System.out.println("Falha ao remover mandado!");
//            }
//        } else {
//            System.out.println("Operação cancelada.");
//        }
//    }
//
//    private static void listarPorStatus(MandadoPrisaoDAO dao, Scanner scanner) {
//        System.out.println("\n--- LISTAR POR STATUS ---");
//        System.out.println("Selecione o status:");
//        exibirOpcoesStatus();
//        System.out.print("Escolha (1-3): ");
//        int statusOpcao = scanner.nextInt();
//        scanner.nextLine();
//        
//        String status = obterStatusPorOpcao(statusOpcao);
//        List<MandadoPrisao> mandados = dao.listarPorStatus(status);
//        
//        if (mandados.isEmpty()) {
//            System.out.println("Nenhum mandado com status '" + status + "' encontrado.");
//            return;
//        }
//        
//        System.out.println("\n--- MANDADOS COM STATUS: " + status + " ---");
//        System.out.printf("%-5s %-10s %-20s %-15s%n", 
//            "ID", "Suspeito", "Número Mandado", "Data Emissão");
//        System.out.println("------------------------------------------------");
//        
//        for (MandadoPrisao m : mandados) {
//            System.out.printf("%-5d %-10d %-20s %-15s%n",
//                m.getIdMandado(),
//                m.getIdSuspeito(),
//                m.getNumeroMandado(),
//                m.getDataEmissao());
//        }
//    }
//
//    public List<MandadoPrisao> listarPorStatus(String status) {
//        List<MandadoPrisao> mandados = new ArrayList<>();
//        String sql = "SELECT * FROM mandados_prisao WHERE status = ? ORDER BY data_emissao DESC";
//        
//        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
//            stmt.setString(1, status);
//            
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    MandadoPrisao m = new MandadoPrisao();
//                    m.setIdMandado(rs.getInt("id_mandado"));
//                    m.setIdSuspeito(rs.getInt("id_suspeito"));
//                    m.setNumeroMandado(rs.getString("numero_mandado"));
//                    m.setDataEmissao(rs.getDate("data_emissao"));
//                    m.setStatus(rs.getString("status"));
//                    mandados.add(m);
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Erro ao listar mandados por status: " + e.getMessage());
//        }
//        return mandados;
//    }
//}
