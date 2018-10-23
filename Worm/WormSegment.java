package Worm;

//import
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import GameComponents.Direction;
import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * A segment of the overall worm. Represented by a colored 
 * ellipse.
 */
public class WormSegment extends Shape2D implements Steppable
{
    private double oldX, oldY; //previous (x,y) coordinates
    private double mySize; //dimensions of segment
    private double mySpd; //speed at which segment moves
    private Direction myDir; //direction of movement of the segment
    private Direction oldDir; //previous direction of movement
    
    /**
     * Constructs a segment at the specified coordinates.
     */
    public WormSegment(double x, double y, double size, double spd, 
                        Direction dir, Color col)
    {
        super(x, y, size, size, col);
        oldX = x;
        oldY = y;
        mySize = size;
        mySpd = spd;
        myDir = dir;
        oldDir = null;
    }
    
    /**
     * Steps the segment, moving it in its direction.
     */
    public void step()
    {
        if (myDir == null) //no directional movement
            return;
        
        if (myDir.equals(Direction.NORTH))
            move(0,-mySpd); //move segment in specific direction
        else if (myDir.equals(Direction.SOUTH))
            move(0,mySpd); //move segment in specific direction
        else if (myDir.equals(Direction.WEST))
            move(-mySpd,0); //move segment in specific direction
        else if (myDir.equals(Direction.EAST))
            move(mySpd,0); //move segment in specific direction
    }
    
    /**
     * Sets the speed of the segment to the specified value.
     */
    public void setSpeed(double spd)
    {
        mySpd = spd;
    }
    
    /**
     * Sets the direction of the segment to the specified one. 
     */
    public void setDirection(Direction dir)
    {
        if (dir == myDir && dir == oldDir)
            return;
            
        oldDir = myDir;
        myDir = dir;
    }
    
    /**
     * Returns the direction the segment is moving in.
     */
    public Direction direction()
    {
        return myDir;
    }
    
    /**
     * Returns the old direction of the segment.
     */
    public Direction oldDirection()
    {
        return oldDir;
    }
    
    /**
     * Returns the speed of the segment.
     */
    public double speed()
    {
        return mySpd;
    }
    
    /**
     * Returns the shape representation of the segment (an ellipse).
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, mySize, mySize);
    }
    
    /**
     * Moves the segment to the specified location.
     */
    public void moveTo(double x, double y)
    {
        oldX = myX;
        oldY = myY;
        myX = x;
        myY = y;
    }
    
    /**
     * Moves the segment in the specified x and y directions.
     */
    public void move(double x, double y)
    {
        moveTo(myX + x, myY + y);
    }
    
    /**
     * Returns the old x coordinates of the segment.
     */
    public double oldX()
    {
        return oldX;
    }
    
    /**
     * Returns the old y coordinates of the segment.
     */
    public double oldY()
    {
        return oldY;
    }
}