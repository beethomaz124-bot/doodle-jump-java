import javax.swing.JFrame;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("DOODLE JUMP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(new GamePanel());        // coloca o painel do jogo dentro da janela
        pack();                      // ajusta o tamanho da janela ao painel
        setLocationRelativeTo(null); // centraliza na tela
        setVisible(true);
    }
}
