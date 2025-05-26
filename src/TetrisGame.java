import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import javax.swing.*;

public class TetrisGame extends JPanel implements ActionListener, KeyListener {

    protected Timer timer;
    protected Board board;
    private Tetromino currentBlock;
    protected TetrisApp app;
    private Queue<Tetromino> nextQueue = new LinkedList<>();
    protected int xgrid = 150;
    private Tetromino heldBlock = null;  // 儲存格
    private boolean canHold = true;      // 是否可以使用 Hold    
    private long lockDelay = 500;
    private long lockStartTime = -1;
    private boolean isTouchingGround = false;
    private boolean isSpace = false;
    private Queue<Integer> blockBag = new LinkedList<>();
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;
    private boolean isDownPressed = false;
    private Timer moveTimer;
    private int moveInterval = 80;
    private int leftHoldFrames = 0;
    private int rightHoldFrames = 0;
    private int movegap = 1;
    private  boolean lastIsTurn = false;
    private StringFader tSpinFader = new StringFader("T-SPIN!");
    private StringFader allClear = new StringFader("ALL Clear!");
    // private String combostr = "";  
    private StringFader combo = new StringFader("");
    private StringFader countdownString = new StringFader("");
    private StringFader go = new StringFader("GO !");
    private double shakeOffsetY = 0;
    SoundManager soundManager;
    private int comboCount = 0; // 初始為 -1 表示尚未開始 Combo
    protected int clearLinesNum = 0;
    private int countdown = 3;  // 倒數秒數
    private Timer countdownTimer;
    protected boolean isCountdown;
    private int tSpinTotal = 0;
    private int maxComboCount = 0;
    private long startTimeMillis;
    protected int linesCleared = 0;
    public TetrisGame(TetrisApp app, SoundManager s, boolean autoCountdown) {
        soundManager = s;
        this.app = app;
        setPreferredSize(new Dimension(600, 700));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        board = new Board();
        spawnNewBlock();
        timer = new Timer(500, this);
        timer.start();
        soundManager.playBGM();
        moveTimer = new Timer(moveInterval, e -> {
            if (isLeftPressed) {
                if(leftHoldFrames > movegap)
                    moveLeft();
                else
                    leftHoldFrames++;
            } else if (isRightPressed) {
                if(rightHoldFrames > movegap)
                    moveRight();
                else
                    rightHoldFrames++;
            } else if (isDownPressed) {
                moveDown();
            }
        });
        if (autoCountdown) {
            startCountdown();
        }
    }
    private Tetromino createBlock(int x, int y, int type) {
        switch (type) {
            case 0: return new TetrominoI(x, y);
            case 1: return new TetrominoO(x, y);
            case 2: return new TetrominoT(x, y);
            case 3: return new TetrominoL(x, y);
            case 4: return new TetrominoJ(x, y);
            case 5: return new TetrominoS(x, y);
            case 6: return new TetrominoZ(x, y);
        }
        return null;
    }

