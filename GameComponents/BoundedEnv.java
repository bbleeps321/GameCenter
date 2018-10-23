package GameComponents;

//import
import java.util.ArrayList;
import java.util.Random;

/**
 * Stores a 2D array of Locatable objects, simulating a 
 * bounded environment.
 */
public class BoundedEnv
{
    protected Locatable[][] grid; //2D array of objects
    private int myWidth, myHeight; //dimensions of environment
    private double myCellWidth, myCellHeight; //dimensions of a single cell
    protected int myNumObjects; //number of objects in environment
    
    /**
     * Creates a bounded environment with the specified number of 
     * width and height in cells.
     */
    public BoundedEnv(int width, int height, double cellWidth, double cellHeight)
    {
        myWidth = width;
        myHeight = height;
        myCellWidth = cellWidth;
        myCellHeight = cellHeight;
        
        grid = new Locatable[myWidth][myHeight];
        
        myNumObjects = 0;
    }
    
    /**
     * Adds the specified Locatable object to the environment .
     */
    public void add(Locatable obj)
    {
        int x = obj.loc().x();
        int y = obj.loc().y();
        grid[x][y] = obj;
        myNumObjects++;
    }
    
    /**
     * Removes the specified Locatable object from the environment.
     */
    public void remove(Locatable obj)
    {
        int x = obj.loc().x();
        int y = obj.loc().y();
        grid[x][y] = null;
        myNumObjects--;
    }
    
    /**
     * Clears the object at the given location of whatever object 
     * it may contain.
     */
    public void remove(Location loc)
    {
        Locatable obj = objectAt(loc);
        if (obj == null)
            return;
        
        remove(obj);
        myNumObjects--;
    }
    
    /**
     * Returns the number of objects contained in the environment.
     */
    public int numObjects()
    {
        return myNumObjects;
    }
    
    /**
     * Returns whether or not the specified location is valid.
     */
    public boolean isValid(Location loc)
    {
        int x = loc.x();
        int y = loc.y();
        return (x >= 0) && (x < myWidth) && (y >= 0) && (y < myHeight);
    }
    
    /**
     * Returns whether or not the specified location is empty.
     * Returns false if the location is not empty or invalid.
     */
    public boolean isEmpty(Location loc)
    {
        if (!isValid(loc))
            return false;
            
        int x = loc.x();
        int y = loc.y();
        return grid[x][y] == null;
    }
    
    /**
     * Returns the width of the environment in cells.
     */
    public int width()
    {
        return myWidth;
    }
    
    /**
     * Returns the height of the environment in cells.
     */
    public int height()
    {
        return myHeight;
    }
    
    /**
     * Returns the width of a single cell.
     */
    public double cellWidth()
    {
        return myCellWidth;
    }
    
    /**
     * Returns the height of a single cell.
     */
    public double cellHeight()
    {
        return myCellHeight;
    }
    
    /**
     * Returns the locatable object at the specified location.
     */
    public Locatable objectAt(Location loc)
    {
        return grid[loc.x()][loc.y()];
    }
    
    /**
     * Returns the location that is adjacent to the specified one 
     * in the given direction.
     */
    public Location neighborOf(Location loc, Direction dir)
    {
        if (dir.equals(Direction.NORTH))
            return new Location(loc.x(), loc.y() - 1);
        else if (dir.equals(Direction.SOUTH))
            return new Location(loc.x(), loc.y() + 1);
        else if (dir.equals(Direction.WEST))
            return new Location(loc.x() - 1, loc.y());
        else if (dir.equals(Direction.EAST))
            return new Location(loc.x() + 1, loc.y());
        else if (dir.equals(Direction.NORTHEAST))
            return new Location(loc.x() + 1, loc.y() - 1);
        else if (dir.equals(Direction.SOUTHEAST))
            return new Location(loc.x() + 1, loc.y() + 1);
        else if (dir.equals(Direction.NORTHWEST))
            return new Location(loc.x() - 1, loc.y() - 1);
        else if (dir.equals(Direction.SOUTHWEST))
            return new Location(loc.x() - 1, loc.y() + 1);
        else
        	return null;
    }
    
    /**
     * Returns an ArrayList of valid, empty locations adjacent to the 
     * specified one.
     */
    public ArrayList<Location> emptyNeighbors(Location loc)
    {;
        ArrayList<Location> nbrs = getNeighbors(loc);
        
        //remove locations that are not empty
        for (int i = 0; i < nbrs.size(); i++)
        {
            if(!isEmpty(nbrs.get(i)))
            {
                nbrs.remove(i);
                i--;
            }
        }
        return nbrs;
    }
    
