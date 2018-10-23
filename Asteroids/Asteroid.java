package Asteroids;

//import
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * An asteroid is a black ellipse with a white outline. 
 * When destroyed, it will split into two parts if it 
 * is big enough.
 */
public class Asteroid extends Shape2D implements Steppable
{
    //constants
    private static final String IMAGE = "asteroids/asteroid.gif";
    private static final int MIN_SIDES = 5; //minimun number of sides
    private static final int MAX_SIDES = 8; //maximum number of sides
    private static final int SIZE_UNIT = 30; //actual size of one size unit
    private static final int MAX_SPEED = 6; //maximum speed in any direction
    private static final int MIN_SPEED = 1; //minimum speed in any direction
    
    private int mySize; //the size of the asteroid
    private double dx, dy; //directional movement
    private boolean hidden; //if asteroid is hidden from view
    private GameComponents.Canvas canvas; //canvas asteroid is on
    
    /**
     * Creates an asteroid with a random number of sides at the 
     * specified )x,y) coordinates with the given size.
     */
    public Asteroid(double x, double y, int size, GameComponents.Canvas acanvas)
    {
        super(x, y, 0, 0, IMAGE); //width and height calculated later
        
        canvas = acanvas;        
        mySize = size;
        
        myWidth = mySize * SIZE_UNIT;
        myHeight = mySize * SIZE_UNIT;
        
        //random directional movement
        Random rand = new Random();
        dx = rand.nextInt(MAX_SPEED) + MIN_SPEED;
        dy = rand.nextInt(MAX_SPEED) + MIN_SPEED;
        if (rand.nextDouble() < .5) //50/50 chance of being "negative" direction
            dx = -dx;
        if (rand.nextDouble() < .5) //50/50 chance of being "negative" direction
            dy = -dy;
            
        hidden = false;
    }
    
    /**
     * Steps the asteroid's movement.
     */
    public void step()
    {
        if (!hidden) //move this asteroid if not hidden
        {
            shift(); //shift if necessary
            myX += dx;
            myY += dy;
        }
    }
    
    /**
     * Explodes the asteroid. If it is large enough, it will split into 
     * multiple asteroids. Returns the asteroids that split off from this 
     * one. Returns an empty list if no asteroids split off.
     */
    public ArrayList<Asteroid> explode(int power)
    {
        hidden = true;
        if (mySize - power <= 0)
        {
            return new ArrayList<Asteroid>(); //empty list
        }
        else
        {
            ArrayList<Asteroid> children = new ArrayList<Asteroid>();
            int numChild = mySize/power;
            Random rand = new Random();
            for (int i = 0; i < numChild; i++)
                children.add(new Asteroid(myX - myWidth, myY - myHeight, 
                                            mySize - power, canvas));
            return children;
        }
    }
    
    /**
     * Shifts the asteroid to appear on the opposite side of the 
     * screen if it is completely off screen.
     */
    private void shift()
    {
        if (myX + myWidth < 0) //off the left
            myX = canvas.width(); //move to be just off the right
        else if (myX > canvas.width()) //off the right
            myX = -myWidth; //move to be just off the left
        
        if (myY + myHeight < 0) //off the top
            myY = canvas.height(); //move to be just off bottom
        else if (myY > canvas.height()) //off the bottom
            myY = -myHeight; //move to be just off top
    }
    
    /**
     * Returns the shape representation of this asteroid.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Returns whether or not the asteroid is hidden from view.
     */
    public boolean isHidden()
    {
        return hidden;
    }
    
    /**
     * Returns whether or not the asteroid overlaps the given shape.
     */
    public boolean overlaps(Shape2D s)
    {
        if (hidden)
            return false;
        return super.overlaps(s);
    }
}