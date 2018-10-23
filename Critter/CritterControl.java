package Critter;

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

import GameComponents.BoundedEnv;
import GameComponents.Direction;
import GameComponents.Game;
import GameComponents.Locatable;
import GameComponents.Location;
import GameComponents.Simulation;
import GameComponents.Steppable;

/**
 * Controls the game functions of the Critter game.
 */
public class CritterControl extends Game implements ActionListener, KeyListener
{
    //constants
    private static final String DIRECTORY = "Critter/"; //where game resources are
    private static final int WIDTH = 500; //width of window
    private static final int HEIGHT = 500; //height of window
    private static final int SCORE_INCREMENT = 100; //increment score grows by
    private static final int START_LIVES = 3; //number of lives initially
    private static final int SIZE = 50; //size of each cell
    private static final int MAX_DELAY = 7; //maximum of delay of ai critters
    private static final int MIN_DELAY = 3; //minimum delay of ai critters
    private static final int MAX_CRITTER = 10; //max number of critters on level
    
    //game play components
    private Critter myCritter; //Critter that can be controlled
    private ArrayList<Steppable> aiCritters; //list of AI controlled critters
    private BoundedEnv itemEnv; //environment containing all items
    private BoundedEnv theEnv; //environment of game
    private Simulation theSim; //simulation for controlling ai critters
    
    private int level; //game level critter is on
    private int lives; //lives critter has left
    private int score; //score in game
    private boolean over; //if game is over
    
    /**
     * Creates a new Critter game.
     */
    public CritterControl()
    {
        super(WIDTH, HEIGHT, DIRECTORY);
    }
    
