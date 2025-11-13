import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EstacionamentoTest {

    @Test
    void depositarESacarAtualizaSaldoCorretamente() {
        Estacionamento e = new Estacionamento(1, "Rua Teste");
        e.depositar(10.0);
        e.sacar(3.0);
        assertEquals(7.0, e.getSaldo(), 0.001);
    }

    @Test
    void sacarMaiorQueSaldoLancaaExcecao() {
        Estacionamento e = new Estacionamento(1, "Rua Teste");
        e.depositar(5.0);
        assertThrows(IllegalStateException.class, () -> e.sacar(10.0));
    }

    @Test
    void setHoraInicioFormatoInvalidoLancaaExcecao() {
        Estacionamento e = new Estacionamento(1, "Rua Teste");
        assertThrows(IllegalArgumentException.class, () -> e.setHoraInicio("7:05"));
    }
}
