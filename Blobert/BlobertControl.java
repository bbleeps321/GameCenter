package Blobert;

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
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import GameComponents.Game;
import GameComponents.Simulation;
import GameComponents.Steppable;

/**
 * Controls the Blobert game's functions.
 */
public class BlobertControl extends Game implements KeyListener, ActionListener
{
    //constants
    private static final String DIRECTORY = "blobert/"; //where game resources are
    private static final int WIDTH = 500; //width of screen
    private static final int HEIGHT = 500; //height of screen
    private static final int APPLE_MAX = 10; //total number of apples
    private static final int SCORE_INCREMENT = 100; //increment score grows by
    private static final int START_LIVES = 3; //number of lives initially
    private static final String PACKAGE = "Blobert."; //package game is in
    
    //blobert choices
    private static final String[] BLOB_CHOICES = {"Blobert",
                                                  "SizeChanger",
                                                  "Reflector",
                                                  "MineLayer",
                                                  "Phaser"};
    //ghost choices (duplicated to simulate a higher chance of being picked)
    private static final String[] GHOST_CHOICES = 
    {
        "Ghost", "Ghost", "Ghost", "Ghost",
        "Phantom", "Phantom", "Phantom",
        "Ghoul", "Ghoul",
    };
    
    //components for options panel
    private JComboBox blobSelect; //combobox for selecting what blobert
    
    //variables for gameplay
    private GameComponents.Canvas canvas; //canvas of game
    private Blobert myBlobert; //blobert that it controlled
    private ArrayList<Apple> apples; //list of apples that can be collected
    private ArrayList<Ghost> graveyard; //graveyard of ghosts
    private Simulation theSim; //simulation controlling all the steppable objects
    
    private int applesCollected; //apples collected on this level
    private int level; //game level blobert is on
    private int lives; //lives blobert has left
    private int score; //score in game
    private boolean over; //if game is over
    
