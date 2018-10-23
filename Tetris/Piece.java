package Tetris;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import GameComponents.BoundedEnv;
import GameComponents.Direction;
import GameComponents.Location;
import GameComponents.Shape2DLocatable;

/**
 * A single, 1-unit piece, part of a tetris piece. Can be a 
 * variety of colors. Represented by a rectangle.
 */
public class Piece extends Shape2DLocatable
{
    /**
     * Creates a Piece at the specified locaton in the environment 
     * with the given color.
     */
    public Piece(BoundedEnv env, Location loc, Color col)
    {
        super(env, loc, col);
        setOpaque(true);
    }
    
    /**
     * Steps the piece, moving it down one unit if possible.
     */
    public void step()
    {
        if (canMove(Direction.SOUTH))
        {
            Location locBelow = theEnv.neighborOf(myLoc, Direction.SOUTH);
            changeLocation(locBelow);
        }
    }
    
    /**
     * Rotates the piece's coordinates counter-clockwise 
     * around the specified origin.
     */
    public void rotateLeft(Location origin)
    {
        if (canRotateLeft(origin))
        {
            int diffX = myLoc.x() - origin.x();
            int diffY = myLoc.y() - origin.y();
            
            int newX = origin.x() + diffY;
            int newY = origin.y() - diffX;
            
            changeLocation(new Location(newX, newY));
        }
    }
    
    /**
     * Rotates the piece's coordinates clockwise 
     * around the specified origin.
     */
    public void rotateRight(Location origin)
    {
        if (canRotateRight(origin))
        {
            int diffX = myLoc.x() - origin.x();
            int diffY = myLoc.y() - origin.y();
            
            int newX = origin.x() - diffY;
            int newY = origin.y() + diffX;

            changeLocation(new Location(newX, newY));
        }
    }
    
    /**
     * Returns whether or not this piece can rotate counter-clockwise.
     */
    public boolean canRotateLeft(Location origin)
    {
        int diffX = myLoc.x() - origin.x();
        int diffY = myLoc.y() - origin.y();
        
        int newX = origin.x() + diffY;
        int newY = origin.y() - diffX;
        
        Location loc = new Location(newX, newY);
        return theEnv.isValid(loc);
    }
    
    /**
     * Returns whether or not this piece can rotate clockwise.
     */
    public boolean canRotateRight(Location origin)
    {
        int diffX = myLoc.x() - origin.x();
        int diffY = myLoc.y() - origin.y();
        
        int newX = origin.x() - diffY;
        int newY = origin.y() + diffX;
        
        Location loc = new Location(newX, newY);
        return theEnv.isValid(loc);
    }
    
    /**
     * Changes the location of the piece, while updating the 
     * environment and catching any errors.
     */
    public void changeLocation(Location newLoc)
    {
        try
        {
            super.changeLocation(newLoc);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    
    /**
     * Returns the shape representation of the piece.
     */
    public Shape shape()
    {
        return new Rectangle2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Returns whether or not the piece can move in the specified direction.
     */
    public boolean canMove(Direction dir)
    {
        Location loc = theEnv.neighborOf(myLoc, dir);
        return theEnv.isEmpty(loc) && theEnv.isValid(loc);
    }
    
    /**
     * Paints the piece onto the container.
     */
    public void paintComponent(Graphics2D g2)
    {
        //sometimes not in environment though should be (dunno why)
        if (!theEnv.contains(this))
            theEnv.add(this);
            
        super.paintComponent(g2);
    }
}