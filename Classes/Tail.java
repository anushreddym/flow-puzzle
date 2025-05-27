package Classes;
import java.util.*;

public class Tail 
{
    private int x;
    private int y;
    private int board[][];
    private int color;
    private ArrayList<Tail> tails = new ArrayList<Tail>();

    public Tail(int x, int y)
    {
        this.x = x;
        this.y = y;
        board = Board.getGrid();
        color = board[x][y];
    }

    public int getX() {return  x;}

    public int getY() {return y;}

    public void setTail(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public static void update(int x1, int y1, int x2, int y2, ArrayList<Tail> tails)
    {
        for(Tail t: tails)
        {
            if (t.getX() == x1 && t.getY() == y1)
            {
                t.setTail(x2, y2);
            }
        }
    }

    public String toString()
    {
        return ("("+x+", "+y+")");
    }
    
}
