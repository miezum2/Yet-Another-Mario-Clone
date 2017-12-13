import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;
/**
 * Write a description of class LevelMaker here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LevelMaker extends Actor
{
    private LevelLoader levelLoader;
    private String levelDir;
    private GreenfootImage image;
    /**
     * Konstruktor vom Levelmaker.
     */
    public LevelMaker (String levelDir, int scale )
    {   
        setImage("menu.png");
        image = this.getImage();
        image.scale(scale,scale);
        setImage(image);
        levelLoader = new LevelLoader(levelDir);
        this.levelDir = levelDir;
    }
    
    /**
     * Zeichnet und Beschriftet die Levelauswahl
     */
    public void createLevelSelector (List<String> name)
    {
        int height = getWorld().getHeight();
        int width = getWorld().getWidth()/2;
        String levelName;
        //neues Bild initalisiern
        GreenfootImage image = new GreenfootImage(width,(height/4)*3) ;
        image.setColor(new Color(0,0,100,50));
        image.fillRect(0,0,width,height);
        //Schrift festlegen
        Font font = image.getFont();
        font = font.deriveFont(20.0f);
        image.setFont(font);
        image.setColor(Color.BLACK);
        
        int  i =30;
        //für alle Dateinnamen werden die levelnamen gelesen und auf das Bild geschrieben
        for (String n :name)
        {
            //Name des Levels wird mit dem jeweiligen LevelDir erfragt
            levelName = levelLoader.getLevelName(n);
            image.drawString(levelName,10,i);
            i+=30;
        }
        
        //setzt das Bild in die Welt
        setLocation(width,height/2);
        setImage(image);
    }
}