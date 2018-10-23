package GameComponents;

/**
 * Stores a direction, either north, south, east, or west.
 */
public class Direction
{
    //constants
    public static final Direction NORTH = new Direction("North");
    public static final Direction SOUTH = new Direction("South");
    public static final Direction EAST = new Direction("East");
    public static final Direction WEST = new Direction("West");
    public static final Direction NORTHEAST = new Direction("North East");
    public static final Direction NORTHWEST = new Direction("North West");
    public static final Direction SOUTHEAST = new Direction("South East");
    public static final Direction SOUTHWEST = new Direction("South West");
    public static final Direction UNKNOWN = new Direction("Unknown");
    
    private String myDir; //direction stored
    
    /**
     * Creates direction with the specified string direction value.
     */
    public Direction(String dir)
    {
        myDir = dir;
    }
    
    /**
     * Returns the reverse of this direction.
     */
    public Direction reverse()
    {
        if (this.equals(Direction.NORTH))
            return Direction.SOUTH;
        else if (this.equals(Direction.SOUTH))
            return Direction.NORTH;
        else if (this.equals(Direction.WEST))
            return Direction.EAST;
        else if (this.equals(Direction.EAST))
            return Direction.WEST;
        else if (this.equals(Direction.NORTHEAST))
            return Direction.SOUTHWEST;
        else if (this.equals(Direction.SOUTHEAST))
            return Direction.NORTHWEST;
        else if (this.equals(Direction.NORTHWEST))
            return Direction.SOUTHEAST;
        else if (this.equals(Direction.SOUTHWEST))
            return Direction.NORTHEAST;
        else
        	return Direction.UNKNOWN;
    }
    
    /**
     * Returns a random direction facing (only the four cardinal directions).
     */
    public static Direction randomDirection()
    {
        double randNum = Math.random();
        if (randNum < .25)
            return Direction.NORTH;
        else if (randNum < .5)
            return Direction.SOUTH;
        else if (randNum < .75)
            return Direction.WEST;
        return Direction.EAST;
    }
    
    /**
     * Returns the string representation of the direction.
     */
    public String toString()
    {
        return myDir;
    }
}
