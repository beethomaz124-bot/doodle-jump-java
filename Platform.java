import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Platform {

    private double x;
    private double y;
    private final int largura;
    private final int altura;
    private String tipo; // "normal", "quebravel", "mola"
    private boolean quebrada = false;

    public Platform(double x, double y) {
        this(x, y, "normal");
    }

    public Platform(double x, double y, String tipo) {
        this.x = x;
        this.y = y;
        this.largura = 70;
        this.altura = 15;
        this.tipo = tipo;
    }

    // Move a plataforma para baixo (usado quando a câmera sobe)
    public void descer(double distancia) {
        y += distancia;
    }

    // Desenha a plataforma no estilo "desenhado à mão" do Doodle Jump
    public void desenhar(Graphics g) {
        if (quebrada) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int px = (int) x;
        int py = (int) y;

        if (tipo.equals("quebravel")) {
            // Plataforma marrom rachada
            g2.setColor(new Color(145, 80, 35));
            g2.fillRoundRect(px, py, largura, altura, 14, 14);
            g2.setColor(new Color(185, 120, 70));
            g2.fillRoundRect(px + 8, py + 3, largura - 22, 4, 4, 4);
            g2.setColor(new Color(45, 20, 5));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(px, py, largura, altura, 14, 14);
            // Rachadura no meio
            g2.drawLine(px + largura / 2 - 2, py, px + largura / 2 + 2, py + altura / 2);
            g2.drawLine(px + largura / 2 + 2, py + altura / 2, px + largura / 2 - 3, py + altura);
        } else {
            // Plataforma verde
            g2.setColor(new Color(110, 200, 40));
            g2.fillRoundRect(px, py, largura, altura, 14, 14);
            g2.setColor(new Color(175, 235, 90));
            g2.fillRoundRect(px + 8, py + 3, largura - 22, 4, 4, 4);
            g2.setColor(new Color(30, 60, 10));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(px, py, largura, altura, 14, 14);

            // Se tiver mola, desenha a molinha cinza em cima
            if (tipo.equals("mola")) {
                g2.setColor(new Color(170, 175, 180));
                g2.fillRect(px + 28, py - 10, 14, 10);
                g2.setColor(Color.BLACK);
                g2.drawRect(px + 28, py - 10, 14, 10);
                g2.drawLine(px + 28, py - 7, px + 42, py - 5);
                g2.drawLine(px + 28, py - 4, px + 42, py - 2);
            }
        }
    }

    // Getters e setters
    public double getX() { return x; }
    public double getY() { return y; }
    public int getLargura() { return largura; }
    public int getAltura() { return altura; }
    public String getTipo() { return tipo; }
    public boolean isQuebrada() { return quebrada; }
    public void setQuebrada(boolean quebrada) { this.quebrada = quebrada; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
