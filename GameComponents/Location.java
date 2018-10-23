package GameComponents;

/**
 * Stores a location (x,Y).
 */
public class Location
{
    private int myX, myY; //coordinates
    
    /**
     * Creates a location at the specified coordinates.
     */
    public Location(int x, int y)
    {
        myX = x;
        myY = y;
    }
    
    /**
     * Returns the x coordinate of the location.
     */
    public int x()
    {
        return myX;
    }
    
    /**
     * Returns the y coordinate of the location.
     */
    public int y()
    {
        return myY;
    }
    
    /**
     * Returns a string representation of this location.
     */
    public String toString()
    {
        return "(" + myX + "," + myY + ")";
    }
    
    /**
     * Tests this location for equality with another.
     */
    public boolean equals(Object loc)
    {
        return this.toString().equals(loc.toString());
    }
}