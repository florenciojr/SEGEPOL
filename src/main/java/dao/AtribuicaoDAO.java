/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author JR5
 */



import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Atribuicao;
import model.Conexao;

public class AtribuicaoDAO {
    private Connection conexao;

    public AtribuicaoDAO() {
        this.conexao = Conexao.conectar();
    }

    // Método auxiliar para verificar existência na tabela usuarios com cargo de investigador
    private boolean isInvestigadorValido(int idInvestigador) {
        String sql = "SELECT 1 FROM usuarios WHERE id_usuario = ? AND cargo = 'Investigador'";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idInvestigador);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar investigador: " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar para verificar existência de queixa
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

    // INSERIR (CREATE)
    public boolean inserir(Atribuicao atribuicao) {
        if (!existeQueixa(atribuicao.getIdQueixa())) {
            System.err.println("Erro: Queixa não encontrada!");
            return false;
        }
        
        if (!isInvestigadorValido(atribuicao.getIdInvestigador())) {
            System.err.println("Erro: Investigador não encontrado ou não tem perfil adequado!");
            return false;
        }

        String sql = "INSERT INTO atribuicoes (id_queixa, id_investigador, data_atribuicao) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, atribuicao.getIdQueixa());
            stmt.setInt(2, atribuicao.getIdInvestigador());
            stmt.setDate(3, new java.sql.Date(atribuicao.getDataAtribuicao().getTime()));
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        atribuicao.setIdAtribuicao(rs.getInt(1));
                        System.out.println("Atribuição registrada com ID: " + atribuicao.getIdAtribuicao());
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir atribuição: " + e.getMessage());
            return false;
        }
    }

    // LISTAR TODOS (READ ALL) com nome do investigador
    public List<Atribuicao> listarTodos() {
        List<Atribuicao> lista = new ArrayList<>();
        String sql = "SELECT a.*, u.nome as nome_investigador " +
                     "FROM atribuicoes a " +
                     "JOIN usuarios u ON a.id_investigador = u.id_usuario";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Atribuicao a = new Atribuicao(
                    rs.getInt("id_atribuicao"),
                    rs.getInt("id_queixa"),
                    rs.getInt("id_investigador"),
                    rs.getDate("data_atribuicao")
                );
                a.setNomeInvestigador(rs.getString("nome_investigador")); // Adicione este método na classe Atribuicao
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar atribuições: " + e.getMessage());
        }
        return lista;
    }

    // BUSCAR POR ID (READ BY ID) com nome do investigador
    public Atribuicao buscarPorId(int id) {
        String sql = "SELECT a.*, u.nome as nome_investigador " +
                     "FROM atribuicoes a " +
                     "JOIN usuarios u ON a.id_investigador = u.id_usuario " +
                     "WHERE a.id_atribuicao = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Atribuicao a = new Atribuicao(
                        rs.getInt("id_atribuicao"),
                        rs.getInt("id_queixa"),
                        rs.getInt("id_investigador"),
                        rs.getDate("data_atribuicao")
                    );
                    a.setNomeInvestigador(rs.getString("nome_investigador"));
                    return a;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar atribuição: " + e.getMessage());
        }
        return null;
    }

    // ATUALIZAR (UPDATE)
    public boolean atualizar(Atribuicao atribuicao) {
        if (buscarPorId(atribuicao.getIdAtribuicao()) == null) {
            System.err.println("Erro: Atribuição não encontrada!");
            return false;
        }
        
        if (!existeQueixa(atribuicao.getIdQueixa())) {
            System.err.println("Erro: Nova queixa não encontrada!");
            return false;
        }
        
        if (!isInvestigadorValido(atribuicao.getIdInvestigador())) {
            System.err.println("Erro: Novo investigador não encontrado ou não tem perfil adequado!");
            return false;
        }

        String sql = "UPDATE atribuicoes SET id_queixa = ?, id_investigador = ?, data_atribuicao = ? WHERE id_atribuicao = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, atribuicao.getIdQueixa());
            stmt.setInt(2, atribuicao.getIdInvestigador());
            stmt.setDate(3, new java.sql.Date(atribuicao.getDataAtribuicao().getTime()));
            stmt.setInt(4, atribuicao.getIdAtribuicao());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar atribuição: " + e.getMessage());
            return false;
        }
    }

    // REMOVER (DELETE)
    public boolean remover(int id) {
        String sql = "DELETE FROM atribuicoes WHERE id_atribuicao = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover atribuição: " + e.getMessage());
            return false;
        }
    }

    // MAIN e métodos auxiliares para o menu (mantidos iguais ao seu código original)
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            AtribuicaoDAO dao = new AtribuicaoDAO();
            
            while (true) {
                System.out.println("\n=== MENU ATRIBUIÇÕES ===");
                System.out.println("1. Inserir Atribuição");
                System.out.println("2. Listar Todas");
                System.out.println("3. Buscar por ID");
                System.out.println("4. Atualizar");
                System.out.println("5. Remover");
                System.out.println("0. Sair");
                System.out.print("Opção: ");
                
                int opcao;
                try {
                    opcao = scanner.nextInt();
                    scanner.nextLine(); // Limpar buffer
                } catch (Exception e) {
                    System.out.println("Entrada inválida! Digite um número.");
                    scanner.nextLine(); // Limpar buffer
                    continue;
                }
                
                switch (opcao) {
                    case 1:
                        inserirAtribuicao(dao, scanner);
                        break;
                    case 2:
                        listarAtribuicoes(dao);
                        break;
                    case 3:
                        buscarAtribuicao(dao, scanner);
                        break;
                    case 4:
                        atualizarAtribuicao(dao, scanner);
                        break;
                    case 5:
                        removerAtribuicao(dao, scanner);
                        break;
                    case 0:
                        System.out.println("Saindo do sistema...");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro no sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void inserirAtribuicao(AtribuicaoDAO dao, Scanner scanner) {
        System.out.println("\n--- NOVA ATRIBUIÇÃO ---");
        
        try {
            System.out.print("ID da Queixa: ");
            int idQueixa = scanner.nextInt();
            
            System.out.print("ID do Investigador: ");
            int idInvestigador = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            Atribuicao nova = new Atribuicao(0, idQueixa, idInvestigador, new java.util.Date());
            
            if (dao.inserir(nova)) {
                System.out.println("Atribuição cadastrada com sucesso! ID: " + nova.getIdAtribuicao());
            } else {
                System.out.println("Falha ao cadastrar atribuição!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao inserir: " + e.getMessage());
            scanner.nextLine(); // Limpar buffer em caso de erro
        }
    }

    private static void listarAtribuicoes(AtribuicaoDAO dao) {
        System.out.println("\n--- LISTA DE ATRIBUIÇÕES ---");
        try {
            List<Atribuicao> lista = dao.listarTodos();
            
            if (lista.isEmpty()) {
                System.out.println("Nenhuma atribuição cadastrada.");
            } else {
                System.out.printf("%-5s %-10s %-15s %-20s %-20s%n", 
                    "ID", "ID Queixa", "Investigador", "Data Atribuição");
                System.out.println("------------------------------------------------------------");
                
                for (Atribuicao a : lista) {
                    System.out.printf("%-5d %-10d %-15s %-20s%n",
                        a.getIdAtribuicao(),
                        a.getIdQueixa(),
                        a.getNomeInvestigador(),
                        new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(a.getDataAtribuicao()));
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar: " + e.getMessage());
        }
    }

    private static void buscarAtribuicao(AtribuicaoDAO dao, Scanner scanner) {
        System.out.println("\n--- BUSCAR ATRIBUIÇÃO ---");
        try {
            System.out.print("ID da Atribuição: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            Atribuicao a = dao.buscarPorId(id);
            
            if (a != null) {
                System.out.println("\nAtribuição encontrada:");
                System.out.println("ID: " + a.getIdAtribuicao());
                System.out.println("ID Queixa: " + a.getIdQueixa());
                System.out.println("Investigador: " + a.getNomeInvestigador());
                System.out.println("Data: " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(a.getDataAtribuicao()));
            } else {
                System.out.println("Atribuição não encontrada!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());
            scanner.nextLine(); // Limpar buffer em caso de erro
        }
    }

    private static void atualizarAtribuicao(AtribuicaoDAO dao, Scanner scanner) {
        System.out.println("\n--- ATUALIZAR ATRIBUIÇÃO ---");
        try {
            System.out.print("ID da Atribuição: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            Atribuicao a = dao.buscarPorId(id);
            
            if (a == null) {
                System.out.println("Atribuição não encontrada!");
                return;
            }
            
            System.out.println("\nDados atuais:");
            System.out.println("ID Queixa: " + a.getIdQueixa());
            System.out.println("Investigador: " + a.getNomeInvestigador());
            System.out.println("Data: " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(a.getDataAtribuicao()));
            
            System.out.print("\nNovo ID da Queixa (" + a.getIdQueixa() + "): ");
            a.setIdQueixa(scanner.nextInt());
            
            System.out.print("Novo ID do Investigador (" + a.getIdInvestigador() + "): ");
            a.setIdInvestigador(scanner.nextInt());
            scanner.nextLine(); // Limpar buffer
            
            a.setDataAtribuicao(new java.util.Date());
            
            if (dao.atualizar(a)) {
                System.out.println("Atribuição atualizada com sucesso!");
            } else {
                System.out.println("Falha ao atualizar atribuição!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
            scanner.nextLine(); // Limpar buffer em caso de erro
        }
    }

    private static void removerAtribuicao(AtribuicaoDAO dao, Scanner scanner) {
        System.out.println("\n--- REMOVER ATRIBUIÇÃO ---");
        try {
            System.out.print("ID da Atribuição: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            System.out.print("Tem certeza que deseja remover? (S/N): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("S")) {
                if (dao.remover(id)) {
                    System.out.println("Atribuição removida com sucesso!");
                } else {
                    System.out.println("Falha ao remover atribuição!");
                }
            } else {
                System.out.println("Operação cancelada.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao remover: " + e.getMessage());
            scanner.nextLine(); // Limpar buffer em caso de erro
        }
    }
}