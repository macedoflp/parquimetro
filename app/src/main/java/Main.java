import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final Estacionamento estacionamento = new Estacionamento(1, "Rua Principal");

    // Refatoração: Uso de Enum para eliminar Switch Case.

    private enum OpcaoTempo {
        MEIA_HORA(1, 30, "30 minutos (R$ 0,50)"),
        UMA_HORA(2, 60, "1 hora (R$ 1,00)"),
        DUAS_HORAS(3, 120, "2 horas (R$ 2,00)");

        private final int id;
        private final int minutos;
        private final String descricao;

        OpcaoTempo(int id, int minutos, String descricao) {
            this.id = id;
            this.minutos = minutos;
            this.descricao = descricao;
        }

        public int getMinutos() {
            return minutos;
        }

        // Método de busca que substitui o Switch Case
        public static OpcaoTempo buscarPorId(int id) {
            return Arrays.stream(values())
                    .filter(opcao -> opcao.id == id)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Opção inválida!"));
        }
    }

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("\n=== Sistema de Parquímetro ===");

                // 1. Cadastro
                System.out.print("Digite a placa do veículo (ou 'sair' para encerrar): ");
                String placa = scanner.nextLine().trim();

                if (placa.equalsIgnoreCase("sair")) {
                    break;
                }

                System.out.print("Digite o nome do proprietário: ");
                String nomeDono = scanner.nextLine().trim();
                Veiculo veiculo = new Veiculo(placa, nomeDono);

                // 2. Seleção de Tempo (Agora usando Enum)
                int tempoEscolhido = selecionarTempo();
                double valorNecessario = (tempoEscolhido / 30.0) * 0.50;

                // 3. Pagamento
                Pagamento pagamento = new Pagamento();
                processarPagamento(pagamento, valorNecessario);

                // 4. Troco e Saldo
                gerirTrocoESaldo(pagamento);

                // 5. Ticket
                imprimirTicket(veiculo, tempoEscolhido, valorNecessario);

            } catch (Exception e) {
                System.out.println("\nErro: " + e.getMessage());
                System.out.println("Por favor, tente novamente.");
            }
        }

        System.out.println("\nSaldo final do equipamento: R$ " + estacionamento.getSaldo());
        System.out.println("Sistema encerrado.");
        scanner.close();
    }

    //Refatoração Tática: Substituição de Condicional por Polimorfismo/Lookup

    private static int selecionarTempo() {
        System.out.println("\nSelecione o tempo de permanência:");
        
        // Loop dinâmico: Se adicionar nova opção no Enum, aparece aqui automaticamente
        for (OpcaoTempo opcao : OpcaoTempo.values()) {
            System.out.println(opcao.id + " - " + opcao.descricao);
        }

        try {
            int entrada = Integer.parseInt(scanner.nextLine());
            // A lógica de decisão foi movida para dentro do Enum
            return OpcaoTempo.buscarPorId(entrada).getMinutos();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("A opção deve ser um número válido!");
        }
    }

    private static void processarPagamento(Pagamento pagamento, double valorNecessario) {
        System.out.printf("\nValor a pagar: R$ %.2f\n", valorNecessario);
        System.out.println("Moedas aceitas: R$ 0,50 | R$ 1,00 | R$ 2,00");

        while (pagamento.getValorTotal() < valorNecessario) {
            System.out.printf("Falta: R$ %.2f\n", valorNecessario - pagamento.getValorTotal());
            System.out.print("Insira uma moeda (0.5, 1 ou 2): ");

            try {
                double moeda = Double.parseDouble(scanner.nextLine());
                if (!pagamento.inserirMoeda(moeda)) {
                    System.out.println("Moeda não aceita!");
                } else {
                    System.out.printf("Valor inserido até agora: R$ %.2f\n", pagamento.getValorTotal());
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número.");
            }
        }
    }

    private static void gerirTrocoESaldo(Pagamento pagamento) {
        double troco = pagamento.getTroco();
        if (troco > 0) {
            System.out.printf("\nTroco a devolver: R$ %.2f\n", troco);
            if (estacionamento.getSaldo() >= troco) {
                estacionamento.sacar(troco);
                System.out.println("Troco devolvido com sucesso!");
            } else {
                System.out.println("Desculpe, não há troco suficiente no equipamento!");
            }
        }
        estacionamento.depositar(pagamento.getValorTotal() - troco);
    }

    private static void imprimirTicket(Veiculo veiculo, int tempoEscolhido, double valorPago) {
        LocalTime horaAtual = LocalTime.now();
        LocalTime horaFim = horaAtual.plusMinutes(tempoEscolhido);

        System.out.println("\n=== Ticket de Estacionamento ===");
        System.out.println("Placa: " + veiculo.getPlaca());
        System.out.println("Proprietário: " + veiculo.getNomeDono());
        System.out.println("Hora início: " + horaAtual.format(TIME_FMT));
        System.out.println("Hora fim: " + horaFim.format(TIME_FMT));
        System.out.println("Tempo: " + tempoEscolhido + " minutos");
        System.out.println("Valor pago: R$ " + String.format("%.2f", valorPago));
        System.out.println("==============================");
    }
}