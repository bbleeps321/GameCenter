package Blobert;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * The blobert object that is controlled in the Blobert game. 
 * A Reflector can reverse the directional movement of all the 
 * ghosts in the game on command. Represented by a red/orange 
 * circle painted over a white rectangle
 */
public class Reflector extends Blobert
{
    /**
     * Creates a Reflector at the specified coordinates on the canvas. 
     * Requires a graveyard of ghost for its ability.
     */
    public Reflector(double x, double y, GameComponents.Canvas acanvas, 
                                ArrayList<Ghost> graveyard)
    {
        super(x, y, acanvas, graveyard);
    }
    
    /**
     * First ability of blobert. Reverses the direction in 
     * which the ghosts are moving.
     */
    public void act1()
    {
        super.act1();
        for (Ghost g : theGrave)
            g.reverse();
    }
    
    /**
     * Second ability of blobert. Does nothing
     */
    public void act2() {}
    
    /**
     * Returns the cost of using the first ability.
     */
    public int act1Cost()
    {
        return 1;
    }
    
    /**
     * Returns the shape representation of the blobert.
     * Returns the white rectangle portion of the blobert 
     * only.
     */
    public Shape shape()
    {
        return new Rectangle2D.Double(myX, myY, mySize, mySize);
    }
    
    /**
     * Returns the preferred color of the blobert.
     */
    protected Color preferredColor()
    {
        if (isMoving())
            return Color.red;
        else
            return Color.orange;
    }
    
    /**
     * Paints the blobert onto the container.
     */
    public void paintComponent(Graphics2D g2)
    {
        g2.setColor(Color.white);
        g2.fill(shape());
        
        g2.setColor(preferredColor());
        g2.fill(new Ellipse2D.Double(myX, myY, mySize, mySize));
    }
}