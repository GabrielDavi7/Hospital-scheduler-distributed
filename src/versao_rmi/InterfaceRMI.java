//Esse arquivo faz a definicao dos metodos que serao implementados no ServidorRMI, e que serao acessados pelo ClienteRMI.

package versao_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import modelo.Consulta;

public interface InterfaceRMI extends Remote {
    
    String agendarConsulta(Consulta consulta) throws RemoteException;
    
    String listarMedicos() throws RemoteException;
    
    List<Consulta> verAgendamentos() throws RemoteException;
    
    List<Consulta> buscarPorCpf(String cpf) throws RemoteException;
    
    List<Consulta> buscarPorCrm(String crm) throws RemoteException;
}