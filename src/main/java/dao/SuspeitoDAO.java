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
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuspeitoDAO {
    private Connection conexao;

    public SuspeitoDAO() {
        this.conexao = Conexao.conectar();
    }

    // Validações básicas
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

    // Operações CRUD
 public boolean inserir(Suspeito suspeito) {
    // Verificar se a queixa existe
    if (!existeQueixa(suspeito.getIdQueixa())) {
        System.err.println("Erro: Queixa não encontrada!");
        return false;
    }

    // Verificar cidadão apenas se não for null (não identificado)
    if (suspeito.getIdCidadao() != null && !existeCidadao(suspeito.getIdCidadao())) {
        System.err.println("Erro: Cidadão não encontrado!");
        return false;
    }

    String sql = "INSERT INTO suspeitos (id_queixa, id_cidadao, descricao, papel_incidente, data_registro) VALUES (?, ?, ?, ?, ?)";
    
    try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, suspeito.getIdQueixa());
        
        if (suspeito.getIdCidadao() != null) {
            stmt.setInt(2, suspeito.getIdCidadao());
        } else {
            stmt.setNull(2, Types.INTEGER);
        }
        
        stmt.setString(3, suspeito.getDescricao());
        stmt.setString(4, suspeito.getPapelIncidente());
        stmt.setTimestamp(5, new java.sql.Timestamp(suspeito.getDataRegistro().getTime()));
        
        int affectedRows = stmt.executeUpdate();
        
        if (affectedRows > 0) {
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
    String sql = "SELECT s.*, c.nome as cidadaoNome, q.descricao as queixaDescricao " +
                 "FROM suspeitos s " +
                 "LEFT JOIN cidadaos c ON s.id_cidadao = c.id_cidadao " +
                 "LEFT JOIN queixas q ON s.id_queixa = q.id_queixa " +
                 "ORDER BY s.data_registro DESC";
    
    try (PreparedStatement stmt = conexao.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            Suspeito s = resultSetParaSuspeito(rs);
            s.setCidadaoNome(rs.getString("cidadaoNome"));
            s.setQueixaDescricao(rs.getString("queixaDescricao"));
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
                    return resultSetParaSuspeito(rs);
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

        if (suspeito.getIdCidadao() != null && !existeCidadao(suspeito.getIdCidadao())) {
            System.err.println("Erro: Cidadão não encontrado!");
            return false;
        }

        String sql = "UPDATE suspeitos SET id_queixa = ?, id_cidadao = ?, descricao = ?, papel_incidente = ? WHERE id_suspeito = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, suspeito.getIdQueixa());
            
            if (suspeito.getIdCidadao() != null) {
                stmt.setInt(2, suspeito.getIdCidadao());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            stmt.setString(3, suspeito.getDescricao());
            stmt.setString(4, suspeito.getPapelIncidente());
            stmt.setInt(5, suspeito.getIdSuspeito());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar suspeito: " + e.getMessage());
            return false;
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM suspeitos WHERE id_suspeito = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover suspeito: " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar para converter ResultSet em objeto Suspeito
    private Suspeito resultSetParaSuspeito(ResultSet rs) throws SQLException {
        Suspeito s = new Suspeito();
        s.setIdSuspeito(rs.getInt("id_suspeito"));
        s.setIdQueixa(rs.getInt("id_queixa"));
        
        int idCidadao = rs.getInt("id_cidadao");
        if (!rs.wasNull()) {
            s.setIdCidadao(idCidadao);
        } else {
            s.setIdCidadao(null);
        }
        
        s.setDescricao(rs.getString("descricao"));
        s.setPapelIncidente(rs.getString("papel_incidente"));
        s.setDataRegistro(rs.getTimestamp("data_registro"));
        
        return s;
    }

    // Métodos adicionais úteis para o servlet
   public List<Suspeito> buscarPorQueixa(int idQueixa) {
    List<Suspeito> lista = new ArrayList<>();
    String sql = "SELECT s.*, c.nome as cidadaoNome, q.descricao as queixaDescricao " +
                 "FROM suspeitos s " +
                 "LEFT JOIN cidadaos c ON s.id_cidadao = c.id_cidadao " +
                 "LEFT JOIN queixas q ON s.id_queixa = q.id_queixa " +
                 "WHERE s.id_queixa = ? " +
                 "ORDER BY s.data_registro DESC";
    
    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        stmt.setInt(1, idQueixa);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Suspeito s = resultSetParaSuspeito(rs);
                s.setCidadaoNome(rs.getString("cidadaoNome"));
                s.setQueixaDescricao(rs.getString("queixaDescricao"));
                lista.add(s);
            }
        }
    } catch (SQLException e) {
        System.err.println("Erro ao buscar suspeitos por queixa: " + e.getMessage());
    }
    return lista;
}

public List<Map<String, String>> listarCidadaosParaDropdown() {
    List<Map<String, String>> cidadaos = new ArrayList<>();
    String sql = "SELECT id_cidadao, nome FROM cidadaos ORDER BY nome";
    
    try (PreparedStatement stmt = conexao.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            Map<String, String> cidadao = new HashMap<>();
            cidadao.put("id", rs.getString("id_cidadao"));
            cidadao.put("nome", rs.getString("nome"));
            cidadaos.add(cidadao);
        }
    } catch (SQLException e) {
        System.err.println("Erro ao listar cidadãos: " + e.getMessage());
    }
    return cidadaos;
}

