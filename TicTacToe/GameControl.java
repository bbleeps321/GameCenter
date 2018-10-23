package TicTacToe;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Prints out the board and controls flow of the game.
 * 
 * Side 0 = player X, side 1 = player O;
 */
public class GameControl extends JPanel implements MouseListener, KeyListener
{
    private static final int DIMENSION = 3; // length and width of board in cells
    private static final String[] MARKER = {"X","O"}; // valid markers
    private static final String NEWLINE = "\n"; // has effect of "ENTER" key
    
    private TicTacToeBoard myBoard; // the board played on
    
    private ArrayList<XMarker> xMarks; // list of all X's on board
    private ArrayList<OMarker> oMarks; // list of all O's on board

    private JFrame myFrame; // frame application is in
    private JTextArea record; // text displaying all moves so far in the game (uneditable)
    private JTextField prompt; // text displaying what the user should do next
    private JTextField input; // where user enters input.
    
    private boolean hasAI; // if the current game is using ai or not.
    
    private int aiNum; // ai's player number (if ai went first or second)
    private int turn; // which turn it is.
    private int players; // the number of players in the current game (1/2).
    private int state; // 0 = asking for players.
                       // 1 = asking if player wants to go first (only if one player).
                       // 2 = in game
                       // 3 = asking if player want to play again.
    
    /**Precondition: none
     * 
     * Post-Condition: initializes a tic-tac-toe game on a given frame.
     */
    public GameControl(JFrame theFrame)
    {
        myFrame = theFrame;
        
        xMarks = new ArrayList<XMarker>();
        oMarks = new ArrayList<OMarker>();
        
        myBoard = new TicTacToeBoard();
        
        myBoard.addMouseListener(this);
        addMouseListener(this);
        
        record = new JTextArea(20,12);
        prompt = new JTextField(12);
        input = new JTextField(12);
        JLabel recordLabel = new JLabel("Move Record", SwingConstants.CENTER);
        
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(recordLabel);
        right.add(record);
        right.add(prompt);
        right.add(input);
        
        record.setEditable(false);
        prompt.setEditable(false);
        
        myFrame.add(myBoard, BorderLayout.CENTER);
        myFrame.add(right, BorderLayout.EAST);
        
        myFrame.setVisible(true);
        
        input.addKeyListener(this);
        addKeyListener(this);
        
        turn = 0;
        state = 0;
        prompt.setText("How many players? (1/2)");
    }

