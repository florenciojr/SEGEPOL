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
public class ClassificacaoCidadao {
    private int idClassificacao;
    private int idCidadao;
    private String classificacao;
    private Timestamp dataClassificacao;
    private String observacoes;
    private String nomeCidadao;

    public ClassificacaoCidadao() {}

    public ClassificacaoCidadao(int idClassificacao, int idCidadao, String classificacao, Timestamp dataClassificacao, String observacoes) {
        this.idClassificacao = idClassificacao;
        this.idCidadao = idCidadao;
        this.classificacao = classificacao;
        this.dataClassificacao = dataClassificacao;
        this.observacoes = observacoes;
    }

    public int getIdClassificacao() { return idClassificacao; }
    public void setIdClassificacao(int idClassificacao) { this.idClassificacao = idClassificacao; }

    public int getIdCidadao() { return idCidadao; }
    public void setIdCidadao(int idCidadao) { this.idCidadao = idCidadao; }

    public String getClassificacao() { return classificacao; }
    public void setClassificacao(String classificacao) { this.classificacao = classificacao; }

    public Timestamp getDataClassificacao() { return dataClassificacao; }
    public void setDataClassificacao(Timestamp dataClassificacao) { this.dataClassificacao = dataClassificacao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

        public String getNomeCidadao() {
        return nomeCidadao;
    }

    public void setNomeCidadao(String nomeCidadao) {
        this.nomeCidadao = nomeCidadao;
    }

    @Override
    public String toString() {
        return "ClassificacaoCidadao{" +
                "idClassificacao=" + idClassificacao +
                ", idCidadao=" + idCidadao +
                ", classificacao='" + classificacao + '\'' +
                ", dataClassificacao=" + dataClassificacao +
                ", observacoes='" + observacoes + '\'' +
                '}';
    }
}
