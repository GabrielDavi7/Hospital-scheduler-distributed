//Cliente rmi mesmo objetivo do cliente socket, mas com a facilidade de enviar objetos inteiros pela rede, sem precisar serializar manualmente.

package versao_rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import modelo.Consulta;

public class ClienteRMI {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            InterfaceRMI servidor = (InterfaceRMI) registry.lookup("HospitalService");
            
            Scanner teclado = new Scanner(System.in);
            System.out.println("--- BEM-VINDO AO HOSPITAL DISTRIBUÍDO (RMI) ---");
            
            System.out.println("\nEspecialidades e Médicos Disponíveis:");
            String[] medicos = servidor.listarMedicos().split(" \\| ");
            for (String medico : medicos) {
                System.out.println(medico);
            }
            
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
            System.out.print("Data e Hora (Ex: 25/10/2026 14:30): ");
            String data = teclado.nextLine();
            
            Consulta novaConsulta = new Consulta(nome, cpf, telefone, especialidade, medico, crm, data);
            
            long tempoInicio = System.currentTimeMillis();
            
            String resposta = servidor.agendarConsulta(novaConsulta);
            
            long tempoTotal = System.currentTimeMillis() - tempoInicio;
            
            System.out.println("\nRESPOSTA DO SERVIDOR: " + resposta);
            System.out.println("📊 Tempo de resposta do servidor RMI: " + tempoTotal + " ms.");
            teclado.close();
            
        } catch (Exception e) {
            System.out.println("Erro no Cliente RMI: " + e.getMessage());
        }
    }
}