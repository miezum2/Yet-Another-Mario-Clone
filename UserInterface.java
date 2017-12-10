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
    
    private String mode;
    private GraphicsManager graphics;
    private LevelSelector levelSelector;
    private Level level;
    private Camera camera;
    
    private Text fpsCounter;
    private Text entityCounter;
    
    private LevelMaker levelMaker; 
    private Overlay makergrid;
    
    private ArrayList<Select> editor = new ArrayList<Select>();
    
    private ArrayList<Select> levelButton = new ArrayList<Select>(); 
    
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
        
        //level = new Level(levelSelector.getLevelList().get(0));
        
        // Kamera erstellen
        camera = new Camera(width, height);
        //addObject(camera, 0,0);
        
        // Debug Informationen
        fpsCounter = new Text();
        addObject(fpsCounter, 20, 6);
        entityCounter = new Text();
        addObject(entityCounter, 20, 18);
        //addObject(new CameraZones(width, height, 50, 100, 50), width/2, height/2);
        
        mode = "ingame";
        
        //addObject(new Player(nameP1, graphics.getImage(nameP1, "small", "walking", "right", 0), controlsP1), 100, 100);
        //addObject(new Object("Mystery_Block"), 200, 200);        
        //addObject(new LevelSelector(), 0, 0);
        
        //JsonObject jsonObject = new JsonParser().parse("{\"name\": \"John\"}").getAsJsonObject();
        //System.out.println(jsonObject.get("name").getAsString());
        levelMaker = new LevelMaker(levelDir);
        
        //addObject(levelMaker,200,200);
        makergrid = new Overlay(width, height);

        addObject(levelMaker,50,10);
        
        selectIni();
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
        
        removeObject(makergrid);
        
        if (mode.equals("levelSelector"))
        {
            GreenfootImage background = Tools.loadImage("images/levelselection20.png");
            background.scale(width, height);
            setBackground(background);  
            
        } else if (mode.equals("ingame"))
        {
            if (level == null)
            {
                level = new Level(levelSelector.getLevelList().get(0));
            }
            
            // Alle Entities vom Bildschirm löschen
            List<Entity> currentEntities = getObjects(Entity.class);
            removeObjects(currentEntities);
            
            level.update();        
            
            // Kamera-Objekt anweisen, die Position der Entities in der Welt in Bildschirm-Koordinaten umzurechnen
            // nicht sichtbare Entities deaktivieren
            List<Entity> allEntities = level.getEntities();
            camera.calculatePositions(allEntities);
            
            // Neuen Zustand aller Objekte ermitteln
            for (Entity entity : allEntities)
            {
                if (entity.isEnabled())
                {
                    entity.update(allEntities);
                    graphics.setScale(1);
                    GreenfootImage image = graphics.getImage(entity.getName(), entity.getState(), entity.getActivity(), entity.getOrientation(), entity.getAnimationIndex());
                    entity.setHeightUnits(image.getHeight());
                    entity.setWidthUnits(image.getWidth());                    
                }
            }
            
            // Objekte simulieren und einzeichnen
            for (Entity entity : allEntities)
            {
                // Objekt aktualisieren und zeichnen, wenn es nicht deaktiviert ist
                if (entity.isEnabled())
                {
                    entity.simulate(allEntities);
                    graphics.setScale(camera.getScale());
                    entity.setImage(graphics.getImage(entity.getName(), entity.getState(), entity.getActivity(), entity.getOrientation(), entity.getAnimationIndex()));
                    entity.calculateExactPos();            
                    addObject(entity, (int)entity.getCameraX(), (int)entity.getCameraY());
                }
            } 
            
            entityCounter.setText(currentEntities.size()+" Entities");
        } else if (mode.equals("editor"))   
        {
            // Alle Entities vom Bildschirm löschen
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
                    graphics.setScale(1);
                    GreenfootImage image = graphics.getImage(entity.getName(), entity.getState(), entity.getActivity(), entity.getOrientation(), entity.getAnimationIndex());
                    entity.setHeightUnits(image.getHeight());
                    entity.setWidthUnits(image.getWidth());
                    graphics.setScale(camera.getScale());
                    entity.setImage(graphics.getImage(entity.getName(), entity.getState(), entity.getActivity(), entity.getOrientation(), entity.getAnimationIndex()));
                    entity.calculateExactPos();            
                    addObject(entity, (int)entity.getCameraX(), (int)entity.getCameraY());
                }
            }    
            
            // Gitter zeichnen
            
            addObject(makergrid, width/2, height/2);
            makergrid.drawGrid(camera.getScale(), camera.getMinX(), camera.getMinY());
        }
        
        entityCounter.setLocation(entityCounter.getImage().getWidth()/2, 18);
        
        //System.out.println("UserInterface Act");
        
        checkMous();
    }
    
    //gibt an ob der LevelMaker gezeichnet wurde
    private boolean levelMakerdraw = false;
    //gebit an ob der Editormodus an ist
    private boolean edit = false;
    /**
     * Eine Mausabfrage welche das händling mit den Actorn ermöglicht.
     */
    public void checkMous ()
    {
        int x;
        int y;
        boolean aenderung = false;
        MouseInfo Maus = Greenfoot.getMouseInfo();
        
        //Prüfen das die Maus einen Actor anglickt
        if (Maus != null)
        {
            //Prüfen welche Taste gedrückt wurde
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
                    if (!levelMakerdraw)
                    {
                        if (name.contains("LevelMaker"))
                        {
                            //Levelasuwahl zeichen und anzeigen
                            levelMaker.createLevelSelector(levelSelector.getLevelList());
                            levelMakerdraw = true;
                            //Buttons für jedes Level erstellen (Play und Edit
                            createButtonLevel(levelSelector.getLevelList());
                        }
                    } 
                    //ändern der Postition je nach Actor
                    if (aenderung)
                    {
                        y= Maus.getY();
                        x= Maus.getX();
                        object.setLocation(x,y);
                    }
                    
                    //Buttonabfrage für die jeweiligen Editor Buttons
                    for(Select s:editor)
                    {
                        if (object.equals(s))
                        {
                            if (s.getName() == "stamp")
                            {
                                System.out.println(s.getName());
                            }
                            if (s.getName() == "trashcan")
                            {
                                System.out.println(s.getName());
                            }
                            if (s.getName() == "worldleft")
                            {
                                System.out.println(s.getName());
                            }
                            if (s.getName() == "worldright")
                            {
                                System.out.println(s.getName());
                            }
                            if (s.getName() == "bloecke")
                            {
                                int height = getHeight();
                                int width = getWidth();
    
                                GreenfootImage image = new GreenfootImage(width/8*7,height/8*1) ;
                                //image.setColor(Color.BLACK);
                                image.setColor(new Color(0,0,80,25));
                                image.fillRect(0,0,width,height);
                                s.setImage(image);
                                s.setLocation(width/2,50);
                            }
                        }
                    } 
                    
                    //Buttonabfrage für die Levelauswahl oder das Editiren der jeweiligen Level
                    if (!levelButton.isEmpty())
                    {
                    for (Select s: levelButton)
                    {
                        
                        if (object.equals(s))
                        {
                            System.out.println(s.getName());
                            if (s.getName().contains("Play"))
                            {
                                level = new Level(levelSelector.getLevelList().get(0));
                                mode = "ingame";
                                removeEditor();
                                removeLevelMaker();
                            }
                            if (s.getName().contains("Edit"))
                            {
                                if (!edit)
                                {
                                    removeEditor();
                                    editMode();
                                    edit = true;
                                    removeLevelMaker();
                                }
                            }
                        
                        }
                    }
                }
                }
            } 
        }
    }
    
    /**
     * Initalisierung der Editor Buttons in das ArrayList editor
     */
    private void selectIni ()
    {
        editor.add(new Select("bloecke","missingImage.png"));
        editor.add(new Select("stamp","missingImage.png"));
        editor.add(new Select("trashcan","missingImage.png"));
        editor.add(new Select("worldleft","missingImage.png"));
        editor.add(new Select("worldright","missingImage.png"));
    }
    
    /**
     * Initalisirung der Levelauswahl Buttons in die Arrylist levelButton. Für jedes Level ein Play und Edit Button
     */
    private void buttonLevel (String name)
    {
        levelButton.add(new Select(name+"Play","missingImage.png"));
        levelButton.add(new Select(name+"Edit","missingImage.png"));
        System.out.println(name);
    }
    
    /**
     * Erzeugen und setzen der Levelauswahl Buttons
     */
    private void createButtonLevel (List<String> name)
    {
        int width = getWidth()/4*3;
        int height = getHeight()/8;
        int i = 0;
        for (String n :name)
        {
            buttonLevel(n);
        }
        for(Select s:levelButton)
        {
            if ((i % 2 ) == 0) 
            {
                addObject(s,width-50,height+20);
            }
            else
            {
                addObject(s,width-20,height+20);
                height+=30;
            }
            i ++;
        }
    }
    
    /**
     * Startet den Editor modus. Erzeugt werkzeuge.
     */
    private void editMode ()
    {
        int position=getWidth()/2-40;
        for(Select e:editor)
        {
            addObject(e,position,10);
            position +=20;
        }
    }
    /**
     * Setzt den LevelMaker zurück auf den Urzustand
     */
    private void removeLevelMaker ()
    {
        removeObjects(getObjects(LevelMaker.class));
        for (Select s:levelButton)
        {
            removeObject(s);
        }
        levelMaker = new LevelMaker(levelDir);
        addObject(levelMaker,50,10);
        levelMakerdraw=false;
        edit=false;
        levelButton = new ArrayList<Select>(); 
    }
    /**
     * setzt den Editor zurück / die Buttons
     */
    private void removeEditor ()
    {
        for (Select s:editor)
        {
            removeObject(s);
        }
        edit=false;
    }
    
}
