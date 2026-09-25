//Esse arquivo serve para inserir os dados no servidor digitando no terminal, 
//ele se conecta ao servidor via Sockets, envia os dados do agendamento e recebe a resposta do servidor.

package versao_sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteSocket {

    public static void main(String[] args) {
        String ipServidor = "127.0.0.1";
        int porta = 5000;
        Scanner teclado = new Scanner(System.in);

        try {
            System.out.println("--- BEM-VINDO AO HOSPITAL DISTRIBUÍDO ---");
            
            Socket socketLista = new Socket(ipServidor, porta);
            PrintWriter saidaLista = new PrintWriter(socketLista.getOutputStream(), true);
            BufferedReader entradaLista = new BufferedReader(new InputStreamReader(socketLista.getInputStream()));
            
            saidaLista.println("LISTAR");
            String respostaLista = entradaLista.readLine();
            
            System.out.println("\nEspecialidades e Médicos Disponíveis:");
            String[] medicos = respostaLista.split(" \\| ");
            for (String medico : medicos) {
                System.out.println(medico);
            }
            socketLista.close();

            System.out.println("\n--- VAMOS AGENDAR SUA CONSULTA ---");
            System.out.print("Seu Nome: ");
            String nome = teclado.nextLine();
            
            System.out.print("Seu CPF: ");
            String cpf = teclado.nextLine();
            
            System.out.print("Seu Telefone: ");
            String telefone = teclado.nextLine();
            
            System.out.print("Especialidade desejada: ");
            String especialidade = teclado.nextLine();
            
            System.out.print("Nome do Médico escolhido: ");
            String medico = teclado.nextLine();
            
            System.out.print("CRM do Médico: ");
            String crm = teclado.nextLine();
            
            System.out.print("Data e Hora (Ex: 25/09/2026 14:30): ");
            String data = teclado.nextLine();

            String comandoAgendar = "AGENDAR;" + nome + ";" + cpf + ";" + telefone + ";" + especialidade + ";" + medico + ";" + crm + ";" + data;

            Socket socketAgendamento = new Socket(ipServidor, porta);
            PrintWriter saidaAgendamento = new PrintWriter(socketAgendamento.getOutputStream(), true);
            BufferedReader entradaAgendamento = new BufferedReader(new InputStreamReader(socketAgendamento.getInputStream()));
            
            saidaAgendamento.println(comandoAgendar);
            String respostaAgendamento = entradaAgendamento.readLine();
            
            System.out.println("\nRESPOSTA DO SERVIDOR: " + respostaAgendamento);
            socketAgendamento.close();

        } catch (Exception e) {
            System.out.println("Erro na comunicação: " + e.getMessage());
        } finally {
            teclado.close();
        }
    }
}