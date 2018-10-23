package Questor;

// import
import GameComponents.Shape2DLocatable;
import GameComponents.BoundedEnv;
import GameComponents.Location;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * Generic information of units.
 */
public abstract class Unit extends Shape2DLocatable
{
    
    private String myName;
    private int[] myStats;
    
    /**
     * Initializes stats of new unit.
     */
    public Unit(String name, int[] stats, BoundedEnv env, Location loc, String img)
    {
        super(env, loc, img);
        myName = name;
        myStats = stats;
    }
    
    /**
     * Returns name of unit.
     */
    public String name()
    {
        return myName;
    }
    
    /**
     * Returns stats of unit.
     */
    public int[] stats()
    {
        return myStats;
    }
    
    public Shape shape()
    {
        return new Rectangle2D.Double(x(), y(), width(), height());
    }
}