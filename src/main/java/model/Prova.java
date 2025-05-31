/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author JR5
 */



import java.util.Date;

public class Prova {
    private int idProva;
    private int idQueixa;
    private String tipo; // Valores permitidos: "Imagem", "Vídeo", "Documento", "Áudio", "Objeto", "Digital"
    private String descricao;
    private String caminhoArquivo;
    private Date dataColeta;
    private Date dataUpload;
    private int idUsuario;

    public Prova() {
        this.dataUpload = new Date();
    }

    // Getters e Setters
    public int getIdProva() {
        return idProva;
    }

    public void setIdProva(int idProva) {
        this.idProva = idProva;
    }

    public int getIdQueixa() {
        return idQueixa;
    }

    public void setIdQueixa(int idQueixa) {
        this.idQueixa = idQueixa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if (tipo == null || (!tipo.equals("Imagem") && !tipo.equals("Vídeo") && 
            !tipo.equals("Documento") && !tipo.equals("Áudio") && 
            !tipo.equals("Objeto") && !tipo.equals("Digital"))) {
            throw new IllegalArgumentException("Tipo de prova inválido. Valores permitidos: Imagem, Vídeo, Documento, Áudio, Objeto, Digital");
        }
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public Date getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(Date dataColeta) {
        this.dataColeta = dataColeta;
    }

    public Date getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(Date dataUpload) {
        this.dataUpload = dataUpload;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return String.format("Prova [ID=%d, Queixa=%d, Tipo=%s, Arquivo=%s, Coleta=%s, Upload=%s, Usuário=%d]",
                idProva, idQueixa, tipo, 
                caminhoArquivo != null ? caminhoArquivo.substring(caminhoArquivo.lastIndexOf("\\") + 1) : "Nenhum",
                dataColeta != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(dataColeta) : "Não informada",
                new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(dataUpload),
                idUsuario);
    }
}
