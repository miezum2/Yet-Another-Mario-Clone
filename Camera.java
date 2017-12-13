import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * rechnet Entitypositionen im Spiel in Positionen auf dem Bildschirm um und umgekehrt
 * 
 * @author Simon Kemmesies
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
     * neue Kamera erstellen und Größe der Welt übergeben
     */
    public Camera(int width, int height)
    {
        // Größe der Greenfoot-Welt speichern
        this.widthPixels = width;
        this.heightPixels = height;  
    }  
    
    /**
     * Skalierungsfaktor der Welt so berechnen, dass Beide Spieler gleichzeitig dargestellt werden können
     */
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
    
    /**
     * Ausdehung des Kamerarahmens um den zuvor bestimmten Mittelpunkt bestimmen
     */    
    private void calculateCameraPos()
    {
        // Kameraposition berechnen
        currentPosX = targetPosX;
        currentPosY = targetPosY; 
        
        // Y-Position der Kamera wird bei 0 verankert, weil der Editor (noch) kein vertikales Scrolling erlaubt
        currentPosY = 0;
        
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
        
    /**
     * Kamera Zoom und Position anhand der aktuellen Welt berechnen
     */
    public void calculateCamera(List<Entity> entities)
    {
        calculateCameraZoom(entities);
        calculateCameraPos();        
    }   
    
    /**
     * Bildschirmposition aller Entities anhand des zuvor berechneten Kameraausschnittes berechnen
     */
    public void calculateEntities(List<Entity> entities)
    {
        // Alle Objekte durchgehen und Position im Level in Position auf Welt umrechnen
        for (Entity entity : entities)
        {
            double rangeX = 16*scale;
            double rangeY = 64*scale;
            
            // Koordinaten in neuen Bereich mappen
            entity.setCameraX(mapX(entity.getPosX()));
            entity.setCameraY(mapY(entity.getPosY()));   
            
            // Objekt deaktivieren, wenn es sich außerhalb des Bildschirms befindet
            if (entity.getCameraX() < - rangeX || entity.getCameraX() > widthPixels + rangeX
                || entity.getCameraY() < - rangeY || entity.getCameraY() > heightPixels + rangeY)
            {
                entity.disable();
            }
            else
            {
                entity.enable();
            }            
        }
    }
    
    /**
     * Wert aus einem Zahlenbereich in einen anderen Zahlenbereich umrechnen
     * (an map() Funktion aus der Arduino Library angelehnt)
     */
    private double map(double value, double in_min, double in_max, double out_min, double out_max)
    {
        return ((value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
    }    
    
    /**
     * X-Koordinate in Bildschirmposition umrechnen
     */
    private double mapX(double value)
    {
        return map(value, minX, maxX, 0, widthPixels);
    }
    
    /**
     * Y-Koordinate in Bildschirmposition umrechnen
     */
    private double mapY(double value)
    {
        return map(value, minY, maxY, heightPixels, 0);
    }
    
    /**
     * X-Position auf dem Bildschirm in Welt-X-Koordinate umrechnen
     */
    public int mapToWorldX(int screenX)
    {
        return (int)Math.round(map(screenX, 0, widthPixels, minX, maxX));
    }
    
    /**
     * Y-Position auf dem Bildschirm in Welt-Y-Position umrechnen
     */
    public int mapToWorldY(int screenY)
    {
        return (int)Math.round(map(screenY, 0, heightPixels, maxY, minY));
    }
    
    /**
     * liefert aktuellen Skalierungsfaktor der Welt
     */
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
    
    /**
     * nimmt X-Bildschirmposition entgegen und gibt an Weltraster eingerastete X-Bildschirmposition zurück
     */
    public int alignXatGrid(int screenX)
    {
        // Bildschirmposition in Weltposition umrechnen
        // nur Vielfache von 16 zulassen
        // Weltposition zurück in Bildschirmposition umrechnen
        return (int)mapX(mapToWorldX(screenX)/16 * 16);
    }
    
    /**
     * nimmt Y-Bildschirmposition entgegen und gibt an Weltraster eingerastete Y-Bildschirmposition zurück
     */
    public int alignYatGrid(int screenY)
    {
        // Bildschirmposition in Weltposition umrechnen
        // nur Vielfache von 16 zulassen
        // Weltposition zurück in Bildschirmposition umrechnen
        return (int)mapY(mapToWorldY(screenY)/16 * 16);
    }
    
    /**
     * Kameraauschnitt um n Units auf der X-Achse verschieben
     */
    public void moveX(double movement)
    {
        targetPosX += movement;
        calculateCameraPos();
    }
    
}
