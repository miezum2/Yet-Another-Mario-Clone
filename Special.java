import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Special here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Special extends Entity
{
    int initPosY;
    boolean movingUpwards;
    Movement movement;
    
    public Special(String name, String id, double x, double y, GreenfootImage image, String state, String activity)
    {
        super(name, id, x, y, image, state, activity);
        //movement = new Movement(0, 0);
        initPosY = (int)getPosY();
        movingUpwards = true;
        if (name.equals("Mushroom"))
        {
            movement = new Movement(-0.8, 0);
        }
        else
        {
            movement = new Movement(0, 0);
            if (name.equals("Coin"))
            {
                setActivity("spinning");
            }
        }
    }   
    
    public void act() 
    {
        // Add your action code here.
    }  
    
    public void update(List<Entity> entities)
    {
        super.update(entities);
        
        if (getGlobalCutscene().equals("victory") && getActivity().equals("bar"))
        {
            disable();
        }
    }
    
    public void checkCollision(List<Entity> entities)
    {
        movement.setEntities(entities);
        
        if (getName().equals("Flagpole"))
        {
            // Spieler löst Zielflagge aus
            if (movement.isIntersecting(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), this, Player.class) && !getGlobalCutscene().equals("victory"))
            {                 
                Tools.playInterrupt("smw_course_clear.wav", 95);
                setGlobalCutscene("victory");
                setCutsceneFrameCounter(0);
            }
        }
        
        if (getName().equals("Mushroom"))
        {
            // Spieler sammelt Pilz ein
            if (movement.isIntersecting(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), this, Player.class))
            {                 
                remove();
            }
        }
        
        if (getName().equals("Coin"))
        {
            // Spieler sammelt Coin ein
            if (movement.isIntersecting(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), this, Player.class))
            {   
                Greenfoot.playSound("sounds/smw_coin.wav");
                remove();
            }
        }
    }
    
    public void simulate(List<Entity> entities)
    {
        if (getActivity().equals("bar"))
        {
            if (movingUpwards)
            {
                setPosY(getPosY()+1);
                if (getPosY() > initPosY+6*16)
                {
                    movingUpwards = false;
                }
            }
            else
            {
                setPosY(getPosY()-1);
                if (getPosY() < initPosY)
                {
                    movingUpwards = true;
                }
            }            
        }
        
        if (getName().equals("Mushroom"))
        {
            setPosY(movement.gravity(getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
            setPosX(movement.move(180, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
        }
        
        setAnimationIndex(globalFrameCounter/8);
        
    }
}
