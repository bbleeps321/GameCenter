package Go;

//import
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import GameComponents.BoundedEnv;
import GameComponents.Location;

/**
 * Board for the go game.
 */
public class Board extends JComponent
{
	// constants
	private static final Location[] STAR_POINTS = {new Location(3,3), new Location(15,3),
												   new Location(3,15), new Location(15,15),
												   new Location(3,9), new Location(9,3),
												   new Location(15,9), new Location(9,15),
												   new Location(9,9)};
	
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
        g2.setColor(Color.yellow);
        g2.fill(new Rectangle2D.Double(0, 0, (theEnv.width()+1)*theEnv.cellWidth(), (theEnv.height()+1)*theEnv.cellHeight()));
        
        // paint grid rectangles
        g2.setColor(Color.black);
        for (int i = 0; i < theEnv.width(); i++)          // for each column
            for (int j = 0; j < theEnv.height(); j++)     // for each row
                g2.draw(new Rectangle2D.Double((i+.5)*theEnv.cellWidth(), (j+.5)*theEnv.cellHeight(), theEnv.cellWidth(), theEnv.cellHeight()));
        
        // paint starpoints
        double size = theEnv.cellWidth()/3;
        for (int i = 0; i < STAR_POINTS.length; i++)
        {
        	double x = (STAR_POINTS[i].x()+.5)*theEnv.cellWidth() - size/2;
        	double y = (STAR_POINTS[i].y()+.5)*theEnv.cellWidth() - size/2;
        	g2.fill(new Ellipse2D.Double(x, y, size, size));
        }
    }
    
}