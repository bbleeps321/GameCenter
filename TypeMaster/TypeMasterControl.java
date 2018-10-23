package TypeMaster;

//import
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import GameComponents.Game;

/**
 * Controls the TypeMaster game.
 */
public class TypeMasterControl extends Game
{
    //constants
    private static final String DIRECTORY = "TypeMaster/"; //directory of files
    private static final int WIDTH = 500; //width of game frame
    private static final int HEIGHT = 500; //height of game frame
    private static final int INIT_DELAY = 15; //initial delay
    private static final int TIME_PER_LEVEL = 25; //steps before level grows
    private static final double NEW_BUBBLE_CHANCE = .7; //chance of new bubble
    private static final int SCORE_INCREMENT = 100; //increment score grows by
       
    private ArrayList<Bubble> bubbles; //list of all active bubbles in game
    
    private int stepCount; //count of attempts since last step
    private int resetCount; //count of how many times stepCount was reset
    private int myDelay; //current delay value
    private double myBubbleChance; //current chance of new bubble
    private int score; //current score in game
    
    private boolean over; //if the game is over
    
    /**
     * Creates a new TypeMasterControl object.
     */
    public TypeMasterControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
        
    /**
     * Handles a key pressed event. Will destroy any bubbles that have 
     * the same character as the one typed and increment points. If no 
     * bubbles have that character, will halve points.
     */
    public void keyTyped(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) //button to pause pressed
            return;

        char charTyped = e.getKeyChar();
        int pointsToAdd = 0;
        //find all bubbles that match
        for (int i = 0; i < bubbles.size(); i++)
        {
            Bubble b = bubbles.get(i);
            //convert characters to lower case
            char bChar = Character.toLowerCase(b.getChar());
            charTyped = Character.toLowerCase(charTyped);
            if (bChar == charTyped) //same
            {
                playSound("type.wav");
                pointsToAdd += SCORE_INCREMENT;
                
                b.pop();
                bubbles.remove(i);
                i--;
            }
        }
        
        if (pointsToAdd == 0) //no points added
        {
            score /= 2; //halve score
            playSound("fire.au");
        }
        else
        {
            score += pointsToAdd;
        }
    }
    
    /**
     * Initializes the game play components.
     */
    protected void initializeGameplayComponents()
    {
        //initialize list of bubbles.
        bubbles = new ArrayList<Bubble>();
        
        score = 0;
        stepCount = 0;
        resetCount = 0;
        myDelay = INIT_DELAY;
        myBubbleChance = NEW_BUBBLE_CHANCE;
    }
    
    /**
     * Runs the game process.
     */
    protected void runProcess()
    {
        if (stepCount >= myDelay)
        {
            //step bubbles
            for (Bubble b : bubbles)
                b.step();
            
            //check if any bubble has reached the bottom
            for (Bubble b : bubbles)
                if (b.y() >= HEIGHT)
                    over = true;
            
            //reset
            stepCount = 0;
            resetCount++;
            
            //decide whether or not to send another bubble
            if (Math.random() < myBubbleChance)
                sendNextBubble();
        }
        else
        {
            stepCount++;
        }
        
        if (resetCount >= TIME_PER_LEVEL)
        {
            resetCount = 0;
            if (myDelay <= 0) //delay cannot be any less
                myBubbleChance += .1;
            else
                myDelay--;
        }
    }
    
    /**
     * Sends the next bubble from the top at a random x coordinate.
     */
    private void sendNextBubble()
    {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH - 30);
        bubbles.add(new Bubble(x,0));
    }
    
    /**
     * Returns the name of the game.
     */
    public String name()
    {
        return "Type Master";
    }
    
    /**
     * Returns the description of the game.
     */
    public String description()
    {
        return "Type the keys in the bubbles to pop them before " + 
                "they hit the bottom!";
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
        
        Font defaultFont = g2.getFont();
        
        //paint background
        g2.setColor(Color.white);
        g2.fill(g2.getClipBounds());
        
        //paint bubbles
        for (Bubble b : bubbles)
            b.paintComponent(g2);
            
        //paint score displays
        g2.setFont(defaultFont);
        g2.setColor(Color.black);
        
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
        Game gui = new TypeMasterControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}