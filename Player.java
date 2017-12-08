import greenfoot.*;
import java.util.*;

/**
 * Write a description of class Player here.dd
 * @author (your name) @version (a version number or a date)
 */
public class Player extends Entity
{

    /* (World, Actor, GreenfootImage, Greenfoot and MouseInfo)*/

    /* Player initialisieren*/

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
        
        // Hier Gravitation berechnen und Keypresses abfangen
        
        // Daten ermitteln       
        setState("small");
        setActivity("standing");
        setOrientation("right");
        setAnimationIndex(getFrameCounter()/5);
        
        //System.out.println("Eigenschaften von "+getName()+" gesetzt");
    }
}
