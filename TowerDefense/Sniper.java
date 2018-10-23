package TowerDefense;

//import
import java.util.ArrayList;

import GameComponents.BoundedEnv;
import GameComponents.Location;

/**
 * A Tower with an exceptionally long range.
 */
public class Sniper extends Tower
{
    //constants
    private static final String IMAGE = "towerdefense/sniper_"; //need add level
    private static final int RANGE = 8; //range of sniper tower
    private static final int DAMAGE = 3; //damage of sniper tower
    private static final int DELAY = 35; //delay of tower
    
    /**
     * Creates a dummy sniper tower
     */
    public Sniper()
    {
        super();
    }
    
    /**
     * Creates a sniper tower at the specified location in the environment 
     * with the given list of targets.
     */
    public Sniper(BoundedEnv env, Location loc, ArrayList<Creep> targList)
    {
        super(env, loc, targList, IMAGE, RANGE, DAMAGE, DELAY);
    }
    
    /**
     * Upgrades the Tower's damage and/or range, based on level.
     */
    public void upgrade()
    {
        if (myLevel < MAX_LEVEL)
        {
            myLevel++;
            if (myLevel == 2)
            {
                myDmg *= 1.5;
            }
            else if (myLevel == 3)
            {
                myDmg *= 1.5;
            }
            else if (myLevel == 4)
            {
                myDmg *= 1.5;
            }
            else if (myLevel == 5)
            {
                myDmg *= 2;
                myRng *= 1.5;
            }
            //set up image
            myImage = sprite.getSprite(IMAGE + myLevel + EXTENSION);
        }
    }
    
    /**
     * Returns the name of the tower.
     */
    public String name()
    {
        return "Sniper " + myLevel;
    }
    
    /**
     * Returns the description of the tower.
     */
    public String description()
    {
        return "Sniper tower";
    }
    
    /**
     * Returns the cost of building/upgrading the tower.
     */
    public int cost()
    {
        if (myLevel == 0) //not built
            return 30;
        else if (myLevel == 1)
            return 50;
        else if (myLevel == 2)
            return 60;
        else if (myLevel == 3)
            return 80;
        else if (myLevel == 4)
            return 150;
        else
            return 0; //cannot be upgraded
    }
}