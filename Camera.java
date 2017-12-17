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
        
        heightUnits = defaultHeightUnits;
        widthUnits = widthPixels*heightUnits/heightPixels; 
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
        
        if (players.size() > 0)
        {
        
            // kleinsten erlaubten Kamerausschnitt berechnen
            double minHeightUnits = defaultHeightUnits;
            double minWidthUnits = widthPixels*minHeightUnits/heightPixels; 
            
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
            targetPosX = (playersMinX + playersMaxX) / 2 + 48;
            targetPosY = (playersMinY + playersMaxY) / 2 - 32;
            
            // Abstand der Spieler in X - und Y-Richtung berechnen
            double distanceX = playersMaxX - playersMinX;
            double distanceY = playersMaxY - playersMinY;   
            
            int borderX = 150;
            int borderY = 50;
            
            if (distanceX + borderX > minWidthUnits)
            {
                widthUnits = smoothMove(widthUnits, distanceX + borderX, 30);                 
                heightUnits = widthUnits*heightPixels/widthPixels;
            }
            else
            {
                if (distanceY + borderY > minHeightUnits)
                {
                    heightUnits = smoothMove(heightUnits, distanceY + borderY, 30);
                    widthUnits = heightUnits*widthPixels/heightPixels;
                }
                else
                {       
                    widthUnits = smoothMove(widthUnits, minWidthUnits, 30);
                    heightUnits = widthUnits*heightPixels/widthPixels;
                }
            }    
            
        }
    }
    
    private double smoothMove(double currentValue, double targetValue, double fraction)
    {
        //System.out.println("current: " + currentValue);
        //System.out.println("target : " + targetValue);
        
        if (fraction < 1)
        {
            fraction = 1;
        }
        
        double delta = targetValue - currentValue;
        return currentValue + delta / fraction; 
        
    }
    
    /**
     * Ausdehung des Kamerarahmens um den zuvor bestimmten Mittelpunkt bestimmen
     */    
    private void calculateCameraPos(String zoomMode)
    {
        // Kameraposition berechnen
        if (zoomMode.equals("slow"))
        {
            currentPosX = smoothMove(currentPosX, targetPosX, 30);
            currentPosY = smoothMove(currentPosY, targetPosY, 30);
        }
        else if (zoomMode.equals("instant"))
        {
        
        }
        else
        {
            currentPosX = targetPosX;
            currentPosY = targetPosY;
        }
        
        // Y-Position der Kamera wird bei 0 verankert, weil der Editor (noch) kein vertikales Scrolling erlaubt
        //currentPosY = 0;
        
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
            currentPosX += Math.abs(minX);
            minX = 0;
        }
        
        if (minY < 0)
        {
            maxY += Math.abs(minY);
            currentPosY += Math.abs(minY);
            minY = 0;
        }        
        
    }
        
    /**
     * Kamera Zoom und Position anhand der aktuellen Welt berechnen
     */
    public void calculateCamera(List<Entity> entities, String refreshMode)
    {
        calculateCameraZoom(entities);
        calculateCameraPos(refreshMode);        
    }   
    
    /**
     * Bildschirmposition aller Entities anhand des zuvor berechneten Kameraausschnittes berechnen
     */
    public void calculateEntities(List<Entity> entities)
    {
        // Alle Objekte durchgehen und Position im Level in Position auf Welt umrechnen
        for (Entity entity : entities)
        {
            double rangeX = 64*scale;
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
    
    public double getPixelOffsetXEnd()
    {
        return ((16 - (minX % 16))*scale);
    }
    
    public double getPixelOffsetXBeginning()
    {
        return ((16 - (minX % 16))*scale - (16*scale - 16*scale*0.95));
    }
    
    public double getPixelOffsetYEnd()
    {
        return ((16 - (minY % 16))*scale);
    }
    
    public double getPixelOffsetYBeginning()
    {
        return ((16 - (minY % 16))*scale - (16*scale - 16*scale*0.95) - 1);
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
        currentPosX += movement;
        calculateCameraPos("instant");
    }
    
    /**
     * Kameraauschnitt um n Units auf der Y-Achse verschieben
     */
    public void moveY(double movement)
    {
        currentPosY += movement;
        calculateCameraPos("instant");
    }
    
}
