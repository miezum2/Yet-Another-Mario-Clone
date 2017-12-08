import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.*;
//https://www.greenfoot.org/doc/native_loader
//http://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.0/

/**
 * Write a description of class LevelSelector here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LevelSelector extends Selector
{
    List<String> levels;
    
    public LevelSelector(String levelDir)
    {
        // Alle Level im übergebenen Ordner suchen
        File[] levelFiles = Tools.getDirContent(levelDir, "file");
        
        // Levelliste initialisieren
        levels = new ArrayList<String>();
        
        for(File file: levelFiles)
        {
            // Levelpfad in Liste hinzufügen
            Tools.log("Level: "+file.getName(), "level");
            levels.add(file.getPath());
            //System.out.println(Tools.getFileContent(file.getPath()));
        }
    }
    
    /**
     * Act - do whatever the LevelSelector wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
    
    public List<String> getLevelList()
    {
         return levels;        
    }
    
    public String getLevelName(String path)
    {
        return "Todo: Name auslesen";
    }
}
