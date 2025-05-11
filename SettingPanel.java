import java.awt.*;
import javax.swing.*;

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
        GridBagConstraints gbc = new GridBagConstraints();
        //settingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // 背景音量
        JLabel bgmLabel = new JLabel("BGM 音量：", SwingConstants.CENTER);
        bgmLabel.setFont(new Font("", Font.BOLD, 24));
        bgmSlider = new JSlider(0, 100, soundManager.getBGMVolume()); // 初始值設為 50
        bgmSlider.setPreferredSize(new Dimension(150, 30));
        bgmLabelValue = new JLabel("" + soundManager.getBGMVolume());
        bgmSlider.addChangeListener(e -> {
            soundManager.setBGMVolume(bgmSlider.getValue());
            bgmLabelValue.setText(String.valueOf(bgmSlider.getValue()));
        });

        // 特效音量
        JLabel sfxLabel = new JLabel("特效音量：");
        sfxLabel.setFont(new Font("", Font.BOLD, 24));
        sfxSlider = new JSlider(0, 100, soundManager.getComboVolume());
        sfxSlider.setPreferredSize(new Dimension(150, 30));
        sfxLabelValue = new JLabel(String.valueOf(soundManager.getComboVolume()));
        sfxSlider.addChangeListener(e -> {
            int value = sfxSlider.getValue();
            sfxLabelValue.setText(String.valueOf(value));
            soundManager.setComboVolume(value); // 呼叫 Piano 類的設定
            soundManager.setSFXVolume(value,"put");
        });

        JButton backMenu = new JButton("Back");
        backMenu.setFont(new Font("Arial", Font.PLAIN, 24));
        backMenu.setAlignmentX(Component.CENTER_ALIGNMENT);  // 使按鈕水平居中
        backMenu.addActionListener(e -> app.backToMenu());

        gbc.insets = new Insets(5, 5, 5, 5); // 加點間距
        // 第 1 行：BGM 音量
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

        // 第 2 行：SFX 音量
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

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(backMenu, gbc);
    }
}
