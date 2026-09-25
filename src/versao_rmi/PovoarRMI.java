//Povoamente do RMI mesmo objetivo do povoamento do socket

package versao_rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import modelo.Consulta;

public class PovoarRMI {
    public static void main(String[] args) {
        int quantidadeTestes = 1000; 
        System.out.println("⏳ Iniciando povoamento automático via RMI (" + quantidadeTestes + " agendamentos)...");
        
        String[] cpfs = {"111.111.111-11", "222.222.222-22", "333.333.333-33", "444.444.444-44", "555.555.555-55"};
        String[] especialidades = {"Cardiologia", "Ortopedia", "Pediatria"};
        String[] medicos = {"Dr. Joao Silva", "Dra. Maria Souza", "Dra. Ana Lima"};
        String[] crms = {"12345", "54321", "98765"};
        String[] dias = {"10/10/2026", "11/10/2026", "12/10/2026", "13/10/2026", "14/10/2026"};
        String[] horas = {"09:00", "10:30", "14:00", "15:45", "17:15"};

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            InterfaceRMI servidor = (InterfaceRMI) registry.lookup("HospitalService");
            
            Random gerador = new Random();
            long tempoInicio = System.currentTimeMillis();

            for (int i = 1; i <= quantidadeTestes; i++) {
                String cpfSorteado = cpfs[gerador.nextInt(cpfs.length)];
                int indexMedico = gerador.nextInt(medicos.length);
                String especialidadeSorteada = especialidades[indexMedico];
                String medicoSorteado = medicos[indexMedico];
                String crmSorteado = crms[indexMedico];
                String dataSorteada = dias[gerador.nextInt(dias.length)] + " " + horas[gerador.nextInt(horas.length)];

                Consulta novaConsulta = new Consulta(
                    "Paciente Teste " + i, cpfSorteado, "38900000000", 
                    especialidadeSorteada, medicoSorteado, crmSorteado, dataSorteada
                );
                
                servidor.agendarConsulta(novaConsulta);
            }

            long tempoTotal = System.currentTimeMillis() - tempoInicio;
            System.out.println("✅ Povoamento RMI concluído!");
            System.out.println("📊 Tempo total para inserir " + quantidadeTestes + " registros via RMI: " + tempoTotal + " ms.");

        } catch (Exception e) {
            System.out.println("Erro no Povoamento RMI: " + e.getMessage());
        }
    }
}