import greenfoot.*;
import java.util.*;

/**
 * Write a description of class Player here.dd
 * @author (your name) @version (a version number or a date)
 */
public class Player extends Entity
{
    private Movement movement;
        
    /**
     * 
     */
    public Player(String name, GreenfootImage image, String[] controls)
    {
        super(name, image);
        /* Konstruktor der Superklasse aufrufen*/
    }
    
    public Player(String name, String id, double x, double y, GreenfootImage image)
    {
        super(name, id, x, y, image);
        movement = new Movement(0, 0.5);
    }
    
    public Player(Entity entity)
    {
        this(entity.getName(), entity.getId(), entity.getPosX(), entity.getPosY(), entity.getImage());
    }
    
    public Player()
    {
        
    }

    /**
     * Act - do whatever the Player wants to do. This method is called whenever the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        
    }
    
    public void update(List<Entity> entities)
    {
        super.update(entities);
        
        int floor = 0;
        int right = 1000000;
        int left = 0;
        for (Entity entity : entities)
        {
            if (entity.getPosX() + entity.getWidthUnits() > getPosX() && getPosX() + getWidthUnits() > entity.getPosX() && !(entity.getClass() == Player.class))
            {
                if (entity.getPosY()+entity.getHeightUnits() > floor)
                {
                    floor = (int)entity.getPosY()+entity.getHeightUnits();
                }
            }
            
            if (entity.getPosY() + entity.getHeightUnits() > getPosY() && getPosY() + getHeightUnits() > entity.getPosY() && !(entity.getClass() == Player.class))
            {
                if (entity.getPosX()+entity.getWidthUnits() > left)
                {
                    left = (int)entity.getPosX()+entity.getWidthUnits();
                }
            }
            
        }
        
        System.out.println(floor);
        
        // Mario steuer
        if (getName().equals("Mario"))
        {
            if(Greenfoot.isKeyDown("w"))
            {
                
            }
            if(Greenfoot.isKeyDown("a"))
            {
                int newX = (int)getPosX() + (int)movement.move(180);
                if (newX < left)
                {
                    setPosX(left);
                }
                else
                {
                    setPosX(newX);
                }
            }
            if(Greenfoot.isKeyDown("s"))
            {
                
            }
            if(Greenfoot.isKeyDown("d"))
            {
                setPosX(getPosX() + movement.move(0));
            }
        }
        
        int newY = (int)getPosY() + (int)movement.gravity();
        if (newY < floor)
        {
            setPosY(floor);
        }
        else
        {
            setPosY(newY);
        }
        
        
        
        // Daten ermitteln       
        setState("small");
        setActivity("standing");
        setOrientation("right");
        setAnimationIndex(getFrameCounter()/5);
        
        //System.out.println("Height: "+getHeightUnits()+" Width: "+getWidthUnits());
    }
}
