import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class GameOverPanel extends JPanel {
    public GameOverPanel(
        int score,
        int linesCleared,
        int tSpinCount,
        int maxCombo,
        double playTimeSeconds,
        Runnable onRestart,
        Runnable onBackToMenu
    ) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        JLabel label = new JLabel("Game Over");
        label.setForeground(Color.RED);
        label.setFont(new Font("Arial", Font.BOLD, 48));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 32));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel linesLabel = new JLabel("Lines Cleared: " + linesCleared);
        linesLabel.setForeground(Color.WHITE);
        linesLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        linesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tSpinLabel = new JLabel("T-Spins: " + tSpinCount);
        tSpinLabel.setForeground(Color.WHITE);
        tSpinLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        tSpinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel comboLabel = new JLabel("Max Combo: " + maxCombo);
        comboLabel.setForeground(Color.WHITE);
        comboLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        comboLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel timeLabel = new JLabel(String.format("Play Time: %.2f s", playTimeSeconds));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton restartBtn = createAnimatedButton("Restart", e -> onRestart.run());
        restartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton menuBtn = createAnimatedButton("Back To Menu", e -> onBackToMenu.run());
        menuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(label);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(scoreLabel);
        add(linesLabel);
        add(tSpinLabel);
        add(comboLabel);
        add(timeLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(restartBtn);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(menuBtn);
        add(Box.createVerticalGlue());
    }

    private JButton createAnimatedButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Border padding = BorderFactory.createEmptyBorder(10, 20, 10, 20);
        Color borderColor = new Color(255, 255, 255, 0);
        Border currentBorder = BorderFactory.createLineBorder(borderColor, 2);
        button.setBorder(BorderFactory.createCompoundBorder(currentBorder, padding));

        final Timer[] animationTimer = new Timer[1];
        final int[] alpha = {0};

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