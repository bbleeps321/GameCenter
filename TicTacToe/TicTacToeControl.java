package TicTacToe;

//import
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import GameComponents.Game;

/**
 * Controls the functions of the TicTacToe game.
 * 
 * Side 0 = player X, side 1 = player O;
 */
public class TicTacToeControl extends Game
{
    //constants
    private static final int WIDTH = 350; //width of frame
    private static final int HEIGHT = 350; //height of frame
    private static final String DIRECTORY = "TicTacToe/"; //directory of game
    private static final int DIMENSION = 3; // length and width of board in cells
    private static final String[] MARKER = {"X","O"}; // valid markers
    private static final String NEWLINE = "\n"; // has effect of "ENTER" key
    
    private TicTacToeBoard myBoard; // the board played on
    
    private ArrayList<XMarker> xMarks; // list of all X's on board
    private ArrayList<OMarker> oMarks; // list of all O's on board
    
    private JComboBox playerCombo; //combobox for selecting number of players
    private JCheckBox goFirst; //check box for going first (ai only)
    
    private boolean hasAI; // if the current game is using ai or not.
    private boolean isOver; //whether or not the game is over
    
    private int aiNum; // ai's player number (if ai went first or second)
    private int turn; // which turn it is.
    private int players; // the number of players in the current game (1/2).
    
    /**Precondition: none
     * 
     * Post-Condition: initializes a tic-tac-toe game on a given frame.
     */
    public TicTacToeControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**Precondition: none
     * 
     * Post-Condition: takes in mouse presses.
     */
    public void mousePressed(MouseEvent e)
    {
        if (!isOver)
        {
            double mx = e.getX();
            double my = e.getY();
    
            int x = myBoard.getXCoor(mx,my);
            int y = DIMENSION - (1 + myBoard.getYCoor(mx,my)); // invert coordinates
            
            int side = turn%2;
                
            if (hasAI) // if one player game
            {
                if (side == aiNum)
                {
                    if (turn <= 1)
                        aiTurn(true);
                    else
                        aiTurn(false);
                }
                else
                {
                    if (x >= 0 && x <= 2 && y >= 0 && y <= 2) // are valid coordinates
                    {
                        if (myBoard.isEmpty(y,x))
                        {
                            // invert coordinates to match system in 1st quadrant
                            myBoard.mark(y,x,MARKER[side]); 
                            
                            if (side == 0)
                            {
                                XMarker newMark = new XMarker(x*100 + 20, 220 - y*100);
                                xMarks.add(newMark);
                            }
                            else
                            {
                                OMarker newMark = new OMarker(x*100 + 20, 220 - y*100);
                                oMarks.add(newMark);
                            }
    
                            turn++;
                        }
                    }
                }
            }
            else // if two player game
            {
                if (x >= 0 && x <= 2 && y >= 0 && y <= 2) // are valid coordinates
                {
                    if (myBoard.isEmpty(y,x))
                    {
                        // invert coordinates to match system in 1st quadrant
                        myBoard.mark(y,x,MARKER[side]); 
                        
                        if (side == 0)
                        {
                            XMarker newMark = new XMarker(x*100 + 20, 220 - y*100);
                            xMarks.add(newMark);
                        }
                        else
                        {
                            OMarker newMark = new OMarker(x*100 + 20, 220 - y*100);
                            oMarks.add(newMark);
                        }
    
                        turn++;
                    }
                }
            }
            
            String end = myBoard.getEnd();
        
            if (!end.equals("") || turn == 9)
                endGame();
        }
    }
    
    /*******************************Used by AI********************************/
    
