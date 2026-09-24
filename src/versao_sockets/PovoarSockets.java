// esse arquivo é usado para povoar o servidor com agendamentos automáticos via Sockets, útil para testes de desempenho e carga assim nao precisaremos 
// digitar no terminal 100 agendamentos manualmente. Ele envia 100 agendamentos automáticos para o servidor e mede o tempo total gasto.

package versao_sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PovoarSockets {

    public static void main(String[] args) {
        String ipServidor = "127.0.0.1";
        int porta = 5000;
        
        int quantidadeTestes = 1000; 

        System.out.println("⏳ Iniciando o povoamento automático de " + quantidadeTestes + " agendamentos...");
        
        long tempoInicio = System.currentTimeMillis();

        for (int i = 1; i <= quantidadeTestes; i++) {
            try {
                Socket socket = new Socket(ipServidor, porta);
                PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String comandoAgendar = "AGENDAR;Paciente Teste " + i + ";111.111.111-11;38900000000;Cardiologia;Dr. Automático;99999;10/10/2026 10:00";
                
                saida.println(comandoAgendar);
                entrada.readLine(); 

                socket.close();
            } catch (Exception e) {
                System.out.println("Erro na inserção " + i + ": " + e.getMessage());
            }
        }

        long tempoFim = System.currentTimeMillis();
        long tempoTotal = tempoFim - tempoInicio;

        System.out.println("✅ Povoamento concluído com sucesso!");
        System.out.println("📊 Tempo total para inserir " + quantidadeTestes + " registros via Sockets: " + tempoTotal + " milissegundos.");
    }
}