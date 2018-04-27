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
    private Map<String, GreenfootImage> imageCache;
    private double lastScale;
        
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
        imageCache = new HashMap<>();
        
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
        EntityGraphics entityGraphics = entities.get(entityName);
        if (entityGraphics != null)
        {
            GreenfootImage image = entityGraphics.getImage(stateName, activityName, orientation, index);
            
            // create unique name for each file
            String subject = entityName + "_" + stateName + "_" + activityName + "_" + orientation + "_" + image.getFont().getName();
                      
            if (image != null)
            {
                return scaleImage(image, subject, scale);
            }
            else
            {
                return scaleImage(noImage, "noImage", scale);
            }
        }
        else
        {
            return scaleImage(noImage, "noImage", scale);
        } 
    }
    
    /**
     * scaliert ein übergebenes Bild auf den gewünschten Faktor
     * etwas größere Skalierung im Spiel, um Flimmern zu vermeiden
     * etwas kleinere Skalierung im Editor, um Gitter hervorzuheben
     */
    private GreenfootImage scaleImage(GreenfootImage image, String identifier, double scale)
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
            
            if (lastScale != scale)
            {
                // if scale changed, delete cache
                //System.out.println("New scale: "+scale);
                imageCache = new HashMap<>();
                lastScale = scale;
            }
            
            // check if image is in cache
            if (imageCache.containsKey(identifier))
            {
                // return image from cache
                return imageCache.get(identifier);
            }
            else
            {
                // scale image and store in cache
                GreenfootImage scaledImage = new GreenfootImage(image);
                scaledImage.scale((int)(image.getWidth()*scale), (int)(image.getHeight()*scale));
                imageCache.put(identifier, scaledImage);
                //System.out.println("stored " + identifier + " in cache");
                return scaledImage;
            }
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
