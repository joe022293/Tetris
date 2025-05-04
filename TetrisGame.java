import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javax.swing.*;

public class TetrisGame extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private Board board;
    private Tetromino currentBlock;
    private TetrisApp app;
    private Queue<Tetromino> nextQueue = new LinkedList<>();
    

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
        int x = 4, y = 0;
    
        // 如果 nextQueue 少於 5 個方塊，填充新方塊
        while (nextQueue.size() < 5) {
            int type = rand.nextInt(7); // 0 到 6
            Tetromino newBlock = createBlock(x, y, type);
            nextQueue.add(newBlock);
        }
    
        // 獲取下一個方塊
        currentBlock = nextQueue.poll();
    
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

    public void actionPerformed(ActionEvent e) {
        if (board.canMoveDown(currentBlock)) {
            currentBlock.moveDown();
        } else {
            board.addBlock(currentBlock);  // 把現在的方塊固定到 board 上
            board.clearFullRows();
            spawnNewBlock();                // 產生新的方塊
        }
        repaint();
    }

    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 畫目前方塊
        for (int y = 0; y < 22; y++) {
            for (int x = 0; x < 10; x++) {
                Cell cell = board.getCell(x, y);
                if (cell != null) {
                    g.setColor(cell.getColor());
                    g.fillRect(x * 30, y * 30, 30, 30);
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(x * 30, y * 30, 30, 30);
                }
            }
        }
        for (Cell c : currentBlock.getCells()) {
            g.setColor(c.getColor());
            g.fillRect(c.getX() * 30, c.getY() * 30, 30, 30);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(c.getX() * 30, c.getY() * 30, 30, 30);
        }
        g.setColor(Color.darkGray);
        for (int x = 0; x <= 10; x++) {
            g.drawLine(x * 30, getHeight() - 20*30, x * 30, getHeight());
        }
        for (int y = 2; y <= getHeight()/20; y++) {
            g.drawLine(0, y * 30, 300, y * 30);
        }
        // 顯示未來方塊
        g.setColor(Color.WHITE);
        g.drawString("Next:", 330, 20);
        int index = 0;
        for (Tetromino t : nextQueue) {
            for (Cell c : t.getCells()) {
                g.setColor(c.getColor());
                int drawX = 330 + (c.getX() - 4) * 20;
                int drawY = 40 + index * 80 + c.getY() * 20;
                g.fillRect(drawX, drawY, 20, 20);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(drawX, drawY, 20, 20);
            }
            index++;
        }
    }

    // ==== 鍵盤控制 ====
    
    public void keyPressed(KeyEvent e) {
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
                // while (board.canMoveDown(currentBlock)) { 
                //     currentBlock.moveDown();
                // }
                break;
            case KeyEvent.VK_UP : 
                currentBlock.rotate();
                currentBlock.adjustPositionAfterRotate();
                break;
            case KeyEvent.VK_SPACE : 
                while (board.canMoveDown(currentBlock)) { 
                    currentBlock.moveDown();
                }
                actionPerformed(null);
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
    // public static void main(String[] args) {
    //     JFrame frame = new JFrame("Tetris Game");
    //     TetrisGame game = new TetrisGame();

    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     frame.getContentPane().add(game);
    //     frame.pack();
    //     frame.setLocationRelativeTo(null);
    //     frame.setVisible(true);
    // }
}
