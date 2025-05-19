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
import java.util.Collections;
import java.util.List;
import model.Cidadao;
import util.Conexao;




public class CidadaoDAO {
    
public List<String> getClassificacoesDisponiveis() throws SQLException {
    List<String> classificacoes = new ArrayList<>();
    String sql = "SELECT COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS " +
                 "WHERE TABLE_NAME = 'cidadaos' AND COLUMN_NAME = 'classificacao'";
    
    try (Connection conn = Conexao.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        if (rs.next()) {
            String enumValues = rs.getString(1);
            // Extrai os valores: enum('Valor1','Valor2',...)
            String[] values = enumValues.replace("enum(", "")
                                       .replace(")", "")
                                       .replaceAll("'", "")
                                       .split(",");
            
            Collections.addAll(classificacoes, values);
        }
    }
    return classificacoes;
}

// Método inserirCidadao atualizado
public void inserirCidadao(Cidadao cidadao) {
    String sql = "INSERT INTO cidadaos (nome, genero, data_nascimento, documento_identificacao, "
               + "tipo_documento, telefone, email, naturalidade, rua, bairro, cidade, provincia, "
               + "caminho_imagem, classificacao, caracteristicas_fisicas) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, cidadao.getNome());
        stmt.setString(2, cidadao.getGenero());
        stmt.setDate(3, Date.valueOf(cidadao.getDataNascimento()));
        stmt.setString(4, cidadao.getDocumentoIdentificacao());
        stmt.setString(5, cidadao.getTipoDocumento());
        stmt.setString(6, cidadao.getTelefone());
        stmt.setString(7, cidadao.getEmail());
        stmt.setString(8, cidadao.getNaturalidade());
        stmt.setString(9, cidadao.getRua());
        stmt.setString(10, cidadao.getBairro());
        stmt.setString(11, cidadao.getCidade());
        stmt.setString(12, cidadao.getProvincia());
        stmt.setString(13, cidadao.getCaminhoImagem());
        stmt.setString(14, mapearClassificacaoParaBanco(cidadao.getClassificacao()));
        stmt.setString(15, cidadao.getCaracteristicasFisicas());

        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public List<Cidadao> listarCidadaos() {
    List<Cidadao> cidadaos = new ArrayList<>();
    String sql = "SELECT * FROM cidadaos";

    try (Connection conn = Conexao.conectar();
         Statement stmt = conn.createStatement(); 
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Cidadao cidadao = new Cidadao();
            cidadao.setIdCidadao(rs.getInt("id_Cidadao"));
            cidadao.setNome(rs.getString("nome"));
            cidadao.setGenero(rs.getString("genero"));
            cidadao.setDataNascimento(rs.getDate("data_Nascimento").toLocalDate());
            cidadao.setDocumentoIdentificacao(rs.getString("documento_Identificacao"));
            cidadao.setTelefone(rs.getString("telefone"));
            cidadao.setEmail(rs.getString("email"));
            cidadao.setNaturalidade(rs.getString("naturalidade"));
            cidadao.setRua(rs.getString("rua"));
            cidadao.setBairro(rs.getString("bairro"));
            cidadao.setCidade(rs.getString("cidade"));
            cidadao.setProvincia(rs.getString("provincia"));
            cidadao.setCaminhoImagem(rs.getString("caminho_imagem"));
            cidadao.setClassificacao(rs.getString("classificacao"));
            cidadao.setCaracteristicasFisicas(rs.getString("caracteristicas_fisicas"));
            cidadaos.add(cidadao);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cidadaos;
}

public Cidadao buscarCidadaoPorId(int id) {
    String sql = "SELECT * FROM cidadaos WHERE id_cidadao = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
         
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            Cidadao cidadao = new Cidadao();
            cidadao.setIdCidadao(rs.getInt("id_cidadao"));
            cidadao.setNome(rs.getString("nome"));
            cidadao.setGenero(rs.getString("genero"));
            cidadao.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
            cidadao.setDocumentoIdentificacao(rs.getString("documento_identificacao"));
            cidadao.setTipoDocumento(rs.getString("tipo_documento"));
            cidadao.setTelefone(rs.getString("telefone"));
            cidadao.setEmail(rs.getString("email"));
            cidadao.setNaturalidade(rs.getString("naturalidade"));
            cidadao.setRua(rs.getString("rua"));
            cidadao.setBairro(rs.getString("bairro"));
            cidadao.setCidade(rs.getString("cidade"));
            cidadao.setProvincia(rs.getString("provincia"));
            cidadao.setCaminhoImagem(rs.getString("caminho_imagem"));
            cidadao.setCaracteristicasFisicas(rs.getString("caracteristicas_fisicas"));
            cidadao.setClassificacao(rs.getString("classificacao"));
             cidadao.setCaracteristicasFisicas(rs.getString("caracteristicas_fisicas"));
            
            return cidadao;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
   public Cidadao buscarCidadaoPorDocumento(String documentoIdentificacao) {
        String sql = "SELECT * FROM cidadaos WHERE documento_identificacao = ?";
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documentoIdentificacao);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cidadao cidadao = new Cidadao();
                cidadao.setIdCidadao(rs.getInt("id_cidadao"));
                cidadao.setNome(rs.getString("nome"));
                cidadao.setGenero(rs.getString("genero"));
                cidadao.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                cidadao.setDocumentoIdentificacao(rs.getString("documento_identificacao"));
                cidadao.setTelefone(rs.getString("telefone"));
                cidadao.setEmail(rs.getString("email"));
                cidadao.setNaturalidade(rs.getString("naturalidade"));
                cidadao.setRua(rs.getString("rua"));
                cidadao.setBairro(rs.getString("bairro"));
                cidadao.setCidade(rs.getString("cidade"));
                cidadao.setProvincia(rs.getString("provincia"));
                cidadao.setCaminhoImagem(rs.getString("caminho_imagem"));
                 cidadao.setCaracteristicasFisicas(rs.getString("caracteristicas_fisicas"));
                return cidadao;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


// Método atualizarCidadao atualizado
public void atualizarCidadao(Cidadao cidadao) {
    String sql = "UPDATE cidadaos SET nome = ?, genero = ?, data_nascimento = ?, "
               + "documento_identificacao = ?, tipo_documento = ?, telefone = ?, email = ?, "
               + "naturalidade = ?, rua = ?, bairro = ?, cidade = ?, provincia = ?, "
               + "caminho_imagem = ?, classificacao = ?, caracteristicas_fisicas = ? " 
               + "WHERE id_cidadao = ?";

    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        // Ordem dos parâmetros corrigida
        stmt.setString(1, cidadao.getNome());
        stmt.setString(2, cidadao.getGenero());
        stmt.setDate(3, Date.valueOf(cidadao.getDataNascimento()));
        stmt.setString(4, cidadao.getDocumentoIdentificacao());
        stmt.setString(5, cidadao.getTipoDocumento());
        stmt.setString(6, cidadao.getTelefone());
        stmt.setString(7, cidadao.getEmail());
        stmt.setString(8, cidadao.getNaturalidade());
        stmt.setString(9, cidadao.getRua());
        stmt.setString(10, cidadao.getBairro());
        stmt.setString(11, cidadao.getCidade());
        stmt.setString(12, cidadao.getProvincia());
        stmt.setString(13, cidadao.getCaminhoImagem());
        stmt.setString(14, mapearClassificacaoParaBanco(cidadao.getClassificacao()));
        stmt.setString(15, cidadao.getCaracteristicasFisicas());
        stmt.setInt(16, cidadao.getIdCidadao());

        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public void deletarCidadao(int id) {
        String sql = "DELETE FROM cidadaos WHERE id_cidadao = ?";

        try (Connection conn = Conexao.conectar(); // Usando a conexão fornecida pela classe Conexao
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Cidadão deletado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }}
    
        
        
public List<Cidadao> buscarCidadaosPorNome(String nome) {
    List<Cidadao> cidadaos = new ArrayList<>();
    String sql = "SELECT * FROM cidadaos WHERE LOWER(nome) LIKE LOWER(?)";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, "%" + nome + "%");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Cidadao cidadao = new Cidadao();
      
            cidadao.setIdCidadao(rs.getInt("id_cidadao"));
            cidadao.setNome(rs.getString("nome"));
            cidadao.setGenero(rs.getString("genero"));
            cidadao.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
            cidadao.setDocumentoIdentificacao(rs.getString("documento_identificacao"));
            cidadao.setTelefone(rs.getString("telefone"));
            cidadao.setEmail(rs.getString("email"));
            cidadao.setNaturalidade(rs.getString("naturalidade"));
            cidadao.setRua(rs.getString("rua"));
            cidadao.setBairro(rs.getString("bairro"));
            cidadao.setCidade(rs.getString("cidade"));
            cidadao.setProvincia(rs.getString("provincia"));
             cidadao.setCaracteristicasFisicas(rs.getString("caracteristicas_fisicas"));
            cidadaos.add(cidadao);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cidadaos;
}

public List<Cidadao> buscarCidadaosPorCidade(String cidade) {
    List<Cidadao> cidadaos = new ArrayList<>();
    String sql = "SELECT * FROM cidadaos WHERE cidade = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, cidade);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Cidadao cidadao = new Cidadao();
            // Preenche os dados do cidadão igual no método anterior
            cidadao.setIdCidadao(rs.getInt("id_cidadao"));
            cidadao.setNome(rs.getString("nome"));
            // ... (preencher todos os campos como nos outros métodos)
            cidadaos.add(cidadao);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cidadaos;
}

public boolean verificarDocumentoExistente(String documentoIdentificacao) {
    String sql = "SELECT COUNT(*) FROM cidadaos WHERE documento_identificacao = ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, documentoIdentificacao);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public int contarTotalCidadaos() {
    String sql = "SELECT COUNT(*) FROM cidadaos";
    
    try (Connection conn = Conexao.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

public List<Cidadao> listarCidadaosComPaginacao(int offset, int limit) {
    List<Cidadao> cidadaos = new ArrayList<>();
    String sql = "SELECT * FROM cidadaos LIMIT ? OFFSET ?";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, limit);
        stmt.setInt(2, offset);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Cidadao cidadao = new Cidadao();
            // Preenche os dados do cidadão igual nos outros métodos
            cidadao.setIdCidadao(rs.getInt("id_cidadao"));
            cidadao.setNome(rs.getString("nome"));
            // ... (preencher todos os campos)
            cidadaos.add(cidadao);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cidadaos;
}

private String mapearClassificacaoParaBanco(String classificacao) {
    switch(classificacao) {
        case "Cidadão Comum":
            return "Comum";
        case "Vítima":
            return "Vitima";
        case "Suspeito":
            return "Suspeito";
        case "Testemunha":
            return "Testemunha";
        case "Informante":
            return "Informante";
        case "Denunciante":
            return "Denunciante";
             case "Detido":
            return "Detido";
        default:
            return "Comum";
    }
}

    


}



