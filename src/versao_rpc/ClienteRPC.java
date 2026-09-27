package versao_rpc;

import java.util.LinkedHashMap;
import java.util.Map;
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
            Map<String, String> dados = new LinkedHashMap<>();
            dados.put("id", consulta.getId());
            dados.put("nomePaciente", consulta.getNomePaciente());
            dados.put("cpfPaciente", consulta.getCpfPaciente());
            dados.put("telefonePaciente", consulta.getTelefonePaciente());
            dados.put("especialidade", consulta.getEspecialidade());
            dados.put("nomeMedico", consulta.getNomeMedico());
            dados.put("crmMedico", consulta.getCrmMedico());
            dados.put("dataHora", consulta.getDataHora());
            // Mede somente a chamada de agendamento e o recebimento da resposta.
            long inicio = System.nanoTime();
            String resposta = servidor.agendarConsulta(dados);
            double tempoMs = (System.nanoTime() - inicio) / 1_000_000.0;

            System.out.println("\nRESPOSTA DO SERVIDOR: " + resposta);
            System.out.printf("Tempo de resposta RPC do agendamento: %.3f ms.%n", tempoMs);
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
