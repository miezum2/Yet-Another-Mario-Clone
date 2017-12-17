import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Speichert alle Koopa-Typen
 */
public class Koopa extends Entity
{
    private Movement movement;
    private int turningCounter;
    
    /**
     * neuen Koopa mit den wichtigsten Eigenschaften erstellen
     */
    public Koopa(String name, String id, double x, double y, GreenfootImage image, String state, String activity)
    {
        super(name, id, x, y, image, state, activity);
        movement = new Movement(-0.5, 0);
        setActivity("walking");
        setOrientation("left");
    }
    
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
            
                Greenfoot.playSound("sounds/smw_stomp.wav");              
                remove();
        }
    }
    
    public void simulate(List<Entity> entities)
    {
        movement.setEntities(entities);   
        
        setPosX(movement.move(180, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
        setPosY(movement.gravity(getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
        
        if (getState().equals("red"))
        {
            setAnimationIndex(getFrameCounter()/8);
        }
        else
        {
            setAnimationIndex(getFrameCounter()/4);
        }
        
        
        
    }
}
