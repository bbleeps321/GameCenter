package Tetris;

//import
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import GameComponents.BoundedEnv;
import GameComponents.Direction;
import GameComponents.Location;
import GameComponents.Steppable;

/**
 * A 4-unit piece part of the Tetris game. Can represent 
 * various configurations of 4 Pieces.
 */
public class TetrisPiece implements Steppable
{
    //constants
    private static final int LENGTH = 4; //number of blocks in one piece
    private static final int KEY_LOC_INDEX = 0; //index of key location
    private static final Color[] COLORS = {Color.blue, //possible piece colors 
                                           Color.green,
                                           Color.red};
    
    private BoundedEnv theEnv; //environment piece is in
    private ArrayList<Piece> whole; //list of pieces making up tetris piece
        
    /**
     * Creates a tetris piece with a random configuration in the 
     * given environment, with one location starting at the center.
     */
    public TetrisPiece(BoundedEnv env, Location start)
    {
        theEnv = env;
        
        whole = new ArrayList<Piece>();
        ArrayList<Location> locs = randomLocations(start);
        Color col = randomColor();
        for (Location loc : locs)
            whole.add(new Piece(theEnv, loc, col));
    }
    
    /**
     * Returns a random configuration of location in an arraylist 
     * starting at the given location.
     */
    private ArrayList<Location> randomLocations(Location start)
    {
        Random rand = new Random();
        ArrayList<Location> locs = new ArrayList<Location>();
        
        locs.add(start); //start location must be included
        
        int numLeft = LENGTH - 1;
        
        while (numLeft > 0)
        {
            int numToPick = rand.nextInt(numLeft) + 1;
            Location last = locs.get(locs.size() - 1); //use last loc in list
            ArrayList<Location> nbrs = theEnv.getNeighbors(last);
            for (int i = 0; i < numToPick; i++)
            {
                int index = rand.nextInt(nbrs.size());
                Location locToAdd = nbrs.get(index);
                int count = 0;
                while (locs.contains(locToAdd)) //already there, pick other loc
                {
                    index = rand.nextInt(nbrs.size());
                    locToAdd = nbrs.get(index);
                    count++;
                    if (count >= 10) //sometimes gets stuck (dunno why)
                        return randomLocations(start); //try again
                }
                locs.add(locToAdd);
                numLeft--;
            }
        }
        return locs;
    }
    
    /**
     * Returns a semi-random color, selecting from a fixed set.
     */
    private Color randomColor()
    {
        Random rand = new Random();
        int index = rand.nextInt(COLORS.length);
        return COLORS[index];
    }
    
    /**
     * Returns a list of all the pieces that make up this tetris piece.
     */
    public ArrayList<Piece> allPieces()
    {
        return whole;
    }
    
    /**
     * Steps the piece, making all of its components move down 
     * one unit.
     */
    public void step()
    {
        if (canFall())
        {
            for (Piece p : whole)
            {
                Location loc = theEnv.neighborOf(p.loc(), Direction.SOUTH);
                p.changeLocation(loc);
            }
        }
    }
    
    /**
     * Moves the piece, and all its components, left.
     */
    public void moveLeft()
    {
        //confirm that all pieces can move left
        if (!canMove(Direction.WEST))
            return;
                
        //move all pieces
        for (Piece p : whole)
        {
            Location loc = theEnv.neighborOf(p.loc(), Direction.WEST);
            p.changeLocation(loc);
        }
    }
    
    /**
     * Moves the piece, and all its components, right.
     */
    public void moveRight()
    {
        //confirm that all pieces can move right
        if (!canMove(Direction.EAST))
            return;
                
        //move all pieces
        for (Piece p : whole)
        {
            Location loc = theEnv.neighborOf(p.loc(), Direction.EAST);
            p.changeLocation(loc);
        }
    }
    
    /**
     * Moves the piece, and all its components, down to the bottom.
     */
    public void drop()
    {
        while (canFall())
        {
            //move all pieces
            for (Piece p : whole)
            {
                Location loc = theEnv.neighborOf(p.loc(), Direction.SOUTH);
                p.changeLocation(loc);
            }
        }
    }
    
    /**
     * Rotates the piece to the left, with the "key" location as the 
     * origin.
     */
    public void rotateLeft()
    {
        if (canRotateLeft())
            for (Piece p : whole)
                p.rotateLeft(whole.get(KEY_LOC_INDEX).loc());
    }
    
    /**
     * Rotates the piece to the right, with the "key" location as the 
     * origin.
     */
    public void rotateRight()
    {
        if (canRotateRight())
            for (Piece p : whole)
                p.rotateRight(whole.get(KEY_LOC_INDEX).loc());
    }
    
    /**
     * Returns whether or not the entire piece can move in the given 
     * direction. Returns false if a single part of the piece cannot 
     * move in the direction.
     */
    public boolean canMove(Direction dir)
    {
        for (Piece p : whole)
        {
            if (!p.canMove(dir))
            {
                //confirm that the blocking piece is not part of this piece
                Location loc = theEnv.neighborOf(p.loc(), dir);
                if (!theEnv.isValid(loc) || !whole.contains(theEnv.objectAt(loc)))
                    return false;
            }
        }
        return true;
    }
    
    /**
     * Returns whether or not the entire piece can rotate 
     * counter-clockwise. Returns false if a single part of 
     * the piece cannot do so.
     */
    public boolean canRotateLeft()
    {
        Location origin = whole.get(KEY_LOC_INDEX).loc();
        for (Piece p : whole)
            if (!p.canRotateLeft(origin))
                return false;
        return true;
    }
    
    /**
     * Returns whether or not the entire piece can rotate 
     * clockwise. Returns false if a single part of the 
     * piece cannot do so.
     */
    public boolean canRotateRight()
    {
        Location origin = whole.get(KEY_LOC_INDEX).loc();
        for (Piece p : whole)
            if (!p.canRotateRight(origin))
                return false;
        return true;
    }
    
    /**
     * Returns whether or not the entire piece can fall any further.
     * Returns false if a single part of the piece can no longer fall.
     */
    public boolean canFall()
    {
        for (Piece p : whole)
        {
            if (!p.canMove(Direction.SOUTH))
            {
                //confirm that the blocking piece is not part of this piece
                Location loc = theEnv.neighborOf(p.loc(), Direction.SOUTH);
                if (!theEnv.isValid(loc) || !whole.contains(theEnv.objectAt(loc)))
                    return false;
            }
        }
        return true;
    }
}