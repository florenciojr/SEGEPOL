/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author JR5
 */
import java.sql.Timestamp;

public class Testemunha {
    private int idTestemunha;
    private int idCidadao;
    private int idQueixa;
    private String tipoTestemunha;
    private String descricao;
    private Timestamp dataRegistro;

    public Testemunha() {}

    public Testemunha(int idTestemunha, int idCidadao, int idQueixa, String tipoTestemunha, String descricao, Timestamp dataRegistro) {
        this.idTestemunha = idTestemunha;
        this.idCidadao = idCidadao;
        this.idQueixa = idQueixa;
        this.tipoTestemunha = tipoTestemunha;
        this.descricao = descricao;
        this.dataRegistro = dataRegistro;
    }

    public int getIdTestemunha() { return idTestemunha; }
    public void setIdTestemunha(int idTestemunha) { this.idTestemunha = idTestemunha; }

    public int getIdCidadao() { return idCidadao; }
    public void setIdCidadao(int idCidadao) { this.idCidadao = idCidadao; }

    public int getIdQueixa() { return idQueixa; }
    public void setIdQueixa(int idQueixa) { this.idQueixa = idQueixa; }

    public String getTipoTestemunha() { return tipoTestemunha; }
    public void setTipoTestemunha(String tipoTestemunha) { this.tipoTestemunha = tipoTestemunha; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Timestamp getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(Timestamp dataRegistro) { this.dataRegistro = dataRegistro; }

    @Override
    public String toString() {
        return "Testemunha{" +
                "idTestemunha=" + idTestemunha +
                ", idCidadao=" + idCidadao +
                ", idQueixa=" + idQueixa +
                ", tipoTestemunha='" + tipoTestemunha + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataRegistro=" + dataRegistro +
                '}';
    }
}
