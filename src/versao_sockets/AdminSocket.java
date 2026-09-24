// Esse arquivo é o painel administrativo que se conecta ao servidor via Sockets e solicita a lista de agendamentos cadastrados. 
// Ele exibe as consultas recebidas do servidor no terminal.

package versao_sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AdminSocket {

    public static void main(String[] args) {
        String ipServidor = "127.0.0.1";
        int porta = 5000;

        try {
            System.out.println("--- PAINEL COM AS CONSULTAS CADASTRADAS NO SOCKET ---\n");
            
            long tempoInicio = System.currentTimeMillis();
            
            Socket socket = new Socket(ipServidor, porta);
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            saida.println("VER_AGENDAMENTOS");
            
            String resposta = entrada.readLine();
            
            if (resposta.equals("Nenhuma consulta agendada no momento.")) {
                System.out.println(resposta);
            } else {
                String[] consultas = resposta.split(" @ ");
                for (String c : consultas) {
                    System.out.println(c);
                    System.out.println("--------------------------------------------------");
                }
            }
            
            socket.close();

            long tempoFim = System.currentTimeMillis();
            long tempoTotal = tempoFim - tempoInicio;
            
            System.out.println("📊 Tempo total para buscar e exibir a agenda: " + tempoTotal + " ms.");

        } catch (Exception e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        }
    }
}