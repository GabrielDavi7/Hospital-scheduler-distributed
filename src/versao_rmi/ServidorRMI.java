package versao_rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import modelo.Consulta;

public class ServidorRMI extends UnicastRemoteObject implements InterfaceRMI {
    
    private List<Consulta> agendamentos;
    private int contadorId = 1;

    private static final String LISTA_MEDICOS = 
            "1. Cardiologia - Dr. Joao Silva (CRM: 12345) | " +
            "2. Ortopedia - Dra. Maria Souza (CRM: 54321) | " +
            "3. Pediatria - Dra. Ana Lima (CRM: 98765)";

    protected ServidorRMI() throws RemoteException {
        super();
        agendamentos = new ArrayList<>();
    }

    @Override
    public String listarMedicos() throws RemoteException {
        return LISTA_MEDICOS;
    }

    @Override
    public String agendarConsulta(Consulta consulta) throws RemoteException {
        consulta.setId("AG-RMI-" + contadorId++);
        agendamentos.add(consulta);
        System.out.println("Novo agendamento recebido (RMI): " + consulta.getNomePaciente());
        return "SUCESSO;Consulta agendada com ID: " + consulta.getId();
    }

    @Override
    public List<Consulta> verAgendamentos() throws RemoteException {
        long inicio = System.nanoTime();
        List<Consulta> resultado = new ArrayList<>(agendamentos);
        long tempoBuscaNs = System.nanoTime() - inicio;
        System.out.printf("[RMI servidor] Agenda completa | registros: %d | retornados: %d | processamento: %.3f ms.%n",
                agendamentos.size(), resultado.size(), tempoBuscaNs / 1_000_000.0);
        return resultado;
    }

    @Override
    public List<Consulta> buscarPorCpf(String cpf) throws RemoteException {
        long inicio = System.nanoTime();
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : agendamentos) {
            if (c.getCpfPaciente().equals(cpf)) {
                resultado.add(c);
            }
        }
        long tempoBuscaNs = System.nanoTime() - inicio;
        System.out.printf("[RMI servidor] CPF: %s | registros: %d | retornados: %d | processamento: %.3f ms.%n",
                cpf, agendamentos.size(), resultado.size(), tempoBuscaNs / 1_000_000.0);
        return resultado;
    }

    @Override
    public List<Consulta> buscarPorCrm(String crm) throws RemoteException {
        long inicio = System.nanoTime();
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : agendamentos) {
            if (c.getCrmMedico().equals(crm)) {
                resultado.add(c);
            }
        }
        long tempoBuscaNs = System.nanoTime() - inicio;
        System.out.printf("[RMI servidor] CRM: %s | registros: %d | retornados: %d | processamento: %.3f ms.%n",
                crm, agendamentos.size(), resultado.size(), tempoBuscaNs / 1_000_000.0);
        return resultado;
    }

    public static void main(String[] args) {
        try {
            ServidorRMI servidor = new ServidorRMI();
            
            Registry registry = LocateRegistry.createRegistry(1099);
            
            registry.rebind("HospitalService", servidor);
            
            System.out.println("🏥 Servidor Hospitalar (RMI) rodando na porta 1099");
        } catch (Exception e) {
            System.out.println("Erro no Servidor RMI: " + e.getMessage());
        }
    }
}