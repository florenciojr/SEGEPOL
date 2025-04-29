/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author JR5
 */

public class Suspeito {
    private int idSuspeito;
    private int idQueixa;
    private String nome;
    private String descricao;
    private String genero;
    private String dataNascimento;

    // Construtores
    public Suspeito() {}

    public Suspeito(int idSuspeito, int idQueixa, String nome, String descricao, String genero, String dataNascimento) {
        this.idSuspeito = idSuspeito;
        this.idQueixa = idQueixa;
        this.nome = nome;
        this.descricao = descricao;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters
    public int getIdSuspeito() {
        return idSuspeito;
    }

    public void setIdSuspeito(int idSuspeito) {
        this.idSuspeito = idSuspeito;
    }

    public int getIdQueixa() {
        return idQueixa;
    }

    public void setIdQueixa(int idQueixa) {
        this.idQueixa = idQueixa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return "Suspeito [idSuspeito=" + idSuspeito + ", idQueixa=" + idQueixa +
               ", nome=" + nome + ", descricao=" + descricao +
               ", genero=" + genero + ", dataNascimento=" + dataNascimento + "]";
    }
}

