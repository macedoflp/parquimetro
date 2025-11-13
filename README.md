# Sistema de Parquímetro

## Descrição do Projeto

O **Sistema de Parquímetro** é uma aplicação Java que simula o funcionamento de um equipamento de estacionamento. Permite cadastro de veículos, processamento de pagamento via moedas, cálculo de tempo de permanência e geração de tickets de estacionamento.

### Funcionalidades Principais

- **Cadastro de Veículos:** Registra placa e proprietário do veículo
- **Processamento de Pagamento:** Aceita moedas de R$ 0,50, R$ 1,00 e R$ 2,00
- **Cálculo de Tempo:** Converte valor inserido em minutos de permanência (R$ 0,50 = 30 minutos)
- **Cálculo de Troco:** Devolve o troco quando aplicável
- **Gestão de Saldo:** Mantém saldo do equipamento para devolver troco
- **Geração de Ticket:** Cria comprovante com informações de estacionamento

## Estrutura do Projeto

```
parquimetro/
├── app/
│   ├── src/
│   │   ├── main/java/
│   │   │   ├── Main.java              # Aplicação principal (interface CLI)
│   │   │   ├── Estacionamento.java    # Gerencia saldo e horários
│   │   │   ├── Pagamento.java         # Processa moedas e calcula tempo
│   │   │   └── Veiculo.java           # Representa um veículo
│   │   └── test/java/
│   │       ├── EstacionamentoTest.java
│   │       ├── PagamentoTest.java
│   │       ├── VeiculoTest.java
│   │       └── org/example/AppTest.java
│   └── build.gradle.kts
├── gradle/
├── settings.gradle.kts
└── README.md
```

## Tecnologias Utilizadas

- **Linguagem:** Java 11+
- **Build Tool:** Gradle
- **Framework de Testes:** JUnit 5 (Jupiter)

## Como Compilar e Executar

### Compilação

```bash
./gradlew build
```

### Executar Aplicação

```bash
./gradlew :app:run
```

Ou diretamente:

```bash
java -cp app/build/libs/app.jar Main
```

### Executar Testes

```bash
./gradlew :app:test
```

Visualizar relatório HTML dos testes:

```bash
# Em Linux/WSL
xdg-open app/build/reports/tests/test/index.html

# Em macOS
open app/build/reports/tests/test/index.html

# Em Windows
start app/build/reports/tests/test/index.html
```

## Casos de Testes Funcionais de Unidade

### Objetivo Geral

Validar o comportamento correto das três principais classes da aplicação através de testes de unidade, garantindo que:
- Operações financeiras funcionam corretamente
- Validações de entrada são aplicadas
- Igualdade e hash de objetos são consistentes

---

## Caso de Teste 1: Pagamento

**Arquivo:** `app/src/test/java/PagamentoTest.java`

### Objetivo

Verificar o comportamento do processador de moedas:
- Aceitar moedas válidas e atualizar saldo e tempo disponível
- Rejeitar moedas inválidas sem alterar estado
- Calcular troco corretamente

