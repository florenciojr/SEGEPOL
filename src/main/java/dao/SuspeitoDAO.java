/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author JR5
 */


import model.Suspeito;
import model.Conexao;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//import javax.swing.JFileChooser;
//import javax.swing.filechooser.FileNameExtensionFilter;

public class SuspeitoDAO {
    private Connection conexao;
    private final String DIRETORIO_UPLOAD = "uploads/suspeitos/";

    public SuspeitoDAO() {
        this.conexao = Conexao.conectar();
        criarDiretorioUpload();
    }

    private void criarDiretorioUpload() {
        File diretorio = new File(DIRETORIO_UPLOAD);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }

    private String copiarImagemParaUpload(String caminhoOriginal) throws IOException {
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

    private String selecionarImagem(Scanner scanner) {
        System.out.println("\n--- INSTRUÇÕES PARA IMAGEM ---");
        System.out.println("1. Digite o caminho completo (ex: C:\\pasta\\foto.jpg)");
        System.out.println("2. OU arraste o arquivo para este terminal");
        System.out.println("3. Formatos aceitos: JPG, PNG");
        System.out.println("0. Cancelar e não adicionar imagem");
        System.out.print("\nInforme o caminho ou 0 para cancelar: ");
        
        String caminho = scanner.nextLine().trim().replace("\"", "");
        
        if (caminho.equals("0")) {
            return null;
        }
        
        try {
            return copiarImagemParaUpload(caminho);
        } catch (IOException e) {
            System.err.println("Erro ao processar imagem: " + e.getMessage());
            return null;
        }
    }

    public boolean existeCidadao(int idCidadao) {
        String sql = "SELECT 1 FROM cidadaos WHERE id_cidadao = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idCidadao);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar cidadão: " + e.getMessage());
            return false;
        }
    }

    public boolean existeQueixa(int idQueixa) {
        String sql = "SELECT 1 FROM queixas WHERE id_queixa = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idQueixa);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar queixa: " + e.getMessage());
            return false;
        }
    }

    public boolean existePorId(int id) {
        String sql = "SELECT 1 FROM suspeitos WHERE id_suspeito = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar suspeito: " + e.getMessage());
            return false;
        }
    }

    public boolean inserir(Suspeito suspeito) {
        if (!existeQueixa(suspeito.getIdQueixa())) {
            System.err.println("Erro: Queixa não encontrada!");
            return false;
        }

        if (suspeito.getIdCidadao() == null) {
            System.err.println("Erro: ID do cidadão é obrigatório!");
            return false;
        }

        if (!existeCidadao(suspeito.getIdCidadao())) {
            System.err.println("Erro: Cidadão não encontrado!");
            return false;
        }

        String sql = "INSERT INTO suspeitos (id_queixa, nome, descricao, genero, data_nascimento, id_cidadao, caminho_imagem, data_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, suspeito.getIdQueixa());
            stmt.setString(2, suspeito.getNome());
            stmt.setString(3, suspeito.getDescricao());
            stmt.setString(4, suspeito.getGenero());
            stmt.setString(5, suspeito.getDataNascimento());
            stmt.setInt(6, suspeito.getIdCidadao());
            stmt.setString(7, suspeito.getCaminhoImagem());
            stmt.setTimestamp(8, new java.sql.Timestamp(suspeito.getDataRegistro().getTime()));
            
            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        suspeito.setIdSuspeito(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir suspeito: " + e.getMessage());
            return false;
        }
    }

    public List<Suspeito> listarTodos() {
        List<Suspeito> lista = new ArrayList<>();
        String sql = "SELECT * FROM suspeitos ORDER BY data_registro DESC";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Suspeito s = new Suspeito();
                s.setIdSuspeito(rs.getInt("id_suspeito"));
                s.setIdQueixa(rs.getInt("id_queixa"));
                s.setNome(rs.getString("nome"));
                s.setDescricao(rs.getString("descricao"));
                s.setGenero(rs.getString("genero"));
                s.setDataNascimento(rs.getString("data_nascimento"));
                s.setIdCidadao(rs.getInt("id_cidadao"));
                s.setCaminhoImagem(rs.getString("caminho_imagem"));
                s.setDataRegistro(rs.getTimestamp("data_registro"));
                lista.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar suspeitos: " + e.getMessage());
        }
        return lista;
    }

    public Suspeito buscarPorId(int id) {
        String sql = "SELECT * FROM suspeitos WHERE id_suspeito = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Suspeito s = new Suspeito();
                    s.setIdSuspeito(rs.getInt("id_suspeito"));
                    s.setIdQueixa(rs.getInt("id_queixa"));
                    s.setNome(rs.getString("nome"));
                    s.setDescricao(rs.getString("descricao"));
                    s.setGenero(rs.getString("genero"));
                    s.setDataNascimento(rs.getString("data_nascimento"));
                    s.setIdCidadao(rs.getInt("id_cidadao"));
                    s.setCaminhoImagem(rs.getString("caminho_imagem"));
                    s.setDataRegistro(rs.getTimestamp("data_registro"));
                    return s;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar suspeito: " + e.getMessage());
        }
        return null;
    }

    public boolean atualizar(Suspeito suspeito) {
        if (!existePorId(suspeito.getIdSuspeito())) {
            System.err.println("Erro: Suspeito não encontrado!");
            return false;
        }

        if (!existeQueixa(suspeito.getIdQueixa())) {
            System.err.println("Erro: Queixa não encontrada!");
            return false;
        }

        if (suspeito.getIdCidadao() == null) {
            System.err.println("Erro: ID do cidadão é obrigatório!");
            return false;
        }

        if (!existeCidadao(suspeito.getIdCidadao())) {
            System.err.println("Erro: Cidadão não encontrado!");
            return false;
        }

        String sql = "UPDATE suspeitos SET id_queixa = ?, nome = ?, descricao = ?, genero = ?, data_nascimento = ?, id_cidadao = ?, caminho_imagem = ? WHERE id_suspeito = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, suspeito.getIdQueixa());
            stmt.setString(2, suspeito.getNome());
            stmt.setString(3, suspeito.getDescricao());
            stmt.setString(4, suspeito.getGenero());
            stmt.setString(5, suspeito.getDataNascimento());
            stmt.setInt(6, suspeito.getIdCidadao());
            stmt.setString(7, suspeito.getCaminhoImagem());
            stmt.setInt(8, suspeito.getIdSuspeito());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar suspeito: " + e.getMessage());
            return false;
        }
    }

    public boolean remover(int id) {
        Suspeito suspeito = buscarPorId(id);
        if (suspeito == null) return false;
        
        String sql = "DELETE FROM suspeitos WHERE id_suspeito = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            boolean sucesso = stmt.executeUpdate() > 0;
            
            if (sucesso && suspeito.getCaminhoImagem() != null) {
                try {
                    Files.deleteIfExists(new File(suspeito.getCaminhoImagem()).toPath());
                } catch (IOException e) {
                    System.err.println("Aviso: Imagem não pôde ser removida: " + e.getMessage());
                }
            }
            return sucesso;
        } catch (SQLException e) {
            System.err.println("Erro ao remover suspeito: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            SuspeitoDAO dao = new SuspeitoDAO();
            
            while (true) {
                System.out.println("\n=== MENU SUSPEITOS ===");
                System.out.println("1. Inserir Suspeito");
                System.out.println("2. Listar Todos");
                System.out.println("3. Buscar por ID");
                System.out.println("4. Atualizar");
                System.out.println("5. Remover");
                System.out.println("6. Visualizar Imagem");
                System.out.println("0. Sair");
                System.out.print("Opção: ");
                
                try {
                    int opcao = Integer.parseInt(scanner.nextLine());
                    
                    switch (opcao) {
                        case 1:
                            inserirSuspeito(dao, scanner);
                            break;
                        case 2:
                            listarSuspeitos(dao);
                            break;
                        case 3:
                            buscarSuspeito(dao, scanner);
                            break;
                        case 4:
                            atualizarSuspeito(dao, scanner);
                            break;
                        case 5:
                            removerSuspeito(dao, scanner);
                            break;
                        case 6:
                            visualizarImagem(dao, scanner);
                            break;
                        case 0:
                            System.out.println("Saindo do sistema...");
                            return;
                        default:
                            System.out.println("Opção inválida!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Erro: Digite um número válido!");
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
        }
    }

    private static void inserirSuspeito(SuspeitoDAO dao, Scanner scanner) {
        System.out.println("\n--- NOVO SUSPEITO ---");
        
        try {
            Suspeito novo = new Suspeito();
            
            System.out.print("ID da Queixa: ");
            novo.setIdQueixa(Integer.parseInt(scanner.nextLine()));
            
            System.out.print("Nome: ");
            novo.setNome(scanner.nextLine());
            
            System.out.print("Descrição: ");
            novo.setDescricao(scanner.nextLine());
            
            System.out.print("Gênero (M/F/Outro): ");
            novo.setGenero(scanner.nextLine());
            
            System.out.print("Data de Nascimento (AAAA-MM-DD): ");
            novo.setDataNascimento(scanner.nextLine());
            
            System.out.print("ID do Cidadão: ");
            novo.setIdCidadao(Integer.parseInt(scanner.nextLine()));
            
            System.out.println("\nDeseja adicionar uma imagem do suspeito? (S/N)");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                String caminhoImagem = dao.selecionarImagem(scanner);
                novo.setCaminhoImagem(caminhoImagem);
            }
            
            if (dao.inserir(novo)) {
                System.out.println("Suspeito cadastrado com sucesso! ID: " + novo.getIdSuspeito());
            } else {
                System.out.println("Falha ao cadastrar suspeito.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void listarSuspeitos(SuspeitoDAO dao) {
        System.out.println("\n--- LISTA DE SUSPEITOS ---");
        List<Suspeito> lista = dao.listarTodos();
        
        if (lista.isEmpty()) {
            System.out.println("Nenhum suspeito cadastrado.");
            return;
        }
        
        System.out.printf("%-5s %-10s %-20s %-15s %-12s %-10s %-20s%n", 
            "ID", "Queixa", "Nome", "Gênero", "Nascimento", "Cidadão", "Imagem");
        System.out.println("--------------------------------------------------------------------------------");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Suspeito s : lista) {
            String nomeImagem = s.getCaminhoImagem() != null ? 
                s.getCaminhoImagem().substring(s.getCaminhoImagem().lastIndexOf("\\") + 1) : "Nenhuma";
            
            System.out.printf("%-5d %-10d %-20s %-15s %-12s %-10d %-20s%n",
                s.getIdSuspeito(),
                s.getIdQueixa(),
                s.getNome().length() > 18 ? s.getNome().substring(0, 15) + "..." : s.getNome(),
                s.getGenero(),
                s.getDataNascimento(),
                s.getIdCidadao(),
                nomeImagem.length() > 18 ? nomeImagem.substring(0, 15) + "..." : nomeImagem);
        }
    }

    private static void buscarSuspeito(SuspeitoDAO dao, Scanner scanner) {
        System.out.println("\n--- BUSCAR SUSPEITO ---");
        System.out.print("ID do Suspeito: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Suspeito s = dao.buscarPorId(id);
            
            if (s != null) {
                System.out.println("\nDetalhes do Suspeito:");
                System.out.println("ID: " + s.getIdSuspeito());
                System.out.println("ID Queixa: " + s.getIdQueixa());
                System.out.println("Nome: " + s.getNome());
                System.out.println("Descrição: " + s.getDescricao());
                System.out.println("Gênero: " + s.getGenero());
                System.out.println("Data Nascimento: " + s.getDataNascimento());
                System.out.println("ID Cidadão: " + s.getIdCidadao());
                System.out.println("Imagem: " + (s.getCaminhoImagem() != null ? s.getCaminhoImagem() : "Nenhuma"));
                System.out.println("Data Registro: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(s.getDataRegistro()));
            } else {
                System.out.println("Suspeito não encontrado!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID deve ser um número!");
        }
    }

    private static void atualizarSuspeito(SuspeitoDAO dao, Scanner scanner) {
        System.out.println("\n--- ATUALIZAR SUSPEITO ---");
        System.out.print("ID do Suspeito: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Suspeito s = dao.buscarPorId(id);
            
            if (s == null) {
                System.out.println("Suspeito não encontrado!");
                return;
            }
            
            System.out.println("\nDados atuais:");
            System.out.println(s);
            
            System.out.print("\nNovo ID da Queixa (" + s.getIdQueixa() + "): ");
            s.setIdQueixa(Integer.parseInt(scanner.nextLine()));
            
            System.out.print("Novo Nome (" + s.getNome() + "): ");
            s.setNome(scanner.nextLine());
            
            System.out.print("Nova Descrição (" + s.getDescricao() + "): ");
            s.setDescricao(scanner.nextLine());
            
            System.out.print("Novo Gênero (" + s.getGenero() + "): ");
            s.setGenero(scanner.nextLine());
            
            System.out.print("Nova Data Nascimento (" + s.getDataNascimento() + "): ");
            s.setDataNascimento(scanner.nextLine());
            
            System.out.print("Novo ID Cidadão (" + s.getIdCidadao() + "): ");
            s.setIdCidadao(Integer.parseInt(scanner.nextLine()));
            
            System.out.print("Deseja alterar a imagem? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                String novaImagem = dao.selecionarImagem(scanner);
                s.setCaminhoImagem(novaImagem);
            }
            
            if (dao.atualizar(s)) {
                System.out.println("Suspeito atualizado com sucesso!");
            } else {
                System.out.println("Falha ao atualizar suspeito!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido!");
        }
    }

    private static void removerSuspeito(SuspeitoDAO dao, Scanner scanner) {
        System.out.println("\n--- REMOVER SUSPEITO ---");
        System.out.print("ID do Suspeito: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Tem certeza que deseja remover? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                if (dao.remover(id)) {
                    System.out.println("Suspeito removido com sucesso!");
                } else {
                    System.out.println("Falha ao remover suspeito!");
                }
            } else {
                System.out.println("Operação cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID deve ser um número!");
        }
    }

    private static void visualizarImagem(SuspeitoDAO dao, Scanner scanner) {
        System.out.println("\n--- VISUALIZAR IMAGEM ---");
        System.out.print("ID do Suspeito: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Suspeito s = dao.buscarPorId(id);
            
            if (s != null && s.getCaminhoImagem() != null) {
                try {
                    File arquivo = new File(s.getCaminhoImagem());
                    if (arquivo.exists()) {
                        java.awt.Desktop.getDesktop().open(arquivo);
                        System.out.println("Imagem aberta com sucesso!");
                    } else {
                        System.out.println("Arquivo de imagem não encontrado no caminho especificado!");
                    }
                } catch (IOException e) {
                    System.out.println("Erro ao abrir imagem: " + e.getMessage());
                }
            } else {
                System.out.println("Suspeito não encontrado ou não possui imagem cadastrada!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID deve ser um número!");
        }
    }
}