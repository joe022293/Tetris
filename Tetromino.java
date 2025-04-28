
import java.awt.Color;


public abstract class Tetromino {
    protected Cell[] cells = new Cell[4];
    protected Color color;
    public abstract void rotate(); // 每種形狀旋轉邏輯不同
    int s = 0;
    public void moveDown() {
        for (Cell cell : cells) {
            cell.setY(cell.getY() + 1);
        }
    }

    public void moveLeft() {
        for (Cell cell : cells) {
            cell.setX(cell.getX() - 1);
        }
    }
    public void moveRight() {
        for (Cell cell : cells) {
            cell.setX(cell.getX() + 1);
        }
    }
    public Cell[] getCells() {
        return cells;
    }
    public void adjustPositionAfterRotate() {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
    
        // 找到目前方塊最左、最右的 x
        for (Cell cell : cells) {
            if (cell.getX() < minX) minX = cell.getX();
            if (cell.getX() > maxX) maxX = cell.getX();
        }
    
        // 如果最左邊超出左邊界
        if (minX < 0) {
            int shift = -minX;  // 要往右移動多少
            for (Cell cell : cells) {
                cell.setX(cell.getX() + shift);
            }
        }
    
        // 如果最右邊超出右邊界
        if (maxX >= 10) {
            int shift = maxX - 9;  // 要往左移動多少
            for (Cell cell : cells) {
                cell.setX(cell.getX() - shift);
            }
        }
    }
    
    
    // public void setColor(Color c){
    //     for (Cell cell : cells) {
    //         cell.setColor(c);
    //     }
    // }
}
