import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

public class MapaPanel extends JPanel {
    private ArrayList<Cidade> cidades;
    private ArrayList<Cidade> rota;

    private final int MARGEM = 30;
    private final int ESCALA = 6;
    private final int TAMANHO_PONTO = 8;

    public MapaPanel(ArrayList<Cidade> cidades) {
        this.cidades = cidades;
        this.rota = new ArrayList<>();
        setBackground(Color.WHITE);
    }

    public void setRota(ArrayList<Cidade> novaRota) {
        this.rota = novaRota;
        repaint(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (rota != null && rota.size() > 1) {
            g2d.setColor(new Color(0, 102, 204)); 
            for (int i = 0; i < rota.size() - 1; i++) {
                Cidade c1 = rota.get(i);
                Cidade c2 = rota.get(i + 1);
                g2d.drawLine(
                    (int)c1.getX() * ESCALA + MARGEM, 
                    (int)c1.getY() * ESCALA + MARGEM, 
                    (int)c2.getX() * ESCALA + MARGEM, 
                    (int)c2.getY() * ESCALA + MARGEM
                );
            }
            Cidade ultima = rota.get(rota.size() - 1);
            Cidade primeira = rota.get(0);
            g2d.drawLine(
                (int)ultima.getX() * ESCALA + MARGEM, 
                (int)ultima.getY() * ESCALA + MARGEM, 
                (int)primeira.getX() * ESCALA + MARGEM, 
                (int)primeira.getY() * ESCALA + MARGEM
            );
        }

        for (Cidade cidade : cidades) {
            int x = (int) cidade.getX() * ESCALA + MARGEM;
            int y = (int) cidade.getY() * ESCALA + MARGEM;
            
            g2d.setColor(Color.RED);
            g2d.fillOval(x - TAMANHO_PONTO / 2, y - TAMANHO_PONTO / 2, TAMANHO_PONTO, TAMANHO_PONTO);
            
            g2d.setColor(Color.BLACK);
            g2d.drawString(cidade.getNome(), x + TAMANHO_PONTO, y);
        }
    }
}