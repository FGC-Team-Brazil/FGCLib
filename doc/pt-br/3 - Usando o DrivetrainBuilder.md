# Usando o DrivetrainBuilder

Nesta documentação ensinaremos como usar o DrivetrainBuilder, a classe que auxilia
na criação de um sistema de drivetrain tank. Para facilitar o entendimento, o Team Brazil gravou um vídeo, explicando
passo a passo todo o processo. Acesse o vídeo [aqui](https://www.youtube.com/watch?v=6j368G8O9kQ).

## Visão Geral

O `DrivetrainBuilder` automatiza a criação de uma base de locomoção diferencial padrão (tank ou arcade), evitando que você precise escrever código repetitivo para configuração de motores.

## Como utilizá-lo

1. **Defina as Constantes:** Configure os nomes dos motores e seus estados de inversão no arquivo `Constants.java`.
2. **Crie a Instância:** Dentro do seu `RobotContainer`, inicialize o drivetrain utilizando a chamada abaixo:

```java
DrivetrainBuilder.build(
    Constants.MOTOR_RIGHT, 
    Constants.MOTOR_LEFT, 
    Constants.MOTOR_RIGHT_INVERTED, 
    Constants.MOTOR_LEFT_INVERTED
);

```

3. **Associe os Controles:** Utilize os gatilhos (triggers) do `SmartGamepad` para conectar diretamente os eixos dos joysticks aos métodos de movimentação do builder, como `arcadeDrive(speed, turn)`.