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

public class Intimacao {
    private int idIntimacao;
    private int idCidadao;
    private int idUsuario;
    private int idQueixa;
    private String motivo;
    private LocalDate dataEmissao;
    private LocalDate dataComparecimento;
    private String localComparecimento;
    private String status;
    private String observacoes;
    
    // Campos adicionais para exibição (não persistidos no banco)
    private String nomeCidadao;
    private String tituloQueixa;
    private String nomeUsuario;

    // Construtor
    public Intimacao() {
        // Pode inicializar valores padrão se necessário
    }

    // Getters e Setters
    public int getIdIntimacao() {
        return idIntimacao;
    }

    public void setIdIntimacao(int idIntimacao) {
        this.idIntimacao = idIntimacao;
    }

    public int getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(int idCidadao) {
        this.idCidadao = idCidadao;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdQueixa() {
        return idQueixa;
    }

    public void setIdQueixa(int idQueixa) {
        this.idQueixa = idQueixa;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

     public LocalDate getDataComparecimento() {
        return dataComparecimento;
    }
    
    public void setDataComparecimento(LocalDate dataComparecimento) {
        this.dataComparecimento = dataComparecimento;
    }

    public String getLocalComparecimento() {
        return localComparecimento;
    }

    public void setLocalComparecimento(String localComparecimento) {
        this.localComparecimento = localComparecimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // Getters e Setters para campos adicionais
    public String getNomeCidadao() {
        return nomeCidadao;
    }

    public void setNomeCidadao(String nomeCidadao) {
        this.nomeCidadao = nomeCidadao;
    }

    public String getTituloQueixa() {
        return tituloQueixa;
    }

    public void setTituloQueixa(String tituloQueixa) {
        this.tituloQueixa = tituloQueixa;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    // Método para verificar se a intimação está vencida
    public boolean isVencida() {
        if (status.equalsIgnoreCase("Pendente") && dataComparecimento != null) {
            return dataComparecimento.isBefore(LocalDate.now());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Intimacao{" +
                "idIntimacao=" + idIntimacao +
                ", idCidadao=" + idCidadao +
                ", idUsuario=" + idUsuario +
                ", idQueixa=" + idQueixa +
                ", motivo='" + motivo + '\'' +
                ", dataEmissao=" + dataEmissao +
                ", dataComparecimento=" + dataComparecimento +
                ", localComparecimento='" + localComparecimento + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
