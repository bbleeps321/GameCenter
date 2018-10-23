package GameComponents;

//import
import java.net.URL;
import java.util.HashMap;

/**
 * Abstract class for different caches used in games (sound, sprites, etc.).
 */
public abstract class ResourceCache
{
    protected HashMap resources;
	
    /**
     * Initializes the cache.
     */
	public ResourceCache() 
	{
        resources = new HashMap();
	}
	
	/**
	 * Loads the resource from the specified name.
	 */
	protected Object loadResource(String name) 
	{
		URL url = null;
		url = getClass().getClassLoader().getResource(name);
		return loadResource(url);
	}
	
	/**
	 * Gets the resource from the specified name.
	 */
	protected Object getResource(String name) 
	{
		Object res = resources.get(name);
		if (res == null)
		{
			res = loadResource("res/"+name);
			resources.put(name, res);
		}
		return res;
	}
	
	protected abstract Object loadResource(URL url);
}