package TowerDefense;

//import
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * The CommandBard provides and interface between the player's 
 * commands and the game, such as starting the game, selecting 
 * or upgrading a tower, or viewing the statisitics of a unit.
 */
public class CommandBar extends JComponent
{
    //constants
    private static final int GAP = 5; //gap between components and border
    private static final int FONT_SIZE = 20; //font size of text
    
    //the indexes of the following lists should correspond to eachother
    private ArrayList<Unit> dummyList; //list of dummy units used to display 
                                       //general properties
    private String[] towerTypes; //text for selecting a tower to build
    private Rectangle2D.Double[] towerButtons; //"buttons" for selecting towers
    
    private UnitDisplay display; //display for showing selected unit's info
    
    private double initX; //upper left x-coordinate of bar
    private double width, height; //dimensions of bar
    private int selectedIndex; //the index of the selected tower type
                               //-1 if nothing/specific unit selected
    
    /**
     * Creates a CommandBar with the specified list of possible units.
     */
    public CommandBar(String[] towerNames, double ax, 
                        double awidth, double aheight)
    {
        towerTypes = towerNames;
        initX = ax;
        width = awidth;
        height = aheight;
        
        //initialize "buttons" and dummy list
        dummyList = new ArrayList<Unit>();
        towerButtons = new Rectangle2D.Double[towerTypes.length];
        for (int i = 0; i < towerButtons.length; i++)
        {
            double x = initX + GAP;
            double y = (3*GAP + FONT_SIZE)*i + GAP;
            towerButtons[i] = new Rectangle2D.Double(x, y,
                                                     width - 2*GAP, 
                                                     2*GAP + FONT_SIZE);
            dummyList.add(generateDummy(towerTypes[i]));
        }
        
        display = new UnitDisplay();
        selectedIndex = -1;
    }
    
    /**
     * Creates an instance of a dummy unit with the given class name. 
     * Returns null if the class or correct constructor cannot be found.
     */
    public static Unit generateDummy(String className)
    {
        try
        {
            //find constructor requiring no parameters
            Class classToCreate = Class.forName("TowerDefense."+className);
            Class[] params = {};
            Constructor constructor = classToCreate.getConstructor(params);
            
            //create instance of object using found constructor
            Object[] argList = {};
            return (Tower)constructor.newInstance(argList);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Takes in a mouse event and evaluates it for its location 
     * relative to the components in the bar. Returns the a string 
     * representation of what should be done based on what was clicked.
     * Returns true if something happened as a result of the call to 
     * this methog (a "button" was pressed)
     */
    public boolean mouseClicked(MouseEvent e)
    {
        double x = e.getX();
        double y = e.getY();
        
        //find if coordinates are in a "button"
        for (int i = 0; i < towerButtons.length; i++)
        {
            if (towerButtons[i].contains(x,y))
            {
                display.setUnit(dummyList.get(i));
                selectedIndex = i;
            }
        }
        
        //within bounds
        if ((new Rectangle2D.Double(initX, 0, width, height)).contains(x,y))
            return true;
        return false;
    }
    
    /**
     * Sets the unit being displayed by the unit display. If set to 
     * null, will hide the display.
     */
    public void setUnit(Unit u)
    {
        display.setUnit(u);
        selectedIndex = -1;
    }
    
    /**
     * Returns the string name of the selected tower type. Returns 
     * null if no tower type is selected.
     */
    public String getSelected()
    {
        if (selectedIndex == -1)
            return null;
            
        return towerTypes[selectedIndex];
    }
    
    /**
     * Returns the selected tower type's cost. Return 
     * 0 if no tower type is selected
     */
    public int selectedCost()
    {
        if (selectedIndex == -1) //nothing selected
            return 0;
            
        return dummyList.get(selectedIndex).cost();
    }
    
    /**
     * Paints the CommandBar onto the container at the specified x coordinates 
     * with the given bar dimensions.
     */
    public void paintComponent(Graphics2D g2)
    {
        //fill background
        g2.setColor(Color.black);
        g2.fill(new Rectangle2D.Double(initX, 0, width, height));
        
        //paint "buttons"
        g2.setColor(Color.white);
        Font f = new Font("", Font.BOLD, FONT_SIZE);
        g2.setFont(f);
        for (int i = 0; i < towerTypes.length; i++)
        {
            g2.draw(towerButtons[i]);
            
            int textLength = towerTypes[i].length();
            double x = towerButtons[i].getX();
            double y = towerButtons[i].getY();
            double textX = x + (width/2 - textLength*FONT_SIZE/4);
            double textY = y + FONT_SIZE;
            g2.drawString(towerTypes[i], (float)textX, (float)textY);
        }
        
        display.paintComponent(g2, initX + GAP, height/2,
                                   width - 2*GAP, height/4, f);
    }
}