package Blobert;

//import
import java.awt.Color;
import java.awt.Graphics2D;

import GameComponents.Shape2D;

/**
 * A Phaser is a Blobert with the ability to go through objects 
 * (ghosts and apples) without being affected by them. There is 
 * no cost for using this ability.
 */
public class Phaser extends Blobert
{
    //constants
    private static final String IMAGE = "blobert/phaser.gif"; //blobert image
    
    private boolean isPhasing; //if the blobert is in "phase" mode
    
    /**
     * Creates a blobert at the specified coordinates on the 
     * given anvas.
     */
    public Phaser(double x, double y, GameComponents.Canvas acanvas)
    {
        super(x, y, acanvas);
        myImage = null;
        isPhasing = false;
    }
    
    /**
     * Returns whether or not the Phaser is overlapping the 
     * specified shape. Returns false if the blobert is 
     * phasing.
     */
    public boolean overlaps(Shape2D s)
    {
        if (isPhasing)
            return false;
            
        return super.overlaps(s);
    }
    
    /**
     * First ability of blobert. Toggles blobert's phase state.
     */
    public void act1()
    {
        super.act1();
        isPhasing = !isPhasing;
    }
    
    /**
     * Returns the cost of using the first ability
     */
    public int act1Cost()
    {
        return 1;
    }
    
    /**
     * Returns the preferred color of the blobert.
     */
    protected Color preferredColor()
    {
        if (isMoving())
            return Color.green;
        else
            return new Color(0, 100, 0);
    }
    
    /**
     * Paints the blobert onto the container.
     */
    public void paintComponent(Graphics2D g2)
    {
        g2.setColor(preferredColor());
        if (isPhasing)
            g2.draw(shape());
        else
            g2.fill(shape());
    }
}