/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author JR5
 */


public class Vitima {
    private int idVitima;
    private int idQueixa;
    private String nome;
    private String descricao;
    private String dataNascimento;
    private String genero;
    private int idCidadao;

    // Construtor padrão (sem parâmetros)
    public Vitima() {
    }

    // Construtor com parâmetros
    public Vitima(int idQueixa, String nome, String descricao, String dataNascimento, String genero, int idCidadao) {
        this.idQueixa = idQueixa;
        this.nome = nome;
        this.descricao = descricao;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.idCidadao = idCidadao;
    }

    // Getters e Setters
    public int getIdVitima() {
        return idVitima;
    }

    public void setIdVitima(int idVitima) {
        this.idVitima = idVitima;
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

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(int idCidadao) {
        this.idCidadao = idCidadao;
    }

    // Método toString() para exibir as informações
    @Override
    public String toString() {
        return "Vitima [idVitima=" + idVitima + ", idQueixa=" + idQueixa + ", nome=" + nome + ", descricao=" + descricao
                + ", dataNascimento=" + dataNascimento + ", genero=" + genero + ", idCidadao=" + idCidadao + "]";
    }
}
