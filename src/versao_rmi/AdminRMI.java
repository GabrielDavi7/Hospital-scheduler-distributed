package versao_rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;
import modelo.Consulta;

public class AdminRMI {
    public static void main(String[] args) {
        try {
            String host = args.length > 0 ? args[0] : "127.0.0.1";
            int porta = args.length > 1 ? Integer.parseInt(args[1]) : 1099;
            Registry registry = LocateRegistry.getRegistry(host, porta);
            InterfaceRMI servidor = (InterfaceRMI) registry.lookup("HospitalService");
            Scanner teclado = new Scanner(System.in);

            System.out.println("--- PAINEL DA RECEPÇÃO RMI ---\n");
            System.out.println("1. Ver toda a agenda do hospital");
            System.out.println("2. Buscar consultas por CPF do Paciente");
            System.out.println("3. Buscar consultas por CRM do Médico");
            System.out.print("Escolha uma opção: ");
            String opcao = teclado.nextLine();

            String filtro = "";
            if (opcao.equals("2")) {
                System.out.print("Digite o CPF (ex: 111.111.111-11): ");
                filtro = teclado.nextLine();
            } else if (opcao.equals("3")) {
                System.out.print("Digite o CRM (ex: 99999): ");
                filtro = teclado.nextLine();
            } else if (!opcao.equals("1")) {
                System.out.println("Opção inválida.");
                teclado.close();
                return;
            }

            long tempoInicio = System.nanoTime();
            List<Consulta> resultados;
            if (opcao.equals("2")) {
                resultados = servidor.buscarPorCpf(filtro);
            } else if (opcao.equals("3")) {
                resultados = servidor.buscarPorCrm(filtro);
            } else {
                resultados = servidor.verAgendamentos();
            }
            double tempoChamadaMs = (System.nanoTime() - tempoInicio) / 1_000_000.0;

            long inicioImpressao = System.nanoTime();
            System.out.println("\n--- RESULTADO DA BUSCA ---");
            if (resultados == null || resultados.isEmpty()) {
                System.out.println("Nenhuma consulta encontrada.");
            } else {
                for (Consulta c : resultados) {
                    System.out.println(c.toString());
                    System.out.println("--------------------------------------------------");
                }
            }

            double tempoImpressaoMs = (System.nanoTime() - inicioImpressao) / 1_000_000.0;
            System.out.println("Consultas retornadas: " + (resultados == null ? 0 : resultados.size()));
            System.out.printf("Tempo total da chamada RMI (cliente): %.3f ms.%n", tempoChamadaMs);
            System.out.printf("Tempo de impressao dos resultados (cliente): %.3f ms.%n", tempoImpressaoMs);
            System.out.println("Tempo de processamento da busca: consulte o terminal do servidor.");
            teclado.close();

        } catch (Exception e) {
            System.out.println("Erro no Painel RMI: " + e.getMessage());
        }
    }
}
