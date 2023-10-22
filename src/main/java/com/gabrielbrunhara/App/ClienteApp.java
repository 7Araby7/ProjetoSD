package com.gabrielbrunhara.App;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper; // Importe o ObjectMapper
import com.gabrielbrunhara.Mensagem;
import com.gabrielbrunhara.Resposta;

public class ClienteApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o endereço IP do servidor:");
        String servidorIP = scanner.nextLine(); // Endereço IP ou hostname do servidor

        System.out.println("Digite a porta do servidor:");
        int servidorPorta = scanner.nextInt(); // Porta do servidor

        try {
            Socket clienteSocket = new Socket(servidorIP, servidorPorta);
            PrintWriter out = new PrintWriter(clienteSocket.getOutputStream(), true);
            Scanner in = new Scanner(clienteSocket.getInputStream());

            ObjectMapper objectMapper = new ObjectMapper(); // Inicialize o ObjectMapper

            boolean logado = false;

            while (true) {
                System.out.println("Escolha uma opção:");
                System.out.println("1 - Cadastrar");
                System.out.println("2 - Login");
                System.out.println("3 - Logout");
                System.out.println("4 - Sair");

                int opcao = scanner.nextInt();

                switch (opcao) {
                   case 1:
                        if (!logado) {
                            System.out.println("Digite seu nome:");
                            String nome = scanner.next();
                            System.out.println("Digite seu email:");
                            String email = scanner.next();
                            System.out.println("Digite sua senha:");
                            String senha = scanner.next();
                            System.out.println("Digite seu CPF:");
                            String cpf = scanner.next();
                            System.out.println("Digite seu tipo (admin ou user):");
                            String tipo = scanner.next();

                            // Cria uma mensagem de cadastro
                            Mensagem mensagemCadastro = new Mensagem();
                            mensagemCadastro.setAcao("cadastrar");
                            mensagemCadastro.setNome(nome);
                            mensagemCadastro.setEmail(email);
                            mensagemCadastro.setSenha(senha);
                            mensagemCadastro.setCpf(cpf);
                            mensagemCadastro.setTipo(tipo);

                            // Converte a mensagem em JSON e envia para o servidor
                            out.println(objectMapper.writeValueAsString(mensagemCadastro));

                            // Aguarda a resposta do servidor
                            String respostaCadastro = in.nextLine();
                            Resposta respostaCadastroObj = objectMapper.readValue(respostaCadastro, Resposta.class);

                            if (!respostaCadastroObj.isError()) {
                                logado = true;
                                System.out.println("Cadastro bem-sucedido. Token: " + respostaCadastroObj.getToken());
                            } else {
                                System.out.println("Erro no cadastro: " + respostaCadastroObj.getMensagem());
                            }
                        } else {
                            System.out.println("Você já está logado.");
                        }
                        break;

                    case 2:
                        if (!logado) {
                            System.out.println("Digite seu email:");
                            String email = scanner.next();
                            System.out.println("Digite sua senha:");
                            String senha = scanner.next();

                            // Cria uma mensagem de login
                            Mensagem mensagemLogin = new Mensagem();
                            mensagemLogin.setAcao("login");
                            mensagemLogin.setEmail(email);
                            mensagemLogin.setSenha(senha);

                            // Converte a mensagem em JSON e envia para o servidor
                            out.println(objectMapper.writeValueAsString(mensagemLogin));

                            // Aguarda a resposta do servidor
                            String respostaLogin = in.nextLine();
                            Resposta respostaLoginObj = objectMapper.readValue(respostaLogin, Resposta.class);

                            if (!respostaLoginObj.isError()) {
                                logado = true;
                                System.out.println("Login bem-sucedido. Token: " + respostaLoginObj.getToken());
                            } else {
                                System.out.println("Erro no login: " + respostaLoginObj.getMensagem());
                            }
                        } else {
                            System.out.println("Você já está logado.");
                        }
                        break;

                    case 3:
                        if (logado) {
                            // Cria uma mensagem de logout
                            Mensagem mensagemLogout = new Mensagem();
                            mensagemLogout.setAcao("logout");

                            // Converte a mensagem em JSON e envia para o servidor
                            out.println(objectMapper.writeValueAsString(mensagemLogout));

                            // Aguarda a resposta do servidor
                            String respostaLogout = in.nextLine();
                            Resposta respostaLogoutObj = objectMapper.readValue(respostaLogout, Resposta.class);

                            if (!respostaLogoutObj.isError()) {
                                logado = false;
                                System.out.println("Logout bem-sucedido.");
                            } else {
                                System.out.println("Erro no logout: " + respostaLogoutObj.getMensagem());
                            }
                        } else {
                            System.out.println("Você não está logado.");
                        }
                        break;

                    case 4:
                        clienteSocket.close();
                        System.exit(0);

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
