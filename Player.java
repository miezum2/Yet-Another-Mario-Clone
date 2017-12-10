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
    
    public Player(String name, String id, double x, double y, GreenfootImage image, String state)
    {
        super(name, id, x, y, image, state);
        movement = new Movement(0, 0.1);
        setOrientation("right");
        setActivity("standing");
    }
    
    public Player(Entity entity)
    {
        this(entity.getName(), entity.getId(), entity.getPosX(), entity.getPosY(), entity.getImage(), entity.getState());
    }
    
    public Player()
    {
        
    }

    /**
     * Act - do whatever the Player wants to do. This method is called whenever the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (jumpcount< 3)
        {
            jumpcount++;
        }
    }
    
    private boolean pressda = false;
    private boolean pressdd = false;
    private int jumpcount = 0;
    private boolean jumpanable = true;
    private boolean jumpabel = true;
    
    public void update(List<Entity> entities)
    {
        super.update(entities);
        
    }
    
    public void simulate(List<Entity> entities)
    {
        // Aktuelle Welt an Movement übergeben, um Kollisionsprüfung zu erlauben
        movement.setEntities(entities);            
        
        // Mario steuer
        if (getName().equals("Mario"))
        {
            if(Greenfoot.isKeyDown("w"))
            {
                if (jumpabel)
                {
                    if (jumpanable)
                    {
                        if (jumpcount==3)
                        {
                           setPosY(getPosY() + movement.jump(3)); 
                        }
                        else
                        {
                            setPosY(getPosY() + movement.jump(1));
                        }
                        jumpanable=false;
                        jumpabel = false;
                    }
                    else
                    {
                        if (movement.getYMove() <= 0)
                        {
                            jumpanable = true; 
                        }
                    }
                }
                
            }
            if(Greenfoot.isKeyDown("a"))
            {
                if (!pressdd)
                {
                    setPosX(movement.move(180, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                    pressda = true;
                    setOrientation("left");
                }
            }
            else
            {
                if (movement.isTouchingLeftObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
                    {
                        movement.setSpeed(0);
                        pressda = false;
                    }
                    else
                    {
                        if (pressda)
                        {
                            if (movement.getSpeed() < 0 )
                            {
                                setPosX(movement.move(0, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                            }
                            else
                            {
                                movement.setSpeed(0);
                                pressda = false;
                            }
                        }
                    }
            }
            if(Greenfoot.isKeyDown("s"))
            {
                
            }
            if(Greenfoot.isKeyDown("d"))
            {
                if (!pressda)
                {
                    setPosX(movement.move(0, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                    pressdd = true;
                    setOrientation("right");
                }
            }
            else
            {
                if (pressdd)
                {
                    if (movement.isTouchingRightObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
                    {
                        movement.setSpeed(0);
                        pressdd = false;
                    }
                    else
                    {
                        if (movement.getSpeed() > 0 )
                        {
                            setPosX(movement.move(180, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                        }
                        else
                        {
                            movement.setSpeed(0);
                            pressdd = false;
                        }
                    }
                }
            }
        }        
        
        // Spieler wird von Koopa verletzt
        if (movement.isTouchedByObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Koopa.class) && getName().equals("Mario"))
        {
            
        }
        
        if (movement.isTouchingObjectBelow(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Koopa.class))
        {
            System.out.println("test");
            setPosY(getPosY() +movement.jump(2));   
        }
        
        setPosY(movement.gravity(getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
        
        jumpabel = movement.isTouchingObjectBelow(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class);
        
                        
        setAnimationIndex(getFrameCounter()/5);
    }
}
