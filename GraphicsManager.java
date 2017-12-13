import greenfoot.*;
import java.io.File;
import java.util.Map;
import java.util.HashMap;

/**
 * Verantwortlich für das Laden, Skalieren, Speichern und Bereitstellen aller Grafiken im Spiel
 * 
 * @author Simon Kemmesies
 * @version 16.11.2017
 */
public class GraphicsManager
{
    private static GreenfootImage noImage;
    private Map<String, EntityGraphics> entities;
    private double scale;
    private String mode;
        
    /**
     * GraphicsManager erstellen und Grafikverzeichnis übergeben
     */
    public GraphicsManager(String path)
    {
        // Variablen vorbereiten
        entities = new HashMap<String, EntityGraphics>();
        noImage = Tools.loadImage("");
        scale = 1;
        mode = "ingame";
        
        // Alle Ordner im angegebenen Pfad suchen
        File[] directories = Tools.getDirContent(path, "dir");
        
        // Ergebnisse durchgehen und für jeden Entity ein neues EntityGraphics-Objekt erstellen
        for(File file: directories)
        {
            Tools.log("Entität: "+file.getName()+" - "+file.getPath(), "entity");
            entities.put(file.getName(), new EntityGraphics(file.getPath()));            
        }  
       
    }  
    
    /**
     * liefert Entity-Grafik anhand der übergebenen Parameter zurück
     */
    public GreenfootImage getImage(String entityName, String stateName, String activityName, String orientation, int index)
    {
        EntityGraphics entity = entities.get(entityName);
        if (entity != null)
        {
            GreenfootImage image = entity.getImage(stateName, activityName, orientation, index);
            if (image != null)
            {
                return scaleImage(image, scale);
            }
            else
            {
                return scaleImage(noImage, scale);
            }
        }
        else
        {
            return scaleImage(noImage, scale);
        } 
    }
    
    /**
     * scaliert ein übergebenes Bild auf den gewünschten Faktor
     * etwas größere Skalierung im Spiel, um Flimmern zu vermeiden
     * etwas kleinere Skalierung im Editor, um Gitter hervorzuheben
     */
    private GreenfootImage scaleImage(GreenfootImage image, double scale)
    {
        if (scale == 1)
        {
            return image;
        }
        else
        {
            if (mode.equals("ingame"))
            {
                scale += 0.1;
            }
            else
            {
                scale = scale*0.95;
            }
            
            GreenfootImage scaledImage = new GreenfootImage(image);
            scaledImage.scale((int)(image.getWidth()*scale), (int)(image.getHeight()*scale));
            return scaledImage;
        }
    }
    
    /**
     * liefert grauen Fragezeichenblock als Platzhalter für andere Grafiken
     */
    public GreenfootImage getImage()
    {
        return noImage;
    }
    
    /**
     * Skalierungsfaktor setzen
     */
    public void setScale(double scale)
    {
        this.scale = scale;
    }
    
    /**
     * Spielmodus (ingame, editor) setzen
     */
    public void setMode(String mode)
    {
        this.mode = mode;
    }
}   
