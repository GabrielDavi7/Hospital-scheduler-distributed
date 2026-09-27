package versao_rpc;

import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;

/** Contrato XML-RPC: structs (Map), arrays e strings, sem serializacao Java. */
public interface InterfaceRPC {
    String SERVICO = "HospitalService";

    String agendarConsulta(Map<String, String> consulta) throws XmlRpcException;
    String listarMedicos() throws XmlRpcException;
    Object[] verAgendamentos() throws XmlRpcException;
    Object[] buscarPorCpf(String cpf) throws XmlRpcException;
    Object[] buscarPorCrm(String crm) throws XmlRpcException;
}
