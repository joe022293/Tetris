import java.util.ArrayList;
import java.util.List;
public class Board {
    public final int rows = 24;
    private final int cols = 10;
    private Cell[][] grid = new Cell[rows][cols];
    public int Score;
    public boolean allClear = false;

    private boolean UsedWallKick = false;

    public boolean didUseWallKick() {
        return UsedWallKick;
    }
    public void setUseWallKick(boolean b) {
        UsedWallKick = b;
    }
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
        allClear = checkAllClear();
    }
    public boolean checkAllClear() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (grid[y][x] != null) {  // 如果有任何方塊，則不是 All Clear
                    return false;
                }
            }
        }
        return true;  // 所有方格都為 null，表示達成 All Clear
    }

    public boolean tryRotate(Tetromino block) {
        // 先備份原本的位置
        List<Cell> originalCells = block.copyCells();
    
        // 嘗試旋轉
        block.rotate();
    
        // 模擬旋轉後的位置
        List<Cell> rotatedCells = block.copyCells();
    
        // 旋轉後，嘗試用不同 offset 找合法位置
        int[][] offsets = {
            {0, 0}, {-1, 0}, {1, 0}, {-2, 0}, {2, 0}, {0, 1}, {0, 2}, {1, 1}, {-1, 1}, {1, 2}, {-1, 2}, {0, -1}, {0, -2}, {0, -3}
        };
    
        for (int[] offset : offsets) {
            if (canPlace(rotatedCells, offset[0], offset[1])) {
                if((offset[0] == 1 && offset[1] == 1) || (offset[0] == -1 && offset[1] == 1) || (offset[0] == 1 && offset[1] == 2) || (offset[0] == -1 && offset[1] == 2))
                    UsedWallKick = true;
                block.movedxdy(offset[0], offset[1]);  // 成功移動
                return true;
            }
        }
    
        // 沒找到合法位置，就還原旋轉
        block.setCells(originalCells);
        return false;
    }
    
    // 檢查 offset 後是否能合法放置
    private boolean canPlace(List<Cell> cells, int offsetX, int offsetY) {
        for (Cell c : cells) {
            int x = c.getX() + offsetX;
            int y = c.getY() + offsetY;
    
            if (x < 0 || x >= cols || y < 0 || y >= rows) return false;
            if (grid[y][x] != null) return false;
        }
        return true;
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
        switch (fullRows.size()){
            case 1:
                Score = Score + 100;
                break;
            case 2:
                Score = Score + 300;
                break;
            case 3:
                Score = Score + 500;
                break;
            case 4:
                Score = Score + 800;
                break;
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
    public boolean isTSpin(Tetromino block) {
        if (!(block instanceof TetrominoT)) return false;
        
    
        Cell center = block.getCells()[3];// 中心座標，根據你的 TetrominoT 實作而定
        int x = center.getX();
        int y = center.getY();
    
        // 檢查四個角落
        int occupiedCorners = 0;
        if (isOccupiedSafe(x - 1, y - 1)) occupiedCorners++;
        if (isOccupiedSafe(x + 1, y - 1)) occupiedCorners++;
        if (isOccupiedSafe(x - 1, y + 1)) occupiedCorners++;
        if (isOccupiedSafe(x + 1, y + 1)) occupiedCorners++;

        if(occupiedCorners >= 3 || didUseWallKick())
            return true;
        else
            return false;
    }
    
    private boolean isOccupiedSafe(int x, int y) {
        return x < 0 || x >= cols || y < 0 || y >= rows || grid[y][x] != null;
    }
    

    // 還可以加碰撞檢查、邊界限制等
}
