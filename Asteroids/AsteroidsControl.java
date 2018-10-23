package Asteroids;

//import
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import GameComponents.Game;

/**
 * Controls the Asteroids game and its components.
 */
public class AsteroidsControl extends Game
{
    //constants
    private static final int WIDTH = 500; //width of window
    private static final int HEIGHT = 500; //height of window
    private static final int SCORE_INCREMENT = 100; //increment score grows by
    private static final String DIRECTORY = "Asteroids/"; //location of resources
    private static final int START_LIVES = 3; //number of lives initially
    private static final int MAX_ASTEROIDS = 9; //max number of asteroids
    //sizes of asteroids (redudant for probability)
    private static final int[] SIZES = {1, 1, 1, 1, 1, 1,
                                        2, 2, 2, 2, 3}; 
    
    private Ship myShip; //human-controlled ship in game
    private ArrayList<Asteroid> asteroids; //list of all asteroids in level
    private GameComponents.Canvas canvas; //canvas of game
    
    private int lives; //lives remaining
    private int score; //score in game
    private int level; //level of game
    private boolean over; //if game is over
    
    /**
     * Creates the AsteroidsControl.
     */
    public AsteroidsControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**
     * Fired when a key pressed event is invoked. Handles the 
     * control the ship.
     */
    public void keyPressed(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP)
            myShip.thrust();
        else if (keyCode == KeyEvent.VK_DOWN)
            myShip.reverse();
        else if (keyCode == KeyEvent.VK_LEFT)
            myShip.turnLeft();
        else if (keyCode == KeyEvent.VK_RIGHT)
            myShip.turnRight();
        else if (keyCode == KeyEvent.VK_CONTROL)
            myShip.brake();
        //weapons
        else if (keyCode == KeyEvent.VK_SPACE)
        {
            int cost = myShip.fireCost("Shot")*SCORE_INCREMENT;
            if (score - cost >= 0) //can afford it
            {
                myShip.fire("Shot");
                playSound("photon.wav");
                score -= cost;
            }
        }
        else if (keyCode == KeyEvent.VK_A)
        {
            int cost = myShip.fireCost("Bomb")*SCORE_INCREMENT;
            if (score - cost >= 0) //can afford it
            {
                myShip.fire("Bomb");
                playSound("missile.wav");
                score -= cost;
            }
        }
    }
    
    /**
     * Initialzies the game play components (asteroids, ship, etc.)
     */
    protected void initializeGameplayComponents()
    {
        //create canvas
        canvas = new GameComponents.Canvas(WIDTH, HEIGHT);
        
        //initialize score, level, lives, etc.
        level = 1;
        score = 0;
        lives = START_LIVES;
        over = false;
        
        //create asteroids, number equal to level
        asteroids = new ArrayList<Asteroid>();
        Random rand = new Random();
        for (int i = 0; i < level; i++)
        {
            int x = rand.nextInt(WIDTH);
            int y = rand.nextInt(HEIGHT);
            asteroids.add(new Asteroid(x, y, 1, canvas)); //small
        }
        
        //create ship at center of canvas
        myShip = new Ship(canvas.width()/2, canvas.height()/2, canvas);
    }
    
    /**
     * Runs the game process.
     */
    protected void runProcess()
    {
        //step asteroids
        for (Asteroid a : asteroids)
            a.step();
            
        //step ship
        myShip.step();
        
        //check if any shots overlap any asteroid
        for (int i = 0; i < asteroids.size(); i++)
        {
            for (int j = 0; j < myShip.numShots(); j++)
            {
                if (myShip.shotAt(j).overlaps(asteroids.get(i)))
                {
                    //destroy asteroid
                    ArrayList<Asteroid> debris = asteroids.get(i).explode(
                                                myShip.shotAt(j).power());
                                                
                    for (Asteroid a : debris) //add pieces to list
                        asteroids.add(a);
                            
                    asteroids.remove(i);
                    i--;
                    
                    //destroy shot
                    myShip.shotAt(j).detonate();
                    
                    //increment score
                    score += SCORE_INCREMENT*(debris.size() + 1);
                    
                    break; //break inner loop
                }
            }
        }
        
        //check if all asteroids on level have been destroyed
        if (allAsteroidsDestroyed())
            loadNextLevel();
            
        //check if ship is hit by asteroid
        for (Asteroid a : asteroids)
        {
            if (a.overlaps(myShip) && myShip.acted())
            {
                playSound("explosion.wav");
                lives--;
                if (lives <= 0)
                    gameOver();
                else
                    resetLevel();
                break;
            }
        }
    }
    
    /**
     * Returns whether or not all the asteroids have been destroyed.
     */
    private boolean allAsteroidsDestroyed()
    {
        for (Asteroid a : asteroids)
            if (!a.isHidden())
                return false;
        return true;
    }
    
    /**
     * Returns whether or not this game can be scored.
     */
    public boolean isScorable()
    {
        return true;
    }
    
    /**
     * Loads the next level of the game.
     */
    private void loadNextLevel()
    {
        level++;
        if (level % 5 == 0) //new life every five levels
            lives++;
            
        int numAsteroids = Math.min(level, MAX_ASTEROIDS);
        Random rand = new Random();
        for (int i = 0; i < numAsteroids; i++)
        {
            int x = rand.nextInt(WIDTH);
            int y = rand.nextInt(HEIGHT);
            asteroids.add(new Asteroid(x, y, randomSize(), canvas)); //small
        }
    }
    
    /**
     * Resets the current level to its initial state.
     */
    private void resetLevel()
    {
        myShip.reset();
        
        //reinitialize the asteroids
        asteroids.clear();
        int numAsteroids = Math.min(level, MAX_ASTEROIDS);
        Random rand = new Random();
        for (int i = 0; i < numAsteroids; i++)
        {
            int x = rand.nextInt(WIDTH);
            int y = rand.nextInt(HEIGHT);
            asteroids.add(new Asteroid(x, y, randomSize(), canvas)); //small
        }
    }
        
    /**
     * Returns a random size for an asteroid.
     */
    private int randomSize()
    {
        Random rand = new Random();
        int index = rand.nextInt(Math.min(level, SIZES.length));
        return SIZES[index];
    }
    
    /**
     * Returns the name of the game.
     */
    public String name()
    {
        return "Asteroids";
    }
    
    /**
     * Returns a description of the game.
     */
    public String description()
    {
        return "Destroy all the asteroids without crashing into them!";
    }

    /**
     * Ends the game.
     */
    private void gameOver()
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
        Image pic = sprite.getSprite("asteroids/space.gif");
        g2.drawImage(pic, 0, 0, WIDTH, HEIGHT, null);
        
        //paint each asteroid
        for (Asteroid a : asteroids)
            a.paintComponent(g2);
            
        //paint ship
        myShip.paintComponent(g2);
            
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
    
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new AsteroidsControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}