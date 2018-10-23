package TowerDefense;

//import
import java.awt.Color;
import java.util.ArrayList;

import GameComponents.BoundedEnv;
import GameComponents.Location;

/**
 * A creep that moves faster than a normal creep.
 */
public class Fast extends Creep
{
    //constants
    private static final int DELAY = 2; //delay of fast creep
    private static final Color COLOR = Color.green; //color of fast creep
    
    /**
     * Creates a fast creep at the specified location in the given 
     * environment with the specified path.
     */
    public Fast(BoundedEnv env, Location loc, ArrayList<Location> path, int hp)
    {
        super(env, loc, path, hp, COLOR, DELAY);
    }
}