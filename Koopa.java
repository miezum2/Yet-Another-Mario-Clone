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
    private Movement movement;
    
    public Koopa(String name, String id, double x, double y, GreenfootImage image, String state)
    {
        super(name, id, x, y, image, state);
        movement = new Movement(-0.5, 0);
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
        
        int floor = 0;
        int right = 1000000;
        int left = 0;
        
        for (Entity entity : entities)
        {
            if (entity.getPosY() + entity.getHeightUnits() <= getPosY() && entity.getPosX() + entity.getWidthUnits() > getPosX() && getPosX() + getWidthUnits() > entity.getPosX() && !(entity.getClass() == Player.class) && !(entity.getClass() == Koopa.class))
            {
                if (entity.getPosY()+entity.getHeightUnits() > floor)
                {
                    floor = (int)entity.getPosY()+entity.getHeightUnits();
                }
            }
            
            if (entity.getPosY() + entity.getHeightUnits() > getPosY() && getPosY() + getHeightUnits() > entity.getPosY() && !(entity.getClass() == Player.class) && !(entity.getClass() == Koopa.class))
            {
                if (entity.getPosX()+entity.getWidthUnits() > left)
                {
                    left = (int)entity.getPosX()+entity.getWidthUnits();
                }
            }
            
        }
        
        setPosX(getPosX()+movement.move(180));
        
        int newY = (int)getPosY() + (int)movement.gravity();
        if (newY <= floor)
        {
            setPosY(floor);
        }
        else
        {
            setPosY(newY);
        }
        
        setActivity("walking");
        setOrientation("left");
        setAnimationIndex(getFrameCounter()/12);
    }   
}
