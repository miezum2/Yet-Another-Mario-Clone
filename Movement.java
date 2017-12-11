import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

public class Movement
{
    //Gibt die Bewegung in Y-Richtung an
    private double Y;
    //Gibt die Beschleunigung der Entity an
    private double acceleration;
    //Sprung
    private double jumpcount;
    //Lauf Vector
    private double speed;
    
    private List<Entity> entities;
        
    public Movement(double speed, double accel)
    {    
        this.speed = speed;
        this.acceleration = accel;
    }   
    
    public void setEntities(List<Entity> entities)
    {
        this.entities = entities;
    }

    /**
     * Bewegt sich in die aktuelle Bewegungsrichtung.
     * Direction gibt die laufrichtung an, dabei ist 0 nach rechts und 180 nach links.
     */
    public double move(int direction, double posX, double posY, double widthUnits, double heightUnits) 
    {
        
        //Bewegung nach rechst
        if (speed<2 && direction==0)
        { 
            speed = speed + acceleration;
        }
        //bewegung nach links
        if (speed>-2 && direction==180)
        {
            speed = speed - acceleration;
        }
        
        double newX = posX + speed;
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
           Y=10;
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
                  Y= 5; 
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
    
    public double getObjectBelow(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        double floor = 0;        
        int tolerance = 4;
        
        for (Entity entity : entities)
        {
            if (entity.getPosY() + entity.getHeightUnits() - tolerance <= posY && entity.getPosX() + entity.getWidthUnits() > posX && posX + widthUnits > entity.getPosX()  && (entity.getClass() == cls))
            {
                if (entity.getPosY()+entity.getHeightUnits() > floor)
                {
                    floor = entity.getPosY()+entity.getHeightUnits();
                }
            }            
        }
        return floor;        
    }
    
    public double getObjectAbove(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        double ceiling = 100000;
        int tolerance = 4;
        
        for (Entity entity : entities)
        {
            if (entity.getPosY() >= posY + heightUnits - tolerance && entity.getPosX() + entity.getWidthUnits() > posX && posX + widthUnits > entity.getPosX()  && (entity.getClass() == cls))
            {
                if (entity.getPosY() - heightUnits < ceiling)
                {
                    ceiling = entity.getPosY() - heightUnits;
                }
            }            
        }
        
        return ceiling;  
    }
    
    public boolean isTouchingObjectBelow(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        return posY <= getObjectBelow(posX, posY, widthUnits, heightUnits, cls);
    }
    
    public boolean isTouchingObjectAbove(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        return posY >= getObjectAbove(posX, posY, widthUnits, heightUnits, cls);
    }
    
    public boolean isTouchingLeftObject(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        return posX <= getLeftObject(posX, posY, widthUnits, heightUnits, cls);
    }
    
    public boolean isTouchingRightObject(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        return posX >= getRightObject(posX, posY, widthUnits, heightUnits, cls);
    }
    
    public boolean isTouchedByObject(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        return isTouchingObjectAbove(posX, posY, widthUnits, heightUnits, cls) || isTouchingLeftObject(posX, posY, widthUnits, heightUnits, cls) || isTouchingRightObject(posX, posY, widthUnits, heightUnits, cls);
    }
    
    public double getLeftObject(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        double left = 0;
        int tolerance = 4;
        
        for (Entity entity : entities)
        {
                        
            if (entity.getPosX() + entity.getWidthUnits() <= posX + tolerance  && entity.getPosY() + entity.getHeightUnits() > posY && posY + heightUnits > entity.getPosY() && (entity.getClass() == cls))
            {
                if (entity.getPosX()+entity.getWidthUnits() > left)
                {
                    left = entity.getPosX()+entity.getWidthUnits();
                }
            }
            
        }
        
        return left;
    }
    
    public double getRightObject(double posX, double posY, double widthUnits, double heightUnits, Class<?> cls)
    {
        double right = 100000;
        int tolerance = 4;
        
        for (Entity entity : entities)
        {
                        
            if (entity.getPosX() >= posX + widthUnits - tolerance && entity.getPosY() + entity.getHeightUnits() > posY && posY + heightUnits > entity.getPosY() && (entity.getClass() == cls))
            {
                if (entity.getPosX() - widthUnits < right)
                {
                    right = entity.getPosX() - widthUnits;
                }
            }
            
        }
        
        return right;
    }

    public void setSpeed (double speed)
    {
        this.speed=speed;
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
