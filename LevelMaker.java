import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;
/**
 * Write a description of class LevelMaker here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LevelMaker extends Selector
{
    
    public LevelMaker ()
    {   
        setImage("missingImage.png");
    }
    
    public void createLevelSelector (List<String> name)
    {
        int height = getWorld().getHeight();
        int width = getWorld().getWidth()/2;
        
        GreenfootImage image = new GreenfootImage(width,(height/4)*3) ;
        //image.setColor(Color.BLACK);
        image.setColor(new Color(0,0,80,25));
        image.fillRect(0,0,width,height);
        //Schrift
        Font font = image.getFont();
        font = font.deriveFont(20.0f);
        image.setFont(font);
        image.setColor(Color.BLACK);
        
        int  i =30;
        for (String n :name)
        {
            image.drawString(n,10,i);
            i+=30;
        }
        
        setLocation(width,height/2);
        setImage(image);
    }
}