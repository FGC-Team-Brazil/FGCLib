# Medindo um Periódico (PeriodicTimer)

Em alguns casos, é necessário saber exatamente quanto tempo uma determinada parte do código leva para ser executada, garantindo que o robô não esteja sofrendo atrasos ou quedas de desempenho.
O Team Brazil gravou um vídeo explicando todo o processo passo a passo!
O vídeo está disponível em [Team Brazil YouTube]().

## Visão Geral

A classe utilitária `PeriodicTimer` permite monitoraro tempo de execução de tarefas periódicas. Ela mede quantos miilissegundos um processo leva para ser executado e registra automaticamente as estatísticas (Últmio Tempo, Tempo Médio, Tempo Máximo e Overruns) diretamente no **KoalaLog**.

### Como Utilizar o PeriodicTimer?

Para medir um bloco de código, basta envolvê-lo com os métodos `start()` e `stop()`, fornecendo um nome único para o temporizador.

```java
import org.firstinspires.ftc.teamcode.core.util.PeriodicTimer;

// Inicia o temporizador antes da lógica mais complexa
PeriodicTimer.start("VisionProcessing");

// ... seu código mais pesado aqui ...

// Finaliza o temporizador ao término da execução
PeriodicTImer.stop("VisionProcessing");

```

### O que é Registrado?

Ao chamar o método `stop()`, a classe calcula o tempo decorrido e atualiza automaticamente o KoalaLog com as seguintes métricas:

* `Timers/VisionProcessing/LastMs`: Tempo gasto na última execução do loop.
* `Timers/VisionProcessing/AvgMs`: Tempo médio de execução considerando todos os loops.
* `Timers/VisionProcessing/MaxMs`: Maior tempo de execução registrado.
* `Timers/VisionProcessing/Overruns`: Quantidade de vezes que o loop ultrapassou o limite de **20.0ms**.