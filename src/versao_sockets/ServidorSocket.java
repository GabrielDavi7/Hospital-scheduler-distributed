// Esse arquivo é o servidor que recebe as requisições do cliente via Sockets, ele processa os comandos e retorna respostas. 
// Ele mantém uma lista de agendamentos em memória.

package versao_sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import modelo.Consulta;

public class ServidorSocket {

    private static List<Consulta> agendamentos = new ArrayList<>();
    private static int contadorId = 1;

    private static final String LISTA_MEDICOS = 
            "1. Cardiologia - Dr. Joao Silva (CRM: 12345) | " +
            "2. Ortopedia - Dra. Maria Souza (CRM: 54321) | " +
            "3. Pediatria - Dra. Ana Lima (CRM: 98765)";

    public static void main(String[] args) {
        int porta = 5000;

        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("🏥 Servidor Hospitalar (Sockets) rodando na porta " + porta);

            while (true) {
                Socket cliente = servidor.accept();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);

                String mensagemCliente = entrada.readLine();

                if (mensagemCliente != null) {
                    if (mensagemCliente.equals("LISTAR")) {
                        saida.println(LISTA_MEDICOS);
                    } 
                    else if (mensagemCliente.startsWith("AGENDAR;")) {
                        String[] dados = mensagemCliente.split(";");
                        
                        String nome = dados[1];
                        String cpf = dados[2];
                        String telefone = dados[3];
                        String especialidade = dados[4];
                        String medico = dados[5];
                        String crm = dados[6];
                        String data = dados[7];

                        Consulta novaConsulta = new Consulta(nome, cpf, telefone, especialidade, medico, crm, data);
                        novaConsulta.setId("AG-" + contadorId++);
                        
                        agendamentos.add(novaConsulta);

                        saida.println("SUCESSO;Consulta agendada com ID: " + novaConsulta.getId());
                        System.out.println("Novo agendamento salvo na memoria: " + novaConsulta.getNomePaciente());
                    } 
                    else if (mensagemCliente.equals("VER_AGENDAMENTOS")) {
                        if (agendamentos.isEmpty()) {
                            saida.println("Nenhuma consulta agendada no momento.");
                        } else {
                            StringBuilder lista = new StringBuilder();
                            for (Consulta c : agendamentos) {
                                lista.append(c.toString()).append(" @ ");
                            }
                            saida.println(lista.toString());
                        }
                        System.out.println("Enviando agenda completa para a recepção.");
                    }
                    
                    else {
                        saida.println("ERRO;Comando desconhecido.");
                    }
                }
                cliente.close();
            }
        } catch (Exception e) {
            System.out.println("Erro no servidor: " + e.getMessage());
        }
    }
}