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
import util.Conexao;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ProvaDetalhada;

public class ProvaDAO {
    private static final Logger LOGGER = Logger.getLogger(ProvaDAO.class.getName());
    private static final String DIRETORIO_UPLOAD = "uploads/provas/";

    
   
    public ProvaDetalhada buscarProvaDetalhada(int idProva) throws SQLException {
    String sql = "SELECT p.*, q.titulo AS titulo_queixa, u.nome AS nome_usuario " +
                 "FROM provas p " +
                 "LEFT JOIN queixas q ON p.id_queixa = q.id_queixa " +
                 "LEFT JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                 "WHERE p.id_prova = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, idProva);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                // Mapeia os dados básicos da prova
                Prova prova = new Prova();
                prova.setIdProva(rs.getInt("id_prova"));
                prova.setIdQueixa(rs.getInt("id_queixa"));
                prova.setTipo(rs.getString("tipo"));
                prova.setDescricao(rs.getString("descricao"));
                prova.setCaminhoArquivo(rs.getString("caminho_arquivo"));
                prova.setDataColeta(rs.getTimestamp("data_coleta"));
                prova.setDataUpload(rs.getTimestamp("data_upload"));
                prova.setIdUsuario(rs.getInt("id_usuario"));
                
                // Cria a prova detalhada
                return new ProvaDetalhada(
                    prova,
                    rs.getString("titulo_queixa"),
                    rs.getString("nome_usuario")
                );
            }
        }
    }
    return null;
}
    
    

    public List<Prova> listarTodasComRelacionamentos() throws SQLException {
        List<Prova> provas = new ArrayList<>();
        String sql = "SELECT p.*, q.titulo AS titulo_queixa, u.nome AS nome_usuario " +
                     "FROM provas p " +
                     "LEFT JOIN queixas q ON p.id_queixa = q.id_queixa " +
                     "LEFT JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                     "ORDER BY p.data_upload DESC";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                provas.add(mapearProvaComRelacionamentos(rs));
            }
        }
        return provas;
    }

    public Prova buscarPorIdComRelacionamentos(int id) throws SQLException {
        String sql = "SELECT p.*, q.titulo AS titulo_queixa, u.nome AS nome_usuario " +
                     "FROM provas p " +
                     "LEFT JOIN queixas q ON p.id_queixa = q.id_queixa " +
                     "LEFT JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                     "WHERE p.id_prova = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProvaComRelacionamentos(rs);
                }
            }
        }
        return null;
    }

