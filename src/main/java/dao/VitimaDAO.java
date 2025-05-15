/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author JR5
 */





import model.Vitima;
import model.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VitimaDAO {
    // Constantes para nomes de colunas
    private static final String COL_ID = "id_vitima";
    private static final String COL_QUEIXA = "id_queixa";
    private static final String COL_CIDADAO = "id_cidadao";
    private static final String COL_DESCRICAO = "descricao";
    private static final String COL_TIPO = "tipo_vitima";

    public VitimaDAO() {
        // Construtor padrão
    }

    // CREATE - Cria uma nova vítima
    public boolean create(Vitima vitima) throws SQLException {
        String sql = String.format(
            "INSERT INTO vitimas (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
            COL_QUEIXA, COL_CIDADAO, COL_DESCRICAO, COL_TIPO
        );

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Validações de integridade referencial
            if (!existeQueixa(conn, vitima.getIdQueixa())) {
                throw new SQLException("A queixa informada não existe");
            }

            if (!existeCidadao(conn, vitima.getIdCidadao())) {
                throw new SQLException("O cidadão informado não existe");
            }

            // Set dos parâmetros
            stmt.setInt(1, vitima.getIdQueixa());
            stmt.setInt(2, vitima.getIdCidadao());
            stmt.setString(3, vitima.getDescricao());
            stmt.setString(4, vitima.getTipoVitima().name());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        vitima.setIdVitima(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    // READ - Busca vítima por ID
    public Vitima read(int id) throws SQLException {
        String sql = String.format("SELECT * FROM vitimas WHERE %s = ?", COL_ID);
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractVitimaFromResultSet(rs);
                }
            }
        }
        return null;
    }

    // READ - Lista todas as vítimas
    public List<Vitima> listAll() throws SQLException {
        List<Vitima> vitimas = new ArrayList<>();
        String sql = String.format("SELECT * FROM vitimas ORDER BY %s DESC", COL_ID);

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vitimas.add(extractVitimaFromResultSet(rs));
            }
        }
        return vitimas;
    }

    // READ - Busca vítimas por queixa
    public List<Vitima> findByQueixa(int idQueixa) throws SQLException {
        List<Vitima> vitimas = new ArrayList<>();
        String sql = String.format("SELECT * FROM vitimas WHERE %s = ? ORDER BY %s DESC", COL_QUEIXA, COL_ID);

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idQueixa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vitimas.add(extractVitimaFromResultSet(rs));
                }
            }
        }
        return vitimas;
    }

    // READ - Busca detalhada com join para dados do cidadão
    public List<Map<String, Object>> findDetailedByQueixa(int idQueixa) throws SQLException {
        List<Map<String, Object>> vitimas = new ArrayList<>();
        String sql = "SELECT v.*, c.nome, c.data_nascimento, c.genero " +
                     "FROM vitimas v JOIN cidadaos c ON v.id_cidadao = c.id_cidadao " +
                     "WHERE v.id_queixa = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idQueixa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> vitima = new HashMap<>();
                    vitima.put("id_vitima", rs.getInt(COL_ID));
                    vitima.put("id_queixa", rs.getInt(COL_QUEIXA));
                    vitima.put("id_cidadao", rs.getInt(COL_CIDADAO));
                    vitima.put("nome", rs.getString("nome"));
                    vitima.put("data_nascimento", rs.getString("data_nascimento"));
                    vitima.put("genero", rs.getString("genero"));
                    vitima.put("descricao", rs.getString(COL_DESCRICAO));
                    vitima.put("tipo_vitima", rs.getString(COL_TIPO));
                    vitimas.add(vitima);
                }
            }
        }
        return vitimas;
    }

    // UPDATE - Atualiza uma vítima existente
    public boolean update(Vitima vitima) throws SQLException {
        String sql = String.format(
            "UPDATE vitimas SET %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            COL_QUEIXA, COL_CIDADAO, COL_DESCRICAO, COL_TIPO, COL_ID
        );

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vitima.getIdQueixa());
            stmt.setInt(2, vitima.getIdCidadao());
            stmt.setString(3, vitima.getDescricao());
            stmt.setString(4, vitima.getTipoVitima().name());
            stmt.setInt(5, vitima.getIdVitima());

            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE - Remove uma vítima
    public boolean delete(int id) throws SQLException {
        String sql = String.format("DELETE FROM vitimas WHERE %s = ?", COL_ID);

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Métodos auxiliares para dropdowns
    public List<Map<String, String>> listarQueixasParaDropdown() throws SQLException {
        List<Map<String, String>> queixas = new ArrayList<>();
        String sql = "SELECT id_queixa, descricao FROM queixas ORDER BY data_registro DESC";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, String> queixa = new HashMap<>();
                queixa.put("key", String.valueOf(rs.getInt("id_queixa")));
                queixa.put("value", rs.getString("descricao"));
                queixas.add(queixa);
            }
        }
        return queixas;
    }

    public List<Map<String, String>> listarCidadaosParaDropdown() throws SQLException {
        List<Map<String, String>> cidadaos = new ArrayList<>();
        String sql = "SELECT id_cidadao, nome FROM cidadaos ORDER BY nome";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, String> cidadao = new HashMap<>();
                cidadao.put("key", String.valueOf(rs.getInt("id_cidadao")));
                cidadao.put("value", rs.getString("nome"));
                cidadaos.add(cidadao);
            }
        }
        return cidadaos;
    }

    // Método auxiliar para extrair Vitima do ResultSet
    private Vitima extractVitimaFromResultSet(ResultSet rs) throws SQLException {
        Vitima vitima = new Vitima();
        vitima.setIdVitima(rs.getInt(COL_ID));
        vitima.setIdQueixa(rs.getInt(COL_QUEIXA));
        vitima.setIdCidadao(rs.getInt(COL_CIDADAO));
        vitima.setDescricao(rs.getString(COL_DESCRICAO));
        vitima.setTipoVitima(Vitima.TipoVitima.valueOf(rs.getString(COL_TIPO)));
        return vitima;
    }

    // VALIDATION METHODS
    private boolean existeQueixa(Connection conn, int idQueixa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM queixas WHERE id_queixa = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idQueixa);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean existeCidadao(Connection conn, int idCidadao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cidadaos WHERE id_cidadao = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCidadao);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Método para verificar se um cidadão já é vítima de uma queixa específica
    public boolean isCidadaoVitima(int idQueixa, int idCidadao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vitimas WHERE id_queixa = ? AND id_cidadao = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idQueixa);
            stmt.setInt(2, idCidadao);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}

