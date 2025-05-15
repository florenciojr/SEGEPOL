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
public class Detencao {
    private int idDetencao;
    private int idCidadao;
    private String motivo;
    private String localDetencao;
    private Timestamp dataDetencao;
    private String status;
    private int idUsuarioResponsavel;

    public Detencao() {}

    public Detencao(int idDetencao, int idCidadao, String motivo, String localDetencao, Timestamp dataDetencao, String status, int idUsuarioResponsavel) {
        this.idDetencao = idDetencao;
        this.idCidadao = idCidadao;
        this.motivo = motivo;
        this.localDetencao = localDetencao;
        this.dataDetencao = dataDetencao;
        this.status = status;
        this.idUsuarioResponsavel = idUsuarioResponsavel;
    }

    public int getIdDetencao() { return idDetencao; }
    public void setIdDetencao(int idDetencao) { this.idDetencao = idDetencao; }

    public int getIdCidadao() { return idCidadao; }
    public void setIdCidadao(int idCidadao) { this.idCidadao = idCidadao; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getLocalDetencao() { return localDetencao; }
    public void setLocalDetencao(String localDetencao) { this.localDetencao = localDetencao; }

    public Timestamp getDataDetencao() { return dataDetencao; }
    public void setDataDetencao(Timestamp dataDetencao) { this.dataDetencao = dataDetencao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getIdUsuarioResponsavel() { return idUsuarioResponsavel; }
    public void setIdUsuarioResponsavel(int idUsuarioResponsavel) { this.idUsuarioResponsavel = idUsuarioResponsavel; }

    @Override
    public String toString() {
        return "Detencao{" +
                "idDetencao=" + idDetencao +
                ", idCidadao=" + idCidadao +
                ", motivo='" + motivo + '\'' +
                ", localDetencao='" + localDetencao + '\'' +
                ", dataDetencao=" + dataDetencao +
                ", status='" + status + '\'' +
                ", idUsuarioResponsavel=" + idUsuarioResponsavel +
                '}';
    }
}
