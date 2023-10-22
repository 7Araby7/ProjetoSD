package com.gabrielbrunhara;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClienteHandler implements Runnable {
    private Socket clienteSocket;
    private Usuario usuario; // Instância do usuário

    public ClienteHandler(Socket clienteSocket) {
        this.clienteSocket = clienteSocket;
    }

    @Override
    public void run() {
        try {
            InputStream entrada = clienteSocket.getInputStream();
            OutputStream saida = clienteSocket.getOutputStream();
            Scanner scanner = new Scanner(entrada);
            ObjectMapper objectMapper = new ObjectMapper();

            while (scanner.hasNextLine()) {
                String mensagemRecebida = scanner.nextLine();

                // Lógica para processar a mensagem recebida em JSON
                Mensagem mensagem = objectMapper.readValue(mensagemRecebida, Mensagem.class);
                String resposta = processarMensagem(mensagem);

                // Enviar a resposta de volta ao cliente
                saida.write((resposta + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processarMensagem(Mensagem mensagem) {
        String acao = mensagem.getAcao();
        ObjectMapper objectMapper = new ObjectMapper();
        Resposta resposta = new Resposta();

        if (acao.equals("cadastrar")) {
            // Lógica para cadastrar o usuário
            usuario = new Usuario(mensagem.getNome(), mensagem.getEmail(), mensagem.getSenha(), mensagem.getCpf(), mensagem.getTipo());
            usuario.cadastrar();
            resposta.setToken(usuario.getToken());
        } else if (acao.equals("login")) {
            if (usuario != null) {
                resposta.setMensagem("Usuário já logado.");
            } else {
                // Lógica para fazer login
                usuario = new Usuario(mensagem.getEmail(), mensagem.getSenha());
                if (usuario.login(mensagem.getEmail(), mensagem.getSenha())) {
                    resposta.setToken(usuario.getToken());
                } else {
                    resposta.setError(true);
                    resposta.setMensagem("Login falhou. Verifique as credenciais.");
                }
            }
        } else if (acao.equals("logout")) {
            if (usuario == null) {
                resposta.setMensagem("Nenhum usuário logado.");
            } else {
                // Lógica para fazer logout
                usuario.logout();
            }
        } else {
            resposta.setError(true);
            resposta.setMensagem("Ação desconhecida.");
        }

        try {
            return objectMapper.writeValueAsString(resposta);
        } catch (IOException e) {
            e.printStackTrace();
            return "{}"; // Resposta vazia em caso de erro
        }
    }
}
