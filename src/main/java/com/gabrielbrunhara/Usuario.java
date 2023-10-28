package com.gabrielbrunhara;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Date;

public class Usuario {
    private static final String SECRET_KEY = "AoT3QFTTEkj16rCby/TPVBWvfSQHL3GeEz3zVwEd6LDrQDT97sgDY8HJyxgnH79jupBWFOQ1+7fRPBLZfpuA2lwwHqTgk+NJcWQnDpHn31CVm63Or5c5gb4H7/eSIdd+7hf3v+0a5qVsnyxkHbcxXquk9ezxrUe93cFppxH4/kF/kGBBamm3kuUVbdBUY39c4U3NRkzSO+XdGs69ssK5SPzshn01axCJoNXqqj+ytebuMwF8oI9+ZDqj/XsQ1CLnChbsL+HCl68ioTeoYU9PLrO4on+rNHGPI0Cx6HrVse7M3WQBPGzOd1TvRh9eWJrvQrP/hm6kOR7KrWKuyJzrQh7OoDxrweXFH8toXeQRD8=";
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private boolean isAdm; // "admin" ou "user"
    private String token;
    private boolean logado = false;

    public Usuario() {
        // Construtor vazio necessário para desserialização
    }

    public Usuario(String nome, String email, String senha, String cpf, boolean isAdm) {
        this.nome = nome;
        this.email = email;
        this.senha = hashSenha(senha);
        this.cpf = cpf;
        this.isAdm = isAdm;
        this.token = null;
    }

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = hashSenha(senha);
    }
    
    public void setToken(String token) {
        this.token = token;
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
            logado = true;
            token = gerarTokenJWT(); // Atribua o token JWT ao usuário no login
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        // Realize o logout e defina o estado de login como falso
        logado = false;
        token = null;
    }

    public boolean isLogado() {
        return logado;
    }

    private String gerarTokenJWT() {
        return Jwts.builder()
                .claim("user_id", email)
                .claim("admin", isAdm)
                .setSubject(email)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private boolean verificarCredenciais(String email, String senha) {
        // Verifica se o email e a senha correspondem às credenciais armazenadas
        return this.email.equals(email) && this.senha.equals(hashSenha(senha));
    }

    public static String hashSenha(String senha) {
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

    public void setIsAdm(boolean b) {
    }
}
