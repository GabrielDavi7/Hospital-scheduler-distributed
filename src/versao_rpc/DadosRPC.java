package versao_rpc;

import java.util.LinkedHashMap;
import java.util.Map;
import modelo.Consulta;

/** Converte o modelo comum para os tipos padrao aceitos pelo XML-RPC. */
public final class DadosRPC {
    private DadosRPC() { }

    public static Map<String, String> paraMapa(Consulta consulta) {
        Map<String, String> dados = new LinkedHashMap<>();
        dados.put("id", consulta.getId());
        dados.put("nomePaciente", consulta.getNomePaciente());
        dados.put("cpfPaciente", consulta.getCpfPaciente());
        dados.put("telefonePaciente", consulta.getTelefonePaciente());
        dados.put("especialidade", consulta.getEspecialidade());
        dados.put("nomeMedico", consulta.getNomeMedico());
        dados.put("crmMedico", consulta.getCrmMedico());
        dados.put("dataHora", consulta.getDataHora());
        return dados;
    }

    public static Consulta paraConsulta(Map<?, ?> dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Consulta obrigatoria.");
        }
        Consulta consulta = new Consulta(
                campo(dados, "nomePaciente"), campo(dados, "cpfPaciente"),
                campo(dados, "telefonePaciente"), campo(dados, "especialidade"),
                campo(dados, "nomeMedico"), campo(dados, "crmMedico"),
                campo(dados, "dataHora"));
        // O servidor sempre substitui o ID recebido ao agendar.
        if (dados.get("id") instanceof String) {
            consulta.setId((String) dados.get("id"));
        }
        return consulta;
    }

    private static String campo(Map<?, ?> dados, String nome) {
        Object valor = dados.get(nome);
        if (!(valor instanceof String) || ((String) valor).trim().isEmpty()) {
            throw new IllegalArgumentException("Campo obrigatorio: " + nome);
        }
        return (String) valor;
    }
}
