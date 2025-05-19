import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(TetrisApp app) {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);  // 背景黑色

        GridBagConstraints gbc = new GridBagConstraints();

        // 標題
        JLabel title = new JLabel("TETRIS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);  // 白色文字

        // 按鈕建立
        JButton startButton = createAnimatedButton("Start", e -> app.startGame());
        JButton lines40Button = createAnimatedButton("40Lines", e -> app.startLines40Game());
        JButton TimeChallengeButton = createAnimatedButton("TimeChallenge", e -> app.startTimeChallengeGame());
        JButton settingButton = createAnimatedButton("Setting", e -> app.showSettings());

        // 加入元件
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridy = 0;
        add(title, gbc);
        gbc.gridy = 1;
        add(startButton, gbc);
        gbc.gridy = 2;
        add(lines40Button, gbc);
        gbc.gridy = 3;
        add(TimeChallengeButton, gbc);
        gbc.gridy = 4;
        add(settingButton, gbc);
    }

    private JButton createAnimatedButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 固定內距 padding
        Border padding = BorderFactory.createEmptyBorder(10, 20, 10, 20);

        // 起始邊框透明
        Color borderColor = new Color(255, 255, 255, 0);
        Border currentBorder = BorderFactory.createLineBorder(borderColor, 2);
        button.setBorder(BorderFactory.createCompoundBorder(currentBorder, padding));

        // 動畫控制參數
        final Timer[] animationTimer = new Timer[1];
        final int[] alpha = {0};  // 透明度從 0 ~ 255

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (animationTimer[0] != null && animationTimer[0].isRunning()) {
                    animationTimer[0].stop();
                }

                animationTimer[0] = new Timer(15, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        alpha[0] = Math.min(alpha[0] + 15, 255);
                        Color c = new Color(255, 255, 255, alpha[0]);
                        Border animatedBorder = BorderFactory.createLineBorder(c, 2);
                        button.setBorder(BorderFactory.createCompoundBorder(animatedBorder, padding));
                        if (alpha[0] >= 255) {
                            animationTimer[0].stop();
                        }
                    }
                });
                animationTimer[0].start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (animationTimer[0] != null && animationTimer[0].isRunning()) {
                    animationTimer[0].stop();
                }

                animationTimer[0] = new Timer(15, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        alpha[0] = Math.max(alpha[0] - 15, 0);
                        Color c = new Color(255, 255, 255, alpha[0]);
                        Border animatedBorder = BorderFactory.createLineBorder(c, 2);
                        button.setBorder(BorderFactory.createCompoundBorder(animatedBorder, padding));
                        if (alpha[0] <= 0) {
                            animationTimer[0].stop();
                        }
                    }
                });
                animationTimer[0].start();
            }
        });

        button.addActionListener(action);
        return button;
    }
}
