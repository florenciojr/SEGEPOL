/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author JR5
 */


import java.util.Date;

public class Suspeito {
    private int idSuspeito;
    private int idQueixa;
    private String nome;
    private String descricao;
    private String genero;
    private String dataNascimento;
    private Integer idCidadao;
    private String caminhoImagem;
    private Date dataRegistro;

    // Construtores
    public Suspeito() {
        this.dataRegistro = new Date();
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

    public Integer getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(Integer idCidadao) {
        this.idCidadao = idCidadao;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    @Override
    public String toString() {
        return String.format("Suspeito [ID=%d, Queixa=%d, Nome=%s, Gênero=%s, Nascimento=%s, Cidadão=%s, Imagem=%s]",
                idSuspeito, idQueixa, nome, genero, dataNascimento, 
                idCidadao != null ? idCidadao : "N/A",
                caminhoImagem != null ? caminhoImagem.substring(caminhoImagem.lastIndexOf("\\") + 1) : "Nenhuma");
    }
}