private ProvaDetalhada mapearProvaComRelacionamentos(ResultSet rs) throws SQLException {
    ProvaDetalhada prova = new ProvaDetalhada();
    
    // Mapear campos básicos da Prova
    prova.setIdProva(rs.getInt("id_prova"));
    prova.setIdQueixa(rs.getInt("id_queixa"));
    prova.setTipo(rs.getString("tipo"));
    prova.setDescricao(rs.getString("descricao"));
    prova.setCaminhoArquivo(rs.getString("caminho_arquivo"));
    prova.setDataColeta(rs.getTimestamp("data_coleta"));
    prova.setDataUpload(rs.getTimestamp("data_upload"));
    prova.setIdUsuario(rs.getInt("id_usuario"));
    
    // Mapear campos adicionais
    prova.setTituloQueixa(rs.getString("titulo_queixa"));
    prova.setNomeUsuario(rs.getString("nome_usuario"));
    
    return prova;
}

    public List<Map<String, Object>> listarProvasComRelacionamentos() throws SQLException {
    List<Map<String, Object>> result = new ArrayList<>();
    String sql = "SELECT p.*, q.titulo AS titulo_queixa, u.nome AS nome_usuario " +
                 "FROM provas p " +
                 "LEFT JOIN queixas q ON p.id_queixa = q.id_queixa " +
                 "LEFT JOIN usuarios u ON p.id_usuario = u.id_usuario";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            result.add(row);
        }
    }
    return result;
}
    
    
    public ProvaDAO() {
        criarDiretorioUpload();
    }

    private void criarDiretorioUpload() {
        File diretorio = new File(DIRETORIO_UPLOAD);
        if (!diretorio.exists()) {
            if (diretorio.mkdirs()) {
                LOGGER.info("Diretório de upload criado: " + DIRETORIO_UPLOAD);
            } else {
                LOGGER.warning("Falha ao criar diretório de upload: " + DIRETORIO_UPLOAD);
            }
        }
    }

    public String processarUpload(String caminhoOriginal) throws IOException {
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
        LOGGER.info("Arquivo copiado para: " + destino.getAbsolutePath());
        return destino.getAbsolutePath();
    }

    private boolean existeQueixa(int idQueixa) {
        String sql = "SELECT 1 FROM queixas WHERE id_queixa = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idQueixa);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao verificar queixa", e);
            return false;
        } finally {
            Conexao.fecharTudo(conn, stmt, rs);
        }
    }

    private boolean existeUsuario(int idUsuario) {
        String sql = "SELECT 1 FROM usuarios WHERE id_usuario = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao verificar usuário", e);
            return false;
        } finally {
            Conexao.fecharTudo(conn, stmt, rs);
        }
    }

    public boolean inserir(Prova prova) {
        if (!existeQueixa(prova.getIdQueixa())) {
            LOGGER.warning("Queixa não encontrada: " + prova.getIdQueixa());
            return false;
        }

        if (!existeUsuario(prova.getIdUsuario())) {
            LOGGER.warning("Usuário não encontrado: " + prova.getIdUsuario());
            return false;
        }

        String sql = "INSERT INTO provas (id_queixa, tipo, descricao, caminho_arquivo, data_coleta, id_usuario) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, prova.getIdQueixa());
            stmt.setString(2, prova.getTipo());
            stmt.setString(3, prova.getDescricao());
            stmt.setString(4, prova.getCaminhoArquivo());
            stmt.setTimestamp(5, prova.getDataColeta() != null ? new Timestamp(prova.getDataColeta().getTime()) : null);
            stmt.setInt(6, prova.getIdUsuario());
            
            LOGGER.info("Executando inserção: " + stmt.toString());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    prova.setIdProva(rs.getInt(1));
                    LOGGER.info("Prova inserida com sucesso. ID: " + prova.getIdProva());
                    return true;
                }
            }
            LOGGER.warning("Nenhuma linha afetada na inserção");
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir prova", e);
            Conexao.rollback(conn);
            return false;
        } finally {
            Conexao.fecharTudo(conn, stmt, rs);
        }
    }

    public List<Prova> listarTodas() {
        List<Prova> provas = new ArrayList<>();
        String sql = "SELECT * FROM provas ORDER BY data_upload DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Prova p = new Prova();
                p.setIdProva(rs.getInt("id_prova"));
                p.setIdQueixa(rs.getInt("id_queixa"));
                p.setTipo(rs.getString("tipo"));
                p.setDescricao(rs.getString("descricao"));
                p.setCaminhoArquivo(rs.getString("caminho_arquivo"));
                p.setDataColeta(rs.getTimestamp("data_coleta"));
                p.setDataUpload(rs.getTimestamp("data_upload"));
                p.setIdUsuario(rs.getInt("id_usuario"));
                provas.add(p);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar provas", e);
        } finally {
            Conexao.fecharTudo(conn, stmt, rs);
        }
        return provas;
    }

    public Prova buscarPorId(int id) {
        String sql = "SELECT * FROM provas WHERE id_prova = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Prova p = new Prova();
                p.setIdProva(rs.getInt("id_prova"));
                p.setIdQueixa(rs.getInt("id_queixa"));
                p.setTipo(rs.getString("tipo"));
                p.setDescricao(rs.getString("descricao"));
                p.setCaminhoArquivo(rs.getString("caminho_arquivo"));
                p.setDataColeta(rs.getTimestamp("data_coleta"));
                p.setDataUpload(rs.getTimestamp("data_upload"));
                p.setIdUsuario(rs.getInt("id_usuario"));
                return p;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar prova", e);
        } finally {
            Conexao.fecharTudo(conn, stmt, rs);
        }
        return null;
    }

    public boolean atualizar(Prova prova) {
        String sql = "UPDATE provas SET id_queixa = ?, tipo = ?, descricao = ?, caminho_arquivo = ?, data_coleta = ?, id_usuario = ? WHERE id_prova = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, prova.getIdQueixa());
            stmt.setString(2, prova.getTipo());
            stmt.setString(3, prova.getDescricao());
            stmt.setString(4, prova.getCaminhoArquivo());
            stmt.setTimestamp(5, prova.getDataColeta() != null ? new Timestamp(prova.getDataColeta().getTime()) : null);
            stmt.setInt(6, prova.getIdUsuario());
            stmt.setInt(7, prova.getIdProva());
            
            LOGGER.info("Executando atualização: " + stmt.toString());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                LOGGER.info("Prova atualizada com sucesso. ID: " + prova.getIdProva());
                return true;
            }
            LOGGER.warning("Nenhuma linha afetada na atualização");
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar prova", e);
            Conexao.rollback(conn);
            return false;
        } finally {
            Conexao.fechar(conn, stmt);
        }
    }

    public boolean remover(int id) {
        Prova prova = buscarPorId(id);
        if (prova == null) {
            LOGGER.warning("Prova não encontrada para remoção. ID: " + id);
            return false;
        }
        
        String sql = "DELETE FROM provas WHERE id_prova = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexao.conectar();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            LOGGER.info("Executando remoção: " + stmt.toString());
            
            boolean sucesso = stmt.executeUpdate() > 0;
            
            if (sucesso && prova.getCaminhoArquivo() != null) {
                try {
                    Files.deleteIfExists(new File(prova.getCaminhoArquivo()).toPath());
                    LOGGER.info("Arquivo removido: " + prova.getCaminhoArquivo());
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Aviso: Arquivo não pôde ser removido", e);
                }
            }
            return sucesso;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao remover prova", e);
            Conexao.rollback(conn);
            return false;
        } finally {
            Conexao.fechar(conn, stmt);
        }
    }
}
