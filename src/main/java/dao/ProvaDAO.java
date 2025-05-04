/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author JR5
 */

import model.Prova;
import model.Conexao;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProvaDAO {
    private Connection conexao;
    private final String DIRETORIO_UPLOAD = "uploads/provas/";

    public ProvaDAO() {
        this.conexao = Conexao.conectar();
        criarDiretorioUpload();
    }

    private void criarDiretorioUpload() {
        File diretorio = new File(DIRETORIO_UPLOAD);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }

    private String copiarArquivoParaUpload(String caminhoOriginal) throws IOException {
        File arquivoOriginal = new File(caminhoOriginal);
        if (!arquivoOriginal.exists()) {
            throw new IOException("Arquivo não encontrado: " + caminhoOriginal);
        }

        String nomeArquivo = arquivoOriginal.getName();
        File destino = new File(DIRETORIO_UPLOAD + nomeArquivo);
        
        int counter = 1;
        while (destino.exists()) {
            int dotIndex = nomeArquivo.lastIndexOf('.');
            String nomeSemExt = dotIndex > 0 ? nomeArquivo.substring(0, dotIndex) : nomeArquivo;
            String ext = dotIndex > 0 ? nomeArquivo.substring(dotIndex) : "";
            destino = new File(DIRETORIO_UPLOAD + nomeSemExt + "_" + counter + ext);
            counter++;
        }
        
        Files.copy(arquivoOriginal.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return destino.getAbsolutePath();
    }

    private String selecionarArquivoManual(Scanner scanner) {
        System.out.println("\n--- INSTRUÇÕES PARA ARQUIVO ---");
        System.out.println("1. Digite o caminho completo (ex: C:\\pasta\\arquivo.jpg)");
        System.out.println("2. OU arraste o arquivo para este terminal");
        System.out.println("0. Cancelar operação");
        System.out.print("\nInforme o caminho ou 0 para cancelar: ");
        
        String caminho = scanner.nextLine().trim().replace("\"", "");
        
        if (caminho.equals("0")) {
            return null;
        }
        
        if (caminho.isEmpty() || caminho.matches("^\\d+$")) {
            System.err.println("Erro: Você deve informar um caminho válido!");
            return selecionarArquivoManual(scanner);
        }
        
        try {
            return copiarArquivoParaUpload(caminho);
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
            return selecionarArquivoManual(scanner);
        }
    }

    private boolean existeQueixa(int idQueixa) {
        String sql = "SELECT 1 FROM queixas WHERE id_queixa = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idQueixa);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar queixa: " + e.getMessage());
            return false;
        }
    }

    public boolean inserir(Prova prova) {
        if (!existeQueixa(prova.getIdQueixa())) {
            System.err.println("Erro: Queixa não encontrada!");
            return false;
        }

        String sql = "INSERT INTO provas (id_queixa, tipo, descricao, caminho_arquivo) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, prova.getIdQueixa());
            stmt.setString(2, prova.getTipo());
            stmt.setString(3, prova.getDescricao());
            stmt.setString(4, prova.getCaminhoArquivo());
            
            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        prova.setIdProva(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir prova: " + e.getMessage());
            return false;
        }
    }

    public List<Prova> listarTodas() {
        List<Prova> provas = new ArrayList<>();
        String sql = "SELECT * FROM provas ORDER BY data_upload DESC";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Prova p = new Prova();
                p.setIdProva(rs.getInt("id_prova"));
                p.setIdQueixa(rs.getInt("id_queixa"));
                p.setTipo(rs.getString("tipo"));
                p.setDescricao(rs.getString("descricao"));
                p.setCaminhoArquivo(rs.getString("caminho_arquivo"));
                p.setDataUpload(rs.getTimestamp("data_upload"));
                provas.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar provas: " + e.getMessage());
        }
        return provas;
    }

    public Prova buscarPorId(int id) {
        String sql = "SELECT * FROM provas WHERE id_prova = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Prova p = new Prova();
                    p.setIdProva(rs.getInt("id_prova"));
                    p.setIdQueixa(rs.getInt("id_queixa"));
                    p.setTipo(rs.getString("tipo"));
                    p.setDescricao(rs.getString("descricao"));
                    p.setCaminhoArquivo(rs.getString("caminho_arquivo"));
                    p.setDataUpload(rs.getTimestamp("data_upload"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar prova: " + e.getMessage());
        }
        return null;
    }

    public boolean atualizar(Prova prova) {
        String sql = "UPDATE provas SET id_queixa = ?, tipo = ?, descricao = ?, caminho_arquivo = ? WHERE id_prova = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, prova.getIdQueixa());
            stmt.setString(2, prova.getTipo());
            stmt.setString(3, prova.getDescricao());
            stmt.setString(4, prova.getCaminhoArquivo());
            stmt.setInt(5, prova.getIdProva());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar prova: " + e.getMessage());
            return false;
        }
    }

    public boolean remover(int id) {
        Prova prova = buscarPorId(id);
        if (prova == null) return false;
        
        String sql = "DELETE FROM provas WHERE id_prova = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            boolean sucesso = stmt.executeUpdate() > 0;
            
            if (sucesso && prova.getCaminhoArquivo() != null) {
                try {
                    Files.deleteIfExists(new File(prova.getCaminhoArquivo()).toPath());
                } catch (IOException e) {
                    System.err.println("Aviso: Arquivo não pôde ser removido: " + e.getMessage());
                }
            }
            return sucesso;
        } catch (SQLException e) {
            System.err.println("Erro ao remover prova: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ProvaDAO dao = new ProvaDAO();
            
            while (true) {
                System.out.println("\n=== MENU PROVAS ===");
                System.out.println("1. Inserir Prova");
                System.out.println("2. Listar Todas");
                System.out.println("3. Buscar por ID");
                System.out.println("4. Atualizar");
                System.out.println("5. Remover");
                System.out.println("0. Sair");
                System.out.print("Opção: ");
                
                try {
                    int opcao = scanner.nextInt();
                    scanner.nextLine();
                    
                    switch (opcao) {
                        case 1:
                            inserirProva(dao, scanner);
                            break;
                        case 2:
                            listarProvas(dao);
                            break;
                        case 3:
                            buscarProva(dao, scanner);
                            break;
                        case 4:
                            atualizarProva(dao, scanner);
                            break;
                        case 5:
                            removerProva(dao, scanner);
                            break;
                        case 0:
                            System.out.println("Saindo do sistema...");
                            return;
                        default:
                            System.out.println("Opção inválida!");
                    }
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                    scanner.nextLine();
                }
            }
        }
    }

    private static void inserirProva(ProvaDAO dao, Scanner scanner) {
        System.out.println("\n--- NOVA PROVA ---");
        
        try {
            System.out.print("ID da Queixa: ");
            int idQueixa = scanner.nextInt();
            scanner.nextLine();
            
            System.out.println("Tipo da Prova:");
            System.out.println("1. Imagem");
            System.out.println("2. Vídeo");
            System.out.println("3. Documento");
            System.out.println("4. Áudio");
            System.out.print("Escolha (1-4): ");
            int tipoOpcao = scanner.nextInt();
            scanner.nextLine();
            
            String tipo;
            switch (tipoOpcao) {
                case 1: tipo = "Imagem"; break;
                case 2: tipo = "Vídeo"; break;
                case 3: tipo = "Documento"; break;
                case 4: tipo = "Áudio"; break;
                default: 
                    System.out.println("Opção inválida! Usando 'Imagem' como padrão.");
                    tipo = "Imagem";
            }
            
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();
            
            String caminhoArquivo = dao.selecionarArquivoManual(scanner);
            if (caminhoArquivo == null) {
                System.out.println("Operação cancelada pelo usuário.");
                return;
            }
            
            Prova nova = new Prova();
            nova.setIdQueixa(idQueixa);
            nova.setTipo(tipo);
            nova.setDescricao(descricao);
            nova.setCaminhoArquivo(caminhoArquivo);
            
            if (dao.inserir(nova)) {
                System.out.println("Prova cadastrada com sucesso! ID: " + nova.getIdProva());
            } else {
                System.out.println("Falha ao cadastrar prova.");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void listarProvas(ProvaDAO dao) {
        System.out.println("\n--- LISTA DE PROVAS ---");
        List<Prova> provas = dao.listarTodas();
        
        if (provas.isEmpty()) {
            System.out.println("Nenhuma prova cadastrada.");
            return;
        }
        
        System.out.printf("%-5s %-10s %-15s %-30s %-20s%n", 
            "ID", "Queixa", "Tipo", "Descrição", "Data Upload");
        System.out.println("------------------------------------------------------------");
        
        for (Prova p : provas) {
            String descricao = p.getDescricao() != null && p.getDescricao().length() > 25 ? 
                p.getDescricao().substring(0, 25) + "..." : p.getDescricao();
            String arquivo = p.getCaminhoArquivo() != null ? 
                p.getCaminhoArquivo().substring(p.getCaminhoArquivo().lastIndexOf("\\") + 1) : "Nenhum";
            
            System.out.printf("%-5d %-10d %-15s %-30s %-20s%n",
                p.getIdProva(),
                p.getIdQueixa(),
                p.getTipo(),
                descricao != null ? descricao : "",
                new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getDataUpload()));
        }
    }

    private static void buscarProva(ProvaDAO dao, Scanner scanner) {
        System.out.println("\n--- BUSCAR PROVA ---");
        System.out.print("ID da Prova: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Prova p = dao.buscarPorId(id);
        if (p != null) {
            System.out.println("\nDetalhes da Prova:");
            System.out.println("ID: " + p.getIdProva());
            System.out.println("ID Queixa: " + p.getIdQueixa());
            System.out.println("Tipo: " + p.getTipo());
            System.out.println("Descrição: " + p.getDescricao());
            System.out.println("Arquivo: " + p.getCaminhoArquivo());
            System.out.println("Data Upload: " + 
                new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(p.getDataUpload()));
        } else {
            System.out.println("Prova não encontrada!");
        }
    }

    private static void atualizarProva(ProvaDAO dao, Scanner scanner) {
        System.out.println("\n--- ATUALIZAR PROVA ---");
        System.out.print("ID da Prova: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Prova p = dao.buscarPorId(id);
        if (p == null) {
            System.out.println("Prova não encontrada!");
            return;
        }
        
        System.out.println("\nDados atuais:");
        System.out.println(p);
        
        System.out.print("\nNovo ID da Queixa (" + p.getIdQueixa() + "): ");
        p.setIdQueixa(scanner.nextInt());
        scanner.nextLine();
        
        System.out.println("Novo Tipo:");
        System.out.println("1. Imagem");
        System.out.println("2. Vídeo");
        System.out.println("3. Documento");
        System.out.println("4. Áudio");
        System.out.print("Escolha (1-4, 0 para manter atual): ");
        int tipoOpcao = scanner.nextInt();
        scanner.nextLine();
        
        if (tipoOpcao > 0) {
            switch (tipoOpcao) {
                case 1: p.setTipo("Imagem"); break;
                case 2: p.setTipo("Vídeo"); break;
                case 3: p.setTipo("Documento"); break;
                case 4: p.setTipo("Áudio"); break;
                default: System.out.println("Opção inválida! Mantendo tipo atual.");
            }
        }
        
        System.out.print("Nova Descrição (" + p.getDescricao() + "): ");
        String novaDescricao = scanner.nextLine();
        if (!novaDescricao.isEmpty()) {
            p.setDescricao(novaDescricao);
        }
        
        System.out.print("Deseja alterar o arquivo? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            System.out.println("Informe o novo arquivo:");
            String novoArquivo = dao.selecionarArquivoManual(scanner);
            if (novoArquivo != null) {
                p.setCaminhoArquivo(novoArquivo);
            }
        }
        
        if (dao.atualizar(p)) {
            System.out.println("Prova atualizada com sucesso!");
        } else {
            System.out.println("Falha ao atualizar prova!");
        }
    }

    private static void removerProva(ProvaDAO dao, Scanner scanner) {
        System.out.println("\n--- REMOVER PROVA ---");
        System.out.print("ID da Prova: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Prova p = dao.buscarPorId(id);
        if (p == null) {
            System.out.println("Prova não encontrada!");
            return;
        }
        
        System.out.println("\nDados da prova a ser removida:");
        System.out.println(p);
        
        System.out.print("Tem certeza que deseja remover esta prova? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            if (dao.remover(id)) {
                System.out.println("Prova removida com sucesso!");
            } else {
                System.out.println("Falha ao remover prova!");
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}