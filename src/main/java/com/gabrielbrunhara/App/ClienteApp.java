package com.gabrielbrunhara.App;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielbrunhara.Mensagem;
import com.gabrielbrunhara.Resposta;
import com.gabrielbrunhara.Usuario;

public class ClienteApp {
    private static boolean logado = false;
    private static String token;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o endereço IP do servidor:");
        String servidorIP = scanner.nextLine();

        System.out.println("Digite a porta do servidor:");
        int servidorPorta = scanner.nextInt();

        try {
            Socket clienteSocket = new Socket(servidorIP, servidorPorta);
            PrintWriter out = new PrintWriter(clienteSocket.getOutputStream(), true);
            Scanner in = new Scanner(clienteSocket.getInputStream());

            ObjectMapper objectMapper = new ObjectMapper();

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
                            System.out.println("Usuario administrador ? s/n:");
                            String tipo = scanner.next();

                            Mensagem mensagemCadastro = new Mensagem();
                            mensagemCadastro.setAcao("cadastro-usuario");
                            mensagemCadastro.setNome(nome);
                            mensagemCadastro.setEmail(email);
                            mensagemCadastro.setSenha(senha);
                            mensagemCadastro.setCpf(cpf);
                            if (tipo.equals("s")) {
                                System.out.println("Digite o token de administrador:");
                                String token = scanner.next();
                                mensagemCadastro.setIsAdm(true);
                                mensagemCadastro.setToken(token);
                            } else {
                                mensagemCadastro.setIsAdm(false);
                            }

                            out.println(objectMapper.writeValueAsString(mensagemCadastro));

                            if (in.hasNextLine()) {
                                String respostaCadastro = in.nextLine();
                                Resposta respostaCadastroObj = objectMapper.readValue(respostaCadastro, Resposta.class);

                                Usuario usuarioLogado = null;

                                if (!respostaCadastroObj.isError()) {
                                    logado = true;
                                    usuarioLogado = new Usuario(email, senha);
                                    System.out
                                            .println("Cadastro bem-sucedido. Token: " + respostaCadastroObj.getToken());
                                } else {
                                    System.out.println("Erro no cadastro: " + respostaCadastroObj.getMessage());
                                }
                            } else {
                                System.out.println("Resposta do servidor não disponível.");
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

                            Mensagem mensagemLogin = new Mensagem();
                            mensagemLogin.setAcao("login");
                            mensagemLogin.setEmail(email);
                            mensagemLogin.setSenha(senha);

                            out.println(objectMapper.writeValueAsString(mensagemLogin));

                            if (in.hasNextLine()) {
                                String respostaLogin = in.nextLine();
                                Resposta respostaLoginObj = objectMapper.readValue(respostaLogin, Resposta.class);

                                if (!respostaLoginObj.isError()) {
                                    logado = true;
                                    System.out.println("Login bem-sucedido. Token: " + respostaLoginObj.getToken());
                                } else {
                                    System.out.println("Erro no login: " + respostaLoginObj.getMessage());
                                }
                            } else {
                                System.out.println("Resposta do servidor não disponível.");
                            }
                        } else {
                            System.out.println("Você já está logado.");
                        }
                        break;

                        case 3:
                        if (logado) {
                            Mensagem mensagemLogout = new Mensagem();
                            mensagemLogout.setAcao("logout");
                            mensagemLogout.setToken(token); // Use a variável token aqui
            
                            out.println(objectMapper.writeValueAsString(mensagemLogout));
            
                            if (in.hasNextLine()) {
                                String respostaLogout = in.nextLine();
                                Resposta respostaLogoutObj = objectMapper.readValue(respostaLogout, Resposta.class);
            
                                if (!respostaLogoutObj.isError()) {
                                    logado = false;
                                    System.out.println("Logout bem-sucedido.");
                                } else {
                                    System.out.println("Erro no logout: " + respostaLogoutObj.getMessage());
                                }
                            } else {
                                System.out.println("Resposta do servidor não disponível.");
                            }
                        } else {
                            System.out.println("Você não está logado.");
                        }
                        break;

                    case 4:
                        clienteSocket.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}