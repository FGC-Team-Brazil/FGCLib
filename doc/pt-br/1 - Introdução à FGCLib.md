# Introdução à FGCLib

Seja bem-vindo a FGCLib, uma biblioteca para FGC e FTC que automatiza algumas tarefas
essências (boilerplate) para a programação do robô, além de trazer uma estrutura de código 
padronizada e de fácil entendimento.
Para preparar o seu ambiente de programação, veja este [vídeo](https://www.youtube.com/watch?v=_te1nUU-av4).

## Estrutura da Biblioteca
A estrutura padrão da Lib é muito parecida com qualquer código padrão da FGC, para acessar o código
basta navegar para ``TeamCode/src/main/java/org/firstinspires/ftc/teamcode``

**Terminal:**

        cd TeamCode/src/main/java/org/firstinspires/ftc/teamcode

Nesse diretório, você encontrará duas pastas:
1. **core**: diretório que contém as classes auxiliares da biblioteca, como PIDF e SmartGamePad, 
recomenda-se que nada na pasta seja alterado, pois como o nome sugere, faz parte do "core" da biblioteca;
2. **robot**: diretório contendo o código do robô em si, aqui será onde a equipe criará os sistemas do robô e seus opmodes.

### Pasta Robot
A biblioteca separá o código do robô em 3 partes, que são:

#### Subsystems
Aqui será onde todos os sistemas do robô serão criados, como tração, garras, elevadores, etc. Tudo que 
o robô é capaz de realizar será criado aqui, sendo que cada subsistema será uma classe diferente.
Todos os subsistemas implementam a interface Subsystem.

Depois da criação dos subsistemas (diretório subsystems), todos os sistemas criados deverão ser
listados na classe RobotSubsystems, que é responsável por executá-los. 

Veja a nossa documentação de
[Criando um Subsistema](2%20-%20Criando%20um%20Subsistema.md) para mais detalhes como criar os seus próprios subsistemas.

#### Constants
Nesta pasta estarão todas as constantes do código, ou seja, valores imutáveis os sistemas necessitarão.
Cada subsistema terá um arquivo de constantes, que por padrão deve ser nomeado assim:
``` js
NomeDoSubsistema + Constants // Ex: DrivetrainConstants
```

Além das constantes de cada subsistema, o pasta também conta com uma classe chamada GlobalConstants, que 
armazenará constantes globais, ou seja, que mais de um subsistema poderá usar.

#### OpModes
Por fim, todos os sistemas do robô serão executados nos modos operacionais do robô. O modo Teleoperado principal
deve ser criado em TeleOpMode.java, o restante dos modos operacionais será criado na parta opmodes.
Todos os modos operacionais estão documentados, acesse a classe e veja as documentações e como criar o 
seu próprio Opmode.

### Core
As funcionalidades da biblioteca são divididas em 2 partes:

#### Utils
Dentro da pasta utils temos classes utilitárias de usos diversos, feitas para automatizar algumas
funções que normalmente são usadas dentro do código. Atualmente a biblioteca conta com apenas uma classe:
 a MathUtils.java. Ela compreende funções matemáticas que auxiliam na produção do código.

### Lib
O diretório Lib contém as funcionalidades principais do site. As funcionalidades são divididas em:
1. **builders**: automatizam a criação de subsistemas, conta apenas com o DrivetrainBuilder ([[Veja como usar aqui](./3%20-%20Usando%20o%20DrivetrainBuilder.md);
2. **gamepad**: gerenciam o uso dos gamepads para controlar o robô ([Veja mais sobre aqui](./4%20-%20Classe%20utilitária%20SmartGamePad.md));
3. **interfaces**: interfaces diversas, atualmente contém apenas a interface Subsystem ([Veja como usar aqui](2%20-%20Criando%20um%20Subsistema.md));
4. **pid**: Funções relacionadas ao uso do controle PID ([Veja como usar aqui](5%20-%20Usando%20o%20Controlador%20PIDF.md)).