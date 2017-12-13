import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Abstrakte Oberklasse für alle im Spiel verwendeten Objekte
 */
public abstract class Entity extends Actor
{
    private String name;
    private String id;
    private double posX;
    private double posY;
    private String data;
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
    private boolean removed;
    private String currentCutscene;
    private int cutsceneFrameCounter;
        
    /*public Entity(String id, GreenfootImage image)
    {
        this.id = id;        
        this.image = image;
        setImage(image);
    } */
    
    /**
     * Erstellung eines neuen Entity mit den wichtigsten Eigenschaften
     */
    public Entity(String name, String id, double posX, double posY, GreenfootImage image, String state, String activity)
    {
        this.name = name;
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.image = image;
        this.state = state;
        this.activity = activity;
        this.orientation = "right";
        this.animationIndex = 0;
        this.data = "";
        setImage(image);
        removed = false;
        currentCutscene = "";
        cutsceneFrameCounter = 0;
    }
        
    /**
     * ungenutzt
     */
    public void act() 
    {
        // Add your action code here.
    }    
    
    /**
     * ermittelt das aktuelle Aussehen des Entity anhand der Cutscene und der in der Welt vorhandenen Blöcke
     * 
     */
    public void update(List<Entity> entities, String currentCutscene, int cutsceneFrameCounter)
    {
        frameCounter++;  
        this.cutsceneFrameCounter++;
    }
    
    public void update()
    {
    
    }
    
    /**
     * prüft Kollision mit anderen Entities
     *
     * @param entities Alle in der Welt enthaltenen Entities
     */
    public void checkCollision(List<Entity> entities)
    {
    
    }
    
    /**
     * bewegt den Entity anhand der momentan wirkenden Kräfte
     *
     * @param entities Alle in der Welt enthaltenen Entities
     */
    public void simulate(List<Entity> entities)
    {
        
    }
    
    /**
     * da der Nullpunkt eines Objektes normalerweise in der Mitte liegt, wird er hier nach unten links verschoben
     */
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
    
    public String getData()
    {    
        return data;
    }
    
    public void setData(String data)
    {
        this.data = data;
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
    
    /**
     * Entity aktivieren (wird simuliert und eingezeichnet)
     */
    public void enable()
    {
        enabled = true;
    }
    
    /**
     * Entity deaktivieren (wird weder simuliert noch eingezeichnet)
     */
    public void disable()
    {
        enabled = false;
    }
    
    /**
     * prüft, ob der Entity momentan deaktiviert ist
     */
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
    
    /**
     * markiert Entity als gelöscht, wodurch er im nächsten Frame gelöscht wird
     */
    public void remove()
    {
        removed = true;
    }
    
    /**
     * prüft, ob der Entity als gelöscht markiert ist
     */
    public boolean isRemoved()
    {
        return removed;
    }
    
    public String getCurrentCutscene()
    {
        return currentCutscene;
    }
    
    public void setCurrentCutscene(String currentCutscene)
    {
        this.currentCutscene = currentCutscene;
    }
    
    public int getCutsceneFrameCounter()
    {
        return cutsceneFrameCounter;
    }
    
    public void setCutsceneFrameCounter(int cutsceneFrameCounter)
    {
        this.cutsceneFrameCounter = cutsceneFrameCounter;
    }    
        
    public boolean isDead()
    {
        return currentCutscene.equals("dead");
    }
}
