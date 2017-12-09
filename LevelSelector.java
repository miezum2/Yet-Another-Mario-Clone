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
    private List<String> levels;
    private boolean show = false;
    private List<SelectionBox> boxes;
    
    public LevelSelector(String levelDir)
    {
        // Alle Level im übergebenen Ordner suchen
        File[] levelFiles = Tools.getDirContent(levelDir, "file");
        
        // Levelliste initialisieren
        levels = new ArrayList<String>();
        
        boxes = new ArrayList<SelectionBox>();
        
        for(File file: levelFiles)
        {
            // Levelpfad in Liste hinzufügen
            Tools.log("Level: "+file.getName(), "level");
            levels.add(file.getPath());
            //System.out.println(Tools.getFileContent(file.getPath()));
        }
        
        boxes.add(new SelectionBox(50, 50));
        
    }
    
    /**
     * Act - do whatever the LevelSelector wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        getWorld().addObject(new SelectionBox(50, 50), 20, 20);
        if (isShown())
        {
            
        }
    }   
    
    public List<String> getLevelList()
    {
         return levels;        
    }
    
    public List getLevelName(String path)
    {
        return levels;
    }
    
    public boolean isShown()
    {
        return show;
    }
    
    public void show()
    {
        show = true;
    }
    
    public void hide()
    {
        show = false;
    }
    
}
