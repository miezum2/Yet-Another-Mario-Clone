import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Movement
{
    private Vector movement;
    private double X;
    private double Y;
    //Gibt die Beschleunigung der Entity an
    private double acceleration;
    //Sprung Vector
    private Vector jump = new Vector(0,0);
    //Lauf Vector
    private double speed;
    //Erdanziegung
    Vector gravityforce = new Vector(0,0.5);
    
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
        
        return X;
       
    }
    
    public void jump()
    {
       
        if (jump.getY()>-2.5)
        {
            jump.add(new Vector(0,-acceleration));          
            //System.out.println(jump.getY());
            //System.out.println(jump.getX());    
        }
        
        /*
        if (exactY>=(getWorld().getHeight()/4)*3)
        {
            exactY=(getWorld().getHeight()/4)*3;
        }
        */
       
    }
    
    public void gravity ()
    {
        
        //exactY = exactY + gravityforce.getY();
        /*
        if (exactY>=(getWorld().getHeight()/4)*3)
        {
            exactY=(getWorld().getHeight()/4)*3;
        }
        */
       /*
        if (walk.getX() != 0)
        {
            if (walk.getX() < 0)
            {
                move(180);
                walk.add(new Vector(acceleration*2,0));
            }
            else
            {
                if (walk.getX() > 0)
                {
                    move(0);
                    walk.add(new Vector(-acceleration*2,0));
                }
            }
        }
        */
        
        
    }

    
}
