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
    private int levelNumber;
    private GreenfootImage image;
    public Select (String name,int levelNumber, String imag)
    {
        this.name = name;
        this.setImage(imag);
        image = this.getImage();
        image.scale(28,28);
        setImage(image);
        this.levelNumber=levelNumber;
    }
    
    public String getName()
    {
        return name;
    }
    public int getLevelNumber()
    {
        return levelNumber;
    }
}
