# Usando um Controlador PIDF

Nesta documentação ensinaremos como usar a classe de PIDFController da Lib. 
Através dela você conseguirá criar os seus próprios PIDs para seus subsistemas.

# Tabela de Conteúdos
- O que é o controlador PID?
    - Proporcional
    - Integral
    - Derivativa
    - FeedFoward
- Classe PIDController
    - Funcionamento da classe
    - Criando o Objeto da classe
    - Métodos complementares

# O que é um controlador PID?
O controlador PID é um controlador de sistemas de feedback. Na róbotica, usamos o PID para
controlar o funcionamento dos subsistemas do robô. O PID gera um output a partir de dois inputs:
- Setpoint: O objetivo final que você quer alcançar;
- Reference: O atual progresso/posição até o objetivo.
Um exemplo que podemos citar é um PID para tração. Vamos supor que sua equipe queira andar com o robô 3m (setpoint)
e seu robô está atualmente na posição 0m (reference). O PID fará o robô andar para frente, até que ele ande 3m.
O output do PID vai variar de acordo com a variação da posição do robô (reference), porém o setpoint sempre será o mesmo.

A equação do PID se da por 3 constantes: proporcional, integral e derivativa. Cada uma delas tem uma função distinta.

## Proporcional
Varia o output do PID de forma proporcional ao reference. Exemplo: se o referencial é 2, o output proporcional será 0.5,
quando o referencial subir para 4, o output proporcional será 0.25 (indiretamente proporcional ao valor do referencial)

## Integral
A constante integral age somando o erro do PID (diferença entre setpoint e reference) e multiplicando isso pela
constante. Mas por que? Bem, apenas a constante proporcional não tem a capacidade de levar o robô até o setpoint,
pense, se ela é proporcional ao referencial, quando o robô estiver bem perto do setpoint, a proporcional não será
quase 0? Isso faz o robô ficar imóvel

Para resolver esse problema, a integral permite que o robô ande até com um erro muito pequeno, pois ela vai somando
ele com o decorrer do tempo, até que seja um valor significativo que faça o robô se mover.

## Derivativa
A constante derivativa age prevendo a movimentação do robô, para evitar erros futuros. Ela é proporcional à
derivativa do erro. Diferente das outras constantes, o funcionamento dela é mais complexo.

## FeedFoward
Além do PID comum, a classe PIDController também possui uma constante FeedFoward simples. Ela é basicamente
um valor mínimo para o output, para evitar que o output seja menor que o atrito do sistema com o meio.

# Classe PIDController
A classe PIDController implementa o PID e o FeedForward em uma equação. Além disso, ela possui algumas funções
para controlar o funcionamento do PID, como um valor de tolerância para o setpoint.

## Funcionamento da Classe
#### Importação
````java
import org.firstinspires.ftc.teamcode.core.lib.pid.PIDController;
````
#### Criação do Objeto
````java
PIDController pidController = new PIDController(kP, kI, kD, kF);
````
#### Settando o Setpoint
````java
pidController.setSetpoint(setPoint);
````
### Usando a classe
O método calculate() retorno o output do PID, então coloque ele como parâmetro de uma função setPower por exemplo:
````java
setPower(pidController.calculate(reference));
````
## Métodos complementares
Além do funcionamento padrão, a classe possui alguns métodos complementares, como métodos de Set e Get,
um método para definir a tolerância da posição, entre outros. Explore a Lib para ver todos os métodos.