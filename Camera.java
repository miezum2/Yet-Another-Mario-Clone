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
    
    private void calculateCameraZoom(List<Entity> entities)
    {
          // Alle Player heraussuchen
        List<Entity> players = new ArrayList<Entity>();
        for (Entity entity: entities)
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
    }
    
    private void calculateCameraPos()
    {
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
            targetPosX += Math.abs(minX);
            minX = 0;
        }
        
        if (minY < 0)
        {
            maxY += Math.abs(minY);
            targetPosY += Math.abs(minY);
            minY = 0;
        }
    }
        
    public void calculateCamera(List<Entity> entities)
    {
        calculateCameraZoom(entities);
        calculateCameraPos();        
    }   
    
    public void calculateEntities(List<Entity> entities)
    {
        // Alle Objekte durchgehen und Position im Level in Position auf Welt umrechnen
        for (Entity entity : entities)
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
    }
    
    // An map() Funktion aus der Arduino Library angelehnt
    private double map(double value, double in_min, double in_max, double out_min, double out_max)
    {
        return ((value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
    }    
    
    private double mapX(double value)
    {
        return map(value, minX, maxX, 0, widthPixels);
    }
    
    private double mapY(double value)
    {
        return map(value, minY, maxY, heightPixels, 0);
    }
    
    public int mapToWorldX(int screenX)
    {
        return (int)Math.round(map(screenX, 0, widthPixels, minX, maxX));
    }
    
    public int mapToWorldY(int screenY)
    {
        return (int)Math.round(map(screenY, 0, heightPixels, maxY, minY));
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
    
    public int alignXatGrid(int screenX)
    {
        // Mausposition in Weltposition umrechnen
        // nur Vielfache von 16 zulassen
        return (int)mapX(mapToWorldX(screenX)/16 * 16);
    }
    
    public int alignYatGrid(int screenY)
    {
        // Mausposition in Weltposition umrechnen
        // nur Vielfache von 16 zulassen
        return (int)mapY(mapToWorldY(screenY)/16 * 16);
    }
    
    public void moveX(double movement)
    {
        targetPosX += movement;
    }
    
}
