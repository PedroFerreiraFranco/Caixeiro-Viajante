import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AGtsp {

    ArrayList<Cidade> cidades = new ArrayList<>();
    private int tamPopulacao;
    private int tamCromossomo = 0;
    private int probMutacao;
    private int qtdeCruzamentos;
    private int numeroGeracoes;
    private ArrayList<ArrayList<Cidade>> populacao = new ArrayList<>();
    private ArrayList<Integer> roletaVirtual = new ArrayList<>();

    public AGtsp(int tamPopulacao, int probMutacao, int qtdeCruzamentos, int numeroGeracoes) {
        this.tamPopulacao = tamPopulacao;
        this.probMutacao = probMutacao;
        this.qtdeCruzamentos = qtdeCruzamentos;
        this.numeroGeracoes = numeroGeracoes;
    }

    public void executar() {
        criarPopulacao();

        for (int i = 0; i < this.numeroGeracoes; i++) {
            //
        }

        int melhor = obterMelhor(); 
        System.out.println("\nMelhor solução encontrada:");
        mostrarRota(populacao.get(melhor));
    }

    public void carregarCidades(String arquivo) {
        String linha;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"))) {
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                String nome = dados[0].trim();
                double x = Double.parseDouble(dados[1]);
                double y = Double.parseDouble(dados[2]);
                Cidade cidade = new Cidade(nome, x, y);
                cidades.add(cidade); // você deve criar a lista cidades: ArrayList<Cidade>
                System.out.println("Carregada: " + cidade);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Cidade> criarCromossomo() {
        ArrayList<Cidade> cromossomo = new ArrayList<>(this.cidades); 
        Collections.shuffle(cromossomo); // 
        return cromossomo;
    }

    private void criarPopulacao() {
        //
    }

    private void mostraPopulacao() {
        for (int i = 0; i < this.populacao.size(); i++) {
            // mostrar indivíduo, fitness e distância total
        }
        System.out.println("-------------------------------");
    }

    private double calcularDistancia(Cidade a, Cidade b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double fitness(ArrayList<Cidade> cromossomo) {
        double distanciaTotal = 0;

        // Somar as distâncias entre cidades consecutivas na rota e adicionar na distancia total
      
        // calcular a distancia da ultima cidade para a primeira e adicionar na distancia total
 

        // Quanto menor a distância, melhor, assim retornar o inverso.
        return (1.0 / distanciaTotal);
    }

    //------------------------------
    private void gerarRoleta() {
        this.roletaVirtual.clear();

        //
    }


    private int roleta() {
        
        return 0;
    }
//------------------------------

    public ArrayList<ArrayList<Cidade>> cruzamentoPMX(ArrayList<Cidade> pai1, ArrayList<Cidade> pai2) {
        int tamanho = pai1.size();
        Random rand = new Random();

        // Gerar dois pontos de corte distintos
        int corte1 = rand.nextInt(tamanho);
        int corte2 = rand.nextInt(tamanho);
        if (corte1 > corte2) {
            int temp = corte1;
            corte1 = corte2;
            corte2 = temp;
        }

        // Inicializar filhos com valores nulos
        ArrayList<Cidade> filho1 = new ArrayList<>(Collections.nCopies(tamanho, null));
        ArrayList<Cidade> filho2 = new ArrayList<>(Collections.nCopies(tamanho, null));

        // Copiar segmento entre os cortes
        for (int i = corte1; i <= corte2; i++) {
            filho1.set(i, pai2.get(i));
            filho2.set(i, pai1.get(i));
        }

        // Preencher o restante dos genes respeitando a permutação
        preencherPMX(filho1, pai1, pai2);
        preencherPMX(filho2, pai2, pai1);

        // Retornar os filhos
        ArrayList<ArrayList<Cidade>> filhos = new ArrayList<>();
        filhos.add(filho1);
        filhos.add(filho2);
        return filhos;
    }

    private void preencherPMX(ArrayList<Cidade> filho, ArrayList<Cidade> paiDeOrigem, ArrayList<Cidade> paiDoSegmento) {
        for (int i = 0; i < paiDeOrigem.size(); i++) {
            if (filho.get(i) == null) {
                Cidade geneAInserir = paiDeOrigem.get(i);
                while (filho.contains(geneAInserir)) {
                    int indexNoPaiSegmento = paiDoSegmento.indexOf(geneAInserir);
                    geneAInserir = paiDeOrigem.get(indexNoPaiSegmento);
                }
                filho.set(i, geneAInserir);
            }
        }
    }

    private void mutacao(ArrayList<Cidade> cromossomo) {
        //
    }

    private int obterMelhor() {
        int melhor = 0;
        //
        return melhor;
    }

    private int obterPior() {
        int pior = 0;
        //
        return pior;
    }

    private void novaPopulacao() {
        //
    }

    private void operadoresGeneticos() {
        //
    }

    private void mostrarRota(ArrayList<Cidade> rota) {
        //
    }

    public static void main(String[] args) {
        AGtsp ag = new AGtsp(50, 5, 10, 100); // população, mutação, cruzamentos, gerações
        ag.carregarCidades("cidades.csv"); // caminho do arquivo CSV
        ag.executar();
    }

}