    /**Precondition: none
     * 
     * Takes in whether it is the first time it is moving
     * 
     * Post-Condition: decides a move for the AI to make.
     */
    public void aiTurn(boolean first)
    {
        Random rand = new Random();
        
        /**Assign values for moves**/
        int[] winMove = getWinningMove(MARKER[aiNum]);
        int[] loseMove;
        int[] winDivineMove = getDivineMove(MARKER[aiNum]);
        int[] loseDivineMove;
        int[] centerMove = {1,1};
        int[] upperLeft = {2,0}; // coordinates inverted to correspond to GUI output
        int[] lowerLeft = {0,0};
        int[] upperRight = {2,2};
        int[] lowerRight = {0,2}; // coordinates inverted to coorespond to GUI output
        int[] specialCondMove = {-1,-1}; // initial coordinates will not actually be used
        
        boolean specialCondition = false; // if a specific condition has occurred.
        if (aiNum == 0)
        {
            loseMove = getWinningMove(MARKER[1]);
            loseDivineMove = getDivineMove(MARKER[1]);
            
            if ((((myBoard.getState(upperLeft[0], upperLeft[1])).equals(MARKER[1]) && (myBoard.getState(lowerRight[0], lowerRight[1])).equals(MARKER[1])) ||
                 ((myBoard.getState(lowerLeft[0], lowerLeft[1])).equals(MARKER[1]) && (myBoard.getState(upperRight[0], upperRight[1])).equals(MARKER[1]))) &&
                 (turn == 3)) // check if conditions for special move have been met
            {
                specialCondition = true;
            }
        }
        else
        {
            loseMove = getWinningMove(MARKER[0]);
            loseDivineMove = getDivineMove(MARKER[0]);
            
            if ((((myBoard.getState(upperLeft[0], upperLeft[1])).equals(MARKER[0]) && (myBoard.getState(lowerRight[0], lowerRight[1])).equals(MARKER[0])) ||
                 ((myBoard.getState(lowerLeft[0], lowerLeft[1])).equals(MARKER[0]) && (myBoard.getState(upperRight[0], upperRight[1])).equals(MARKER[0]))) &&
                 (turn == 3)) // check if conditions for special move have been met
            {
                specialCondition = true;
            }
        }
        
        if (specialCondition)
        {
            int[] topEdge = {2,1};
            int[] leftEdge = {1,0};
            int[] rightEdge = {1,2};
            int[] bottomEdge = {0,1};
            
            if (myBoard.isEmpty(topEdge[0], topEdge[1]))
            {
                specialCondMove = topEdge;
            }
            else if (myBoard.isEmpty(leftEdge[0], leftEdge[1]))
            {
                specialCondMove = leftEdge;
            } 
            else if (myBoard.isEmpty(rightEdge[0], rightEdge[1]))
            {
                specialCondMove = rightEdge;
            } 
            else if (myBoard.isEmpty(bottomEdge[0], bottomEdge[1]))
            {
                specialCondMove = bottomEdge;
            } 
        }
        
        /**Make the moves**/
        if (specialCondition) // if special condition has been fulfilled (rare)
        {
            aiPrint(specialCondMove[0], specialCondMove[1], MARKER[aiNum]);
        }
        else if (TicTacToeBoard.isValidCoord(winMove[0], winMove[1])) // move to win game
        {
            aiPrint(winMove[0], winMove[1], MARKER[aiNum]);
        }
        else if (TicTacToeBoard.isValidCoord(loseMove[0], loseMove[1])) // move to not lose game
        {
            aiPrint(loseMove[0], loseMove[1], MARKER[aiNum]);
        }
        else if (TicTacToeBoard.isValidCoord(winDivineMove[0], winDivineMove[1])) // move to divine move helping AI
        {
            aiPrint(winDivineMove[0], winDivineMove[1], MARKER[aiNum]);
        }
        else if (TicTacToeBoard.isValidCoord(loseDivineMove[0], loseDivineMove[1])) // move to block opponent's divine move
        {
            aiPrint(loseDivineMove[0], loseDivineMove[1], MARKER[aiNum]);
        }
        else if (!first) // move to adjacent
        {
            int[] adjacentMove = getAdjacentMove(MARKER[aiNum]);
            
            aiPrint(adjacentMove[0], adjacentMove[1], MARKER[aiNum]);
        }
        else if (myBoard.isEmpty(centerMove[0], centerMove[1])) // move to center
        {
            aiPrint(centerMove[0], centerMove[1], MARKER[aiNum]);
        }
        else if (myBoard.isEmpty(upperLeft[0], upperLeft[1])) // move to upper left
        {
            aiPrint(upperLeft[0], upperLeft[1], MARKER[aiNum]);
        }
        else if (myBoard.isEmpty(lowerLeft[0], lowerLeft[1])) // move to lower left
        {
            aiPrint(lowerLeft[0], lowerLeft[1], MARKER[aiNum]);
        }
        else if (myBoard.isEmpty(upperRight[0], upperRight[1])) // move to upper right
        {
            aiPrint(upperRight[0], upperRight[1], MARKER[aiNum]);
        }
        else if (myBoard.isEmpty(lowerRight[0], lowerRight[1])) // move to lower right
        {
            aiPrint(lowerRight[0], lowerRight[1], MARKER[aiNum]);
        }
        else // move randomly
        {
            int[] randMove = {rand.nextInt(3), rand.nextInt(3)};
            
            while (!TicTacToeBoard.isValidCoord(randMove[0], randMove[1]))
            {
                randMove[0] = rand.nextInt(3);
                randMove[1] = rand.nextInt(3);
            }
            
            aiPrint(randMove[0], randMove[1], MARKER[aiNum]);
        }
        turn++;
    }
    
