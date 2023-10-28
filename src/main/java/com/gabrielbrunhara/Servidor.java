package com.gabrielbrunhara;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private static List<Usuario> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        // Adicione o usuário administrador à lista
        Usuario admin = new Usuario("gabriel", "gmail", "123", "0000", true);
        usuarios.add(admin);

        int porta = 3500; // Porta do servidor

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor aguardando conexões na porta " + porta);

            while (true) {
                Socket clienteSocket = serverSocket.accept(); // Aguarda a conexão de um cliente
                System.out.println("Novo cliente conectado: " + clienteSocket.getInetAddress().getHostAddress());

                // Crie uma thread para lidar com as ações do cliente
                Thread clienteThread = new Thread(new ClienteHandler(clienteSocket, usuarios));
                clienteThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
