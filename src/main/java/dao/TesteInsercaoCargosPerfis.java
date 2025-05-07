/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author JR5
 */





import dao.UsuarioDAO;
import model.Usuario;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class TesteInsercaoCargosPerfis {

    private static final AtomicInteger counter = new AtomicInteger(1);

    public static void main(String[] args) {
        System.out.println("=== TESTE DE INSERÇÃO DE TODOS CARGOS E PERFIS (VERSÃO CORRIGIDA) ===");
        
        UsuarioDAO dao = new UsuarioDAO();
        int sucessos = 0;
        int falhas = 0;

        try {
            // 1. TESTAR TODOS CARGOS COM PERFIS PADRÃO
            System.out.println("\n1. TESTANDO TODOS CARGOS COM SEUS PERFIS PADRÃO:");
            for (String cargo : UsuarioDAO.CARGOS_DISPONIVEIS) {
                try {
                    Usuario usuario = criarUsuarioTeste(cargo, counter.getAndIncrement());
                    if (dao.inserirUsuario(usuario)) {
                        System.out.println("✔ " + cargo + " (Perfil: " + usuario.getPerfil() + ")");
                        sucessos++;
                    } else {
                        System.out.println("✘ " + cargo + " - FALHA NA INSERÇÃO");
                        falhas++;
                    }
                } catch (SQLException e) {
                    System.out.println("✘ " + cargo + " - ERRO: " + e.getMessage());
                    falhas++;
                }
            }

            // 2. TESTAR TODOS PERFIS COM CARGO GENÉRICO
            System.out.println("\n2. TESTANDO TODOS PERFIS COM CARGO 'Comandante':");
            for (String perfil : UsuarioDAO.PERFIS_DISPONIVEIS) {
                try {
                    Usuario usuario = criarUsuarioTeste("Comandante", counter.getAndIncrement());
                    usuario.setPerfil(perfil);
                    
                    if (dao.inserirUsuario(usuario)) {
                        System.out.println("✔ Perfil " + perfil + " com cargo Comandante");
                        sucessos++;
                    } else {
                        System.out.println("✘ Perfil " + perfil + " - FALHA NA INSERÇÃO");
                        falhas++;
                    }
                } catch (SQLException e) {
                    System.out.println("✘ Perfil " + perfil + " - ERRO: " + e.getMessage());
                    falhas++;
                }
            }

            // 3. TESTAR COMBINAÇÕES ALEATÓRIAS
            System.out.println("\n3. TESTANDO COMBINAÇÕES ALEATÓRIAS:");
            String[][] combinacoes = {
                {"Chefe das operações", "Operacional"},
                {"Chefe da ética", "Ética"},
                {"Oficial de permanência", "Plantão"},
                {"Administrador", "Super Admin"}
            };

            for (String[] combinacao : combinacoes) {
                try {
                    Usuario usuario = criarUsuarioTeste(combinacao[0], counter.getAndIncrement());
                    usuario.setPerfil(combinacao[1]);
                    
                    if (dao.inserirUsuario(usuario)) {
                        System.out.println("✔ " + combinacao[0] + " + " + combinacao[1] + " - OK");
                        sucessos++;
                    } else {
                        System.out.println("✘ " + combinacao[0] + " + " + combinacao[1] + " - FALHA");
                        falhas++;
                    }
                } catch (SQLException e) {
                    System.out.println("✘ " + combinacao[0] + " + " + combinacao[1] + " - ERRO: " + e.getMessage());
                    falhas++;
                }
            }

        } finally {
            System.out.println("\nRESULTADO FINAL:");
            System.out.println("Sucessos: " + sucessos);
            System.out.println("Falhas: " + falhas);
            System.out.println("Total de testes: " + (sucessos + falhas));
        }
    }

    private static Usuario criarUsuarioTeste(String cargo, int id) {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste " + cargo + " #" + id);
        usuario.setEmail(cargo.toLowerCase().replace(" ", "") + id + "@teste.com");
        usuario.setSenha("senhateste" + id);
        usuario.setCargo(cargo);
        usuario.setPerfil(UsuarioDAO.getPerfilPadraoParaCargo(cargo));
        usuario.setTelefone("(11) 99999-999" + (id % 10));
        usuario.setStatus("Ativo");
        usuario.setNumero_identificacao("ID" + System.currentTimeMillis() + id);
        return usuario;
    }
}