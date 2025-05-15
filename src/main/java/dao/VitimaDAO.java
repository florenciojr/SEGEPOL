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
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VitimaDAO {
    // Constantes para nomes de colunas e tabelas
    private static final String TABLE_NAME = "vitimas";
    private static final String COL_ID = "id_vitima";
    private static final String COL_QUEIXA = "id_queixa";
    private static final String COL_CIDADAO = "id_cidadao";
    private static final String COL_DESCRICAO = "descricao";
    private static final String COL_TIPO = "tipo_vitima";
    private static final String COL_DATA_REGISTRO = "data_registro";

    public VitimaDAO() {
        // Construtor padrão
    }

    // CREATE - Cria uma nova vítima com tratamento de transação
    public boolean create(Vitima vitima) throws SQLException {
        String sql = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
            TABLE_NAME, COL_QUEIXA, COL_CIDADAO, COL_DESCRICAO, COL_TIPO, COL_DATA_REGISTRO
        );

        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false); // Inicia transação
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Validações de integridade referencial
            if (!existeQueixa(conn, vitima.getIdQueixa())) {
                throw new SQLException("A queixa informada não existe");
            }

            if (!existeCidadao(conn, vitima.getIdCidadao())) {
                throw new SQLException("O cidadão informado não existe");
            }

            // Verifica se o cidadão já é vítima nesta queixa
            if (isCidadaoVitima(conn, vitima.getIdQueixa(), vitima.getIdCidadao())) {
                throw new SQLException("Este cidadão já está registrado como vítima nesta queixa");
            }

            // Set dos parâmetros
            stmt.setInt(1, vitima.getIdQueixa());
            stmt.setInt(2, vitima.getIdCidadao());
            stmt.setString(3, vitima.getDescricao());
            stmt.setString(4, vitima.getTipoVitima().name());
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        vitima.setIdVitima(rs.getInt(1));
                    }
                }
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
    
    
    
    
    public boolean isCidadaoVitima(int idQueixa, int idCidadao) throws SQLException {
    String sql = "SELECT COUNT(*) FROM vitimas WHERE id_queixa = ? AND id_cidadao = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, idQueixa);
        stmt.setInt(2, idCidadao);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}

    // READ - Busca vítima por ID
    public Vitima read(int id) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, COL_ID);
        
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

    // READ - Lista todas as vítimas com paginação
    public List<Vitima> listAll(int offset, int limit) throws SQLException {
        List<Vitima> vitimas = new ArrayList<>();
        String sql = String.format(
            "SELECT * FROM %s ORDER BY %s DESC LIMIT ? OFFSET ?", 
            TABLE_NAME, COL_DATA_REGISTRO
        );

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vitimas.add(extractVitimaFromResultSet(rs));
                }
            }
        }
        return vitimas;
    }

    // READ - Conta o total de vítimas para paginação
    public int countAll() throws SQLException {
        String sql = String.format("SELECT COUNT(*) FROM %s", TABLE_NAME);

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    // READ - Busca vítimas por queixa com paginação
    public List<Vitima> findByQueixa(int idQueixa, int offset, int limit) throws SQLException {
        List<Vitima> vitimas = new ArrayList<>();
        String sql = String.format(
            "SELECT * FROM %s WHERE %s = ? ORDER BY %s DESC LIMIT ? OFFSET ?", 
            TABLE_NAME, COL_QUEIXA, COL_DATA_REGISTRO
        );

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idQueixa);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vitimas.add(extractVitimaFromResultSet(rs));
                }
            }
        }
        return vitimas;
    }

    // READ - Conta vítimas por queixa para paginação
    public int countByQueixa(int idQueixa) throws SQLException {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", TABLE_NAME, COL_QUEIXA);

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idQueixa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }

    // READ - Busca detalhada com join para dados do cidadão (para exibição em UI)
    public List<Map<String, Object>> findDetailedByQueixa(int idQueixa) throws SQLException {
        List<Map<String, Object>> vitimas = new ArrayList<>();
        String sql = "SELECT v.*, c.nome, c.data_nascimento, c.genero, c.telefone " +
                     "FROM vitimas v JOIN cidadaos c ON v.id_cidadao = c.id_cidadao " +
                     "WHERE v.id_queixa = ? ORDER BY v.data_registro DESC";

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
                    vitima.put("data_nascimento", rs.getDate("data_nascimento"));
                    vitima.put("genero", rs.getString("genero"));
                    vitima.put("telefone", rs.getString("telefone"));
                    vitima.put("descricao", rs.getString(COL_DESCRICAO));
                    vitima.put("tipo_vitima", rs.getString(COL_TIPO));
                    vitima.put("data_registro", rs.getTimestamp(COL_DATA_REGISTRO));
                    vitimas.add(vitima);
                }
            }
        }
        return vitimas;
    }

    // UPDATE - Atualiza uma vítima existente com tratamento de transação
    public boolean update(Vitima vitima) throws SQLException {
        String sql = String.format(
            "UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            TABLE_NAME, COL_QUEIXA, COL_CIDADAO, COL_DESCRICAO, COL_TIPO, COL_ID
        );

        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false);
            
            stmt = conn.prepareStatement(sql);

            // Validações de integridade referencial
            if (!existeQueixa(conn, vitima.getIdQueixa())) {
                throw new SQLException("A queixa informada não existe");
            }

            if (!existeCidadao(conn, vitima.getIdCidadao())) {
                throw new SQLException("O cidadão informado não existe");
            }

            // Verifica se outro registro já tem este cidadão como vítima nesta queixa
            if (isAnotherVitimaWithSameCidadao(conn, vitima.getIdVitima(), vitima.getIdQueixa(), vitima.getIdCidadao())) {
                throw new SQLException("Outro registro já tem este cidadão como vítima nesta queixa");
            }

            stmt.setInt(1, vitima.getIdQueixa());
            stmt.setInt(2, vitima.getIdCidadao());
            stmt.setString(3, vitima.getDescricao());
            stmt.setString(4, vitima.getTipoVitima().name());
            stmt.setInt(5, vitima.getIdVitima());

            boolean result = stmt.executeUpdate() > 0;
            conn.commit();
            return result;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // DELETE - Remove uma vítima com tratamento de transação
    public boolean delete(int id) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s = ?", TABLE_NAME, COL_ID);

        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false);
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            boolean result = stmt.executeUpdate() > 0;
            conn.commit();
            return result;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Métodos auxiliares para UI
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
        vitima.setDataRegistro(rs.getTimestamp(COL_DATA_REGISTRO));
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

    private boolean isCidadaoVitima(Connection conn, int idQueixa, int idCidadao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vitimas WHERE id_queixa = ? AND id_cidadao = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idQueixa);
            stmt.setInt(2, idCidadao);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean isAnotherVitimaWithSameCidadao(Connection conn, int idVitima, int idQueixa, int idCidadao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vitimas WHERE id_queixa = ? AND id_cidadao = ? AND id_vitima != ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idQueixa);
            stmt.setInt(2, idCidadao);
            stmt.setInt(3, idVitima);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Métodos adicionais para relatórios
    public Map<String, Integer> countByTipoVitima() throws SQLException {
        Map<String, Integer> counts = new HashMap<>();
        String sql = String.format("SELECT %s, COUNT(*) as total FROM %s GROUP BY %s", COL_TIPO, TABLE_NAME, COL_TIPO);

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                counts.put(rs.getString(COL_TIPO), rs.getInt("total"));
            }
        }
        return counts;
    }

    public List<Map<String, Object>> findTopCidadaosVitimas(int limit) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT c.id_cidadao, c.nome, COUNT(v.id_vitima) as total " +
                     "FROM vitimas v JOIN cidadaos c ON v.id_cidadao = c.id_cidadao " +
                     "GROUP BY c.id_cidadao, c.nome ORDER BY total DESC LIMIT ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id_cidadao", rs.getInt("id_cidadao"));
                    item.put("nome", rs.getString("nome"));
                    item.put("total", rs.getInt("total"));
                    results.add(item);
                }
            }
        }
        return results;
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
