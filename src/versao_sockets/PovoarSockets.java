// esse arquivo é usado para povoar o servidor com agendamentos automáticos via Sockets, útil para testes de desempenho e carga assim nao precisaremos 
// digitar no terminal 100 agendamentos manualmente. Ele envia 100 agendamentos automáticos para o servidor e mede o tempo total gasto.

package versao_sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class PovoarSockets {

    public static void main(String[] args) {
        String ipServidor = "127.0.0.1";
        int porta = 5000;
        int quantidadeTestes = 1000; 

        System.out.println("⏳ Iniciando o povoamento automático com dados variados (" + quantidadeTestes + " agendamentos)...");
        
        String[] cpfs = {"111.111.111-11", "222.222.222-22", "333.333.333-33", "444.444.444-44", "555.555.555-55"};
        
        String[] especialidades = {"Cardiologia", "Ortopedia", "Pediatria"};
        String[] medicos = {"Dr. Joao Silva", "Dra. Maria Souza", "Dra. Ana Lima"};
        String[] crms = {"12345", "54321", "98765"};
        
        String[] dias = {"10/10/2026", "11/10/2026", "12/10/2026", "13/10/2026", "14/10/2026"};
        String[] horas = {"09:00", "10:30", "14:00", "15:45", "17:15"};

        Random gerador = new Random();
        long tempoInicio = System.currentTimeMillis();

        for (int i = 1; i <= quantidadeTestes; i++) {
            try {
                Socket socket = new Socket(ipServidor, porta);
                PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String cpfSorteado = cpfs[gerador.nextInt(cpfs.length)];
                
                int indexMedico = gerador.nextInt(medicos.length);
                String especialidadeSorteada = especialidades[indexMedico];
                String medicoSorteado = medicos[indexMedico];
                String crmSorteado = crms[indexMedico];
                
                String dataSorteada = dias[gerador.nextInt(dias.length)] + " " + horas[gerador.nextInt(horas.length)];

                String comandoAgendar = "AGENDAR;Paciente Teste " + i + ";" + cpfSorteado + ";38900000000;" 
                                      + especialidadeSorteada + ";" + medicoSorteado + ";" + crmSorteado + ";" + dataSorteada;
                
                saida.println(comandoAgendar);
                entrada.readLine(); 

                socket.close();
            } catch (Exception e) {
                System.out.println("Erro na inserção " + i + ": " + e.getMessage());
            }
        }

        long tempoFim = System.currentTimeMillis();
        long tempoTotal = tempoFim - tempoInicio;

        System.out.println("✅ Povoamento inteligente concluído com sucesso!");
        System.out.println("📊 Tempo total para inserir " + quantidadeTestes + " registros misturados via Sockets: " + tempoTotal + " ms.");
    }
}