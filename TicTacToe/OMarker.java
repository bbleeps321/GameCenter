package TicTacToe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

public class OMarker extends JComponent
{
    private static final double SIZE = 80;
    
    private Ellipse2D.Double mark;
    
    /**Precondition: none
     * 
     * Post-Condition: creates an ellipse representing an O at the specified coordinates
     */
    public OMarker(double left, double top)
    {
        mark = new Ellipse2D.Double(left + 10, top + 10, SIZE, SIZE);
    }
    
    /**Precondition: none
     * 
     * Post-Condition: paints the O onto the canvas.
     */
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.blue);
        
        g2.draw(mark);
    }

}