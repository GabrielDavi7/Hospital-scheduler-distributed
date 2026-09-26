## 📌 Sobre o Projeto

Este projeto acadêmico tem como objetivo comparar o desempenho e a arquitetura de diferentes tecnologias de comunicação em Sistemas Distribuídos. A aplicação simula um sistema de agendamento hospitalar, implementado em Java, avaliando as vantagens e desvantagens de três abordagens de rede: **Sockets TCP puros**, **RMI (Remote Method Invocation)** e **RPC (Remote Procedure Call)**.

## ⚙️ Funcionalidades

A entidade central do sistema é o objeto `Consulta`. As funcionalidades incluem:

- Agendamento de novas consultas.
- Listagem de especialidades e médicos disponíveis.
- Busca e filtragem de agenda por **CPF do Paciente**.
- Busca e filtragem de agenda por **CRM do Médico**.
- Teste de carga (Povoamento) automatizado para medição de desempenho.

## 🏗️ Fases e Arquitetura

1. **Fase 1: Modelo de Dados** - Definição da classe `Consulta` padronizada.
2. **Fase 2: Comunicação via Sockets** - Troca de mensagens baseada em texto puro, exigindo parsing manual (uso de `.split(";")`) e controle direto das portas TCP.
3. **Fase 3: RMI (Remote Method Invocation)** - Comunicação orientada a objetos com tipagem forte (_Type Safety_), utilizando serialização e interfaces remotas nativas do Java.
4. **Fase 4: RPC (Remote Procedure Call)** - Implementação focada na abstração de procedimentos remotos, isolando a regra de negócio da infraestrutura de rede.

## 📊 Testes de Estresse e Desempenho

O projeto acompanha scripts `Povoar.java` que injetam **10.000 registros simultâneos** no servidor para cada arquitetura. As métricas avaliadas incluem:

- Tempo de execução total (em milissegundos) para inserção em lote.
- Tempo de resposta do servidor na filtragem de consultas por CPF/CRM.
- Comportamento do sistema operacional frente à exaustão de portas (_Port Exhaustion_ / `Address already in use`) durante sobrecarga de requisições Sockets.
- _Trade-off_ entre a leveza de tráfego de texto (Sockets) contra o custo computacional de serialização de objetos (RMI/RPC).
