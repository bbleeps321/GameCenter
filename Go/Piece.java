package Go;

//import
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import GameComponents.BoundedEnv;
import GameComponents.Location;
import GameComponents.Shape2DLocatable;

/**
 * Represents a piece in the five-in-a-row game. Either white or 
 * black. Contains the graphical and data representation.
 */
public class Piece extends Shape2DLocatable
{
    private Side mySide;            // the side the piece belongs to.

    /**
     * Creates a piece at the specified location in the environment 
     * on the given side.
     */
    public Piece(BoundedEnv env, Location loc, Side aSide)
    {
        super(env, loc, aSide.color());
        mySide = aSide;
        if (mySide != null)
        	mySide.addPiece(this);
    }
    
    /**
     * Returns the side this piece belongs to.
     */
    public Side side()
    {
        return mySide;
    }
    
    /**
     * Returns the shape of the piece.
     */
    public Shape shape()
    {
        return new Ellipse2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Sets the side of this piece to the specified one.
     */
    public void setSide(Side s)
    {
    	mySide = s;
    	if (mySide != null)
    		mySide.addPiece(this);
    }

}