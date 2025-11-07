public class Pagamento {
    private static final double VALOR_POR_30MIN = 0.50;
    private static final double[] MOEDAS_ACEITAS = {0.50, 1.00, 2.00};
    private double valorTotal;
    private int tempoMinutos;

    public Pagamento() {
        this.valorTotal = 0.0;
        this.tempoMinutos = 0;
    }

    public boolean inserirMoeda(double valorMoeda) {

        boolean moedaAceita = false;
        for (double moeda : MOEDAS_ACEITAS) {
            if (Math.abs(moeda - valorMoeda) < 0.01) {
                moedaAceita = true;
                break;
            }
        }

        if (!moedaAceita) {
            return false;
        }

        valorTotal += valorMoeda;

        calcularTempo();
        return true;
    }

    private void calcularTempo() {
  
        tempoMinutos = (int) (valorTotal / VALOR_POR_30MIN * 30);
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public int getTempoDisponivel() {
        return tempoMinutos;
    }

    public double[] getMoedasAceitas() {
        return MOEDAS_ACEITAS;
    }

    public double getTroco() {
 
        double valorNecessarioProximoIntervalo = Math.ceil(valorTotal / VALOR_POR_30MIN) * VALOR_POR_30MIN;
        return valorTotal - valorNecessarioProximoIntervalo >= 0 ? 
               valorTotal - valorNecessarioProximoIntervalo : 0;
    }

    @Override
    public String toString() {
        return String.format(
            "Valor inserido: R$%.2f%n" +
            "Tempo disponível: %d minutos%n" +
            "Troco: R$%.2f",
            valorTotal,
            tempoMinutos,
            getTroco()
        );
    }
}