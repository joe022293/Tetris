import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.Instant;
import javax.swing.*;

public class TimeAttackGame extends TetrisGame {
    private int linesCleared = 0;
    private Instant startTime;
    private Timer gameTimer; // 用來更新畫面時間（如果你有顯示用）
    private double elapsedSeconds = 0.0;

    public TimeAttackGame(TetrisApp app, SoundManager sm) {
        super(app, sm, false); // 不自動倒數
        startTimer();
        startCountdown(); // 子類建構子最後呼叫
    }
    // 開啟計時器並每 0.1 秒更新畫面
    private void startTimer() {
        gameTimer = new Timer(10, e -> {
            elapsedSeconds = Duration.between(startTime, Instant.now()).toMillis() / 1000.0;
            repaint(); // 每 0.1 秒重繪一次畫面
        });
        gameTimer.start();
    }

    // 繪製時間顯示的方法
    private void drawTime(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(String.format("Time: %.2fs", elapsedSeconds), xgrid + 80, 90);  // 顯示時間，精度到 0.1 秒
    }
    private void drawLinesNum(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(String.format("Lines: " + linesCleared), xgrid + 100, 60);  // 顯示時間，精度到 0.1 秒
    }
    private void endGame() {
        gameTimer.stop(); // 停止時間更新
        timer.stop();     // 停止遊戲邏輯更新（你原本就有）

        Duration finalTime = Duration.between(startTime, Instant.now());
        double seconds = finalTime.toMillis() / 1000.0;
        JOptionPane.showMessageDialog(this,
            "挑戰完成！總耗時: " + String.format("%.2f", seconds) + " 秒",
            "完成 40 行！",
            JOptionPane.INFORMATION_MESSAGE);
        app.backToMenu();  // 回主選單
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  // 保留父類繪圖內容

        // ➕ 繪製額外的東西：時間、模式標籤等等
        drawTime(g); // 你自己的方法
        drawLinesNum(g);
    }
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        linesCleared += clearLinesNum;
        if (linesCleared >= 40) {
            endGame();
        }
    }
    
    @Override
    public void pauseAllTimers() {
        super.pauseAllTimers();
        if (gameTimer != null && gameTimer.isRunning()) gameTimer.stop();
    }

    @Override
    public void resumeAllTimers() {
        super.resumeAllTimers();
        if (gameTimer != null && !gameTimer.isRunning()){
            gameTimer.start();
            startTime = Instant.now();
        } 
    }
}
