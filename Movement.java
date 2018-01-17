import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Berechnung der Bewegung von Objekten und Gravitation
 */
public class Movement
{
    // Wer auch immer das hier liest, möge bitte die fast identischen Methoden zur Kollisionsprüfung entschuldigen
    // ...es war nicht mehr viel Zeit übrig
    
    //Gibt die Bewegung in Y-Richtung an
    private double Y;
    //Gibt die Beschleunigung der Entity an
    private double acceleration;
    //Sprung
    private double jumpcount;
    //Bewegung in X-Richtung
    private double speed;
    
    private double maxSpeed = 1.5;
    
    private List<Entity> entities;
        
    /**
     * neue Bewegung erstellen mit Initialgeschwindigkeit und Beschleunigung in X-Richtung
     */
    public Movement(double speed, double accel)
    {    
        this.speed = speed;
        this.acceleration = accel;
    }   
    
    /**
     * Entities zur Kollisionsprüfung entgegennehmen
     */
    public void setEntities(List<Entity> entities)
    {
        this.entities = entities;
    }

    /**
     * Bewegt sich in die aktuelle Bewegungsrichtung.
     * Direction gibt die laufrichtung an, dabei ist 0 nach rechts und 180 nach links.
     *
     * @param direction Ein Parameter
     * @param posX Ein Parameter
     * @param posY Ein Parameter
     * @param widthUnits Ein Parameter
     * @param heightUnits Ein Parameter
     * @return Der Rückgabewert
     */
    public double move(int direction, double posX, double posY, double widthUnits, double heightUnits) 
    {
        
        //Bewegung nach rechst
        //Beschleunigen bis zur Höchstgeschwindigkeit
        if (direction == 0)
        {
            double newSpeed = speed + acceleration;
            if (newSpeed<maxSpeed)
            { 
                speed = newSpeed;
            }
            else
            {
                speed = speed - acceleration;
            }
        }
        
        
        //bewegung nach links
        //Beschleunigen bis zur Höchstgeschwindigkeit
        if (direction == 180)
        {
            double newSpeed = speed - acceleration;
            if (newSpeed>-maxSpeed && direction==180)
            {
                speed = newSpeed;
            }
            else
            {
                speed = speed + acceleration;
            } 
        }
        
        double newX = posX + speed;
        if (newX < 0)
        {
            newX = 0;
        }
        double leftWall = getLeftObject(posX, posY, widthUnits, heightUnits, Block.class);
        double rightWall = getRightObject(posX, posY, widthUnits, heightUnits, Block.class);
        if (newX <= leftWall)
        {
            return leftWall;
        }
        else if (newX >= rightWall)
        {
            return rightWall;
        } 
        else
        {
            return newX;
        }        
    }
    
    /**
     * Gibt den Wert der Sprung änderung wieder
     */
    public double jump(int i)
    {
       if (i==3)
       {
           Y+=1.5;
       }
       else
       {
           if (i == 2)
           {
               Y=2.5;
           }
           else
           {
               if (i==1)
               {
                  Y= 4.5; 
               }    
            }
       }
       return Y;
    }
    
    public void setY(double y)
    {
        Y = y;
    }
    
    /**
     * Läst auf den Körper der Figur eine Graditation wirken.
     */
    public double gravity (double posX, double posY, double widthUnits, double heightUnits)
    {
        if (Y > -70)
        {
            Y -= 0.25;
        }
              
        double newY = posY + Y;
        double floor = getObjectBelow(posX, posY, widthUnits, heightUnits, Block.class);
        double ceiling = getObjectAbove(posX, posY, widthUnits, heightUnits, Block.class);        
        if (newY <= floor)
        {
            Y=0;
            return floor;
        }        
        else if (newY >= ceiling)
        {
            Y=0;
            return ceiling;
        }
        else
        {
            return newY;
        }
    }
    
    /**
     * liefert Position+Hoehe des Objektes, was sich unter dem Objekt an den gegebenen Koordinaten befindet, wenn es der übergebenen Klasse entspricht
     */
    public double getObjectBelow(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        double floor = -40;        
        int tolerance = 6;
        
        for (Entity entity : entities)
        {
            if (entity.getPosY() + entity.getHeightUnits() - tolerance <= posY && entity.getPosX() + entity.getWidthUnits() > posX && posX + widthUnits > entity.getPosX()  && (entity.getClass() == cls))
            {
                if (entity.getPosY()+entity.getHeightUnits() > floor && entity.isCollisionEnabled())
                {
                    floor = entity.getPosY()+entity.getHeightUnits();
                }
            }            
        }
        return floor;        
    }
    
