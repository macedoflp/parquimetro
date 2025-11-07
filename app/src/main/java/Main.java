import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final Estacionamento estacionamento = new Estacionamento(1, "Rua Principal");
    
    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("\n=== Sistema de Parquímetro ===");
                
                // Cadastro do veículo
                System.out.print("Digite a placa do veículo (ou 'sair' para encerrar): ");
                String placa = scanner.nextLine().trim();
                
                if (placa.equalsIgnoreCase("sair")) {
                    break;
                }
                
                System.out.print("Digite o nome do proprietário: ");
                String nomeDono = scanner.nextLine().trim();
                
                Veiculo veiculo = new Veiculo(placa, nomeDono);
                
                // Seleção do tempo
                System.out.println("\nSelecione o tempo de permanência:");
                System.out.println("1 - 30 minutos (R$ 0,50)");
                System.out.println("2 - 1 hora (R$ 1,00)");
                System.out.println("3 - 2 horas (R$ 2,00)");
                
                int opcao = Integer.parseInt(scanner.nextLine());
                int tempoEscolhido = switch (opcao) {
                    case 1 -> 30;
                    case 2 -> 60;
                    case 3 -> 120;
                    default -> throw new IllegalArgumentException("Opção inválida!");
                };
                
                double valorNecessario = (tempoEscolhido / 30.0) * 0.50;
                
                // Processo de pagamento
                Pagamento pagamento = new Pagamento();
                System.out.printf("\nValor a pagar: R$ %.2f\n", valorNecessario);
                System.out.println("Moedas aceitas: R$ 0,50 | R$ 1,00 | R$ 2,00");
                
                while (pagamento.getValorTotal() < valorNecessario) {
                    System.out.printf("Falta: R$ %.2f\n", valorNecessario - pagamento.getValorTotal());
                    System.out.print("Insira uma moeda (0.5, 1 ou 2): ");
                    double moeda = Double.parseDouble(scanner.nextLine());
                    
                    if (!pagamento.inserirMoeda(moeda)) {
                        System.out.println("Moeda não aceita!");
                        continue;
                    }
                    
                    System.out.printf("Valor inserido até agora: R$ %.2f\n", pagamento.getValorTotal());
                }
                
                // Cálculo do troco
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
                
                // Depósito do valor no estacionamento
                estacionamento.depositar(pagamento.getValorTotal() - troco);
                
                // Geração do ticket
                LocalTime horaAtual = LocalTime.now();
                LocalTime horaFim = horaAtual.plusMinutes(tempoEscolhido);
                
                System.out.println("\n=== Ticket de Estacionamento ===");
                System.out.println("Placa: " + veiculo.getPlaca());
                System.out.println("Proprietário: " + veiculo.getNomeDono());
                System.out.println("Hora início: " + horaAtual.format(TIME_FMT));
                System.out.println("Hora fim: " + horaFim.format(TIME_FMT));
                System.out.println("Tempo: " + tempoEscolhido + " minutos");
                System.out.println("Valor pago: R$ " + valorNecessario);
                System.out.println("==============================");
                
            } catch (Exception e) {
                System.out.println("\nErro: " + e.getMessage());
                System.out.println("Por favor, tente novamente.");
            }
        }
        
        System.out.println("\nSaldo final do equipamento: R$ " + estacionamento.getSaldo());
        System.out.println("Sistema encerrado.");
        scanner.close();
    }
}
