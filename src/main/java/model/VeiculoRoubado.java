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

/**
 * Modelo para representar um veículo roubado
 */
public class VeiculoRoubado {
    private int idVeiculo;
    private int idQueixa;
    private String marca;
    private String modelo;
    private String cor;
    private String matricula;
    private Integer ano;
    private String fotoVeiculo;
    private Timestamp dataRegistro;

    // Construtores
    public VeiculoRoubado() {
        this.dataRegistro = new Timestamp(System.currentTimeMillis());
    }

    public VeiculoRoubado(String marca, String modelo, String cor, String matricula) {
        this();
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.matricula = matricula;
    }

    // Getters e Setters
    public int getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public int getIdQueixa() {
        return idQueixa;
    }

    public void setIdQueixa(int idQueixa) {
        this.idQueixa = idQueixa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getFotoVeiculo() {
        return fotoVeiculo;
    }

    public void setFotoVeiculo(String fotoVeiculo) {
        this.fotoVeiculo = fotoVeiculo;
    }

    public Timestamp getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Timestamp dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    @Override
    public String toString() {
        return "VeiculoRoubado{" +
                "idVeiculo=" + idVeiculo +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", matricula='" + matricula + '\'' +
                ", foto=" + (fotoVeiculo != null ? "Sim" : "Não") +
                '}';
    }
}