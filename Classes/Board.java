package Classes;
import java.util.*;

public class Board 
{
    private final int numRows;
    private final int numCols;
    private static int numFlows;
    private static int[][] grid;
    private final int MINLENGTH;
    private final int MAXLENGTH;
    private static ArrayList<Tail> tails;

    public Board(int row, int col) {
        numRows = row;
        numCols = col;
        numFlows = 0;
        grid = new int[numRows][numCols];
        MINLENGTH = 2;
        MAXLENGTH = Math.max(numRows, numCols);
        tails = new ArrayList<Tail>();
    }





    public Board(int board[][])
    {
        numRows = board.length;
        numCols = board[0].length;
        numFlows = 0;
        grid = board;
        MINLENGTH = 2;
        MAXLENGTH = Math.max(numRows, numCols);
    }





    public void instantiateGrid(int numFlows)
    {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int numSpaces = numRows * numCols;
        int length = numSpaces / numFlows;
        this.numFlows = numFlows;

        if (! (length >= MINLENGTH && length <= MAXLENGTH))
        {
            System.out.println("Invalid number of flows.");
            return;
        }

        for (int i = 0; i < numFlows; i++) //number of colors for each flow
            colors.add(i+1);


            for (int i = 0; i < numRows; i++)
            {
                for (int j = 0; j < numCols; j++)
                {
                    int color; //randomly choose color
                        
                    boolean direction; //true = horizontal, false = vertical
                    if ( (int) (Math.random() * 2) == 0) direction = true;
                    else direction = false;

                    if (i == 0 && j == 0 && numSpaces % numFlows != 0) //ensures that longest flow is initialize first
                    {
                        int max = (numSpaces % numFlows) + length;
                        color = colors.remove((int) (Math.random() * colors.size())); //randomly choose color
                        
                        int x = i, y = j, itr = 0;
                        while (x < numRows && y < numCols && itr < max) //initializes the flow with a longest length first
                        {
                            if (x == numRows - 1 && y == 0)
                            {
                                direction = true; //if x is at bottom left corner, switch direction from false --> true
                                
                            }

                            if (y == numCols - 1 && x == 0)
                            {
                                direction = false; //if y is at top right corner, switch direction from true --> false
                                
                            }

                            if (direction) //horizontal
                            {
                                grid[x][y] = color;
                                if (itr == 0 || itr == max - 1)
                                { //set tails
                                    grid[x][y] = -color;
                                    tails.add(new Tail(x,y));
                                }
                                y++;
                            }

                            else //vertical
                            {
                                grid[x][y] = color;
                                if (itr == 0 || itr == max - 1)
                                { //set tails
                                    grid[x][y] = -color;
                                    tails.add(new Tail(x,y));
                                }
                                x++;
                            }
                            itr++;
                        }
                      //System.out.println(this);  
                    }
                    
        //--------------------------------------------------------------------------------------------------------
                else if (grid[i][j] == 0)
                {  
                    if (colors.size() <= 0)
                        continue;
                    else
                        color = colors.remove((int) (Math.random() * colors.size())); //randomly choose color

                    if (direction)
                    {
                        if (j + length > numCols)
                        {
                            direction = false;
                        }
                             
                        
                        else 
                        {
                            for (int check = j; check < j + length; check++)
                            {
                                if (grid[i][check] != 0) 
                                {
                                    direction = false; //change direction
                                    check = j + length; //break
                                }
                            }
                        }
                    }

                    if (!direction)
                    {
                        if (i + length > numRows)
                        {
                            direction = true;
                        }
                            
                        
                        else 
                        {
                            for (int check = i; check < i + length; check++)
                            {
                                if (grid[check][j] != 0) 
                                {
                                    direction = true;
                                    check = i + length;
                                }
                            }
                        }   
                    }

                    /*System.out.println("at: "+ i + " " + j + "  direction: " + direction);
                    System.out.println("before:");
                    System.out.println(this); debugging*/ 


                    if (i + length > numRows && j + length > numCols) //account for irregularly shaped flow --> may cause some puzzles to have more than 1 solution
                    {   
                        int itr = 0;
                        for (int x = i; x < numRows; x++)
                        {
                            for (int y = j; y < numCols; y++)
                            {
                                if (grid[x][y] == 0)
                                {
                                    grid[x][y] = color;
                                    if (itr == 0 || itr == length-1)
                                    {
                                        grid[x][y] = -color;
                                        tails.add(new Tail(x,y));
                                    }
                                    itr++;
                                }
                            }
                        }

                    } 

                    else if (direction)
                    {
                        int x = i; //horizontal
                            for (int y = j; y < j + length; y++)
                            {
                                grid[x][y] = color;
                                if (y == j || y == j + length - 1)
                                {
                                    grid[x][y] = -color;
                                    tails.add(new Tail(x,y));
                                }
                            }
                        
                    }

                    else if (!direction)
                    {
                        int y = j; //vertical
                            for (int x = i; x < i + length; x++)
                            {
                                grid[x][y] = color;
                                if (x == i || x == i + length - 1)
                                {
                                    grid[x][y] = -color;
                                    tails.add(new Tail(x,y));
                                }
                            }
                        
                    } 
                        
                }
                        
                        //System.out.println("after");
                        //System.out.print(this + "\n"); also debugging
                }
            }
        
            
    }





    public static int[][] getGrid() {
        return grid;
    }





    public void setGrid(int row, int col, int val) {
        int numRows = grid.length;
        int numCols = grid[0].length;

        // grid wraps so its impossible for the row and col index to be out of bounds :D
        row = ((row % numRows) + numRows) % numRows;
        col = ((col % numCols) + numCols) % numCols;

        grid[row][col] = val;
    }






