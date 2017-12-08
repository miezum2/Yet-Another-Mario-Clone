import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.File;
import com.google.gson.*;
import java.util.*;

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class UserInterface extends World
{
    private static final int width = 500;
    private static final int height = 400;
    private static final String imageDir = "images";
    private static final String levelDir = "levels";
    
    private static final String nameP1 = "Mario";
    private static final String[] controlsP1 = {"w","a","s","d","space"};   
    
    private GraphicsManager graphics;
    private LevelSelector levelSelector;
    private Level level;
    private Camera camera;
    
    private Text fpsCounter;
    private Text entityCounter;
    
    private LevelMaker levelMaker; 
    
    // Quelle: https://www.greenfoot.org/doc/native_loader
    static {
        NativeLoader loader = new NativeLoader();        
        loader.addClasspath("./+libs/gson-2.8.0.jar"); 
        loader.loadClass("com.google.gson.Gson");
    }
    
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public UserInterface()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(width, height, 1, false); 
        
        // Log "ausleeren"
        for (int i = 0; i < 50; i++)
        { 
            System.out.println();
        }
        
        // Graphics-Objekt initialisieren, alle Bilder laden
        graphics = new GraphicsManager(imageDir);  
        
        // Level Selector
        levelSelector = new LevelSelector(levelDir);
        
        // Level erstellen
        level = new Level(levelSelector.getLevelList().get(0));
        
        // Kamera erstellen
        camera = new Camera(width, height);
        //addObject(camera, 0,0);
        
        // Debug Informationen
        fpsCounter = new Text();
        addObject(fpsCounter, 20, 6);
        entityCounter = new Text();
        addObject(entityCounter, 20, 18);
        //addObject(new CameraZones(width, height, 50, 100, 50), width/2, height/2);
        
        //addObject(new Player(nameP1, graphics.getImage(nameP1, "small", "walking", "right", 0), controlsP1), 100, 100);
        //addObject(new Object("Mystery_Block"), 200, 200);        
        //addObject(new LevelSelector(), 0, 0);
        
        //JsonObject jsonObject = new JsonParser().parse("{\"name\": \"John\"}").getAsJsonObject();
        //System.out.println(jsonObject.get("name").getAsString());
        levelMaker = new LevelMaker();
        addObject(levelMaker,200,200);
    }
    
    private long lastNanoTime = 0;
    public void act()
    {
        long newNanoTime = System.nanoTime();
        long diff = newNanoTime - lastNanoTime;
        String fps = (int)Math.floor(1/(diff/1000000000.0))+" FPS";
        lastNanoTime = newNanoTime;
        
        fpsCounter.setText(fps);
        fpsCounter.setLocation(fpsCounter.getImage().getWidth()/2, 6);
        
        // Alle Objekte vom Bildschirm löschen
        List<Entity> currentEntities = getObjects(Entity.class);
        removeObjects(currentEntities);
        
        level.update();        
        
        // Kamera-Objekt anweisen, die Position der Entities in der Welt in Bildschirm-Koordinaten umzurechnen
        // nicht sichtbare Entities deaktivieren
        List<Entity> allEntities = level.getEntities();
        camera.calculatePositions(allEntities);
            
        // Alle Objekte durchgehen
        for (Entity entity : allEntities)
        {
            // Objekt aktualisieren und zeichnen, wenn es nicht deaktiviert ist
            if (entity.isEnabled())
            {
                entity.update(allEntities);
                graphics.setScale(camera.getScale());
                entity.setImage(graphics.getImage(entity.getName(), entity.getState(), entity.getActivity(), entity.getOrientation(), entity.getAnimationIndex()));
                entity.calculateExactPos();            
                addObject(entity, (int)entity.getCameraX(), (int)entity.getCameraY());
            }
        }
        
        entityCounter.setText(currentEntities.size()+" Entities");
        entityCounter.setLocation(entityCounter.getImage().getWidth()/2, 18);
        
        //System.out.println("UserInterface Act");
        
        checkMous();
    }
    
    
    private boolean frage = false;
    public void checkMous ()
    {
        int x;
        int y;
        boolean aenderung = false;
        MouseInfo Maus = Greenfoot.getMouseInfo();
            if (Maus != null)
            {
                if (Maus.getButton()==1)
                {
                    if (Maus.getActor() != null)
                    {
                        Actor object = Maus.getActor();
                        String name = object.toString();
                        name = name.intern();
                        System.out.println(name);
                        //prüfen welcher Actor vorliegt
                        if (name.contains("Block"))
                        {
                            aenderung = true;
                        }
                        //prüfen ob object Levelmaker Angesteuert wird
                        if (!frage)
                        {
                            if (name.contains("LevelMaker"))
                            {
                                levelMaker.createLevelSelector();
                                frage=true;
                            }
                        } 
                        //ändern der Postition je nach Actor
                        if (aenderung)
                        {
                            y= Maus.getY();
                            x= Maus.getX();
                            object.setLocation(x,y);
                        }
                         
                        
                    }
                } 
            }
    }
}
