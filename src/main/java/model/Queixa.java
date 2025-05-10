/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author JR5
 */


import java.time.LocalDate;

public class Queixa {
    private int idQueixa;
    private String titulo;
    private String descricao;
    private LocalDate dataIncidente;
    private LocalDate dataRegistro;
    private String localIncidente;
    private String coordenadas;
    private String status;
    private int idCidadao;
    private int idTipo;
    private int idUsuario;

    
    private String nomeCidadao;
    private String nomeUsuario;
    private String nomeTipoQueixa;
    private String gravidadeTipo;

    public String getNomeCidadao() {
        return nomeCidadao;
    }

    public void setNomeCidadao(String nomeCidadao) {
        this.nomeCidadao = nomeCidadao;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeTipoQueixa() {
        return nomeTipoQueixa;
    }

    public void setNomeTipoQueixa(String nomeTipoQueixa) {
        this.nomeTipoQueixa = nomeTipoQueixa;
    }

    public String getGravidadeTipo() {
        return gravidadeTipo;
    }

    public void setGravidadeTipo(String gravidadeTipo) {
        this.gravidadeTipo = gravidadeTipo;
    }
    


    // Construtor vazio
    public Queixa() {
    }

    // Getters e Setters
    public int getIdQueixa() {
        return idQueixa;
    }

    public void setIdQueixa(int idQueixa) {
        this.idQueixa = idQueixa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataIncidente() {
        return dataIncidente;
    }

    public void setDataIncidente(LocalDate dataIncidente) {
        this.dataIncidente = dataIncidente;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getLocalIncidente() {
        return localIncidente;
    }

    public void setLocalIncidente(String localIncidente) {
        this.localIncidente = localIncidente;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(int idCidadao) {
        this.idCidadao = idCidadao;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Queixa{" +
                "idQueixa=" + idQueixa +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataIncidente=" + dataIncidente +
                ", dataRegistro=" + dataRegistro +
                ", localIncidente='" + localIncidente + '\'' +
                ", coordenadas='" + coordenadas + '\'' +
                ", status='" + status + '\'' +
                ", idCidadao=" + idCidadao +
                ", idTipo=" + idTipo +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
