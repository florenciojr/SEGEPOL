package testes;

import dao.CidadaoDAO;
import java.time.LocalDate;
import java.util.List;
import model.Cidadao;

public class TesteCidadaoDAO {

    public static void main(String[] args) {
        CidadaoDAO cidadaoDAO = new CidadaoDAO();
        
        // Teste de inserção
        System.out.println("=== TESTE DE INSERÇÃO ===");
        Cidadao novoCidadao = new Cidadao();
        novoCidadao.setNome("João da Silva macamo 2");
        novoCidadao.setGenero("Masculino");
        novoCidadao.setDataNascimento(LocalDate.of(1985, 5, 15));
        novoCidadao.setDocumentoIdentificacao("12345678901ab");
        novoCidadao.setTelefone("(11) 98765-4321");
        novoCidadao.setEmail("joao.silva@email.com");
        novoCidadao.setNaturalidade("São Paulo");
        novoCidadao.setRua("Rua das Flores");
        novoCidadao.setBairro("Centro");
        novoCidadao.setCidade("São Paulo");
        novoCidadao.setProvincia("SP");
        novoCidadao.setCaminhoImagem("uploads/foto_joao.jpg");
        novoCidadao.setClassificacao("Comum");
        novoCidadao.setCaracteristicasFisicas("preto e escuro");
        
        cidadaoDAO.inserirCidadao(novoCidadao);
        System.out.println("Cidadão inserido com sucesso!");
        
        // Teste de listagem
        System.out.println("\n=== TESTE DE LISTAGEM ===");
        List<Cidadao> cidadaos = cidadaoDAO.listarCidadaos();
        System.out.println("Total de cidadãos cadastrados: " + cidadaos.size());
        cidadaos.forEach(c -> System.out.println(c.getNome() + " - " + c.getClassificacao()));
        
        // Teste de busca por ID
        System.out.println("\n=== TESTE DE BUSCA POR ID ===");
        if (!cidadaos.isEmpty()) {
            Cidadao cidadao = cidadaoDAO.buscarCidadaoPorId(cidadaos.get(0).getIdCidadao());
            System.out.println("Cidadão encontrado: " + cidadao.getNome());
        }
        
        // Teste de busca por documento
        System.out.println("\n=== TESTE DE BUSCA POR DOCUMENTO ===");
        Cidadao cidadaoPorDoc = cidadaoDAO.buscarCidadaoPorDocumento("123456789");
        System.out.println("Cidadão encontrado por documento: " + 
                (cidadaoPorDoc != null ? cidadaoPorDoc.getNome() : "Não encontrado"));
        
        // Teste de atualização
        System.out.println("\n=== TESTE DE ATUALIZAÇÃO ===");
        if (cidadaoPorDoc != null) {
            cidadaoPorDoc.setClassificacao("Vítima");
            cidadaoDAO.atualizarCidadao(cidadaoPorDoc);
            System.out.println("Classificação do cidadão atualizada para: " + 
                    cidadaoDAO.buscarCidadaoPorId(cidadaoPorDoc.getIdCidadao()).getClassificacao());
        }
        
        // Teste de busca por nome
        System.out.println("\n=== TESTE DE BUSCA POR NOME ===");
        List<Cidadao> cidadaosPorNome = cidadaoDAO.buscarCidadaosPorNome("João");
        System.out.println("Cidadãos encontrados com 'João' no nome: " + cidadaosPorNome.size());
        
        // Teste de busca por cidade
        System.out.println("\n=== TESTE DE BUSCA POR CIDADE ===");
        List<Cidadao> cidadaosPorCidade = cidadaoDAO.buscarCidadaosPorCidade("São Paulo");
        System.out.println("Cidadãos encontrados em São Paulo: " + cidadaosPorCidade.size());
        
//        // Teste de busca por classificação
//        System.out.println("\n=== TESTE DE BUSCA POR CLASSIFICAÇÃO ===");
//        List<Cidadao> cidadaosPorClassificacao = cidadaoDAO.buscarCidadaosPorClassificacao("Vítima");
//        System.out.println("Cidadãos classificados como Vítima: " + cidadaosPorClassificacao.size());
//        
        // Teste de verificação de documento
        System.out.println("\n=== TESTE DE VERIFICAÇÃO DE DOCUMENTO ===");
        boolean documentoExiste = cidadaoDAO.verificarDocumentoExistente("123456789");
        System.out.println("Documento 123456789 existe? " + (documentoExiste ? "Sim" : "Não"));
        
        // Teste de contagem
//        System.out.println("\n=== TESTE DE CONTAGEM ===");
//        System.out.println("Total de cidadãos: " + cidadaoDAO.contarTotalCidadaos());
//        System.out.println("Total de Vítimas: " + cidadaoDAO.contarCidadaosPorClassificacao("Vítima"));
//        
        // Teste de paginação
        System.out.println("\n=== TESTE DE PAGINAÇÃO ===");
        List<Cidadao> cidadaosPaginados = cidadaoDAO.listarCidadaosComPaginacao(0, 5);
        System.out.println("Primeiros 5 cidadãos: " + cidadaosPaginados.size());
        
//        // Teste de paginação por classificação
//        System.out.println("\n=== TESTE DE PAGINAÇÃO POR CLASSIFICAÇÃO ===");
//        List<Cidadao> vitimasPaginadas = cidadaoDAO.listarCidadaosPorClassificacaoComPaginacao("Vítima", 0, 5);
//        System.out.println("Primeiras 5 vítimas: " + vitimasPaginadas.size());
        
        // Teste de exclusão (comente se não quiser excluir)
//        System.out.println("\n=== TESTE DE EXCLUSÃO ===");
//        if (cidadaoPorDoc != null) {
//            cidadaoDAO.deletarCidadao(cidadaoPorDoc.getIdCidadao());
//            System.out.println("Cidadão excluído. Verificando...");
//            System.out.println("Documento ainda existe? " + 
//                    (cidadaoDAO.verificarDocumentoExistente("123456789") ? "Sim" : "Não"));
//        }
    }
}
