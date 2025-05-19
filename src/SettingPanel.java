import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class SettingPanel extends JPanel {
    private JSlider bgmSlider;
    private JSlider sfxSlider;
    private JLabel bgmLabelValue;
    private JLabel sfxLabelValue;
    private SoundManager soundManager;
    private TetrisApp app;

    public SettingPanel(TetrisApp app, SoundManager sm) {
        this.soundManager = sm;
        this.app = app;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);  // 黑色背景

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // BGM 音量
        JLabel bgmLabel = createWhiteLabel("BGM:");
        bgmSlider = new JSlider(0, 100, soundManager.getBGMVolume());
        styleSlider(bgmSlider);
        bgmLabelValue = createWhiteLabel(String.valueOf(soundManager.getBGMVolume()));
        bgmSlider.addChangeListener(e -> {
            soundManager.setBGMVolume(bgmSlider.getValue());
            bgmLabelValue.setText(String.valueOf(bgmSlider.getValue()));
        });

        // 特效音量
        JLabel sfxLabel = createWhiteLabel("Special Voice:");
        sfxSlider = new JSlider(0, 100, soundManager.getComboVolume());
        styleSlider(sfxSlider);
        sfxLabelValue = createWhiteLabel(String.valueOf(soundManager.getComboVolume()));
        sfxSlider.addChangeListener(e -> {
            int value = sfxSlider.getValue();
            sfxLabelValue.setText(String.valueOf(value));
            soundManager.setComboVolume(value);
            soundManager.setSFXVolume(value, "put");
        });

        // 返回主選單按鈕
        JButton backMenu = createAnimatedButton("Back", e -> app.backToMenu());

        // 加入元件：BGM
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(bgmLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(bgmSlider, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(bgmLabelValue, gbc);

        // 加入元件：SFX
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(sfxLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(sfxSlider, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(sfxLabelValue, gbc);

        // 返回按鈕
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(backMenu, gbc);
    }

    private JLabel createWhiteLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        return label;
    }

    private void styleSlider(JSlider slider) {
        slider.setPreferredSize(new Dimension(150, 30));
        slider.setBackground(Color.BLACK);
        slider.setForeground(Color.WHITE);
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
        slider.setOpaque(true);
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
