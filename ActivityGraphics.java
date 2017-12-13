import greenfoot.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;

/**
 * Speichert rechte und linke Version aller Grafiken die zu einer Animation (Activity) gehören
 */
public class ActivityGraphics 
{
    private List<GreenfootImage> facingRight;
    private List<GreenfootImage> facingLeft;
    
    /**
     * Grafiken aus übergebenem Verzeichnis laden
     * oder einzelnes Bild laden
     */
    public ActivityGraphics(String path)
    {        
        // Variablen vorbereiten
        facingRight = new ArrayList<GreenfootImage>();
        facingLeft = new ArrayList<GreenfootImage>();
        
        // Verweist der übergebene Pfad auf einen Ordner oder eine Datei?
        if (path.endsWith(".png"))
        {
            // Logeintrag
            Tools.log("   Einzelgrafik: "+path, "image"); 
            
            // Bild speichern
            facingRight.add(Tools.loadImage(path));
            
            // Bild spiegeln und speichern
            GreenfootImage left = Tools.loadImage(path);
            left.mirrorHorizontally();
            facingLeft.add(left);
        }
        else
        {
            // Dateien in übergebenem Verzeichnis durchgehen
            File[] files = Tools.getDirContent(path, "file");
        
            // Alle Dateien durchgehen
            for(File file: files)
            {
                // Logeintrag
                Tools.log("   Grafik: "+file.getName()+" - "+file.getPath(), "image");                 
                
                // GreenfootImage aus Bilddatei erstellen und an Liste anhängen
                facingRight.add(Tools.loadImage(file.getPath()));
                
                // Bild spiegeln und speichern
                GreenfootImage left = Tools.loadImage(file.getPath());
                left.mirrorHorizontally();
                facingLeft.add(left);
            }        
        }
    }
        
    /**
     * Bild mit der gewünschten Ausrichtung und dem entsprechenden Index in der Animation zurückgeben
     */
    public GreenfootImage getImage(String orientation, int index) 
    {
        //System.out.println(currentOrientation.equals("right"));
        if(orientation.equals("right"))
        {
            return facingRight.get(index % facingRight.size());
        }
        else
        {
            return facingLeft.get(index % facingLeft.size());
        }
    }
    
}