package Critter;

//import
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import GameComponents.BoundedEnv;
import GameComponents.Location;
import GameComponents.Shape2DLocatable;

/**
 * An item is can be placed in a BoundedEnv. Represented by 
 * a colored ellipse.
 */
public class Item extends Shape2DLocatable
{
    //constants
    private static final String GOOD_PIC = "critter/treasure.gif";
    private static final String BAD_PIC = "critter/rock.gif";

    private boolean isCollected; //whether or not the item has been collected
    private boolean isGood; //whether or not the item is good
    
    /**
     * Creates an item at the specified location in the environment 
     * with the given color.
     */
    public Item(BoundedEnv env, Location loc, boolean good)
    {
        super(env, loc);

        isGood = good;
        
        if (isGood)
            myImage = sprite.getSprite(GOOD_PIC);
        else
            myImage = sprite.getSprite(BAD_PIC);
        
        isCollected = false; //initially not collected
    }
    
    /**
     * Returns whether or not this item has been collected.
     */
    public boolean collected()
    {
        return isCollected;
    }
    
    /**
     * Sets the value of whether or not the item has been collected.
     */
    public void setCollected(boolean newVal)
    {
        isCollected = newVal;
    }
    
    /**
     * Returns whether or not the item is good
     */
    public boolean good()
    {
        return isGood;
    }
    
    /**
     * Returns the shape representation of this item.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Paints the component onto the container if it has not 
     * been collected.
     */
    public void paintComponent(Graphics2D g2)
    {
        if (!isCollected)
            super.paintComponent(g2);
    }
}
