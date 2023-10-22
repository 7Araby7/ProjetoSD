package com.gabrielbrunhara;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String tipo; // "admin" ou "user"
    private String token;

    public Usuario(String nome, String email, String senha, String cpf, String tipo) {
        this.nome = nome;
        this.email = email;
        this.senha = hashSenha(senha);
        this.cpf = cpf;
        this.tipo = tipo;
    }

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = hashSenha(senha);
    }
    

    public void cadastrar() {
        // Lógica para cadastrar o usuário no servidor
        // Gere o token JWT e armazene-o no campo 'token'
        // Exemplo:
        this.token = gerarTokenJWT();
    }

    public boolean login(String email, String senha) {
        // Lógica para realizar o login no servidor
        // Verifique o token JWT retornado e armazene-o no campo 'token'
        // Exemplo:
        if (verificarCredenciais(email, senha)) {
            this.token = gerarTokenJWT();
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        // Lógica para fazer o logout no servidor
        this.token = null; // Limpa o token
    }

    private String gerarTokenJWT() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Token válido por 24 horas
                .signWith(key)
                .compact();
    }

    private boolean verificarCredenciais(String email, String senha) {
        // Implemente a verificação das credenciais aqui
        return true; // Simplesmente retornando true como exemplo
    }

    private String hashSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(senha.getBytes());
            return Hex.encodeHexString(hash).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getToken() {
        return token;
    }
}
