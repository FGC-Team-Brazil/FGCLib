# Criando um Subsistema

Nesta documentação você encontrará os passos necessários para criar o seu próprio subsistema usando 
as funcionalidades da FGCLib. Para facilitar o entendimento, o Team Brazil gravou um vídeo, explicando 
passo a passo todo o processo. O vídeo será lançado no Youtube do [Team Brazil](https://www.youtube.com/watch?v=-YRU4m8W1CU&list=PLnI4KYu0M96vkjjEv6ENr2LWzJCaoZvFi).

## Visão Geral

Um subsistema encapsula um mecanismo físico específico do seu robô (por exemplo: Drivetrain, Shooter ou Intake).

## Principais Etapas para Criar um Subsistema

1. **Implemente a Interface:** Sua classe deve implementar a interface Subsystem, que exige métodos como `initialize()`, `start()`, `execute()` e `stop()`.
2. **Utilize o Padrão Singleton:** Garanta que apenas uma instância do seu subsistema exista tornando o construtor `protected` e fornecendo um método `getInstance()`.
3. **Adicione o @AutoLog:** Adicione a anotação `@AutoLog` acima da definição da sua classe. Isso registra automaticamente suas variáveis públicas no FTC Dashboard e no KoalaLog, sem a necessidade de código adicional.
4. **Registre no RobotContainer:** Passe a instância do seu subsistema para a chamada `super()` dentro do construtor do `RobotContainer`, permitindo que o framework gerencie automaticamente seu ciclo de vida.