    public void randomizeGrid() 
    {
        for (int itr = 0; itr < tails.size() * ((numRows * numCols) / numFlows); itr++)
        {
            for (int t = 0; t < ((numRows * numCols) / numFlows); t++)
            {
                Tail tail = tails.get(itr % tails.size());
                int x = tail.getX();
                int y = tail.getY();

                        int direction = searchTail(x,y);
                        //System.out.println("at ["+ x + "][" + y + "] "+"\n" + this);
                        
                        switch(direction)
                        {
                            case 1: swap(x, y, 1);
                            break;

                            case 2: swap(x, y, 2);
                            break;

                            case 3: swap(x, y, 3);
                            break;

                            case 4: swap(x, y, 4);
                            break;
                        } 
            }
        }
    }





    public int searchTail(int x, int y) //searches for a valid tail to swap with
    {
        //1 north, 2 east, 3 south, 4 west
        int direction = 0;
        int range = 0; //creates randomization
        int currentTail;

        if (x < 0 || x > numRows - 1 || y < 0 || y > numCols - 1)
            return direction;
        
        currentTail = Math.abs(grid[x][y]); //positive
        
                if(x >= 1) 
                {
                    //NORTH
                    if (grid[x-1][y] < 0 && grid[x-1][y] != grid[x][y] && checkAdjacent(x-1, y, currentTail))
                    {
                        range++;

                        if ( (int) (Math.random() * range) == 0)
                            direction = 1;
                    }
                }
                    

                
                    //SOUTH
                if (x < numRows - 1)
                {
                    if (grid[x+1][y] < 0 && grid[x+1][y] != grid[x][y] && checkAdjacent(x+1, y, currentTail))
                    {
                        range++;
                        
                        if ( (int) (Math.random() * range) == 0)
                            direction = 3;
                    }
                }    



                    //EAST
                if (y < numCols - 1)
                { 
                    if (grid[x][y+1] < 0 && grid[x][y+1] != grid[x][y] && checkAdjacent(x, y+1, currentTail))
                    {
                        range++;

                        if ( (int) (Math.random() * range) == 0)
                            direction = 2;
                    }
                }
                    

                
                    //WEST
                if (y >= 1)  
                {
                    if (grid[x][y-1] < 0 && grid[x][y-1] != grid[x][y] && checkAdjacent(x, y-1, currentTail))
                    {
                    range++;

                        if ( (int) (Math.random() * range) == 0)
                            direction = 4;
                    }
                }
        return direction;
    }
    



    public void swap(int x, int y, int direction)
    {
        //search to shorten flow
        int target;
        int t1 = x, t2 = y; //target coords
        //t1 t2 are always inbounds because it is called after finding a valid direction

        switch(direction)
        {
            case 1: t1--;
            break;

            case 2: t2++;
            break;

            case 3: t1++;
            break;

            case 4: t2--;
            break;
        }
            target = -grid[t1][t2]; //the target is the parts of the flow that are not the tail (positive vals)

            if (flowLength(t1, t2) <= MINLENGTH)
                return;

            for (int i = -1, j = -1; i <= 1 && j <= 1; i+= 2, j+=2)
            {
                boolean cont = true;

                try 
                {
                //set the tail of the shortened flow
                    if (grid[t1+i][t2] == target)
                    {
                        grid[t1+i][t2] = -target; 
                        Tail.update(t1, t2, t1+i, t2, tails);
                        //System.out.println(tails);
                        cont = false; //prevent proceeding statement from running
                        i = 2; //break immediately so that more tails are not created
                    }
                }

                catch(Exception e) {}
               
                try 
                {
                    if (grid[t1][t2+j] == target && cont)
                    {  
                        grid[t1][t2+j] = -target;  
                        Tail.update(t1, t2, t1, t2+j, tails); 
                        //System.out.println(tails);
                    }
                }

                catch(Exception e) {}
            }
                

            grid[x][y] = -grid[x][y]; //negate current tail to make positive
            grid[t1][t2] = -grid[x][y]; //set new tail
            Tail.update(x, y, t1, t2, tails);
            //System.out.println(tails);
            

            
    }


    public int flowLength(int x, int y)
    {
        int target = Math.abs(grid[x][y]);
        int length = 0;


        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                if (Math.abs(grid[i][j]) == target)
                    length++;
        
        return length;
    }

    /* checks if there are more than 2 adjacent flow tiles with value val around an area
     this (theoretically) ensures that every flow puzzle has no empty spaces (i think, honestly idk if it does)
    */
    public boolean checkAdjacent(int x, int y, int val) 
    {
        int adjacent = 0;
        //val is positive

        if (x >= 1)
            if (Math.abs(grid[x-1][y]) == val)
                adjacent++;
        
        if (x < numRows - 1)
            if (Math.abs(grid[x+1][y]) == val)
                adjacent++;

        if (y >= 1)
            if (Math.abs(grid[x][y-1]) == val)
                adjacent++;
        
        if (y < numCols - 1)
            if (Math.abs(grid[x][y+1]) == val)
                adjacent++;
        
        //System.out.print("adjacent: "+ adjacent +" \n"); //FOR DEBUGGING
        return (adjacent <= 1);
    }

    //sets all positive values -> 0
    public void cleanGrid()
    {
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                if (grid[i][j] > 0)
                    grid[i][j] = 0;
    }



    @Override
    public String toString()
    {
        String result = "";
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numCols; j++)
                result += grid[i][j] + " ";
        result += "\n";
        }
        return result;
    }

    public static int getNumFlows() {return numFlows;}

    public int getTileSize(int numcol) {
        int goal = 800;
        return goal / numcol;
    }

}