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
import java.time.LocalDateTime;

/**
 * Classe que representa um cidadão no sistema
 */
public class Cidadao {
    private int idCidadao;
    private String nome;
    private String genero;
    private LocalDate dataNascimento;
    private String documentoIdentificacao;
    private String tipoDocumento;
    private String telefone;
    private String email;
    private String naturalidade;
    private String rua;
    private String bairro;
    private String cidade;
    private String provincia;
    private String caminhoImagem;
    private LocalDateTime dataRegistro;
        private String classificacao;
           private String caracteristicasFisicas; // Adicione esta linha

    // Construtores
    public Cidadao() {
    }

    public Cidadao(int idCidadao, String nome, String genero, LocalDate dataNascimento, 
                  String documentoIdentificacao, String tipoDocumento, String telefone, 
                  String email, String naturalidade, String rua, String bairro, 
                  String cidade, String provincia, String caminhoImagem, String classificacao, 
                   LocalDateTime dataRegistro) {
        this.idCidadao = idCidadao;
        this.nome = nome;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
        this.documentoIdentificacao = documentoIdentificacao;
        this.tipoDocumento = tipoDocumento;
        this.telefone = telefone;
        this.email = email;
        this.naturalidade = naturalidade;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.provincia = provincia;
        this.caminhoImagem = caminhoImagem;
        this.dataRegistro = dataRegistro;
    }

    // Getters e Setters
    public int getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(int idCidadao) {
        this.idCidadao = idCidadao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDocumentoIdentificacao() {
        return documentoIdentificacao;
    }

    public void setDocumentoIdentificacao(String documentoIdentificacao) {
        this.documentoIdentificacao = documentoIdentificacao;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

 

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
        public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }
    
    public void setCaracteristicasFisicas(String caracteristicas) {
    this.caracteristicasFisicas = caracteristicas;
}
    public String getCaracteristicasFisicas() {
    return caracteristicasFisicas;
}

    // Método toString para representação textual do objeto
    @Override
    public String toString() {
        return "Cidadao{" +
                "idCidadao=" + idCidadao +
                ", nome='" + nome + '\'' +
                ", genero='" + genero + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", documentoIdentificacao='" + documentoIdentificacao + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", naturalidade='" + naturalidade + '\'' +
                ", rua='" + rua + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", provincia='" + provincia + '\'' +
                ", caminhoImagem='" + caminhoImagem + '\'' +
                ", dataRegistro=" + dataRegistro +
                ", classificacao='" + classificacao + '\'' +
                '}';
    }

}
