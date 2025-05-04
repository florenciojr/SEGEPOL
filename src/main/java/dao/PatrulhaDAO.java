/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author JR5
 */



import model.Patrulha;
import model.Conexao;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PatrulhaDAO {
    
    // Método para criar a tabela com todas as colunas necessárias
    public void criarTabela() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS patrulhas (" +
                     "id_patrulha INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                     "nome VARCHAR(50) NOT NULL, " +
                     "responsavel_id INTEGER NOT NULL, " +
                     "membros TEXT, " +  // Armazenará IDs dos membros separados por vírgula
                     "data DATE NOT NULL, " +
                     "hora_inicio TIME NOT NULL, " +
                     "hora_fim TIME, " +
                     "tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('Ronda', 'Operação', 'Especial')), " +
                     "observacoes TEXT, " +
                     "status VARCHAR(20) NOT NULL CHECK (status IN ('Planejada', 'Em Andamento', 'Concluída', 'Cancelada')), " +
                     "criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    // Método para inserir uma nova patrulha
    public int inserir(Patrulha patrulha) throws SQLException {
        String sql = "INSERT INTO patrulhas (nome, responsavel_id, membros, data, hora_inicio, " +
                     "hora_fim, tipo, observacoes, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, patrulha.getNome());
            stmt.setInt(2, patrulha.getResponsavelId());
            stmt.setString(3, patrulha.getMembrosAsString());
            stmt.setDate(4, Date.valueOf(patrulha.getData()));
            stmt.setTime(5, Time.valueOf(patrulha.getHoraInicio()));
            stmt.setTime(6, patrulha.getHoraFim() != null ? Time.valueOf(patrulha.getHoraFim()) : null);
            stmt.setString(7, patrulha.getTipo());
            stmt.setString(8, patrulha.getObservacoes());
            stmt.setString(9, patrulha.getStatus());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    // Método para atualizar uma patrulha
    public boolean atualizar(Patrulha patrulha) throws SQLException {
        String sql = "UPDATE patrulhas SET nome = ?, responsavel_id = ?, membros = ?, data = ?, " +
                     "hora_inicio = ?, hora_fim = ?, tipo = ?, observacoes = ?, status = ? " +
                     "WHERE id_patrulha = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, patrulha.getNome());
            stmt.setInt(2, patrulha.getResponsavelId());
            stmt.setString(3, patrulha.getMembrosAsString());
            stmt.setDate(4, Date.valueOf(patrulha.getData()));
            stmt.setTime(5, Time.valueOf(patrulha.getHoraInicio()));
            stmt.setTime(6, patrulha.getHoraFim() != null ? Time.valueOf(patrulha.getHoraFim()) : null);
            stmt.setString(7, patrulha.getTipo());
            stmt.setString(8, patrulha.getObservacoes());
            stmt.setString(9, patrulha.getStatus());
            stmt.setInt(10, patrulha.getIdPatrulha());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Método para listar todas as patrulhas
    public List<Patrulha> listarTodas() throws SQLException {
        List<Patrulha> patrulhas = new ArrayList<>();
        String sql = "SELECT * FROM patrulhas ORDER BY data, hora_inicio";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                patrulhas.add(criarPatrulhaFromResultSet(rs));
            }
        }
        return patrulhas;
    }
    
    // Método para buscar patrulha por ID
    public Patrulha buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM patrulhas WHERE id_patrulha = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarPatrulhaFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    // Método para listar patrulhas por status
    public List<Patrulha> listarPorStatus(String status) throws SQLException {
        List<Patrulha> patrulhas = new ArrayList<>();
        String sql = "SELECT * FROM patrulhas WHERE status = ? ORDER BY data, hora_inicio";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patrulhas.add(criarPatrulhaFromResultSet(rs));
                }
            }
        }
        return patrulhas;
    }
    
    // Método para listar patrulhas por data
    public List<Patrulha> listarPorData(LocalDate data) throws SQLException {
        List<Patrulha> patrulhas = new ArrayList<>();
        String sql = "SELECT * FROM patrulhas WHERE data = ? ORDER BY hora_inicio";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(data));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patrulhas.add(criarPatrulhaFromResultSet(rs));
                }
            }
        }
        return patrulhas;
    }
    
    // Método para deletar uma patrulha
    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM patrulhas WHERE id_patrulha = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Método para iniciar uma patrulha (muda status para "Em Andamento")
    public boolean iniciar(int idPatrulha) throws SQLException {
        String sql = "UPDATE patrulhas SET status = 'Em Andamento' WHERE id_patrulha = ? AND status = 'Planejada'";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPatrulha);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Método para finalizar uma patrulha (muda status para "Concluída")
    public boolean finalizar(int idPatrulha, LocalTime horaFim, String observacoes) throws SQLException {
        String sql = "UPDATE patrulhas SET status = 'Concluída', hora_fim = ?, observacoes = ? " +
                     "WHERE id_patrulha = ? AND status = 'Em Andamento'";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTime(1, Time.valueOf(horaFim));
            stmt.setString(2, observacoes);
            stmt.setInt(3, idPatrulha);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Método para cancelar uma patrulha (muda status para "Cancelada")
    public boolean cancelar(int idPatrulha, String motivo) throws SQLException {
        String sql = "UPDATE patrulhas SET status = 'Cancelada', observacoes = ? " +
                     "WHERE id_patrulha = ? AND status IN ('Planejada', 'Em Andamento')";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, motivo);
            stmt.setInt(2, idPatrulha);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Método auxiliar para criar objeto Patrulha a partir de ResultSet
    private Patrulha criarPatrulhaFromResultSet(ResultSet rs) throws SQLException {
        Patrulha patrulha = new Patrulha();
        patrulha.setIdPatrulha(rs.getInt("id_patrulha"));
        patrulha.setNome(rs.getString("nome"));
        patrulha.setResponsavelId(rs.getInt("responsavel_id"));
        
        // Converter string de membros para lista
        String membrosStr = rs.getString("membros");
        if (membrosStr != null && !membrosStr.isEmpty()) {
            patrulha.setMembrosFromString(membrosStr);
        }
        
        patrulha.setData(rs.getDate("data").toLocalDate());
        patrulha.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
        
        Time horaFim = rs.getTime("hora_fim");
        if (horaFim != null) {
            patrulha.setHoraFim(horaFim.toLocalTime());
        }
        
        patrulha.setTipo(rs.getString("tipo"));
        patrulha.setObservacoes(rs.getString("observacoes"));
        patrulha.setStatus(rs.getString("status"));
        
        Timestamp criadoEm = rs.getTimestamp("criado_em");
        if (criadoEm != null) {
            patrulha.setCriadoEm(criadoEm.toLocalDateTime());
        }
        
        Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
        if (atualizadoEm != null) {
            patrulha.setAtualizadoEm(atualizadoEm.toLocalDateTime());
        }
        
        return patrulha;
    }

    // Main interativo para teste
    public static void main(String[] args) {
        PatrulhaDAO dao = new PatrulhaDAO();
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Criar tabela se não existir
            dao.criarTabela();
            
            int opcao = -1;
            while (opcao != 0) {
                System.out.println("\n=== MENU PATRULHAS ===");
                System.out.println("1. Cadastrar nova patrulha");
                System.out.println("2. Listar todas as patrulhas");
                System.out.println("3. Buscar patrulha por ID");
                System.out.println("4. Listar por status");
                System.out.println("5. Listar por data");
                System.out.println("6. Atualizar patrulha");
                System.out.println("7. Remover patrulha");
                System.out.println("8. Iniciar patrulha");
                System.out.println("9. Finalizar patrulha");
                System.out.println("10. Cancelar patrulha");
                System.out.println("11. Gerenciar membros");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                
                opcao = Integer.parseInt(scanner.nextLine());
                
                switch (opcao) {
                    case 1:
                        cadastrarPatrulha(dao, scanner);
                        break;
                    case 2:
                        listarTodasPatrulhas(dao);
                        break;
                    case 3:
                        buscarPatrulhaPorId(dao, scanner);
                        break;
                    case 4:
                        listarPorStatus(dao, scanner);
                        break;
                    case 5:
                        listarPorData(dao, scanner);
                        break;
                    case 6:
                        atualizarPatrulha(dao, scanner);
                        break;
                    case 7:
                        removerPatrulha(dao, scanner);
                        break;
                    case 8:
                        iniciarPatrulha(dao, scanner);
                        break;
                    case 9:
                        finalizarPatrulha(dao, scanner);
                        break;
                    case 10:
                        cancelarPatrulha(dao, scanner);
                        break;
                    case 11:
                        gerenciarMembros(dao, scanner);
                        break;
                    case 0:
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    // Métodos auxiliares para o menu interativo
    private static void cadastrarPatrulha(PatrulhaDAO dao, Scanner scanner) throws SQLException {
        System.out.println("\n--- CADASTRAR NOVA PATRULHA ---");
        Patrulha patrulha = new Patrulha();
        
        System.out.print("Nome da patrulha: ");
        patrulha.setNome(scanner.nextLine());
        
        System.out.print("ID do responsável: ");
        patrulha.setResponsavelId(Integer.parseInt(scanner.nextLine()));
        
        System.out.print("Data (AAAA-MM-DD): ");
        patrulha.setData(LocalDate.parse(scanner.nextLine()));
        
        System.out.print("Hora de início (HH:MM): ");
        patrulha.setHoraInicio(LocalTime.parse(scanner.nextLine()));
        
        System.out.print("Tipo (Ronda/Operação/Especial): ");
        patrulha.setTipo(scanner.nextLine());
        
        System.out.print("Observações (opcional): ");
        String obs = scanner.nextLine();
        if (!obs.isEmpty()) {
            patrulha.setObservacoes(obs);
        }
        
        // Adicionar membros
        System.out.println("\nAdicionar membros à patrulha (digite 0 para parar):");
        while (true) {
            System.out.print("ID do membro (0 para terminar): ");
            int idMembro = Integer.parseInt(scanner.nextLine());
            if (idMembro == 0) break;
            patrulha.adicionarMembro(idMembro);
        }
        
        int id = dao.inserir(patrulha);
        if (id > 0) {
            System.out.println("Patrulha cadastrada com sucesso! ID: " + id);
        } else {
            System.out.println("Falha ao cadastrar patrulha!");
        }
    }
    
    private static void listarTodasPatrulhas(PatrulhaDAO dao) throws SQLException {
        System.out.println("\n--- LISTA DE TODAS AS PATRULHAS ---");
        List<Patrulha> patrulhas = dao.listarTodas();
        
        if (patrulhas.isEmpty()) {
            System.out.println("Nenhuma patrulha cadastrada.");
            return;
        }
        
        for (Patrulha p : patrulhas) {
            System.out.println(p.toResumo());
        }
    }
    
    private static void buscarPatrulhaPorId(PatrulhaDAO dao, Scanner scanner) throws SQLException {
        System.out.print("\nDigite o ID da patrulha: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Patrulha patrulha = dao.buscarPorId(id);
        if (patrulha != null) {
            System.out.println("\n" + patrulha.toDetalhes());
        } else {
            System.out.println("Patrulha não encontrada!");
        }
    }
    
    private static void listarPorStatus(PatrulhaDAO dao, Scanner scanner) throws SQLException {
        System.out.print("\nDigite o status (Planejada/Em Andamento/Concluída/Cancelada): ");
        String status = scanner.nextLine();
        
        List<Patrulha> patrulhas = dao.listarPorStatus(status);
        
        if (patrulhas.isEmpty()) {
            System.out.println("Nenhuma patrulha encontrada com status '" + status + "'");
            return;
        }
        
        System.out.println("\nPatrulhas com status '" + status + "':");
        for (Patrulha p : patrulhas) {
            System.out.println(p.toResumo());
        }
    }
    
    private static void listarPorData(PatrulhaDAO dao, Scanner scanner) throws SQLException {
        System.out.print("\nDigite a data (AAAA-MM-DD): ");
        LocalDate data = LocalDate.parse(scanner.nextLine());
        
        List<Patrulha> patrulhas = dao.listarPorData(data);
        
        if (patrulhas.isEmpty()) {
            System.out.println("Nenhuma patrulha encontrada para a data " + data);
            return;
        }
        
        System.out.println("\nPatrulhas para " + data + ":");
        for (Patrulha p : patrulhas) {
            System.out.println(p.toResumo());
        }
    }
    
    private static void atualizarPatrulha(PatrulhaDAO dao, Scanner scanner) throws SQLException {
        System.out.print("\nDigite o ID da patrulha a ser atualizada: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Patrulha patrulha = dao.buscarPorId(id);
        if (patrulha == null) {
            System.out.println("Patrulha não encontrada!");
            return;
        }
        
        System.out.println("\nDeixe em branco para manter o valor atual");
        
        System.out.print("Novo nome [" + patrulha.getNome() + "]: ");
        String nome = scanner.nextLine();
        if (!nome.isEmpty()) patrulha.setNome(nome);
        
        System.out.print("Novo responsável ID [" + patrulha.getResponsavelId() + "]: ");
        String resp = scanner.nextLine();
        if (!resp.isEmpty()) patrulha.setResponsavelId(Integer.parseInt(resp));
        
        System.out.print("Nova data [" + patrulha.getData() + "]: ");
        String data = scanner.nextLine();
        if (!data.isEmpty()) patrulha.setData(LocalDate.parse(data));
        
        System.out.print("Nova hora início [" + patrulha.getHoraInicio() + "]: ");
        String horaInicio = scanner.nextLine();
        if (!horaInicio.isEmpty()) patrulha.setHoraInicio(LocalTime.parse(horaInicio));
        
        System.out.print("Nova hora fim [" + (patrulha.getHoraFim() != null ? patrulha.getHoraFim() : "") + "]: ");
        String horaFim = scanner.nextLine();
        if (!horaFim.isEmpty()) patrulha.setHoraFim(LocalTime.parse(horaFim));
        
        System.out.print("Novo tipo [" + patrulha.getTipo() + "]: ");
        String tipo = scanner.nextLine();
        if (!tipo.isEmpty()) patrulha.setTipo(tipo);
        
        System.out.print("Novas observações [" + (patrulha.getObservacoes() != null ? patrulha.getObservacoes() : "") + "]: ");
        String obs = scanner.nextLine();
        if (!obs.isEmpty()) patrulha.setObservacoes(obs);
        
        System.out.print("Novo status [" + patrulha.getStatus() + "]: ");
        String status = scanner.nextLine();
        if (!status.isEmpty()) patrulha.setStatus(status);
        
        if (dao.atualizar(patrulha)) {
            System.out.println("Patrulha atualizada com sucesso!");
        } else {
            System.out.println("Falha ao atualizar patrulha!");
        }
    }
    
    private static void removerPatrulha(PatrulhaDAO dao, Scanner scanner) throws SQLException {
        System.out.print("\nDigite o ID da patrulha a ser removida: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Tem certeza que deseja remover esta patrulha? (S/N): ");
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("S")) {
            if (dao.deletar(id)) {
                System.out.println("Patrulha removida com sucesso!");
            } else {
                System.out.println("Falha ao remover patrulha ou patrulha não encontrada.");
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }
    
    private static void iniciarPatrulha(PatrulhaDAO dao, Scanner scanner) throws SQLException {
        System.out.print("\nDigite o ID da patrulha a ser iniciada: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        if (dao.iniciar(id)) {
            System.out.println("Patrulha iniciada com sucesso!");
        } else {
            System.out.println("Falha ao iniciar patrulha. Verifique se o status atual é 'Planejada'.");
        }
    }
    
    private static void finalizarPatrulha(PatrulhaDAO dao, Scanner scanner) throws SQLException {
        System.out.print("\nDigite o ID da patrulha a ser finalizada: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Hora de término (HH:MM): ");
        LocalTime horaFim = LocalTime.parse(scanner.nextLine());
        
        System.out.print("Relatório final: ");
        String relatorio = scanner.nextLine();
        
        if (dao.finalizar(id, horaFim, relatorio)) {
            System.out.println("Patrulha finalizada com sucesso!");
        } else {
            System.out.println("Falha ao finalizar patrulha. Verifique se o status atual é 'Em Andamento'.");
        }
    }
    
    private static void cancelarPatrulha(PatrulhaDAO dao, Scanner scanner) throws SQLException {
        System.out.print("\nDigite o ID da patrulha a ser cancelada: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Motivo do cancelamento: ");
        String motivo = scanner.nextLine();
        
        if (dao.cancelar(id, motivo)) {
            System.out.println("Patrulha cancelada com sucesso!");
        } else {
            System.out.println("Falha ao cancelar patrulha. Verifique se o status atual é 'Planejada' ou 'Em Andamento'.");
        }
    }
    
    private static void gerenciarMembros(PatrulhaDAO dao, Scanner scanner) throws SQLException {
        System.out.print("\nDigite o ID da patrulha: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Patrulha patrulha = dao.buscarPorId(id);
        if (patrulha == null) {
            System.out.println("Patrulha não encontrada!");
            return;
        }
        
        while (true) {
            System.out.println("\n--- GERENCIAR MEMBROS DA PATRULHA " + id + " ---");
            System.out.println("1. Adicionar membro");
            System.out.println("2. Remover membro");
            System.out.println("3. Listar membros");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 1:
                    System.out.print("ID do membro a adicionar: ");
                    int idAdd = Integer.parseInt(scanner.nextLine());
                    if (idAdd == patrulha.getResponsavelId()) {
                        System.out.println("O responsável já é membro da patrulha!");
                    } else {
                        patrulha.adicionarMembro(idAdd);
                        if (dao.atualizar(patrulha)) {
                            System.out.println("Membro adicionado com sucesso!");
                        } else {
                            System.out.println("Falha ao adicionar membro!");
                        }
                    }
                    break;
                case 2:
                    System.out.print("ID do membro a remover: ");
                    int idRem = Integer.parseInt(scanner.nextLine());
                    try {
                        patrulha.removerMembro(idRem);
                        if (dao.atualizar(patrulha)) {
                            System.out.println("Membro removido com sucesso!");
                        } else {
                            System.out.println("Falha ao remover membro!");
                        }
                    } catch (IllegalStateException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("\nMembros da patrulha:");
                    System.out.println("Responsável: " + patrulha.getResponsavelId());
                    System.out.println("Membros: " + patrulha.getMembrosAsString());
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}