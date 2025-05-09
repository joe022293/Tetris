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

    private Timer timer;
    private Board board;
    private Tetromino currentBlock;
    private TetrisApp app;
    private Queue<Tetromino> nextQueue = new LinkedList<>();
    private int xgrid = 150;
    private Tetromino heldBlock = null;  // 儲存格
    private boolean canHold = true;      // 是否可以使用 Hold    
    private long lockDelay = 500;
    private long lockStartTime = -1;
    private boolean isTouchingGround = false;
    private boolean isSpace = false;
    private Queue<Integer> blockBag = new LinkedList<>();

    public TetrisGame(TetrisApp app) {
        this.app = app;
        setPreferredSize(new Dimension(600, 700)); // 10 cols x 30 px, 20 rows x 30 px
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        board = new Board();
        spawnNewBlock();

        timer = new Timer(500, this); // 每 500ms 下落一次
        timer.start();
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
        int x = 4, y = 2;
    
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
    
        // 如果當前方塊的位置有衝突，遊戲結束
        for (Cell c : currentBlock.getCells()) {
            if (board.isOccupied(c.getX(), c.getY())) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over!");
                app.backToMenu();  // 回主畫面
                return;
            }
        }
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
        if(isTouchingGround){
            long now = System.currentTimeMillis();
            if (now - lockStartTime >= lockDelay || isSpace == true) {
                isSpace = false;
                lockDelay = 500;
                board.addBlock(currentBlock);
                board.clearFullRows();
                spawnNewBlock();
                isTouchingGround = false;
                lockStartTime = -1;
            }
        }           // 產生新的方塊
        repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBoard(g);
        drawCurrentBlock(g);
        drawGridLines(g);
        drawNextQueue(g);
        drawHoldBlock(g);
        drawScore(g);
        drawGhost(g);
    }

    private void drawBoard(Graphics g) {
        for (int y = 0; y < board.rows; y++) {
            for (int x = 0; x < 10; x++) {
                Cell cell = board.getCell(x, y);
                if (cell != null) {
                    g.setColor(cell.getColor());
                    g.fillRect(xgrid + x * 30, y * 30, 30, 30);
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(xgrid + x * 30, y * 30, 30, 30);
                }
            }
        }
    }
    private void drawCurrentBlock(Graphics g) {
        for (Cell c : currentBlock.getCells()) {
            g.setColor(c.getColor());
            g.fillRect(xgrid + c.getX() * 30, c.getY() * 30, 30, 30);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(xgrid + c.getX() * 30, c.getY() * 30, 30, 30);
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
    private void drawNextQueue(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Next:", xgrid + 330, 20);
        int index = 0;
        for (Tetromino t : nextQueue) {
            for (Cell c : t.getCells()) {
                g.setColor(c.getColor());
                int drawX = xgrid + 350 + (c.getX() - 4) * 20;
                int drawY = 60 + index * 80 + c.getY() * 20;
                g.fillRect(drawX, drawY, 20, 20);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(drawX, drawY, 20, 20);
            }
            index++;
        }
    }
    private void drawHoldBlock(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Hold:", xgrid - 80, 20);
    
        if (heldBlock != null) {
            for (Cell c : heldBlock.getCells()) {
                g.setColor(c.getColor());
                int drawX_h = xgrid - 100 + (c.getX() - 4) * 20;
                int drawY_h = 50 + c.getY() * 20;
                g.fillRect(drawX_h, drawY_h, 20, 20);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(drawX_h, drawY_h, 20, 20);
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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT : 
                if(board.canMoveLeft(currentBlock)){
                    currentBlock.moveLeft();
                }
                break;
            case KeyEvent.VK_RIGHT : 
                if(board.canMoveRight(currentBlock)){
                    currentBlock.moveRight();
                }
                break;
            case KeyEvent.VK_DOWN : 
                if (board.canMoveDown(currentBlock)){
                    currentBlock.moveDown();
                }
                break;
            case KeyEvent.VK_UP : 
                r = board.tryRotate(currentBlock);
                currentBlock.adjustPositionAfterRotate();
                if(r)
                {
                    lockStartTime = System.currentTimeMillis() + 500;
                    r = false;
                }
                break;
            case KeyEvent.VK_SPACE : 
                while (board.canMoveDown(currentBlock)) { 
                    currentBlock.moveDown();
                    isSpace = true;
                }
                actionPerformed(null);
                break;
            case KeyEvent.VK_C : 
                swapHold();
                break;
        }
        repaint();
    }

    public void keyTyped(KeyEvent e) {
        // 可以留空
    }
    public void keyReleased(KeyEvent e) {
        // 可以留空，或寫上你要的邏輯
    }
}
