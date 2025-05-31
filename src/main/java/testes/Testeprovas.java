///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package testes;
//
//import dao.ProvaDAO;
//import java.util.List;
//import java.util.Scanner;
//import model.Prova;
//
///**
// *
// * @author JR5
// */
//public class Testeprovas {
//    
//    
//       public static void main(String[] args) {
//        try (Scanner scanner = new Scanner(System.in)) {
//            ProvaDAO dao = new ProvaDAO();
//            
//            while (true) {
//                System.out.println("\n=== MENU PROVAS ===");
//                System.out.println("1. Inserir Prova");
//                System.out.println("2. Listar Todas");
//                System.out.println("3. Buscar por ID");
//                System.out.println("4. Atualizar");
//                System.out.println("5. Remover");
//                System.out.println("0. Sair");
//                System.out.print("Opção: ");
//                
//                try {
//                    int opcao = scanner.nextInt();
//                    scanner.nextLine();
//                    
//                    switch (opcao) {
//                        case 1:
//                            inserirProva(dao, scanner);
//                            break;
//                        case 2:
//                            listarProvas(dao);
//                            break;
//                        case 3:
//                            buscarProva(dao, scanner);
//                            break;
//                        case 4:
//                            atualizarProva(dao, scanner);
//                            break;
//                        case 5:
//                            removerProva(dao, scanner);
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
//    private static void inserirProva(ProvaDAO dao, Scanner scanner) {
//        System.out.println("\n--- NOVA PROVA ---");
//        
//        try {
//            System.out.print("ID da Queixa: ");
//            int idQueixa = scanner.nextInt();
//            scanner.nextLine();
//            
//            System.out.println("Tipo da Prova:");
//            System.out.println("1. Imagem");
//            System.out.println("2. Vídeo");
//            System.out.println("3. Documento");
//            System.out.println("4. Áudio");
//            System.out.print("Escolha (1-4): ");
//            int tipoOpcao = scanner.nextInt();
//            scanner.nextLine();
//            
//            String tipo;
//            switch (tipoOpcao) {
//                case 1: tipo = "Imagem"; break;
//                case 2: tipo = "Vídeo"; break;
//                case 3: tipo = "Documento"; break;
//                case 4: tipo = "Áudio"; break;
//                default: 
//                    System.out.println("Opção inválida! Usando 'Imagem' como padrão.");
//                    tipo = "Imagem";
//            }
//            
//            System.out.print("Descrição: ");
//            String descricao = scanner.nextLine();
//            
//            String caminhoArquivo = dao.selecionarArquivoManual(scanner);
//            if (caminhoArquivo == null) {
//                System.out.println("Operação cancelada pelo usuário.");
//                return;
//            }
//            
//            Prova nova = new Prova();
//            nova.setIdQueixa(idQueixa);
//            nova.setTipo(tipo);
//            nova.setDescricao(descricao);
//            nova.setCaminhoArquivo(caminhoArquivo);
//            
//            if (dao.inserir(nova)) {
//                System.out.println("Prova cadastrada com sucesso! ID: " + nova.getIdProva());
//            } else {
//                System.out.println("Falha ao cadastrar prova.");
//            }
//        } catch (Exception e) {
//            System.out.println("Erro: " + e.getMessage());
//        }
//    }
//
//    private static void listarProvas(ProvaDAO dao) {
//        System.out.println("\n--- LISTA DE PROVAS ---");
//        List<Prova> provas = dao.listarTodas();
//        
//        if (provas.isEmpty()) {
//            System.out.println("Nenhuma prova cadastrada.");
//            return;
//        }
//        
//        System.out.printf("%-5s %-10s %-15s %-30s %-20s%n", 
//            "ID", "Queixa", "Tipo", "Descrição", "Data Upload");
//        System.out.println("------------------------------------------------------------");
//        
//        for (Prova p : provas) {
//            String descricao = p.getDescricao() != null && p.getDescricao().length() > 25 ? 
//                p.getDescricao().substring(0, 25) + "..." : p.getDescricao();
//            String arquivo = p.getCaminhoArquivo() != null ? 
//                p.getCaminhoArquivo().substring(p.getCaminhoArquivo().lastIndexOf("\\") + 1) : "Nenhum";
//            
//            System.out.printf("%-5d %-10d %-15s %-30s %-20s%n",
//                p.getIdProva(),
//                p.getIdQueixa(),
//                p.getTipo(),
//                descricao != null ? descricao : "",
//                new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getDataUpload()));
//        }
//    }
//
//    private static void buscarProva(ProvaDAO dao, Scanner scanner) {
//        System.out.println("\n--- BUSCAR PROVA ---");
//        System.out.print("ID da Prova: ");
//        int id = scanner.nextInt();
//        scanner.nextLine();
//        
//        Prova p = dao.buscarPorId(id);
//        if (p != null) {
//            System.out.println("\nDetalhes da Prova:");
//            System.out.println("ID: " + p.getIdProva());
//            System.out.println("ID Queixa: " + p.getIdQueixa());
//            System.out.println("Tipo: " + p.getTipo());
//            System.out.println("Descrição: " + p.getDescricao());
//            System.out.println("Arquivo: " + p.getCaminhoArquivo());
//            System.out.println("Data Upload: " + 
//                new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(p.getDataUpload()));
//        } else {
//            System.out.println("Prova não encontrada!");
//        }
//    }
//
//    private static void atualizarProva(ProvaDAO dao, Scanner scanner) {
//        System.out.println("\n--- ATUALIZAR PROVA ---");
//        System.out.print("ID da Prova: ");
//        int id = scanner.nextInt();
//        scanner.nextLine();
//        
//        Prova p = dao.buscarPorId(id);
//        if (p == null) {
//            System.out.println("Prova não encontrada!");
//            return;
//        }
//        
//        System.out.println("\nDados atuais:");
//        System.out.println(p);
//        
//        System.out.print("\nNovo ID da Queixa (" + p.getIdQueixa() + "): ");
//        p.setIdQueixa(scanner.nextInt());
//        scanner.nextLine();
//        
//        System.out.println("Novo Tipo:");
//        System.out.println("1. Imagem");
//        System.out.println("2. Vídeo");
//        System.out.println("3. Documento");
//        System.out.println("4. Áudio");
//        System.out.print("Escolha (1-4, 0 para manter atual): ");
//        int tipoOpcao = scanner.nextInt();
//        scanner.nextLine();
//        
//        if (tipoOpcao > 0) {
//            switch (tipoOpcao) {
//                case 1: p.setTipo("Imagem"); break;
//                case 2: p.setTipo("Vídeo"); break;
//                case 3: p.setTipo("Documento"); break;
//                case 4: p.setTipo("Áudio"); break;
//                default: System.out.println("Opção inválida! Mantendo tipo atual.");
//            }
//        }
//        
//        System.out.print("Nova Descrição (" + p.getDescricao() + "): ");
//        String novaDescricao = scanner.nextLine();
//        if (!novaDescricao.isEmpty()) {
//            p.setDescricao(novaDescricao);
//        }
//        
//        System.out.print("Deseja alterar o arquivo? (S/N): ");
//        if (scanner.nextLine().equalsIgnoreCase("S")) {
//            System.out.println("Informe o novo arquivo:");
//            String novoArquivo = dao.selecionarArquivoManual(scanner);
//            if (novoArquivo != null) {
//                p.setCaminhoArquivo(novoArquivo);
//            }
//        }
//        
//        if (dao.atualizar(p)) {
//            System.out.println("Prova atualizada com sucesso!");
//        } else {
//            System.out.println("Falha ao atualizar prova!");
//        }
//    }
//
//    private static void removerProva(ProvaDAO dao, Scanner scanner) {
//        System.out.println("\n--- REMOVER PROVA ---");
//        System.out.print("ID da Prova: ");
//        int id = scanner.nextInt();
//        scanner.nextLine();
//        
//        Prova p = dao.buscarPorId(id);
//        if (p == null) {
//            System.out.println("Prova não encontrada!");
//            return;
//        }
//        
//        System.out.println("\nDados da prova a ser removida:");
//        System.out.println(p);
//        
//        System.out.print("Tem certeza que deseja remover esta prova? (S/N): ");
//        if (scanner.nextLine().equalsIgnoreCase("S")) {
//            if (dao.remover(id)) {
//                System.out.println("Prova removida com sucesso!");
//            } else {
//                System.out.println("Falha ao remover prova!");
//            }
//        } else {
//            System.out.println("Operação cancelada.");
//        }
//    }
//}
//
//    
