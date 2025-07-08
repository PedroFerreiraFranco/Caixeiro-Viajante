import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AGtsp {

    public ArrayList<Cidade> cidades = new ArrayList<>();
    public ArrayList<ArrayList<Cidade>> populacao = new ArrayList<>();
    public int numeroGeracoes;
    
    private int tamPopulacao;
    private double probMutacao;
    private int qtdeCruzamentos;
    private ArrayList<Double> fitnessPopulacao = new ArrayList<>();

    public AGtsp(int tamPopulacao, double probMutacao, int qtdeCruzamentos, int numeroGeracoes) {
        this.tamPopulacao = tamPopulacao;
        this.probMutacao = probMutacao;
        this.qtdeCruzamentos = qtdeCruzamentos;
        this.numeroGeracoes = numeroGeracoes;
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
                cidades.add(cidade);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void criarPopulacao() {
        for (int i = 0; i < this.tamPopulacao; i++) {
            this.populacao.add(criarCromossomo());
        }
    }

    public void avaliarPopulacao() {
        this.fitnessPopulacao.clear();
        for (ArrayList<Cidade> individuo : this.populacao) {
            this.fitnessPopulacao.add(fitness(individuo));
        }
    }

    public double fitness(ArrayList<Cidade> cromossomo) {
        double distanciaTotal = 0;
        for (int i = 0; i < cromossomo.size() - 1; i++) {
            distanciaTotal += cromossomo.get(i).distanciaPara(cromossomo.get(i + 1));
        }
        distanciaTotal += cromossomo.get(cromossomo.size() - 1).distanciaPara(cromossomo.get(0));
        return 1.0 / distanciaTotal;
    }
    
    public int obterMelhor() {
        int melhor = 0;
        for (int i = 1; i < this.populacao.size(); i++) {
            if (this.fitnessPopulacao.get(i) > this.fitnessPopulacao.get(melhor)) {
                melhor = i;
            }
        }
        return melhor;
    }

    public void operadoresGeneticos() {
        ArrayList<ArrayList<Cidade>> novaPopulacao = new ArrayList<>();
        novaPopulacao.add(this.populacao.get(obterMelhor())); 

        while (novaPopulacao.size() < this.tamPopulacao) {
            int idxPai1 = roleta();
            int idxPai2 = roleta();
            while (idxPai1 == idxPai2) {
                idxPai2 = roleta();
            }

            ArrayList<Cidade> pai1 = this.populacao.get(idxPai1);
            ArrayList<Cidade> pai2 = this.populacao.get(idxPai2);

            ArrayList<ArrayList<Cidade>> filhos = cruzamentoPMX(pai1, pai2);
            mutacao(filhos.get(0));
            mutacao(filhos.get(1));

            novaPopulacao.add(filhos.get(0));
            if (novaPopulacao.size() < this.tamPopulacao) {
                novaPopulacao.add(filhos.get(1));
            }
        }
        this.populacao = novaPopulacao;
    }
    
    private ArrayList<Cidade> criarCromossomo() {
        ArrayList<Cidade> cromossomo = new ArrayList<>(this.cidades);
        Collections.shuffle(cromossomo);
        return cromossomo;
    }

    private int roleta() {
        double totalFitness = 0;
        for (double f : this.fitnessPopulacao) {
            totalFitness += f;
        }

        double valorSorteado = new Random().nextDouble() * totalFitness;
        double somaParcial = 0;
        for (int i = 0; i < this.populacao.size(); i++) {
            somaParcial += this.fitnessPopulacao.get(i);
            if (somaParcial >= valorSorteado) {
                return i;
            }
        }
        return this.populacao.size() - 1;
    }

    private ArrayList<ArrayList<Cidade>> cruzamentoPMX(ArrayList<Cidade> pai1, ArrayList<Cidade> pai2) {
        int tamanho = pai1.size();
        Random rand = new Random();

        int corte1 = rand.nextInt(tamanho);
        int corte2 = rand.nextInt(tamanho);
        if (corte1 > corte2) {
            int temp = corte1;
            corte1 = corte2;
            corte2 = temp;
        }

        ArrayList<Cidade> filho1 = new ArrayList<>(Collections.nCopies(tamanho, null));
        ArrayList<Cidade> filho2 = new ArrayList<>(Collections.nCopies(tamanho, null));

        for (int i = corte1; i <= corte2; i++) {
            filho1.set(i, pai2.get(i));
            filho2.set(i, pai1.get(i));
        }

        preencherPMX(filho1, pai1, pai2, corte1, corte2);
        preencherPMX(filho2, pai2, pai1, corte1, corte2);

        ArrayList<ArrayList<Cidade>> filhos = new ArrayList<>();
        filhos.add(filho1);
        filhos.add(filho2);
        return filhos;
    }
    
    private void preencherPMX(ArrayList<Cidade> filho, ArrayList<Cidade> paiOrigem, ArrayList<Cidade> paiSegmento, int corte1, int corte2) {
        for (int i = 0; i < paiOrigem.size(); i++) {
            if (i >= corte1 && i <= corte2) continue;

            Cidade gene = paiOrigem.get(i);
            while (filho.contains(gene)) {
                int indexNoFilho = -1;
                for(int j=corte1; j<=corte2; j++){
                    if(filho.get(j).equals(gene)){
                        indexNoFilho = j;
                        break;
                    }
                }
                gene = paiOrigem.get(indexNoFilho);
            }
            filho.set(i, gene);
        }
    }

    private void mutacao(ArrayList<Cidade> cromossomo) {
        Random rand = new Random();
        if (rand.nextDouble() < this.probMutacao) {
            int pos1 = rand.nextInt(cromossomo.size());
            int pos2 = rand.nextInt(cromossomo.size());
            Collections.swap(cromossomo, pos1, pos2);
        }
    }
}