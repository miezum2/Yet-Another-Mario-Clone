import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Buttons auf der Benutzeroberfläche
 */
public class Select extends Actor
{
    private String name;
    private int levelNumber;
    private GreenfootImage image;
    
    /**
     * erstellt neuen Button mit wichtigen Eigenschaften
     */
    public Select (String name,int levelNumber, String imag, int scale)
    {
        this.name = name;
        this.setImage(imag);
        image = this.getImage();
        image.scale(scale,scale);
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
