import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
        
    }
    
    /**
     * Act - do whatever the LevelMaker wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
    
    public void createLevelSelector ()
    {
        int height = getWorld().getHeight();
        int width = getWorld().getWidth()/2;
        
        GreenfootImage image = new GreenfootImage(width,(height/4)*3) ;
        //image.setColor(Color.BLACK);
        image.setColor(new Color(0,0,80,25));
        image.fillRect(0,0,width,height);
        setImage(image);
        setLocation(width,height/2);
        
        
    }
}