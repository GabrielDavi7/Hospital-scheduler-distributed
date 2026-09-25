// Arquivo que implementa as regras da interface. Mantém a lista de agendamentos na memória, 
// processando os objetos Consulta que chegam prontos e devolvendo listas reais para as requisições.

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
        System.out.println("Recepção solicitou a agenda completa.");
        return agendamentos; // Retorna a lista direto pela rede!
    }

    @Override
    public List<Consulta> buscarPorCpf(String cpf) throws RemoteException {
        System.out.println("Recepção buscou CPF: " + cpf);
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : agendamentos) {
            if (c.getCpfPaciente().equals(cpf)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    @Override
    public List<Consulta> buscarPorCrm(String crm) throws RemoteException {
        System.out.println("Recepção buscou CRM: " + crm);
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : agendamentos) {
            if (c.getCrmMedico().equals(crm)) {
                resultado.add(c);
            }
        }
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