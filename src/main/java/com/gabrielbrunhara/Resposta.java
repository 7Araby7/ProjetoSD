package com.gabrielbrunhara;

public class Resposta {
    private String mensagem;
    private boolean error;
    private String token;

    public String getMensagem() {
        return mensagem;
    }

    public boolean isError() {
        return error;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
