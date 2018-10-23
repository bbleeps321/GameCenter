package TicTacToe;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * 3x3 grid for tic-tac-toe.
 * 2[][][]  "" = empty
 * 1[][][]  X = marked with X
 * 0[][][]  O = marked with O
 *  0 1 2
 */
public class TicTacToeBoard extends JComponent
{
    private static final int DIMENSION = 3;
    private static final String XMARKER = "X";
    private static final String OMARKER = "O";
    private static final double SIZE = 100;
    
    private Rectangle2D.Double[][] grid;
    
    private String[][] myBoard;
    
    /** Precondition: none
     * 
     * Post-Condition: initializes a tic-tac-toe board with dimensions 3x3 and all spaces empty.
     */
    public TicTacToeBoard()
    {
        myBoard = new String[DIMENSION][DIMENSION];
                
        grid = new Rectangle2D.Double[DIMENSION][DIMENSION];
        
        for (int i = 0; i < DIMENSION; i++)
        {
            for (int j = 0; j < DIMENSION; j++)
            {
                myBoard[i][j] = " ";
                grid[i][j] = new Rectangle2D.Double(i*SIZE + 20, j*SIZE + 20, SIZE, SIZE);
            }
        }
    }
    
    /**Precondition: none
     * 
     * Post-Condition: creates a tic-tac-toe board that is identical to the specified tic-tac-toe board
     */
    public TicTacToeBoard(TicTacToeBoard original)
    {
        myBoard = new String[DIMENSION][DIMENSION];
        
        for (int i = 0; i < DIMENSION; i++)
        {
            for (int j = 0; j < DIMENSION; j++)
            {
                myBoard[i][j] = original.getState(i,j);
            }
        }
    }
    
    /**Precondition: coordinates are on the board.
     * 
     * Post-Condition: marks the specified coordinate on the board with the specified "signature."
     */
    public void mark(int x, int y, String marker)
    {
        myBoard[x][y] = marker;
    }
    
    /**Precondition: none
     * 
     * Post-Condition: returns whether the game has finished or not.
     *                 if it has finished, returns the who won/if it was a draw.
     */
    public String getEnd()
    {
        if (checkVertical(XMARKER) || checkHorizontal(XMARKER) || checkDiagonal(XMARKER))
        {
            return "Player X has won!";
        }
        else if (checkVertical(OMARKER) || checkHorizontal(OMARKER) || checkDiagonal(OMARKER))
        {
            return "Player O has won!";
        }
        else if (isFilled())
        {
            return "Both players have tied!";
        }
        else
        {
            return ""; // as in game is not over
        }
    }
    
    /**Precondition: coordinate are on the board.
     * 
     * Post-Condition: returns the state of the specified coordinate on the board.
     *                 returns whether it is empty, has an X, or has an O.
     */
    public String getState(int x, int y)
    {
        return myBoard[x][y];
    }
    
    /**Precondition: none
     * 
     * Post-Condition: returns whether the vertical rows are filled with the specified marker.
     */
    private boolean checkVertical(String marker)
    {
        for (int i = 0; i < DIMENSION; i++)
        {
            if (myBoard[i][0].equals(marker) && myBoard[i][1].equals(marker) && myBoard[i][2].equals(marker))
            {
                return true;
            }
        }
        return false; // executed if "true" is never returned
    }
    
    /**Precondition: none
     * 
     * Post-Condition: returns whether the horizontal rows are filled with the specified marker.
     */
    private boolean checkHorizontal(String marker)
    {
        for (int i = 0; i < DIMENSION; i++)
        {
            if (myBoard[0][i].equals(marker) && myBoard[1][i].equals(marker) && myBoard[2][i].equals(marker))
            {
                return true;
            }
        }
        return false; // executed if "true" is never returned
    }
    
    /**Precondition: none
     * 
     * Post-Condition: returns whether the diagonals are filled with the specified marker.
     */
    private boolean checkDiagonal(String marker)
    {
        if ((myBoard[0][0].equals(marker) && myBoard[1][1].equals(marker) && myBoard[2][2].equals(marker)) ||
            (myBoard[0][2].equals(marker) && myBoard[1][1].equals(marker) && myBoard[2][0].equals(marker)))
        {
            return true;
        }
        return false; // executed if "true" is never returned
    }
    
