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


public class Suspeito {
    private int idSuspeito;
    private int idQueixa;
    private Integer idCidadao;  // Pode ser null para suspeitos não identificados
    private String descricao;
    private String papelIncidente;  // "Principal", "Cúmplice", "Acessório" ou "Testemunha"
    private Date dataRegistro;
        private String cidadaoNome;
    private String queixaDescricao;

    public String getCidadaoNome() {
        return cidadaoNome;
    }

    public void setCidadaoNome(String cidadaoNome) {
        this.cidadaoNome = cidadaoNome;
    }

    public String getQueixaDescricao() {
        return queixaDescricao;
    }

    public void setQueixaDescricao(String queixaDescricao) {
        this.queixaDescricao = queixaDescricao;
    }
    
    

    // Construtores
    public Suspeito() {
        this.dataRegistro = new Date();
        this.papelIncidente = "Principal";  // Valor padrão
    }

    // Getters e Setters
    public int getIdSuspeito() {
        return idSuspeito;
    }

    public void setIdSuspeito(int idSuspeito) {
        this.idSuspeito = idSuspeito;
    }

    public int getIdQueixa() {
        return idQueixa;
    }

    public void setIdQueixa(int idQueixa) {
        this.idQueixa = idQueixa;
    }

    public Integer getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(Integer idCidadao) {
        this.idCidadao = idCidadao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPapelIncidente() {
        return papelIncidente;
    }

    public void setPapelIncidente(String papelIncidente) {
        if (papelIncidente == null || 
            papelIncidente.isEmpty() ||
            (!papelIncidente.equals("Principal") && 
             !papelIncidente.equals("Cúmplice") && 
             !papelIncidente.equals("Acessório") && 
             !papelIncidente.equals("Testemunha"))) {
            this.papelIncidente = "Principal";
        } else {
            this.papelIncidente = papelIncidente;
        }
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    @Override
    public String toString() {
        return String.format("Suspeito [ID=%d, Queixa=%d, Cidadão=%s, Papel=%s]",
                idSuspeito, 
                idQueixa, 
                idCidadao != null ? idCidadao : "Não identificado",
                papelIncidente);
    }
}
