package Gateway;

//import
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import GameComponents.Game;

/**
 * Controls the Gateway game's functions and components.
 */
public class GatewayControl extends Game
{
    //constants
    private static final String DIRECTORY = "Gateway/"; //directory of files
    private static final int WIDTH = 500; //width of game frame
    private static final int HEIGHT = 500; //height of game frame
    private static final int INIT_DELAY = 12; //initial delay for stepping
    private static final int TIME_PER_LEVEL = 50; //steps before level grows
    private static final double NEW_PORTAL_CHANCE = .4; //chance of new portal
    private static final int SCORE_INCREMENT = 100; //increment score grows by
    private static final String[] BACKGROUNDS = {"coast.jpg", //background images
                                                 "winter.jpg",
                                                 "blue hills.jpg",
                                                 "sunset.jpg"};
    
    private Key myKey; //key controlled by player
    private ArrayList<Portal> portals; //list of active portals on the stage
    
    private int stepCount; //count of attempts since last step
    private int resetCount; //count of how many times stepCount was reset
    private int myDelay; //current delay value
    private int score; //current score in game
    
    private boolean over; //if the game is over
    
    /**
     * Creates a GatewayControl object.
     */
    public GatewayControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**
     * Fired when a key pressed event is invoked. Changes the key's 
     * shape and/or color. Changes background color based on color 
     * change.
     */
    public void keyPressed(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_A)
            myKey.shiftColorPrev();
        else if (keyCode == KeyEvent.VK_S)
            myKey.shiftColorNext();
        else if (keyCode == KeyEvent.VK_SPACE)
            myKey.shiftShape();
    }
    
    /**
     * Fired when a mouse moved event is invoked. Moves the key 
     * to the cursor's location.
     */
    public void mouseMoved(MouseEvent e)
    {
        double x = e.getX();
        double y = e.getY();
        
        myKey.moveTo(x,y);
    }
    
    /**
     * Fired when a mouse press event is invoked. Rotates the key.
     */
    public void mousePressed(MouseEvent e)
    {
        myKey.rotate();
    }
    
    /**
     * Initializes the game components.
     */
    protected void initializeGameplayComponents()
    {
        //initialize key
        myKey = new Key(WIDTH/2, HEIGHT/2);
        
        //initialize portals
        portals = new ArrayList<Portal>();
        sendNextPortal(); //send one portal
        
        stepCount = 0;
        myDelay = INIT_DELAY;
        resetCount = 0;
        score = 0;
        over = false;
    }
    
    /**
     * Runs the game process.
     */
    protected void runProcess()
    {
        if (stepCount >= myDelay)
        {
            //step portals
            for (Portal p : portals)
                p.step();
                
            stepCount = 0;
            resetCount++;
            
            //decide whether or not to send another portal
            if (Math.random() < NEW_PORTAL_CHANCE)
                sendNextPortal();
                
            //check if any portals have reached bottom
            for (Portal p : portals)
                if (p.y() >= HEIGHT) //reached bottom
                    over = true; //end game
        }
        else
        {
            stepCount++;
        }
        
        if (resetCount >= TIME_PER_LEVEL) //should increase level
        {
            myDelay--;
            resetCount = 0;
        }
        
        //check if key fits any portal
        for (int i = 0; i < portals.size(); i++)
        {
            Portal p = portals.get(i);
            
            //remove the portal if it contains the center of the key 
            //and the key matches it
            if (p.contains(myKey.centerX(), myKey.centerY()) && 
                keyMatches(p))
            {
                portals.remove(i);
                i--;
                score += SCORE_INCREMENT;
                playSound("ding.au");
            }
        }
    }
    
    /**
     * Sends a portal down from the top at a random x location.
     */
    private void sendNextPortal()
    {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH - 30);
        portals.add(new Portal(x, 0));
    }
    
    /**
     * Returns the name of the game.
     */
    public String name()
    {
        return "Gateway";
    }
    
    /**
     * Returns whether or not this game can be scored.
     */
    public boolean isScorable()
    {
        return true;
    }
    
    /**
     * Returns the description of the game.
     */
    public String description()
    {
        return "Morph yourself to fit through the falling " + 
                "portals and destroy them!";
    }
    
    /**
     * Returns whether or not the key matches the specified portal 
     * in terms of shape, color, and angle.
     */
    private boolean keyMatches(Portal p)
    {
        //test if have same shape
        Class keyShapeType = myKey.shape().getClass();
        Class portShapeType = p.shape().getClass();
        boolean sameShape = keyShapeType.equals(portShapeType);
        
        //test if have same color
        boolean sameColor = myKey.color().equals(p.color());
        
        //test if have same angle
        final double EPSILON = .1;
        boolean sameAngle = Math.abs(myKey.angle() - p.angle()) < EPSILON;
        
        //return results
        return sameShape && sameColor && sameAngle;
    }
    
    /**
     * Paints the game onto the container.
     */
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        //paint background only if score has changed to save memory
        int bgIndex = (score/500) % BACKGROUNDS.length; //index of image
        Image image = sprite.getSprite(DIRECTORY + BACKGROUNDS[bgIndex]);
        g2.drawImage(image, 0, 0, WIDTH, HEIGHT, null);

        //paint portals
        for (Portal p : portals)
            p.paintComponent(g2);
            
        //paint key
        myKey.paintComponent(g2);
        
        //paint score displays
        g2.setColor(Color.yellow);
        g2.drawString("Score: " + score, WIDTH/2 - 15, 3*GAP); //score
        
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
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new GatewayControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}