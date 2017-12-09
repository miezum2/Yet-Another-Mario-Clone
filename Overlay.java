import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DebuggingOverlay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Overlay extends Actor
{
    GreenfootImage image;
    
    public Overlay(int width, int height)
    {
        image = new GreenfootImage(width, height);
    }
    
    /**
     * Act - do whatever the DebuggingOverlay wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }  
    
    public void drawGrid(double scale, double minX, double minY)
    {
        
    }
}
