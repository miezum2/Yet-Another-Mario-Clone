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
    private int turningCounter;
    
    public Koopa(String name, String id, double x, double y, GreenfootImage image, String state)
    {
        super(name, id, x, y, image, state);
        movement = new Movement(-0.5, 0);
        setActivity("walking");
        setOrientation("left");
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
        
        if (getActivity().equals("turning"))
        {
            turningCounter++;
            if (turningCounter == 4)
            {
                if (getOrientation().equals("left"))
                {
                    setOrientation("right");
                }
                else
                {
                    setOrientation("left");
                }
            }
            if (turningCounter == 8)
            {
                setActivity("walking");
            }
        }
            
    }   
    
    public void checkCollision(List<Entity> entities)
    {
        movement.setEntities(entities);
        
        // Richtungsänderung
        if (movement.isTouchingLeftObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class)
            || movement.isTouchingLeftObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Koopa.class))
        {
            setActivity("turning");
            setOrientation("left");
            turningCounter = 0;
            movement.setSpeed(0.5);
        }
        
        if (movement.isTouchingRightObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class)
            || movement.isTouchingRightObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Koopa.class))
        {
            setActivity("turning");
            setOrientation("right");
            turningCounter = 0;
            movement.setSpeed(-0.5);
        }     
        
        // Spieler springt auf Koopa
        if (movement.isTouchingObjectAbove(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Player.class))
        {
            //remove();  
            System.out.println("removed");
        }
    }
    
    public void simulate(List<Entity> entities)
    {
        movement.setEntities(entities);   
        
        setPosX(movement.move(180, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
        setPosY(movement.gravity(getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
        
                
        setAnimationIndex(getFrameCounter()/8);
        
        
        
        
    }
}
