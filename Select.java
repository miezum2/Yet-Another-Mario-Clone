import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Select here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Select extends Actor
{
    private String name;
    
    public Select (String name, String image)
    {
        this.name = name;
        this.setImage(image);
    }
    
    public String getName()
    {
        return name;
    }
    
}
