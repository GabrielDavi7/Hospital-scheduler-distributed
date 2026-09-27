package versao_rpc;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/** Stub: encapsula nomes dos procedimentos e transporte HTTP da biblioteca. */
public final class ConexaoRPC implements InterfaceRPC {
    private final XmlRpcClient cliente = new XmlRpcClient();

    public ConexaoRPC(String endereco) throws MalformedURLException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(URI.create(endereco).toURL());
        config.setConnectionTimeout(5000);
        config.setReplyTimeout(30000);
        config.setEncoding("UTF-8");
        cliente.setConfig(config);
    }

    public static ConexaoRPC conectar(String[] args) throws MalformedURLException {
        return new ConexaoRPC(args.length > 0 ? args[0] : "http://127.0.0.1:8080/RPC2");
    }

    private Object chamar(String metodo, Object... parametros) throws XmlRpcException {
        return cliente.execute(SERVICO + "." + metodo, parametros);
    }

    @Override
    public String agendarConsulta(Map<String, String> consulta) throws XmlRpcException {
        return (String) chamar("agendarConsulta", consulta);
    }

    @Override
    public String listarMedicos() throws XmlRpcException {
        return (String) chamar("listarMedicos");
    }

    @Override
    public Object[] verAgendamentos() throws XmlRpcException {
        return (Object[]) chamar("verAgendamentos");
    }

    @Override
    public Object[] buscarPorCpf(String cpf) throws XmlRpcException {
        return (Object[]) chamar("buscarPorCpf", cpf);
    }

    @Override
    public Object[] buscarPorCrm(String crm) throws XmlRpcException {
        return (Object[]) chamar("buscarPorCrm", crm);
    }
}