    /**Precondition: none
     * 
     * Post-Condition: takes in key input.
     */
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if (state == 0) // asking for players
            {
                int in = Integer.parseInt(input.getText());
                
                if (in == 1) // only one player
                {
                    players = in;
                    
                    hasAI = true;
                    state++;
                    prompt.setText("Go first? (y/n)");
                }
                else if (in == 2) // two players
                {
                    players = in;
                    
                    hasAI = false;
                    state += 2; // skip asking who goes first
                    prompt.setText("Player X's turn (x y)");
                }
                input.setText("");
            }
            else if (state == 1) // asking who goes first
            {
                String in = input.getText().trim();
                
                if (in.equalsIgnoreCase("y"))
                {
                    aiNum = 1;
                    
                    state++;
                    prompt.setText("Player X's turn (x y)");
                }
                else if (in.equalsIgnoreCase("n"))
                {
                    aiNum = 0;
                    
                    state++;
                    prompt.setText("Player X's turn (x y)");
                    input.setEditable(false); // ensure player cannot enter something
                }
                input.setText("");
            }
            else if (state == 2) // in actual game play
            {
                int side = turn%2;
                
                if (hasAI)
                {
                    if (side == aiNum)
                    {
                        if (turn <= 1)
                        {
                            aiTurn(true);
                        }
                        else
                        {
                            aiTurn(false);
                        }
                        
                        if (side == 0)
                        {
                            prompt.setText("Player " + MARKER[1] + "'s turn (x y)");
                        }
                        else
                        {
                            prompt.setText("Player " + MARKER[0] + "'s turn (x y)");
                        }
                        input.setEditable(true); // ensure player can enter something
                        turn++;
                    }
                    else
                    {
                        String[] in = input.getText().split(" ");
                        int x = Integer.parseInt(in[0]) - 1;
                        int y = Integer.parseInt(in[1]) - 1;
                        
                        if (x >= 0 && x <= 2 && y >= 0 && y <= 2) // are valid coordinates
                        {
                            if (myBoard.isEmpty(y,x))
                            {
                                myBoard.mark(y,x,MARKER[side]); // invert coordinates to match system in 1st quadrant
                                
                                if (side == 0)
                                {
                                    XMarker newMark = new XMarker(x*100 + 20, 220 - y*100);
                                    xMarks.add(newMark);
                                    myFrame.add(newMark);
                                }
                                else
                                {
                                    OMarker newMark = new OMarker(x*100 + 20, 220 - y*100);
                                    oMarks.add(newMark);
                                    myFrame.add(newMark);
                                }
                                record.append(MARKER[side] + ": " + (x + 1) + " " + (y + 1) + NEWLINE);
                                myFrame.setVisible(true);
                                
                                if (side == 0)
                                {
                                    prompt.setText("Player " + MARKER[1] + "'s turn (x y)");
                                }
                                else
                                {
                                    prompt.setText("Player " + MARKER[0] + "'s turn (x y)");
                                }
                                input.setEditable(false); // ensure player cannot enter something
                                input.setText("");
                                turn++;
                            }
                        }
                        else
                        {
                            input.setText("");
                        }
                    }
                }
                else
                {
                    String[] in = input.getText().split(" ");
                    int x = Integer.parseInt(in[0]) - 1;
                    int y = Integer.parseInt(in[1]) - 1;
                    
                    if (x >= 0 && x <= 2 && y >= 0 && y <= 2) // are valid coordinates
                    {
                        if (myBoard.isEmpty(y,x))
                        {
                            myBoard.mark(y,x,MARKER[side]); // invert coordinates to match system in 1st quadrant
                            
                            if (side == 0)
                            {
                                XMarker newMark = new XMarker(x*100 + 20, 220 - y*100);
                                xMarks.add(newMark);
                                myFrame.add(newMark);
                            }
                            else
                            {
                                OMarker newMark = new OMarker(x*100 + 20, 220 - y*100);
                                oMarks.add(newMark);
                                myFrame.add(newMark);
                            }
                            record.append(MARKER[side] + ": " + (x + 1) + " " + (y + 1) + NEWLINE);
                            myFrame.setVisible(true);
                            
                            if (side == 0)
                            {
                                prompt.setText("Player " + MARKER[1] + "'s turn (x y)");
                            }
                            else
                            {
                                prompt.setText("Player " + MARKER[0] + "'s turn (x y)");
                            }
                            input.setText("");
                            turn++;
                        }
                    }
                    else
                    {
                        input.setText("");
                    }
                }
                
                String end = myBoard.getEnd();
                
                if (!end.equals("") || turn == 9)
                {
                    record.append(end);
                    input.setEditable(true); // ensure player can enter something
                    prompt.setText("Play again (y/n)?");
                    state++;
                }
            }
            else if (state == 3) // asking if play again
            {
                String in = input.getText();
                
                if (in.equalsIgnoreCase("y"))
                {
                    reset();
                    prompt.setText("How many players? (1/2)");
                    input.setText("");
                }
                else
                {
                    prompt.setText("Thanks for playing!");
                    input.setText("");
                    input.setEditable(false);
                }
            }
        }
    }
    
    /**Precondition: none
     * 
     * Post-Condition: takes in mouse presses.
     */
    public void mousePressed(MouseEvent e)
    {
        if (state == 2) // if currently in game
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
                    {
                        aiTurn(true);
                    }
                    else
                    {
                        aiTurn(false);
                    }
                    
                    if (side == 0)
                    {
                        prompt.setText("Player " + MARKER[1] + "'s turn (x y)");
                    }
                    else
                    {
                        prompt.setText("Player " + MARKER[0] + "'s turn (x y)");
                    }
                    input.setEditable(true); // ensure player can enter something
                    turn++;
                }
                else
                {
                    if (x >= 0 && x <= 2 && y >= 0 && y <= 2) // are valid coordinates
                    {
                        if (myBoard.isEmpty(y,x))
                        {
                            myBoard.mark(y,x,MARKER[side]); // invert coordinates to match system in 1st quadrant
                            
                            if (side == 0)
                            {
                                XMarker newMark = new XMarker(x*100 + 20, 220 - y*100);
                                xMarks.add(newMark);
                                myFrame.add(newMark);
                            }
                            else
                            {
                                OMarker newMark = new OMarker(x*100 + 20, 220 - y*100);
                                oMarks.add(newMark);
                                myFrame.add(newMark);
                            }
                            record.append(MARKER[side] + ": " + (x + 1) + " " + (y + 1) + NEWLINE);
                            myFrame.setVisible(true);
                            
                            if (side == 0)
                            {
                                prompt.setText("Player " + MARKER[1] + "'s turn.");
                            }
                            else
                            {
                                prompt.setText("Player " + MARKER[0] + "'s turn.");
                            }
                            input.setEditable(false); // ensure player cannot enter something
                            input.setText("");
                            turn++;
                        }
                    }
                    else
                    {
                        input.setText("");
                    }
                }
            }
            else // if two player game
            {
                if (x >= 0 && x <= 2 && y >= 0 && y <= 2) // are valid coordinates
                {
                    if (myBoard.isEmpty(y,x))
                    {
                        myBoard.mark(y,x,MARKER[side]); // invert coordinates to match system in 1st quadrant
                        
                        if (side == 0)
                        {
                            XMarker newMark = new XMarker(x*100 + 20, 220 - y*100);
                            xMarks.add(newMark);
                            myFrame.add(newMark);
                        }
                        else
                        {
                            OMarker newMark = new OMarker(x*100 + 20, 220 - y*100);
                            oMarks.add(newMark);
                            myFrame.add(newMark);
                        }
                        record.append(MARKER[side] + ": " + (x + 1) + " " + (y + 1) + NEWLINE);
                        myFrame.setVisible(true);
                        
                        if (side == 0)
                        {
                            prompt.setText("Player " + MARKER[1] + "'s turn (x y)");
                        }
                        else
                        {
                            prompt.setText("Player " + MARKER[0] + "'s turn (x y)");
                        }
                        input.setText("");
                        turn++;
                    }
                }
                else
                {
                    input.setText("");
                }
            }
            
            String end = myBoard.getEnd();
        
            if (!end.equals("") || turn == 9)
            {
                record.append(end);
                input.setEditable(true); // ensure player can enter something
                prompt.setText("Play again (y/n)?");
                state++;
            }
        }
    }
    
    /**Precondtion: game has at least started.
     * 
     * Post-Condition: removes all x- and o- marks from the frame and tic-tac-toe board
     */
    public void reset()
    {
        for (int i = 0; i < DIMENSION; i++)
        {
            for (int j = 0; j < DIMENSION; j++)
            {
                myBoard.reset(i,j);
            }
        }
        
        for (int m = 0; m < xMarks.size(); m++)
        {
            myFrame.remove(xMarks.get(m));
        }
        for (int n = 0; n < oMarks.size(); n++)
        {
            myFrame.remove(oMarks.get(n));
        }
        
        xMarks.clear();
        oMarks.clear();
        
        xMarks.trimToSize();
        oMarks.trimToSize();
        
        myFrame.setVisible(true);
        
        turn = 0;
        state = 0;
        
        record.setText(""); // reset record of moves
    }
    
    /*******************************************************************Used by AI*******************************************************************/
    
    /**Precondition: none
     * 
     * Post-Condition: decides a move for the AI to make.
     */
    public void aiTurn(boolean first) // takes in whether it is the first time it is moving
    {
        Random rand = new Random();
        
        /**Assign values for moves**/
        int[] winMove = getWinningMove(MARKER[aiNum]);
        int[] loseMove;
        int[] winDivineMove = getDivineMove(MARKER[aiNum]);
        int[] loseDivineMove;
        int[] centerMove = {1,1};
        int[] upperLeft = {2,0}; // coordinates inverted to coorespond to GUI output
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
        record.append(MARKER[aiNum] + ":" + (y + 1) + " " + (x + 1) + NEWLINE); // invert coordinates to match system in 1st quadrant
        myBoard.mark(x, y, MARKER[aiNum]);
        if (aiNum == 0)
        {
            XMarker newMark = new XMarker(y*100 + 20, 220 - x*100); // invert coordinates to match system in 1st quadrant
            xMarks.add(newMark);
            myFrame.add(newMark);
        }
        else
        {
            OMarker newMark = new OMarker(y*100 + 20, 220 - x*100); // invert coordinates to match system in 1st quadrant
            oMarks.add(newMark);
            myFrame.add(newMark);
        }
        myFrame.setVisible(true);
    }
    
    /**Required by MouseListener**/
    public void mouseExited(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    
    /**Required by KeyListener**/
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    
} // END CLASS