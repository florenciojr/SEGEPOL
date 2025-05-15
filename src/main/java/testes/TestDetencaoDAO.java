/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testes;

/**
 *
 * @author JR5
 */


import dao.DetencaoDAO;
import model.Detencao;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class TestDetencaoDAO {

    public static void main(String[] args) {
        try {
            DetencaoDAO detencaoDAO = new DetencaoDAO();
            
            // 1. Teste CREATE
            System.out.println("=== TESTE CREATE ===");
            Detencao novaDetencao = new Detencao();
            novaDetencao.setIdCidadao(1); // Substitua por um ID de cidadão existente
            novaDetencao.setMotivo("Suspeita de roubo");
            novaDetencao.setLocalDetencao("Centro da cidade");
            novaDetencao.setStatus("Detido");
            novaDetencao.setIdUsuarioResponsavel(1); // Substitua por um ID de usuário existente
            novaDetencao.setDataDetencao(new Timestamp(System.currentTimeMillis()));
            
            boolean criado = detencaoDAO.create(novaDetencao);
            if (criado) {
                System.out.println("Detenção criada com sucesso! ID: " + novaDetencao.getIdDetencao());
            } else {
                System.out.println("Falha ao criar detenção.");
            }
            
            // 2. Teste READ
            System.out.println("\n=== TESTE READ ===");
            if (criado) {
                Detencao detencaoLida = detencaoDAO.read(novaDetencao.getIdDetencao());
                if (detencaoLida != null) {
                    System.out.println("Detenção encontrada:");
                    System.out.println("ID: " + detencaoLida.getIdDetencao());
                    System.out.println("ID Cidadão: " + detencaoLida.getIdCidadao());
                    System.out.println("Motivo: " + detencaoLida.getMotivo());
                    System.out.println("Local: " + detencaoLida.getLocalDetencao());
                    System.out.println("Status: " + detencaoLida.getStatus());
                    System.out.println("ID Usuário Responsável: " + detencaoLida.getIdUsuarioResponsavel());
                    System.out.println("Data: " + detencaoLida.getDataDetencao());
                } else {
                    System.out.println("Detenção não encontrada.");
                }
            }
            
            // 3. Teste UPDATE
            System.out.println("\n=== TESTE UPDATE ===");
            if (criado) {
                novaDetencao.setMotivo("Suspeita de furto qualificado");
                novaDetencao.setStatus("Liberado");
                boolean atualizado = detencaoDAO.update(novaDetencao);
                if (atualizado) {
                    System.out.println("Detenção atualizada com sucesso!");
                    Detencao detencaoAtualizada = detencaoDAO.read(novaDetencao.getIdDetencao());
                    System.out.println("Novo motivo: " + detencaoAtualizada.getMotivo());
                    System.out.println("Novo status: " + detencaoAtualizada.getStatus());
                } else {
                    System.out.println("Falha ao atualizar detenção.");
                }
            }
            
            // 4. Teste LIST ALL
            System.out.println("\n=== TESTE LIST ALL ===");
            List<Detencao> todasDetencoes = detencaoDAO.listAll();
            System.out.println("Total de detenções: " + todasDetencoes.size());
            for (Detencao d : todasDetencoes) {
                System.out.println("ID: " + d.getIdDetencao() + 
                                 " | Cidadão: " + d.getIdCidadao() + 
                                 " | Motivo: " + d.getMotivo() + 
                                 " | Status: " + d.getStatus());
            }
            
            // 5. Teste FIND BY CIDADAO
            System.out.println("\n=== TESTE FIND BY CIDADAO ===");
            List<Detencao> detencoesCidadao = detencaoDAO.findByCidadao(1); // Substitua por um ID de cidadão existente
            System.out.println("Detenções para o cidadão 1: " + detencoesCidadao.size());
            for (Detencao d : detencoesCidadao) {
                System.out.println("ID: " + d.getIdDetencao() + 
                                 " | Data: " + d.getDataDetencao() + 
                                 " | Motivo: " + d.getMotivo());
            }
            
            // 6. Teste DROPDOWN CIDADÃOS
            System.out.println("\n=== TESTE DROPDOWN CIDADÃOS ===");
            List<Map<String, String>> cidadaosDropdown = detencaoDAO.listarCidadaosParaDropdown();
            System.out.println("Cidadãos para dropdown:");
            for (Map<String, String> cidadao : cidadaosDropdown) {
                System.out.println(cidadao.get("key") + " - " + cidadao.get("value"));
            }
            
            // 7. Teste DROPDOWN USUÁRIOS
            System.out.println("\n=== TESTE DROPDOWN USUÁRIOS ===");
            List<Map<String, String>> usuariosDropdown = detencaoDAO.listarUsuariosParaDropdown();
            System.out.println("Usuários para dropdown:");
            for (Map<String, String> usuario : usuariosDropdown) {
                System.out.println(usuario.get("key") + " - " + usuario.get("value"));
            }
            
            // 8. Teste DELETE
//            System.out.println("\n=== TESTE DELETE ===");
//            if (criado) {
//                boolean deletado = detencaoDAO.delete(novaDetencao.getIdDetencao());
//                if (deletado) {
//                    System.out.println("Detenção deletada com sucesso!");
//                } else {
//                    System.out.println("Falha ao deletar detenção.");
//                }
//            }
//            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro durante os testes: " + e.getMessage());
        }
    }
}
