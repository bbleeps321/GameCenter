package Gateway;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import GameComponents.Shape2D;
import GameComponents.Steppable;

/**
 * Portal that needs to be penetrated in game. Assigned a random 
 * shape and color at initialization. Represented by an outlined shape.
 */
public class Portal extends Shape2D implements Steppable
{
    //constants
    private static final int SIZE = 30; //default size of portal
    private static final double ROTATE_UNIT = Math.PI/4; //unit portal rotates by
    private static final double FALL_UNIT = 10; //unit portal falls by
    private static final int ROTATE_DELAY = 3; // delay for rotating
    
    //possible portal colors
    private static final Color[] COLORS = {Color.red, Color.yellow, Color.blue, 
                                           Color.green}; 
    //possible portal shapes
    private static final String CIRCLE_SHAPE = "Circle";
    private static final String SQUARE_SHAPE = "Square";
    private static final String[] SHAPES = {CIRCLE_SHAPE, 
                                            SQUARE_SHAPE};
    
    private String myShape; //current shape of portal
    private int myColorIndex; //index of current color
    private int myShapeIndex; //index of current shape
    private double myAngle; //the angle of rotation of the shape in radians
    private double rotation; //increment by which this portal rotates
    private int stepCount; //count for steps since last rotation

    /**
     * Creates a portal at the specified coordinates with a random 
     * shape and color
     */
    public Portal(double x, double y)
    {
        super(x, y, SIZE, SIZE);
        
        myAngle = 0;
        stepCount = 0;
        
        myColor = randomColor();
        myShape = randomShape();
        
        //determine a random direction of rotation
        if (Math.random() < .5)
            rotation = ROTATE_UNIT; //rotate right
        else
            rotation = -ROTATE_UNIT; //rotate left
    }
    
    /**
     * Steps the portal, making it fall and rotate by one unit.
     */
    public void step()
    {
        stepCount++;
        myY += FALL_UNIT; //fall
        
        if (stepCount >= ROTATE_DELAY)
        {
            myAngle += rotation; //rotate
            
            //correct for coterminal angles differently based on shape
            if (myShape.equals(CIRCLE_SHAPE)) //circle
                myAngle = 0; //all angles have no effect on appearance
            else //square
                myAngle %= Math.PI/2; //pi/2 radians per angle
        
            stepCount = 0;
        }
    }
    
    /**
     * Returns a randomly selected color from the possible choices. 
     * Sets the index of the color.
     */
    private Color randomColor()
    {
        //determine index randomly
        Random rand = new Random();
        myColorIndex = rand.nextInt(COLORS.length);
            
        //set color
        return COLORS[myColorIndex];
    }
    
    /**
     * Returns a randomly selected shape (String) from the possible choices. 
     * Sets the index of the shape.
     */
    private String randomShape()
    {
        //determine index randomly
        Random rand = new Random();
        myShapeIndex = rand.nextInt(SHAPES.length);
            
        //set color
        return SHAPES[myShapeIndex];
    }

    /**
     * Returns whether or not the given coordinates are in the contained 
     * by the portal.
     */
    public boolean contains(double x, double y)
    {
        return shape().contains(x,y);
    }
    
    /**
     * Returns the angle of rotation of the portal.
     */
    public double angle()
    {
        return myAngle;
    }
    
    /**
     * Returns the x-coordinate of the center of the portal.
     */
    public double centerX()
    {
        return myX + SIZE/2;
    }
    
    /**
     * Returns the y-coordinate of the center of the portal.
     */
    public double centerY()
    {
        return myY + SIZE/2;
    }
    
    /**
     * Returns the shape representation of the portal, different based 
     * on the myShape field. A call to shape().getClass() will 
     * determine its shape when used in comparisons.
     */
    public Shape shape()
    {
        if (myShape.equals(CIRCLE_SHAPE)) //circle
        {
            return new Ellipse2D.Double(myX, myY, SIZE, SIZE);
        }
        else //square
            return new Rectangle2D.Double(myX, myY, SIZE, SIZE);
    }
    
    /**
     * Returns the name of the portal's shape.
     */
    public String shapeName()
    {
        return myShape;
    }
    
    /**
     * Paints the key onto the container, rotating it as needed.
     */
    public void paintComponent(Graphics2D g2)
    {
        g2.rotate(angle(), centerX(), centerY()); //rotate
        
        //draw
        g2.setColor(myColor); //white fill
        g2.fill(shape());
        g2.setColor(Color.white); //outline
        g2.draw(shape());

        g2.rotate(-angle(), centerX(), centerY()); //rotate back
    }
}