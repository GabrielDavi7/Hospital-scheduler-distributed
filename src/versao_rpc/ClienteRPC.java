package versao_rpc;

import java.util.Scanner;
import modelo.Consulta;

public class ClienteRPC {
    public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in)) {
            InterfaceRPC servidor = ConexaoRPC.conectar(args);
            System.out.println("--- BEM-VINDO AO HOSPITAL DISTRIBUÍDO (RPC) ---");
            System.out.println("\nEspecialidades e Médicos Disponíveis:");
            for (String medico : servidor.listarMedicos().split(" \\| ")) {
                System.out.println(medico);
            }
            System.out.println("\n--- VAMOS AGENDAR SUA CONSULTA ---");
            String nome = ler(teclado, "Seu Nome: ");
            String cpf = ler(teclado, "Seu CPF: ");
            String telefone = ler(teclado, "Seu Telefone: ");
            String especialidade = ler(teclado, "Especialidade desejada: ");
            String medico = ler(teclado, "Nome do Médico escolhido: ");
            String crm = ler(teclado, "CRM do Médico: ");
            String data = ler(teclado, "Data e Hora (Ex: 25/10/2026 14:30): ");
            Consulta consulta = new Consulta(nome, cpf, telefone, especialidade, medico, crm, data);
            System.out.println("\nRESPOSTA DO SERVIDOR: "
                    + servidor.agendarConsulta(DadosRPC.paraMapa(consulta)));
        } catch (Exception e) {
            System.err.println("Erro no Cliente RPC: " + e.getMessage());
            System.exit(1);
        }
    }

    private static String ler(Scanner teclado, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = teclado.nextLine().trim();
            if (!valor.isEmpty()) { return valor; }
            System.out.println("Preencha este campo.");
        }
    }
}
