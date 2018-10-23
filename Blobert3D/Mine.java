package Blobert3D;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * A mine can destroy any ghosts that go over it. A mine is 
 * represented by a red ellipse.
 */
public class Mine extends Shape2D implements Steppable
{
    //contants
    protected static final int SIZE = 5; //size of mine
    private static final Color DEFAULT_COLOR = Color.red; //default color of mine
    
    protected boolean hidden; //if the mine is hidden
    protected ArrayList<Ghost> ghosts; //list of all ghosts in env
    
    /**
     * Creates a Mine at the specified (x,y) coordinates.
     */
    public Mine(double x, double y, ArrayList<Ghost> someGhosts)
    {
        super(x, y, SIZE, SIZE, DEFAULT_COLOR);
        hidden = false; //initially visible
        ghosts = someGhosts;
    }
    
    /**
     * Creates a Mine at the specified (x,y) coordinates. For use 
     * with classes subclassing this one.
     */
    public Mine(double x, double y, ArrayList<Ghost> someGhosts, Color col)
    {
        super(x, y, SIZE, SIZE, col);
        hidden = false; //initially visible
        ghosts = someGhosts;
    }
    
    /**
     * Steps the mine, destroying any ghosts over it
     */
    public void step()
    {
        for (Ghost g : ghosts)
            if (!hidden && this.overlaps(g))
                detonate(g);
    }
    
    /**
     * Detonates the mine, hiding it while destroying the specified
     * ghost.
     */
    public void detonate(Ghost g)
    {
        g.hideGhost();
        hidden = true; //hide mine
    }
    
    /**
     * Returns whether or not the mine is hidden.
     */
    public boolean isHidden()
    {
        return hidden;
    }

    /**
     * Returns the shape representation of the mine.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Paints the components onto the container.
     */
    public void paintComponent(Graphics2D g2)
    {
        if (!hidden)
            super.paintComponent(g2);
    }
}