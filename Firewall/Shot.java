package Firewall;

//import
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Constructor;

import GameComponents.BoundedEnv;
import GameComponents.Direction;
import GameComponents.Location;
import GameComponents.Shape2DLocatable;
import GameComponents.Steppable;

/**
 * A shot is a generic weapon represented by a white ellipse.
 * Fired by a Gunner.
 */
public class Shot extends Shape2DLocatable implements Steppable
{
    //constants
    private static final Color COLOR = Color.white; //color of shot
    
    protected int movesPerStep; //moves in each step (affects speed)
    
    /**
     * Creates a shot with nothing initialized. Should be used when 
     * using methods that do not require them to be (cost).
     */
    public Shot()
    {
        super(null, null);
    }
    
    /**
     * Creates a shot at the specified location in the given 
     * environment.
     */
    public Shot(BoundedEnv env, Location loc)
    {
        super(env, loc, COLOR);
        
        movesPerStep = 2;
        
        //recalculate dimensions and coordinates
        myWidth = theEnv.cellWidth()/3;
        myHeight = theEnv.cellHeight()/3;
        myX = theEnv.doubleX(loc.x()) + (theEnv.cellWidth() + myWidth)/2;
        myY = theEnv.doubleY(loc.y()) + (theEnv.cellHeight() + myHeight)/2;
        
        theEnv.add(this);
    }
    
    /**
     * Creates a shot at the specified location in the given 
     * environment with the specified color.
     */
    protected Shot(BoundedEnv env, Location loc, Color col, int mps)
    {
        super(env, loc, col);

        movesPerStep = mps;
        
        //recalculate dimensions and coordinates
        myWidth = theEnv.cellWidth()/3;
        myHeight = theEnv.cellHeight()/3;
        myX = theEnv.doubleX(loc.x()) + (theEnv.cellWidth() + myWidth)/2;
        myY = theEnv.doubleY(loc.y()) + (theEnv.cellHeight() + myHeight)/2;
        
        theEnv.add(this);
    }
    
    /**
     * Creates an instance of a shot with the given class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Shot createInstanceOf(String className, BoundedEnv env,
                                            Location loc)
    {
        try
        {
            //find constructor requiring boundedEnv, location
            Class classToCreate = Class.forName("Firewall."+className);
            Class[] params = {env.getClass(), loc.getClass()};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] argList = {env, loc};
            return (Shot)constructor.newInstance(argList);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Creates an instance of a shot with the given class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Shot createInstanceOf(String className)
    {
        try
        {
            //find constructor requiring no parameter
            Class classToCreate = Class.forName("Firewall."+className);
            Class[] params = {};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] argList = {};
            return (Shot)constructor.newInstance(argList);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Steps the shot, moving it upwards. Will remove itself 
     * if it hits a brick. Moves multiple times per step so 
     * shot is faster.
     */
    public void step()
    {
        for (int i = 0; i < movesPerStep; i++)
        {
            if (!theEnv.contains(this))
                return;
                
            Location loc = theEnv.neighborOf(myLoc, Direction.NORTH);
            if (!theEnv.isValid(loc)) //location is out of bounds
            {
                theEnv.remove(this);
                return;
            }
            
            if (!theEnv.isEmpty(loc)) //brick is there
                detonate();
            else
                changeLocation(loc);
        }
    }
    
    /**
     * Detonates the shot, removing it and any bricks in its 
     * range from the environment.
     */
    protected void detonate()
    {
        Location loc = theEnv.neighborOf(myLoc, Direction.NORTH);
        theEnv.remove(theEnv.objectAt(loc));
        theEnv.remove(this);
    }
    
    /**
     * Returns the cost of the shot.
     */
    public int cost()
    {
        return 0;
    }
    
    /**
     * Changes the location of the gunner to the specified one 
     * and updates the location in the environment.
     */
    public void changeLocation(Location loc)
    {
        Location oldLoc = loc();
        myLoc = loc;
        myX = theEnv.doubleX(loc.x()) + (theEnv.cellWidth() + myWidth)/2;
        myY = theEnv.doubleY(loc.y()) + (theEnv.cellHeight() + myHeight)/2;
        theEnv.recordMove(this, oldLoc);
    }
    
    /**
     * Returns the shape representation of the gunner.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
}