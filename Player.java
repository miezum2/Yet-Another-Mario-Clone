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
    private boolean leftDown = false;
    private boolean rightDown = false;
    private boolean jumpabel = true;
    private boolean directionChange=false;
    /**
     * 
     */
    public Player(String name, GreenfootImage image, String[] controls)
    {
        super(name, image);
        this.controls=controls;
        /* Konstruktor der Superklasse aufrufen*/
    }
    
    public Player(String name, String id, double x, double y, GreenfootImage image, String state, String[] controls)
    {
        super(name, id, x, y, image, state);
        movement = new Movement(0, 0.1);
        setOrientation("right");
        setActivity("standing");
        this.controls=controls;
    }
    
    /*
    public Player(Entity entity)
    {
        this(entity.getName(), entity.getId(), entity.getPosX(), entity.getPosY(), entity.getImage(), entity.getState());
    }
    */
   
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
    
    /**
     * ...
     *
     * @param entities Ein Parameter
     */
    public void simulate(List<Entity> entities)
    {
        // Aktuelle Welt an Movement übergeben, um Kollisionsprüfung zu erlauben
        movement.setEntities(entities);         
        //jumpabel = true fals er den Boden berührt
        jumpabel = movement.isTouchingObjectBelow(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class);
        
        //Bewegung nach links
        if(Greenfoot.isKeyDown(controls[1]))
        {
            //nach rechts nicht gedückt
            if (!rightDown)
            {
                //neue Position setzten 
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
            //prüfen fals Spieler gegen die Wand läuft
            if (movement.isTouchingLeftObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
                {
                    movement.setSpeed(0);
                    leftDown = false;
                }
                else
                {
                    if (leftDown)
                    {
                        //Richtungsänderung wärenden des Laufen
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
        if(Greenfoot.isKeyDown(controls[2]))
        {
            
        }
        //Bewegung nach rechts
        if(Greenfoot.isKeyDown(controls[3]))
        {
            //nach links nicht gedückt
            if (!leftDown)
            {
                //neue Position setzten 
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
            //prüfen fals Spieler gegen die Wand läuft
            if (movement.isTouchingRightObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
            {
                movement.setSpeed(0);
                rightDown = false;
            }
            else
            {
                if (rightDown)
                {
                    //Richtungsänderung wärenden des Laufen
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
        //Sprung
        if(Greenfoot.isKeyDown(controls[4]))
        {
            //prüft ob Spiele Springen darf, also am Boden angekommen ist
            if (jumpabel)
            {
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
                    jumpabel = false;
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
