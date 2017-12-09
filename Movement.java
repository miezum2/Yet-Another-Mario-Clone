import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
    
    public Movement(double speed, double accel)
    {    
        this.speed = speed;
        this.acceleration = accel;
    }
   

    /**
     * Bewegt sich in die aktuelle Bewegungsrichtung.
     * Direction gibt die laufrichtung an, dabei ist 0 nach rechts und 180 nach links.
     */
    public double move(int direction) 
    {
        
        //Bewegung nach rechst
        if (speed<2.5 && direction==0)
        { 
            speed = speed + acceleration;
        }
        //bewegung nach links
        if (speed>-2.5 && direction==180)
        {
            speed = speed - acceleration;
        }
        
        return speed;
    }
    
    /**
     * Gibt den Wert der Sprung änderung wieder
     */
    public double jump(int i)
    {
       if (i == 3)
       {
           Y= 10;
           jumpcount = 0;
       }
       else
       {
          Y= 5; 
       }
       return Y;
    }
    /**
     * Läst auf den Körper der Figur eine Graditation wirken.
     */
    public double gravity ()
    {
        if (Y > -70)
        {
            Y -= 0.25;
        }
        return Y;
        
    }

    public void setSpeed (int speed)
    {
        this.speed=speed;
    }
    
    public double getYMove ()
    {
        return Y;
    }
}
