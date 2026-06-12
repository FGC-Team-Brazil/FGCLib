# Classe Utilitária SmartGamePad

Nesta documentação ensinaremos como usar o SmartGamePad, a classe que controla o acesso
aos gamepads/controles do robô. Para facilitar o entendimento, o Team Brazil gravou um vídeo, explicando
passo a passo todo o processo. O vídeo será lançado no Youtube do [Team Brazil](https://www.youtube.com/watch?v=-YRU4m8W1CU&list=PLnI4KYu0M96vkjjEv6ENr2LWzJCaoZvFi).

## Visão Geral

O `SmartGamePad` substitui a tradicional verificação de botões com estruturas `if/else` por um sistema moderno e reativo baseado em Triggers. Ele permite associar ações do robô diretamente aos estados dos botões, além de fornecer recursos avançados de feedback para o operador.

## Principais Recursos

* **Triggers Baseados em Eventos:** Associe ações a estados específicos utilizando métodos como `.onTrue()`, `.onFalse()`, `.whileTrue()`, e `.whileFalse()`.
* **Combinações Lógicas:** Combine entradas utilizando meétodos como `.and()`, `.or()`, e `.negate()`.
* **HID Controller:** Acesse recursos de vibração (rumble) e feedback por LEDs atráveis do método `getHID()`.

**Example:** 

```java
// Executa o intake enquanto o botão 'A' estiver pressionado
// e para quando ele for solto
driver.a().whileTrue(intake::run).onFalse(intake::stop);

// Vibra o controle por 500ms
driver.getHID().rumble(500);

```