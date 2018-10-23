package Tetris;

//import
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import GameComponents.BoundedEnv;
import GameComponents.Direction;
import GameComponents.Game;
import GameComponents.Locatable;
import GameComponents.Location;

/**
 * Controls the Tetris game.
 */
public class TetrisControl extends Game
{
    //constants
    private static final String DIRECTORY = "Tetris/"; //directory of files
    private static final int WIDTH = 500; //width of game frame
    private static final int HEIGHT = 500; //height of game frame
    private static final int ENV_WIDTH = 15; //env width in cells
    private static final int ENV_HEIGHT = 15; //env height in cells
    private static final int SCORE_INCREMENT = 10; //increment score grows by
    
    //indexes of arrays correspond to each other
    private static final String[] DIFF_CHOICES = {"Easy", //difficulty choices
                                                  "Medium",
                                                  "Hard"};
    private static final int STEP_DELAY[] = {35, 25, 15}; //delay for stepping
    
    //for options panel
    private JComboBox diffCombo; //combo for selecting difficulty
    private TetrisPiece myPiece; //tetris piece currently controlled by player
    private ArrayList<Piece> allPieces; //list of all pieces in game
    private BoundedEnv theEnv; //environment game is in
                               //should only contain pieces

    private int score; //current score in game
    private int diffIndex; //index of selected difficulty setting
    private boolean over; //if the game is over
    
    private int myDelay; //delay for stepping
    private int stepCount; //number of step attempts since last step
    
    /**
     * Initializes the controller object.
     */
    public TetrisControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**
     * Returns the option panel for this game.
     */
    public JPanel getOptionsPanel()
    {
        //create components for dialog 
        diffCombo = new JComboBox(DIFF_CHOICES);
        diffCombo.setSelectedIndex(0);
        Border emptyBorder = BorderFactory.createEmptyBorder(GAP,GAP,GAP,GAP);
        diffCombo.setBorder(BorderFactory.createTitledBorder(emptyBorder, 
                                "Difficulty "));
        
        //place in panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(150,60));
        panel.add(diffCombo, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Fired when a key event is invoked. Handles the 
     * controlling of the tetris piece.
     */
    public void keyPressed(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT)
            myPiece.moveLeft();
        else if (keyCode == KeyEvent.VK_RIGHT)
            myPiece.moveRight();
        else if (keyCode == KeyEvent.VK_A)
            myPiece.rotateLeft();
        else if (keyCode == KeyEvent.VK_S)
            myPiece.rotateRight();
        else if (keyCode == KeyEvent.VK_SPACE)
            myPiece.drop();
    }
    
    /**
     * Initializes the gameplay components.
     */
    protected void initializeGameplayComponents()
    {
        //initialize environment
        theEnv = new BoundedEnv(ENV_WIDTH, ENV_HEIGHT, 
                                WIDTH/ENV_WIDTH, HEIGHT/ENV_HEIGHT);
                                
        //initialize list of all pieces
        allPieces = new ArrayList<Piece>();
        
        sendNextPiece();
        
        diffIndex = diffCombo.getSelectedIndex();
        
        myDelay = STEP_DELAY[diffIndex];
        stepCount = 0;
    }
    
    /**
     * Runs the game process.
     */
    protected void runProcess()
    {
        if (stepCount >= myDelay)
        {
            //step tetris piece
            myPiece.step();
            
            //clear any completed rows
            if (clearRows())
                score += SCORE_INCREMENT;
                
            //send another piece if current piece has stopped
            if (!myPiece.canFall()) //can't fall
                sendNextPiece();
                
            stepCount = 0; //reset
            
            //check if game is over
            if (isOver())
                over = true;
        }
        else
        {
            stepCount++;
        }
    }
    
    /**
     * Sends the next tetris piece.
     */
    private void sendNextPiece()
    {
        Random rand = new Random();
        int startX = rand.nextInt(theEnv.width());
        myPiece = new TetrisPiece(theEnv, new Location(startX, 0));
        ArrayList<Piece> pieces = myPiece.allPieces();
        for (Piece p : pieces)
            allPieces.add(p);
    }
    
    /**
     * Returns the name of the game.
     */
    public String name()
    {
        return "Tetris";
    }
    
    /**
     * Returns the description of the game.
     */
    public String description()
    {
        return "Manuever the pieces and prevent the blocks from building up!";
    }

    /**
     * Returns whether or not any piece (not the player's) 
     * is at the top of the environment, dictating whether 
     * or not the game is over.
     */
    private boolean isOver()
    {
        for (Piece p : allPieces)
            if (!myPiece.allPieces().contains(p)) //not player's piece
                if (p.loc().y() <= 0) //at top
                    return true;
        return false;
    }
    
    /**
     * Clears any complete rows in the environment. Returns whether or 
     * not any rows were cleared.
     */
    private boolean clearRows()
    {
        boolean retVal = false;
        for (int i = 0; i < theEnv.height(); i++)
        {
            if (fullRow(i))
            {
                clearRow(i);
                shiftDown(i);
                retVal = true;
            }
        }
        return retVal;
    }
    
    /**
     * Returns whether or not this game can be scored.
     */
    public boolean isScorable()
    {
        return true;
    }
    
    /**
     * Returns whether or not the row index in the environment 
     * is full or not.
     */
    private boolean fullRow(int index)
    {
        for (int i = 0; i < theEnv.width(); i++) //check each column
            if (theEnv.isEmpty(new Location(i, index)))
                return false;
        return true;
    }
    
    /**
     * Clears the row at the given index in the environment.
     */
    private void clearRow(int index)
    {
        for (int i = 0; i < theEnv.width(); i++) //for each column
        {
            Locatable obj = theEnv.objectAt(new Location(i, index));
            theEnv.remove(obj);
            int indexOf = allPieces.indexOf(obj);
            if (indexOf != -1) //object is there
                allPieces.remove(indexOf);
            score += SCORE_INCREMENT;
        }
    }
    
    /**
     * Pushes all the pieces that are not part of the current 
     * tetris piece and above the given row index down one unit.
     */
    private void shiftDown(int row)
    {
        for (Piece p : allPieces)
        {
            if (!myPiece.allPieces().contains(p) &&  //not part of player piece
                p.loc().y() < row) //above row
            {
                Location locBelow = theEnv.neighborOf(p.loc(), Direction.SOUTH);
                p.changeLocation(locBelow);
            }
        }
    }
    
    /**
     * Paints the game onto the container.
     */
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        //paint background
        g2.setColor(Color.yellow);
        g2.fill(g2.getClipBounds());
        
        //paint all pieces
        for (Piece p : allPieces)
            p.paintComponent(g2);
            
        //paint score displays
        g2.setColor(Color.black);
        g2.drawString("Score: " + score, GAP, 3*GAP); //score
        g2.drawString("Level: " + DIFF_CHOICES[diffIndex], //level
                            WIDTH - 80, 3*GAP); 
        
        if (over) //game over
        {
            g2.setColor(Color.black);
            g2.setFont(new Font("", Font.BOLD, 40));
            g2.drawString("Game Over", WIDTH/2 - 100, HEIGHT/2 - 10);
            pauseGame(); //stop game actions
            submitScore(score);
        }
    }
    
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new TetrisControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 155));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}