public List<Map<String, String>> listarQueixasParaDropdown() {
    List<Map<String, String>> queixas = new ArrayList<>();
    String sql = "SELECT id_queixa, descricao FROM queixas ORDER BY id_queixa DESC";
    
    try (PreparedStatement stmt = conexao.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            Map<String, String> queixa = new HashMap<>();
            queixa.put("id", rs.getString("id_queixa"));
            queixa.put("descricao", rs.getString("descricao"));
            queixas.add(queixa);
        }
    } catch (SQLException e) {
        System.err.println("Erro ao listar queixas: " + e.getMessage());
    }
    return queixas;
}



}
////}
////    public static void main(String[] args) {
////        try (Scanner scanner = new Scanner(System.in)) {
////            SuspeitoDAO dao = new SuspeitoDAO();
////            
////            while (true) {
////                System.out.println("\n=== MENU SUSPEITOS ===");
////                System.out.println("1. Inserir Suspeito");
////                System.out.println("2. Listar Todos");
////                System.out.println("3. Buscar por ID");
////                System.out.println("4. Atualizar");
////                System.out.println("5. Remover");
////                System.out.println("6. Visualizar Imagem");
////                System.out.println("0. Sair");
////                System.out.print("Opção: ");
////                
////                try {
////                    int opcao = Integer.parseInt(scanner.nextLine());
////                    
////                    switch (opcao) {
////                        case 1:
////                            inserirSuspeito(dao, scanner);
////                            break;
////                        case 2:
////                            listarSuspeitos(dao);
////                            break;
////                        case 3:
////                            buscarSuspeito(dao, scanner);
////                            break;
////                        case 4:
////                            atualizarSuspeito(dao, scanner);
////                            break;
////                        case 5:
////                            removerSuspeito(dao, scanner);
////                            break;
////                        case 6:
////                            visualizarImagem(dao, scanner);
////                            break;
////                        case 0:
////                            System.out.println("Saindo do sistema...");
////                            return;
////                        default:
////                            System.out.println("Opção inválida!");
////                    }
////                } catch (NumberFormatException e) {
////                    System.out.println("Erro: Digite um número válido!");
////                } catch (Exception e) {
////                    System.out.println("Erro: " + e.getMessage());
////                }
////            }
////        }
////    }
////
//// private static void inserirSuspeito(SuspeitoDAO dao, Scanner scanner) {
////    System.out.println("\n--- NOVO SUSPEITO ---");
////    
////    try {
////        Suspeito novo = new Suspeito();
////        
////        System.out.print("ID da Queixa: ");
////        novo.setIdQueixa(Integer.parseInt(scanner.nextLine()));
////        
////        System.out.print("Nome: ");
////        String nomeSuspeito = scanner.nextLine();
////        novo.setNome(nomeSuspeito);
////        
////        System.out.print("Descrição: ");
////        novo.setDescricao(scanner.nextLine());
////        
////        System.out.print("Gênero (M/F/Outro): ");
////        novo.setGenero(scanner.nextLine());
////        
////        System.out.print("Data de Nascimento (AAAA-MM-DD): ");
////        novo.setDataNascimento(scanner.nextLine());
////        
////        System.out.print("ID do Cidadão: ");
////        novo.setIdCidadao(Integer.parseInt(scanner.nextLine()));
////        
////        System.out.println("\nDeseja adicionar uma imagem do suspeito? (S/N)");
////        if (scanner.nextLine().equalsIgnoreCase("S")) {
////            String caminhoImagem = dao.selecionarImagem(scanner, nomeSuspeito);
////            novo.setCaminhoImagem(caminhoImagem);
////        }
////        
////        if (dao.inserir(novo)) {
////            System.out.println("Suspeito cadastrado com sucesso! ID: " + novo.getIdSuspeito());
////        } else {
////            System.out.println("Falha ao cadastrar suspeito.");
////        }
////    } catch (NumberFormatException e) {
////        System.out.println("Erro: Valor numérico inválido!");
////    } catch (Exception e) {
////        System.out.println("Erro: " + e.getMessage());
////    }
////}
////
////    private static void listarSuspeitos(SuspeitoDAO dao) {
////        System.out.println("\n--- LISTA DE SUSPEITOS ---");
////        List<Suspeito> lista = dao.listarTodos();
////        
////        if (lista.isEmpty()) {
////            System.out.println("Nenhum suspeito cadastrado.");
////            return;
////        }
////        
////        System.out.printf("%-5s %-10s %-20s %-15s %-12s %-10s %-20s%n", 
////            "ID", "Queixa", "Nome", "Gênero", "Nascimento", "Cidadão", "Imagem");
////        System.out.println("--------------------------------------------------------------------------------");
////        
////        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
////        
////        for (Suspeito s : lista) {
////            String nomeImagem = s.getCaminhoImagem() != null ? 
////                s.getCaminhoImagem().substring(s.getCaminhoImagem().lastIndexOf("\\") + 1) : "Nenhuma";
////            
////            System.out.printf("%-5d %-10d %-20s %-15s %-12s %-10d %-20s%n",
////                s.getIdSuspeito(),
////                s.getIdQueixa(),
////                s.getNome().length() > 18 ? s.getNome().substring(0, 15) + "..." : s.getNome(),
////                s.getGenero(),
////                s.getDataNascimento(),
////                s.getIdCidadao(),
////                nomeImagem.length() > 18 ? nomeImagem.substring(0, 15) + "..." : nomeImagem);
////        }
////    }
////
////    private static void buscarSuspeito(SuspeitoDAO dao, Scanner scanner) {
////        System.out.println("\n--- BUSCAR SUSPEITO ---");
////        System.out.print("ID do Suspeito: ");
////        
////        try {
////            int id = Integer.parseInt(scanner.nextLine());
////            Suspeito s = dao.buscarPorId(id);
////            
////            if (s != null) {
////                System.out.println("\nDetalhes do Suspeito:");
////                System.out.println("ID: " + s.getIdSuspeito());
////                System.out.println("ID Queixa: " + s.getIdQueixa());
////                System.out.println("Nome: " + s.getNome());
////                System.out.println("Descrição: " + s.getDescricao());
////                System.out.println("Gênero: " + s.getGenero());
////                System.out.println("Data Nascimento: " + s.getDataNascimento());
////                System.out.println("ID Cidadão: " + s.getIdCidadao());
////                System.out.println("Imagem: " + (s.getCaminhoImagem() != null ? s.getCaminhoImagem() : "Nenhuma"));
////                System.out.println("Data Registro: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(s.getDataRegistro()));
////            } else {
////                System.out.println("Suspeito não encontrado!");
////            }
////        } catch (NumberFormatException e) {
////            System.out.println("Erro: ID deve ser um número!");
////        }
////    }
////
////private static void atualizarSuspeito(SuspeitoDAO dao, Scanner scanner) {
////    System.out.println("\n--- ATUALIZAR SUSPEITO ---");
////    System.out.print("ID do Suspeito: ");
////    
////    try {
////        int id = Integer.parseInt(scanner.nextLine());
////        Suspeito s = dao.buscarPorId(id);
////        
////        if (s == null) {
////            System.out.println("Suspeito não encontrado!");
////            return;
////        }
////        
////        System.out.println("\nDados atuais:");
////        System.out.println(s);
////        
////        System.out.print("\nNovo ID da Queixa (" + s.getIdQueixa() + "): ");
////        s.setIdQueixa(Integer.parseInt(scanner.nextLine()));
////        
////        System.out.print("Novo Nome (" + s.getNome() + "): ");
////        String novoNome = scanner.nextLine();
////        s.setNome(novoNome);
////        
////        System.out.print("Nova Descrição (" + s.getDescricao() + "): ");
////        s.setDescricao(scanner.nextLine());
////        
////        System.out.print("Novo Gênero (" + s.getGenero() + "): ");
////        s.setGenero(scanner.nextLine());
////        
////        System.out.print("Nova Data Nascimento (" + s.getDataNascimento() + "): ");
////        s.setDataNascimento(scanner.nextLine());
////        
////        System.out.print("Novo ID Cidadão (" + s.getIdCidadao() + "): ");
////        s.setIdCidadao(Integer.parseInt(scanner.nextLine()));
////        
////        System.out.print("Deseja alterar a imagem? (S/N): ");
////        if (scanner.nextLine().equalsIgnoreCase("S")) {
////            String novaImagem = dao.selecionarImagem(scanner, novoNome);
////            s.setCaminhoImagem(novaImagem);
////        }
////        
////        if (dao.atualizar(s)) {
////            System.out.println("Suspeito atualizado com sucesso!");
////        } else {
////            System.out.println("Falha ao atualizar suspeito!");
////        }
////    } catch (NumberFormatException e) {
////        System.out.println("Erro: Valor numérico inválido!");
////    }
////}
////
////    private static void removerSuspeito(SuspeitoDAO dao, Scanner scanner) {
////        System.out.println("\n--- REMOVER SUSPEITO ---");
////        System.out.print("ID do Suspeito: ");
////        
////        try {
////            int id = Integer.parseInt(scanner.nextLine());
////            
////            System.out.print("Tem certeza que deseja remover? (S/N): ");
////            if (scanner.nextLine().equalsIgnoreCase("S")) {
////                if (dao.remover(id)) {
////                    System.out.println("Suspeito removido com sucesso!");
////                } else {
////                    System.out.println("Falha ao remover suspeito!");
////                }
////            } else {
////                System.out.println("Operação cancelada.");
////            }
////        } catch (NumberFormatException e) {
////            System.out.println("Erro: ID deve ser um número!");
////        }
////    }
////
////    private static void visualizarImagem(SuspeitoDAO dao, Scanner scanner) {
////        System.out.println("\n--- VISUALIZAR IMAGEM ---");
////        System.out.print("ID do Suspeito: ");
////        
////        try {
////            int id = Integer.parseInt(scanner.nextLine());
////            Suspeito s = dao.buscarPorId(id);
////            
////            if (s != null && s.getCaminhoImagem() != null) {
////                try {
////                    File arquivo = new File(s.getCaminhoImagem());
////                    if (arquivo.exists()) {
////                        java.awt.Desktop.getDesktop().open(arquivo);
////                        System.out.println("Imagem aberta com sucesso!");
////                    } else {
////                        System.out.println("Arquivo de imagem não encontrado no caminho especificado!");
////                    }
////                } catch (IOException e) {
////                    System.out.println("Erro ao abrir imagem: " + e.getMessage());
////                }
////            } else {
////                System.out.println("Suspeito não encontrado ou não possui imagem cadastrada!");
////            }
////        } catch (NumberFormatException e) {
////            System.out.println("Erro: ID deve ser um número!");
////        }
////    }
////}
