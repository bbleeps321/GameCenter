package GameComponents;

//import
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * Cache providing access to images, buffered to a standard 
 * configuration for efficiency.
 */
public class SpriteCache extends ResourceCache implements ImageObserver
{
    /**
     * Loads the resource from the specified url
     */
    protected Object loadResource(URL url)
    {
        try 
        {
            return ImageIO.read(url);
        } 
        catch (Exception e) 
        {
            System.out.println("Image " + url + " not found.");
        
            return null;
        }
    }
    
    /**
     * Returns a BufferedImage of a standard configuration.
     */
    public BufferedImage createCompatible(int width, int height, 
                                            int transparency)
    {
        GraphicsConfiguration gc = GraphicsEnvironment.
                                    getLocalGraphicsEnvironment().
                                    getDefaultScreenDevice().
                                    getDefaultConfiguration();
        BufferedImage compatible = gc.createCompatibleImage(width, height, 
                                                        transparency);
        return compatible;
    }
    
    /**
     * Returns a sprite image using the given file name.
     */
    public BufferedImage getSprite(String name)
    {
        BufferedImage loaded = (BufferedImage)getResource(name);
        BufferedImage compatible = createCompatible(loaded.getWidth(),
                            loaded.getHeight(), Transparency.BITMASK); 
        Graphics g = compatible.getGraphics();
        g.drawImage(loaded, 0, 0, this);
        return compatible;
    }
    
    /**
     * Updates the image.
     */
    public boolean imageUpdate(Image img, int infoflags,int x, 
                                int y, int w, int h)
    {
        return (infoflags & (ALLBITS|ABORT)) == 0;
    }
}