    /**Precondition: marker is valid.
     * 
     * Post-Condition: returns a coordinate that, if played next by the opponent, would win the game.
     *                 returns {-1,-1} if there are no winning moves.
     */
    private int[] getWinningMove(String marker)
    {
        TicTacToeBoard tempBoard = new TicTacToeBoard(myBoard);
        
        int[] theMove = new int[2];
        
        for (int m = 0; m < DIMENSION; m++)
        {
            for (int n = 0; n < DIMENSION; n++)
            {
                int[] tempMove = {m,n};
                
                if (tempBoard.isEmpty(tempMove[0], tempMove[1]))
                {
                    tempBoard.mark(tempMove[0], tempMove[1], marker);
                    
                    String futureEnd = tempBoard.getEnd();
                    if (futureEnd.equals("Player " + marker + " has won!"))
                    {
                        theMove[0] = tempMove[0];
                        theMove[1] = tempMove[1];
                        
                        return theMove;
                    }
                    else
                    {
                        tempBoard.reset(tempMove[0], tempMove[1]);
                    }
                }
            }
        }
        // if nothing has been returned yet:
        theMove[0] = -1;
        theMove[1] = -1;
        
        return theMove;
    }
    
    /**Precondition: marker is valid
     * 
     * Post-Condition: returns a "divine move" or move that would create two winning possibilities at once.
     *                 returns {-1,-1} if no divine moves.
     */
    private int[] getDivineMove(String marker)
    {
        TicTacToeBoard tempBoard = new TicTacToeBoard(myBoard); // create a copy
        
        int[] theMove = new int[2];
        
        for (int i = 0; i < DIMENSION; i++)
        {
            for (int j = 0; j < DIMENSION; j++)
            {
                int[] tempCoord = {i,j};
                if (tempBoard.isEmpty(i,j))
                {
                    tempBoard.mark(tempCoord[0], tempCoord[1], marker);
                    if (getWinningMoveNum(marker, tempBoard) > 1) // if move will result in more than one possible location of victory
                    {
                        theMove = tempCoord;
                        return theMove;
                    }
                    tempBoard.reset(tempCoord[0], tempCoord[1]);
                }
            }
        }
        // if nothing has been returned yet:
        theMove[0] = -1;
        theMove[1] = -1;
        
        return theMove;
    }
    
    /**Precondition: AI has had at least one turn. Marker is valid.
     * 
     * Post-Condition: returns a coordinate selected from a list of all empty spaces adjacent to a marker on the current side.
     */
    private int[] getAdjacentMove(String marker)
    {
        Random rand = new Random();
        
        // find all markers on own side
        ArrayList<Integer[]> ownCoords = new ArrayList<Integer[]>();
                    
        for (int i = 0; i < DIMENSION; i++)
        {
            for (int j = 0; j < DIMENSION; j++)
            {
                if (myBoard.getState(i,j).equals(marker))
                {
                    Integer[] coord = {i,j};
                    ownCoords.add(coord);
                }
            }
        }
        
        // find all empty spaces adjacent to a marker on own side
        ArrayList<Integer[]> allEmptyCoords = new ArrayList<Integer[]>();
        
        for (int m = 0; m < ownCoords.size(); m++)
        {
            Integer[] coord = ownCoords.get(m);
            
            ArrayList<Integer[]> emptyCoords = myBoard.getEmptyNeighbors(coord[0], coord[1]);
            
            if (!(emptyCoords.size() == 0))
            {
                for (int n = 0; n < emptyCoords.size(); n++)
                {
                    allEmptyCoords.add(emptyCoords.get(n));
                }
            }
        }
        
        // randomly choose a coordinate
        int index = rand.nextInt(allEmptyCoords.size());
        Integer[] theMoveTemp = allEmptyCoords.get(index);
        int[] theMove = {theMoveTemp[0], theMoveTemp[1]};
            
        return theMove;
    }
    
