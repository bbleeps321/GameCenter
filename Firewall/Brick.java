package Firewall;

//import
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import GameComponents.BoundedEnv;
import GameComponents.Direction;
import GameComponents.Location;
import GameComponents.Shape2D;
import GameComponents.Shape2DLocatable;
import GameComponents.Steppable;

/**
 * A Brick is a single part of a wall. A Brick can have different 
 * durability values. A Brick is represented by a colored rectangle.
 * In each step, a brick will move down one unit.
 */
public class Brick extends Shape2DLocatable implements Steppable
{
    //constants
    private static final String IMAGE_NAME = "firewall/fire.gif"; //name of image

    private int myDur; //durability of brick
    
    /**
     * Creates a brick at the specified Location in the 
     * given environment with the specified durability.
     */
    public Brick(BoundedEnv env, Location loc, int durability)
    {
        super(env, loc, IMAGE_NAME); //values defined later
        myDur = durability;
    }
    
    /**
     * Steps the brick, moving it down one unit.
     */
    public void step()
    {
        if (!theEnv.contains(this))
            return;
            
        Location loc = theEnv.neighborOf(myLoc, Direction.SOUTH);
        if (theEnv.isValid(loc))
            changeLocation(loc);
    }
    
    /**
     * Damages the brick, decreasing its durability by the
     * given amount. If the durability becomes less than 
     * or equal to one, the brick will disappear. Returns 
     * whether or not the brick was destroyed.
     */
    public boolean damage(int amt)
    {
        myDur -= amt;
        
        if (myDur <= 0) //destroyed
        {
            theEnv.remove(this);
            return true; //brick destroyed;
        }
        return false; //brick still alive
    }
    
    /**
     * Returns the shape representation of this brick.
     */
    public Shape shape()
    {
        return new Rectangle2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Returns whether or not the shape overlaps this brick.
     */
    public boolean overlaps(Shape2D s)
    {
        if (!theEnv.contains(this))
            return false;
        return super.overlaps(s);
    }
}