import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    public static final int LARGURA = 400;
    public static final int ALTURA = 600;

    // Estados do jogo
    private static final int MENU = 0;
    private static final int JOGANDO = 1;
    private static final int GAME_OVER = 2;

    private static final int LIMITE_CAMERA = 250;      // altura onde a câmera começa a subir
    private static final int ESPACO_PLATAFORMAS = 80;  // distância vertical entre plataformas
    private static final int QTD_PLATAFORMAS = 9;

    private int estado;
    private int pontuacao;
    private double alturaPercorrida;

    private Player jogador;
    private ArrayList<Platform> plataformas;
    private ArrayList<Monster> monstros;
    private Timer timer;
    private Random random;

    private JButton botaoJogar;
    private JButton botaoJogarNovamente;
    private JButton botaoMenu;

    public GamePanel() {
        setPreferredSize(new Dimension(LARGURA, ALTURA));
        setLayout(null); // posicionamos os botões manualmente
        setFocusable(true);
        addKeyListener(this);

        random = new Random();

        botaoJogar = criarBotao("JOGAR");
        botaoJogarNovamente = criarBotao("JOGAR NOVAMENTE");
        botaoMenu = criarBotao("MENU");
        botaoJogarNovamente.setFont(fonte(20));
        botaoMenu.setFont(fonte(18));

        timer = new Timer(16, this); // ~60 atualizações por segundo
        timer.start();

        mostrarMenu();
    }

    // ---------- Criação e controle de telas ----------

    private static Font fonte(int tamanho) {
        return new Font("Comic Sans MS", Font.BOLD, tamanho);
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();

                g2.setColor(new Color(250, 242, 222));   // papel
                g2.fillOval(3, 3, w - 6, h - 6);
                g2.setColor(new Color(235, 165, 55));    // laranja interno
                g2.setStroke(new BasicStroke(3f));
                g2.drawOval(8, 8, w - 16, h - 16);
                g2.setColor(Color.BLACK);                // contorno preto
                g2.setStroke(new BasicStroke(4f));
                g2.drawOval(3, 3, w - 6, h - 6);

                g2.setFont(getFont());
                FontMetrics medidas = g2.getFontMetrics();
                int tx = (w - medidas.stringWidth(getText())) / 2;
                int ty = (h - medidas.getHeight()) / 2 + medidas.getAscent();
                g2.drawString(getText(), tx, ty);
            }
        };
        botao.setFont(fonte(22));
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setFocusable(false);
        botao.addActionListener(this);
        add(botao);
        return botao;
    }

    private String sortearTipoPlataforma() {
        int sorteio = random.nextInt(100);
        if (sorteio < 15) return "quebravel"; // 15% quebradiça (marrom)
        if (sorteio < 30) return "mola";      // 15% com mola
        return "normal";                      // 70% normal (verde)
    }

    // Cria jogador, plataformas e monstros na posição inicial
    private void prepararJogo() {
        jogador = new Player(LARGURA / 2 - 20, ALTURA - 100);
        jogador.pular();

        plataformas = new ArrayList<Platform>();
        monstros = new ArrayList<Monster>();

        plataformas.add(new Platform(LARGURA / 2 - 35, ALTURA - 60, "normal")); // plataforma sob o jogador
        for (int i = 1; i < QTD_PLATAFORMAS; i++) {
            double px = random.nextInt(LARGURA - 70);
            double py = ALTURA - 60 - i * ESPACO_PLATAFORMAS;
            plataformas.add(new Platform(px, py, sortearTipoPlataforma()));
        }

        // Adiciona um monstro inicial no topo da tela para já aparecer logo no início
        monstros.add(new Monster(random.nextInt(LARGURA - 50), 90));

        pontuacao = 0;
        alturaPercorrida = 0;
    }

    private void mostrarMenu() {
        prepararJogo();
        estado = MENU;
        botaoJogar.setBounds(100, 320, 200, 80);
        botaoJogar.setVisible(true);
        botaoJogarNovamente.setVisible(false);
        botaoMenu.setVisible(false);
    }

    private void iniciarJogo() {
        prepararJogo();
        estado = JOGANDO;
        botaoJogar.setVisible(false);
        botaoJogarNovamente.setVisible(false);
        botaoMenu.setBounds(304, 8, 88, 38);
        botaoMenu.setVisible(true);
        requestFocusInWindow();
    }

    private void fimDeJogo() {
        estado = GAME_OVER;
        botaoJogarNovamente.setBounds(60, 345, 280, 70);
        botaoMenu.setBounds(110, 425, 180, 55);
        botaoJogarNovamente.setVisible(true);
        botaoMenu.setVisible(true);
    }

    // ---------- Lógica do jogo ----------

    private void atualizar() {
        jogador.atualizar(LARGURA);

        // Colisão com as plataformas
        for (Platform p : plataformas) {
            if (!p.isQuebrada() && jogador.pousouEm(p)) {
                if (p.getTipo().equals("quebravel")) {
                    p.setQuebrada(true); // quebra ao pisar
                } else if (p.getTipo().equals("mola")) {
                    jogador.pularComForcaExtra();
                } else {
                    jogador.pular();
                }
            }
        }

        // Atualiza e verifica colisão com os monstros
        for (Monster m : monstros) {
            m.atualizar(LARGURA);
            if (m.foiPisadoPor(jogador)) {
                m.setAtivo(false);
                jogador.pular();
                pontuacao += 20;
            } else if (m.colidiuCom(jogador)) {
                fimDeJogo();
            }
        }

        // Câmera: se o jogador passar da linha limite, o mundo desce
        if (jogador.getY() < LIMITE_CAMERA) {
            double diferenca = LIMITE_CAMERA - jogador.getY();
            jogador.setY(LIMITE_CAMERA);
            for (Platform p : plataformas) {
                p.descer(diferenca);
            }
            for (Monster m : monstros) {
                m.descer(diferenca);
            }
            alturaPercorrida += diferenca;
            pontuacao = (int) (alturaPercorrida / 10);
        }

        reciclarPlataformasEMonstros();

        // Caiu da tela
        if (jogador.getY() > ALTURA) {
            fimDeJogo();
        }
    }

    // Plataformas que saem por baixo reaparecem no topo
    private void reciclarPlataformasEMonstros() {
        for (Platform p : plataformas) {
            if (p.getY() > ALTURA) {
                p.setY(encontrarMaisAlta() - ESPACO_PLATAFORMAS);
                p.setX(random.nextInt(LARGURA - p.getLargura()));
                p.setTipo(sortearTipoPlataforma());
                p.setQuebrada(false);

                // 20% de chance de gerar um monstro acima da nova plataforma
                if (random.nextInt(100) < 20) {
                    monstros.add(new Monster(random.nextInt(LARGURA - 50), p.getY() - 45));
                }
            }
        }

        monstros.removeIf(m -> !m.isAtivo() || m.getY() > ALTURA);
    }

    private double encontrarMaisAlta() {
        double menorY = plataformas.get(0).getY();
        for (Platform p : plataformas) {
            if (p.getY() < menorY) {
                menorY = p.getY();
            }
        }
        return menorY;
    }

    // ---------- Desenho ----------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        desenharFundo(g2);

        if (estado == MENU) {
            plataformas.get(0).desenhar(g2);
            jogador.desenhar(g2);
            desenharMenu(g2);
        } else {
            for (Platform p : plataformas) {
                p.desenhar(g2);
            }
            for (Monster m : monstros) {
                m.desenhar(g2);
            }
            jogador.desenhar(g2);

            if (estado == JOGANDO) {
                g2.setColor(new Color(40, 40, 40));
                g2.setFont(fonte(22));
                g2.drawString("PONTOS: " + pontuacao, 12, 30);
            } else {
                desenharGameOver(g2);
            }
        }
    }

    private void desenharFundo(Graphics2D g2) {
        g2.setColor(new Color(250, 243, 228));
        g2.fillRect(0, 0, LARGURA, ALTURA);

        g2.setColor(new Color(236, 215, 196));
        g2.setStroke(new BasicStroke(1f));
        for (int i = 0; i < LARGURA; i += 15) {
            g2.drawLine(i, 0, i, ALTURA);
        }
        for (int j = 0; j < ALTURA; j += 15) {
            g2.drawLine(0, j, LARGURA, j);
        }
    }

    private void desenharMenu(Graphics2D g2) {
        AffineTransform original = g2.getTransform();
        g2.rotate(-0.1, LARGURA / 2.0, 120);
        desenharTextoCentralizado(g2, "DOODLE JUMP", 120, 46, new Color(190, 40, 40));
        g2.setTransform(original);

        desenharTextoCentralizado(g2, "use as setas do teclado", 470, 18, new Color(40, 40, 40));
        desenharTextoCentralizado(g2, "para se mover", 494, 18, new Color(40, 40, 40));
    }

    private void desenharGameOver(Graphics2D g2) {
        g2.setColor(new Color(250, 243, 228, 240));
        g2.fillRoundRect(40, 190, 320, 300, 40, 40);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRoundRect(40, 190, 320, 300, 40, 40);

        desenharTextoCentralizado(g2, "GAME OVER", 255, 40, new Color(190, 40, 40));
        desenharTextoCentralizado(g2, "Pontuação: " + pontuacao, 305, 26, new Color(40, 40, 40));
    }

    private void desenharTextoCentralizado(Graphics2D g2, String texto, int y, int tamanho, Color cor) {
        Font fonte = fonte(tamanho);
        g2.setFont(fonte);
        FontMetrics medidas = g2.getFontMetrics(fonte);
        int x = (LARGURA - medidas.stringWidth(texto)) / 2;
        g2.setColor(cor);
        g2.drawString(texto, x, y);
    }

    // ---------- Eventos ----------

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origem = e.getSource();

        if (origem == botaoJogar || origem == botaoJogarNovamente) {
            iniciarJogo();
        } else if (origem == botaoMenu) {
            mostrarMenu();
        } else if (origem == timer) {
            if (estado == JOGANDO) {
                atualizar();
            }
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            jogador.setMovendoEsquerda(true);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            jogador.setMovendoDireita(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            jogador.setMovendoEsquerda(false);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            jogador.setMovendoDireita(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // não utilizado
    }
}
