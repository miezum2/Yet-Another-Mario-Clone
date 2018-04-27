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
            GreenfootImage image = Tools.loadImage(path);
            image.setFont(new Font("0", 12));
            facingRight.add(image);
            
            // Bild spiegeln und speichern
            GreenfootImage left = Tools.loadImage(path);
            left.setFont(new Font("0", 12));
            left.mirrorHorizontally();
            facingLeft.add(left);
        }
        else
        {
            // Dateien in übergebenem Verzeichnis durchgehen
            File[] files = Tools.getDirContent(path, "file");
        
            // Alle Dateien durchgehen
            int index = 0;
            for(File file: files)
            {
                // Logeintrag
                Tools.log("   Grafik: "+file.getName()+" - "+file.getPath(), "image");                 
                
                // GreenfootImage aus Bilddatei erstellen und an Liste anhängen
                GreenfootImage right = Tools.loadImage(file.getPath());
                right.setFont(new Font(Integer.toString(index), 12));
                facingRight.add(right);
                
                // Bild spiegeln und speichern
                GreenfootImage left = Tools.loadImage(file.getPath());
                left.setFont(new Font(Integer.toString(index), 12));
                left.mirrorHorizontally();
                facingLeft.add(left);
                //System.out.println(left.getFont().getName());
                
                index++;
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