package Hunter;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * The basic kind of weapon, with average damage and ammo.
 */
public class Rifle extends Weapon
{
    //constants
    private static final String NAME = "Rifle"; //name of weapon
    private static final int MAX_AMMO = 15; //ammo the weapon has initially
    private static final int DAMAGE = 1; //damage each shot inflicts; score bonus
    private static final int SIZE = 40; //size of weapon's crosshair
    private static final Color COLOR = Color.black; //color of weapon's crosshair
    
    /**
     * Creates a fully-loaded rifle.
     */
    public Rifle()
    {
        super(NAME, MAX_AMMO, DAMAGE);
    }
    
    /**
     * Paints the crosshairs representing this weapon at the specified 
     * coordinates.
     */
    public void paintWeapon(Graphics2D g2, double x, double y)
    {
        //initialize lines of crosshair
        Line2D.Double line1 = new Line2D.Double(x, y + SIZE/2,
                                                x + SIZE, y + SIZE/2);
        Line2D.Double line2 = new Line2D.Double(x + SIZE/2, y,
                                                x + SIZE/2, y + SIZE);
                                                
        //paint lines
        g2.setColor(COLOR);
        g2.draw(line1);
        g2.draw(line2);
    }
}