    /**
     * Returns an ArrayList of valid locations adjacent to the 
     * specified one.
     */
    public ArrayList<Location> getNeighbors(Location loc)
    {
        ArrayList<Location> nbrs = new ArrayList<Location>();
        nbrs.add(neighborOf(loc, Direction.NORTH));
        nbrs.add(neighborOf(loc, Direction.SOUTH));
        nbrs.add(neighborOf(loc, Direction.EAST));
        nbrs.add(neighborOf(loc, Direction.WEST));
        
        //remove invalid locations
        for (int i = 0; i < nbrs.size(); i++)
        {
            if(!isValid(nbrs.get(i)))
            {
                nbrs.remove(i);
                i--;
            }
        }
        
        return nbrs;
    }
    
    /**
     * Returns an ArrayList of valid locations adjacent to the 
     * specified one, both directly and diagonally.
     */
    public ArrayList<Location> getNeighbors8(Location loc)
    {
        ArrayList<Location> nbrs = new ArrayList<Location>();
        nbrs.add(neighborOf(loc, Direction.NORTH));
        nbrs.add(neighborOf(loc, Direction.SOUTH));
        nbrs.add(neighborOf(loc, Direction.EAST));
        nbrs.add(neighborOf(loc, Direction.WEST));
        nbrs.add(neighborOf(loc, Direction.NORTHEAST));
        nbrs.add(neighborOf(loc, Direction.NORTHWEST));
        nbrs.add(neighborOf(loc, Direction.SOUTHEAST));
        nbrs.add(neighborOf(loc, Direction.SOUTHWEST));
        
        //remove invalid locations
        for (int i = 0; i < nbrs.size(); i++)
        {
            if(!isValid(nbrs.get(i)))
            {
                nbrs.remove(i);
                i--;
            }
        }
        
        return nbrs;
    }

    /**
     * Updates the grid for the location of the specified 
     * Locatable object.
     */
    public void recordMove(Locatable obj, Location oldLoc)
    {
        //empty old location
        int oldX = oldLoc.x();
        int oldY = oldLoc.y();
        grid[oldX][oldY] = null;
        
        //fill new location
        add(obj);
    }
    
    /**
     * Returns whether or not the objects at the two locations are 
     * of the same type (same class/superclass).
     */
    public boolean areSame(Location loc1, Location loc2)
    {
        Object obj1 = grid[loc1.x()][loc1.y()];
        Object obj2 = grid[loc2.x()][loc2.y()];
        
        //are same class
        boolean sameClass = obj1.getClass().equals(obj2.getClass());
        
        //are same super class
        boolean sameSuper = obj1.getClass().getSuperclass().equals(
                                        obj2.getClass().getSuperclass());
                                        
        //object 1 subclasses object 2
        boolean sub1to2 = obj1.getClass().getSuperclass().equals(obj2.getClass());
        
        //object 2 subclasses object 1
        boolean sub2to1 = obj2.getClass().getSuperclass().equals(obj1.getClass());
        
        //return true if any above booleans are true
        return  sameClass || sameSuper || sub1to2 || sub2to1;
    }
    
