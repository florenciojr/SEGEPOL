package model;

/**
 * Classe que estende Prova para incluir informações detalhadas de relacionamentos
 * como título da queixa e nome do usuário associado.
 * 
 * @author JR5
 */

public class ProvaDetalhada extends Prova {
    private String tituloQueixa;
    private String nomeUsuario;

    // Construtor padrão
    public ProvaDetalhada() {
        super();
    }

    // Construtor completo usando setters
    public ProvaDetalhada(Prova prova, String tituloQueixa, String nomeUsuario) {
        // Usando setters em vez de construtor da superclasse
        this.setIdProva(prova.getIdProva());
        this.setIdQueixa(prova.getIdQueixa());
        this.setTipo(prova.getTipo());
        this.setDescricao(prova.getDescricao());
        this.setCaminhoArquivo(prova.getCaminhoArquivo());
        this.setDataColeta(prova.getDataColeta());
        this.setDataUpload(prova.getDataUpload());
        this.setIdUsuario(prova.getIdUsuario());
        
        this.tituloQueixa = tituloQueixa;
        this.nomeUsuario = nomeUsuario;
    }

    // Getters e Setters
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

    @Override
    public String toString() {
        return super.toString() + 
               String.format(", Queixa: %s, Usuário: %s", 
               tituloQueixa != null ? tituloQueixa : "N/D",
               nomeUsuario != null ? nomeUsuario : "N/D");
    }
}
