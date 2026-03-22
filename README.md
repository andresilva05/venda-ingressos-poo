# Sobre o Projeto
Sistema de venda de ingressos com interface gráfica Swing, com suporte a persistência por serialização de arquivos e simulação de concorrência com Multithreading para proteger as operações de compra.

## Tecnologias
- Java 17+
- IDE (NetBeans/IntelliJ/Eclipse)

## Estrutura de Pacotes
- `venda_ingresso.main`: Ponto de entrada — somente o método `main()`.
- `venda_ingresso.entities`: Entidade de domínio (`Ingresso.java`) — implements `Serializable`.
- `venda_ingresso.enums`: Enumeração de Setores (`SetorEnum.java`) com os dados dos ingressos.
- `venda_ingresso.exceptions`: Exceções personalizadas (`QuantidadeInvalidaException`, `SetorEsgotadoException`).
- `venda_ingresso.services`: Lógica de regras de negócio, serialização e tarefas assíncronas (`GerenciadorIngresso`, `GerenciadorArquivo`, `CompradorRunnable`).
- `venda_ingresso.ui`: Telas do sistema (Swing) sem regras de negócios (`TelaInicial`, `JanelaCadastroIngresso`, `JanelaGrafica`).
- `venda_ingresso.teste`: Classes de testes de integração como `TesteTxt`.

## Como Executar
1. Clone o repositório: `git clone https://github.com/andresilva05/venda-ingressos-poo.git`
2. Abra a pasta do projeto em sua IDE (IntelliJ, Eclipse, VSCode).
3. Execute a classe `venda_ingresso.main.Principal` para ver a simulação de multithreading ou as interfaces Swing.

## Conceitos Aplicados
- **Serialização**: Capacidade de salvar o estado de objetos Java em disco (ex: `ingressos.ser`) por intermédio das classes `ObjectOutputStream` e `ObjectInputStream`.
- **Multithreading**: Simula várias compras ao mesmo tempo através da interface `Runnable` e `Thread.sleep()`.
- **Sincronização (`synchronized`)**: Protege métodos críticos no `GerenciadorIngresso`, garantindo que duas threads não registrem a mesma compra e nem burlem o limite de lotação de um setor.
- **Palavra-chave `transient`**: Usada na variável `threadOrigem` para não ser salva no disco, retornando `null` após a desserialização.

## Branches
- `main`: Código-fonte final.
- `feature/pacotes`: Reorganização de todos os arquivos nos pacotes corretos (`venda_ingresso`).
- `feature/enums`: Criação do `SetorEnum` e refatoração da `JanelaCadastroIngresso` e `GerenciadorIngresso`.
- `feature/excecoes`: Correção de bugs de sintaxe em `CompradorRunnable` e uso dos tipos.
- `feature/serializacao`: Uso de métodos de serialização isolados em arquivos de serviços.
- `feature/threads`: Proteção `synchronized`, daemon de salvamento a cada 500ms, simuladores com `Runnable` e campo `transient`.

## Autor
André Silva (Turma - Preencher a critério do aluno)