//    // Main interativo
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        VitimaDAO vitimaDAO = new VitimaDAO(); // CERTO
//
//        while (true) {
//            System.out.println("\nEscolha uma opção:");
//            System.out.println("1. Inserir nova vítima");
//            System.out.println("2. Listar todas as vítimas");
//            System.out.println("3. Buscar vítima por ID");
//            System.out.println("4. Atualizar vítima");
//            System.out.println("5. Deletar vítima");
//            System.out.println("6. Sair");
//
//            int opcao = scanner.nextInt();
//            scanner.nextLine(); // Consumir newline
//
//            switch (opcao) {
//                case 1:
//                    System.out.print("ID da queixa: ");
//                    int idQueixa = scanner.nextInt();
//                    scanner.nextLine();
//                    System.out.print("Nome da vítima: ");
//                    String nome = scanner.nextLine();
//                    System.out.print("Descrição: ");
//                    String descricao = scanner.nextLine();
//                    System.out.print("Data de nascimento (YYYY-MM-DD): ");
//                    String dataNascimento = scanner.nextLine();
//                    System.out.print("Gênero: ");
//                    String genero = scanner.nextLine();
//                    System.out.print("ID do cidadão: ");
//                    int idCidadao = scanner.nextInt();
//
//                    Vitima novaVitima = new Vitima(idQueixa, nome, descricao, dataNascimento, genero, idCidadao);
//                    vitimaDAO.inserirVitima(novaVitima);
//                    break;
//
//                case 2:
//                    List<Vitima> vitimas = vitimaDAO.listarVitimas();
//                    for (Vitima v : vitimas) {
//                        System.out.println(v);
//                    }
//                    break;
//
//                case 3:
//                    System.out.print("ID da vítima: ");
//                    int idBuscar = scanner.nextInt();
//                    Vitima vitima = vitimaDAO.buscarVitimaPorId(idBuscar);
//                    if (vitima != null) {
//                        System.out.println(vitima);
//                    } else {
//                        System.out.println("Vítima não encontrada.");
//                    }
//                    break;
//
//                case 4:
//                    System.out.print("ID da vítima para atualizar: ");
//                    int idAtualizar = scanner.nextInt();
//                    scanner.nextLine();
//                    Vitima vitimaAtualizar = vitimaDAO.buscarVitimaPorId(idAtualizar);
//
//                    if (vitimaAtualizar != null) {
//                        System.out.print("Novo nome (deixe vazio para não alterar): ");
//                        String novoNome = scanner.nextLine();
//                        if (!novoNome.isEmpty()) vitimaAtualizar.setNome(novoNome);
//
//                        System.out.print("Nova descrição (deixe vazio para não alterar): ");
//                        String novaDescricao = scanner.nextLine();
//                        if (!novaDescricao.isEmpty()) vitimaAtualizar.setDescricao(novaDescricao);
//
//                        System.out.print("Nova data de nascimento (deixe vazio para não alterar): ");
//                        String novaData = scanner.nextLine();
//                        if (!novaData.isEmpty()) vitimaAtualizar.setDataNascimento(novaData);
//
//                        System.out.print("Novo gênero (deixe vazio para não alterar): ");
//                        String novoGenero = scanner.nextLine();
//                        if (!novoGenero.isEmpty()) vitimaAtualizar.setGenero(novoGenero);
//
//                        vitimaDAO.atualizarVitima(vitimaAtualizar);
//                    } else {
//                        System.out.println("Vítima não encontrada.");
//                    }
//                    break;
//
//                case 5:
//                    System.out.print("ID da vítima para deletar: ");
//                    int idDeletar = scanner.nextInt();
//                    vitimaDAO.deletarVitima(idDeletar);
//                    break;
//
//                case 6:
//                    System.out.println("Saindo...");
//                    scanner.close();
//                    System.exit(0);
//                    break;
//
//                default:
//                    System.out.println("Opção inválida!");
//            }
//        }
//    }
//}
