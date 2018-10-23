package TowerDefense;

//import
import java.util.ArrayList;

import GameComponents.BoundedEnv;
import GameComponents.Location;

/**
 * A Tower with an exceptionally long range.
 */
public class Rapid extends Tower
{
    //constants
    private static final String IMAGE = "towerdefense/rapid_"; //need add level
    protected static final int DAMAGE = 2; //damage of sniper tower
    protected static final int DELAY = 8; //delay of tower
    
    /**
     * Creates a dummy sniper tower
     */
    public Rapid()
    {
        super();
    }
    
    /**
     * Creates a sniper tower at the specified location in the environment 
     * with the given list of targets.
     */
    public Rapid(BoundedEnv env, Location loc, ArrayList<Creep> targList)
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
                myDmg *= 2;
            }
            else if (myLevel == 5)
            {
                myDmg *= 2.5;
                myRng *= 2.5;
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
        return "Rapid " + myLevel;
    }
    
    /**
     * Returns the description of the tower.
     */
    public String description()
    {
        return "Rapid tower";
    }
    
    /**
     * Returns the cost of building/upgrading the tower.
     */
    public int cost()
    {
        if (myLevel == 0) //not built
            return 15;
        else if (myLevel == 1)
            return 30;
        else if (myLevel == 2)
            return 50;
        else if (myLevel == 3)
            return 80;
        else if (myLevel == 4)
            return 160;
        else
            return 0; //cannot be upgraded
    }
}