    public double getObjectBelow(double posX, double posY, double widthUnits, double heightUnits, String name)
    {
        double floor = -40;        
        int tolerance = 6;
        
        for (Entity entity : entities)
        {
            if (entity.getPosY() + entity.getHeightUnits() - tolerance <= posY && entity.getPosX() + entity.getWidthUnits() > posX && posX + widthUnits > entity.getPosX()  && entity.getName().equals(name))
            {
                if (entity.getPosY()+entity.getHeightUnits() > floor && entity.isCollisionEnabled())
                {
                    floor = entity.getPosY()+entity.getHeightUnits();
                }
            }            
        }
        return floor;        
    }
    
    /**
     * liefert Position des Objektes, was sich über dem Objekt an den gegebenen Koordinaten befindet, wenn es der übergebenen Klasse entspricht
     */
    public double getObjectAbove(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        double ceiling = 100000;
        int tolerance = 6;
        
        for (Entity entity : entities)
        {
            if (entity.getPosY() >= posY + heightUnits - tolerance && entity.getPosX() + entity.getWidthUnits() > posX && posX + widthUnits > entity.getPosX()  && (entity.getClass() == cls))
            {
                if (entity.getPosY() - heightUnits < ceiling && entity.isCollisionEnabled())
                {
                    ceiling = entity.getPosY() - heightUnits;
                }
            }            
        }
        
        return ceiling;  
    }
    
    public double getObjectAbove(double posX, double posY, double widthUnits, double heightUnits, String name)
    {
        double ceiling = 100000;
        int tolerance = 6;
        
        for (Entity entity : entities)
        {
            if (entity.getPosY() >= posY + heightUnits - tolerance && entity.getPosX() + entity.getWidthUnits() > posX && posX + widthUnits > entity.getPosX()  && entity.getName().equals(name))
            {
                if (entity.getPosY() - heightUnits < ceiling && entity.isCollisionEnabled())
                {
                    ceiling = entity.getPosY() - heightUnits;
                }
            }            
        }
        
        return ceiling;  
    }
    
    /**
     * prüft, ob Objekt an den übergebenen Koordinaten ein Objekt von der übergebenen Klasse berührt
     */
    public boolean isTouchingObjectBelow(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        return posY <= getObjectBelow(posX, posY, widthUnits, heightUnits, cls);
    }
    
    /**
     * prüft, ob Objekt an den übergebenen Koordinaten ein Objekt von der übergebenen Klasse berührt
     */
    public boolean isTouchingObjectAbove(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        return posY >= getObjectAbove(posX, posY, widthUnits, heightUnits, cls);
    }
        
    /**
     * prüft, ob Objekt an den übergebenen Koordinaten ein Objekt von der übergebenen Klasse berührt
     */
    public boolean isTouchingLeftObject(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        return posX <= getLeftObject(posX, posY, widthUnits, heightUnits, cls);
    }
    
    /**
     * prüft, ob Objekt an den übergebenen Koordinaten ein Objekt von der übergebenen Klasse berührt
     */
    public boolean isTouchingRightObject(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        return posX >= getRightObject(posX, posY, widthUnits, heightUnits, cls);
    }
    
    /**
     * prüft, ob Objekt an den übergebenen Koordinaten von einem Objekt von der übergebenen Klasse berührt wird
     */
    public boolean isTouchedByObject(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        return isTouchingObjectAbove(posX, posY, widthUnits, heightUnits, cls) 
        || isTouchingLeftObject(posX, posY, widthUnits, heightUnits, cls) 
        || isTouchingRightObject(posX, posY, widthUnits, heightUnits, cls);
    }
    
    /**
     * liefert Position+Breite des Objektes, was sich links vom Objekt an den gegebenen Koordinaten befindet, wenn es der übergebenen Klasse entspricht
     */
    public double getLeftObject(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        double left = -100;
        int tolerance = 4;
        
        for (Entity entity : entities)
        {
                        
            if (entity.getPosX() + entity.getWidthUnits() <= posX + tolerance  && entity.getPosY() + entity.getHeightUnits() > posY && posY + heightUnits > entity.getPosY() && (entity.getClass() == cls))
            {
                if (entity.getPosX()+entity.getWidthUnits() > left && entity.isCollisionEnabled())
                {
                    left = entity.getPosX()+entity.getWidthUnits();
                }
            }
            
        }
        
        return left;
    }
    
