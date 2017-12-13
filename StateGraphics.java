import greenfoot.*;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

/**
 * Speichert alle Grafiken, die zum Zustand eines Entities gehören (Activities)
 */
public class StateGraphics 
{
    private Map<String, ActivityGraphics> activities;    
    
    /**
     * sucht nach Activities im übergebenen Pfad
     */
    public StateGraphics(String path)
    {
        // Variablen vorbereiten
        activities = new HashMap<String, ActivityGraphics>();
        
        // Alle Objekte im aktuellen Verzeichnis auflisten       
        File[] directories = Tools.getDirContent(path);
        
        // Alle gefundenen Objekte durchgehen und auswerten
        for (File file: directories)
        {
            // ist das aktuelle Objekt ein Ordner?
            if (file.isDirectory())
            {
                // Ordnerpfad an ActivityGraphics übergeben
                Tools.log("  Aktivität: "+file.getName()+" - "+file.getPath(), "activity");
                activities.put(file.getName(), new ActivityGraphics(file.getPath())); 
            }
            else
            {
                String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
                Tools.log("  Aktivität: "+name, "activity");
                activities.put(name, new ActivityGraphics(file.getPath()));
            }    
                       
        }        
    }   
        
    /**
     * liefert Grafik mit gewünschten Parametern zurück
     */
    public GreenfootImage getImage(String activityName, String orientation, int index)
    {
        ActivityGraphics activity = activities.get(activityName);
        if (activity != null)
        {
            return activity.getImage(orientation, index);
        }
        else
        {
            return null;
        }
        
    }   
}