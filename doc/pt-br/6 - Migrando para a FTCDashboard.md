# Migrando para a FTCDashboard

Nesta documentação, ensinaremos como registrar dados (logging) utilizando a FGCLib. 
Para facilitar o entedimento, o Team Brazil gravou um vídeo explicando todo o processo passo a passo.
O vídeo será disponibilizado em [Team Brazil YouTube]().

## Visão Geral

O registro de dados (logging) permite visualizar em tempo real o que o seu robô está fazendo e processando, sendo uma ferramenta essencial para deduração (debugging) e ajuste (tunning) de mecanismos.

### A anotação `@AutoLog` (Recomendado)

A maneira mais simples de registrar dados de um subsistema é adicionando a anotação `@Autolog` à sua classe.

Qualquer campo `public` presente em uma classe anotada com `@Autolog` será automaticamente publicado no FTC Dashboard e no KoalaLog a cada ciclo de execução do robô. **Nenhuma chamada manual de logging é necessária!**

### Logging Manual

Para cálculos específicos, valores temporários ou ajustes durante o desenvolvimento, você também pode utilizar o KoalaLog diretamente:

```java
Koala.log("SystemName/VariableName", value, true);

```