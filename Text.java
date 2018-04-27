import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Werte auf dem Bildschirm darstellen zum debuggen
 */
public class Text extends Actor
{
    private String name;
    
    /**
     * Act - do whatever the Text wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
    
    public String getName()
    {
        return name;
    }
    
    public void setText(String text)
    {
        int imageWidth= (text.length() + 2) * 10;
        GreenfootImage image = new GreenfootImage(imageWidth, 16);
        image.clear();
        image.setColor(Color.WHITE);
        image.drawString(text, 1, 12);
        setImage(image);
    }
}
