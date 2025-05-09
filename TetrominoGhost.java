import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
public class TetrominoGhost extends Tetromino {
    public TetrominoGhost(List<Cell> cells) {
        this.cells = new Cell[cells.size()];
        for (int i = 0; i < cells.size(); i++) {
            this.cells[i] = new Cell(cells.get(i).getX(), cells.get(i).getY(), new Color(255, 255, 255, 100)); // 使用半透明顏色
        }
    }

    @Override
    public void rotate() {
        // Ghost不需要旋轉邏輯
    }

    @Override
    public Tetromino cloneAt(int x, int y) {
        // 這裡可以直接返回一個新的 TetrominoGhost
        List<Cell> newCells = new ArrayList<>();
        for (Cell c : cells) {
            newCells.add(new Cell(c.getX() + x, c.getY() + y, c.getColor()));
        }
        return new TetrominoGhost(newCells);
    }
}
