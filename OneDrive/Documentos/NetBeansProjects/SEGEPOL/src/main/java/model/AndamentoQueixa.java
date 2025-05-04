/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author JR5
 */



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AndamentoQueixa {
    private int idAndamento;
    private int idQueixa;
    private String descricao;
    private LocalDateTime dataAndamento;
    private int idUsuario;

    // Construtores
    public AndamentoQueixa() {
        this.dataAndamento = LocalDateTime.now();
    }

    public AndamentoQueixa(int idQueixa, String descricao, int idUsuario) {
        this();
        this.idQueixa = idQueixa;
        this.descricao = descricao;
        this.idUsuario = idUsuario;
    }

    // Getters e Setters
    public int getIdAndamento() {
        return idAndamento;
    }

    public void setIdAndamento(int idAndamento) {
        this.idAndamento = idAndamento;
    }

    public int getIdQueixa() {
        return idQueixa;
    }

    public void setIdQueixa(int idQueixa) {
        this.idQueixa = idQueixa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataAndamento() {
        return dataAndamento;
    }

    public void setDataAndamento(LocalDateTime dataAndamento) {
        this.dataAndamento = dataAndamento;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format(
            "Andamento #%d | Queixa #%d | Usuário #%d\nData: %s\nDescrição: %s\n",
            idAndamento, idQueixa, idUsuario,
            dataAndamento.format(formatter),
            descricao
        );
    }
}