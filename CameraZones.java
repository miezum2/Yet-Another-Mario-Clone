import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CameraZones here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CameraZones
{
    private int width;
    private int height;
    private int widthRoom;
    private int heightRoom;
    private int maxSpeed;
    private GreenfootImage image;
    /*
    public CameraZones(int width, int height, int widthRoom, int heightRoom, int maxSpeed)
    {
        this.width = width;
        this.height = height;
        this.widthRoom = widthRoom;
        this.heightRoom = heightRoom;
        this.maxSpeed = maxSpeed;
        
        //Bild initialisieren
        image = new GreenfootImage(width, height);

        // Raumbegrenzung ermitteln
        int minX = width/2-widthRoom/2;
        int maxX = width/2+widthRoom/2;
        int minY = height/2-heightRoom/2;
        int maxY = height/2+heightRoom/2;
        
        //Linien einzeichnen
        image.setColor(Color.BLACK);
        image.drawLine(minX, minY, maxX, minY);
        image.drawLine(maxX, minY, maxX, maxY);
        image.drawLine(maxX, maxY, minX, maxY);
        image.drawLine(minX, maxY, minX, minY);
        setImage(image);
    } */
    
    /**
     * Act - do whatever the CameraZones wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
}