    /**Precondition: none
     * 
     * Post-Condition: returns whether the board has been completely filled.
     */
    private boolean isFilled()
    {
        boolean filled = true; // assume is filled
        
        for (int i = 0; i < DIMENSION; i++)
        {
            for (int j = 0; j < DIMENSION; j++)
            {
                if (myBoard[i][j].equals(" ")) // if square is empty
                {
                    filled = false; // mark entire board as not filled
                }
            }
        }
        
        return filled;
    }
    
    /**Precondition: coordinates are valid.
     * 
     * Post-Condition: returns whether inputted coordinates is empty.
     */
    public boolean isEmpty(int x, int y)
    {
        return myBoard[x][y].equals(" "); // y needs to be inverted.
    }
    
    /**Precondition: coordinates are valid.
     * 
     * Post-Condition: sets the specified grid to empty (" ").
     */
    public void reset(int x, int y)
    {
        myBoard[x][y] = " ";
    }
    
    /**Precondition: none
     * 
     * Post-Condition: returns whether the coordinates are valid.
     */
    public static boolean isValidCoord(int x, int y)
    {
        return (x <= 2 && x >= 0 && y <= 2 && y >= 0);
    }
    
    /*******************************************************************Used by AI*******************************************************************/
    
    /**Precondition: coordinates are valid.
     * 
     * Post-Condition: returns a list of coordinates of all the neighbors of the specified coordinate (including the diagonally).
     */
    private ArrayList<Integer[]> getNeighbors(int x, int y)
    {
        ArrayList<Integer[]> myCoords = new ArrayList<Integer[]>();
        ArrayList<Integer> toExclude = new ArrayList<Integer>();
        
        // add surrounding eight coordinates
        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                if (!(i == 0 && j == 0) && (x + i >= 0 && x + i <= 2) && (y + j >= 0 && y + j <= 2))
                {
                    Integer[] coord = {x + i, y + j}; 
                    myCoords.add(coord);
                }
            }
        }
        
        myCoords.trimToSize();
        
        return myCoords;
    }
    
    /**Precondition: coordinates are valid.
     * 
     * Post-Condition: returns a list of coordinates of all the empty neighbors of the specified coordinate.
     */
    public ArrayList<Integer[]> getEmptyNeighbors(int x, int y)
    {
        ArrayList<Integer[]> myCoords = new ArrayList<Integer[]>();
        ArrayList<Integer[]> allCoords = getNeighbors(x,y);
        
        for (int i = 0; i < allCoords.size(); i++)
        {
            Integer[] coord = allCoords.get(i);

            if (isEmpty(coord[0], coord[1]))
            {
                myCoords.add(coord);
            }
        }
        
        return myCoords;
    }
    
    /******************************************************Used for graphics*****************************************************/
    
    public void paintComponent(Graphics2D g2)
    {
        for (int i = 0; i < DIMENSION; i++)
        {
            for (int j = 0; j < DIMENSION; j++)
            {
                g2.draw(grid[i][j]);   
            }
        }
    }
    
    /**Precondition: none
     * 
     * Post-Condition: returns the x-coordinate of the grid the coordinates are part of.
     *                 returns -1 if the coordinate is not in any of the squares.
     */
    public int getXCoor(double x, double y)
    {
        int xCoord = -1;
        for (int i = 0; i < DIMENSION; i++)
        {
            for (int j = 0; j < DIMENSION; j++)
            {
                if (grid[i][j].contains(x,y))
                {
                    xCoord = i;
                }
            }
        }
        
        return xCoord;
    }
    
    /**Precondition: none
     * 
     * Post-Condition: returns the y-coordinate of the grid the coordinates are part of.
     *                 returns -1 if the coordinate is not in any of the squares.
     */
    public int getYCoor(double x, double y)
    {
        int yCoord = -1;
        for (int i = 0; i < DIMENSION; i++)
        {
            for (int j = 0; j < DIMENSION; j++)
            {
                if (grid[i][j].contains(x,y))
                {
                    yCoord = j;
                }
            }
        }
        
        return yCoord;
    }
    
} // END CLASS