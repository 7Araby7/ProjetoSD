package com.gabrielbrunhara;

public class Mensagem {
    private String acao;
    private String token;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private boolean isAdm;

    public Mensagem() {
        // Construtor vazio necessário para desserialização
    }

    public String getAcao() {
        return acao;
    }

    public String getToken() {
        return token;
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

    public boolean getIsAdm() {
        return isAdm;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public void setToken(String token) {
        this.token = token;
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

    public void setIsAdm(boolean isAdm) {
        this.isAdm = isAdm;
    }
}
