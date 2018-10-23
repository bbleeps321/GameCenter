package Gateway;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import GameComponents.Shape2D;

/**
 * Shape controlled by player. Can change shape and color at the 
 * player's command. Represented by a filled shape.
 */
public class Key extends Shape2D
{
    //constants
    private static final int SIZE = 30; //default size of key
    private static final double ROTATE_UNIT = Math.PI/4; //unit key rotates by
    
    //possible key colors
    private static final Color[] COLORS = {Color.red, Color.yellow, Color.blue, 
                                           Color.green}; 
    //possible key shapes
    private static final String CIRCLE_SHAPE = "Circle";
    private static final String SQUARE_SHAPE = "Square";
    private static final String[] SHAPES = {CIRCLE_SHAPE, 
                                            SQUARE_SHAPE};
    
    private String myShape; //current shape of key
    private int myColorIndex; //index of current color
    private int myShapeIndex; //index of current shape
    private double myAngle; //the angle of rotation of the shape in radians

    /**
     * Creates a key at the specified coordinates with the default 
     * properties (red, circle).
     */
    public Key(double x, double y)
    {
        super(x, y, SIZE, SIZE);
        
        myColorIndex = 0;
        myShapeIndex = 0;
        myAngle = 0;
        
        myColor = COLORS[myColorIndex];
        myShape = SHAPES[myShapeIndex];
    }
    
    /**
     * Changes the key's color to the next one.
     */
    public void shiftColorNext()
    {
        //shift index
        myColorIndex++;
        if (myColorIndex >= COLORS.length) //out of bounds
            myColorIndex = 0;
            
        //set color
        myColor = COLORS[myColorIndex];
    }
    
    /**
     * Changes the key's color to the previors one.
     */
    public void shiftColorPrev()
    {
        //shift index
        myColorIndex--;
        if (myColorIndex < 0) //out of bounds
            myColorIndex = COLORS.length - 1;
            
        //set color
        myColor = COLORS[myColorIndex];
    }
    
    /**
     * Changes the key's shape to the next one.
     */
    public void shiftShape()
    {
        //shift index
        myShapeIndex++;
        if (myShapeIndex >= SHAPES.length) //out of bounds
            myShapeIndex = 0;
            
        //set color
        myShape = SHAPES[myShapeIndex];
    }
    
    /**
     * Rotates the key to the left by one increment.
     */
    public void rotate()
    {
        myAngle -= ROTATE_UNIT;
        
        //correct for coterminal angles differently based on shape
        if (myShape.equals(CIRCLE_SHAPE)) //circle
            myAngle = 0; //all angles have no effect on appearance
        else //square
            myAngle %= Math.PI/2; //pi/2 radians per angle
    }
    
    /**
     * Returns the x-coordinate of the center of the key.
     */
    public double centerX()
    {
        return myX + SIZE/2;
    }
    
    /**
     * Returns the y-coordinate of the center of the key.
     */
    public double centerY()
    {
        return myY + SIZE/2;
    }
    
    /**
     * Moves the key's center coordinates to the specified ones.
     */
    public void moveTo(double x, double y)
    {
        myX = x - SIZE/2;
        myY = y - SIZE/2;
    }
    
    /**
     * Returns the angle of rotation of the portal.
     */
    public double angle()
    {
        return myAngle;
    }
    
    /**
     * Returns the shape representation of the key, different based 
     * on the myShape field.
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
     * Returns the name of the key's shape.
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
        super.paintComponent(g2); //draw
        g2.rotate(-angle(), centerX(), centerY()); //rotate back
    }
}