package GameComponents;

/**
 * A BoundedEnv that can contain multilocatable objects.
 */
public class MultiBoundedEnv extends BoundedEnv
{
    /**
     * Creates a BoundedEnv of the specified dimensions with no objects.
     */
    public MultiBoundedEnv(int width, int height, 
                            double cellWidth, double cellHeight)
    {
        super(width, height, cellWidth, cellHeight);
    }
    
    /**
     * Adds the specified multi-locatable object to the environment.
     */
    public void add(MultiLocatable obj)
    {
        Location[] locs = obj.locs();
        for (int i = 0; i < locs.length; i++)
        {
            int x = locs[i].x();
            int y = locs[i].y();
            grid[x][y] = obj;
        }
        myNumObjects++;
    }
    
    /**
     * Removes the specified multi-locatable object from the environment.
     */
    public void remove(MultiLocatable obj)
    {
        Location[] locs = obj.locs();
        for (int i = 0; i < locs.length; i++)
        {
            int x = locs[i].x();
            int y = locs[i].y();
            grid[x][y] = null;
        }
        myNumObjects--;
    }
    
    /**
     * Updates the grid for the location of the specified 
     * MultiLocatable object.
     */
    public void recordMove(MultiLocatable obj, Location[] oldLocs)
    {
        //empty old locations
        for (int i = 0; i < oldLocs.length; i++)
        {
            int oldX = oldLocs[i].x();
            int oldY = oldLocs[i].y();
            grid[oldX][oldY] = null;
        }
        
        //fill new locations
        add(obj);
    }
}