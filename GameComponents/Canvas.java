package GameComponents;

/**
 * A date representation of a bounding rectangle where 
 * graphics can be drawn.
 */
public class Canvas
{
    private double myWidth; //width of canvas
    private double myHeight; //height of canvas
    
    /**
     * Creates a new canvas with the specified dimensions.
     */
    public Canvas(double width, double height)
    {
        myWidth = width;
        myHeight = height;
    }
    
    /**
     * Returns the width of the canvas.
     */
    public double width()
    {
        return myWidth;
    }
    
    /**
     * Returns the height of the canvas.
     */
    public double height()
    {
        return myHeight;
    }
}