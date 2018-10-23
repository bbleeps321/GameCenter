package Pong;

//import
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import GameComponents.Direction;
import GameComponents.Game;
import GameComponents.Simulation;
import GameComponents.Steppable;

/**
 * Controls the Pong game. Provides an option panel and 
 * controls for playing the pong game.
 */
public class PongControl extends Game implements KeyListener, ActionListener
{
    //constants
    private static final String DIRECTORY = "pong/"; //directory of resources
    private static final int WIDTH = 500; //width of window
    private static final int HEIGHT = 500; //height of window
    private static final double INIT_BALL_SPD = 6; //ball's starting speed
    private static final Integer[] NUM_CHOICES = {1,3,5,7,9};//choices total games
    
    //components for options panel
    private JComboBox gameNumSelect; //combobox for selecting how many matches
    
    //variables for gameplay
    private Paddle myPaddle1, myPaddle2; //p1 and p2 paddles
    private Ball myBall; //ball in the game
    private GameComponents.Canvas canvas; //canvas game is on
    private Simulation theSim; //simulation for stepping paddles and ball
    
    private int totalGames; //total number of games to play
    private int matchNum; //number of current match
    private int scoreP1, scoreP2; //scores in game of p1 and p2
    private boolean over; //if game is over
    
    /**
     * Initializes the game.
     */
    public PongControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**
     * Returns the panel containing the options of the game.
     */
    public JPanel getOptionsPanel()
    {
        //create components for panel 
        gameNumSelect = new JComboBox(NUM_CHOICES);
        gameNumSelect.setSelectedIndex(0);
        Border emptyBorder = BorderFactory.createEmptyBorder(GAP,GAP,GAP,GAP);
        gameNumSelect.setBorder(BorderFactory.createTitledBorder(emptyBorder, 
                                "Best of: "));
        
        //place in panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(150,80));
        panel.add(gameNumSelect, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Required by KeyListener. Handles keyboard input 
     * for controlling the blobert.
     */
    public void keyPressed(KeyEvent e)
    {
        if (myPaddle1 != null)
        {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_W) //player 1's controls
                myPaddle1.moveUp();
            else if (keyCode == KeyEvent.VK_S)
                myPaddle1.moveDown();
            else if (keyCode == KeyEvent.VK_SHIFT)
                myPaddle1.brake();
            else if (keyCode == KeyEvent.VK_UP) //player 2's controls
                myPaddle2.moveUp();
            else if (keyCode == KeyEvent.VK_DOWN)
                myPaddle2.moveDown();
            else if (keyCode == KeyEvent.VK_ENTER)
                myPaddle2.brake();
            else if (keyCode == KeyEvent.VK_SPACE) //start
                startMatch();
        }
    }
    
    /**
     * Initializes the gameplay components.
     */
    protected void initializeGameplayComponents()
    {
        //create canvas
        canvas = new GameComponents.Canvas(WIDTH, HEIGHT);
        
        totalGames = NUM_CHOICES[gameNumSelect.getSelectedIndex()];
        
        //create paddles
        myPaddle1 = new Paddle(Direction.WEST, canvas);
        myPaddle2 = new Paddle(Direction.EAST, canvas);
        
        //create ball
        myBall = new Ball(WIDTH/2, HEIGHT/2, canvas);
        
        //initialize variables
        over = false;
        matchNum = 1;
        
        //create simulation
        ArrayList<Steppable> objs = new ArrayList<Steppable>();
        objs.add(myPaddle1);
        objs.add(myPaddle2);
        objs.add(myBall);
        theSim = new Simulation(objs);
    }
    
    /**
     * Runs the game process.
     */
    protected void runProcess()
    {
        theSim.step(); //step paddles and ball
            
        //ball bounces on paddle
        if (myBall.leftBound() <= myPaddle1.rightBound() &&
                myPaddle1.encompasses(myBall.y())) //ball hit p1
        {
            myBall.paddleBounce(myPaddle1.speed());
        }
        else if (myBall.rightBound() >= myPaddle2.leftBound() &&
                myPaddle2.encompasses(myBall.y())) //ball hit p2
        {
            myBall.paddleBounce(myPaddle2.speed());
        }
        
        if (!myBall.inBounds()) //ball in someone's goal
        {
            if (myBall.x() < 0) //p1's goal
            {
                scoreP2++;
                
                int winner = getWinner();
                if (winner != -1) //winner decided
                    endGame(); //end the game
                else
                    nextMatch(); //go to next match
            }
            else if (myBall.x() > canvas.width()) //p2's goal
            {
                scoreP1++;
                
                int winner = getWinner();
                if (winner != -1) //winner decided
                    endGame(); //end the game
                else
                    nextMatch(); //go to next match
            }
        }
    }
    
    /**
     * Starts the current match.
     */
    private void startMatch()
    {
        if (!myBall.isMoving()) //ball not moving, game not started yet
            if (matchNum % 2 == 1) //p1's turn
                myBall.setDirection(-INIT_BALL_SPD, 0);
            else //p2's turn
                myBall.setDirection(INIT_BALL_SPD, 0);
    }
    
    /**
     * Prepares level for next match.
     */
    public void nextMatch()
    {
        matchNum++;
        
        myBall.reset();
        myPaddle1.reset();
        myPaddle2.reset();
    }
    
    /**
     * Required by abstract class Game. Returns the name of the game.
     */
    public String name()
    {
        return "Pong";
    }
    
    /**
     * Required by abstract class Game. Returns the description of the game.
     */
    public String description()
    {
        return "Score into the opponent's goal while protecting your own!";
    }

    /**
     * Paints the game to the container.
     */
    protected void paintComponent(Graphics g)
    {
        if (myPaddle1 != null)
        {
            Graphics2D g2 = (Graphics2D) g;
            
            //paint background
            g2.setColor(Color.black);
            g2.fill(g2.getClipBounds());
            
            //paint score displays
            g2.setColor(Color.white);
            g2.setFont(new Font("", Font.PLAIN, 50));
            g2.drawString(scoreP1 + " : " + scoreP2, WIDTH/2 - 40, 8*GAP); //score
            
            //paint ball
            myBall.paintComponent(g2);
            
            //paint paddles
            myPaddle1.paintComponent(g2);
            myPaddle2.paintComponent(g2);
            
            if (over) //game over
            {
                g2.setColor(Color.red);
                g2.setFont(new Font("", Font.BOLD, 40));
                g2.drawString("Player " + getWinner() + " Wins!", 
                                WIDTH/2 - 125, HEIGHT/2 - 10);
                pauseGame(); //stop game actions
            }
        }
    }
    
    //private methods
    /**
     * Ends the game, displaying a "game over" text.
     */
    private void endGame()
    {
        over = true;
    }
    
    /**
     * Returns the projected winner of the game as an int.
     * Returns -1 if cannot tell yet.
     */
    private int getWinner()
    {
        if (scoreP1 >= totalGames/2 + 1) //p1 has won more than half
            return 1;
        else if (scoreP2 >= totalGames/2 + 1) //p2 has won more than half
            return 2;
        else //none have won more than half
            return -1;
    }
    
    /**
     * Returns whether or not this game can be scored.
     */
    public boolean isScorable()
    {
        return false;
    }
    
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new PongControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}