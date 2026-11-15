import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Player {

    private static final double GRAVIDADE = 0.4;
    private static final double FORCA_PULO = -11;
    private static final double FORCA_PULO_EXTRA = -18;
    private static final double VELOCIDADE_LATERAL = 5;

    private double x;
    private double y;
    private double velocidadeY;
    private final int largura;
    private final int altura;
    private boolean movendoEsquerda;
    private boolean movendoDireita;
    private int direcao; // 1 = olhando para a direita, -1 = esquerda

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.largura = 40;
        this.altura = 40;
        this.velocidadeY = 0;
        this.direcao = 1;
    }

    // Atualiza posição: movimento lateral + gravidade
    public void atualizar(int larguraTela) {
        if (movendoEsquerda) {
            x -= VELOCIDADE_LATERAL;
            direcao = -1;
        }
        if (movendoDireita) {
            x += VELOCIDADE_LATERAL;
            direcao = 1;
        }

        // Se sair por um lado da tela, aparece do outro
        if (x + largura < 0) {
            x = larguraTela;
        } else if (x > larguraTela) {
            x = -largura;
        }

        velocidadeY += GRAVIDADE; // a gravidade aumenta a velocidade de queda
        y += velocidadeY;
    }

    public void pular() {
        velocidadeY = FORCA_PULO;
    }

    public void pularComForcaExtra() {
        velocidadeY = FORCA_PULO_EXTRA;
    }

    // Verifica se o jogador está caindo e "pousou" em cima da plataforma
    public boolean pousouEm(Platform p) {
        double pes = y + altura;              // posição dos pés agora
        double pesAntes = pes - velocidadeY;  // posição dos pés no quadro anterior

        boolean caindo = velocidadeY > 0;
        boolean cruzouTopo = pesAntes <= p.getY() + 1 && pes >= p.getY();
        boolean alinhado = x + largura > p.getX() && x < p.getX() + p.getLargura();

        return caindo && cruzouTopo && alinhado;
    }

    // X de um retângulo; se o boneco olha para a esquerda, espelha
    private int posicaoX(int px, int deslocamento, int larguraParte) {
        if (direcao == 1) {
            return px + deslocamento;
        }
        return px + largura - deslocamento - larguraParte;
    }

    // X de um ponto; também espelha conforme a direção
    private int espelhar(int px, int deslocamento) {
        if (direcao == 1) {
            return px + deslocamento;
        }
        return px + largura - deslocamento;
    }

    // Desenha o Doodler: cabeça amarela, focinho comprido, barriga listrada e perninhas
    public void desenhar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2f));

        int px = (int) x;
        int py = (int) y;

        Color amarelo = new Color(205, 215, 40);
        Color verde = new Color(110, 180, 50);
        Color verdeClaro = new Color(190, 225, 110);
        Color contorno = new Color(30, 40, 10);

        // pernas (4 tracinhos com pezinho)
        g2.setColor(contorno);
        for (int i = 0; i < 4; i++) {
            int lx = px + 9 + i * 7;
            g2.drawLine(lx, py + 38, lx, py + 41);
            g2.drawLine(lx, py + 41, lx + (direcao == 1 ? 3 : -3), py + 41);
        }

        // focinho: tubo + ponta alargada
        g2.setColor(amarelo);
        g2.fillRect(posicaoX(px, 30, 10), py + 13, 10, 8);
        int[] pontaX = { espelhar(px, 38), espelhar(px, 46), espelhar(px, 46), espelhar(px, 38) };
        int[] pontaY = { py + 12, py + 9, py + 25, py + 21 };
        g2.fillPolygon(pontaX, pontaY, 4);
        g2.setColor(contorno);
        g2.drawLine(posicaoX(px, 30, 8), py + 13, posicaoX(px, 38, 0), py + 13);
        g2.drawLine(posicaoX(px, 30, 8), py + 21, posicaoX(px, 38, 0), py + 21);
        g2.drawPolygon(pontaX, pontaY, 4);

        // cabeça/corpo (topo arredondado)
        Area forma = new Area(new RoundRectangle2D.Double(px + 4, py, 32, 24, 28, 28));
        forma.add(new Area(new Rectangle2D.Double(px + 4, py + 12, 32, 14)));
        g2.setColor(amarelo);
        g2.fill(forma);
        g2.setColor(contorno);
        g2.draw(forma);

        // barriga listrada
        for (int i = 0; i < 4; i++) {
            g2.setColor(i % 2 == 0 ? verde : verdeClaro);
            g2.fillRect(px + 6, py + 26 + i * 3, 28, 3);
        }
        g2.setColor(contorno);
        g2.drawRect(px + 6, py + 26, 28, 12);

        // olhos
        g2.fillOval(posicaoX(px, 20, 4), py + 5, 4, 6);
        g2.fillOval(posicaoX(px, 27, 4), py + 5, 4, 6);
    }

    // Getters e setters (encapsulamento)
    public double getX() { return x; }
    public double getY() { return y; }
    public int getLargura() { return largura; }
    public int getAltura() { return altura; }
    public double getVelocidadeY() { return velocidadeY; }
    public void setY(double y) { this.y = y; }
    public void setMovendoEsquerda(boolean valor) { movendoEsquerda = valor; }
    public void setMovendoDireita(boolean valor) { movendoDireita = valor; }
}
