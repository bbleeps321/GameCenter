package Worm;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;

import GameComponents.Direction;
import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * A worm for the Worm game. Stores and ArrayList of worm segments 
 * and has methods for adding segments.
 */
public class Worm extends Shape2D implements Steppable
{
    //constants
    private static final double SPEED_INCREMENT = 1; //rate at which speed grows
    private static final double START_SPEED = 13; //initial speed of worm
    private static final double MAX_SPEED = 20; //maximum speed
    private static final Color HEAD_COLOR = Color.green; //color of head
    private static final Color BODY_COLOR = Color.yellow; //color of body segments
    
    private ArrayList<WormSegment> worm; //list of segments making up worm
    private Direction myDir; //direction of worm's movement
    private double mySpd; //speed at which worm moves
    private double mySize; //size of each segment
    private double myGap; //gap between segments
    private double initX, initY; //initial location at creation
    
    private boolean toAdd; //segment to be added at the end of the step

    /**
     * Creates a worm with a single segment at the specified 
     * coordinates.
     */
    public Worm(double x, double y, double size)
    {
        super(x, y, size, size);
        initX = x;
        initY = y;
        mySize = size;
        
        myDir = null; //no movement
        
        mySpd = START_SPEED;
        myGap = 1*mySpd;
        
        worm = new ArrayList<WormSegment>();
        worm.add(new WormSegment(initX, initY, mySize, mySpd, myDir, HEAD_COLOR));

        toAdd = false;
    }
    
    /**
     * Steps the worm. Moves it segments in its set 
     * direction unless no direction is set.
     */
    public void step()
    {
        if (myDir == null) //no directional movement
            return; //stop method
            
        WormSegment seg = worm.get(0); //head
        double lastx = seg.x();
        double lasty = seg.y();
        double newX;
        double newY;
        
        if (myDir.equals(Direction.NORTH))
            seg.move(0,-mySpd); //move segment in specific direction
        else if (myDir.equals(Direction.SOUTH))
            seg.move(0,mySpd); //move segment in specific direction
        else if (myDir.equals(Direction.WEST))
            seg.move(-mySpd,0); //move segment in specific direction
        else if (myDir.equals(Direction.EAST))
            seg.move(mySpd,0); //move segment in specific direction
            
        //step each segment to the location of the one before it
        for (int i = 1; i < worm.size(); i++)
        {
            seg = worm.get(i);
            WormSegment last = worm.get(i - 1);
            //move to old loc of previous segment
            seg.moveTo(last.oldX(), last.oldY()); 
        }
        
        if (toAdd) //should add segment
        {
            worm.add(newSegment());
            toAdd = false;
        }
    }
    
    /**
     * Adds another segment to the end of the worm. Will not complete
     * if the worm is currently stepping
     */
    public void addSegment()
    {
        toAdd = true;
    }
    
    /**
     * Returns the wormsegment to be added to the worm based 
     * on the worm's movements.
     */
    private WormSegment newSegment()
    {
        WormSegment lastSegment = worm.get(worm.size()-1); //tail
        double x, y;
        Direction lastDir = lastSegment.direction(); //direction of tail segment\
        if (myDir.equals(Direction.NORTH))
        {
            x = lastSegment.x();
            y = lastSegment.y() + (myGap);
        }
        else if (myDir.equals(Direction.SOUTH))
        {
            x = lastSegment.x();
            y = lastSegment.y() - myGap;
        }
        else if (myDir.equals(Direction.WEST))
        {
            x = lastSegment.x() + (myGap);
            y = lastSegment.y();
        }
        else // must be moving east
        {
            x = lastSegment.x() - myGap;
            y = lastSegment.y();
        }
        
        return new WormSegment(x, y, mySize, mySpd, null, BODY_COLOR);
    }
    
    /**
     * Move the worm in the upward direction.
     */
    public void moveUp()
    {
        myDir = Direction.NORTH;
    }
    
    /**
     * Move the worm in the downward direction.
     */
    public void moveDown()
    {
        myDir = Direction.SOUTH;
    }
    
    /**
     * Move the worm in the left direction.
     */
    public void moveLeft()
    {
        myDir = Direction.WEST;
    }
    
    /**
     * Move the worm in the right direction.
     */
    public void moveRight()
    {
        myDir = Direction.EAST;
    }
    
    /**
     * Stops the worm's movement.
     */
    public void stopMovement()
    {
        myDir = null;
    }
    
    /**
     * Increases the speed of the worm.
     */
    public void speedUp()
    {
        mySpd += SPEED_INCREMENT;
        if (mySpd > MAX_SPEED)
            mySpd = MAX_SPEED;
        myGap = 1*mySpd;
        
        //update speeds of all segment
        for (WormSegment seg : worm)
            seg.setSpeed(mySpd);
    }
    
    /**
     * Resets the worm, its starting location with all of 
     * its other segments under it and not moving.
     */
    public void resetWorm()
    {
        for (WormSegment seg : worm)
            seg.moveTo(initX, initY);
        stopMovement();
    }
    
    /**Required by Shape2D interface.**/
    public Shape shape() { return null; }
    
    /**
     * Returns whether or not the worm overlaps the other Shape2D.
     */
    public boolean overlaps(Shape2D s)
    {
        for (WormSegment seg : worm)
            if (seg.overlaps(s))
                return true;
        return false;
    }
    
    /**
     * Returns whether the head of the worm is overlapping any other segment. 
     * Returns false if the worm is not moving.
     */
    public boolean overlapsSelf()
    {
        if (myDir == null)
            return false;
            
        //compare the head segment with all other ones
        WormSegment head = worm.get(0);
        for (int i = 1; i < worm.size(); i++)
            if (head.overlaps(worm.get(i)))
                return true;
        return false;
    }
    
    /**
     * Returns the x coordinate of this worm (head).
     */
    public double x()
    {
        return worm.get(0).x();
    }
    
    /**
     * Returns the y coordinate of this worm (head).
     */
    public double y()
    {
        return worm.get(0).y();
    }
    
    /**
     * Returns the size of a single segment in the worm.
     */
    public double segmentSize()
    {
        return mySize;
    }
    
    /**
     * Paints the worm onto the container.
     */
    public void paintComponent(Graphics2D g2)
    {
        //paint starting at tail, so head is on top
        for (int i = worm.size() - 1; i >= 0; i--)
            worm.get(i).paintComponent(g2);
            
    }
}