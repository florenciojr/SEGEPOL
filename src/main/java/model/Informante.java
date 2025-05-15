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
public class Informante {
    private int idInformante;
    private int idCidadao;
    private int idQueixa;
    private String relato;
    private String confiabilidade;
    private boolean anonimato;
    private Timestamp dataRegistro;

    public Informante() {}

    public Informante(int idInformante, int idCidadao, int idQueixa, String relato, String confiabilidade, boolean anonimato, Timestamp dataRegistro) {
        this.idInformante = idInformante;
        this.idCidadao = idCidadao;
        this.idQueixa = idQueixa;
        this.relato = relato;
        this.confiabilidade = confiabilidade;
        this.anonimato = anonimato;
        this.dataRegistro = dataRegistro;
    }

    public int getIdInformante() { return idInformante; }
    public void setIdInformante(int idInformante) { this.idInformante = idInformante; }

    public int getIdCidadao() { return idCidadao; }
    public void setIdCidadao(int idCidadao) { this.idCidadao = idCidadao; }

    public int getIdQueixa() { return idQueixa; }
    public void setIdQueixa(int idQueixa) { this.idQueixa = idQueixa; }

    public String getRelato() { return relato; }
    public void setRelato(String relato) { this.relato = relato; }

    public String getConfiabilidade() { return confiabilidade; }
    public void setConfiabilidade(String confiabilidade) { this.confiabilidade = confiabilidade; }

    public boolean isAnonimato() { return anonimato; }
    public void setAnonimato(boolean anonimato) { this.anonimato = anonimato; }

    public Timestamp getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(Timestamp dataRegistro) { this.dataRegistro = dataRegistro; }

    @Override
    public String toString() {
        return "Informante{" +
                "idInformante=" + idInformante +
                ", idCidadao=" + idCidadao +
                ", idQueixa=" + idQueixa +
                ", relato='" + relato + '\'' +
                ", confiabilidade='" + confiabilidade + '\'' +
                ", anonimato=" + anonimato +
                ", dataRegistro=" + dataRegistro +
                '}';
    }
}
