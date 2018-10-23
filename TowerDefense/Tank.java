package TowerDefense;

//import
import java.awt.Color;
import java.util.ArrayList;

import GameComponents.BoundedEnv;
import GameComponents.Location;

/**
 * A creep that moves slower than a normal creep but has more health
 */
public class Tank extends Creep
{
    //constants
    private static final int DELAY = 8; //delay of tank creep
    private static final Color COLOR = Color.orange; //color of creep
    private static final int BASE_HP = 15; //base hp of tank
    
    /**
     * Creates a tank creep at the specified location in the given 
     * environment with the specified path.
     */
    public Tank(BoundedEnv env, Location loc, ArrayList<Location> path, int hp)
    {
        super(env, loc, path, hp, COLOR, DELAY);
    }
    
    /**
     * Returns the base (initial) hp of a creep of this type.
     */
    public int baseHP()
    {
        return BASE_HP;
    }
}