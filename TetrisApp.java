import javax.swing.*;

public class TetrisApp {
    private JFrame frame;
    private MainMenuPanel menuPanel;
    private TetrisGame gamePanel;

    public TetrisApp() {
        frame = new JFrame("Tetris Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(320, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        menuPanel = new MainMenuPanel(this);
        frame.setContentPane(menuPanel);
        frame.setVisible(true);
    }

    public void startGame() {
        gamePanel = new TetrisGame(this);
        frame.setContentPane(gamePanel);
        frame.revalidate();
        gamePanel.requestFocusInWindow(); // 確保接收鍵盤
    }

    public void backToMenu() {
        menuPanel = new MainMenuPanel(this);
        frame.setContentPane(menuPanel);
        frame.revalidate();
    }

    public static void main(String[] args) {
        new TetrisApp();
    }
}
