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
    private int idCidadao;
    private String descricao;
    private TipoVitima tipoVitima;

    // Enum para os tipos de vítima
    public enum TipoVitima {
        DIRETA("Direta"),
        INDIRETA("Indireta"),
        FAMILIAR("Familiar"),
        TESTEMUNHA("Testemunha");

        private final String descricao;

        TipoVitima(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    // Construtores
    public Vitima() {
    }

    public Vitima(int idQueixa, int idCidadao, String descricao, TipoVitima tipoVitima) {
        this.idQueixa = idQueixa;
        this.idCidadao = idCidadao;
        this.descricao = descricao;
        this.tipoVitima = tipoVitima;
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

    public int getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(int idCidadao) {
        this.idCidadao = idCidadao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoVitima getTipoVitima() {
        return tipoVitima;
    }

    public void setTipoVitima(TipoVitima tipoVitima) {
        this.tipoVitima = tipoVitima;
    }

    // Método para obter o tipo como String (para uso no BD)
    public String getTipoVitimaAsString() {
        return tipoVitima != null ? tipoVitima.name() : null;
    }

    // Método para definir o tipo a partir de String (para leitura do BD)
    public void setTipoVitimaFromString(String tipo) {
        if (tipo != null) {
            this.tipoVitima = TipoVitima.valueOf(tipo.toUpperCase());
        }
    }

    // toString aprimorado
    @Override
    public String toString() {
        return "Vitima{" +
                "idVitima=" + idVitima +
                ", idQueixa=" + idQueixa +
                ", idCidadao=" + idCidadao +
                ", descricao='" + descricao + '\'' +
                ", tipoVitima=" + (tipoVitima != null ? tipoVitima.getDescricao() : "null") +
                '}';
    }
}
