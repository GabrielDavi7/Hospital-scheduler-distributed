// Esse arquivo serve como modelo de entradas dos dados no sistema, eles sao essa estrutura ai. 

package modelo;

import java.io.Serializable;

public class Consulta implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String id;
    private String nomePaciente;
    private String cpfPaciente;
    private String telefonePaciente;
    private String especialidade;
    private String nomeMedico;
    private String crmMedico;
    private String dataHora;

    public Consulta(String nomePaciente, String cpfPaciente, String telefonePaciente, String especialidade, String nomeMedico, String crmMedico, String dataHora) {
        this.nomePaciente = nomePaciente;
        this.cpfPaciente = cpfPaciente;
        this.telefonePaciente = telefonePaciente;
        this.especialidade = especialidade;
        this.nomeMedico = nomeMedico;
        this.crmMedico = crmMedico;
        this.dataHora = dataHora;
        this.id = ""; 
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }

    public String getCpfPaciente() { return cpfPaciente; }
    public void setCpfPaciente(String cpfPaciente) { this.cpfPaciente = cpfPaciente; }

    public String getTelefonePaciente() { return telefonePaciente; }
    public void setTelefonePaciente(String telefonePaciente) { this.telefonePaciente = telefonePaciente; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getNomeMedico() { return nomeMedico; }
    public void setNomeMedico(String nomeMedico) { this.nomeMedico = nomeMedico; }

    public String getCrmMedico() { return crmMedico; }
    public void setCrmMedico(String crmMedico) { this.crmMedico = crmMedico; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    @Override
    public String toString() {
        return "Consulta [ID: " + (id.isEmpty() ? "Pendente" : id) + "] -> Paciente: " + nomePaciente + 
               " | CPF: " + cpfPaciente + " | Telefone: " + telefonePaciente + " | Especialidade: " + especialidade + " | Médico: " + nomeMedico + " | CRM: " + crmMedico + " | Data/Hora: " + dataHora;
    }
}