    /**
     * Returns the distance between the two locations in cells.
     */
    public int distance(Location loc1, Location loc2)
    {
        int x1 = loc1.x();
        int y1 = loc1.y();
        int x2 = loc2.x();
        int y2 = loc2.y();
        
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    
    /**
     * Returns the angle measure between the two locations in radians.
     */
    public double angleRad(Location loc1, Location loc2)
    {
        int x1 = loc1.x();
        int y1 = loc1.y();
        int x2 = loc2.x();
        int y2 = loc2.y();
        double xDiff = Math.abs(x2 - x1);
        double yDiff = Math.abs(y2 - y1);
        double alpha = Math.atan(yDiff/xDiff); //only reference angle
        
        //(reference angle is central angle in I quadrant)
        
        if (x2 < x1 && y2 >= y1) //angle in II quadrant
            alpha = Math.PI - alpha;
        else if (x2 < x1 && y2 < y1) //angle in III quadrant
            alpha = Math.PI + alpha;
        else if (x2 >= x1 && y2 < y1) //angle in IV quadrant
            alpha = 2*Math.PI - alpha;
        
        return alpha;
    }
    
    /**
     * Returns the double x coordinate of the location at the 
     * specified cell.
     */
    public double doubleX(int x)
    {
        return x*myCellWidth;
    }
    
    /**
     * Returns the double y coordinate of the location at the 
     * specified cell.
     */
    public double doubleY(int y)
    {
        return y*myCellHeight;
    }
    
    /**
     * Returns the x coordinate of the location in the environment 
     * that the specified double coordinate is in.
     */
    public int cellX(double x)
    {
        return (int)(x/myCellWidth);
    }
    
    /**
     * Returns the y coordinate of the location in the environment 
     * that the specified double coordinate is in.
     */
    public int cellY(double y)
    {
        return (int)(y/myCellHeight);
    }
    
    /**
     * Returns the Location that corresponds to the x,y double coordinates.
     */
    public Location toLocation(double x, double y)
    {
        return new Location(cellX(x), cellY(y));
    }
    
    /**
     * Returns a copy of this environment.
     */
    public BoundedEnv copy()
    {
        //copy dimensions
        BoundedEnv env = new BoundedEnv(myWidth, myHeight, 
                                        myCellWidth, myCellHeight);
                                        
        //copy objects
        ArrayList<Locatable> objs = this.allObjects();
        for (Locatable obj : objs)
            env.add(obj);
        
        return env;
    }
    
    /**
     * Returns an arraylist of all the locatable objects in the environment.
     */
    public ArrayList<Locatable> allObjects()
    {
        ArrayList<Locatable> objs = new ArrayList<Locatable>();
        for(int i = 0; i < grid.length; i++)
            for(int j = 0; j < grid[i].length; j++)
                if(grid[i][j] != null) //something there
                    objs.add(grid[i][j]);
        return objs;
    }
    
    /**
     * Returns an arraylist of all the empty locations in the environment.
     */
    public ArrayList<Location> allEmptyLocations()
    {
        ArrayList<Location> locs = new ArrayList<Location>();
        for(int i = 0; i < grid.length; i++)
            for(int j = 0; j < grid[i].length; j++)
                if(grid[i][j] == null) //loc is empty
                    locs.add(new Location(i,j));
        return locs;
    }
    
    /**
     * Returns whether or not the environment contains the given object.
     */
    public boolean contains(Locatable obj)
    {
        return this.objectAt(obj.loc()) != null;
    }
    
    /**
     * Returns the direction that would need to be traveled to go 
     * from loc1 to loc2. If the locations do not share any row or 
     * column, will return the direction that will bring it closest.
     * Returns null if the locations are the same.
     */
    public Direction getDirection(Location loc1, Location loc2)
    {
        int x1 = loc1.x();
        int y1 = loc1.y();
        int x2 = loc2.x();
        int y2 = loc2.y();
        
        if (loc1.equals(loc2))
            return Direction.UNKNOWN;
            
        if (y1 == y2) // no change in y
        	return (x1 - x2 > 0) ? Direction.WEST : Direction.EAST;
        else if (x1 == x2) // no change in x
        	return (y1 - y2 > 0) ? Direction.NORTH : Direction.SOUTH;
        else if (x1 > x2 && y1 > y2) // north west
        	return Direction.NORTHWEST;
        else if (x1 < x2 && y1 > y2) // north east
        	return Direction.NORTHEAST;
        else if (x1 > x2 && y1 < y2) // south west
        	return Direction.SOUTHWEST;
        else if (x1 < x2 && y1 < y2) // south east
        	return Direction.SOUTHEAST;
        
        return Direction.UNKNOWN;

    }
    
    /**
     * Returns a path (ArrayList of locations in order) from 
     * start to end that manuevers around the objects in the  
     * environment, ignoring the last location it just checked. 
     * Returns null if no path can be found. The path returned 
     * is in order, including the starting and ending locations.
     */
    public ArrayList<Location> getPath(Location start, Location end)
    {
        ArrayList<PathNode> path = new ArrayList<PathNode>();
        ArrayList<Location> forks = new ArrayList<Location>();
        
        path.add(new PathNode(this, start));
        
        boolean backtrack = false; //if currently backtracking
        
        Random rand = new Random();
        PathNode next = path.get(path.size() - 1);
        PathNode last = null;
        Direction favDir = getDirection(start, end); //general direction
        while (!next.location().equals(end)) //not at end
        {
            if (last != null && next.adjacentTo(last.location()))
                next.setExplored(next.indexOf(last.location()), true);
                
            if (next.hasUnexplored()) //there are empty locations around it
            {
                //none unexplored, backtrack to last fork
                if (next.numUnexplored() == 0) 
                {
                    //go back to last fork
                    for (int i = path.size() - 1; i >= 0; i--)
                    {
                        if (path.get(i).hasUnexplored()) //found one
                            break;
                        else
                            path.remove(i);
                    }
                }
                //contains location going in general direction, try it
                else if (next.adjacentTo(neighborOf(next.location(), favDir)))
                {
                    int index = next.indexOf(neighborOf(next.location(), favDir));
                    path.add(new PathNode(this, next.getNeighbor(index)));
                    next.setExplored(index, true);
                }
                //does not contain location in general direction, try other loc
                else
                {
                    Location loc = next.nextUnexplored();
                    path.add(new PathNode(this, loc));
                    next.setExplored(next.indexOf(loc), true);
                }
            }
            else
            {
                return null;
            }
            last = next;
            next = path.get(path.size() - 1);
        }
        
        //convert to location list
        ArrayList<Location> pathLoc = new ArrayList<Location>();
        for (int i = 0; i < path.size(); i++)
            pathLoc.add(path.get(i).location());
        
        return pathLoc;
    }
    
    /**
     * Returns a path (ArrayList of locations in order) from 
     * start to end that manuevers around the objects in the  
     * environment, ignoring the last location it just checked 
     * and all objects of the specified type. Returns null if 
     * no path can be found. The path returned is in order, 
     * including the starting and ending locations.
     */
    public ArrayList<Location> getPath(Location start, Location end, 
                                            String[] ignoreString)
    {
        //create copy of this environment
        BoundedEnv copy = this.copy();
        
        //find classes with specified class names
        try
        {
            Class[] ignore = new Class[ignoreString.length];
            for (int i = 0; i < ignoreString.length; i++)
                ignore[i] = Class.forName(ignoreString[i]);
                
            //remove all objects with the specified class/superclass
            ArrayList<Locatable> objs = copy.allObjects();
            for (Locatable obj : objs)
            {
                Class objClass = obj.getClass();
                Class objSuper = objClass.getSuperclass();
                for (int i = 0; i < ignore.length; i++)
                    if (objClass.equals(ignore[i]) || objSuper.equals(ignore[i]))
                        copy.remove(obj);
            }
//             System.out.println(copy);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e);
        }
        finally
        {
            return copy.getPath(start, end);
        }
    }
    
    /**
     * Returns a String representation of the environment.
     */
    public String toString()
    {
        String s = "";
        for (int j = 0; j < myHeight; j++)
        {
            for (int i = 0; i < myWidth; i++)
            {
                if (grid[i][j] != null)
                    s += grid[i][j].loc().toString();
                else
                    s += "empty";
            }
            s += "\n";
        }
        return s;
    }
    
    /**
     * The PathNode class represents a location on a single path. 
     * Each node keeps track of its adjacent, empty locations.
     */
    private class PathNode
    {
        private ArrayList<Location> nbrs; //list of adjacent empty neighbors
        private ArrayList<Boolean> explored; //returns whether or not the 
                                             //neighbor has been traveled
                                             //correpsonds to the locations in
                                             //nbrs
        private BoundedEnv theEnv; //environment node is in
        private Location myLoc; //location represented by this node
        
        /**
         * Creates a PathNode with the specified location in the 
         * given environment.
         */
        public PathNode(BoundedEnv env, Location loc)
        {
            theEnv = env;
            myLoc = loc;
            
            nbrs = theEnv.emptyNeighbors(loc);
            explored = new ArrayList<Boolean>();
            for (int i = 0; i < nbrs.size(); i++)
                explored.add(false);
        }
        
        /**
         * Returns the neighboring location at the specified index.
         */
        public Location getNeighbor(int index)
        {
            explored.set(index, true);
            return nbrs.get(index);
        }
        
        /**
         * Returns how many empty neighbors the path node has.
         */
        public int numNeighbors()
        {
            return nbrs.size();
        }
        
        /**
         * Returns how many empty neighbors the node contains that 
         * have not been explored yet.
         */
        public int numUnexplored()
        {
            int count = 0;
            for (int i = 0; i < nbrs.size(); i++)
                if (!explored.get(i)) //not explored
                    count++;
            return count;
        }
        
        /**
         * Returns whether or not the node contains the given 
         * location as an empty neighbor.
         */
        public boolean adjacentTo(Location loc)
        {
            return nbrs.contains(loc);
        }
        
        /**
         * Returns whether or not the neighbor at the specified 
         * index has been explored yet.
         */
        public boolean isExplored(int index)
        {
            return explored.get(index);
        }
        
        /**
         * Sets the explored state of the neighbor at the 
         * specified index to the given value.
         */
        public void setExplored(int index, boolean newVal)
        {
            explored.set(index, newVal);
        }
        
        /**
         * Returns the index of the given location.
         */
        public int indexOf(Location loc)
        {
            return nbrs.indexOf(loc);
        }
        
        /**
         * Returns whether or not the node has any more unexplored 
         * empty locations next to it.
         */
        public boolean hasUnexplored()
        {
            return numUnexplored() > 0;
        }
        
        /**
         * Returns the location represented by this path node.
         */
        public Location location()
        {
            return myLoc;
        }
        
        /**
         * Returns the next unexplored location adjacent to the node.
         * Returns null if no more unexplored locations.
         */
        public Location nextUnexplored()
        {
            for (int i = 0; i < nbrs.size(); i++)
            {
                if (!explored.get(i))
                {
                    explored.set(i, true);
                    return nbrs.get(i);
                }
            }
            return null;
        }
    }
}