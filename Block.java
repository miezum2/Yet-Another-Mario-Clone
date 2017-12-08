import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Block here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Block extends Entity
{
    public Block(String name, String id, double x, double y, GreenfootImage image)
    {
        super(name, id, x, y, image);
    }
    
    /**
     * Act - do whatever the Block wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        
    }    
    
    public void update()
    {
        setState("grass");
        setActivity("grass");
        setOrientation("right");
        setAnimationIndex(0);
        
        System.out.println("Eigenschaften von "+getName()+" gesetzt");       
    }
}