    /**Precondition: marker is valid.
     * 
     * Post-Condition: returns the number of one-turn moves that would result in a win for the specified marker.
     */
    private int getWinningMoveNum(String marker, TicTacToeBoard theBoard)
    {
        TicTacToeBoard tempBoard = new TicTacToeBoard(theBoard);
        
        int moves = 0;
        
        for (int m = 0; m < DIMENSION; m++)
        {
            for (int n = 0; n < DIMENSION; n++)
            {
                int[] tempMove = {m,n};
                
                if (tempBoard.isEmpty(tempMove[0], tempMove[1]))
                {
                    tempBoard.mark(tempMove[0], tempMove[1], marker);
                    
                    String futureEnd = tempBoard.getEnd();
                    if (futureEnd.equals("Player " + marker + " has won!"))
                    {
                        moves++;
                        tempBoard.reset(tempMove[0], tempMove[1]);
                    }
                    else
                    {
                        tempBoard.reset(tempMove[0], tempMove[1]);
                    }
                }
            }
        }
        
        return moves;
    }
    
    /**Precondition: marker and coordinates are valid.
     * 
     * Post-Condition: prints out a specified marker at a specified coordinate and updates record accordingly
     */
    private void aiPrint(int x, int y, String marker)
    {
        myBoard.mark(x, y, MARKER[aiNum]);
        if (aiNum == 0)
        {
            XMarker newMark = new XMarker(y*100 + 20, 220 - x*100); // invert coordinates to match system in 1st quadrant
            xMarks.add(newMark);
        }
        else
        {
            OMarker newMark = new OMarker(y*100 + 20, 220 - x*100); // invert coordinates to match system in 1st quadrant
            oMarks.add(newMark);
        }
    }

    
    /**Required by Game**/
    /**
     * Returns a panel containing the options for this game.
     */
    public JPanel getOptionsPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        
        //initialize components
        playerCombo = new JComboBox(new Integer[] {1,2});
        playerCombo.setEditable(false);
        Border emptyBorder = BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP);
        playerCombo.setBorder(BorderFactory.createTitledBorder(emptyBorder, "Players"));
        
        playerCombo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if ((Integer)playerCombo.getSelectedItem() == 1)
                    goFirst.setEnabled(true);
                else
                    goFirst.setEnabled(false);
            }
        });
        
        goFirst = new JCheckBox("Go First");
        goFirst.setEnabled(true);
        
        //place them in panels
        JPanel playerPanel = new JPanel(new GridLayout(1,2));
        playerPanel.add(playerCombo);
        playerPanel.add(goFirst);
        
        panel.add(playerPanel, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Initializes the game play components.
     */
    protected void initializeGameplayComponents()
    {
        xMarks = new ArrayList<XMarker>();
        oMarks = new ArrayList<OMarker>();
        
        myBoard = new TicTacToeBoard();
        myBoard.addMouseListener(this);
        
        //find number of players
        players = (Integer)playerCombo.getSelectedItem();
        if (players == 1) //ai will be used
        {
            if (goFirst.isSelected())
                aiNum = 1;
            else
                aiNum = 0;
            hasAI = true;
        }
        else
        {
            hasAI = false;
        }

        turn = 0;
        isOver = false;
        
        //set cursor
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    /**
     * Runs the game process. (Not needed).
     */
    protected void runProcess() {}

    /**
     * Returns the name of this game.
     */
    public String name()
    {
        return "Tic Tac Toe";
    }
    
    /**
     * Returns a description of this game.
     */
    public String description()
    {
        return "Try to get three in a row while blocking you opponent's moves!";
    }
    
    /**
     * Ends the game.
     */
    private void endGame()
    {
        isOver = true;
    }
    
    /**
     * Resets the components.
     */
    private void reset()
    {
        //reset board
        for (int i = 0; i < DIMENSION; i++)
            for (int j = 0; j < DIMENSION; j++)
                myBoard.reset(i,j);
                
        //reset list of markers
        xMarks.clear();
        oMarks.clear();
    }
    
    /**
     * Returns whether or not this game can be scored.
     */
    public boolean isScorable()
    {
        return false;
    }
    
    /**
     * Paints this game onto the container.
     */
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        //paint background
        g2.setColor(Color.white);
        g2.fill(g2.getClipBounds());
        
        //paint board
        myBoard.paintComponent(g2);
        
        //paint marks
        for (int i = 0; i < xMarks.size(); i++)
            xMarks.get(i).paintComponent(g);
        for (int i = 0; i < oMarks.size(); i++)
            oMarks.get(i).paintComponent(g);
            
        if (isOver) //game over
        {
            g2.setColor(Color.red);
            g2.setFont(new Font("", Font.BOLD, 30));
            String end = myBoard.getEnd();
            int length = end.length();
            double textX = WIDTH/2 - length*30/4;
            double textY = HEIGHT/2 - 10;
            g2.drawString(end, (float)textX, (float)textY);
        }
    }
    
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new TicTacToeControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 155));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
} // END CLASS