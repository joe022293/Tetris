import java.awt.Color;

public class TetrominoT extends Tetromino {
    public TetrominoT(int x, int y) {
        color = Color.MAGENTA;
        cells[0] = new Cell(x - 1, y, color);
        cells[1] = new Cell(x, y - 1, color);
        cells[2] = new Cell(x + 1, y, color);
        cells[3] = new Cell(x, y, color);
        s=0;
    }

    @Override
    public void rotate() {
        if(s == 0){
            cells[0].setX(cells[1].getX());
            cells[1].setX(cells[2].getX());
            cells[2].setX(cells[3].getX());
            cells[3].setX(cells[3].getX());

            cells[0].setY(cells[1].getY());
            cells[1].setY(cells[2].getY());
            cells[2].setY(cells[3].getY()+1);
            cells[3].setY(cells[3].getY());
            s=1;
        }
        else if(s == 1){
            cells[0].setX(cells[1].getX());
            cells[1].setX(cells[2].getX());
            cells[2].setX(cells[3].getX()-1);
            cells[3].setX(cells[3].getX());

            cells[0].setY(cells[1].getY());
            cells[1].setY(cells[2].getY());
            cells[2].setY(cells[3].getY());
            cells[3].setY(cells[3].getY());
            s=2;
        }
        else if(s == 2){
            cells[0].setX(cells[1].getX());
            cells[1].setX(cells[2].getX());
            cells[2].setX(cells[3].getX());
            cells[3].setX(cells[3].getX());

            cells[0].setY(cells[1].getY());
            cells[1].setY(cells[2].getY());
            cells[2].setY(cells[3].getY()-1);
            cells[3].setY(cells[3].getY());
            s=3;
        }
        else if(s == 3){
            cells[0].setX(cells[1].getX());
            cells[1].setX(cells[2].getX());
            cells[2].setX(cells[3].getX()+1);
            cells[3].setX(cells[3].getX());

            cells[0].setY(cells[1].getY());
            cells[1].setY(cells[2].getY());
            cells[2].setY(cells[3].getY());
            cells[3].setY(cells[3].getY());
            s=0;
        }
    }
    @Override
    public void rotateBack() {
        rotate();
        rotate();
        rotate();
    }
    @Override
    public Tetromino cloneAt(int x, int y) {
        return new TetrominoT(x, y);
    }
}
