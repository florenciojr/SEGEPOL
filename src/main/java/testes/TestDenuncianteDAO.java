/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testes;

/**
 *
 * @author JR5
 */




import dao.DenuncianteDAO;
import model.Denunciante;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class TestDenuncianteDAO {

    public static void main(String[] args) {
        try {
            DenuncianteDAO denuncianteDAO = new DenuncianteDAO();
            
            // Teste CREATE
            System.out.println("=== TESTE CREATE ===");
            Denunciante novoDenunciante = new Denunciante(
                0, // id será gerado
                1, // id_cidadao (deve existir na tabela cidadaos)
                1, // id_queixa (deve existir na tabela queixas)
                "Online",
                new Timestamp(System.currentTimeMillis())
            );
            
            boolean criado = denuncianteDAO.create(novoDenunciante);
            if (criado) {
                System.out.println("Denunciante criado com sucesso! ID: " + novoDenunciante.getIdDenunciante());
            } else {
                System.out.println("Falha ao criar denunciante.");
            }
            
            // Teste READ
            System.out.println("\n=== TESTE READ ===");
            if (criado) {
                Denunciante denuncianteLido = denuncianteDAO.read(novoDenunciante.getIdDenunciante());
                if (denuncianteLido != null) {
                    System.out.println("Denunciante encontrado:");
                    System.out.println("ID: " + denuncianteLido.getIdDenunciante());
                    System.out.println("ID Cidadão: " + denuncianteLido.getIdCidadao());
                    System.out.println("ID Queixa: " + denuncianteLido.getIdQueixa());
                    System.out.println("Modo Denúncia: " + denuncianteLido.getModoDenuncia());
                    System.out.println("Data Denúncia: " + denuncianteLido.getDataDenuncia());
                } else {
                    System.out.println("Denunciante não encontrado.");
                }
            }
            
            // Teste UPDATE
            System.out.println("\n=== TESTE UPDATE ===");
            if (criado) {
                novoDenunciante.setModoDenuncia("Presencial");
                boolean atualizado = denuncianteDAO.update(novoDenunciante);
                if (atualizado) {
                    System.out.println("Denunciante atualizado com sucesso!");
                    Denunciante denuncianteAtualizado = denuncianteDAO.read(novoDenunciante.getIdDenunciante());
                    System.out.println("Novo modo de denúncia: " + denuncianteAtualizado.getModoDenuncia());
                } else {
                    System.out.println("Falha ao atualizar denunciante.");
                }
            }
            
            // Teste LIST ALL
            System.out.println("\n=== TESTE LIST ALL ===");
            List<Denunciante> todosDenunciantes = denuncianteDAO.listAll();
            System.out.println("Total de denunciantes: " + todosDenunciantes.size());
            for (Denunciante d : todosDenunciantes) {
                System.out.println("ID: " + d.getIdDenunciante() + " | Cidadão: " + d.getIdCidadao() + 
                                 " | Queixa: " + d.getIdQueixa() + " | Modo: " + d.getModoDenuncia());
            }
            
            // Teste FIND BY QUEIXA
            System.out.println("\n=== TESTE FIND BY QUEIXA ===");
            List<Denunciante> denunciantesQueixa = denuncianteDAO.findByQueixa(1);
            System.out.println("Denunciantes para a queixa 1: " + denunciantesQueixa.size());
            for (Denunciante d : denunciantesQueixa) {
                System.out.println("ID: " + d.getIdDenunciante() + " | Cidadão: " + d.getIdCidadao());
            }
            
            // Teste DROPDOWN CIDADÃOS
            System.out.println("\n=== TESTE DROPDOWN CIDADÃOS ===");
            List<Map<String, String>> cidadaosDropdown = denuncianteDAO.listarCidadaosParaDropdown();
            System.out.println("Cidadãos para dropdown:");
            for (Map<String, String> cidadao : cidadaosDropdown) {
                System.out.println(cidadao.get("key") + " - " + cidadao.get("value"));
            }
            
            // Teste DROPDOWN QUEIXAS
            System.out.println("\n=== TESTE DROPDOWN QUEIXAS ===");
            List<Map<String, String>> queixasDropdown = denuncianteDAO.listarQueixasParaDropdown();
            System.out.println("Queixas para dropdown:");
            for (Map<String, String> queixa : queixasDropdown) {
                System.out.println(queixa.get("key") + " - " + queixa.get("value"));
            }
            
            // Teste DELETE
//            System.out.println("\n=== TESTE DELETE ===");
//            if (criado) {
//                boolean deletado = denuncianteDAO.delete(novoDenunciante.getIdDenunciante());
//                if (deletado) {
//                    System.out.println("Denunciante deletado com sucesso!");
//                } else {
//                    System.out.println("Falha ao deletar denunciante.");
//                }
//            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro durante os testes: " + e.getMessage());
        }
    }
}
