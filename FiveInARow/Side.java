package FiveInARow;

//import
import java.awt.Color;
import java.util.ArrayList;

import GameComponents.BoundedEnv;
import GameComponents.Location;

/**
 * Contains an arraylist of pieces, making up a side.
 */
public class Side
{
    private String              myName;             // name of this side.
    private Color               myColor;            // the color of this side.
    private ArrayList<Piece>    myPieces;           // pieces on this side.
    private BoundedEnv          theEnv;             // the environment the pieces are in.
    
    /**
     * Initializes a side, assigning it with the given name.
     */
    public Side(String aName, Color col, BoundedEnv env)
    {
        myName = aName;
        myColor = col;
        myPieces = new ArrayList<Piece>();
        theEnv = env;
    }
    
    /**
     * Adds a new piece to this side at the specified location.
     * Returns the piece that was added.
     */
    public Piece addPiece(Location loc)
    {
        return new Piece(theEnv, loc, this); // piece adds it self to the side.
    }
    
    /**
     * Adds the specified piece to this side.
     */
    public void addPiece(Piece p)
    {
        myPieces.add(p);
    }
    
    /**
     * Removes all pieces from this side.
     */
    public void removeAll()
    {
        myPieces.clear();
    }
    
    /**
     * Returns the number of pieces in this side.
     */
    public int pieceCount()
    {
        return myPieces.size();
    }
    
    /**
     * Returns the color of this side.
     */
    public Color color()
    {
        return myColor;
    }
        
    /**
     * Returns the piece at the specified index.
     */
    public Piece piece(int index)
    {
        return myPieces.get(index);
    }
    
    /**
     * Returns the name of this side.
     */
    public String name()
    {
        return myName;
    }
}