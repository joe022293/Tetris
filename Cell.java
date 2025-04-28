
import java.awt.Color;

public class Cell {
    private int myX, myY;
    private Color mycolor;
    private boolean isFilled;

    public Cell(int x, int y, Color color) {
        myX = x;
        myY = y;
        mycolor = color;
        this.isFilled = true;
    }
    public Color getColor()
    {
        return mycolor;
    }
    public void setColor(Color c)
    {
        mycolor = c;
    }
    public int getX()
    {
        return myX;
    }
    public int getY()
    {
        return myY;
    }
    public void setX(int x)
    {
        myX=x;
    }
    public void setY(int y)
    {
        myY=y;
    }
}