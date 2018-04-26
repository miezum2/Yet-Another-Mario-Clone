import greenfoot.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.nio.file.*;
import java.nio.charset.*;

/**
 * Fasst einige nützliche Methoden zusammen
 */
public class Tools  
{
    private static final String missingImage = "images/missingImage.png";
    private static GreenfootSound bgm;
    private static GreenfootSound interrupt;
    private static int bgmVolume;
    
    /**
     * alle Ordner und Dateien im aktuellen Verzeichnis auflisten
     */
    public static File[] getDirContent(String path)
    {
        File[] objects = new File(path).listFiles();
        if (objects == null)
        {
            objects = new File[0];
        }
        Arrays.sort(objects);
        return objects;                
    }
    
    /**
     * alle Ordner oder Dateien im aktuellen Verzeichnis auflisten
     * @param type "dir" für Verzeichnisse, "file" für Dateien
     */
    public static File[] getDirContent(String path, String type)
    {
        //Alle Objekte aus Ordner abrufen
        File[] objects = getDirContent(path);
        
        //Jedes Objekt betrachten und in entsprechende Listen kopieren
        List<File> directoryList = new ArrayList<File>();
        List<File> fileList = new ArrayList<File>();
        for(File object: objects)
        {  
            if(object.isDirectory())
            {
                directoryList.add(object);
            }
            else
            {
                fileList.add(object);
            }
        }
    
        //List wieder in Array umwandeln und zurückgeben
        //Listenumwandlung orientiert an https://stackoverflow.com/questions/5374311/convert-arrayliststring-to-string-array
        //Nutzer: Stephen C
        if(type.equals("directory") || type.equals("dir"))
        {
            return directoryList.toArray(new File[0]);
        }
        else if(type.equals("file"))
        {
            return fileList.toArray(new File[0]);
        }
        else
        {
            return objects;
        }
    }  
    
    /**
     * lädt Inhalt einer Datei und gibt ihn als String zurück
     */
    public static String getFileContent(String path) {
        // Quelle: https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java
        // Nutzer: Knubo
        if (!fileExists(path))
        {
            return "";
        }
        
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
        
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        return "";
    }
    
    /**
     * Vereinfachte Methode zur Abfrage der Existenz einer Datei
     */
    public static boolean fileExists(String path)
    {
        return new File(path).isFile();
    }
    
    /**
     * Liefert Bilddatei an gegebenem Speicherort als GreenfootImage.
     * Wenn Datei nicht existiert, wird missingImage.png geliefert.
     */
    public static GreenfootImage loadImage(String path)
    {
        if(fileExists(path))
        {
            return new GreenfootImage(path);
        }
        else
        {
            return new GreenfootImage(missingImage);
        }
    }
    
    /**
     * entfernt alle Buchstaben und Sonderzeichen aus einem String und konvertiert die verbleibenden Ziffern zu einem Integer
     *
     * @param content zu durchsuchender String
     * @return liefert positiven Integer oder 0. -1 wenn keine Ziffern enthalten
     */
    public static int parseToInt(String content)
    {
        String value = "";
        for (int i = 0; i < content.length(); i++)
        {
            if (Character.isDigit(content.charAt(i)))
            {
                value = value + content.charAt(i);
            }
        }
        
        if (value.equals(""))
        {
            return -1;
        }
        else
        {
            return Integer.parseInt(value);
        }
        
    }
    
    /**
     * schreibt Text in eine Datei und erstellt gegebenenfalls eine neue
     */
    public static void writeFile(String path, String content)
    {
        // neue Datei erstellen
        List<String> lines = Arrays.asList(content);
        Path file = Paths.get(path);
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * löscht Datei
     */
    public static void deleteFile(String path)
    {
        if (fileExists(path))
        {
            Path file = Paths.get(path);
            try
            {
                Files.delete(file);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }        
    }
    
    /**
     * fügt eine neue Zeile zum Log hinzu, wenn übergebener type im Array enthalten
     */
    public static void log(String content, String type)
    {
        List <String> types = Arrays.asList("image_", "activity_", "state_", "entity_",
                                            "level_");
        if (types.contains(type))
        {
            System.out.println(content);
        }
    }   
    
    public static void playBgm(String path, int volume)
    {
        if (bgm != null)
        {
            bgm.stop();
        }        
        bgm = new GreenfootSound("sounds/"+path);
        bgm.setVolume(volume);
        //bgm.playLoop();
        bgmVolume = volume;
        if (interrupt != null)
        {
            interrupt.stop();       
        }
    }
    
    public static void playSound(String path, int volume)
    {
        GreenfootSound sound = new GreenfootSound("sounds/"+path);
        sound.setVolume(volume);
        //sound.play();    
    }
    
    public static void playInterrupt(String path, int volume)
    {
        bgm.setVolume(0);
        if (interrupt != null)
        {
            interrupt.stop();       
        }
        interrupt = new GreenfootSound("sounds/"+path);
        interrupt.setVolume(volume);
        //interrupt.play();  
    }
    
    public static void checkSound()
    {
        if (bgm != null && interrupt != null)
        {
            if (!interrupt.isPlaying())
            {
                bgm.setVolume(bgmVolume);
            }
        }
    }
}
