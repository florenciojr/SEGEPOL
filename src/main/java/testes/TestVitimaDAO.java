/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testes;

/**
 *
 * @author JR5
 */


import dao.VitimaDAO;
import model.Vitima;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TestVitimaDAO {

    public static void main(String[] args) {
        try {
            VitimaDAO vitimaDAO = new VitimaDAO();
            
            // Teste 1: Criar nova vítima
            testCreate(vitimaDAO);
            
            // Teste 2: Buscar vítima por ID
            testRead(vitimaDAO);
            
            // Teste 3: Listar todas as vítimas
            testListAll(vitimaDAO);
            
            // Teste 4: Atualizar vítima
            testUpdate(vitimaDAO);
            
            // Teste 5: Deletar vítima
//            testDelete(vitimaDAO);
            
            // Teste 6: Métodos auxiliares
            testAuxiliaryMethods(vitimaDAO);
            
            // Teste 7: Relatórios
            testReports(vitimaDAO);
            
        } catch (SQLException e) {
            System.err.println("Erro ao testar VitimaDAO:");
            e.printStackTrace();
        }
    }

    private static void testCreate(VitimaDAO vitimaDAO) throws SQLException {
        System.out.println("\n=== TESTE CREATE ===");
        
        // Criar uma nova vítima (substitua os IDs por valores válidos do seu banco)
        Vitima novaVitima = new Vitima();
        novaVitima.setIdQueixa(2); // ID de queixa existente
        novaVitima.setIdCidadao(8); // ID de cidadão existente
        novaVitima.setDescricao("Vítima de teste criada pelo método main");
        novaVitima.setTipoVitima(Vitima.TipoVitima.Direta);
        
        boolean criado = vitimaDAO.create(novaVitima);
        System.out.println("Vítima criada com sucesso? " + criado);
        
        if (criado) {
            System.out.println("ID da nova vítima: " + novaVitima.getIdVitima());
        }
    }

    private static void testRead(VitimaDAO vitimaDAO) throws SQLException {
        System.out.println("\n=== TESTE READ ===");
        
        // Buscar uma vítima existente (substitua pelo ID de uma vítima que existe)
        int idVitima = 1; // ID de vítima existente
        Vitima vitima = vitimaDAO.read(idVitima);
        
        if (vitima != null) {
            System.out.println("Vítima encontrada:");
            System.out.println("ID: " + vitima.getIdVitima());
            System.out.println("Queixa ID: " + vitima.getIdQueixa());
            System.out.println("Cidadão ID: " + vitima.getIdCidadao());
            System.out.println("Descrição: " + vitima.getDescricao());
            System.out.println("Tipo: " + vitima.getTipoVitima());
            System.out.println("Data Registro: " + vitima.getDataRegistro());
        } else {
            System.out.println("Vítima com ID " + idVitima + " não encontrada");
        }
    }

    private static void testListAll(VitimaDAO vitimaDAO) throws SQLException {
        System.out.println("\n=== TESTE LIST ALL ===");
        
        // Listar todas as vítimas com paginação
        int offset = 0;
        int limit = 5;
        List<Vitima> vitimas = vitimaDAO.listAll(offset, limit);
        
        System.out.println("Total de vítimas: " + vitimaDAO.countAll());
        System.out.println("Listando " + vitimas.size() + " vítimas:");
        
        for (Vitima v : vitimas) {
            System.out.println("- ID: " + v.getIdVitima() + 
                    ", Queixa: " + v.getIdQueixa() + 
                    ", Cidadão: " + v.getIdCidadao() +
                    ", Tipo: " + v.getTipoVitima());
        }
    }

    private static void testUpdate(VitimaDAO vitimaDAO) throws SQLException {
        System.out.println("\n=== TESTE UPDATE ===");
        
        // Primeiro, vamos buscar uma vítima existente para atualizar
        int idVitima = 1; // Substitua por um ID existente
        Vitima vitima = vitimaDAO.read(idVitima);
        
        if (vitima != null) {
            System.out.println("Antes da atualização:");
            System.out.println("Descrição: " + vitima.getDescricao());
            
            // Modificar os dados
            vitima.setDescricao("Descrição atualizada pelo teste");
            vitima.setTipoVitima(Vitima.TipoVitima.Direta);
            
            boolean atualizado = vitimaDAO.update(vitima);
            System.out.println("Vítima atualizada com sucesso? " + atualizado);
            
            if (atualizado) {
                Vitima vitimaAtualizada = vitimaDAO.read(idVitima);
                System.out.println("Depois da atualização:");
                System.out.println("Descrição: " + vitimaAtualizada.getDescricao());
                System.out.println("Tipo: " + vitimaAtualizada.getTipoVitima());
            }
        } else {
            System.out.println("Vítima com ID " + idVitima + " não encontrada para atualização");
        }
    }

//    private static void testDelete(VitimaDAO vitimaDAO) throws SQLException {
//        System.out.println("\n=== TESTE DELETE ===");
//        
//        // Primeiro, vamos criar uma vítima para depois deletar
//        Vitima vitimaParaDeletar = new Vitima();
//        vitimaParaDeletar.setIdQueixa(1); // ID de queixa existente
//        vitimaParaDeletar.setIdCidadao(4); // ID de cidadão existente
//        vitimaParaDeletar.setDescricao("Vítima para teste de deleção");
//        vitimaParaDeletar.setTipoVitima(Vitima.TipoVitima.Familiar);
//        
//        boolean criado = vitimaDAO.create(vitimaParaDeletar);
//        
//        if (criado) {
//            System.out.println("Vítima criada para teste de deleção com ID: " + vitimaParaDeletar.getIdVitima());
//            
//            // Agora deletamos
//            boolean deletado = vitimaDAO.delete(vitimaParaDeletar.getIdVitima());
//            System.out.println("Vítima deletada com sucesso? " + deletado);
//            
//            // Verificamos se realmente foi deletada
//            Vitima vitimaDeletada = vitimaDAO.read(vitimaParaDeletar.getIdVitima());
//            System.out.println("Tentativa de buscar vítima deletada: " + (vitimaDeletada == null ? "não encontrada" : "encontrada (problema!)"));
//        } else {
//            System.out.println("Não foi possível criar vítima para teste de deleção");
//        }
//    }

    private static void testAuxiliaryMethods(VitimaDAO vitimaDAO) throws SQLException {
        System.out.println("\n=== TESTE MÉTODOS AUXILIARES ===");
        
        // Testar listarQueixasParaDropdown
        System.out.println("\nQueixas para dropdown:");
        List<Map<String, String>> queixas = vitimaDAO.listarQueixasParaDropdown();
        for (Map<String, String> queixa : queixas) {
            System.out.println("ID: " + queixa.get("key") + " - Descrição: " + queixa.get("value"));
        }
        
        // Testar listarCidadaosParaDropdown
        System.out.println("\nCidadãos para dropdown:");
        List<Map<String, String>> cidadaos = vitimaDAO.listarCidadaosParaDropdown();
        for (Map<String, String> cidadao : cidadaos) {
            System.out.println("ID: " + cidadao.get("key") + " - Nome: " + cidadao.get("value"));
        }
        
        // Testar findByQueixa
        int idQueixa = 1; // Substitua por um ID de queixa existente
        System.out.println("\nVítimas da queixa " + idQueixa + ":");
        List<Vitima> vitimasQueixa = vitimaDAO.findByQueixa(idQueixa, 0, 10);
        for (Vitima v : vitimasQueixa) {
            System.out.println("- ID: " + v.getIdVitima() + ", Cidadão: " + v.getIdCidadao());
        }
        
        // Testar findDetailedByQueixa
        System.out.println("\nDetalhes das vítimas da queixa " + idQueixa + ":");
        List<Map<String, Object>> vitimasDetalhadas = vitimaDAO.findDetailedByQueixa(idQueixa);
        for (Map<String, Object> v : vitimasDetalhadas) {
            System.out.println("- Nome: " + v.get("nome") + 
                    ", Gênero: " + v.get("genero") + 
                    ", Tipo: " + v.get("tipo_vitima"));
        }
    }

    private static void testReports(VitimaDAO vitimaDAO) throws SQLException {
        System.out.println("\n=== TESTE RELATÓRIOS ===");
        
        // Contagem por tipo de vítima
        System.out.println("\nContagem por tipo de vítima:");
        Map<String, Integer> contagemPorTipo = vitimaDAO.countByTipoVitima();
        for (Map.Entry<String, Integer> entry : contagemPorTipo.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        
        // Top cidadãos vítimas
        System.out.println("\nTop 5 cidadãos vítimas:");
        List<Map<String, Object>> topCidadaos = vitimaDAO.findTopCidadaosVitimas(5);
        for (Map<String, Object> cidadao : topCidadaos) {
            System.out.println("ID: " + cidadao.get("id_cidadao") + 
                    ", Nome: " + cidadao.get("nome") + 
                    ", Total: " + cidadao.get("total"));
        }
    }
}
