package Firewall;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;

import GameComponents.BoundedEnv;
import GameComponents.Direction;
import GameComponents.Locatable;
import GameComponents.Location;
import GameComponents.Shape2DLocatable;

/**
 * A Gunner is human controlled and can fire shots. Represented 
 * by a colored triangle.
 */
public class Gunner extends Shape2DLocatable implements Locatable
{
    //constants
    private static final Color COLOR = Color.cyan; //color of gunner

    private ArrayList<Shot> shots; //list of all shots gunner has fired
    
    /**
     * Creates a gunner at the specified location in the given 
     * environment.
     */
    public Gunner(BoundedEnv env, Location loc)
    {
        super(env, loc, COLOR);

        shots = new ArrayList<Shot>();
    }
    
    /**
     * Steps all the shots the gunner has fired.
     */
    public void step()
    {
        for (Shot s : shots)
            s.step();
        removeDeadShots();
    }
    
    /**
     * Moves the gunner to the left.
     */
    public void moveLeft()
    {
        Location loc = theEnv.neighborOf(myLoc, Direction.WEST);
        if (theEnv.isValid(loc))
            changeLocation(loc);
    }
    
    /**
     * Moves the gunner to the right.
     */
    public void moveRight()
    {
        Location loc = theEnv.neighborOf(myLoc, Direction.EAST);
        if (theEnv.isValid(loc))
            changeLocation(loc);
    }
    
    /**
     * Fires a shot with the specified class name.
     */
    public void fire(String shotName)
    {
        Location locAbove = theEnv.neighborOf(myLoc, Direction.NORTH);
        shots.add(Shot.createInstanceOf(shotName, theEnv, locAbove));
    }
    
    /**
     * Returns the cost of firing a weapon with the specified name 
     * (does not actually fire it).
     */
    public int fireCost(String shotName)
    {
        return Shot.createInstanceOf(shotName).cost();
    }
    
    /**
     * Returns the shape representation of the gunner.
     */
    public Shape shape()
    {
        Polygon p = new Polygon();
        p.addPoint((int)((myX + (myX + myWidth))/2), (int)myY); //top point
        p.addPoint((int)myX, (int)(myY + myHeight)); //left base
        p.addPoint((int)(myX + myWidth), (int)(myY + myHeight)); //right base
        return p;
    }
    
    /**
     * Removes the shots that are no longer in the environment.
     */
    private void removeDeadShots()
    {
        for (int i = 0; i < shots.size(); i++)
        {
            if (!theEnv.contains(shots.get(i)))
            {
                shots.remove(i);
                i--;
            }
        }
    }
    
    /**
     * Paints the gunner and its shots onto the container.
     */
    public void paintComponent(Graphics2D g2)
    {
        super.paintComponent(g2);
        for(Shot s : shots)
            s.paintComponent(g2);
    }
}