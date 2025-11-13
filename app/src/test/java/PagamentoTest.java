import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PagamentoTest {

    @Test
    void inserirMoedaAceitaAtualizaValorETempo() {
        Pagamento p = new Pagamento();
        assertTrue(p.inserirMoeda(1.00));
        assertEquals(1.00, p.getValorTotal(), 0.001);
        // 1.00 corresponde a 60 minutos (1.00 / 0.50 * 30)
        assertEquals(60, p.getTempoDisponivel());
    }

    @Test
    void inserirMoedaRecusadaNaoAlteraEstado() {
        Pagamento p = new Pagamento();
        assertFalse(p.inserirMoeda(0.25));
        assertEquals(0.0, p.getValorTotal(), 0.001);
        assertEquals(0, p.getTempoDisponivel());
    }

    @Test
    void trocoRetornaZeroQuandoValorEhMultiploDeIntervalo() {
        Pagamento p = new Pagamento();
        // inserir 2.00 (aceita) gera múltiplo de 0.50 => troco esperado 0
        assertTrue(p.inserirMoeda(2.00));
        assertEquals(0.0, p.getTroco(), 0.001);
    }
}
