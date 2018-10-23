package Worm;

//import
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import GameComponents.Game;
import GameComponents.Shape2D;

/**
 * Controls the game functions of the Worm game.
 */
public class WormControl extends Game implements ActionListener, KeyListener
{
    //constants
    private static final String DIRECTORY = "worm/"; //where game resources are
    private static final int BORDER = 15; //border by along which apples
                                          //and obstacles cannot be placed on
    private static final int WIDTH = 500; //width of window
    private static final int HEIGHT = 500; //height of window
    private static final int SCORE_INCREMENT = 100; //increment score grows by
    private static final int START_LIVES = 3; //number of lives initially
    private static final double SIZE = 12; //size of segments, apples, obstacles
    
    //game play components
    private Worm myWorm; //worm that can be controlled
    private Apple apple; //apple that is being repeated collected
    private ArrayList<Obstacle> obstacles; //list of obstacles in worm's way
    
    private int level; //game level worm is on
    private int lives; //lives worm has left
    private int score; //score in game
    private boolean over; //if game is over
    
    /**
     * Creates a new Worm game.
     */
    public WormControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**
     * Required by KeyListener. Handles keyboard input 
     * for controlling the worm.
     */
    public void keyPressed(KeyEvent e)
    {
        if (myWorm != null)
        {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_UP)
                myWorm.moveUp();
            else if (keyCode == KeyEvent.VK_DOWN)
                myWorm.moveDown();
            else if (keyCode == KeyEvent.VK_LEFT)
                myWorm.moveLeft();
            else if (keyCode == KeyEvent.VK_RIGHT)
                myWorm.moveRight();
        }
    }
    
    /**
     * Initializes the game play components.
     */
    protected void initializeGameplayComponents()
    {
        double x = WIDTH/2.0;
        double y = HEIGHT/2.0;
        myWorm = new Worm(x, y, SIZE);
        
        apple = new Apple(randomX(), randomY(), SIZE);
        
        obstacles = new ArrayList<Obstacle>(); //no obstacles initially
        
        level = 1; //level 1 initially
        lives = START_LIVES; //lives initially
        score = 0; //score starts at zero
        
        over = false;
    }
    
    /**
     * Runs the game process. Checks for worm overlapping apple and/or 
     * obstacle or hitting wall.
     */
    protected void runProcess()
    {
        myWorm.step();
            
        //worm is out of bounds, overlapping itself, or hitting an obstacle
        if (!inBounds() || myWorm.overlapsSelf() || overlapsObstacle(myWorm))
        {
            lives--;
            if (lives <= 0)
                gameOver(); //end game
            else
                myWorm.resetWorm(); //re-place worm in center, not moving
        }
            
        //go to next level is apple was collected
        if (myWorm.overlaps(apple))
        {
            playSound("apple.wav");
            score += apple.bonus()*SCORE_INCREMENT;
            loadNextLevel();
        }
    }
    
    /**
     * Returns the description of the game.
     */
    public String description()
    {
        return "Eat apples and grow longer while avoiding " + 
               "obstacles and yourself!";
    }
    
    /**
     * Returns the name of the game.
     */
    public String name()
    {
        return "Worm";
    }

    //private methods
    /**
     * Loads the next level of the game.
     */
    private void loadNextLevel()
    {
        level++;
        if (level % 5 == 0) //new life every five levels
            lives++;
                
        //add one more segment to the end of the worm
        myWorm.addSegment();
        
        //re-place apple in random location
        apple.moveTo(randomX(), randomY());
        while(overlapsObstacle(apple)) //apple is too close to obstacle
            apple.moveTo(randomX(), randomY());
            
        //add one more obstacle once every three levels (multiples of 3)
        if (level % 3 == 0)
            obstacles.add(new Obstacle(randomX(), randomY(), SIZE));
        //increase worm's speed once every five levels (multiples of 5)    
        if (level % 5 == 0)
            myWorm.speedUp();
    }
    
    /**
     * Returns a random x location between 10 and window width - 10.
     */
    private double randomX()
    {
        Random rand = new Random();
        
        return rand.nextInt(WIDTH - 2*BORDER) + BORDER;
    }

    /**
     * Returns a random y location between 10 and window height - 10.
     */
    private double randomY()
    {
        Random rand = new Random();
        
        return rand.nextInt(HEIGHT - 2*BORDER) + BORDER;
    }
    
    /**
     * Returns whether or not the worm's head is within bounds.
     */
    private boolean inBounds()
    {
        double x = myWorm.x();
        double y = myWorm.y();
        double size = myWorm.segmentSize();
        return ((x >= 0) && (x + size <= WIDTH) && 
                (y >= 0) && (y + size <= HEIGHT));
    }
    
    /**
     * Ends the game, displaying the "game over" text.
     */
    private void gameOver()
    {
        over = true;
    }
    
    /**
     * Returns whether or not the specified shape overlaps an obstacle.
     */
    private boolean overlapsObstacle(Shape2D s)
    {
        for (Obstacle obs : obstacles)
            if (s.overlaps(obs))
                return true;
        return false;
    }
    
    /**
     * Returns whether or not this game can be scored.
     */
    public boolean isScorable()
    {
        return true;
    }
    
    /**
     * Paints the game to the container.
     */
    protected void paintComponent(Graphics g)
    {
        if (myWorm != null)
        {
            Graphics2D g2 = (Graphics2D) g;
            
            //paint background
            g2.setColor(Color.white);
            g2.fill(g2.getClipBounds());
            
            //paint score displays
            g2.setColor(Color.black);
            g2.drawString("Score: " + score, GAP, 3*GAP); //score
            g2.drawString("Level: " + level, WIDTH/2 - 15, 3*GAP); //level
            g2.drawString("Lives: " + lives, WIDTH - 60, 3*GAP); //lives
            
            //paint apple
            apple.paintComponent(g2);
            
            //paint obstacles
            for (Obstacle obs : obstacles)
                obs.paintComponent(g2);
            
            //paint worm last so it is on top
            myWorm.paintComponent(g2);
            
            if (over) //game over
            {
                g2.setColor(Color.red);
                g2.setFont(new Font("", Font.BOLD, 40));
                g2.drawString("Game Over", WIDTH/2 - 100, HEIGHT/2 - 10);
                pauseGame(); //stop game actions
                submitScore(score); //submit score
            }
        }
    }
    
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new WormControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}