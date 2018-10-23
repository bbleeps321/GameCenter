package Firewall;

//import
import java.awt.Color;

import GameComponents.BoundedEnv;
import GameComponents.Direction;
import GameComponents.Location;

/**
 * A Pierce acts just like a normal shot except when it can 
 * destroy all the bricks bricks in the same column.
 */
public class Pierce extends Shot
{
    //constants
    private static final Color COLOR = Color.green; //color of bomb
    
    /**
     * Creates a Pierce with nothing initialized. Should be used when 
     * using methods that do not require them to be (cost).
     */
    public Pierce()
    {
        super();
    }
    
    /**
     * Creates a Pierce at the specified location in the given 
     * environment.
     */
    public Pierce(BoundedEnv env, Location loc)
    {
        super(env, loc, COLOR, 3); //faster that normal shot
    }
    
    /**
     * Detonates the Pierce, removing it and any bricks in 
     * the same column from the environment.
     */
    protected void detonate()
    {
        Location lastLoc = myLoc;
        while (theEnv.isValid(lastLoc))
        {
            Location locAbove = theEnv.neighborOf(lastLoc, Direction.NORTH);
            if (!theEnv.isEmpty(locAbove) && //brick is there
                theEnv.isValid(locAbove)) 
                theEnv.remove(theEnv.objectAt(locAbove));
            lastLoc = locAbove;
        }
        theEnv.remove(this);
    }
    
    /**
     * Returns the cost of the shot.
     */
    public int cost()
    {
        return 12;
    }
}