package model;

import java.sql.Timestamp;

/**
 *
 * @author JR5
 */
public class TiposQueixa {
    private int idTipo;
    private String nomeTipo;
    private String descricao;
    private String gravidade;
    private Timestamp dataCadastro;
    
    // Construtor vazio
    public TiposQueixa() {
    }

    // Construtor completo
    public TiposQueixa(int idTipo, String nomeTipo, String descricao, String gravidade, Timestamp dataCadastro) {
        this.idTipo = idTipo;
        this.nomeTipo = nomeTipo;
        this.descricao = descricao;
        this.gravidade = gravidade;
        this.dataCadastro = dataCadastro;
    }

    // Getters e Setters
    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGravidade() {
        return gravidade;
    }

    public void setGravidade(String gravidade) {
        this.gravidade = gravidade;
    }

    public Timestamp getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Timestamp dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        return "TiposQueixa{" + "idTipo=" + idTipo + ", nomeTipo=" + nomeTipo + 
               ", descricao=" + descricao + ", gravidade=" + gravidade + 
               ", dataCadastro=" + dataCadastro + '}';
    }
}
