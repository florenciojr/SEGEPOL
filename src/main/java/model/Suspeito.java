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

    private Integer idSuspeito;
    private int idQueixa;
    private String nome;
    private String descricao;
    private String genero;
    private String dataNascimento;
    private Integer idCidadao; // Pode ser null se ainda não estiver vinculado

    public Suspeito() {
        // Construtor padrão
    }

    public Integer getIdSuspeito() {
        return idSuspeito;
    }

    public void setIdSuspeito(Integer idSuspeito) {
        this.idSuspeito = idSuspeito;
    }

    public int getIdQueixa() {
        return idQueixa;
    }

    public void setIdQueixa(int idQueixa) {
        this.idQueixa = idQueixa;
    }

    public String getNome() {
        return nome != null ? nome : "";
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao != null ? descricao : "";
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGenero() {
        return genero != null ? genero : "";
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDataNascimento() {
        return dataNascimento != null ? dataNascimento : "";
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Integer getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(Integer idCidadao) {
        this.idCidadao = idCidadao;
    }

    @Override
    public String toString() {
        return "Suspeito {" +
                "ID = " + idSuspeito +
                ", Queixa = " + idQueixa +
                ", Nome = '" + nome + '\'' +
                ", Descrição = '" + descricao + '\'' +
                ", Gênero = '" + genero + '\'' +
                ", Data de Nascimento = '" + dataNascimento + '\'' +
                ", ID Cidadão = " + (idCidadao != null ? idCidadao : "N/A") +
                '}';
    }
}
