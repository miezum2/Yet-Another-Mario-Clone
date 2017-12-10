import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Camera here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Camera
{
    private static final int defaultHeightUnits = 200;
    
    GraphicsManager graphics;
    
    int widthPixels;
    int heightPixels;
    double currentPosX;
    double currentPosY;
    double targetPosX;
    double targetPosY;
    double scale = 1;
    
    double widthUnits;
    double heightUnits;
    double minX;
    double maxX;
    double minY;
    double maxY;
    
    /**
     * Constructor for objects of class Camera
     */
    public Camera(int width, int height)
    {
        // Größe der Greenfoot-Welt speichern
        this.widthPixels = width;
        this.heightPixels = height;        
    }  

    public void calculatePositions(List<Entity> allEntities)
    {
        // Alle Player heraussuchen
        List<Entity> players = new ArrayList<Entity>();
        for (Entity entity: allEntities)
        {
            if (entity.getClass() == Player.class)
            {
                players.add(entity);
            }
        }
        
        // kleinsten erlaubten Kamerausschnitt berechnen
        heightUnits = defaultHeightUnits;
        widthUnits = widthPixels*heightUnits/heightPixels; 
        
        // Position der Kamera an den Spielern ausrichten
        // Berechnungen gelten auch für Einzelspieler      
        
        // Mittelpunkt zwischen den Spielern und deren maximale Position ermitteln
        double sumX = 0;
        double sumY = 0;
        
        double playersMinX = players.get(0).getPosX();
        double playersMaxX = playersMinX;
        double playersMinY = players.get(0).getPosY();
        double playersMaxY = playersMinY;
        for (Entity player : players)
        {
            // X und Y Positionen der Spieler addieren
            sumX += player.getPosX();
            sumY += player.getPosY();
            
            // minimale und maximale x und y position aller Spieler ermitteln
            if (player.getPosX() < playersMinX) 
            { 
                playersMinX = player.getPosX(); 
            }
            if (player.getPosX() > playersMaxX) 
            { 
                playersMaxX = player.getPosX(); 
            }
            if (player.getPosY() < playersMinY) 
            { 
                playersMinY = player.getPosY(); 
            }
            if (player.getPosY() > playersMaxY) 
            { 
                playersMaxY = player.getPosY(); 
            }
        }
        
        // Mittelpunkt berechnen
        targetPosX = sumX / players.size();
        targetPosY = sumY / players.size();
        
        // Abstand der Spieler in X - und Y-Richtung berechnen
        double distanceX = playersMaxX - playersMinX;
        double distanceY = playersMaxY - playersMinY;
        
        // wenn horizontaler Abstand größer als n -> Kameraauschnitt vergrößern
        if (distanceX > widthUnits-50)
        {
            widthUnits = distanceX+50;
            heightUnits = heightPixels*widthUnits/widthPixels;
        }
        
        // wenn vertikaler Abstand größer als n -> Kameraauschnitt vergrößern
        if (distanceY > heightUnits-50)
        {
            heightUnits = distanceY+50;
            widthUnits = widthPixels*heightUnits/heightPixels; 
        }
          
        // Kameraposition berechnen
        currentPosX = targetPosX;
        currentPosY = targetPosY; 
        
        // Kameraausschnitt berechnen              
        minY = currentPosY - heightUnits/2;
        maxY = currentPosY + heightUnits/2;
        minX = currentPosX - widthUnits/2;
        maxX = currentPosX + widthUnits/2;          
        scale = heightPixels/heightUnits;
        
        // Wenn Kamera in den negativen Bereich ragt, in positiven Bereich verschieben
        if (minX < 0)
        {
            maxX += Math.abs(minX);
            minX = 0;
        }
        
        if (minY < 0)
        {
            maxY += Math.abs(minY);
            minY = 0;
        }
          
        /*
        System.out.println("heightUnits: "+heightUnits);
        System.out.println("widthUnits: "+widthUnits);
        System.out.println("minY: "+minY);
        System.out.println("maxY: "+maxY);
        System.out.println("minX: "+minX);
        System.out.println("maxX: "+maxX);
        System.out.println("scale: "+scale);      */ 
        
        
        // Alle Objekte durchgehen und Position im Level in Position auf Welt umrechnen
        for (Entity entity : allEntities)
        {
            double range = 16*scale;
            
            // Koordinaten in neuen Bereich mappen
            entity.setCameraX(mapX(entity.getPosX()));
            entity.setCameraY(mapY(entity.getPosY()));   
            
            // Objekt deaktivieren, wenn es sich außerhalb des Bildschirms befindet
            if (entity.getCameraX() < - range || entity.getCameraX() > widthPixels + range
                || entity.getCameraY() < - range || entity.getCameraY() > heightPixels + range)
            {
                entity.disable();
            }
            else
            {
                entity.enable();
            }            
        }
        
        /*
        System.out.println("map minX: "+minX+" -> "+mapX(minX));        
        System.out.println("map maxX: "+maxX+" -> "+mapX(maxX));
        System.out.println("map minY: "+minY+" -> "+mapY(minY));        
        System.out.println("map maxY: "+maxY+" -> "+mapY(maxY));
        */
        
        //Entity test = new Entity("0", graphics.getImage("Ground", "default", "dirt", "right", 0));
        //getWorld().addObject(new SmoothMover(), 100, 100);
    }      
    
    // An map() Funktion aus der Arduino Library angelehnt
    private int map(double value, double in_min, double in_max, double out_min, double out_max)
    {
        return (int)((value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
    }    
    
    private int mapX(double value)
    {
        return map(value, minX, maxX, 0, widthPixels);
    }
    
    private int mapY(double value)
    {
        return map(value, minY, maxY, heightPixels, 0);
    }
    
    public double getScale()
    {
        return scale;
    }
    
    public double getMinX()
    {
        return minX;
    }
    
    public double getMinY()
    {
        return minY;
    }
}
