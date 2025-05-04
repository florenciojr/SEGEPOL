/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author JR5
 */



import model.VeiculoRoubado;
import model.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.*;
import java.io.IOException;

public class VeiculoRoubadoDAO {
    private static final String UPLOAD_DIR = "uploads/veiculos/";
    private static final String[] EXTENSOES_PERMITIDAS = {".jpg", ".jpeg", ".png", ".gif"};
    private static final int MAX_TAMANHO_MB = 5;

    // Método para inserir veículo
    public boolean inserir(VeiculoRoubado veiculo) throws SQLException {
        String sql = "INSERT INTO veiculos_roubados (id_queixa, marca, modelo, cor, matricula, ano, foto_veiculo, data_registro) " +
                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, veiculo.getIdQueixa());
            stmt.setString(2, veiculo.getMarca());
            stmt.setString(3, veiculo.getModelo());
            stmt.setString(4, veiculo.getCor());
            stmt.setString(5, veiculo.getMatricula());
            
            if (veiculo.getAno() != null) {
                stmt.setInt(6, veiculo.getAno());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setString(7, veiculo.getFotoVeiculo());
            stmt.setTimestamp(8, veiculo.getDataRegistro());

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        veiculo.setIdVeiculo(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // Método para buscar por ID
    public VeiculoRoubado buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM veiculos_roubados WHERE id_veiculo = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
                return null;
            }
        }
    }

    // Método para listar todos
    public List<VeiculoRoubado> listarTodos() throws SQLException {
        List<VeiculoRoubado> veiculos = new ArrayList<>();
        String sql = "SELECT * FROM veiculos_roubados ORDER BY data_registro DESC";
        
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                veiculos.add(mapear(rs));
            }
        }
        return veiculos;
    }

    // Método para atualizar
    public boolean atualizar(VeiculoRoubado veiculo) throws SQLException {
        String sql = "UPDATE veiculos_roubados SET id_queixa = ?, marca = ?, modelo = ?, " +
                     "cor = ?, matricula = ?, ano = ?, foto_veiculo = ? WHERE id_veiculo = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, veiculo.getIdQueixa());
            stmt.setString(2, veiculo.getMarca());
            stmt.setString(3, veiculo.getModelo());
            stmt.setString(4, veiculo.getCor());
            stmt.setString(5, veiculo.getMatricula());
            
            if (veiculo.getAno() != null) {
                stmt.setInt(6, veiculo.getAno());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setString(7, veiculo.getFotoVeiculo());
            stmt.setInt(8, veiculo.getIdVeiculo());

            return stmt.executeUpdate() > 0;
        }
    }

    // Método para deletar
    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM veiculos_roubados WHERE id_veiculo = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Método para buscar por matrícula
    public List<VeiculoRoubado> buscarPorMatricula(String matricula) throws SQLException {
        List<VeiculoRoubado> veiculos = new ArrayList<>();
        String sql = "SELECT * FROM veiculos_roubados WHERE matricula LIKE ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + matricula + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    veiculos.add(mapear(rs));
                }
            }
        }
        return veiculos;
    }

    // Método auxiliar para mapear ResultSet
    private VeiculoRoubado mapear(ResultSet rs) throws SQLException {
        VeiculoRoubado veiculo = new VeiculoRoubado();
        veiculo.setIdVeiculo(rs.getInt("id_veiculo"));
        veiculo.setIdQueixa(rs.getInt("id_queixa"));
        veiculo.setMarca(rs.getString("marca"));
        veiculo.setModelo(rs.getString("modelo"));
        veiculo.setCor(rs.getString("cor"));
        veiculo.setMatricula(rs.getString("matricula"));
        veiculo.setAno(rs.getObject("ano") != null ? rs.getInt("ano") : null);
        veiculo.setFotoVeiculo(rs.getString("foto_veiculo"));
        veiculo.setDataRegistro(rs.getTimestamp("data_registro"));
        return veiculo;
    }

    // Método para upload de foto
    public static String uploadFoto(Scanner sc, String matricula) throws IOException {
        System.out.print("\nCaminho da foto do veículo: ");
        String caminhoOriginal = sc.nextLine().trim();
        
        if (caminhoOriginal.isEmpty()) {
            return null;
        }

        Path origem = Paths.get(caminhoOriginal);
        
        // Verifica se arquivo existe
        if (!Files.exists(origem)) {
            throw new IOException("Arquivo não encontrado: " + caminhoOriginal);
        }

        // Verifica extensão
        String extensao = caminhoOriginal.substring(caminhoOriginal.lastIndexOf(".")).toLowerCase();
        
          boolean extensaoValida = false;
          for (String ext : EXTENSOES_PERMITIDAS) {
          if (ext.equalsIgnoreCase(extensao)) {
          extensaoValida = true;
        break;
    }
}
if (!extensaoValida) {
    throw new IOException("Extensão inválida. Use: " + String.join(", ", EXTENSOES_PERMITIDAS));
}

        // Verifica tamanho
        long tamanhoMB = Files.size(origem) / (1024 * 1024);
        if (tamanhoMB > MAX_TAMANHO_MB) {
            throw new IOException("Tamanho excede o limite de " + MAX_TAMANHO_MB + "MB");
        }

        // Cria diretório se não existir
        Path destinoDir = Paths.get(UPLOAD_DIR);
        if (!Files.exists(destinoDir)) {
            Files.createDirectories(destinoDir);
        }

        // Gera nome único para o arquivo
        String nomeArquivo = "veiculo_" + matricula.toLowerCase().replaceAll("[^a-z0-9]", "") + 
                           "_" + System.currentTimeMillis() + extensao;
        Path destino = destinoDir.resolve(nomeArquivo);

        // Copia arquivo
        Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);

        return destino.toString();
    }

    // MAIN para teste interativo
    public static void main(String[] args) {
    VeiculoRoubadoDAO dao = new VeiculoRoubadoDAO();
    Scanner sc = new Scanner(System.in);
    int opcao;
    
    do {
        System.out.println("\n=== SISTEMA VEÍCULOS ROUBADOS ===");
        System.out.println("1. Cadastrar veículo");
        System.out.println("2. Listar todos");
        System.out.println("3. Buscar por ID");
        System.out.println("4. Atualizar veículo");
        System.out.println("5. Remover veículo");
        System.out.println("6. Buscar por matrícula");
        System.out.println("0. Sair");
        System.out.print("Opção: ");
        
        try {
            opcao = Integer.parseInt(sc.nextLine());
            
            switch (opcao) {
                case 1:
                    cadastrarVeiculo(dao, sc);
                    break;
                case 2:
                    listarVeiculos(dao);
                    break;
                case 3:
                    buscarPorId(dao, sc);
                    break;
                case 4:
                    atualizarVeiculo(dao, sc);
                    break;
                case 5:
                    removerVeiculo(dao, sc);
                    break;
                case 6:
                    buscarPorMatricula(dao, sc);
                    break;
                case 0:
                    System.out.println("Encerrando sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido!");
            opcao = -1;  // Força continuar no loop
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            opcao = -1;  // Força continuar no loop
        }
    } while (opcao != 0);
    
    sc.close();
}

    private static void cadastrarVeiculo(VeiculoRoubadoDAO dao, Scanner sc) throws Exception {
        System.out.println("\n--- CADASTRAR VEÍCULO ---");
        VeiculoRoubado v = new VeiculoRoubado();
        
        System.out.print("ID Queixa (0 se não houver): ");
        v.setIdQueixa(Integer.parseInt(sc.nextLine()));
        
        System.out.print("Marca: ");
        v.setMarca(sc.nextLine());
        
        System.out.print("Modelo: ");
        v.setModelo(sc.nextLine());
        
        System.out.print("Cor: ");
        v.setCor(sc.nextLine());
        
        System.out.print("Matrícula: ");
        String matricula = sc.nextLine();
        v.setMatricula(matricula);
        
        System.out.print("Ano (opcional): ");
        String anoStr = sc.nextLine();
        v.setAno(anoStr.isEmpty() ? null : Integer.parseInt(anoStr));
        
        try {
            v.setFotoVeiculo(uploadFoto(sc, matricula));
        } catch (IOException e) {
            System.out.println("Aviso: " + e.getMessage());
        }
        
        if (dao.inserir(v)) {
            System.out.println("Veículo cadastrado com sucesso! ID: " + v.getIdVeiculo());
        } else {
            System.out.println("Falha ao cadastrar veículo!");
        }
    }

    private static void listarVeiculos(VeiculoRoubadoDAO dao) throws SQLException {
        System.out.println("\n--- VEÍCULOS ROUBADOS ---");
        List<VeiculoRoubado> lista = dao.listarTodos();
        
        if (lista.isEmpty()) {
            System.out.println("Nenhum veículo encontrado.");
        } else {
           for (VeiculoRoubado veiculo : lista) {
    System.out.println(veiculo);
}
        }
    }

    private static void buscarPorId(VeiculoRoubadoDAO dao, Scanner sc) throws SQLException {
        System.out.print("\nID do veículo: ");
        int id = Integer.parseInt(sc.nextLine());
        
        VeiculoRoubado v = dao.buscarPorId(id);
        if (v != null) {
            exibirDetalhes(v);
        } else {
            System.out.println("Veículo não encontrado!");
        }
    }

    private static void atualizarVeiculo(VeiculoRoubadoDAO dao, Scanner sc) throws Exception {
        System.out.print("\nID do veículo para atualizar: ");
        int id = Integer.parseInt(sc.nextLine());
        
        VeiculoRoubado v = dao.buscarPorId(id);
        if (v == null) {
            System.out.println("Veículo não encontrado!");
            return;
        }
        
        System.out.println("\nDeixe em branco para manter o valor atual");
        
        System.out.print("Nova marca [" + v.getMarca() + "]: ");
        String marca = sc.nextLine();
        if (!marca.isEmpty()) v.setMarca(marca);
        
        System.out.print("Novo modelo [" + v.getModelo() + "]: ");
        v.setModelo(sc.nextLine());
        
        System.out.print("Nova cor [" + v.getCor() + "]: ");
        v.setCor(sc.nextLine());
        
        System.out.print("Nova matrícula [" + v.getMatricula() + "]: ");
        String matricula = sc.nextLine();
        if (!matricula.isEmpty()) v.setMatricula(matricula);
        
        System.out.print("Novo ano [" + v.getAno() + "]: ");
        String anoStr = sc.nextLine();
        if (!anoStr.isEmpty()) v.setAno(Integer.parseInt(anoStr));
        
        System.out.print("Deseja alterar a foto? (s/n): ");
        if (sc.nextLine().equalsIgnoreCase("s")) {
            try {
                v.setFotoVeiculo(uploadFoto(sc, v.getMatricula()));
            } catch (IOException e) {
                System.out.println("Aviso: " + e.getMessage());
            }
        }
        
        if (dao.atualizar(v)) {
            System.out.println("Veículo atualizado com sucesso!");
        } else {
            System.out.println("Falha ao atualizar veículo!");
        }
    }

    private static void removerVeiculo(VeiculoRoubadoDAO dao, Scanner sc) throws SQLException {
        System.out.print("\nID do veículo para remover: ");
        int id = Integer.parseInt(sc.nextLine());
        
        if (dao.deletar(id)) {
            System.out.println("Veículo removido com sucesso!");
        } else {
            System.out.println("Falha ao remover veículo!");
        }
    }

    private static void buscarPorMatricula(VeiculoRoubadoDAO dao, Scanner sc) throws SQLException {
        System.out.print("\nMatrícula (ou parte): ");
        String matricula = sc.nextLine();
        
        List<VeiculoRoubado> lista = dao.buscarPorMatricula(matricula);
        
        if (lista.isEmpty()) {
            System.out.println("Nenhum veículo encontrado.");
        } else {
            System.out.println("\nRESULTADOS:");
            for (VeiculoRoubado veiculo : lista) {
            System.out.println(veiculo);
}
        }
    }

    private static void exibirDetalhes(VeiculoRoubado v) {
        System.out.println("\n--- DETALHES DO VEÍCULO ---");
        System.out.println("ID: " + v.getIdVeiculo());
        System.out.println("ID Queixa: " + (v.getIdQueixa() > 0 ? v.getIdQueixa() : "N/A"));
        System.out.println("Marca: " + v.getMarca());
        System.out.println("Modelo: " + v.getModelo());
        System.out.println("Cor: " + v.getCor());
        System.out.println("Matrícula: " + v.getMatricula());
        System.out.println("Ano: " + (v.getAno() != null ? v.getAno() : "N/A"));
        System.out.println("Foto: " + (v.getFotoVeiculo() != null ? v.getFotoVeiculo() : "Nenhuma"));
        System.out.println("Data Registro: " + v.getDataRegistro());
    }
}