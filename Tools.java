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
 * Write a description of class Tools here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tools  
{
    private static final String missingImage = "images/missingImage.png";
    
    public static File[] getDirContent(String path)
    {
        // Alle Ordner und Dateien im aktuellen Verzeichnis auflisten
        File[] objects = new File(path).listFiles();
        if (objects == null)
        {
            objects = new File[0];
        }
        return objects;                
    }
    
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
    
    // Quelle: https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java
    // Nutzer: Knubo
    public static String getFileContent(String path) {
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
    
    /*
     * Vereinfachte Methode zur Abfrage der Existenz einer Datei
     */
    public static boolean fileExists(String path)
    {
        return new File(path).isFile();
    }
    
    /*
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
    
    public static void log(String content, String type)
    {
        List <String> types = Arrays.asList("image_", "activity_", "state_", "entity_",
                                            "level_");
        if (types.contains(type))
        {
            System.out.println(content);
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
    
    public static void deleteFile(String path)
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
