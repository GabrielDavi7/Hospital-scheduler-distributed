package versao_rpc;

import java.util.Random;
import modelo.Consulta;

public class PovoarRPC {
    public static void main(String[] args) {
        int quantidadeTestes = 1000;
        int confirmados = 0;
        String[] cpfs = {"111.111.111-11", "222.222.222-22", "333.333.333-33", "444.444.444-44", "555.555.555-55"};
        String[] especialidades = {"Cardiologia", "Ortopedia", "Pediatria"};
        String[] medicos = {"Dr. Joao Silva", "Dra. Maria Souza", "Dra. Ana Lima"};
        String[] crms = {"12345", "54321", "98765"};
        String[] dias = {"10/10/2026", "11/10/2026", "12/10/2026", "13/10/2026", "14/10/2026"};
        String[] horas = {"09:00", "10:30", "14:00", "15:45", "17:15"};

        try {
            InterfaceRPC servidor = ConexaoRPC.conectar(args);
            Random gerador = new Random();
            System.out.println("Iniciando povoamento RPC (1000 agendamentos sequenciais)...");
            long inicio = System.nanoTime();
            for (int i = 1; i <= quantidadeTestes; i++) {
                String cpf = cpfs[gerador.nextInt(cpfs.length)];
                int indexMedico = gerador.nextInt(medicos.length);
                String data = dias[gerador.nextInt(dias.length)] + " " + horas[gerador.nextInt(horas.length)];
                Consulta consulta = new Consulta("Paciente Teste " + i, cpf, "38900000000",
                        especialidades[indexMedico], medicos[indexMedico], crms[indexMedico], data);
                String resposta = servidor.agendarConsulta(DadosRPC.paraMapa(consulta));
                if (!resposta.startsWith("SUCESSO;")) {
                    throw new IllegalStateException("Agendamento recusado: " + resposta);
                }
                confirmados++;
            }
            double tempoMs = (System.nanoTime() - inicio) / 1_000_000.0;
            System.out.println("Povoamento RPC concluído: " + confirmados + " registros confirmados.");
            System.out.printf("Tempo total para inserir %d registros via RPC: %.3f ms.%n", confirmados, tempoMs);
        } catch (Exception e) {
            System.err.println("Erro no Povoamento RPC após " + confirmados + " confirmações: " + e.getMessage());
            System.exit(1);
        }
    }
}
