# Migrando para a FTCDashboard

Nesta documentação ensinaremos como usar a FtcDashboard no lugar do Telemetry padrão do SDK. 
Para facilitar o entendimento, o Team Brazil produziu um exemplo de subsistema que usa a FTCDashboard.
Para acessar o exemplo navegue até a branch **_new/ftcDashboard_**.

## Usando o MultipleTelemetry
Quando se usa o Telemetry padrão com a FGCLib, o objeto Telemetry é passado para os 
subsistemas através da classe Robot:
````java
// Dentro da Classe Robot.java

// ...
public void init(@NonNull HardwareMap hardwareMap, @NonNull **Telemetry telemetry**) {
        this.telemetry = telemetry;
        this.subsystems = RobotSubsystems.get();

        subsystems.forEach(subsystem -> subsystem.initialize(hardwareMap, **telemetry**));

        telemetry.update();
    }
// ...
````

Porém para usarmos a FtcDashboard, vamos usar o Telemetry próprio da biblioteca. Para usá-lo, no seu
subsistema, no método initialize(), ao invés de passar para a variável telemetry o parâmetro de 
telemetry do método initialize(), use a classe MultipleTelemetry da FtcDashboard:
### Exemplo Telemetry Padrão
````java
public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
    // ...
    
    this.telemetry = telemetry;
    
    // ...
    telemetry.addData(...);
````

### Exemplo MultipleTelemetry FtcDashboard 
````java
public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
    // ...

    telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    
    // ...
    telemetry.addData(...);
````

## Aprendendo a usar a FtcDashboard
Caso você não saiba como funciona a FtcDashboard, veja a documentação da própria biblioteca.
Ela é bem completa, detalhando o funcionamento das classes usadas e conta com exemplos
de uso. Veja a [documentação aqui](https://acmerobotics.github.io/ftc-dashboard/).
Acesse o Github da biblioteca [aqui](https://github.com/acmerobotics/ftc-dashboard).