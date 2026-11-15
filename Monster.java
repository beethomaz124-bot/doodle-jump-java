import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Monster {
    private double x;
    private double y;
    private double vx;
    private final int largura = 44;
    private final int altura = 38;
    private boolean ativo = true;

    public Monster(double x, double y) {
        this.x = x;
        this.y = y;
        this.vx = 1.2;
    }

    public void atualizar(int larguraTela) {
        if (!ativo) return;
        x += vx;
        if (x < 0) {
            x = 0;
            vx = -vx;
        } else if (x + largura > larguraTela) {
            x = larguraTela - largura;
            vx = -vx;
        }
    }

    public void descer(double distancia) {
        y += distancia;
    }

    public void desenhar(Graphics g) {
        if (!ativo) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int px = (int) x;
        int py = (int) y;

        // Perninhas
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(px + 12, py + altura - 4, px + 10, py + altura + 4);
        g2.drawLine(px + 32, py + altura - 4, px + 34, py + altura + 4);

        // Corpo do monstro (azul estilo Doodle Jump)
        g2.setColor(new Color(65, 155, 215));
        g2.fillRoundRect(px, py + 4, largura, altura - 6, 22, 22);

        // Pelinhos no topo
        g2.setColor(Color.BLACK);
        g2.drawLine(px + 14, py + 4, px + 12, py - 2);
        g2.drawLine(px + 22, py + 4, px + 22, py - 3);
        g2.drawLine(px + 30, py + 4, px + 32, py - 2);

        // Olho grande central
        g2.setColor(Color.WHITE);
        g2.fillOval(px + 13, py + 9, 18, 16);
        g2.setColor(Color.BLACK);
        g2.drawOval(px + 13, py + 9, 18, 16);
        g2.fillOval(px + 20, py + 14, 5, 5);

        // Boca com dentinhos
        g2.setColor(new Color(140, 45, 130));
        g2.fillRect(px + 10, py + 27, 24, 6);
        g2.setColor(Color.BLACK);
        g2.drawRect(px + 10, py + 27, 24, 6);

        // Contorno
        g2.drawRoundRect(px, py + 4, largura, altura - 6, 22, 22);
    }

    // Verifica se o jogador pisou em cima do monstro (derrota o monstro e quica!)
    public boolean foiPisadoPor(Player p) {
        if (!ativo) return false;
        double pes = p.getY() + p.getAltura();
        double pesAntes = pes - p.getVelocidadeY();
        boolean caindo = p.getVelocidadeY() > 0;
        boolean cruzouTopo = pesAntes <= y + 10 && pes >= y;
        boolean alinhado = p.getX() + p.getLargura() > x && p.getX() < x + largura;
        return caindo && cruzouTopo && alinhado;
    }

    // Verifica colisão frontal/lateral com o monstro
    public boolean colidiuCom(Player p) {
        if (!ativo) return false;
        return p.getX() + p.getLargura() - 6 > x + 4 && p.getX() + 6 < x + largura - 4 &&
               p.getY() + p.getAltura() - 4 > y + 6 && p.getY() + 4 < y + altura - 4;
    }

    public double getY() { return y; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
