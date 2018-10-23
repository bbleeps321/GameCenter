package TowerDefense;

//import
import java.awt.Color;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import GameComponents.BoundedEnv;
import GameComponents.Location;
import GameComponents.Shape2DLocatable;
import GameComponents.Steppable;

/**
 * A Creep is ai controlled. It seeks the best path 
 * through a maze and and tries to manuever through it.
 */
public class Creep extends Shape2DLocatable implements Steppable, Unit
{
    //constants
    protected static final Color COLOR = Color.gray; //default color of creep
    protected static final int DELAY = 5; //default step delay (15)
    protected static final int BASE_HP = 10; //base hp of creep
    
    private ArrayList<Location> myPath; //path creep is planning to travel
    private ArrayList<Location> past; //list of locations creep has moved on
    
    protected int myHP; //hit points of creep
    protected int myInitHP; //initial hit points of creep
    protected int myDelay; //how many times step\() must be called for it to move
    protected int stepCount;
    
    /**
     * Creates a creep at the specified location in the given 
     * environment.
     */
    public Creep(BoundedEnv env, Location loc, ArrayList<Location> path, int hp)
    {
        super(env, loc, COLOR);

        myHP = hp;
        myInitHP = myHP;
        myDelay = DELAY;
        myPath = path;
        
        past = new ArrayList<Location>();
        past.add(myLoc);
        
        stepCount = 0;
    }
    
    /**
     * Creates a creep at the specified location in the given 
     * environment with the specified parameters.
     */
    protected Creep(BoundedEnv env, Location loc, ArrayList<Location> path,
                    int hp, Color col, int delay)
    {
        super(env, loc, col);

        myHP = hp;
        myInitHP = myHP;
        myDelay = delay;
        myPath = path;
        
        past = new ArrayList<Location>();
        past.add(myLoc);
        
        stepCount = 0;
    }
    
    /**
     * Creates an instance of a creep with the given class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Creep createInstanceOf(String className, BoundedEnv env, 
                                                Location loc, 
                                                ArrayList<Location> path,
                                                int hp)
    {
        try
        {
            //find constructor requiring environment, location, arraylist, int
            Class classToCreate = Class.forName("TowerDefense."+className);
            Class[] params = {env.getClass(), loc.getClass(), path.getClass(),
                                    Integer.TYPE};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] argList = {env, loc, path, hp};
            return (Creep)constructor.newInstance(argList);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Steps the creep if it is still in the environment.
     */
    public void step()
    {
        if (stepCount >= myDelay)
        {
            //reached end
            if (myLoc.equals(myPath.get(myPath.size() - 1)))
                theEnv.remove(this);
            
            if (!theEnv.contains(this))
                return;
            
            //find adjacent location that is on path
            Location nextLoc = null;
            ArrayList<Location> nbrs = theEnv.getNeighbors(myLoc);
            for (Location loc : nbrs)
                //location on path but has not been on it before
                if (myPath.contains(loc) && !past.contains(loc)) 
                    nextLoc = loc;
            if (theEnv.isEmpty(nextLoc))
                changeLocation(nextLoc);
                
            stepCount = 0;
        }
        else
        {
            stepCount++;
        }
    }

    /**
     * Returns the shape representation of the creep.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Returns the name of the creep.
     */
    public String name()
    {
        return "Creep";
    }
    
    /**
     * Returns the description of the creep.
     */
    public String description()
    {
        return "Basic creep with minimal abilities.";
    }
    
    /**
     * Returns the image representation of the creep.
     */
    public Image image()
    {
        return myImage;
    }
    
    /**
     * Returns the cost of the unit, which is the money bonus 
     * a creep provided when killed.
     */
    public int cost()
    {
        return myInitHP/baseHP();
    }
    
    /**
     * Returns whether or not this creep is a dummy.
     */
    public boolean isDummy()
    {
        return false;
    }
    
    /**
     * Damages the creep by the specified amount. If the creep's 
     * HP goes below 0, it dies.
     */
    public void hurt(int amt)
    {
        myHP -= amt;
        if (myHP <= 0)
            die();
    }
    
    /**
     * Makes the creeps die, removing it from the environment.
     */
    public void die()
    {
        theEnv.remove(this);
    }
    
    /**
     * Returns the base (initial) hp of a creep of this type.
     */
    public int baseHP()
    {
        return BASE_HP;
    }
    
    /**
     * Sets the hp of the creep to the specified value.
     */
    public void setHP(int newVal)
    {
        myHP = newVal;
        myInitHP = myHP;
    }
    
    /**
     * Changes the location of the creep to the specified one 
     * and updates the location in the environment.
     */
    public void changeLocation(Location loc)
    {
        super.changeLocation(loc);
        past.add(myLoc);
    }
    
    /**
     * Returns a string representation of this creep.
     */
    public String toString()
    {
        return this.getClass().getName() + "[loc=" + myLoc + ", hp=" + myHP + "]";
    }
}