    /**
     * Required by KeyListener. Handles keyboard input 
     * for controlling the Critter.
     */
    public void keyPressed(KeyEvent e)
    {
        if (myCritter != null)
        {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_UP)
                myCritter.moveUp();
            else if (keyCode == KeyEvent.VK_DOWN)
                myCritter.moveDown();
            else if (keyCode == KeyEvent.VK_LEFT)
                myCritter.moveLeft();
            else if (keyCode == KeyEvent.VK_RIGHT)
                myCritter.moveRight();
        }
    }
    
    /**
     * Initializes the game play components.
     */
    protected void initializeGameplayComponents()
    {
        level = 1; //level 1 initially
        lives = START_LIVES; //lives initially
        score = 0; //score starts at zero
        
        aiCritters = new ArrayList<Steppable>();
        
        //create environment
        theEnv = new BoundedEnv(WIDTH/SIZE, HEIGHT/SIZE, SIZE, SIZE);
        
        //create human critter
        myCritter = new Critter(theEnv, new Location(0,0));
        
        //create the item environment
        itemEnv = createItemEnvironment();
        
        //create number of ai critters equal to the level with random speeds
        Random rand = new Random();
        for (int i = 0; i < level; i++)
        {
            int spd = rand.nextInt(MAX_DELAY) + MIN_DELAY;
            aiCritters.add(new Critter(theEnv, randomEmptyLocation(), 
                                Direction.randomDirection(), spd));
        }
        
        //create simulation to control ai critters
        theSim = new Simulation(aiCritters);
        over = false;
    }
    
    /**
     * Runs the game process.
     */
    protected void runProcess()
    {
        theSim.step(); //step ai critters
            
        //check if human critter overlaps any ai critters
        if (crittersOverlap())
        {
            lives--;
            sound.playSound(DIRECTORY + "die.au");
            if (lives <= 0)
                gameOver();
            else
                loadNextLevel(false);
        }
        
        //check if human critter overlaps item
        Item item = (Item)itemEnv.objectAt(myCritter.loc()); 
        if (item != null)
        {
            if (item.good()) //good item
            {
                score += SCORE_INCREMENT;
                itemEnv.remove(item);
                playSound("eat.au");
                if (!goodItemsLeft()) //make next level if all good items gone
                    loadNextLevel(true);
            }
            else //bad item
            {
                lives--;
                playSound("die.au");
                if (lives <= 0)
                    gameOver();
                else
                    loadNextLevel(false);
            }
        }
    }
    
    /**
     * Returns the description of the game.
     */
    public String description()
    {
        return "Collect all the green objects while avoiding everything else!";
    }
    
    /**
     * Returns the name of the game.
     */
    public String name()
    {
        return "Critter";
    }

    //private methods
    /**
     * Loads the next level of the game.
     */
    private void loadNextLevel(boolean growLevel)
    {
        Random rand = new Random();
        if (growLevel)
        {
            level++;
            if (level % 5 == 0) //new life every five levels
                lives++;
            
            if (level % 2 == 1 && //new critter every two levels
                aiCritters.size() + 1 <= MAX_CRITTER) 
            {
                int spd = rand.nextInt(MAX_DELAY) + MIN_DELAY;
                aiCritters.add(new Critter(theEnv, randomEmptyLocation(), 
                                    Direction.randomDirection(), spd));
            }
        }
            
        
        //move human critter to new location
        myCritter.changeLocation(randomEmptyLocation());
        
        //reset ai critters' locations, directions, and speed randomly
        for (int i = 0; i < aiCritters.size(); i++)
        {
            Critter crit = (Critter)aiCritters.get(i);
            crit.changeLocation(randomEmptyLocation());
            crit.changeDirection(Direction.randomDirection());
            crit.changeDelay(rand.nextInt(MAX_DELAY) + MIN_DELAY);
        }
        
        //create new item env
        itemEnv = createItemEnvironment();
    }
    
    /**
     * Returns whether or not their are any more good items on the level.
     */
    private boolean goodItemsLeft()
    {
        int count = 0;
        ArrayList<Locatable> objs = itemEnv.allObjects();
        for (int i = 0; i < objs.size(); i++)
            if (((Item)objs.get(i)).good())
                count++;
                
        return count > 0;
    }
    
    /**
     * Sets up an environment filled with items.
     */
    private BoundedEnv createItemEnvironment()
    {
        BoundedEnv env = new BoundedEnv(WIDTH/SIZE, HEIGHT/SIZE, SIZE, SIZE);
        
        //create path of good items
        createPath(env);
        
        //fill all other locations with random items
        ArrayList<Location> emptyLocs = env.allEmptyLocations();
        for (Location loc : emptyLocs)
            new Item(env, loc, false);
            
        //remove item at human critter's location
        Location startLoc = myCritter.loc(); //location where critter starts
        env.remove(env.objectAt(startLoc));
        
        return env;
    }
    
    /**
     * Creates a random path of items in the given item.
     */
    private void createPath(BoundedEnv env)
    {
        Random rand = new Random();
        
        int maxPathLength = (int)((WIDTH/SIZE)*(HEIGHT/SIZE)/6);
        int minPathLength = (int)((WIDTH/SIZE)*(HEIGHT/SIZE)/12);
        int pathLength = rand.nextInt(maxPathLength) + minPathLength;

        Location lastLoc = myCritter.loc(); //start from critter's location
        for (int i = 0; i < pathLength; i++)
        {
            ArrayList<Location> locChoices = env.emptyNeighbors(lastLoc);
            int size = locChoices.size();
            if(size <= 0)
                break;
                
            //remove human critter's location as a choice
            int critLocIndex = locChoices.indexOf(myCritter.loc());
            if (critLocIndex != -1) //a choice
                locChoices.remove(critLocIndex);
                
            size = locChoices.size();
            if(size <= 0)
                break;
                
            int index = rand.nextInt(locChoices.size());
            Location loc = locChoices.get(index);
            new Item(env, loc, true);
            lastLoc = locChoices.get(index);
        }
    }
    
    /**
     * Returns a random empty location in the environment that does 
     * not share any column or row as the human critter. 
     */
    private Location randomEmptyLocation()
    {
        Random rand = new Random();
        int x = rand.nextInt(theEnv.width());
        int y = rand.nextInt(theEnv.height());
        Location loc = new Location(x,y);
        Location myLoc = myCritter.loc();
        while (!theEnv.isEmpty(loc) || loc.x() == myLoc.x() || 
                                       loc.y() == myLoc.y())
        {
            x = rand.nextInt(theEnv.width());
            y = rand.nextInt(theEnv.height());
            loc = new Location(x,y);
        }
        
        return loc;
    }
    
    /**
     * Ends the game, displaying the "game over" text.
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
     * Returns whether or not the human critter overlaps any 
     * ai critters.
     */
    private boolean crittersOverlap()
    {
        for (int i = 0; i < aiCritters.size(); i++)
            if (myCritter.overlaps((Critter)aiCritters.get(i)))
                return true;
        return false;
    }
    
    /**
     * Paints the game to the container.
     */
    protected void paintComponent(Graphics g)
    {
        if (myCritter != null)
        {
            Graphics2D g2 = (Graphics2D) g;
            
            //paint background
            g2.setColor(Color.white);
            g2.fill(g2.getClipBounds());
            
            //paint items
            ArrayList<Locatable> objs = itemEnv.allObjects();
            for (Locatable obj : objs)
                ((Item)obj).paintComponent(g2);
                
            //paint ai critters
            for (int i = 0; i < aiCritters.size(); i++)
                ((Critter)aiCritters.get(i)).paintComponent(g2);
                
            //paint human critter
            myCritter.paintComponent(g2);
            
            //paint score displays last to be on top
            g2.setColor(Color.red);
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
     * Runs the game.
     */
    public static void main(String[] args)
    {
        Game gui = new CritterControl();
        JFrame frame = new JFrame(gui.name());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(150, 120));
        
        frame.setContentPane(gui.getOptionsPanel());
        
        frame.pack();
        frame.setVisible(true);
    }
}