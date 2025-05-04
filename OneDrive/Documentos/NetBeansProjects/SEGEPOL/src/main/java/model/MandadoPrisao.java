/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

import java.sql.Date;

/**
 *
 * @author JR5
 */

public class MandadoPrisao {
    private int idMandado;
    private int idSuspeito;
    private String numeroMandado;
    private Date dataEmissao;
    private String status; // Pode ser: Ativo, Cumprido ou Cancelado

    public MandadoPrisao() {
        this.status = "Ativo"; // Valor padrão
    }

    // Getters e Setters
    public int getIdMandado() {
        return idMandado;
    }

    public void setIdMandado(int idMandado) {
        this.idMandado = idMandado;
    }

    public int getIdSuspeito() {
        return idSuspeito;
    }

    public void setIdSuspeito(int idSuspeito) {
        this.idSuspeito = idSuspeito;
    }

    public String getNumeroMandado() {
        return numeroMandado;
    }

    public void setNumeroMandado(String numeroMandado) {
        this.numeroMandado = numeroMandado;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals("Ativo") || status.equals("Cumprido") || status.equals("Cancelado")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Status inválido. Deve ser: Ativo, Cumprido ou Cancelado");
        }
    }

    @Override
    public String toString() {
        return "MandadoPrisao{" + "idMandado=" + idMandado + 
               ", idSuspeito=" + idSuspeito + 
               ", numeroMandado=" + numeroMandado + 
               ", dataEmissao=" + dataEmissao + 
               ", status=" + status + '}';
    }
}