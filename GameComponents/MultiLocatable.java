package GameComponents;

/**
 * Interface for all locatable objects that may be in more 
 * than one location.
 */
public interface MultiLocatable extends Locatable
{
    /**Returns an array of locations that this object occupies.**/
    public Location[] locs();
}