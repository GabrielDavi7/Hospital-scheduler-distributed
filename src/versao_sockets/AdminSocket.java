// Esse arquivo é o painel administrativo que se conecta ao servidor via Sockets e solicita a lista de agendamentos cadastrados. 
// Ele exibe as consultas recebidas do servidor no terminal.

package versao_sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AdminSocket {

    public static void main(String[] args) {
        String ipServidor = "127.0.0.1";
        int porta = 5000;
        Scanner teclado = new Scanner(System.in);

        System.out.println("--- PAINEL DA RECEPÇÃO: CONTROLE DE AGENDAS ---\n");
        System.out.println("1. Ver toda a agenda do hospital");
        System.out.println("2. Buscar consultas por CPF do Paciente");
        System.out.println("3. Buscar consultas por CRM do Médico");
        System.out.print("Escolha uma opção: ");
        String opcao = teclado.nextLine();

        String comandoEnvio = "";

        if (opcao.equals("1")) {
            comandoEnvio = "VER_AGENDAMENTOS";
        } else if (opcao.equals("2")) {
            System.out.print("Digite o CPF do Paciente (ex: 111.111.111-11): ");
            String cpf = teclado.nextLine();
            comandoEnvio = "BUSCAR_CPF;" + cpf;
        } else if (opcao.equals("3")) {
            System.out.print("Digite o CRM do Médico (ex: 99999): ");
            String crm = teclado.nextLine();
            comandoEnvio = "BUSCAR_CRM;" + crm;
        } else {
            System.out.println("Opção inválida. Encerrando.");
            teclado.close();
            return;
        }

        try {
            long tempoInicio = System.currentTimeMillis();
            
            Socket socket = new Socket(ipServidor, porta);
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            saida.println(comandoEnvio);
            
            String resposta = entrada.readLine();
            
            System.out.println("\n--- RESULTADO DA BUSCA ---");
            if (resposta.startsWith("Nenhuma")) {
                System.out.println(resposta);
            } else {
                String[] consultas = resposta.split(" @ ");
                for (String c : consultas) {
                    System.out.println(c);
                    System.out.println("--------------------------------------------------");
                }
            }
            
            socket.close();

            long tempoTotal = System.currentTimeMillis() - tempoInicio;
            System.out.println("📊 Tempo de resposta do servidor: " + tempoTotal + " ms.");

        } catch (Exception e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        } finally {
            teclado.close();
        }
    }
}