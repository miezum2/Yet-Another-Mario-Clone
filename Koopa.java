import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Koopa here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Koopa extends Entity
{
    public Koopa(String name, String id, double x, double y, GreenfootImage image, String state)
    {
        super(name, id, x, y, image, state);
    }
    
    /**
     * Act - do whatever the Koopa wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
    
    public void update(List<Entity> entities)
    {
        super.update(entities);
        
        setActivity("walking");
        setOrientation("right");
        setAnimationIndex(getFrameCounter()/12);
    }   
}
