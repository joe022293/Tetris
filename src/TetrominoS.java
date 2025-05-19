import java.awt.Color;

public class TetrominoS extends Tetromino {
    public TetrominoS(int x, int y) {
        color = Color.GREEN;
        cells[0] = new Cell(x + 1, y, color);
        cells[1] = new Cell(x, y, color);
        cells[2] = new Cell(x, y + 1, color);
        cells[3] = new Cell(x - 1, y + 1, color);
    }

    @Override
    public void rotate() {
        if(s == 0){
            cells[0].setX(cells[1].getX());
            cells[1].setX(cells[1].getX());
            cells[2].setX(cells[1].getX()+1);
            cells[3].setX(cells[1].getX()+1);

            cells[0].setY(cells[1].getY()-1);
            cells[1].setY(cells[1].getY());
            cells[2].setY(cells[1].getY());
            cells[3].setY(cells[1].getY()+1);
            s=1;
        }
        else{
            cells[0].setX(cells[1].getX()+1);
            cells[1].setX(cells[1].getX());
            cells[2].setX(cells[1].getX());
            cells[3].setX(cells[1].getX()-1);

            cells[0].setY(cells[1].getY());
            cells[1].setY(cells[1].getY());
            cells[2].setY(cells[1].getY()+1);
            cells[3].setY(cells[1].getY()+1);
            s=0;
        }
    }
    @Override
    public Tetromino cloneAt(int x, int y) {
        return new TetrominoS(x, y);
    }
}
