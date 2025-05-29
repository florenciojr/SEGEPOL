package model;

import java.sql.Date;
import java.text.Normalizer;

public class MandadoPrisao {
    private int idMandado;
    private int idSuspeito;
    private String numeroMandado;
    private Date dataEmissao;
    private Date dataValidade;
    private String tipo;
    private String status;
    private String descricao;
    private int idUsuarioEmissor;
    private int versao;
    private Suspeito suspeito;

    // Construtor
    public MandadoPrisao() {
        this.versao = 1; // Versão inicial
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
        if (numeroMandado == null || numeroMandado.trim().isEmpty()) {
            throw new IllegalArgumentException("Número do mandado é obrigatório");
        }
        this.numeroMandado = numeroMandado.trim();
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        if (dataEmissao == null) {
            throw new IllegalArgumentException("Data de emissão é obrigatória");
        }
        this.dataEmissao = dataEmissao;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getTipo() {
        return tipo;
    }

public void setTipo(String tipo) {
    if (tipo == null || tipo.trim().isEmpty()) {
        throw new IllegalArgumentException("Tipo de mandado é obrigatório");
    }
    
    // Remove acentos e converte para minúsculas
    String tipoNormalizado = Normalizer.normalize(tipo, Normalizer.Form.NFD)
        .replaceAll("[^\\p{ASCII}]", "")
        .toLowerCase();
    
    // Verifica os tipos permitidos de forma flexível
    if (tipoNormalizado.contains("prisao") || tipoNormalizado.contains("prisão") || tipoNormalizado.contains("prision")) {
        this.tipo = "Prisão";
    } 
    else if (tipoNormalizado.contains("busca") || tipoNormalizado.contains("apreensao") || tipoNormalizado.contains("apreensão")) {
        this.tipo = "Busca e Apreensão";
    }
    else if (tipoNormalizado.contains("comparecencia") || tipoNormalizado.contains("comparecência") || tipoNormalizado.contains("compear")) {
        this.tipo = "Comparência";
    }
    else {
        // Aceita o valor original se não reconhecer
        this.tipo = tipo;
    }
}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || !(status.equals("Ativo") || status.equals("Cancelado") || 
                               status.equals("Cumprido") || status.equals("Expirado"))) {
            throw new IllegalArgumentException("Status inválido");
        }
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdUsuarioEmissor() {
        return idUsuarioEmissor;
    }

    public void setIdUsuarioEmissor(int idUsuarioEmissor) {
        this.idUsuarioEmissor = idUsuarioEmissor;
    }

    public int getVersao() {
        return versao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public Suspeito getSuspeito() {
        return suspeito;
    }

    public void setSuspeito(Suspeito suspeito) {
        this.suspeito = suspeito;
        if (suspeito != null) {
            this.idSuspeito = suspeito.getIdSuspeito();
        }
    }

    @Override
    public String toString() {
        return "MandadoPrisao{" + "idMandado=" + idMandado + ", numeroMandado=" + numeroMandado + 
               ", suspeito=" + (suspeito != null ? suspeito.getCidadaoNome() : "null") + '}';
    }
}
