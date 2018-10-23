package GameComponents;

//import
import java.util.ArrayList;

/**
 * Controls an ArrayList of Steppable objects. Has a method for 
 * strpping them.
 */
public class Simulation implements Steppable
{
    private ArrayList<Steppable> objects; //list of Steppable objects
    
    /**
     * Creates a Simulation controlling the specified ArrayList 
     * of Steppable objects to control.
     */
    public Simulation(ArrayList<Steppable> list)
    {
        objects = list;
    }
    
    /**
     * Steps all the objects in the Steppable ArrayList.
     */
    public void step()
    {
        for (Steppable obj : objects)
            obj.step();
    }
    
    /**
     * Adds the specified Steppable object to the list.
     */
    public void add(Steppable obj)
    {
        objects.add(obj);
    }
}