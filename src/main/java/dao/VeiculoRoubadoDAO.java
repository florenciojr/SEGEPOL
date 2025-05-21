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
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.Part;
import javax.servlet.ServletContext;
import java.io.File;

public class VeiculoRoubadoDAO {
    private static final String UPLOAD_DIR = "resources/uploads/veiculos/";
    private static final String[] EXTENSOES_PERMITIDAS = {".jpg", ".jpeg", ".png", ".gif"};
    private static final int MAX_TAMANHO_MB = 5;
    
    private final ServletContext servletContext;

    public VeiculoRoubadoDAO(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

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

    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM veiculos_roubados WHERE id_veiculo = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

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

    public String uploadFoto(Part filePart, String matricula) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        // Verificar tamanho do arquivo
        if (filePart.getSize() > MAX_TAMANHO_MB * 1024 * 1024) {
            throw new IOException("Tamanho do arquivo excede o limite de " + MAX_TAMANHO_MB + "MB");
        }

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        // Verificar extensão do arquivo
        boolean extensaoValida = false;
        for (String ext : EXTENSOES_PERMITIDAS) {
            if (ext.equalsIgnoreCase(fileExtension)) {
                extensaoValida = true;
                break;
            }
        }
        if (!extensaoValida) {
            throw new IOException("Extensão de arquivo não permitida. Use: " + String.join(", ", EXTENSOES_PERMITIDAS));
        }

        // Criar diretório se não existir
        String uploadPath = servletContext.getRealPath("") + File.separator + UPLOAD_DIR;
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Gerar nome único para o arquivo
        String uniqueFileName = "veiculo_" + matricula.toLowerCase().replaceAll("[^a-z0-9]", "") + 
                              "_" + System.currentTimeMillis() + fileExtension;
        Path filePath = uploadDir.resolve(uniqueFileName);

        // Salvar arquivo
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return UPLOAD_DIR + uniqueFileName;
    }
}
