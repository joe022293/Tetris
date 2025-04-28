import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TetrisGame extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private Board board;
    private Tetromino currentBlock;

    public TetrisGame() {
        setPreferredSize(new Dimension(300, 600)); // 10 cols x 30 px, 20 rows x 30 px
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        board = new Board();
        spawnNewBlock();

        timer = new Timer(500, this); // 每 500ms 下落一次
        timer.start();
    }

    private void spawnNewBlock() {
        currentBlock = new TetrominoI(4, 0); // 簡單起見先用 I 形，後續可隨機
        
    }

    public void actionPerformed(ActionEvent e) {
        if (board.canMoveDown(currentBlock)) {
            currentBlock.moveDown();
        } else {
            board.addBlock(currentBlock);  // 把現在的方塊固定到 board 上
            spawnNewBlock();                // 產生新的方塊
        }
        repaint();
    }

    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 畫目前方塊
        for (int y = 0; y < 20; y++) {
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
        // 畫已固定的格子（略）
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


    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Game");
        TetrisGame game = new TetrisGame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
