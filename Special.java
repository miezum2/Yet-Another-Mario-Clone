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
        movement = new Movement(0, 0);
    }   
    
    public void act() 
    {
        // Add your action code here.
    }  
    
    public void update(List<Entity> entities, String currentCutscene, int cutsceneFrameCounter)
    {
        super.update(entities, currentCutscene, cutsceneFrameCounter);
        
        if (currentCutscene.equals("victory") && getActivity().equals("bar"))
        {
            disable();
        }
    }
    
    public void checkCollision(List<Entity> entities)
    {
        movement.setEntities(entities);
        
        // Spieler löst Zielflagge aus
        if (movement.isTouchedByObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Player.class))
        {                    
            setCurrentCutscene("victory");
            setCutsceneFrameCounter(0);
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
    }
}
