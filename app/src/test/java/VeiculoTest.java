import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VeiculoTest {

    @Test
    void placaObrigatoriaLancaaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new Veiculo(null, "João"));
        assertThrows(IllegalArgumentException.class, () -> new Veiculo("   ", "Maria"));
    }

    @Test
    void veiculosComMesmaPlacaSaoIguais() {
        Veiculo v1 = new Veiculo("ABC-123", "Ana");
        Veiculo v2 = new Veiculo("ABC-123", "Bruno");
        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }
}
