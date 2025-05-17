import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StringFader {
    private float alpha = 1.0f;  // 初始透明度為 1.0f
    private final int duration = 1000; // 動畫持續時間（毫秒）
    private long startTime = 0;
    private Timer fadeTimer;
    private boolean fading = false;
    private String str;

    public StringFader(String s) {
        str = s;
        fadeTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fading) {
                    updateAlpha();
                }
            }
        });
    }

    // 啟動淡出效果
    public void triggerFade() {
        startTime = System.currentTimeMillis();
        alpha = 1.0f;
        fading = true;
        fadeTimer.start();  // 啟動定時器
    }

    // 更新透明度
    private void updateAlpha() {
        long now = System.currentTimeMillis();
        float progress = (now - startTime) / (float) duration;

        if (progress >= 1.0f) {
            alpha = 0.0f;
            fading = false;
            fadeTimer.stop();  // 結束淡出
        } else {
            alpha = 1.0f - progress;  // 透明度逐漸降低
        }
    }

    // 根據透明度繪製 T-Spin
    public void draw(Graphics2D g, int x, int y, Color c) {
        if (!fading) return;

        Composite old = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.setColor(c);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(str, x, y);
        g.setComposite(old);
    }
    public void draw(Graphics2D g, int x, int y, Color c, int size) {
        if (!fading) return;

        Composite old = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, size));
        g.drawString(str, x, y);
        g.setComposite(old);
    }

    // 檢查是否還在淡出
    public boolean isFading() {
        return fading;
    }
    public void setString(String s){
        str = s;
    }
}