    /**
     * Initializes the game.
     */
    public BlobertControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**
     * Returns the panel containing the options of the game.
     */
    public JPanel getOptionsPanel()
    {
        //create components 
        blobSelect = new JComboBox(BLOB_CHOICES);
        blobSelect.setSelectedIndex(0);
        Border emptyBorder = BorderFactory.createEmptyBorder(GAP,GAP,GAP,GAP);
        blobSelect.setBorder(BorderFactory.createTitledBorder(emptyBorder, 
                                "Choose Your Blobert "));
        
        //place in panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(150,60));
        panel.add(blobSelect, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Required by KeyListener. Handles keyboard input 
     * for controlling the blobert.
     */
    public void keyPressed(KeyEvent e)
    {
        if (myBlobert != null)
        {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_UP)
                myBlobert.moveUp();
            else if (keyCode == KeyEvent.VK_DOWN)
                myBlobert.moveDown();
            else if (keyCode == KeyEvent.VK_LEFT)
                myBlobert.moveLeft();
            else if (keyCode == KeyEvent.VK_RIGHT)
                myBlobert.moveRight();
            else if (keyCode == KeyEvent.VK_SPACE)
                myBlobert.brake();
            //abilities
            else if (keyCode == KeyEvent.VK_A)
            {
                //can afford points to act
                if (score - myBlobert.act1Cost()*SCORE_INCREMENT >= 0)
                {
                    myBlobert.act1();
                    score -= myBlobert.act1Cost()*SCORE_INCREMENT;
                }
            }
            else if (keyCode == KeyEvent.VK_S)
            {
                //can afford points to act
                if (score - myBlobert.act2Cost()*SCORE_INCREMENT >= 0)
                {
                    myBlobert.act2();
                    score -= myBlobert.act2Cost()*SCORE_INCREMENT;
                }
            }
        }
    }
    
    /**
     * Initializes the gameplay components.
     */
    protected void initializeGameplayComponents()
    {
        //create canvas
        canvas = new GameComponents.Canvas(WIDTH,HEIGHT);
        
        //create graveyard
        graveyard = new ArrayList<Ghost>();
        
        //create selected blobert in center of screen
        String className = (String)blobSelect.getSelectedItem();
        double x = WIDTH/2;
        double y = HEIGHT/2;
        myBlobert = Blobert.createInstanceOf(className, x, y, canvas, graveyard);
        
        //add one ghost
        addGhost(GHOST_CHOICES[0]);
        
        //create apples and randomly place them in locations
        apples = new ArrayList<Apple>();
        Random rand = new Random();
        for (int i = 0; i < APPLE_MAX; i++)
        {
            x = rand.nextInt(WIDTH - Apple.SIZE);
            y = rand.nextInt(WIDTH - Apple.SIZE);
            
            apples.add(new Apple(x,y));
        }
        
        applesCollected = 0; //no apples collected initially
        level = 1; //level 1 initially
        lives = START_LIVES; //lives initially
        score = 0; //score starts at zero
        
        ArrayList<Steppable> list = new ArrayList<Steppable>();
        list.add(myBlobert); //add blobert
        for (Ghost g : graveyard) //add ghosts
            list.add(g);
        theSim = new Simulation(list);
        
        over = false;
    }
    
    /**
     * Runs the game process. Checks for blobert overlap of apple and/or 
     * ghost. Steps ghosts and blobert.
     */
    protected void runProcess()
    {
        //check if is overlapping apple.
        for (Apple apple : apples)
        {
            if (myBlobert.overlaps(apple) &&
                !apple.collected())
            {
                playSound("apple.wav");
                apple.setCollected(true);
                applesCollected++;
                score += level*SCORE_INCREMENT;
                if (applesCollected == APPLE_MAX) //all apples on level 
                    loadNextLevel();
            }
        }
        
        //check if overlapping ghost
        for (Ghost g : graveyard)
        {
            //blobert intersects ghost and blobert has acted
            if (myBlobert.overlaps(g) && myBlobert.hasActed())
            {
                playSound("die.au");
                lives -= g.damageEffect();
                if (lives <= 0) //no lives left
                    gameOver(); //end game
                else
                    myBlobert.resetBlobert(); //re=place blobert in center
            }
        }
        
        theSim.step();
    }
    
    /**
     * Loads the next level of the game.
     */
    private void loadNextLevel()
    {
        level++;
        if (level % 5 == 0) //new life every five levels
            lives++;
            
        //re-place apples in random locations
        Random rand = new Random();
        double x, y; //new (x,y) coordinates of apple
        for (Apple apple : apples)
        {
            x = rand.nextInt(WIDTH - Apple.SIZE);
            y = rand.nextInt(WIDTH - Apple.SIZE);
            
            apple.moveTo(x,y);
            apple.setCollected(false);
        }
        
        //add one more ghost of random type to the level
        int index = rand.nextInt(Math.min(level, GHOST_CHOICES.length));
        addGhost(GHOST_CHOICES[index]);
        
        //reset movement of ghosts and revive them
        resetGhosts();
        
        //reset number of apples collected on this level to 0
        applesCollected = 0;
    }

    /**
     * Required by abstract class Game. Returns the name of the game.
     */
    public String name()
    {
        return "Blobert";
    }
    
    /**
     * Required by abstract class Game. Returns the description of the game.
     */
    public String description()
    {
        return "Avoid ghosts while trying to collect apples!";
    }
    
    /**
     * Adds a Ghost of the specified string name to the arraylist.
     */
    private void addGhost(String ghostName)
    {
        Ghost g = Ghost.createInstanceOf(ghostName, canvas);
        graveyard.add(g);
        
        if (theSim != null)
            theSim.add(g);
    }
    
    /**
     * Resets the ghosts in the environment to be moving in new 
     * directions.
     */
    private void resetGhosts()
    {
        for (Ghost g : graveyard)
        {
            if (g.isHidden())
                g.showGhost();
            g.reset();
        }
    }
    
    /**
     * Paints the game to the container.
     */
    protected void paintComponent(Graphics g)
    {
        if (myBlobert != null)
        {
            Graphics2D g2 = (Graphics2D) g;
            
            //paint background
            g2.setColor(Color.black);
            g2.fill(g2.getClipBounds());
            
            //paint uncollected apples
            for (Apple apple : apples)
                apple.paintComponent(g2);
            
            //paint ghosts
            for (Ghost ghost : graveyard)
                ghost.paintComponent(g2);
            
            //paint blobert
            myBlobert.paintComponent(g2);
            
            //paint score displays
            g2.setColor(Color.white);
            g2.drawString("Score: " + score, GAP, 3*GAP); //score
            g2.drawString("Level: " + level, WIDTH/2 - 15, 3*GAP); //level
            g2.drawString("Lives: " + lives, WIDTH - 60, 3*GAP); //lives
            
            if (over) //game over
            {
                g2.setColor(Color.red);
                g2.setFont(new Font("", Font.BOLD, 40));
                g2.drawString("Game Over", WIDTH/2 - 100, HEIGHT/2 - 10);
                pauseGame(); //stop game actions
                submitScore(score);
            }
        }
    }
    
    /**
     * Ends the game, displaying a "game over" text.
     */
    private void gameOver()
    {
        over = true;
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
        Game gui = new BlobertControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}