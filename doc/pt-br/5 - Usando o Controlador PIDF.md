## Usando um Controlador PIDF

Nesta documentação ensinaremos como usar a classe de PIDFController da Lib. 
Através dela você conseguirá criar os seus próprios PIDs para seus subsistemas.

## Tabela de Conteúdos
* O que é o controlador PID?
    - Proporcional
    - Integral
    - Derivativa
    - FeedFoward
* Classe PIDController
    - Funcionamento da classe
    - Criando o Objeto da classe
    - Métodos complementares

## O que é um controlador PID?
O controlador PID é um controlador de sistemas de feedback. Na róbotica, usamos o PID para controlar o funcionamento dos subsistemas do robô. O PID gera um output a partir de dois inputs:

* **Setpoint:** O objetivo final que você quer alcançar;
* **Reference:** O atual progresso/posição até o objetivo.

Um exemplo que podemos citar é um PID para tração. Vamos supor que sua equipe queira andar com o robô 3m (setpoint)
e seu robô está atualmente na posição 0m (reference). O PID fará o robô andar para frente, até que ele ande 3m.
O output do PID vai variar de acordo com a variação da posição do robô (reference), porém o setpoint sempre será o mesmo.

A equação do PID se da por 3 constantes: proporcional, integral e derivativa. Cada uma delas tem uma função distinta.

### A Matemática por Trás do PIDF

* **Proporcional ($k_P$):**
Varia o output do PID de forma proporcional ao reference. Exemplo: se o referencial é 2, o output proporcional será 0.5,
quando o referencial subir para 4, o output proporcional será 0.25 (indiretamente proporcional ao valor do referencial)

* **Integral ($k_I$):**
A constante integral age somando o erro do PID (diferença entre setpoint e reference) e multiplicando isso pela
constante. Mas por que? Bem, apenas a constante proporcional não tem a capacidade de levar o robô até o setpoint,
pense, se ela é proporcional ao referencial, quando o robô estiver bem perto do setpoint, a proporcional não será
quase 0? Isso faz o robô ficar imóvel.
Para resolver esse problema, a integral permite que o robô ande até com um erro muito pequeno, pois ela vai somando
ele com o decorrer do tempo, até que seja um valor significativo que faça o robô se mover.

* **Derivativa ($k_D$):**
A constante derivativa age prevendo a movimentação do robô, para evitar erros futuros. Ela é proporcional à
derivativa do erro. Diferente das outras constantes, o funcionamento dela é mais complexo.

* **FeedFoward ($k_F):**
Além do PID comum, a classe PIDController também possui uma constante FeedFoward simples. Ela é basicamente
um valor mínimo para o output, para evitar que o output seja menor que o atrito do sistema com o meio.


## Classe PIDController

A classe `PIDController` implementa o PID e o FeedForward em uma equação. Além disso, ela possui algumas funções
para controlar o funcionamento do PID, como um valor de tolerância para o setpoint.

### Funcionamento da Classe

````java
import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;

// Criando o controlador PIDF 
PIDController pidController = new PIDController(kP, kI, kD, kF);

````

### Controle de Posição vs. Controle de Velocidade

* **Controle de Posição**: Utilizado em braços, sides e movimentações até um ponto específico.
````java
double power = pid.calculate(targetPostion, motor.getCurrentPosition());
motor.setPower(power);

````

* **Controle de Velocidade**: Usado em Shooters e flywheels para manter uma velocidade constante (ticks por segundo).
```java
pid.runVelocity(shooterMotor, targetTicksPerSecond);

```



### Métodos complementares
Além do funcionamento padrão, o PID da Lib inclui funcionalidades desenvolvidas para aumentar a consistência dos mecanismos do robô:

| Recursos| Descrição | Examplo de Configuração |

| **Motion Profiling** | Gera uma trajetório trapezoidal suave, limitando aceleração e velocidade máximas para evitar impactos mecânicos em slides e braços. | `pid.enableMotionProfile(maxVel, maxAccel);` |
| **Compensação de Tensão** | Ajusta automaticamente a saída dos motores com base na tensão atual da bateria, mantendo o comportamento do mecanismo consistente durante toda a partida. | `pid.enableVoltageCompensation(hardwareMap);` |