import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class FloatingEntity here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FloatingEntity extends Actor
{
    private String type;
    private String name;
    private String state;
    
    public FloatingEntity (GreenfootImage image)
    {
        setImage(image);
    }
    
    public FloatingEntity (String type, String name, String state, GreenfootImage image, double scale)
    {
        GreenfootImage scaledImage = new GreenfootImage(image);
        scaledImage.scale((int)(image.getWidth()*scale), (int)(image.getHeight()*scale));
        setImage(scaledImage);
        this.type = type;
        this.name=name;
        this.state = state;
    }
    
    public FloatingEntity(Entity entity)
    {
        setImage(entity.getImage());
        if (entity.getClass() == Block.class)
        {
            type = "block";
        }
        else if (entity.getClass() == Player.class)
        {
            type = "player";
        }
        else if (entity.getClass() == Koopa.class)
        {
            type = "koopa";
        }
        
        name = entity.getName();
        state = entity.getState();
    }
    
    public String getType()
    {
        return type;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getState()
    {
        return state;
    }
}
