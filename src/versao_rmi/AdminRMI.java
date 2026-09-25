//Painel de administração RMI, mesmo objetivo do painel de administração via socket filtra uma lista de agendamentos cadastrados no servidor RMI e exibe no terminal.

package versao_rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;
import modelo.Consulta;

public class AdminRMI {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            InterfaceRMI servidor = (InterfaceRMI) registry.lookup("HospitalService");
            Scanner teclado = new Scanner(System.in);

            System.out.println("--- PAINEL DA RECEPÇÃO RMI ---\n");
            System.out.println("1. Ver toda a agenda do hospital");
            System.out.println("2. Buscar consultas por CPF do Paciente");
            System.out.println("3. Buscar consultas por CRM do Médico");
            System.out.print("Escolha uma opção: ");
            String opcao = teclado.nextLine();

            long tempoInicio = System.currentTimeMillis();
            List<Consulta> resultados = null;

            if (opcao.equals("1")) {
                resultados = servidor.verAgendamentos();
            } else if (opcao.equals("2")) {
                System.out.print("Digite o CPF (ex: 111.111.111-11): ");
                String cpf = teclado.nextLine();
                resultados = servidor.buscarPorCpf(cpf);
            } else if (opcao.equals("3")) {
                System.out.print("Digite o CRM (ex: 99999): ");
                String crm = teclado.nextLine();
                resultados = servidor.buscarPorCrm(crm);
            }

            System.out.println("\n--- RESULTADO DA BUSCA ---");
            if (resultados == null || resultados.isEmpty()) {
                System.out.println("Nenhuma consulta encontrada.");
            } else {
                for (Consulta c : resultados) {
                    System.out.println(c.toString());
                    System.out.println("--------------------------------------------------");
                }
            }

            long tempoTotal = System.currentTimeMillis() - tempoInicio;
            System.out.println("Tempo de resposta RMI: " + tempoTotal + " ms.");
            teclado.close();

        } catch (Exception e) {
            System.out.println("Erro no Painel RMI: " + e.getMessage());
        }
    }
}