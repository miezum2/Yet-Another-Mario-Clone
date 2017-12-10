import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Block here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Block extends Entity
{
    public Block(String name, String id, double x, double y, GreenfootImage image, String state)
    {
        super(name, id, x, y, image, state);
        
        // Mystery_Block
        if (getName().equals("Mystery_Block"))
        {
            setState("yellow");
            setActivity("spinning");
            setOrientation("right");
        }
    }
    
    /**
     * Act - do whatever the Block wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        
    }    
    
    public void update(List<Entity> entities)
    {
        super.update(entities);
        
        // Block je nach Typ setzen       
        
        // Ground
        if (getName().equals("Ground"))
        {        
            boolean aboveIsGround = false;
            boolean leftIsGround = false;
            boolean rightIsGround = false;
            boolean upperLeftIsGround = false;
            boolean upperRightIsGround = false;
            
            // nach Ground-Blöcken in der Umgebung suchen
            for (Entity entity : entities)
            {
                if (entity.getName().equals("Ground"))
                {
                    // nach oben prüfen
                    if (getPosY() == entity.getPosY() - 16)
                    {
                        if (getPosX() == entity.getPosX())
                        {
                            aboveIsGround = true;
                        }
                        
                        // nach oben links prüfen
                        if (getPosX() == entity.getPosX() + 16 || getPosX() <= 16)
                        {
                            upperLeftIsGround = true;
                        }
                        
                        // nach oben rechts prüfen
                        if (getPosX() == entity.getPosX() - 16)
                        {
                            upperRightIsGround = true;
                        }
                    }
                    
                    // nach links prüfen
                    if ((getPosX() == entity.getPosX() + 16 && getPosY() == entity.getPosY()) || getPosX() <= 16)
                    {
                        leftIsGround = true;
                    }
                    
                    // nach rechts prüfen
                    if (getPosX() == entity.getPosX() - 16 && getPosY() == entity.getPosY())
                    {
                        rightIsGround = true;
                    }                         
                }
            }  
            
            // Aussehen anhand von umgebenden Blöcken festlegen
            if (aboveIsGround)
            {
                if (!leftIsGround && !rightIsGround)
                {
                    setActivity("dirt_single");
                }
                else if (!leftIsGround)
                {
                    setActivity("grass_left_edge");
                }
                else if (!rightIsGround)
                {
                    setActivity("grass_right_edge");
                }
                else if (!upperLeftIsGround && !upperRightIsGround)
                {
                    setActivity("dirt_single_corner");
                }                
                else if (!upperLeftIsGround)
                {
                    setActivity("dirt_left_corner");
                }
                else if (!upperRightIsGround)
                {
                    setActivity("dirt_right_corner");
                }
                else
                {
                    setActivity("dirt");
                }
            }
            else
            {
                if (!leftIsGround && !rightIsGround)
                {
                    setActivity("grass_single_corner");
                }
                else if (!leftIsGround)
                {
                    setActivity("grass_left_corner");
                }
                else if (!rightIsGround)
                {
                    setActivity("grass_right_corner");
                }
                else
                {
                    setActivity("grass");
                }
            }
            
            setState("grass");
            setOrientation("right");  
            setAnimationIndex(0);
        }
        
        //System.out.println("Eigenschaften von "+getName()+" gesetzt");       
    }
    
    public void simulate(List<Entity> entities)
    {
        // Mystery_Block
        if (getName().equals("Mystery_Block"))
        {
            setAnimationIndex(getFrameCounter()/8);
        }
    }
}
