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
4. **Fase 4: RPC (Remote Procedure Call)** - XML-RPC com Apache XML-RPC, usando procedimentos registrados como `HospitalService` e parâmetros/retornos XML sobre HTTP.

## Executar a versão RPC

Requer JDK 17 ou superior. No PowerShell, na raiz do projeto, baixe as bibliotecas (o script apenas prepara `lib/`):

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File scripts/compilar-rpc.ps1
```

Compile separadamente e inicie o servidor:

```powershell
javac -encoding UTF-8 -cp "lib/*" -d out src/modelo/Consulta.java src/versao_rpc/*.java
java -cp "out;lib/*" versao_rpc.ServidorRPC
```

Mantenha o servidor aberto. Em outro terminal, execute um dos programas:

```powershell
java -cp "out;lib/*" versao_rpc.ClienteRPC
java -cp "out;lib/*" versao_rpc.AdminRPC
java -cp "out;lib/*" versao_rpc.PovoarRPC
```

O servidor usa a porta 8080 e mantém a agenda em memória. Encerrar o servidor apaga os agendamentos. Para outra máquina, passe a URL como argumento ao cliente, à recepção ou ao povoamento, por exemplo: `java -cp "out;lib/*" versao_rpc.ClienteRPC http://192.168.1.10:8080/RPC2`.

`InterfaceRPC` define os métodos remotos e `ConexaoRPC` realiza as chamadas com Apache XML-RPC. A conversão entre `Consulta` e mapas é feita diretamente no cliente, servidor, recepção e povoamento, sem uma classe `DadosRPC` compartilhada.

O cliente exibe o tempo de resposta do agendamento; a recepção exibe o tempo de resposta da busca. Ambos medem apenas a chamada remota e seu retorno, sem digitação ou impressão. O script `compilar-rpc.ps1`, apesar do nome, apenas baixa as bibliotecas para `lib/`; a compilação é feita pelo comando `javac` acima.

## 📊 Testes de Estresse e Desempenho

O projeto acompanha os programas `PovoarSockets`, `PovoarRMI` e `PovoarRPC`, que inserem **1.000 registros sequencialmente** no servidor de cada arquitetura. As métricas de interesse incluem:

- Tempo de execução total (em milissegundos) para inserção em lote.
- Tempo de resposta do servidor na filtragem de consultas por CPF/CRM.
- Comportamento do sistema operacional frente à exaustão de portas (_Port Exhaustion_ / `Address already in use`) durante sobrecarga de requisições Sockets.
- Custo de codificação das mensagens: texto delimitado (Sockets), serialização Java (RMI) e XML (RPC).

Antes de comparar resultados, padronize os limites do cronômetro e os logs entre as versões: o painel RMI atual inclui digitação e impressão, enquanto o RPC mede apenas a chamada e seu retorno.
