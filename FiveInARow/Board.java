package FiveInARow;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import GameComponents.BoundedEnv;

/**
 * Board for the five-in-a-row game.
 */
public class Board extends JComponent
{
    private BoundedEnv theEnv;          // the environment containing the pieces.

    /**
     * Initializes the board.
     */
    public Board(int rows, int cols, int size)
    {
        theEnv = new BoundedEnv(rows, cols, size, size);
    }
    
    /**
     * Returns the environment this board represents.
     */
    public BoundedEnv env()
    {
        return theEnv;
    }

    /**
     * Paints the board.
     */
    public void paintBoard(Graphics2D g2)
    {
        // paint background
        g2.setColor(new Color(255, 222, 173));
        g2.fill(new Rectangle2D.Double(0, 0, (theEnv.width()+1)*theEnv.cellWidth(), (theEnv.height()+1)*theEnv.cellHeight()));
        
        // paint grid rectangles
        g2.setColor(Color.black);
        for (int i = 0; i < theEnv.width(); i++)          // for each column
            for (int j = 0; j < theEnv.height(); j++)     // for each row
                g2.draw(new Rectangle2D.Double((i+.5)*theEnv.cellWidth(), (j+.5)*theEnv.cellHeight(), theEnv.cellWidth(), theEnv.cellHeight()));
    }
    
}