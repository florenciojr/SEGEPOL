package dao;

import model.MandadoPrisao;
import model.Suspeito;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MandadoPrisaoDAO {
    private Connection conexao;

    public MandadoPrisaoDAO() {
        this.conexao = Conexao.conectar();
    }

    // Inserir novo mandado
    public boolean inserir(MandadoPrisao mandado) {
        String sql = "INSERT INTO mandados_prisao (id_suspeito, numero_mandado, data_emissao, "
                   + "data_validade, tipo, status, descricao, id_usuario_emissor, versao) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, mandado.getIdSuspeito());
            stmt.setString(2, mandado.getNumeroMandado());
            stmt.setDate(3, new java.sql.Date(mandado.getDataEmissao().getTime()));
            stmt.setDate(4, mandado.getDataValidade() != null ? 
                new java.sql.Date(mandado.getDataValidade().getTime()) : null);
            stmt.setString(5, mandado.getTipo());
            stmt.setString(6, mandado.getStatus());
            stmt.setString(7, mandado.getDescricao());
            stmt.setInt(8, mandado.getIdUsuarioEmissor());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        mandado.setIdMandado(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir mandado: " + e.getMessage(), e);
        }
    }

    // Atualizar mandado
    public boolean atualizar(MandadoPrisao mandado) {
        String sql = "UPDATE mandados_prisao SET id_suspeito=?, numero_mandado=?, data_emissao=?, "
                   + "data_validade=?, tipo=?, status=?, descricao=?, versao=versao+1 "
                   + "WHERE id_mandado=? AND versao=?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, mandado.getIdSuspeito());
            stmt.setString(2, mandado.getNumeroMandado());
            stmt.setDate(3, new java.sql.Date(mandado.getDataEmissao().getTime()));
            stmt.setDate(4, mandado.getDataValidade() != null ? 
                new java.sql.Date(mandado.getDataValidade().getTime()) : null);
            stmt.setString(5, mandado.getTipo());
            stmt.setString(6, mandado.getStatus());
            stmt.setString(7, mandado.getDescricao());
            stmt.setInt(8, mandado.getIdMandado());
            stmt.setInt(9, mandado.getVersao());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar mandado: " + e.getMessage(), e);
        }
    }

    // Buscar por ID
public MandadoPrisao buscarPorId(int id) {
    String sql = "SELECT m.*, c.nome as nome_suspeito FROM mandados_prisao m " +
               "JOIN suspeitos s ON m.id_suspeito = s.id_suspeito " +
               "JOIN cidadaos c ON s.id_cidadao = c.id_cidadao " +
               "WHERE m.id_mandado = ?";
    
    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        stmt.setInt(1, id);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapearResultSetParaMandado(rs);
            } else {
                throw new RuntimeException("Mandado não encontrado com ID: " + id);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao buscar mandado por ID: " + e.getMessage(), e);
    }
}

    // Listar todos com paginação
    public List<MandadoPrisao> listarTodos(int pagina, int registrosPorPagina) {
        List<MandadoPrisao> mandados = new ArrayList<>();
        String sql = "SELECT m.*, c.nome as nome_suspeito FROM mandados_prisao m "
                   + "JOIN suspeitos s ON m.id_suspeito = s.id_suspeito "
                   + "JOIN cidadaos c ON s.id_cidadao = c.id_cidadao "
                   + "ORDER BY m.data_emissao DESC LIMIT ? OFFSET ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, registrosPorPagina);
            stmt.setInt(2, (pagina - 1) * registrosPorPagina);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mandados.add(mapearResultSetParaMandado(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar mandados: " + e.getMessage(), e);
        }
        return mandados;
    }

    // Listar por status
    public List<MandadoPrisao> listarPorStatus(String status) {
        List<MandadoPrisao> mandados = new ArrayList<>();
        String sql = "SELECT m.*, c.nome as nome_suspeito FROM mandados_prisao m "
                   + "JOIN suspeitos s ON m.id_suspeito = s.id_suspeito "
                   + "JOIN cidadaos c ON s.id_cidadao = c.id_cidadao "
                   + "WHERE m.status = ? ORDER BY m.data_emissao DESC";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mandados.add(mapearResultSetParaMandado(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar mandados por status: " + e.getMessage(), e);
        }
        return mandados;
    }

    // Remover mandado
    public boolean remover(int id) {
        String sql = "DELETE FROM mandados_prisao WHERE id_mandado = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover mandado: " + e.getMessage(), e);
        }
    }

    // Contar total de registros
    public int contarTotalRegistros() {
        String sql = "SELECT COUNT(*) as total FROM mandados_prisao";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar mandados: " + e.getMessage(), e);
        }
    }

    // Verificar se número de mandado já existe
    public boolean existeNumeroMandado(String numeroMandado) {
        String sql = "SELECT 1 FROM mandados_prisao WHERE numero_mandado = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, numeroMandado);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar número do mandado: " + e.getMessage(), e);
        }
    }

    // Mapear ResultSet para objeto MandadoPrisao
    private MandadoPrisao mapearResultSetParaMandado(ResultSet rs) throws SQLException {
        MandadoPrisao mandado = new MandadoPrisao();
        mandado.setIdMandado(rs.getInt("id_mandado"));
        mandado.setIdSuspeito(rs.getInt("id_suspeito"));
        mandado.setNumeroMandado(rs.getString("numero_mandado"));
        mandado.setDataEmissao(rs.getDate("data_emissao"));
        mandado.setDataValidade(rs.getDate("data_validade"));
        mandado.setTipo(rs.getString("tipo"));
        mandado.setStatus(rs.getString("status"));
        mandado.setDescricao(rs.getString("descricao"));
        mandado.setIdUsuarioEmissor(rs.getInt("id_usuario_emissor"));
        mandado.setVersao(rs.getInt("versao"));
        
        // Configurar suspeito
        Suspeito suspeito = new Suspeito();
        suspeito.setIdSuspeito(rs.getInt("id_suspeito"));
        suspeito.setCidadaoNome(rs.getString("nome_suspeito"));
        mandado.setSuspeito(suspeito);
        
        return mandado;
    }
    
    
    public List<MandadoPrisao> buscarPorTermo(String termo) {
    List<MandadoPrisao> mandados = new ArrayList<>();
    String sql = "SELECT m.*, c.nome as nome_suspeito FROM mandados_prisao m " +
               "JOIN suspeitos s ON m.id_suspeito = s.id_suspeito " +
               "JOIN cidadaos c ON s.id_cidadao = c.id_cidadao " +
               "WHERE m.numero_mandado LIKE ? " +
               "   OR c.nome LIKE ? " +
               "   OR m.descricao LIKE ? " +
               "ORDER BY m.data_emissao DESC";
    
    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        String termoLike = "%" + termo + "%";
        stmt.setString(1, termoLike);
        stmt.setString(2, termoLike);
        stmt.setString(3, termoLike);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                mandados.add(mapearResultSetParaMandado(rs));
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao buscar mandados por termo: " + e.getMessage(), e);
    }
    return mandados;
}

    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar conexão: " + e.getMessage(), e);
        }
    }
}
