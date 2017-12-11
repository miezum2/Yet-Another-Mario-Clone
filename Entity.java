import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Entity here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Entity extends Actor
{
    private String name;
    private String id;
    private double posX;
    private double posY;
    private double cameraX;
    private double cameraY;
    private String state;
    private String activity;
    private String orientation;
    private int frameCounter;
    private int animationIndex;
    private GreenfootImage image;
    private boolean enabled;
    private int heightUnits;
    private int widthUnits;
    private boolean removed = false;
    
    public Entity()
    {
        this("0", null);
    }
    
    public Entity(String id, GreenfootImage image)
    {
        this.id = id;        
        this.image = image;
        setImage(image);
    }
    
    public Entity(String name, String id, double posX, double posY, GreenfootImage image, String state)
    {
        this.name = name;
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.image = image;
        this.state = state;
        setImage(image);
    }
    
    public Entity(Entity entity)
    {
        this(entity.getName(), entity.getId(), entity.getPosX(), entity.getPosY(), entity.getImage(), entity.getState());
    }
    
    /**
     * Act - do whatever the Entity wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
    
    public void update(List<Entity> entities)
    {
        frameCounter++;        
    }
    
    public void checkCollision(List<Entity> entities)
    {
    
    }
    
    public void simulate(List<Entity> entities)
    {
        
    }
    
    public void calculateExactPos()
    {
        setCameraX(getCameraX() + getImage().getWidth() / 2);
        setCameraY(getCameraY() - getImage().getHeight() / 2);        
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getId()
    {
        return id;
    }
    
    public double getPosX()
    {
        return posX;
    }    
    
    public void setPosX(double posX)
    {
        this.posX = posX;
    }
    
    public double getPosY()
    {
        return posY;
    } 
    
    public void setPosY(double posY)
    {
        this.posY = posY;
    }
    
    public double getCameraX()
    {
        return cameraX;
    }    
    
    public void setCameraX(double cameraX)
    {
        this.cameraX = cameraX;
    }
    
    public double getCameraY()
    {
        return cameraY;
    } 
    
    public void setCameraY(double cameraY)
    {
        this.cameraY = cameraY;
    }
        
    public String getState()
    {
        return state;
    }
    
    public void setState(String state)
    {
        this.state = state;
    }
    
    public String getActivity()
    {
        return activity;
    }
    
    public void setActivity(String activity)
    {
        this.activity = activity;
    }
    
    public String getOrientation()
    {
        return orientation;
    }
    
    public void setOrientation(String orientation)
    {
        this.orientation = orientation;
    }
    
    public int getFrameCounter()
    {
        return frameCounter;
    }
    
    public int getAnimationIndex()
    {
        return animationIndex;
    }
    
    public void setAnimationIndex(int animationIndex)
    {
        this.animationIndex = animationIndex;
    }
    
    public void enable()
    {
        enabled = true;
    }
    
    public void disable()
    {
        enabled = false;
    }
    
    public boolean isEnabled()
    {
        return enabled;
    }
    
    public int getHeightUnits()
    {
        return heightUnits;
    }
    
    public void setHeightUnits(int heightUnits)
    {
        this.heightUnits = heightUnits;
    }
    
    public int getWidthUnits()
    {
        return widthUnits;
    }
    
    public void setWidthUnits(int widthUnits)
    {
        this.widthUnits = widthUnits;
    }
    
    public void remove()
    {
        removed = true;
    }
    
    public boolean isRemoved()
    {
        return removed;
    }
}
