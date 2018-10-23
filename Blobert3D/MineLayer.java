package Blobert3D;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * The blobert object that is controlled in the Blobert game. 
 * A MineLayer can create a mine with the ability to freeze
 * ghosts in the game on command. Represented by a blue/cyan 
 * rectangle
 */
public class MineLayer extends Blobert
{
    //constants
    private ArrayList<Mine> mines; //list of all mines laid by blobert
    
    public MineLayer(double x, double y, GameComponents.Canvas acanvas, 
                                ArrayList<Ghost> graveyard)
    {
        super(x, y, acanvas, graveyard);
        mines = new ArrayList<Mine>();
    }
    
    /**
     * Steps the blobert. Also steps all this blober's mines.
     */
    public void step()
    {
       super.step();
       for (Mine mine : mines)
           mine.step();
           
       removeHiddenMines();
    }
    
    /**
     * First ability of blobert. Lays a freeze mine at the current 
     * location.
     */
    public void act1()
    {
        super.act1();
        mines.add(new FreezeMine(myX, myY, theGrave));
    }
    
    /**
     * Second ability of blobert. Lays an explosive mine at the 
     * current location.
     */
    public void act2() 
    {
        super.act2();
        mines.add(new Mine(myX, myY, theGrave));
    }
    
    /**
     * Returns the cost of using the first ability.
     */
    public int act1Cost()
    {
        return 2;
    }
    
    /**
     * Returns the cost of using the second ability.
     */
    public int act2Cost()
    {
        return 3;
    }
    
    /**
     * Returns the shape representation of the blobert.
     */
    public Shape shape()
    {
        return new Rectangle2D.Double(myX, myY, mySize, mySize);
    }
    
    /**
     * Removes all the hidden mines from the arraylist.
     */
    private void removeHiddenMines()
    {
        for (int i = 0; i < mines.size(); i++)
        {
            if (mines.get(i).isHidden())
            {
                mines.remove(i);
                i--;
            }
        }
    }       
    
    /**
     * Returns the preferred color of the blobert.
     */
    protected Color preferredColor()
    {
        if (isMoving())
            return Color.cyan;
        else
            return Color.blue;
    }
    
    /**
     * Paints the component and all of its mines onto the canvas.
     */
    public void paintComponent(Graphics2D g2)
    {
        super.paintComponent(g2);
        for (Mine mine : mines)
            mine.paintComponent(g2);
    }
}
