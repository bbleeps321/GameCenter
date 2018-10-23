package TowerDefense;

//import
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

/**
 * A display for information on a unit (tower, creep). Displays the 
 * unit's range, speed, cost, health (all as applicable).
 */
public class UnitDisplay extends JComponent
{
    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;
    private Unit myUnit; //unit being displayed
    
    /**
     * Creates a UnitDisplay with no unit.
     */
    public UnitDisplay()
    {
        myUnit = null;
    }
    
    /**
     * Sets the unit being displayed to the specified one.
     */
    public void setUnit(Unit aUnit)
    {
        myUnit = aUnit;
    }
    
    /**
     * Paints the current unit's stats at the specified 
     * coordinates with the given dimensions in the container.
     */
    public void paintComponent(Graphics2D g2, double x, double y,
                                        double width, double height, Font f)
    {
        if (myUnit != null) //paint only if there is a unit
        {
            //fill background
            g2.setColor(Color.yellow);
            g2.fill(new Rectangle2D.Double(x, y, width, height));
            
            //draw text
            g2.setColor(Color.red);
            g2.setFont(f);
            double nextY = y;
            String[] text;
            if (myUnit.isDummy())
                text = new String[] {myUnit.name(), "Cost: " + myUnit.cost(),
                        myUnit.description()};
            else
                text = new String[] {myUnit.name(), 
                        "Upgrade Cost: " + myUnit.cost(),
                        myUnit.description()};
            for (int i = 0; i < text.length; i++)
            {
                int length = text[i].length();
                double textX = x + (width/2 - length*f.getSize()/4);
                double textY = nextY + f.getSize();
                
                g2.drawString(text[i], (float)textX, (float)textY);
                nextY = textY;
            }
        }
    }
}