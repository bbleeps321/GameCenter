package GameComponents;

//import
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * Provides access to sound files.
 */
public class SoundCache extends ResourceCache
{
    private AudioClip song; //currently loaded audio
    private String songName; //string name of song
    
    /**
     * Creates an instance of a SoundCache. Initializes 
     * the song and songName fields.
     */
    public SoundCache()
    {
        song = null;
        songName = "";
    }
    
    /**
     * Loads the resource using the given name.
     */
    protected Object loadResource(URL url) 
    {
        return Applet.newAudioClip(url);
    }
    
    /**
     * Returns the audio clip from the specified name.
     */
    public AudioClip getAudioClip(String name)
    {
        return (AudioClip)getResource(name);
    }
    
    /**
     * Plays the sound file with the given name, loading 
     * it in a separate thread.
     */
    public void playSound(final String name)
    {
        new Thread
        (
            new Runnable()
            {
                public void run()
                {
                    try
                    {
                        getAudioClip(name).play();
                    }
                    catch (Exception e) {}
                }
            }
        ).start();
    }
    
    /**
     * Loops the sound file with the given name, loading 
     * it in a separate thread.
     */
    public void loopSound(final String name)
    {
        new Thread
        (
            new Runnable()
            {
                public void run()
                {
                    try
                    {
                        getAudioClip(name).loop();
                    }
                    catch (Exception e) {}
                }
            }
        ).start();
    }
    
    /**
     * Loads the song file with the given name in a 
     * separate thread and updates the current song field.
     */
    public void loadSong(final String name)
    {
        new Thread
        (
            new Runnable()
            {
                public void run()
                {
                    if (!songName.equals(name)) //song is not already loaded
                    {
                        try
                        {
                            song = getAudioClip(name);
                            songName = name;
                        }
                        catch (Exception e) {}
                    }
                }
            }
        ).start();
    }
    
    /**
     * Plays the song file with the given name, loading it 
     * in a separate thread and updating the current song 
     * field.
     */
    public void playSong(final String name)
    {
        new Thread
        (
            new Runnable()
            {
                public void run()
                {
                    if (songName.equals(name)) //already loaded
                    {
                        playSong();
                    }
                    else
                    {
                        try
                        {
                            song = getAudioClip(name);
                            song.play();
                            songName = name;
                        }
                        catch (Exception e) {}
                    }
                }
            }
        ).start();
    }
    
    /**
     * Loops the song file with the given name, loading 
     * it in a separate thread and updating the current 
     * song field.
     */
    public void loopSong(final String name)
    {
        new Thread
        (
            new Runnable()
            {
                public void run()
                {
                    if (songName.equals(name)) //already loaded
                    {
                        loopSong();
                    }
                    else
                    {
                        try
                        {
                            song = getAudioClip(name);
                            song.loop();
                            songName = name;
                        }
                        catch (Exception e) {}
                    }
                }
            }
        ).start();
    }
    
    /**
     * Plays the currently loaded song file.
     */
    public void playSong()
    {
        if (song != null)
            song.play();
    }
    
    /**
     * Loops the currently loaded song file.
     */
    public void loopSong()
    {
        if (song != null)
            song.loop();
    }
    
    /**
     * Stops the currently loaded song.
     */
    public void stopSong()
    {
        if (song != null)
            song.stop();
    }
}