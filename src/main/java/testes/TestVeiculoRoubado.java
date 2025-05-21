///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package testes;
//
//import dao.VeiculoRoubadoDAO;
////import static dao.VeiculoRoubadoDAO.uploadFoto;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Scanner;
//import model.VeiculoRoubado;
//
///**
// *
// * @author JR5
// */
//public class TestVeiculoRoubado {
//    
//
//    public static void main(String[] args) {
//    VeiculoRoubadoDAO dao = new VeiculoRoubadoDAO();
//    Scanner sc = new Scanner(System.in);
//    int opcao;
//    
//    do {
//        System.out.println("\n=== SISTEMA VEÍCULOS ROUBADOS ===");
//        System.out.println("1. Cadastrar veículo");
//        System.out.println("2. Listar todos");
//        System.out.println("3. Buscar por ID");
//        System.out.println("4. Atualizar veículo");
//        System.out.println("5. Remover veículo");
//        System.out.println("6. Buscar por matrícula");
//        System.out.println("0. Sair");
//        System.out.print("Opção: ");
//        
//        try {
//            opcao = Integer.parseInt(sc.nextLine());
//            
//            switch (opcao) {
//                case 1:
//                    cadastrarVeiculo(dao, sc);
//                    break;
//                case 2:
//                    listarVeiculos(dao);
//                    break;
//                case 3:
//                    buscarPorId(dao, sc);
//                    break;
//                case 4:
//                    atualizarVeiculo(dao, sc);
//                    break;
//                case 5:
//                    removerVeiculo(dao, sc);
//                    break;
//                case 6:
//                    buscarPorMatricula(dao, sc);
//                    break;
//                case 0:
//                    System.out.println("Encerrando sistema...");
//                    break;
//                default:
//                    System.out.println("Opção inválida!");
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Digite um número válido!");
//            opcao = -1;  // Força continuar no loop
//        } catch (Exception e) {
//            System.out.println("Erro: " + e.getMessage());
//            opcao = -1;  // Força continuar no loop
//        }
//    } while (opcao != 0);
//    
//    sc.close();
//}
//
//    private static void cadastrarVeiculo(VeiculoRoubadoDAO dao, Scanner sc) throws Exception {
//        System.out.println("\n--- CADASTRAR VEÍCULO ---");
//        VeiculoRoubado v = new VeiculoRoubado();
//        
//        System.out.print("ID Queixa (0 se não houver): ");
//        v.setIdQueixa(Integer.parseInt(sc.nextLine()));
//        
//        System.out.print("Marca: ");
//        v.setMarca(sc.nextLine());
//        
//        System.out.print("Modelo: ");
//        v.setModelo(sc.nextLine());
//        
//        System.out.print("Cor: ");
//        v.setCor(sc.nextLine());
//        
//        System.out.print("Matrícula: ");
//        String matricula = sc.nextLine();
//        v.setMatricula(matricula);
//        
//        System.out.print("Ano (opcional): ");
//        String anoStr = sc.nextLine();
//        v.setAno(anoStr.isEmpty() ? null : Integer.parseInt(anoStr));
//        
//        try {
//            v.setFotoVeiculo(uploadFoto(sc, matricula));
//        } catch (IOException e) {
//            System.out.println("Aviso: " + e.getMessage());
//        }
//        
//        if (dao.inserir(v)) {
//            System.out.println("Veículo cadastrado com sucesso! ID: " + v.getIdVeiculo());
//        } else {
//            System.out.println("Falha ao cadastrar veículo!");
//        }
//    }
//
//    private static void listarVeiculos(VeiculoRoubadoDAO dao) throws SQLException {
//        System.out.println("\n--- VEÍCULOS ROUBADOS ---");
//        List<VeiculoRoubado> lista = dao.listarTodos();
//        
//        if (lista.isEmpty()) {
//            System.out.println("Nenhum veículo encontrado.");
//        } else {
//           for (VeiculoRoubado veiculo : lista) {
//    System.out.println(veiculo);
//}
//        }
//    }
//
//    private static void buscarPorId(VeiculoRoubadoDAO dao, Scanner sc) throws SQLException {
//        System.out.print("\nID do veículo: ");
//        int id = Integer.parseInt(sc.nextLine());
//        
//        VeiculoRoubado v = dao.buscarPorId(id);
//        if (v != null) {
//            exibirDetalhes(v);
//        } else {
//            System.out.println("Veículo não encontrado!");
//        }
//    }
//
//    private static void atualizarVeiculo(VeiculoRoubadoDAO dao, Scanner sc) throws Exception {
//        System.out.print("\nID do veículo para atualizar: ");
//        int id = Integer.parseInt(sc.nextLine());
//        
//        VeiculoRoubado v = dao.buscarPorId(id);
//        if (v == null) {
//            System.out.println("Veículo não encontrado!");
//            return;
//        }
//        
//        System.out.println("\nDeixe em branco para manter o valor atual");
//        
//        System.out.print("Nova marca [" + v.getMarca() + "]: ");
//        String marca = sc.nextLine();
//        if (!marca.isEmpty()) v.setMarca(marca);
//        
//        System.out.print("Novo modelo [" + v.getModelo() + "]: ");
//        v.setModelo(sc.nextLine());
//        
//        System.out.print("Nova cor [" + v.getCor() + "]: ");
//        v.setCor(sc.nextLine());
//        
//        System.out.print("Nova matrícula [" + v.getMatricula() + "]: ");
//        String matricula = sc.nextLine();
//        if (!matricula.isEmpty()) v.setMatricula(matricula);
//        
//        System.out.print("Novo ano [" + v.getAno() + "]: ");
//        String anoStr = sc.nextLine();
//        if (!anoStr.isEmpty()) v.setAno(Integer.parseInt(anoStr));
//        
//        System.out.print("Deseja alterar a foto? (s/n): ");
//        if (sc.nextLine().equalsIgnoreCase("s")) {
//            try {
//                v.setFotoVeiculo(uploadFoto(sc, v.getMatricula()));
//            } catch (IOException e) {
//                System.out.println("Aviso: " + e.getMessage());
//            }
//        }
//        
//        if (dao.atualizar(v)) {
//            System.out.println("Veículo atualizado com sucesso!");
//        } else {
//            System.out.println("Falha ao atualizar veículo!");
//        }
//    }
//
//    private static void removerVeiculo(VeiculoRoubadoDAO dao, Scanner sc) throws SQLException {
//        System.out.print("\nID do veículo para remover: ");
//        int id = Integer.parseInt(sc.nextLine());
//        
//        if (dao.deletar(id)) {
//            System.out.println("Veículo removido com sucesso!");
//        } else {
//            System.out.println("Falha ao remover veículo!");
//        }
//    }
//
//    private static void buscarPorMatricula(VeiculoRoubadoDAO dao, Scanner sc) throws SQLException {
//        System.out.print("\nMatrícula (ou parte): ");
//        String matricula = sc.nextLine();
//        
//        List<VeiculoRoubado> lista = dao.buscarPorMatricula(matricula);
//        
//        if (lista.isEmpty()) {
//            System.out.println("Nenhum veículo encontrado.");
//        } else {
//            System.out.println("\nRESULTADOS:");
//            for (VeiculoRoubado veiculo : lista) {
//            System.out.println(veiculo);
//}
//        }
//    }
//
//    private static void exibirDetalhes(VeiculoRoubado v) {
//        System.out.println("\n--- DETALHES DO VEÍCULO ---");
//        System.out.println("ID: " + v.getIdVeiculo());
//        System.out.println("ID Queixa: " + (v.getIdQueixa() > 0 ? v.getIdQueixa() : "N/A"));
//        System.out.println("Marca: " + v.getMarca());
//        System.out.println("Modelo: " + v.getModelo());
//        System.out.println("Cor: " + v.getCor());
//        System.out.println("Matrícula: " + v.getMatricula());
//        System.out.println("Ano: " + (v.getAno() != null ? v.getAno() : "N/A"));
//        System.out.println("Foto: " + (v.getFotoVeiculo() != null ? v.getFotoVeiculo() : "Nenhuma"));
//        System.out.println("Data Registro: " + v.getDataRegistro());
//    }
//}
