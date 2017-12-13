import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.*;
import com.google.gson.*;
//https://www.greenfoot.org/doc/native_loader
//http://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.0/

/**
 * stellt Informationen zu Leveldateien im angegebenen Verzeichnis zur Verfügung
 */
public class LevelLoader extends Actor
{
    List<String> levels;
    Map<String, String> levelnames;
    
    /**
     * LevelLoader erstellen mit Levelverzeichnis
     */
    public LevelLoader(String levelDir)
    {
        // Alle Level im übergebenen Ordner suchen
        File[] levelFiles = Tools.getDirContent(levelDir, "file");
        
        // Levelliste initialisieren
        levels = new ArrayList<String>();
        levelnames = new HashMap<String, String>();
        
        for(File file: levelFiles)
        {
            // Levelpfad in Liste hinzufügen
            Tools.log("Level: "+file.getName(), "level");
            levels.add(file.getPath());
            String fileContent = Tools.getFileContent(file.getPath());
            
            // Dateiinhalt verarbeiten
            // Gson-Objekte erstellen
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            
            // Levelinfo extrahieren
            Map<String, String> levelInfo = new HashMap<String, String>();
            JsonObject levelInfoObject = parser.parse(fileContent)
                            .getAsJsonObject().getAsJsonObject("levelInfo");
            
            // Quelle: https://stackoverflow.com/questions/2779251/how-can-i-convert-json-to-a-hashmap-using-gson
            // Nutzer: Angel
            levelInfo = (Map<String, String>) 
                            gson.fromJson(levelInfoObject, levelInfo.getClass());
            // Levelname speichern
            levelnames.put(file.getPath(), levelInfo.get("name"));
        }
    }
    
    /**
     * liefert alle gefundenen Leveldateien
     */
    public List<String> getLevelList()
    {
         return levels;        
    }
    
    /**
     * liefert Levelnamen zu übergebenen Dateipfaden
     */
    public String getLevelName(String path)
    {
        String name = levelnames.get(path);
        if (name == null)
        {
            return "missing name";
        }
        else
        {
            return name;
        }
    }
}
