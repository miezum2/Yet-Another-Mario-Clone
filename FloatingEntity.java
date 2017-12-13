import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * stellt Entities dar, die mit der Maus über den Bildschirm gezogen werden 
 */
public class FloatingEntity extends Actor
{
    private String type;
    private String name;
    private String state;
    
    /**
     * erstellt FloatingEntity mit einem Bild
     */
    public FloatingEntity (GreenfootImage image)
    {
        setImage(image);
    }
    
    /**
     * erstellt FloatingEntity mit wichtigen Eigenschaften, die einen Entity widerspiegeln
     */
    public FloatingEntity (String type, String name, String state, GreenfootImage image, double scale)
    {
        GreenfootImage scaledImage = new GreenfootImage(image);
        scaledImage.scale((int)(image.getWidth()*scale), (int)(image.getHeight()*scale));
        setImage(scaledImage);
        this.type = type;
        this.name=name;
        this.state = state;
    }
    
    /**
     * erstellt FloatingEntity anhand eines Entities
     */
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
        else if (entity.getClass() == Special.class)
        {
            type = "special";
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
