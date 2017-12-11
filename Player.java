import greenfoot.*;
import java.util.*;

/**
 * Write a description of class Player here.dd
 * @author (your name) @version (a version number or a date)
 */
public class Player extends Entity
{
    private Movement movement;
    private String[] controls;    
    /**
     * 
     */
    public Player(String name, GreenfootImage image, String[] controls)
    {
        super(name, image);
        this.controls=controls;
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
        /*
        if (jumpCount< 5)
        {
            jumpCount++;
        }
        */
    }
    
    private boolean leftDown = false;
    private boolean rightDown = false;
    //private int jumpCount = 0;
    private boolean jumpanable = true;
    private boolean jumpabel = true;
    private boolean directionChange=false;
    
    public void update(List<Entity> entities)
    {
        super.update(entities);
        
    }
    
    public void checkCollision(List<Entity> entities)
    {
        movement.setEntities(entities);
        
        // Spieler wird von Koopa verletzt
        if (movement.isTouchedByObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Koopa.class) && getName().equals("Mario"))
        {
            
        }
        
        //System.out.println("check Koopa");
        if (movement.isTouchingObjectBelow(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Koopa.class))
        {
            setActivity("jumping");
            movement.setY(2.5);
            jumpabel=true;            
        }    
    }
    
    public void simulate(List<Entity> entities)
    {
        // Aktuelle Welt an Movement übergeben, um Kollisionsprüfung zu erlauben
        movement.setEntities(entities);         
        jumpabel = movement.isTouchingObjectBelow(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class);
        
        // Mario steuer
        if (getName().equals("Mario"))
        {
            if(Greenfoot.isKeyDown("w"))
            {
                if (jumpabel)
                {
                    if (jumpanable)
                    {
                        /*
                        if (jumpCount==5)
                        {
                           setPosY(getPosY() + movement.jump(3)); 
                           jumpCount=0;
                        }
                        else
                        */
                        {
                            setPosY(getPosY() + movement.jump(1));
                            setActivity("jumping");
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
                if (!rightDown)
                {
                    setPosX(movement.move(180, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                    leftDown = true;
                    setOrientation("left");
                    if ((getActivity()!="jumping"))
                    {
                        setActivity("walking");
                    }
                }
                if (movement.getSpeed()>0)
                {
                    directionChange=true;
                }
            }
            else
            {
                if (movement.isTouchingLeftObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
                    {
                        movement.setSpeed(0);
                        leftDown = false;
                    }
                    else
                    {
                        if (leftDown)
                        {
                            if (movement.getSpeed()>0)
                            {
                                directionChange=true;
                            }
                            if (movement.getSpeed() < 0 )
                            {
                                setPosX(movement.move(0, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                                if ((movement.getSpeed()<1) && (movement.getSpeed()<0) && directionChange)
                                {
                                    setActivity("braking");
                                    setOrientation("right");
                                }
                            }
                            else
                            {
                                movement.setSpeed(0);
                                directionChange=false;
                                leftDown = false;
                            }
                        }
                    }
            }
            if(Greenfoot.isKeyDown("s"))
            {
                
            }
            if(Greenfoot.isKeyDown("d"))
            {
                if (!leftDown)
                {
                    setPosX(movement.move(0, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                    rightDown = true;
                    setOrientation("right");
                    if ((getActivity()!="jumping"))
                    {
                        setActivity("walking");
                    }
                }
                if (movement.getSpeed()<0)
                {
                    directionChange=true;
                }
            }
            else
            {
                if (movement.isTouchingRightObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
                {
                    movement.setSpeed(0);
                    rightDown = false;
                }
                else
                {
                    if (rightDown)
                    {
                        if (movement.getSpeed() > 0 )
                        {
                            setPosX(movement.move(180, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                            if ((movement.getSpeed()<1) && (movement.getSpeed()>0) && directionChange)
                            {
                                setActivity("braking");
                                setOrientation("left");
                            }
                        }
                        else
                        {
                            movement.setSpeed(0);
                            rightDown = false;
                            directionChange=false;
                        }
                    }
                }
            }
        }    
        if (movement.getYMove()==0 && movement.getSpeed()==0)
        {
            setActivity("standing");
            
        }
        if (movement.getYMove()<0)
        {
            setActivity("falling");
        }
        setPosY(movement.gravity(getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
        setAnimationIndex(getFrameCounter()/5);
    }
}
