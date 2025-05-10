/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author JR5
 */



public class TiposQueixa {
    private int idTipo;
    private String nomeTipo;
    private String descricao;
    private String gravidade;
    
    // Construtor vazio
    public TiposQueixa() {
    }

    // Construtor completo
    public TiposQueixa(int idTipo, String nomeTipo, String descricao, String gravidade) {
        this.idTipo = idTipo;
        this.nomeTipo = nomeTipo;
        this.descricao = descricao;
        this.gravidade = gravidade;
    }

    // Métodos Getters e Setters COMPLETOS e IMPLEMENTADOS

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGravidade() {
        return gravidade;
    }

    // MÉTODO CORRIGIDO - implementação real
    public void setGravidade(String gravidade) {
        this.gravidade = gravidade;
    }

    @Override
    public String toString() {
        return nomeTipo + " - " + gravidade;
    }
}
