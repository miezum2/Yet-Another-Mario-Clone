import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Movement
{
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
    
    public double jump()
    {
       if (jumpcount == 3)
       {
           Y= 70;
           jumpcount = 0;
       }
       return Y;
    }
    
    public void gravity ()
    {
        if (Y > -70)
        {
        Y -= 10;
        }
        
    }

    
}
