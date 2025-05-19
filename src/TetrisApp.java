import javax.swing.*;

public class TetrisApp {
    private JFrame frame;
    private MainMenuPanel menuPanel;
    private TetrisGame gamePanel;
    SoundManager soundManager = new SoundManager();

    public TetrisApp() {
        soundManager.loadBGM("music/korobeiniki.wav");
        soundManager.loadSFX("put", "music/tap.wav");
        frame = new JFrame("Tetris Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        menuPanel = new MainMenuPanel(this);
        frame.setContentPane(menuPanel);
        frame.setVisible(true);
    }

    public void startGame() {
        gamePanel = new TetrisGame(this, soundManager, true);
        frame.setContentPane(gamePanel);
        frame.revalidate();
        gamePanel.requestFocusInWindow(); // 確保接收鍵盤
    }
    public void startLines40Game() {
        gamePanel = new TimeAttackGame(this, soundManager);
        frame.setContentPane(gamePanel);
        frame.revalidate();
        gamePanel.requestFocusInWindow(); // 確保接收鍵盤
    }
    public void startTimeChallengeGame() {
        gamePanel = new TimeChallengeGame(this, soundManager);
        frame.setContentPane(gamePanel);
        frame.revalidate();
        gamePanel.requestFocusInWindow(); // 確保接收鍵盤
    }
    public void showSettings() {
        SettingPanel settings = new SettingPanel(this, soundManager);
        // 顯示設定畫面
        frame.setContentPane(settings);
        frame.revalidate();
    }

    public void backToMenu() {
        menuPanel = new MainMenuPanel(this);
        frame.setContentPane(menuPanel);
        frame.revalidate();
    }

    public void showGameOverPanel(
        int score,
        int linesCleared,
        int tSpinCount,
        int maxCombo,
        double playTimeSeconds,
        Runnable onRestart,
        Runnable onBackToMenu
    ) {
        GameOverPanel panel = new GameOverPanel(
            score,
            linesCleared,
            tSpinCount,
            maxCombo,
            playTimeSeconds,
            onRestart,
            onBackToMenu
        );
        frame.setContentPane(panel);
        frame.revalidate();
        panel.requestFocusInWindow();
    }

    public static void main(String[] args) {
        new TetrisApp();
    }
}
