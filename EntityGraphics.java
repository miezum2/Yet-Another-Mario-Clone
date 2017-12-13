import greenfoot.*;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

/**
 * Verwaltung aller Grafiken eines Entities (Zustände)
 */
public class EntityGraphics
{
    private static final String noImageFile = "noImage.png";
    private static GreenfootImage noImage;
    
    private Map<String, StateGraphics> states;
    
    /**
     * alle Status aus übergebenem Verzeichnis laden
     */
    public EntityGraphics(String path)
    {
        // Variablen vorbereiten
        states = new HashMap<String, StateGraphics>();
        noImage = Tools.loadImage(path+"/"+noImageFile);
                   
        // Ordner mit Zuständen laden
        File[] directories = Tools.getDirContent(path, "dir");
        
        // Ergebnisse durchgehen und für jeden Zustand neues AnimationSet anlegen
        for(File file: directories)
        {
            Tools.log(" Erscheinungsbild: "+file.getName()+" - "+file.getPath(), "state");
            states.put(file.getName(), new StateGraphics(file.getPath()));            
        }       
    }
    
    /**
     * liefert Grafik eines Entity entsprechend der übergebenen Parameter
     */
    public GreenfootImage getImage(String stateName, String activityName, String orientation, int index)
    {
        StateGraphics state = states.get(stateName);
        if (state != null)
        {
            GreenfootImage image = state.getImage(activityName, orientation, index);
            if (image != null)
            {
                return image;
            }
            else
            {
                return noImage;
            }
        }
        else
        {
            return noImage;
        } 
    }    
  
}