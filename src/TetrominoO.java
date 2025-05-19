import java.awt.Color;

public class TetrominoO extends Tetromino {
    public TetrominoO(int x, int y) {
        color = Color.YELLOW;
        cells[0] = new Cell(x, y, color);
        cells[1] = new Cell(x + 1, y, color);
        cells[2] = new Cell(x, y + 1, color);
        cells[3] = new Cell(x + 1, y + 1, color);
    }

    @Override
    public void rotate() {
        // 方塊不旋轉
    }
    @Override
    public Tetromino cloneAt(int x, int y) {
        return new TetrominoO(x, y);
    }
}
