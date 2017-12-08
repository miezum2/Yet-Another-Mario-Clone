 import greenfoot.Greenfoot;

/**
 * Ein 2D-Vektor.
 * 
 * @author Poul Henriksen
 * @author Michael Kolling
 * 
 * @version 2.0
 */
public final class Vector
{
    double dx;
    double dy;
    int direction;
    double length;
    
    /**
     * Erzeugt einen neuen neutralen Vektor.
     */
    public Vector()
    {
    }

    /**
     * Erzeugt einen Vektor einer gegebenen Richtung und L�nge. Die Richtung sollte im Bereich
     * [0..359] liegen, wobei 0 OSTEN ist und die Grade im Uhrzeigersinn zunehmen.
     */
    /*
    public Vector(int direction, double length)
    {
        this.length = length;
        this.direction = direction;
        updateCartesian();
    }
    */
    /**
     * Erzeugt einen Vektor durch die Angabe der X- und Y-Abst�nde zwischen Anfangs- und Endpunkt.
     */
    public Vector(double dx, double dy)
    {
        this.dx = dx;
        this.dy = dy;
        updatePolar();
    }

    /**
     * Setzt die Richtung dieses Vektors, ohne die L�nge zu ver�ndern.
     */
    public void setDirection(int direction) 
    {
        this.direction = direction;
        updateCartesian();
    }
   
    /**
     * Addiert einen weiteren Vektor zu diesem Vektor.
     */
    public void add(Vector other) 
    {
        dx += other.dx;
        dy += other.dy;
        updatePolar();
    }
    
    /**
     * Setzt die L�nge dieses Vektors, ohne die Richtung zu ver�ndern.
     */
    public void setLength(double length) 
    {
        this.length = length;
        updateCartesian();
    }
    
    public void resetVector ()
    {
        dx=0;
        dy=0;
    }
    
    /**
     * Kehrt die horizontale Komponente dieses Bewegungsvektors um.
     */
    public void revertHorizontal() 
    {
        dx = -dx;
        updatePolar();
    }
    
    
    /**
     * Liefert den X-Wert dieses Vektors zur�ck (Anfangs- bis Endpunkt).
     */
    public double getX() 
    {
        return dx;
    }
     
    /**
     * Liefert den Y-Wert dieses Vektors zur�ck (Anfangs- bis Endpunkt).
     */
    public double getY() 
    {
        return  dy;
    }
    
    /**
     * Liefert die Richtung dieses Vektors (in Grad) zur�ck. 0 ist OSTEN.
     */
    public int getDirection() 
    {
        return direction;
    }
    
    /**
     * Liefert die L�nge dieses Vektors zur�ck.
     */
    public double getLength() 
    {
        return length;
    }

    /**
     * Aktualisiert die Richtung und die L�nge aus den aktuellen Werten von dx, dy.
     */
    private void updatePolar() 
    {
        this.direction = (int) Math.toDegrees(Math.atan2(dy, dx));
        this.length = Math.sqrt(dx*dx+dy*dy);
    }   
    
    /**
     * Aktualisiert dx und dy aus der aktuellen Richtung und L�nge.
     */
    private void updateCartesian() 
    {
        dx = length * Math.cos(Math.toRadians(direction));
        dy = length * Math.sin(Math.toRadians(direction));   
    }
    
    /**
     * Liefert eine Kopie dieses Vektors zur�ck.
     */
    public Vector copy() 
    {
        Vector copy = new Vector();
        copy.dx = dx;
        copy.dy = dy;
        copy.direction = direction;
        copy.length = length;
        return copy;
    }    
}