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
    private String tipo; // Valores permitidos: "Imagem", "Vídeo", "Documento", "Áudio"
    private String descricao;
    private String caminhoArquivo;
    private Date dataUpload;

    public Prova() {
        this.dataUpload = new Date();
    }

    // Getters e Setters com validação
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
            !tipo.equals("Documento") && !tipo.equals("Áudio"))) {
            throw new IllegalArgumentException("Tipo de prova inválido. Valores permitidos: Imagem, Vídeo, Documento, Áudio");
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

    public Date getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(Date dataUpload) {
        this.dataUpload = dataUpload;
    }

    @Override
    public String toString() {
        return String.format("Prova [ID=%d, Queixa=%d, Tipo=%s, Arquivo=%s, Data=%s]",
                idProva, idQueixa, tipo, 
                caminhoArquivo != null ? caminhoArquivo.substring(caminhoArquivo.lastIndexOf("\\") + 1) : "Nenhum",
                new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(dataUpload));
    }
}