    public double getLeftObject(double posX, double posY, double widthUnits, double heightUnits, String name)
    {
        double left = -100;
        int tolerance = 4;
        
        for (Entity entity : entities)
        {
                        
            if (entity.getPosX() + entity.getWidthUnits() <= posX + tolerance  && entity.getPosY() + entity.getHeightUnits() > posY && posY + heightUnits > entity.getPosY() && entity.getName().equals(name))
            {
                if (entity.getPosX()+entity.getWidthUnits() > left && entity.isCollisionEnabled())
                {
                    left = entity.getPosX()+entity.getWidthUnits();
                }
            }
            
        }
        
        return left;
    }
    
    /**
     * liefert Position des Objektes, was sich rechts vom Objekt an den gegebenen Koordinaten befindet, wenn es der übergebenen Klasse entspricht
     */
    public double getRightObject(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        double right = 100000;
        int tolerance = 4;
        
        for (Entity entity : entities)
        {
                        
            if (entity.getPosX() >= posX + widthUnits - tolerance && entity.getPosY() + entity.getHeightUnits() > posY && posY + heightUnits > entity.getPosY() && (entity.getClass() == cls))
            {
                if (entity.getPosX() - widthUnits < right && entity.isCollisionEnabled())
                {
                    right = entity.getPosX() - widthUnits;
                }
            }
            
        }
        
        return right;
    }
    
    public double getRightObject(double posX, double posY, double widthUnits, double heightUnits, String name)
    {
        double right = 100000;
        int tolerance = 4;
        
        for (Entity entity : entities)
        {
                        
            if (entity.getPosX() >= posX + widthUnits - tolerance && entity.getPosY() + entity.getHeightUnits() > posY && posY + heightUnits > entity.getPosY() && entity.getName().equals(name))
            {
                if (entity.getPosX() - widthUnits < right && entity.isCollisionEnabled())
                {
                    right = entity.getPosX() - widthUnits;
                }
            }
            
        }
        
        return right;
    }
    
    public boolean isIntersecting(double posX, double posY, double widthUnits, double heightUnits, Object thisObject, Class<?> cls)
    {
        for (Entity entity : entities)
        {
            if (entity.getPosX() < posX + widthUnits && entity.getPosX() + entity.getWidthUnits() > posX && entity.getPosY() < posY + heightUnits && entity.getPosY() + entity.getHeightUnits() > posY) 
            {
                if (!(entity == thisObject) && entity.getClass() == cls && entity.isCollisionEnabled())
                {
                    return true;
                }
            }       
        }
        return false;
    }
    
    public boolean isIntersecting(double posX, double posY, double widthUnits, double heightUnits, Object thisObject, String name, String state)
    {
        for (Entity entity : entities)
        {
            if (entity.getPosX() < posX + widthUnits && entity.getPosX() + entity.getWidthUnits() > posX && entity.getPosY() < posY + heightUnits && entity.getPosY() + entity.getHeightUnits() > posY) 
            {
                if (!(entity == thisObject) && entity.getName().equals(name) && entity.isCollisionEnabled() && entity.getState().equals(state))
                {
                    return true;
                }
            }          
        }
        
        return false;
    }
    
    public boolean isIntersecting(double posX, double posY, double widthUnits, double heightUnits, Object thisObject, String name, String state, String activity)
    {
        for (Entity entity : entities)
        {
            if (entity.getPosX() < posX + widthUnits && entity.getPosX() + entity.getWidthUnits() > posX && entity.getPosY() < posY + heightUnits && entity.getPosY() + entity.getHeightUnits() > posY) 
            {
                if (!(entity == thisObject) && entity.getName().equals(name) && entity.isCollisionEnabled() && entity.getState().equals(state) && entity.getActivity().equals(activity))
                {
                    return true;
                }
            }          
        }
        
        return false;
    }
    
    public boolean isIntersecting(double posX, double posY, double widthUnits, double heightUnits, Object thisObject, String name)
    {
        for (Entity entity : entities)
        {
            if (entity.getPosX() < posX + widthUnits && entity.getPosX() + entity.getWidthUnits() > posX && entity.getPosY() < posY + heightUnits && entity.getPosY() + entity.getHeightUnits() > posY) 
            {
                if (!(entity == thisObject) && entity.getName().equals(name) && entity.isCollisionEnabled())
                {
                    return true;
                }
            }          
        }
        
        return false;
    }

    public void setSpeed (double speed)
    {
        this.speed=speed;
    }
    
    public void setMaxSpeed (double maxSpeed)
    {
        this.maxSpeed = maxSpeed;
    }
    
    public double getSpeed ()
    {
        return speed;
    }
    
    public double getYMove ()
    {
        return Y;
    }
}
