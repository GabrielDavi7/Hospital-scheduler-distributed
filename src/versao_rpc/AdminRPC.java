package versao_rpc;

import java.util.Map;
import java.util.Scanner;
import modelo.Consulta;

public class AdminRPC {
    public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in)) {
            InterfaceRPC servidor = ConexaoRPC.conectar(args);
            System.out.println("--- PAINEL DA RECEPÇÃO RPC ---\n");
            System.out.println("1. Ver toda a agenda do hospital");
            System.out.println("2. Buscar consultas por CPF do Paciente");
            System.out.println("3. Buscar consultas por CRM do Médico");
            System.out.print("Escolha uma opção: ");
            String opcao = teclado.nextLine().trim();
            if (!opcao.equals("1") && !opcao.equals("2") && !opcao.equals("3")) {
                System.out.println("Opção inválida.");
                return;
            }
            String filtro = "";
            if (!opcao.equals("1")) {
                System.out.print(opcao.equals("2") ? "Digite o CPF: " : "Digite o CRM: ");
                filtro = teclado.nextLine().trim();
            }

            // Mede somente a chamada remota, incluindo transporte e retorno.
            long inicio = System.nanoTime();
            Object[] resultados;
            switch (opcao) {
                case "2": resultados = servidor.buscarPorCpf(filtro); break;
                case "3": resultados = servidor.buscarPorCrm(filtro); break;
                default: resultados = servidor.verAgendamentos();
            }
            double tempoMs = (System.nanoTime() - inicio) / 1_000_000.0;

            System.out.println("\n--- RESULTADO DA BUSCA ---");
            if (resultados.length == 0) {
                System.out.println("Nenhuma consulta encontrada.");
            }
            for (Object resultado : resultados) {
                Map<?, ?> dados = (Map<?, ?>) resultado;
                Consulta consulta = new Consulta(
                        campo(dados, "nomePaciente"), campo(dados, "cpfPaciente"),
                        campo(dados, "telefonePaciente"), campo(dados, "especialidade"),
                        campo(dados, "nomeMedico"), campo(dados, "crmMedico"),
                        campo(dados, "dataHora"));
                if (dados.get("id") instanceof String) {
                    consulta.setId((String) dados.get("id"));
                }
                System.out.println(consulta);
                System.out.println("--------------------------------------------------");
            }
            System.out.println("Consultas encontradas: " + resultados.length);
            System.out.printf("Tempo de resposta RPC: %.3f ms.%n", tempoMs);
        } catch (Exception e) {
            System.err.println("Erro no Painel RPC: " + e.getMessage());
            System.exit(1);
        }
    }

    private static String campo(Map<?, ?> dados, String nome) {
        Object valor = dados.get(nome);
        if (!(valor instanceof String) || ((String) valor).trim().isEmpty()) {
            throw new IllegalArgumentException("Campo obrigatorio: " + nome);
        }
        return (String) valor;
    }
}
