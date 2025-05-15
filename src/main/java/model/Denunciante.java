/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author JR5
 */

public class Denunciante {
    private int idDenunciante;
    private int idCidadao;
    private int idQueixa;
    private String modoDenuncia;
    private Timestamp dataDenuncia;
    
    // Campos adicionais para exibição (não persistidos no banco)
    private String nomeCidadao;
    private String descricaoQueixa;

    /**
     * Construtor padrão
     */
    public Denunciante() {
    }

    /**
     * Construtor completo
     */
    public Denunciante(int idDenunciante, int idCidadao, int idQueixa, 
                      String modoDenuncia, Timestamp dataDenuncia) {
        this.idDenunciante = idDenunciante;
        this.idCidadao = idCidadao;
        this.idQueixa = idQueixa;
        this.modoDenuncia = modoDenuncia;
        this.dataDenuncia = dataDenuncia;
    }

    // Getters e Setters

    public int getIdDenunciante() {
        return idDenunciante;
    }

    public void setIdDenunciante(int idDenunciante) {
        this.idDenunciante = idDenunciante;
    }

    public int getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(int idCidadao) {
        this.idCidadao = idCidadao;
    }

    public int getIdQueixa() {
        return idQueixa;
    }

    public void setIdQueixa(int idQueixa) {
        this.idQueixa = idQueixa;
    }

    public String getModoDenuncia() {
        return modoDenuncia;
    }

    public void setModoDenuncia(String modoDenuncia) {
        this.modoDenuncia = modoDenuncia;
    }

    public Timestamp getDataDenuncia() {
        return dataDenuncia;
    }

    public void setDataDenuncia(Timestamp dataDenuncia) {
        this.dataDenuncia = dataDenuncia;
    }

    public String getNomeCidadao() {
        return nomeCidadao;
    }

    public void setNomeCidadao(String nomeCidadao) {
        this.nomeCidadao = nomeCidadao;
    }

    public String getDescricaoQueixa() {
        return descricaoQueixa;
    }

    public void setDescricaoQueixa(String descricaoQueixa) {
        this.descricaoQueixa = descricaoQueixa;
    }

    /**
     * Representação textual do objeto
     */
    @Override
    public String toString() {
        return "Denunciante{" +
                "idDenunciante=" + idDenunciante +
                ", idCidadao=" + idCidadao +
                ", idQueixa=" + idQueixa +
                ", modoDenuncia='" + modoDenuncia + '\'' +
                ", dataDenuncia=" + dataDenuncia +
                ", nomeCidadao='" + nomeCidadao + '\'' +
                ", descricaoQueixa='" + descricaoQueixa + '\'' +
                '}';
    }

    /**
     * Valida os campos obrigatórios antes de persistir
     */
    public void validar() throws IllegalArgumentException {
        if (idCidadao <= 0) {
            throw new IllegalArgumentException("ID do cidadão é obrigatório");
        }
        if (idQueixa <= 0) {
            throw new IllegalArgumentException("ID da queixa é obrigatório");
        }
        if (modoDenuncia == null || modoDenuncia.trim().isEmpty()) {
            throw new IllegalArgumentException("Modo de denúncia é obrigatório");
        }
        if (dataDenuncia == null) {
            this.dataDenuncia = new Timestamp(System.currentTimeMillis());
        }
    }
}
