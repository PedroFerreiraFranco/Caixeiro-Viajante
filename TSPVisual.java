// Arquivo: TSPVisual.java (VERSÃO FINAL COM LISTA DE ROTA)
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TSPVisual {

    // Componentes da Interface
    private JFrame frame;
    private MapaPanel mapaPanel;
    private JButton startButton;
    private JLabel infoLabel;
    private JTextArea rotaTextArea; // NOVO: Área para mostrar a rota final

    // Instância do Algoritmo Genético
    private AGtsp ag;

    public TSPVisual() {
        // 1. Configura o Algoritmo
        ag = new AGtsp(100, 0.05, 50, 500); // Pop, Mut, Cruz, Gerações
        ag.carregarCidades("cidades.csv");

        // 2. Cria os componentes da interface
        frame = new JFrame("Visualizador do Problema do Caixeiro Viajante");
        mapaPanel = new MapaPanel(ag.cidades);
        startButton = new JButton("Iniciar Algoritmo");
        infoLabel = new JLabel("Clique em 'Iniciar' para começar. | Gerações: 500");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // NOVO: Configura a área de texto para a rota
        rotaTextArea = new JTextArea("A melhor rota aparecerá aqui...");
        rotaTextArea.setEditable(false);
        rotaTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        rotaTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(rotaTextArea); // Adiciona uma barra de rolagem
        scrollPane.setPreferredSize(new Dimension(200, 0)); // Define a largura preferida

        // 3. Monta a janela
        JPanel controlePanel = new JPanel(new BorderLayout());
        controlePanel.add(startButton, BorderLayout.WEST);
        controlePanel.add(infoLabel, BorderLayout.CENTER);
        controlePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        frame.setLayout(new BorderLayout());
        frame.add(mapaPanel, BorderLayout.CENTER);
        frame.add(controlePanel, BorderLayout.SOUTH);
        frame.add(scrollPane, BorderLayout.EAST); // NOVO: Adiciona o painel de texto à direita

        // 4. Configura a ação do botão
        startButton.addActionListener(e -> executarAlgoritmo());

        // 5. Finaliza e exibe a janela
        frame.setSize(1000, 800); // Aumentei a largura para caber a nova área
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void executarAlgoritmo() {
        startButton.setEnabled(false);
        infoLabel.setText("Executando...");
        rotaTextArea.setText("Calculando a melhor rota..."); // NOVO: Limpa a área de texto

        TspWorker worker = new TspWorker();
        worker.execute();
    }

    private class TspWorker extends SwingWorker<ArrayList<Cidade>, ArrayList<Cidade>> {

        @Override
        protected ArrayList<Cidade> doInBackground() throws Exception {
            ag.criarPopulacao();
            ag.avaliarPopulacao();

            for (int i = 0; i < ag.numeroGeracoes; i++) {
                ag.operadoresGeneticos();
                ag.avaliarPopulacao();

                ArrayList<Cidade> melhorDaGeracao = ag.populacao.get(ag.obterMelhor());
                publish(melhorDaGeracao); // Publica a rota para a animação
                Thread.sleep(50);
            }
            // Retorna a melhor rota final
            return ag.populacao.get(ag.obterMelhor());
        }

        @Override
        protected void process(List<ArrayList<Cidade>> chunks) {
            ArrayList<Cidade> melhorRota = chunks.get(chunks.size() - 1);
            mapaPanel.setRota(melhorRota);

            double distancia = 1.0 / ag.fitness(melhorRota);
            infoLabel.setText(String.format("Melhor Distância: %.2f", distancia));
        }

        @Override
        protected void done() {
            try {
                // Pega a rota final retornada pelo 'doInBackground'
                ArrayList<Cidade> melhorRotaFinal = get();

                // Constrói o texto da rota final
                StringBuilder rotaStr = new StringBuilder("Rota Final:\n\n");
                for (Cidade c : melhorRotaFinal) {
                    rotaStr.append(" -> ").append(c.getNome()).append("\n");
                }
                rotaStr.append(" -> ").append(melhorRotaFinal.get(0).getNome()); // Volta ao início

                // Atualiza a área de texto com a lista
                rotaTextArea.setText(rotaStr.toString());

            } catch (Exception e) {
                e.printStackTrace();
                rotaTextArea.setText("Ocorreu um erro ao finalizar o algoritmo.");
            }

            startButton.setEnabled(true);
            infoLabel.setText(infoLabel.getText() + " | Concluído!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TSPVisual());
    }
}