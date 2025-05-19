/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testes;

/**
 *
 * @author JR5
 */



import dao.IntimacaoDAO;
import model.Intimacao;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TestIntimacaoDAO {

    public static void main(String[] args) {
        try {
            IntimacaoDAO intimacaoDAO = new IntimacaoDAO();
            
            // Teste CREATE
            System.out.println("=== TESTE CREATE ===");
            Intimacao novaIntimacao = new Intimacao();
            novaIntimacao.setIdCidadao(1); // deve existir na tabela cidadaos
            novaIntimacao.setIdUsuario(1); // deve existir na tabela usuarios
            novaIntimacao.setIdQueixa(1); // deve existir na tabela queixas
            novaIntimacao.setMotivo("Prestar esclarecimentos");
            novaIntimacao.setDataEmissao(LocalDate.now());
novaIntimacao.setDataComparecimento(LocalDate.now().plusDays(7));       novaIntimacao.setLocalComparecimento("Delegacia Central");
            novaIntimacao.setStatus("Pendente");
            novaIntimacao.setObservacoes("Trazer documentos de identificação");
            
            boolean criado = intimacaoDAO.inserirIntimacao(novaIntimacao);
            if (criado) {
                System.out.println("Intimação criada com sucesso! ID: " + novaIntimacao.getIdIntimacao());
            } else {
                System.out.println("Falha ao criar intimação.");
            }
            
            // Teste READ
            System.out.println("\n=== TESTE READ ===");
            if (criado) {
                Intimacao intimacaoLida = intimacaoDAO.buscarIntimacaoPorId(novaIntimacao.getIdIntimacao());
                if (intimacaoLida != null) {
                    System.out.println("Intimação encontrada:");
                    System.out.println("ID: " + intimacaoLida.getIdIntimacao());
                    System.out.println("ID Cidadão: " + intimacaoLida.getIdCidadao());
                    System.out.println("ID Usuário: " + intimacaoLida.getIdUsuario());
                    System.out.println("ID Queixa: " + intimacaoLida.getIdQueixa());
                    System.out.println("Motivo: " + intimacaoLida.getMotivo());
                    System.out.println("Data Emissão: " + intimacaoLida.getDataEmissao());
                    System.out.println("Data Comparecimento: " + intimacaoLida.getDataComparecimento());
                    System.out.println("Local: " + intimacaoLida.getLocalComparecimento());
                    System.out.println("Status: " + intimacaoLida.getStatus());
                    System.out.println("Observações: " + intimacaoLida.getObservacoes());
                } else {
                    System.out.println("Intimação não encontrada.");
                }
            }
            
            // Teste UPDATE
            System.out.println("\n=== TESTE UPDATE ===");
            if (criado) {
                novaIntimacao.setStatus("Compareceu");
                novaIntimacao.setObservacoes("Compareceu na data marcada");
                boolean atualizado = intimacaoDAO.atualizarIntimacao(novaIntimacao);
                if (atualizado) {
                    System.out.println("Intimação atualizada com sucesso!");
                    Intimacao intimacaoAtualizada = intimacaoDAO.buscarIntimacaoPorId(novaIntimacao.getIdIntimacao());
                    System.out.println("Novo status: " + intimacaoAtualizada.getStatus());
                    System.out.println("Novas observações: " + intimacaoAtualizada.getObservacoes());
                } else {
                    System.out.println("Falha ao atualizar intimação.");
                }
            }
            
            // Teste LIST ALL
            System.out.println("\n=== TESTE LIST ALL ===");
            List<Intimacao> todasIntimacoes = intimacaoDAO.listarIntimacoes();
            System.out.println("Total de intimações: " + todasIntimacoes.size());
            for (Intimacao i : todasIntimacoes) {
                System.out.println("ID: " + i.getIdIntimacao() + 
                                 " | Cidadão: " + i.getIdCidadao() + 
                                 " | Queixa: " + i.getIdQueixa() + 
                                 " | Status: " + i.getStatus() +
                                 " | Data: " + i.getDataComparecimento());
            }
            
            // Teste LISTAR POR CIDADAO
//            System.out.println("\n=== TESTE LISTAR POR CIDADAO ===");
//            List<Intimacao> intimacoesCidadao = intimacaoDAO.(novaIntimacao.getIdCidadao());
//            System.out.println("Intimações para o cidadão 1: " + intimacoesCidadao.size());
//            for (Intimacao i : intimacoesCidadao) {
//                System.out.println("ID: " + i.getIdIntimacao() + 
//                                 " | Motivo: " + i.getMotivo() + 
//                                 " | Status: " + i.getStatus());
//            }
            
            // Teste DROPDOWN CIDADÃOS
            System.out.println("\n=== TESTE DROPDOWN CIDADÃOS ===");
            Map<Integer, String> cidadaosDropdown = intimacaoDAO.listarCidadaosRecentes();
            System.out.println("Cidadãos para dropdown:");
            for (Map.Entry<Integer, String> entry : cidadaosDropdown.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
            
            // Teste DROPDOWN QUEIXAS
            System.out.println("\n=== TESTE DROPDOWN QUEIXAS ===");
            Map<Integer, String> queixasDropdown = intimacaoDAO.listarQueixasRecentes();
            System.out.println("Queixas para dropdown:");
            for (Map.Entry<Integer, String> entry : queixasDropdown.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
            
            // Teste STATUS PERMITIDOS
            System.out.println("\n=== TESTE STATUS PERMITIDOS ===");
            String[] statusPermitidos = intimacaoDAO.getStatusPermitidos();
            System.out.println("Status permitidos para intimações:");
            for (String status : statusPermitidos) {
                System.out.println(status);
            }
            
//            // Teste DELETE
//            System.out.println("\n=== TESTE DELETE ===");
//            if (criado) {
//                boolean deletado = intimacaoDAO.deletarIntimacao(novaIntimacao.getIdIntimacao());
//                if (deletado) {
//                    System.out.println("Intimação deletada com sucesso!");
//                } else {
//                    System.out.println("Falha ao deletar intimação.");
//                }
//            }
//            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro durante os testes: " + e.getMessage());
        }
    }
}