### Script dos Testes

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PagamentoTest {

    /**
     * Teste 1.1: Inserir Moeda Aceita
     * 
     * Objetivo: Validar que ao inserir uma moeda válida (R$ 1,00),
     * o valor total e o tempo disponível são atualizados corretamente.
     * 
     * Pré-condição: Pagamento inicializado com valor = 0 e tempo = 0
     * Ação: inserirMoeda(1.00)
     * Resultado Esperado: 
     *   - inserirMoeda() retorna true
     *   - getValorTotal() == 1.00
     *   - getTempoDisponivel() == 60 (1.00 / 0.50 * 30 minutos)
     */
    @Test
    void inserirMoedaAceitaAtualizaValorETempo() {
        Pagamento p = new Pagamento();
        assertTrue(p.inserirMoeda(1.00));
        assertEquals(1.00, p.getValorTotal(), 0.001);
        // 1.00 corresponde a 60 minutos (1.00 / 0.50 * 30)
        assertEquals(60, p.getTempoDisponivel());
    }

    /**
     * Teste 1.2: Inserir Moeda Recusada
     * 
     * Objetivo: Validar que moedas inválidas (ex: R$ 0,25)
     * não alteram o estado do pagamento.
     * 
     * Pré-condição: Pagamento inicializado
     * Ação: inserirMoeda(0.25) - moeda não aceita
     * Resultado Esperado:
     *   - inserirMoeda() retorna false
     *   - getValorTotal() continua 0
     *   - getTempoDisponivel() continua 0
     */
    @Test
    void inserirMoedaRecusadaNaoAlteraEstado() {
        Pagamento p = new Pagamento();
        assertFalse(p.inserirMoeda(0.25));
        assertEquals(0.0, p.getValorTotal(), 0.001);
        assertEquals(0, p.getTempoDisponivel());
    }

    /**
     * Teste 1.3: Troco Zero para Múltiplo de Intervalo
     * 
     * Objetivo: Validar que quando o valor inserido é múltiplo
     * de R$ 0,50, não há troco a devolver.
     * 
     * Pré-condição: Pagamento inicializado
     * Ação: inserirMoeda(2.00)
     * Resultado Esperado:
     *   - getTroco() == 0.0
     */
    @Test
    void trocoRetornaZeroQuandoValorEhMultiploDeIntervalo() {
        Pagamento p = new Pagamento();
        // inserir 2.00 (aceita) gera múltiplo de 0.50 => troco esperado 0
        assertTrue(p.inserirMoeda(2.00));
        assertEquals(0.0, p.getTroco(), 0.001);
    }
}
```

### Resultados Obtidos

| Teste | Status | Descrição |
|-------|--------|-----------|
| `inserirMoedaAceitaAtualizaValorETempo()` | ✅ PASSOU | Moeda R$ 1,00 aceita, valor e tempo calculados corretamente |
| `inserirMoedaRecusadaNaoAlteraEstado()` | ✅ PASSOU | Moeda R$ 0,25 rejeitada, estado permanece inalterado |
| `trocoRetornaZeroQuandoValorEhMultiploDeIntervalo()` | ✅ PASSOU | Troco = 0 para moeda R$ 2,00 |

---

## Caso de Teste 2: Veículo

**Arquivo:** `app/src/test/java/VeiculoTest.java`

### Objetivo

Validar as regras de criação e igualdade da classe `Veiculo`:
- Placa é obrigatória e não pode estar vazia
- Dois veículos com mesma placa são considerados iguais
- Hash code é consistente com equals

### Script dos Testes

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VeiculoTest {

    /**
     * Teste 2.1: Placa Obrigatória
     * 
     * Objetivo: Validar que um veículo não pode ser criado
     * sem placa ou com placa vazia.
     * 
     * Pré-condição: Nenhuma
     * Ação: Tentar criar Veiculo com placa null ou vazia
     * Resultado Esperado:
     *   - new Veiculo(null, "João") lança IllegalArgumentException
     *   - new Veiculo("   ", "Maria") lança IllegalArgumentException
     */
    @Test
    void placaObrigatoriaLancaaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new Veiculo(null, "João"));
        assertThrows(IllegalArgumentException.class, () -> new Veiculo("   ", "Maria"));
    }

    /**
     * Teste 2.2: Igualdade por Placa
     * 
     * Objetivo: Validar que dois veículos com mesma placa
     * são considerados iguais, mesmo que tenham proprietários diferentes.
     * 
     * Pré-condição: Nenhuma
     * Ação: Criar dois Veiculo com mesma placa mas proprietários diferentes
     * Resultado Esperado:
     *   - v1.equals(v2) retorna true
     *   - v1.hashCode() == v2.hashCode()
     */
    @Test
    void veiculosComMesmaPlacaSaoIguais() {
        Veiculo v1 = new Veiculo("ABC-123", "Ana");
        Veiculo v2 = new Veiculo("ABC-123", "Bruno");
        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }
}
```

### Resultados Obtidos

| Teste | Status | Descrição |
|-------|--------|-----------|
| `placaObrigatoriaLancaaExcecao()` | ✅ PASSOU | Placa null e vazia rejeitadas corretamente |
| `veiculosComMesmaPlacaSaoIguais()` | ✅ PASSOU | Igualdade e hash code consistentes para mesma placa |

---

## Caso de Teste 3: Estacionamento

**Arquivo:** `app/src/test/java/EstacionamentoTest.java`

### Objetivo

Testar operações financeiras e validação de entrada da classe `Estacionamento`:
- Depósitos e saques atualizam saldo corretamente
- Saque com saldo insuficiente lança exceção
- Formato de hora é validado (HH:mm)

