public class Veiculo {
    private final String placa;
    private String nomeDono;

    public Veiculo(String placa, String nomeDono) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("placa não pode ser vazia");
        }
        this.placa = placa.trim();
        this.nomeDono = nomeDono == null ? "" : nomeDono.trim();
    }

    public String getPlaca() {
        return placa;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono == null ? "" : nomeDono.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;
        return placa.equals(veiculo.placa);
    }

    @Override
    public int hashCode() {
        return placa.hashCode();
    }

    @Override
    public String toString() {
        return "Veiculo{placa='" + placa + "', nomeDono='" + nomeDono + "'}";
    }
}
