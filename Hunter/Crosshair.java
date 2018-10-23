package Hunter;

//import 
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import GameComponents.Shape2D;

/**
 * Crosshair that replaces the mouse cursor. Used in targeting 
 * and shooting a target.
 */
public class Crosshair extends Shape2D
{
    //constants
    private static final String IMAGE = "hunter/crosshair.gif"; //image
    private static final int SIZE = 45; //size of crosshairs
    
    
    private String[] WEAPONS; //list of weapon types
    private GameComponents.Canvas canvas; //canvas crosshair is on
    private ArrayList<Weapon> myWpns; //list of all weapons objects
    private Weapon myWpn; //currently selected weapon
    private int myWpnIndex; //index of current weapon
    
    /**
     * Creates a Crosshair at the specified (x,y) coordinates onto 
     * the given canvas with the specified initial weapon types
     */
    public Crosshair(double x, double y, String[] wpnTypes, 
                        GameComponents.Canvas acanvas)
    {
        super(x, y, SIZE, SIZE);
        canvas = acanvas;
        WEAPONS = wpnTypes;
        
        //initialize all weapons
        myWpns = new ArrayList<Weapon>();
        for (int i = 0; i < WEAPONS.length; i++)
            myWpns.add(Weapon.createInstanceOf(WEAPONS[i]));
            
        //set selected weapon to one at first index
        myWpnIndex = 0;
        myWpn = myWpns.get(myWpnIndex);
    }
    
    /**
     * Switches the weapon to the next one.
     */
    public void nextWpn()
    {
        myWpnIndex++;
        
        if (myWpnIndex >= myWpns.size()) //out of bounds
            myWpnIndex = 0;
            
        myWpn = myWpns.get(myWpnIndex);
        System.out.println("wpn switched to " + myWpn.name());
    }
    
    /**
     * Switches the weapon to the previous one.
     */
    public void prevWpn()
    {
        myWpnIndex--;
        
        if (myWpnIndex < 0) //out of bounds
            myWpnIndex = myWpns.size() - 1;
            
        myWpn = myWpns.get(myWpnIndex);
        System.out.println("wpn switched to " + myWpn.name());
    }
    
    /**
     * Fires the currently selected weapon if possible. Returns whether or 
     * not the weapon successfully fired.
     */
    public boolean fire()
    {
        if (myWpn == null) //no selected weapon
            return false;
        
        int oldAmmo = myWpn.ammo();
        myWpn.fire();
        if (myWpn.ammo() == oldAmmo) //no change
            return false;
        
        //remove weapon if out of ammo
        if (myWpn.ammo() <= 0)
        {
            myWpns.remove(myWpnIndex);
            
            if (!hasWpnsLeft()) //no weapons
                myWpn = null;
            else
                prevWpn();
        }
        return true;
    }
    
    /**
     * Moves the center of the crosshair to the specified coordinates.
     */
    public void moveTo(double x, double y)
    {
        myX = x - SIZE/2;
        myY = y - SIZE/2;
    }
    
    /**
     * Sets the weapon set to the specified one, reloading them. 
     * Sets the currently selected weapon to the first in the list.
     */
    public void setWpns(String[] wpnTypes)
    {
        WEAPONS = wpnTypes;
        
        //initialize all weapons
        myWpns = new ArrayList<Weapon>();
        for (int i = 0; i < WEAPONS.length; i++)
            myWpns.add(Weapon.createInstanceOf(WEAPONS[i]));
            
        //set selected weapon to one at first index
        myWpnIndex = 0;
        myWpn = myWpns.get(myWpnIndex);
    }
    
    /**
     * Returns the currently equipped weapon.
     */
    public Weapon currentWpn()
    {
        return myWpn;
    }
    
    /**
     * Returns whether or not the crosshair has any more weapons.
     */
    public boolean hasWpnsLeft()
    {
        return !(myWpns.size() <= 0);
    }
    
    /**
     * Returns the x coordinates of the center of the crosshair.
     */
    public double centerX()
    {
        return myX + SIZE/2;
    }
    
    /**
     * Returns the y coordinates of the center of the crosshair.
     */
    public double centerY()
    {
        return myY + SIZE/2;
    }
    
    /**
     * Returns a shape representation of the crosshairs.
     */
    public Shape shape()
    {
        return new Rectangle2D.Double(myX, myY, myWidth, myHeight);
    }
    
    /**
     * Paints the crosshair onto the container. Paints the label 
     * of the currently selected weapon.
     */
    public void paintComponent(Graphics2D g2)
    {
        //paint crosshair and label of current weapon if not null
        if (myWpn != null)
        {
            myWpn.paintWeapon(g2, myX, myY);
            g2.setColor(Color.black);
            
            g2.setFont(new Font("", Font.PLAIN, 20));
            g2.drawString(myWpn.name() + ": " + myWpn.ammo() + "/" + 
                            myWpn.maxAmmo(), 20, (float)(canvas.height() - 50));
        }
    }
}