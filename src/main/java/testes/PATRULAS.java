/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testes;

import dao.PatrulhaDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import model.Patrulha;

/**
 *
 * @author JR5
 */
public class PATRULAS {
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
  

