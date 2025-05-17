import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.Instant;
import javax.swing.*;

public class TimeChallengeGame extends TetrisGame {
    private int linesCleared = 0;
    private Instant startTime;
    private Timer gameTimer;
    private double elapsedSeconds = 0.0;
    private final double timeLimit = 30.0; // 60 秒限制

    public TimeChallengeGame(TetrisApp app, SoundManager sm) {
        super(app, sm, false); // 不自動倒數
        startTimer();
        startCountdown(); // 子類建構子最後呼叫
    }

    private void startTimer() {
        gameTimer = new Timer(10, e -> {
            elapsedSeconds = Duration.between(startTime, Instant.now()).toMillis() / 1000.0;
            repaint();

            if (elapsedSeconds >= timeLimit) {
                endGame();
            }
        });
        gameTimer.start();
    }

    private void drawTime(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        double remaining = Math.max(0.0, timeLimit - elapsedSeconds);
        g.drawString(String.format("Time Left: %.2fs", remaining), xgrid + 80, 90);
    }

    private void drawLinesNum(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Lines: " + linesCleared, xgrid + 100, 60);
    }

    private void endGame() {
        gameTimer.stop();
        timer.stop();  // 停止遊戲邏輯更新

        JOptionPane.showMessageDialog(this,
            "時間到！你總共消除了 " + linesCleared + " 行",
            "限時 1 分鐘挑戰結束",
            JOptionPane.INFORMATION_MESSAGE);
        app.backToMenu();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTime(g);
        drawLinesNum(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        linesCleared += clearLinesNum;
        // 不用手動觸發 endGame()，因為計時器會處理
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
