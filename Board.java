import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int rows = 20;
    private final int cols = 10;
    private Cell[][] grid = new Cell[rows][cols];

    public boolean isOccupied(int x, int y) {
        return grid[y][x] != null;
    }

    public boolean canMoveDown(Tetromino block) {
        for (Cell c : block.getCells()) {
            int newY = c.getY() + 1;
            if (newY >= rows || (grid[newY][c.getX()] != null)) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveRight(Tetromino block) {
        for (Cell c : block.getCells()) {
            int newX = c.getX() + 1;
            // 檢查是否超出右邊界，或右邊是否已經有方塊
            if (newX >= cols || (newX < cols && grid[c.getY()][newX] != null)) {
                return false;
            }
        }
        return true;
    }

    // 檢查是否能向左移動
    public boolean canMoveLeft(Tetromino block) {
        for (Cell c : block.getCells()) {
            int newX = c.getX() - 1;
            // 檢查是否超出左邊界，或左邊是否已經有方塊
            if (newX < 0 || (newX >= 0 && grid[c.getY()][newX] != null)) {
                return false;
            }
        }
        return true;
    }
    
    public void addBlock(Tetromino block) {
        for (Cell c : block.getCells()) {
            grid[c.getY()][c.getX()] = c;
        }
    }
    public void clearFullRows() {
        List<Integer> fullRows = getFullRows();
        for (int row : fullRows) {
            removeRow(row);
            shiftDown(row);
        }
    }
    private List<Integer> getFullRows() {
        List<Integer> fullRows = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            boolean full = true;
            for (int x = 0; x < cols; x++) {
                if (grid[y][x] == null) {
                    full = false;
                    break;
                }
            }
            if (full) {
                fullRows.add(y);
            }
        }
        return fullRows;
    }
    private void removeRow(int row) {
        for (int x = 0; x < cols; x++) {
            grid[row][x] = null;
        }
    }
    private void shiftDown(int fromRow) {
        for (int y = fromRow; y > 0; y--) {
            for (int x = 0; x < cols; x++) {
                grid[y][x] = grid[y - 1][x];
                if (grid[y][x] != null) {
                    grid[y][x].setY(y); // 同步 Cell 的 Y 座標
                }
            }
        }
    
        // 最上面一行清空
        for (int x = 0; x < cols; x++) {
            grid[0][x] = null;
        }
    }
    public Cell getCell(int x, int y) {
        return grid[y][x];
    }

    // 還可以加碰撞檢查、邊界限制等
}
