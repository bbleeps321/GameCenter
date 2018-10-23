package GameComponents;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * Abstract class for game units that can be drawn in a 2D plane
 * and represented as a shape or conglomerate of shapes. Provides 
 * methods for returning the shape or bounding rectangle of the 
 * graphical representation of the unit. Can be painted onto a 
 * container. Stores it (x,y) coordinates and size.
 */
public abstract class Shape2D extends JComponent
{
    //constants
    protected static final SpriteCache sprite = new SpriteCache();
    
    protected double myX, myY; //(x,Y) coordinates of shape
    protected double myWidth, myHeight; //dimensions of shape
    protected Color myColor; //color of shape
    protected BufferedImage myImage; //image of shape
    
    /**
     * Creates a shape with the specified coordinates and dimensions.
     */
    public Shape2D(double x, double y, double width, double height)
    {
        myX = x;
        myY = y;
        myWidth = width;
        myHeight = height;
        myColor = null;
    }
    
    /**
     * Creates a shape with the specified coordinates, dimensions, and color.
     */
    public Shape2D(double x, double y, double width, double height, Color color)
    {
        myX = x;
        myY = y;
        myWidth = width;
        myHeight = height;
        myColor = color;
    }
    
    /**
     * Creates a shape with the specified coordinates, dimensions, and image.
     */
    public Shape2D(double x, double y, double width, double height, String pic)
    {
        myX = x;
        myY = y;
        myWidth = width;
        myHeight = height;
        myImage = sprite.getSprite(pic);
    }
    
    /**
     * Paints the shape onto the container.
     */
    public void paintComponent(Graphics2D g2)
    {
        //paint image if available, otherwise, paint shape
        if (myImage != null)
        {
            g2.drawImage(myImage, (int)myX, (int)myY, 
                            (int)myWidth, (int)myHeight, this);
        }
        else
        {
            if (myColor != null)
                g2.setColor(myColor);
                
            g2.fill(shape());
        }
    }
    
    /**
     * Returns the x coordinate of the shape.
     */
    public double x()
    {
        return myX;
    }
    
    /**
     * Returns the y coordinate of the shape.
     */
    public double y()
    {
        return myY;
    }
    
    /**
     * Moves the shape to the specified coordinates.
     */
    public void moveTo(double x, double y)
    {
        myX = x;
        myY = y;
    }
    
    /**
     * Returns the width of the shape.
     */
    public double width()
    {
        return myWidth;
    }
    
    /**
     * Returns the height of the shape.
     */
    public double height()
    {
        return myHeight;
    }
    
    /**
     * Returns the color of the shape.
     */
    public Color color()
    {
        return myColor;
    }
    
    /**
     * Returns whether or not the shape overlaps the other one.
     */
    public boolean overlaps(Shape2D s)
    {
        return shape().intersects(s.shape().getBounds2D());
    }
    
    //abstract methods
    /**Returns the shape or bounding rectangle of the graphical shape unit.**/
    public abstract Shape shape();
    
    
}