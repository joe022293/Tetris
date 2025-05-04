import java.awt.*;
import javax.swing.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(TetrisApp app) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // 設置標題
        JLabel title = new JLabel("TETRIS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 設置 Tetris 遊戲區域
        TetrisGame gamePanel = new TetrisGame(app);  // 假設 TetrisGame 需要傳入 TetrisApp 物件
        gamePanel.setPreferredSize(new Dimension(320, 700));  // 設定 TetrisGame 的大小
        
        // 設置開始按鈕
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);  // 使按鈕水平居中
        startButton.addActionListener(e -> app.startGame());

        // GridBagLayout 組件安排
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);  // 設置間距
        add(title, gbc);
        
        // 設置開始按鈕在第二行
        gbc.gridy = 1;
        add(startButton, gbc);
    }
}
