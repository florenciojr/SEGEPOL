/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testes;

import dao.TiposQueixaDAO;
import java.util.List;
import java.util.Scanner;
import model.TiposQueixa;

/**
 *
 * @author JR5
 */
public class TestTipodeQueixa {
   public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TiposQueixaDAO tiposQueixaDAO = new TiposQueixaDAO();

        while (true) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1. Inserir novo tipo de queixa");
            System.out.println("2. Atualizar tipo de queixa");
            System.out.println("3. Deletar tipo de queixa");
            System.out.println("4. Listar todos os tipos de queixa");
            System.out.println("5. Sair");
            int opcao = scanner.nextInt();
            scanner.nextLine();  // Consumir o newline restante

            switch (opcao) {
                case 1:
                    // Inserir novo tipo de queixa
                    System.out.println("\nDigite o nome do tipo de queixa:");
                    String nomeInserir = scanner.nextLine();
                    System.out.println("Digite a descrição do tipo de queixa:");
                    String descricaoInserir = scanner.nextLine();

                    TiposQueixa tipoInserir = new TiposQueixa();
                    tipoInserir.setNomeTipo(nomeInserir);
                    tipoInserir.setDescricao(descricaoInserir);

                    if (tiposQueixaDAO.inserir(tipoInserir)) {
                        System.out.println("Tipo de queixa inserido com sucesso!");
                    } else {
                        System.out.println("Erro ao inserir tipo de queixa.");
                    }
                    break;

                case 2:
                    // Atualizar tipo de queixa
                    System.out.println("\nDigite o ID do tipo de queixa que deseja atualizar:");
                    int idAtualizar = scanner.nextInt();
                    scanner.nextLine();  // Consumir o newline restante
                    System.out.println("Digite o novo nome do tipo de queixa:");
                    String nomeAtualizar = scanner.nextLine();
                    System.out.println("Digite a nova descrição do tipo de queixa:");
                    String descricaoAtualizar = scanner.nextLine();

                    TiposQueixa tipoAtualizar = new TiposQueixa();
                    tipoAtualizar.setIdTipo(idAtualizar);
                    tipoAtualizar.setNomeTipo(nomeAtualizar);
                    tipoAtualizar.setDescricao(descricaoAtualizar);

                    if (tiposQueixaDAO.atualizar(tipoAtualizar)) {
                        System.out.println("Tipo de queixa atualizado com sucesso!");
                    } else {
                        System.out.println("Erro ao atualizar tipo de queixa.");
                    }
                    break;

                case 3:
                    // Deletar tipo de queixa
                    System.out.println("\nDigite o ID do tipo de queixa que deseja deletar:");
                    int idDeletar = scanner.nextInt();
                    scanner.nextLine();  // Consumir o newline restante

                    if (tiposQueixaDAO.deletar(idDeletar)) {
                        System.out.println("Tipo de queixa deletado com sucesso!");
                    } else {
                        System.out.println("Erro ao deletar tipo de queixa.");
                    }
                    break;

                case 4:
                    // Listar todos os tipos de queixa
                    List<TiposQueixa> tiposQueixa = tiposQueixaDAO.listarTodos();
                    if (tiposQueixa.isEmpty()) {
                        System.out.println("Não há tipos de queixa cadastrados.");
                    } else {
                        System.out.println("\nTipos de Queixa:");
                        for (TiposQueixa tipo : tiposQueixa) {
                            System.out.println("ID: " + tipo.getIdTipo() + " | Nome: " + tipo.getNomeTipo() + " | Descrição: " + tipo.getDescricao());
                        }
                    }
                    break;

                case 5:
                    // Sair
                    System.out.println("Saindo...");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }
}


   

