package Firewall;

//import
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import GameComponents.BoundedEnv;
import GameComponents.Game;
import GameComponents.Location;

/**
 * Controls the functions of the Firewall game.
 */
public class FirewallControl extends Game
{
    //constants
    private static final String DIRECTORY = "Firewall/"; //directory of resources
    private static final int WIDTH = 500; //width of game frame
    private static final int HEIGHT = 500; //height of game frame
    private static final int SCORE_INCREMENT = 100; //increment score grows by
    
    private Wall myWall; //wall in the game
    private BoundedEnv theEnv; //environment of game
    private Gunner myGunner; //gunner in game
    private int score; //current score
    private int deduct; //points to deduct from score
    private boolean over; //if game is over
   
    /**
     * Creates an instance of the Firewall game.
     */
    public FirewallControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**
     * Fired when a KeyEvent is invoked. Controls the gunner.
     */
    public void keyPressed(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        
        //movement
        if (keyCode == KeyEvent.VK_LEFT)
            myGunner.moveLeft();
        else if (keyCode == KeyEvent.VK_RIGHT)
            myGunner.moveRight();
        //weapons
        else if (keyCode == KeyEvent.VK_SPACE)
        {
            int cost = myGunner.fireCost("Shot")*SCORE_INCREMENT;
            if (score - cost >= 0) //can afford it
            {
                myGunner.fire("Shot");
                playSound("photon.wav");
                deduct += cost;
            }
        }
        else if (keyCode == KeyEvent.VK_A)
        {
            int cost = myGunner.fireCost("Bomb")*SCORE_INCREMENT;
            if (score - cost >= 0) //can afford it
            {
                myGunner.fire("Bomb");
                playSound("missile.wav");
                deduct += cost;
            }
        }
        else if (keyCode == KeyEvent.VK_S)
        {
            int cost = myGunner.fireCost("Pierce")*SCORE_INCREMENT;
            if (score - cost >= 0) //can afford it
            {
                myGunner.fire("Pierce");
                playSound("missile.wav");
                deduct += cost;
            }
        }
    }
    
    /**
     * Initilizss the game components.
     */
    protected void initializeGameplayComponents()
    {
        //create environment
        theEnv = new BoundedEnv(25, 25, WIDTH/25, HEIGHT/25);
        
        //create wall
        myWall = new Wall(theEnv, 25);
        
        //create gunner
        Location gunLoc = new Location(theEnv.width()/2, theEnv.height() - 1);
        myGunner = new Gunner(theEnv, gunLoc);
        
        over = false;
        
        score = 0; //score starts at zero
        deduct = 0; //nothing to deduct at start
    }
    
    /**
     * Runs the game process.
     */
    protected void runProcess()
    {
        myWall.step();
        myGunner.step();
        
        //recalculate score
        score = myWall.numBricksLost()*SCORE_INCREMENT - deduct;
        
        //check if wall has reached bottom
        if (myWall.reachedBottom())
            endGame();
    }
    
    /**
     * Returns a description of the game.
     */
    public String description()
    {
        return "Destroy the wall of fire that's crashing down on you!";
    }
    
    /**
     * Returns the name of the game.
     */
    public String name()
    {
        return "Firewall";
    }
    
    /**
     * Returns the String url of the text file containing the game's 
     * help information.
     */
    public String helpFileName()
    {
        return DIRECTORY + "help.txt";
    }

    /**
     * Ends the game.
     */
    private void endGame()
    {
        over = true;
    }
    
    /**
     * Paints the game onto the container.
     */
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        //paint background
        g2.setColor(Color.black);
        g2.fill(g2.getClipBounds());
        
        //paint wall
        myWall.paintComponent(g2);
        
        //paint gunner
        myGunner.paintComponent(g2);
        
        //paint score displays
        g2.setColor(Color.green);
        g2.drawString("Score: " + score, GAP, 3*GAP); //score
            
        if (over) //game over
        {
            g2.setColor(Color.red);
            g2.setFont(new Font("", Font.BOLD, 40));
            g2.drawString("Game Over", WIDTH/2 - 100, HEIGHT/2 - 10);
            pauseGame(); //stop game actions
            submitScore(score);
        }
    }
    
    /**
     * Returns whether or not this game can be scored.
     */
    public boolean isScorable()
    {
        return true;
    }
    
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new FirewallControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}