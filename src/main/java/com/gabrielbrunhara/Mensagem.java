package com.gabrielbrunhara;

public class Mensagem {
    private String acao;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String tipo;

    public String getAcao() {
        return acao;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTipo() {
        return tipo;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
