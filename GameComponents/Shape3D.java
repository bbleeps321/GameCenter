/**
 * 
 */
package GameComponents;

import java.awt.Color;
import java.awt.Shape;

import javax.media.j3d.DirectionalLight;
import javax.media.j3d.TransformGroup;

/**
 * @author Owner
 *
 */
public abstract class Shape3D extends Shape2D implements Steppable
{
	protected float myZ;						// z coordinate of shape.
	protected double myDepth;					// depth of shape.
	protected TransformGroup transGroup;		// transform group of object.
	protected DirectionalLight light;			// light shining on object.
	
	/**
	 * Initializes a 3D shape at the specified xyz coordinates with the given width, height,
	 * and depth in the given branch group with the given light. The TransformGroup and 
	 * DirectionalLight should be initialized and added to the root node 
	 * after this apple has been created for it to display correctly.
	 */
	public Shape3D(float x, float y, float z, float width, float height, float depth,
			TransformGroup g, DirectionalLight l)
	{
		super(x, y, width, height);
		myZ = z;
		myDepth = depth;
		transGroup = g;
		light = l;
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public Shape3D(double x, double y, double width, double height, Color color)
	{
		super(x, y, width, height, color);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param pic
	 */
	public Shape3D(double x, double y, double width, double height, String pic)
	{
		super(x, y, width, height, pic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see GameComponents.Shape2D#shape()
	 */
	@Override
	public Shape shape()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see GameComponents.Steppable#step()
	 */
	@Override
	public void step()
	{
		// TODO Auto-generated method stub

	}

}
