import java.awt.Color;

public class TetrominoJ extends Tetromino {
    public TetrominoJ(int startX, int startY) {
        color = Color.BLUE;
        cells[0] = new Cell(startX + 1, startY, color);
        cells[1] = new Cell(startX, startY, color);
        cells[2] = new Cell(startX - 1, startY, color);
        cells[3] = new Cell(startX - 1, startY - 1, color);
        s=1;
    }
    
    
    public void rotate() {
        if(s == 0){
            cells[0].setX(cells[0].getX()+1);
            cells[1].setX(cells[1].getX());
            cells[2].setX(cells[2].getX()-1);
            cells[3].setX(cells[3].getX());

            cells[0].setY(cells[0].getY()+1);
            cells[1].setY(cells[1].getY());
            cells[2].setY(cells[2].getY()-1);
            cells[3].setY(cells[3].getY()-2);
            s=1;
        }
        else if(s == 1){
            cells[0].setX(cells[0].getX()-1);
            cells[1].setX(cells[1].getX());
            cells[2].setX(cells[2].getX()+1);
            cells[3].setX(cells[3].getX()+2);

            cells[0].setY(cells[0].getY()+1);
            cells[1].setY(cells[1].getY());
            cells[2].setY(cells[2].getY()-1);
            cells[3].setY(cells[3].getY());
            s=2;
        }
        else if(s == 2){
            cells[0].setX(cells[0].getX()-1);
            cells[1].setX(cells[1].getX());
            cells[2].setX(cells[2].getX()+1);
            cells[3].setX(cells[3].getX());

            cells[0].setY(cells[0].getY()-1);
            cells[1].setY(cells[1].getY());
            cells[2].setY(cells[2].getY()+1);
            cells[3].setY(cells[3].getY()+2);
            s=3;
        }
        else if(s == 3){
            cells[0].setX(cells[0].getX()+1);
            cells[1].setX(cells[1].getX());
            cells[2].setX(cells[2].getX()-1);
            cells[3].setX(cells[3].getX()-2);

            cells[0].setY(cells[0].getY()-1);
            cells[1].setY(cells[1].getY());
            cells[2].setY(cells[2].getY()+1);
            cells[3].setY(cells[3].getY());
            s=0;
        }
    }
    public void rotateBack () {
        rotate();
        rotate();
        rotate();
    }
    @Override
    public Tetromino cloneAt(int x, int y) {
        return new TetrominoJ(x, y);
    }
}