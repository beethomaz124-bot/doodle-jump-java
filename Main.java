import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Garante que a janela seja criada na thread correta do Swing
        SwingUtilities.invokeLater(() -> new GameFrame());
    }
}
