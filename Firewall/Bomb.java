package Firewall;

//import
import java.awt.Color;
import java.util.ArrayList;

import GameComponents.BoundedEnv;
import GameComponents.Direction;
import GameComponents.Location;

/**
 * A Bomb acts just like a normal shot except when it hits, 
 * a one-unit radius of bricks are destroyed.
 */
public class Bomb extends Shot
{
    //constants
    private static final Color COLOR = Color.red; //color of bomb
    
    /**
     * Creates a bomb with nothing initialized. Should be used when 
     * using methods that do not require them to be (cost).
     */
    public Bomb()
    {
        super();
    }
    
    /**
     * Creates a Bomb at the specified location in the given 
     * environment.
     */
    public Bomb(BoundedEnv env, Location loc)
    {
        super(env, loc, COLOR, 1); //slower than normal shot
    }
    
    /**
     * Detonates the bomb, removing it and any bricks in its 
     * range from the environment.
     */
    protected void detonate()
    {
        Location locAbove = theEnv.neighborOf(myLoc, Direction.NORTH);
        ArrayList<Location> nbrs = theEnv.getNeighbors8(locAbove); //all neighbors
        for (Location loc : nbrs)
            if (!theEnv.isEmpty(loc)) //brick is there
                theEnv.remove(theEnv.objectAt(loc));
        super.detonate(); //remove loc above's item and self
    }
    
    /**
     * Returns the cost of the shot.
     */
    public int cost()
    {
        return 5;
    }
}