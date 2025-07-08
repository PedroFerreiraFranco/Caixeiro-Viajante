import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TSPVisual {

    // Componentes da Interface
    private JFrame frame;
    private MapaPanel mapaPanel;
    private JButton startButton;
    private JLabel infoLabel;

    // Instância do Algoritmo Genético
    private AGtsp ag;

    public TSPVisual() {
        // 1. Configura o Algoritmo
        ag = new AGtsp(100, 0.05, 50, 500); // Pop, Mut, Cruz, Gerações
        ag.carregarCidades("cidades.csv");

        // 2. Cria os componentes da interface
        frame = new JFrame("Visualizador do Problema do Caixeiro Viajante");
        mapaPanel = new MapaPanel(ag.cidades); // Passa as cidades para o painel de desenho
        startButton = new JButton("Iniciar Algoritmo");
        infoLabel = new JLabel("Clique em 'Iniciar' para começar. | Gerações: 500");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 3. Monta a janela
        JPanel controlePanel = new JPanel(new BorderLayout());
        controlePanel.add(startButton, BorderLayout.WEST);
        controlePanel.add(infoLabel, BorderLayout.CENTER);
        controlePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        frame.setLayout(new BorderLayout());
        frame.add(mapaPanel, BorderLayout.CENTER);
        frame.add(controlePanel, BorderLayout.SOUTH);

        // 4. Configura a ação do botão
        startButton.addActionListener(e -> executarAlgoritmo());

        // 5. Finaliza e exibe a janela
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void executarAlgoritmo() {
        startButton.setEnabled(false); // Desabilita o botão durante a execução
        infoLabel.setText("Executando...");

        // Cria um SwingWorker para rodar o AG em segundo plano
        TspWorker worker = new TspWorker();
        worker.execute();
    }

    /**
     * Classe interna que executa o algoritmo em uma thread
     * separada para não congelar a interface gráfica.
    */
    private class TspWorker extends SwingWorker<Void, ArrayList<Cidade>> {

        @Override
        protected Void doInBackground() throws Exception {
            ag.criarPopulacao();
            ag.avaliarPopulacao();

            for (int i = 0; i < ag.numeroGeracoes; i++) {
                ag.operadoresGeneticos();
                ag.avaliarPopulacao();

                // Pega a melhor rota da geração atual
                ArrayList<Cidade> melhorDaGeracao = ag.populacao.get(ag.obterMelhor());

                // Publica o resultado intermediário para a interface
                publish(melhorDaGeracao);
                
                // Pausa para a animação ser visível
                Thread.sleep(50);
            }
            return null;
        }

        @Override
        protected void process(List<ArrayList<Cidade>> chunks) {
            // Este método é chamado na thread da interface gráfica
            // Pega a última rota publicada
            ArrayList<Cidade> melhorRota = chunks.get(chunks.size() - 1);
            
            // Atualiza o painel de desenho
            mapaPanel.setRota(melhorRota);

            // Atualiza o texto de informações
            double distancia = 1.0 / ag.fitness(melhorRota);
            infoLabel.setText(String.format("Melhor Distância: %.2f", distancia));
        }

        @Override
        protected void done() {
            // Este método é chamado quando o 'doInBackground' termina
            startButton.setEnabled(true);
            infoLabel.setText(infoLabel.getText() + " | Concluído!");
        }
    }

    public static void main(String[] args) {
        // Garante que a interface seja criada na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> new TSPVisual());
    }
}