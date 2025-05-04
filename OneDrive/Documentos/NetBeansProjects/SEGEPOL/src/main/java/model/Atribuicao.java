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

public class Atribuicao {
    private int idAtribuicao;
    private int idQueixa;
    private int idInvestigador;
    private Date dataAtribuicao;

    // Construtores
    public Atribuicao() {
    }
    
    private String nomeInvestigador;

public String getNomeInvestigador() {
    return nomeInvestigador;
}

public void setNomeInvestigador(String nomeInvestigador) {
    this.nomeInvestigador = nomeInvestigador;
}

    public Atribuicao(int idAtribuicao, int idQueixa, int idInvestigador, Date dataAtribuicao) {
        this.idAtribuicao = idAtribuicao;
        this.idQueixa = idQueixa;
        this.idInvestigador = idInvestigador;
        this.dataAtribuicao = dataAtribuicao;
    }

    // Getters e Setters
    public int getIdAtribuicao() {
        return idAtribuicao;
    }

    public void setIdAtribuicao(int idAtribuicao) {
        this.idAtribuicao = idAtribuicao;
    }

    public int getIdQueixa() {
        return idQueixa;
    }

    public void setIdQueixa(int idQueixa) {
        this.idQueixa = idQueixa;
    }

    public int getIdInvestigador() {
        return idInvestigador;
    }

    public void setIdInvestigador(int idInvestigador) {
        this.idInvestigador = idInvestigador;
    }

    public Date getDataAtribuicao() {
        return dataAtribuicao;
    }

    public void setDataAtribuicao(Date dataAtribuicao) {
        this.dataAtribuicao = dataAtribuicao;
    }

    @Override
    public String toString() {
        return "Atribuicao{" +
                "idAtribuicao=" + idAtribuicao +
                ", idQueixa=" + idQueixa +
                ", idInvestigador=" + idInvestigador +
                ", dataAtribuicao=" + dataAtribuicao +
                '}';
    }
}
