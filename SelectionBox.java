import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SelectionBox here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SelectionBox extends Actor
{
    GreenfootImage image;
    
    public SelectionBox(int width, int height)
    {
        image = new GreenfootImage(width, height);
        image.setColor(new Color(30, 30, 30, 128));
        image.fill();
        setImage(image);
    }
    
    /**
     * Act - do whatever the SelectionBox wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
}
