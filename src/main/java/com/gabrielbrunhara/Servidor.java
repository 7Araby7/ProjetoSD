package com.gabrielbrunhara;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        int porta = 3500; // Porta do servidor

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor aguardando conexões na porta " + porta);

            while (true) {
                Socket clienteSocket = serverSocket.accept(); // Aguarda a conexão de um cliente
                System.out.println("Novo cliente conectado: " + clienteSocket.getInetAddress().getHostAddress());

                // Crie uma thread para lidar com as ações do cliente
                Thread clienteThread = new Thread(new ClienteHandler(clienteSocket));
                clienteThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
