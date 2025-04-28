/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author JR5
 */
package testes;

import model.Conexao;
import java.sql.Connection;

public class testeconexao {
    public static void main(String[] args) {
        Connection conn = Conexao.conectar();
        if (conn != null) {
            System.out.println("✅ Conexão com o banco de dados realizada com sucesso!");
        } else {
            System.out.println("❌ Falha ao conectar com o banco de dados!");
        }
    }
}
