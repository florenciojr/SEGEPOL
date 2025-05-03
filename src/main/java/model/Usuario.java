/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.Timestamp;

/**
 *
 * @author JR5
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



/**
 *
 * @author JR5
 */
public class Usuario {
    private int id_usuario;
    private String nome;
    private String email;
    private String senha;
    private String cargo;  // Agente, Investigador, Comandante, Administrador
    private String contacto;
    private String foto_perfil;
    private String status;  // Ativo, Inativo, Suspenso, Férias
    private Timestamp data_cadastro;
    private Timestamp data_atualizacao;
    private String perfil;  // Operacional, Tático, Estratégico, Administrativo
    private String estado;  // UF do estado
    private String numero_identificacao;

    // Construtor vazio
    public Usuario() {
        this.status = "Ativo"; // Valor padrão
        this.data_cadastro = new Timestamp(System.currentTimeMillis());
    }

    // Construtor com campos básicos
    public Usuario(String nome, String email, String senha, String cargo) {
        this();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
    }
    
    // Construtor completo (opcional)
    public Usuario(String nome, String email, String senha, String cargo, 
                  String contacto, String status, String perfil, String estado, 
                  String numeroIdentificacao) {
        this(nome, email, senha, cargo);
        this.contacto = contacto;
        this.status = status;
        this.perfil = perfil;
        this.estado = estado;
        this.numero_identificacao = numeroIdentificacao;
    }

    // Getters e Setters
    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(Timestamp data_cadastro) {
        this.data_cadastro = data_cadastro;
    }

    public Timestamp getData_atualizacao() {
        return data_atualizacao;
    }

    public void setData_atualizacao(Timestamp data_atualizacao) {
        this.data_atualizacao = data_atualizacao;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumero_identificacao() {
        return numero_identificacao;
    }

    public void setNumero_identificacao(String numero_identificacao) {
        this.numero_identificacao = numero_identificacao;
    }

    // Método toString melhorado
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id_usuario +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cargo='" + cargo + '\'' +
                ", status='" + status + '\'' +
                ", perfil='" + perfil + '\'' +
                ", numId='" + numero_identificacao + '\'' +
                ", cadastro=" + data_cadastro +
                (data_atualizacao != null ? ", atualizacao=" + data_atualizacao : "") +
                '}';
    }

    // Método para verificar se o usuário está ativo
    public boolean isAtivo() {
        return "Ativo".equalsIgnoreCase(this.status);
    }

    public void setData_criacao(Timestamp timestamp) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setUltimo_login(Timestamp timestamp) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}