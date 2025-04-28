import java.awt.Color;

public class TetrominoI extends Tetromino {

    public TetrominoI(int startX, int startY) {
        color = Color.CYAN;
        cells[0] = new Cell(startX, startY, color);
        cells[1] = new Cell(startX, startY + 1, color);
        cells[2] = new Cell(startX, startY + 2, color);
        cells[3] = new Cell(startX, startY + 3, color);
    }

    public void rotate() {
        if(s == 0){
            cells[0].setX(cells[0].getX()-1);
            cells[1].setX(cells[1].getX());
            cells[2].setX(cells[2].getX()+1);
            cells[3].setX(cells[3].getX()+2);

            cells[0].setY(cells[0].getY()+1);
            cells[1].setY(cells[1].getY());
            cells[2].setY(cells[2].getY()-1);
            cells[3].setY(cells[3].getY()-2);
            s=1;
        }
        else{
            cells[0].setX(cells[0].getX()+1);
            cells[1].setX(cells[1].getX());
            cells[2].setX(cells[2].getX()-1);
            cells[3].setX(cells[3].getX()-2);

            cells[0].setY(cells[0].getY()-1);
            cells[1].setY(cells[1].getY());
            cells[2].setY(cells[2].getY()+1);
            cells[3].setY(cells[3].getY()+2);
            s=0;
        }

    }
}