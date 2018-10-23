package Critter;

//import
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import GameComponents.BoundedEnv;
import GameComponents.Direction;
import GameComponents.Location;
import GameComponents.Shape2DLocatable;
import GameComponents.Steppable;

/**
 * A Critter is controlled by either a player or an AI 
 * and can move up, down, left, or right in a bounded 
 * environment. If controlled by an AI, can move in either 
 * the up and down directions or left and right directions.
 */
public class Critter extends Shape2DLocatable implements Steppable
{
    //constants
    private static final double DIR_CHANGE_PROB = .1; //prob of changing dir
    
    //for ai
    private Direction myDir; //direction of critter (null if human controlled)
    private Direction dirFacing; //direction critter is facing
    private int myDelay; //moves once every this many timesteps
    private int stepCount; //how many steps since last move
    private int stepCountDir; //how many steps since last direction change
    
    /**
     * Creates a critter at the specified Location in the specified 
     * environment. Constructor for human controlled critter.
     */
    public Critter(BoundedEnv env, Location loc)
    {
        super(env, loc);
        
        myDir = null;
        
        dirFacing = Direction.NORTH;
    }
    
    /**
     * Creates a critter at the specified Location in the specified 
     * environment with the initial direction. Constructor for AI 
     * controlled critter.
     */
    public Critter(BoundedEnv env, Location loc, Direction dir, int delay)
    {
        super(env, loc);

        myDir = dir;
        myDelay = delay;
        
        dirFacing = myDir;
    }
    
    /**
     * Steps the critter in its direction. For use when 
     * AI controlled.
     */
    public void step()
    {
        //assert that this critter is AI controlled
        if (myDir == null)
            return;
            
        //check for delay
        if (stepCount >= myDelay) //move critter 
        {
            Location loc = theEnv.neighborOf(myLoc, myDir);        
            if (theEnv.isValid(loc)) //location is in bounds
                changeLocation(loc);
            else
                myDir = myDir.reverse(); //reverse direction
            stepCount = 0; //reset
            dirFacing = myDir;
//             //randomly decide whether or not to change direction
//             Random rand = new Random();
//             if (rand.nextDouble() < DIR_CHANGE_PROB)
//                 myDir = Direction.randomDirection();
        }
        else
        {
            stepCount++;
        }
    }
    
    /**
     * Moves the critter up.
     */
    public void moveUp()
    {
        Location loc = theEnv.neighborOf(myLoc, Direction.NORTH);
        if (theEnv.isValid(loc)) //location in bounds
            changeLocation(loc);
            
        dirFacing = Direction.NORTH;
    }
    
    /**
     * Moves the critter down.
     */
    public void moveDown()
    {
        Location loc = theEnv.neighborOf(myLoc, Direction.SOUTH);
        if (theEnv.isValid(loc)) //location in bounds
            changeLocation(loc);
            
        dirFacing = Direction.SOUTH;
    }
    
    /**
     * Moves the critter left.
     */
    public void moveLeft()
    {
        Location loc = theEnv.neighborOf(myLoc, Direction.WEST);
        if (theEnv.isValid(loc)) //location in bounds
            changeLocation(loc);
            
        dirFacing = Direction.WEST;
    }
    
    /**
     * Moves the critter right.
     */
    public void moveRight()
    {
        Location loc = theEnv.neighborOf(myLoc, Direction.EAST);
        if (theEnv.isValid(loc)) //location in bounds
            changeLocation(loc);
            
        dirFacing = Direction.EAST;
    }
    
    /**
     * Changes the direction of the critter to the given one.
     * Will enable the ai.
     */
    public void changeDirection(Direction dir)
    {
        myDir = dir;
    }
    
    /**
     * Changes the delay of the critter to the given one.
     */
    public void changeDelay(int delay)
    {
        myDelay = delay;
    }
    
    /**
     * Returns the shape representation of this critter.
     */
    public Shape shape()
    {
        return new Rectangle2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Paints the critter onto the container.
     */
    public void paintComponent(Graphics2D g2)
    {
        String dirSign = "";
        if (dirFacing.equals(Direction.NORTH)) //facing north
            dirSign = "N";
        else if (dirFacing.equals(Direction.SOUTH)) //facing south
            dirSign = "S";
        else if (dirFacing.equals(Direction.WEST)) //facing west
            dirSign = "W";
        else if (dirFacing.equals(Direction.EAST)) //facing east
            dirSign = "E";
            
        if (myDir == null) //human controlled
            myImage = sprite.getSprite("critter/good_bug" + dirSign + ".gif");
        else //ai controlled
            myImage = sprite.getSprite("critter/bad_bug" + dirSign + ".gif");
            
        super.paintComponent(g2);
    }
}