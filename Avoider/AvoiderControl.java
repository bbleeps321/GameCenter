package Avoider;

//import
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import GameComponents.Game;

/**
 * Controls the Gateway game's functions and components.
 */
public class AvoiderControl extends Game
{
    //constants
    private static final String DIRECTORY = "Avoider/"; //directory of files
    private static final int WIDTH = 500; //width of game frame
    private static final int HEIGHT = 500; //height of game frame
    private static final int STEP_DELAY = 12; //delay for stepping
    private static final int TIME_PER_LEVEL = 200; //steps before level grows
    private static final int INIT_LIVES = 3; //starting number of lives
    
    //different types of chasers. redundant to simulate probabilities
    private static final String[] CHASERS = {"Chaser", "Chaser", "Chaser",
                                             "RandomChaser", "RandomChaser",
                                             "TrickyChaser"};
    
    private GameComponents.Canvas canvas; //canvas game is using
    private Avoider myAvoider; //avoider controlled by player
    private ArrayList<Chaser> chasers; //list of active chasers on the stage
    
    private int stepCount; //count of attempts since last step
    private int chaserCount; //current number of chasers in game
    private int lives; //lives remaining
    
    private boolean over; //if the game is over
    private boolean started; //if current level has started
    
    /**
     * Creates a AvoiderControl object.
     */
    public AvoiderControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**
     * Fired when a mouse button is clicked. Handles the starting 
     * of the current level.
     */
    public void mousePressed(MouseEvent e)
    {
        started = true;
    }
    
    /**
     * Fired when a mouse moved event is invoked. Moves the avoider 
     * to the cursor's location.
     */
    public void mouseMoved(MouseEvent e)
    {
        if (started)
        {
            double x = e.getX();
            double y = e.getY();
            
            myAvoider.moveTo(x,y);
        }
    }
    
    /**
     * Initializes the game components.
     */
    protected void initializeGameplayComponents()
    {
        //initialize canvas
        canvas = new GameComponents.Canvas(WIDTH, HEIGHT);
        
        //initialize avoider
        myAvoider = new Avoider(WIDTH/2, HEIGHT/2, canvas);
        
        //initialize chasers list
        chasers = new ArrayList<Chaser>();
        
        //add one chaser
        Random rand = new Random();
        int x = rand.nextInt(WIDTH);
        int y = rand.nextInt(HEIGHT);
        chasers.add(Chaser.createInstanceOf(CHASERS[0], x, y, myAvoider, canvas));
        
        stepCount = 0;
        chaserCount = 1;
        lives = INIT_LIVES;
        over = false;
        started = false;
    }
    
    /**
     * Runs the game process.
     */
    protected void runProcess()
    {
        if (started)
        {
            stepCount++;
            
            //step portals
            for (Chaser c : chasers)
                c.step();
                
            //check if avoider is overlapping any chasers
            for (Chaser c : chasers)
            {
                if (myAvoider.overlaps(c)) //overlapping
                {
                    lives--;
                    playSound("die.au");
                    if (lives <= 0) //no lives left
                        over = true;
                    else //can continue with current level
                        resetLevel();
                }
            }
                
            
            if (stepCount >= TIME_PER_LEVEL) //should increase level
            {
                addChaser(); //add a chaser
                stepCount = 0;
            }
        }
    }
    
    /**
     * Adds a chaser to the game.
     */
    private void addChaser()
    {
        Random rand = new Random();
        
        //select chaser type
        int index = rand.nextInt(Math.min(chaserCount, CHASERS.length));
        String type = CHASERS[index];
        
        //create and add to list
        int x = rand.nextInt(WIDTH);
        int y = rand.nextInt(HEIGHT);
        chasers.add(Chaser.createInstanceOf(type, x, y, myAvoider, canvas));
        chaserCount++;
        
        //new life every five levels
        if (chaserCount % 5 == 0)
            lives++;
    }
    
    /**
     * Resets the current level, allowing the player to start when 
     * ready.
     */
    private void resetLevel()
    {
        started = false;
        
        //place avoider in center of screen
        myAvoider.moveTo(WIDTH/2, HEIGHT/2);
        
        //change direction and location of all chasers
        for (Chaser c : chasers)
        {
            c.randomizeDirection();
            c.randomizeLocation();
        }
        
        stepCount = 0; //reset
    }
    
    /**
     * Returns the name of the game.
     */
    public String name()
    {
        return "Avoider";
    }
    
    /**
     * Returns the description of the game.
     */
    public String description()
    {
        return "Dodge the objects that are after you!";
    }
    
    /**
     * Returns whether or not this game can be scored.
     */
    public boolean isScorable()
    {
        return true;
    }
    
    /**
     * Paints the game onto the container.
     */
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        //paint background
        g2.setColor(Color.white);
        g2.fill(g2.getClipBounds());

        //paint chasers
        for (Chaser c : chasers)
            c.paintComponent(g2);
            
        //paint avoider
        myAvoider.paintComponent(g2);
        
        //paint score displays
        g2.setColor(Color.black);
        g2.drawString("Level: " + chaserCount, GAP, 3*GAP); //level
        g2.drawString("Lives: " + lives, WIDTH - 60, 3*GAP); //lives
        
        if (over) //game over
        {
            g2.setColor(Color.red);
            g2.setFont(new Font("", Font.BOLD, 40));
            g2.drawString("Game Over", WIDTH/2 - 100, HEIGHT/2 - 10);
            pauseGame(); //stop game actions
            submitScore(chaserCount);
        }
    }
    
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new AvoiderControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}