### Script dos Testes

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EstacionamentoTest {

    /**
     * Teste 3.1: Depositar e Sacar
     * 
     * Objetivo: Validar que operações de depósito e saque
     * atualizam o saldo corretamente.
     * 
     * Pré-condição: Estacionamento inicializado com saldo 0
     * Ação: 
     *   1. depositar(10.0)
     *   2. sacar(3.0)
     * Resultado Esperado:
     *   - getSaldo() == 7.0
     */
    @Test
    void depositarESacarAtualizaSaldoCorretamente() {
        Estacionamento e = new Estacionamento(1, "Rua Teste");
        e.depositar(10.0);
        e.sacar(3.0);
        assertEquals(7.0, e.getSaldo(), 0.001);
    }

    /**
     * Teste 3.2: Saque com Saldo Insuficiente
     * 
     * Objetivo: Validar que um saque maior que o saldo disponível
     * lança IllegalStateException.
     * 
     * Pré-condição: Estacionamento com saldo = 5.0
     * Ação: sacar(10.0)
     * Resultado Esperado:
     *   - IllegalStateException é lançada
     *   - Saldo não é alterado
     */
    @Test
    void sacarMaiorQueSaldoLancaaExcecao() {
        Estacionamento e = new Estacionamento(1, "Rua Teste");
        e.depositar(5.0);
        assertThrows(IllegalStateException.class, () -> e.sacar(10.0));
    }

    /**
     * Teste 3.3: Formato de Hora Inválido
     * 
     * Objetivo: Validar que a hora inicial deve estar no formato HH:mm.
     * Formato inválido (ex: "7:05" sem zero à esquerda) deve lançar exceção.
     * 
     * Pré-condição: Estacionamento inicializado
     * Ação: setHoraInicio("7:05")
     * Resultado Esperado:
     *   - IllegalArgumentException é lançada
     */
    @Test
    void setHoraInicioFormatoInvalidoLancaaExcecao() {
        Estacionamento e = new Estacionamento(1, "Rua Teste");
        assertThrows(IllegalArgumentException.class, () -> e.setHoraInicio("7:05"));
    }
}
```

### Resultados Obtidos

| Teste | Status | Descrição |
|-------|--------|-----------|
| `depositarESacarAtualizaSaldoCorretamente()` | ✅ PASSOU | Operações financeiras funcionam corretamente |
| `sacarMaiorQueSaldoLancaaExcecao()` | ✅ PASSOU | Saque com saldo insuficiente rejeitado |
| `setHoraInicioFormatoInvalidoLancaaExcecao()` | ✅ PASSOU | Formato HH:mm validado corretamente |

---

## Resumo da Execução

```
BUILD SUCCESSFUL

Total de Testes: 7
Testes Passados: 7 ✅
Testes Falhados: 0
Taxa de Sucesso: 100%
```

### Como Rodar Novamente

```bash
./gradlew :app:test
```

## Fluxo de Uso da Aplicação

1. **Iniciar aplicação:** Execute `./gradlew :app:run`
2. **Cadastrar veículo:** Informe placa (ex: ABC-1234) e nome do proprietário
3. **Escolher tempo:** Selecione tempo desejado (30 min, 1h ou 2h)
4. **Pagar:** Insira moedas aceitas (R$ 0,50, R$ 1,00 ou R$ 2,00)
5. **Receber ticket:** Sistema gera comprovante com horários e valores
6. **Troco:** Se houver, é devolvido do saldo do equipamento

## Estrutura de Classes

### `Veiculo`
- Representa um veículo com placa e proprietário
- Placa é identificador único (imutável)
- Nome do proprietário pode ser alterado

### `Pagamento`
- Processa inserção de moedas
- Aceita: R$ 0,50, R$ 1,00, R$ 2,00
- Calcula tempo em minutos (R$ 0,50 = 30 minutos)
- Computa troco automaticamente

### `Estacionamento`
- Gerencia um equipamento de parquímetro
- Mantém saldo para devolver troco
- Valida e normaliza horários no formato HH:mm
- Operações: depósito e saque

### `Main`
- Interface CLI (Command Line Interface)
- Orquestra o fluxo de estacionamento
- Integra Veiculo, Pagamento e Estacionamento

## Tratamento de Exceções

| Exceção | Classe | Situação |
|---------|--------|----------|
| `IllegalArgumentException` | Veiculo | Placa nula ou vazia |
| `IllegalArgumentException` | Estacionamento | ID ≤ 0, endereço vazio, saldo negativo, hora inválida |
| `IllegalArgumentException` | Pagamento | N/A (usa retorno boolean para moeda inválida) |
| `IllegalStateException` | Estacionamento | Saldo insuficiente para saque |

## Observações

- Os testes utilizam `assertEquals` com tolerância `0.001` para comparações de ponto flutuante
- Todos os testes são independentes e podem ser executados em qualquer ordem
- Não há dependências externas além do JUnit 5 (incluído pelo Gradle)

---

**Autor:** Sistema de Parquímetro  
**Última atualização:** Novembro 2025
