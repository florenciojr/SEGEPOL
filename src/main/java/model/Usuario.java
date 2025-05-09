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


import java.sql.Timestamp;

public class Usuario {
    private int id_usuario;
    private String nome;
    private String email;
    private String senha;
    private String cargo;
    private String telefone;
    private String foto_perfil;
    private String status;
    private Timestamp data_cadastro;
    private String perfil;
    private String numero_identificacao;
    private Timestamp data_atualizacao;
    private Timestamp ultimo_login;
    private String ip_ultimo_login;
    private boolean oculto;

    // Construtor padrão
    public Usuario() {
    }

    // Construtor com campos básicos
    public Usuario(String nome, String email, String senha, String cargo, String perfil, String numero_identificacao) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.perfil = perfil;
        this.numero_identificacao = numero_identificacao;
        this.status = "Ativo"; // Valor padrão
    }

    // Getters e Setters para todos os campos

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getNumero_identificacao() {
        return numero_identificacao;
    }

    public void setNumero_identificacao(String numero_identificacao) {
        this.numero_identificacao = numero_identificacao;
    }

    public Timestamp getData_atualizacao() {
        return data_atualizacao;
    }

    public void setData_atualizacao(Timestamp data_atualizacao) {
        this.data_atualizacao = data_atualizacao;
    }

    public Timestamp getUltimo_login() {
        return ultimo_login;
    }

    public void setUltimo_login(Timestamp ultimo_login) {
        this.ultimo_login = ultimo_login;
    }

    public String getIp_ultimo_login() {
        return ip_ultimo_login;
    }

    public void setIp_ultimo_login(String ip_ultimo_login) {
        this.ip_ultimo_login = ip_ultimo_login;
    }

    public boolean isOculto() {
        return oculto;
    }

    public void setOculto(boolean oculto) {
        this.oculto = oculto;
    }

    // Método toString para facilitar a visualização
    @Override
    public String toString() {
        return "Usuario{" +
                "id_usuario=" + id_usuario +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cargo='" + cargo + '\'' +
                ", perfil='" + perfil + '\'' +
                ", status='" + status + '\'' +
                ", numero_identificacao='" + numero_identificacao + '\'' +
                ", ultimo_login=" + ultimo_login +
                '}';
    }

    // Métodos auxiliares de negócio
    public boolean isAtivo() {
        return "Ativo".equalsIgnoreCase(this.status);
    }

    public boolean isAdministrador() {
        return "Administrador".equalsIgnoreCase(this.cargo) || 
               "Super Admin".equalsIgnoreCase(this.perfil);
    }

    public boolean podeGerenciarUsuarios() {
        return isAdministrador() || 
               "Chefe de gestor de pessoal".equalsIgnoreCase(this.cargo);
    }
}
