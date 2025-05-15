package model;

import java.sql.Timestamp;

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
    private Timestamp dataRegistro;

    
public enum TipoVitima {
    Direta("Direta"),
    Indireta("Indireta"),
    Familiar("Familiar"),
    Testemunha("Testemunha");

    // ... resto do código do enum permanece igual ...

        private final String descricao;

        TipoVitima(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public Vitima() {
        this.dataRegistro = new Timestamp(System.currentTimeMillis());
    }

    public Vitima(int idQueixa, int idCidadao, String descricao, TipoVitima tipoVitima) {
        this.idQueixa = idQueixa;
        this.idCidadao = idCidadao;
        this.descricao = descricao;
        this.tipoVitima = tipoVitima;
        this.dataRegistro = new Timestamp(System.currentTimeMillis());
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

    public Timestamp getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Timestamp dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getTipoVitimaAsString() {
        return tipoVitima != null ? tipoVitima.name() : null;
    }

    public void setTipoVitimaFromString(String tipo) {
        if (tipo != null) {
            try {
                this.tipoVitima = TipoVitima.valueOf(tipo.toUpperCase());
            } catch (IllegalArgumentException e) {
                this.tipoVitima = TipoVitima.Direta;
            }
        }
    }

    @Override
    public String toString() {
        return "Vitima{" +
                "idVitima=" + idVitima +
                ", idQueixa=" + idQueixa +
                ", idCidadao=" + idCidadao +
                ", descricao='" + descricao + '\'' +
                ", tipoVitima=" + (tipoVitima != null ? tipoVitima.getDescricao() : "null") +
                ", dataRegistro=" + dataRegistro +
                '}';
    }
}
