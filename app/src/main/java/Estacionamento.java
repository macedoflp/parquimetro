import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Estacionamento {
    private final int id;
    private String endereco;
    private String horaInicio;
    private String horaFim;
    private double saldo;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public Estacionamento(int id, String endereco, String horaInicio, String horaFim, double saldo) {
        if (id <= 0) throw new IllegalArgumentException("id deve ser maior que zero");
        this.id = id;
        setEndereco(endereco);
        setHoraInicio(horaInicio);
        setHoraFim(horaFim);
        if (saldo < 0) throw new IllegalArgumentException("saldo não pode ser negativo");
        this.saldo = saldo;
    }

  
    public Estacionamento(int id, String endereco) {
        this(id, endereco, "00:00", "23:59", 0.0);
    }

    public int getId() {
        return id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new IllegalArgumentException("endereco não pode ser vazio");
        }
        this.endereco = endereco.trim();
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = normalizeTime(horaInicio);
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = normalizeTime(horaFim);
    }

    public double getSaldo() {
        return saldo;
    }


    public void depositar(double valor) {
        if (valor <= 0) throw new IllegalArgumentException("valor do depósito deve ser positivo");
        this.saldo += valor;
    }


    public void sacar(double valor) {
        if (valor <= 0) throw new IllegalArgumentException("valor do saque deve ser positivo");
        if (valor > this.saldo) throw new IllegalStateException("saldo insuficiente");
        this.saldo -= valor;
    }

    private static String normalizeTime(String hora) {
        if (hora == null) throw new IllegalArgumentException("hora não pode ser nula");
        String trimmed = hora.trim();
        try {
            LocalTime t = LocalTime.parse(trimmed, TIME_FMT);
            return t.format(TIME_FMT);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("hora deve estar no formato HH:mm: " + hora, ex);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estacionamento that = (Estacionamento) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Estacionamento{id=" + id + ", endereco='" + endereco + "', horaInicio='" + horaInicio + "', horaFim='" + horaFim + "', saldo=" + saldo + "}";
    }
}
