package versao_rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import modelo.Consulta;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.webserver.WebServer;

/** Servico de agenda em memoria, registrado como HospitalService. */
public class ServidorRPC implements InterfaceRPC {
    private final List<Consulta> agendamentos = new ArrayList<>();
    private int contadorId = 1;

    @Override
    public String listarMedicos() {
        return "1. Cardiologia - Dr. Joao Silva (CRM: 12345) | "
                + "2. Ortopedia - Dra. Maria Souza (CRM: 54321) | "
                + "3. Pediatria - Dra. Ana Lima (CRM: 98765)";
    }

    @Override
    public synchronized String agendarConsulta(Map<String, String> dados) {
        Consulta consulta = DadosRPC.paraConsulta(dados);
        consulta.setId("AG-RPC-" + contadorId++);
        agendamentos.add(consulta);
        return "SUCESSO;Consulta agendada com ID: " + consulta.getId();
    }

    @Override
    public synchronized Object[] verAgendamentos() {
        return agendamentos.stream().map(DadosRPC::paraMapa).toArray();
    }

    @Override
    public synchronized Object[] buscarPorCpf(String cpf) {
        return agendamentos.stream().filter(c -> c.getCpfPaciente().equals(cpf))
                .map(DadosRPC::paraMapa).toArray();
    }

    @Override
    public synchronized Object[] buscarPorCrm(String crm) {
        return agendamentos.stream().filter(c -> c.getCrmMedico().equals(crm))
                .map(DadosRPC::paraMapa).toArray();
    }

    public static void main(String[] args) {
        WebServer web = null;
        try {
            int porta = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
            if (porta < 1 || porta > 65535) {
                throw new IllegalArgumentException("Porta deve estar entre 1 e 65535.");
            }
            ServidorRPC servico = new ServidorRPC();
            PropertyHandlerMapping registro = new PropertyHandlerMapping();
            // A biblioteca cria handlers por chamada por padrao. Reutilizamos a
            // mesma instancia para que todos os clientes compartilhem a agenda.
            registro.setRequestProcessorFactoryFactory(tipo -> requisicao -> servico);
            registro.addHandler(SERVICO, ServidorRPC.class);
            web = new WebServer(porta);
            web.getXmlRpcServer().setHandlerMapping(registro);
            web.start();
            WebServer servidorAtivo = web;
            Runtime.getRuntime().addShutdownHook(new Thread(servidorAtivo::shutdown));
            System.out.println("Servidor Hospitalar (XML-RPC) rodando na porta " + porta);
            System.out.println("Endpoint: http://127.0.0.1:" + porta + "/RPC2");
        } catch (Exception e) {
            if (web != null) { web.shutdown(); }
            System.err.println("Erro no Servidor RPC: " + e.getMessage());
            System.exit(1);
        }
    }
}
