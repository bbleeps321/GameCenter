package TowerDefense;

//import
import java.awt.Image;

/**
 * Interface for all objects whose information can be viewed. 
 * Imposes methods such as description and image. Intended for 
 * use with a CommandBar.
 */
public interface Unit
{
    /**Returns the name of the unit.**/
    public String name();
    
    /**Returns a description of the unit.**/
    public String description();
    
    /**Returns the image representation of the unit.**/
    public Image image();
    
    /**Returns the cost of the unit.**/
    public int cost();
    
    /**Returns whether or not the unit is a dummy.**/
    public boolean isDummy();
}