    private void spawnNewBlock() {
        Random rand = new Random();
        int x = 4, y = 3;
    
        // 如果 nextQueue 少於 5 個方塊，填充新方塊
        while (nextQueue.size() < 5) {
            if (blockBag.isEmpty()) {
                refillBag();  // 補充新的隨機方塊
            }
            int type = blockBag.poll(); // 0 到 6
            Tetromino newBlock = createBlock(x, y, type);
            nextQueue.add(newBlock);
        }
    
        // 獲取下一個方塊
        currentBlock = nextQueue.poll();
        canHold = true;
        board.setUseWallKick(false);
    
        // 如果當前方塊的位置有衝突，遊戲結束
        for (Cell c : currentBlock.getCells()) {
            if (board.isOccupied(c.getX(), c.getY())) {
                timer.stop();
                // 換成顯示 GameOverPanel
                app.showGameOverPanel(
                    board.Score,
                    linesCleared,
                    tSpinTotal,
                    maxComboCount,
                    (System.currentTimeMillis() - startTimeMillis) / 1000.0,
                    () -> app.startGame(), // 重新開始
                    () -> app.backToMenu() // 回主選單
                    );
                soundManager.stopBGM();
                return;
            }
        }
    }
    protected void endGame() {
        timer.stop();
        moveTimer.stop();
        soundManager.stopBGM();
        app.showGameOverPanel(
            board.Score,
            linesCleared,
            tSpinTotal,
            maxComboCount,
            (System.currentTimeMillis() - startTimeMillis) / 1000.0,
            () -> app.startGame(), // 重新開始
            () -> app.backToMenu() // 回主選單
        );
    }
    private void refillBag() {
        List<Integer> newBag = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            for (int type = 0; type < 7; type++) {
                newBag.add(type);
            }
        }
        Collections.shuffle(newBag); // 隨機打亂
        blockBag.addAll(newBag);
    }

    private void swapHold() {
        if (!canHold) return; // 一次只能 Hold 一次直到下個方塊生成
    
        Tetromino temp = heldBlock;
        heldBlock = currentBlock.cloneAt(4, 0);
        // heldBlock.resetPosition();  // 將 heldBlock 重置到原始位置 (0,4)
        
        if (temp == null) {
            spawnNewBlock(); // 沒有備份過的方塊，從 queue 補新的
        } else {
            currentBlock = temp;
            currentBlock.cloneAt(4, 0);
        }
        canHold = false;
    }

    public void actionPerformed(ActionEvent e) {
        
        if (board.canMoveDown(currentBlock)) {
            currentBlock.moveDown();
            lockStartTime = -1;
        }
        if (!board.canMoveDown(currentBlock) && !isTouchingGround) {
            isTouchingGround = true;
            lockStartTime = System.currentTimeMillis();
        }
        clearLinesNum = 0;
        if(isTouchingGround){
            long now = System.currentTimeMillis();
            if (now - lockStartTime >= lockDelay || isSpace == true) {
                soundManager.playSFX("put");
                isSpace = false;
                lockDelay = 500;
                board.addBlock(currentBlock);
                triggerShake();
                if (board.isTSpin(currentBlock) && lastIsTurn) {
                    tSpinFader.triggerFade();
                    tSpinTotal++;
                }
                clearLinesNum = board.clearFullRows();
                if(clearLinesNum>0)
                {
                    comboCount = comboCount + 1;
                    soundManager.playCombo(comboCount);
                    if(comboCount > 1){
                        combo.setString("Combo " + comboCount + " !");
                        combo.triggerFade();
                    }
                    if(comboCount > maxComboCount){
                        maxComboCount = comboCount;
                    }
                }
                else
                {
                    comboCount = 0;
                }
                
                if(board.allClear){
                    allClear.triggerFade();
                }
                spawnNewBlock();
                isTouchingGround = false;
                lockStartTime = -1;
                
                
            }
        }           // 產生新的方塊
        repaint();
        linesCleared += clearLinesNum;
    }
    public void startCountdown() {
        isCountdown = true;
        pauseAllTimers();
        countdown = 3;
        countdownString.setString(Integer.toString(countdown));
        countdownString.triggerFade();
        countdownTimer = new Timer(1000, e -> {
            countdown--;
            if(countdown > 0) {
                countdownString.setString(Integer.toString(countdown));
                countdownString.triggerFade();
                repaint();
            }
            if (countdown <= 0) {
                countdownTimer.stop();
                isCountdown = false;
                go.triggerFade();
                resumeAllTimers();
                repaint();
            }
        });
        countdownTimer.start();
    }
    public void pauseAllTimers() {
        if (timer != null && timer.isRunning()) timer.stop();
        if (moveTimer != null && moveTimer.isRunning()) moveTimer.stop();
    }

    public void resumeAllTimers() {
        if (timer != null && !timer.isRunning()) timer.start();
        startTimeMillis = System.currentTimeMillis();
        // moveTimer 只在有按鍵時才啟動，不需要強制啟動
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(0, shakeOffsetY);
        drawBoard(g2d);
        drawCurrentBlock(g2d);
        drawGridLines(g);
        drawNextQueue(g2d);
        drawHoldBlock(g2d);
        drawScore(g);
        drawGhost(g);
        tSpinFader.draw(g2d,xgrid - 120, 150, new Color(128, 0, 128));
        allClear.draw(g2d,xgrid - 130, 180, Color.CYAN);
        combo.draw(g2d, xgrid - 140, 170, Color.CYAN);
        countdownString.draw(g2d, xgrid + 120, 400, Color.CYAN, 150);
        go.draw(g2d, xgrid-5, 400, Color.CYAN, 150);
        if (tSpinFader.isFading()) 
            repaint();  // 這樣會讓淡出動畫持續
        if (allClear.isFading()) 
            repaint();  // 這樣會讓淡出動畫持續
        if(combo.isFading())
            repaint();
        if(countdownString.isFading())
            repaint();
        if(go.isFading())
            repaint();
    }   

    public void triggerShake() {
        final int totalFrames = 5;
        final double maxOffset = 4.0;
        final int[] frame = {0};
    
        Timer timer = new Timer(16, null); // 約 60fps
        timer.addActionListener(e -> {
            if (frame[0] < totalFrames) {
                double progress = (frame[0] < totalFrames / 2) ?
                        frame[0] / (double)(totalFrames / 2) :
                        (totalFrames - frame[0]) / (double)(totalFrames / 2);
                shakeOffsetY = maxOffset * progress;
                frame[0]++;
                repaint();
            } else {
                shakeOffsetY = 0;
                ((Timer) e.getSource()).stop();
                repaint();
            }
        });
        timer.start();
    }
    private void drawBlock(Graphics2D g2d, int x, int y, int size, Color color) {
        int innerOffset = size / 5; // 調整內縮大小
        Color shadowBase = new Color(0, 0, 0, 50); // 半透明黑色
        Color lightBase = color.brighter().brighter().brighter().brighter(); // 更亮的顏色用於高光
        Color veryLightBase = lightBase.brighter();
        Color borderColor = Color.BLACK;

        // 繪製外層
        g2d.setColor(color);
        g2d.fillRect(x+1, y+1, size-2, size-2);

        // 繪製外層陰影（右邊漸變）
        GradientPaint rightShadow = new GradientPaint(
                x + size - 5, y, new Color(0, 0, 0, 0),
                x + size, y, shadowBase
        );
        g2d.setPaint(rightShadow);
        g2d.fillRect(x + size - 5, y, 5, size);

        // 繪製外層陰影（下邊漸變）
        GradientPaint bottomShadow = new GradientPaint(
                x, y + size - 5, new Color(0, 0, 0, 0),
                x, y + size, shadowBase
        );
        g2d.setPaint(bottomShadow);
        g2d.fillRect(x, y + size - 5, size, 5);

        // 繪製外層高光（上邊漸變）
        GradientPaint topHighlight = new GradientPaint(
                x, y, veryLightBase,
                x, y + 5, veryLightBase
        );
        g2d.setPaint(topHighlight);
        g2d.fillRect(x, y, size, 5);

        // 繪製外層高光（左邊漸變）
        GradientPaint leftHighlight = new GradientPaint(
                x, y, veryLightBase,
                x + 5, y, veryLightBase
        );
        g2d.setPaint(leftHighlight);
        g2d.fillRect(x, y, 5, size);

        // 繪製內層
        g2d.setColor(color.brighter());
        g2d.fillRect(x + innerOffset, y + innerOffset, size - 2 * innerOffset, size - 2 * innerOffset);

        // 繪製內層陰影（右邊漸變）
        GradientPaint innerRightShadow = new GradientPaint(
                x + innerOffset + (size - 2 * innerOffset) - 5, y + innerOffset, new Color(0, 0, 0, 0),
                x + innerOffset + (size - 2 * innerOffset), y + innerOffset, shadowBase
        );
        g2d.setPaint(innerRightShadow);
        g2d.fillRect(x + innerOffset + (size - 2 * innerOffset) - 5, y + innerOffset, 5, size - 2 * innerOffset);

        // 繪製內層陰影（下邊漸變）
        GradientPaint innerBottomShadow = new GradientPaint(
                x + innerOffset, y + innerOffset + (size - 2 * innerOffset) - 5, new Color(0, 0, 0, 0),
                x + innerOffset, y + innerOffset + (size - 2 * innerOffset), shadowBase
        );
        g2d.setPaint(innerBottomShadow);
        g2d.fillRect(x + innerOffset, y + innerOffset + (size - 2 * innerOffset) - 5, size - 2 * innerOffset, 5);

        // 繪製內層高光（上邊漸變）
        GradientPaint innerTopHighlight = new GradientPaint(
                x + innerOffset, y + innerOffset, veryLightBase,
                x + innerOffset, y + innerOffset + 5, veryLightBase
        );
        g2d.setPaint(innerTopHighlight);
        g2d.fillRect(x + innerOffset, y + innerOffset, size - 2 * innerOffset, 5);

        // 繪製內層高光（左邊漸變）
        GradientPaint innerLeftHighlight = new GradientPaint(
                x + innerOffset, y + innerOffset, veryLightBase,
                x + innerOffset + 5, y + innerOffset, veryLightBase
        );
        g2d.setPaint(innerLeftHighlight);
        g2d.fillRect(x + innerOffset, y + innerOffset, 5, size - 2 * innerOffset);

        // 繪製外層黑色邊框
        g2d.setPaint(null); // 恢復單色繪製
        g2d.setColor(borderColor);
        g2d.drawRect(x, y, size - 1, size - 1);
    }

    private void drawBoard(Graphics2D g2d) {
        for (int y = 0; y < board.rows; y++) {
            for (int x = 0; x < 10; x++) {
                Cell cell = board.getCell(x, y);
                if (cell != null) 
                    drawBlock(g2d,xgrid + x * 30, y * 30, 30,cell.getColor());
                // if (cell != null) {
                //     g.setColor(cell.getColor());
                //     g.fillRect(xgrid + x * 30, y * 30, 30, 30);
                //     g.setColor(Color.DARK_GRAY);
                //     g.drawRect(xgrid + x * 30, y * 30, 30, 30);
                // }
            }
        }
    }
    
    private void drawCurrentBlock(Graphics2D g2d) {
        for (Cell c : currentBlock.getCells()) {
            drawBlock(g2d, xgrid + c.getX() * 30, c.getY() * 30,30,c.getColor());
            // g.setColor(c.getColor());
            // g.fillRect(xgrid + c.getX() * 30, c.getY() * 30, 30, 30);
            // g.setColor(Color.DARK_GRAY);
            // g.drawRect(xgrid + c.getX() * 30, c.getY() * 30, 30, 30);
        }
    }
    private void drawGridLines(Graphics g) {
        g.setColor(Color.darkGray);
        for (int x = 0; x <= 10; x++) {
            g.drawLine(xgrid + x * 30, (board.rows - 20) * 30, xgrid + x * 30, board.rows * 30);
        }
        for (int y = board.rows - 20; y <= board.rows; y++) {
            g.drawLine(xgrid, y * 30, xgrid + 300, y * 30);
        }
    }
    private void drawNextQueue(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Next:", xgrid + 330, 20);
        int index = 0;
        for (Tetromino t : nextQueue) {
            for (Cell c : t.getCells()) {
                
                // g2d.setColor(c.getColor());
                int drawX = xgrid + 350 + (c.getX() - 4) * 20;
                int drawY = 60 + index * 80 + c.getY() * 20;
                drawBlock(g2d,drawX, drawY, 20,c.getColor());
                // g.fillRect(drawX, drawY, 20, 20);
                // g.setColor(Color.DARK_GRAY);
                // g.drawRect(drawX, drawY, 20, 20);
            }
            index++;
        }
    }
    private void drawHoldBlock(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Hold:", xgrid - 80, 20);
    
        if (heldBlock != null) {
            for (Cell c : heldBlock.getCells()) {
                // g2d.setColor(c.getColor());
                int drawX_h = xgrid - 100 + (c.getX() - 4) * 20;
                int drawY_h = 50 + c.getY() * 20;
                drawBlock(g2d, drawX_h, drawY_h, 20,c.getColor());
                // g.fillRect(drawX_h, drawY_h, 20, 20);
                // g.setColor(Color.DARK_GRAY);
                // g.drawRect(drawX_h, drawY_h, 20, 20);
            }
        }
    }
    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 35));
        g.drawString("Score:" + board.Score, xgrid + 80, 30);
    }
    private void drawGhost(Graphics g) {
        TetrominoGhost ghostBlock = new TetrominoGhost(currentBlock.copyCells()); // 假設copyCells返回目前方塊的Cell
        while (board.canMoveDown(ghostBlock)) {
            ghostBlock.moveDown();  // 不停地向下移動，直到碰到地板或其他方塊
        }
        for (Cell c : ghostBlock.getCells()) {
            g.setColor(c.getColor());
            g.fillRect(xgrid + c.getX() * 30, c.getY() * 30, 30, 30);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(xgrid + c.getX() * 30, c.getY() * 30, 30, 30);
        }
    }

    // ==== 鍵盤控制 ====
    
    public void keyPressed(KeyEvent e) {
        boolean r = false;
        if(isCountdown == false){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT : 
                    isLeftPressed = true;
                    if (!moveTimer.isRunning()) {
                        moveTimer.start(); // 啟動定時器
                        moveLeft();
                    }
                    break;
                case KeyEvent.VK_RIGHT : 
                    isRightPressed = true;
                    if (!moveTimer.isRunning()) {
                        moveTimer.start(); // 啟動定時器
                        moveRight();
                    }
                    break;
                case KeyEvent.VK_DOWN : 
                    isDownPressed = true;
                    if (!moveTimer.isRunning()) {
                        moveTimer.start(); // 啟動定時器
                    }
                    break;
                case KeyEvent.VK_UP : 
                    r = board.tryRotate(currentBlock, true);
                    currentBlock.adjustPositionAfterRotate();
                    if(r)
                    {
                        lockStartTime = System.currentTimeMillis() + 300;
                        r = false;
                        lastIsTurn = true;
                    }
                    break;
                case KeyEvent.VK_Z :
                    r = board.tryRotate(currentBlock, false);
                    currentBlock.adjustPositionAfterRotate();
                    if(r)
                    {
                        lockStartTime = System.currentTimeMillis() + 300;
                        r = false;
                        lastIsTurn = true;
                    }
                    break;
                case KeyEvent.VK_SPACE : 
                    while (board.canMoveDown(currentBlock)) { 
                        currentBlock.moveDown();
                        isSpace = true;
                    }
                    lastIsTurn = false;
                    actionPerformed(null);
                    break;
                case KeyEvent.VK_C : 
                    swapHold();
                    break;
            }
        }
        repaint();
    }
    private void moveLeft() {
        if (board.canMoveLeft(currentBlock)) {
            currentBlock.moveLeft();
            repaint();
        }
    }
    
    private void moveRight() {
        if (board.canMoveRight(currentBlock)) {
            currentBlock.moveRight();
            repaint();
        }
    }
    
    private void moveDown() {
        if (board.canMoveDown(currentBlock)) {
            currentBlock.moveDown();
            lastIsTurn = false;
            repaint();
        }
    }

    public void keyTyped(KeyEvent e) {
        // 可以留空
    }
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: 
                leftHoldFrames = 0;
                isLeftPressed = false;
                break;
            case KeyEvent.VK_RIGHT: 
                rightHoldFrames = 0;
                isRightPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                isDownPressed = false;
                break;
        }
        // 如果所有按鍵都釋放，停止定時器
        if (!isLeftPressed && !isRightPressed && !isDownPressed) {
            moveTimer.stop();
        }
    }
}
