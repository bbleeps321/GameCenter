package Hunter;

//import
import java.awt.Graphics2D;
import java.lang.reflect.Constructor;

/**
 * Abstract class for all weapons.
 */
public abstract class Weapon
{
    protected String myName; //name of weapon
    protected int myAmmo; //ammo left
    protected int myMaxAmmo; //max ammo
    protected int myDmg; //amount of damage the weapon inflicts
    
    /**
     * Creates a Weapon with the specified name, ammo, and damage amount.
     */
    protected Weapon(String name, int ammo, int dmg)
    {
        myName = name;
        myAmmo = ammo;
        myMaxAmmo = myAmmo;
        myDmg = dmg;
    }
    
    /**
     * Creates a new instance of a weapon with the given class name.
     */
    public static Weapon createInstanceOf(String className)
    {
        try
        {
            //find constructor requiring types of arguments
            Class classToCreate = Class.forName("Hunter." + className);
            Class[] params = {};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] args = {};
            return (Weapon)constructor.newInstance(args);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Fires the weapon if there is any ammo left.
     */
    public void fire()
    {
        if (myAmmo > 0) //has ammo
            myAmmo--;
    }
    
    /**
     * Returns the name of the weapon.
     */
    public String name()
    {
        return myName;
    }
    
    /**
     * Returns the ammo remaining for the weapon.
     */
    public int ammo()
    {
        return myAmmo;
    }
    
    /**
     * Returns the max ammunition for this weapon
     */
    public int maxAmmo()
    {
        return myMaxAmmo;
    }
    
    /**
     * Returns the damage this weapon inflicts
     */
    public int dmg()
    {
        return myDmg;
    }
    
    /**The crosshairs representing this weapon at the specified coordinates.**/
    public abstract void paintWeapon(Graphics2D g2, double x, double y);
}