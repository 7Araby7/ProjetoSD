package com.gabrielbrunhara;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClienteHandler implements Runnable {
    private Socket clienteSocket;
    private List<Usuario> usuarios;

    public ClienteHandler(Socket clienteSocket, List<Usuario> usuarios) {
        this.clienteSocket = clienteSocket;
        this.usuarios = usuarios;
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
                Mensagem mensagem = objectMapper.readValue(mensagemRecebida, Mensagem.class);
                Resposta resposta = processarMensagem(mensagem);

                String respostaJSON = objectMapper.writeValueAsString(resposta);
                saida.write((respostaJSON + "\n").getBytes());
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

    private Resposta processarMensagem(Mensagem mensagem) {
        Resposta resposta = new Resposta();
        resposta.setAcao(mensagem.getAcao());

        if (mensagem.getAcao().equals("cadastro-usuario")) {
            if (mensagem.getIsAdm() && mensagem.getToken().equals("token_de_administrador")) {
                Usuario usuario = new Usuario(mensagem.getNome(), mensagem.getEmail(), mensagem.getSenha(), mensagem.getCpf(),
                        true);
                usuario.cadastrar();
                usuarios.add(usuario);
                resposta.setToken(usuario.getToken());
                resposta.setMessage("Cadastro bem-sucedido!");
            } else {
                Usuario usuario = new Usuario(mensagem.getNome(), mensagem.getEmail(), mensagem.getSenha(), mensagem.getCpf(),
                        false);
                usuario.cadastrar();
                usuarios.add(usuario);
                resposta.setToken(usuario.getToken());
                resposta.setMessage("Cadastro bem-sucedido!");
            }
        } else if (mensagem.getAcao().equals("login")) {
            Usuario usuarioLogado = encontrarUsuarioPorEmailSenha(mensagem.getEmail(), mensagem.getSenha());
            if (usuarioLogado != null) {
                usuarioLogado.login(mensagem.getEmail(), mensagem.getSenha());
                resposta.setToken(usuarioLogado.getToken());
                resposta.setMessage("Login bem-sucedido!");
            } else {
                resposta.setError(true);
                resposta.setMessage("Credenciais inválidas.");
            }
        } else if (mensagem.getAcao().equals("pedido-edicao-usuario")) {
            if (mensagem.getIsAdm() && mensagem.getToken().equals("token_de_administrador")) {
                Usuario detalhesUsuario = encontrarUsuarioPorEmail(mensagem.getEmail());
                resposta.setUser(detalhesUsuario);
            } else {
                resposta.setError(true);
                resposta.setMessage("Ação não autorizada ou token de administrador inválido.");
            }
        } else if (mensagem.getAcao().equals("listar-usuarios")) {
            if (mensagem.getIsAdm() && mensagem.getToken().equals("token_de_administrador")) {
                resposta.setUsers(usuarios);
            } else {
                resposta.setError(true);
                resposta.setMessage("Ação não autorizada ou token de administrador inválido.");
            }
        } else if (mensagem.getAcao().equals("edicao-usuario")) {
            if (mensagem.getIsAdm() && mensagem.getToken().equals("token_de_administrador")) {
                resposta.setMessage("Usuário atualizado com sucesso!");
            } else {
                resposta.setError(true);
                resposta.setMessage("Ação não autorizada ou token de administrador inválido.");
            }
        } else if (mensagem.getAcao().equals("excluir-usuario")) {
            if (mensagem.getIsAdm() && mensagem.getToken().equals("token_de_administrador")) {
                resposta.setMessage("Usuário removido com sucesso!");
            } else {
                resposta.setError(true);
                resposta.setMessage("Ação não autorizada ou token de administrador inválido.");
            }
        } else if (mensagem.getAcao().equals("logout")) {
            if (mensagem.getToken() != null) {
                Usuario usuarioLogado = encontrarUsuarioPorToken(mensagem.getToken());
                if (usuarioLogado != null) {
                    usuarioLogado.logout();
                    resposta.setMessage("Logout bem-sucedido.");
                } else {
                    resposta.setError(true);
                    resposta.setMessage("Token inválido.");
                }
            } else {
                resposta.setError(true);
                resposta.setMessage("Token ausente na mensagem de logout.");
            }
        }

        return resposta;
    }

    private Usuario encontrarUsuarioPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    private Usuario encontrarUsuarioPorEmailSenha(String email, String senha) {
    for (Usuario u : usuarios) {
        if (u.getEmail().equals(email) && u.getSenha().equals(Usuario.hashSenha(senha))) {
            return u;
        }
    }
    return null;
}


    private Usuario encontrarUsuarioPorToken(String token) {
        for (Usuario u : usuarios) {
            if (u.getToken() != null && u.getToken().equals(token)) {
                return u;
            }
        }
        return null;
    }
}
