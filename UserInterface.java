import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.File;
import com.google.gson.*;
import java.util.*;
import javax.swing.*;

/**
 * Beinhaltet alle benötigten Objekte und koordiniert Programmablauf
 * 
 * @author Phil Schneider & Simon Kemmesies
 * 
 */
public class UserInterface extends World

{
    private static int defaultWidth = 1024;
    private static int defaultHeight = 786;
    private static final String imageDir = "images";
    private static final String levelDir = "levels";
    
    private String mode;
    private GraphicsManager graphics;
    private LevelLoader levelLoader;
    private Level level;
    private Camera camera;
    private GreenfootImage background;

    private Text fpsCounter;
    private Text entityCounter;
    //Level auswahl
    private LevelMaker levelMaker; 
    //Liste von verschieden Buttons
    private ArrayList<Select> editor = new ArrayList<Select>();
    private ArrayList<Select> levelButton = new ArrayList<Select>();
    //Object zum Ziehen im Editor
    private FloatingEntity floatingEntity;
    //Object für das auswählen der neuen Blöcke im Editor(Button)
    private Select blockChosing;
    //Object für das erstellen von neuen Leveln(Button)
    private Select ingameToEditor;
    //Object für das erstellen von neuen Leveln(Button)
    private Select editorToIngame;
    //Object für das erstellen von neuen Leveln(Button)
    private Select newLevel;
    //Button für die Credits
    private Select btcredits;
    //Buttons für Welt Löschen frage
    private Select field;
    private Select ok;
    private Select cancel;
    private Select objectInter;
    //Scall in Px für die Buttons
    private int buttonScale;
    //gibt die Postion für die Buttons (obere Bildschrimrand) an
    private int buttonYPos;
    //Liste von den Objecten die im Editor hinzugefügt werden können
    private List<FloatingEntity> placeableEntities;
    //Die boolischen Variablen geben an ob etwas Aktiv ist (Button)
    private boolean levelMakerdraw;
    private boolean edit;
    private boolean stampActiv;
    private boolean trashcanActiv;
    private boolean mouseButtonLeft;
    private boolean isDragging;
    private boolean creditShown;
    //Zählvariable die für Cooldown verwendet wird
    private int switchClock =0;
    private int delayTime;
    //Credits Box
    private GreenfootImage imCredit;

    // Quelle: https://www.greenfoot.org/doc/native_loader
    static {
        NativeLoader loader = new NativeLoader();        
        loader.addClasspath("./+libs/gson-2.8.0.jar"); 
        loader.loadClass("com.google.gson.Gson");
    }
    

    /**
     * Wird bei Programmstart aufgerufen und initialisiert wichtige Variablen und Objekte
     * 
     */
    public UserInterface()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(defaultWidth, defaultHeight, 1, false); 
        
        setPaintOrder(Text.class, LevelLoader.class,FloatingEntity.class, Select.class, LevelMaker.class,  Player.class, Koopa.class, Block.class, Special.class );

        // Log "ausleeren"
        for (int i = 0; i < 50; i++)
        { 
            //System.out.println();
        }

        // Graphics-Objekt initialisieren, alle Bilder laden
        graphics = new GraphicsManager(imageDir);  

        // Level Selector
        levelLoader = new LevelLoader(levelDir);

        // Level erstellen

        //level = new Level(levelSelector.getLevelList().get(0));

        // Kamera erstellen
        camera = new Camera(getWidth(), getHeight());
        //addObject(camera, 0,0);

        setBackground(background); 
        
        // Debug Informationen
        fpsCounter = new Text();
        addObject(fpsCounter, 20, 86);
        entityCounter = new Text();
        addObject(entityCounter, 20, 100);
        //addObject(new CameraZones(width, height, 50, 100, 50), width/2, height/2);

        //addObject(new Player(nameP1, graphics.getImage(nameP1, "small", "walking", "right", 0), controlsP1), 100, 100);
        //addObject(new Object("Mystery_Block"), 200, 200);        
        //addObject(new LevelSelector(), 0, 0);

        //JsonObject jsonObject = new JsonParser().parse("{\"name\": \"John\"}").getAsJsonObject();
        //System.out.println(jsonObject.get("name").getAsString());

        //Scale für Buttons berehnen 
        buttonScale = (int)(getHeight()*0.08);
        buttonYPos = (int)(buttonScale/2+getHeight()*0.0125);
        //Initialisieren von der LevelAuswahl und zeichen
        levelMaker = new LevelMaker(levelDir, buttonScale);
        addObject(levelMaker,getWidth()/16*3,30);
        levelMakerhaendler();
        //Button neues Level erstellen und zeichenen
        newLevel = new Select("newLevel",0,"newLevel.png",buttonScale);
        btcredits = new Select("credits",0,"info.png",buttonScale/4*3);
        addObject(newLevel,getWidth()/8*2+(newLevel.getImage().getWidth()/2),buttonYPos);
        //Credits zeichen
        addObject(btcredits,getWidth()/8*2+(btcredits.getImage().getWidth()/2),getHeight()/16*15);

        field = new Select("fieldQuestion",0,"missingImage.png",1);
        ok = new Select("OK",0,"ja.png",40);
        cancel = new Select("Cancel",0,"nein.png",40);
        
        creditShown = false;
        
        mode="init";
        initializeSelect();
        initializePlaceable();
        
    }

    private long lastNanoTime = 0;
    /**
     * Wird einmal pro Frame aufgerufen und weist enthaltene Objekte an, die Welt neu zu berechnen
     */
    public void act()
    {
        //Fenster rescalieren
        if (Greenfoot.isKeyDown("+"))
        {
            if (getWidth() == 1024 && getHeight() == 786)
            {
                defaultWidth = 500;
                defaultHeight = 350;
            }
            else
            {
                defaultWidth = 1024;
                defaultHeight = 786;
            }
            Greenfoot.setWorld(new UserInterface());
        }
        if (Greenfoot.isKeyDown("4"))
        {            
            defaultWidth -= 100;
            Greenfoot.setWorld(new UserInterface());
        }
        if (Greenfoot.isKeyDown("6"))
        {            
            defaultWidth += 100;
            Greenfoot.setWorld(new UserInterface());
        }
        if (Greenfoot.isKeyDown("2"))
        {            
            defaultHeight -= 100;
            Greenfoot.setWorld(new UserInterface());
        }
        if (Greenfoot.isKeyDown("8"))
        {            
            defaultHeight += 100;
            
            Greenfoot.setWorld(new UserInterface());
        }
        
        
        long newNanoTime = System.nanoTime();
        long diff = newNanoTime - lastNanoTime;
        String fps = (int)Math.floor(1/(diff/1000000000.0))+" FPS";
        lastNanoTime = newNanoTime;

        fpsCounter.setText(fps);
        fpsCounter.setLocation(fpsCounter.getImage().getWidth()/2, 86);
 
        if (mode.equals("init"))
        {
            GreenfootImage background = Tools.loadImage("images/levelselection20.png");
            background.scale(getWidth(), getHeight());
            setBackground(background);  
            mode = "levelSelector";
            Tools.playBgm("Map_1.wav", 40);
        }
        else if (mode.equals("levelSelector"))
        {
            
        } 
        else if (mode.equals("ingame"))
        {
            if (level == null)
            {
                level = new Level(levelLoader.getLevelList().get(0), "ingame");
            }

            Tools.checkSound();
            delayTime--;
            
            // Alle Entities vom Bildschirm löschen
            List<Entity> currentEntities = getObjects(Entity.class);
            removeObjects(currentEntities);

            level.update();        
            if (level.isLevelCleared() || level.isLevelLost())
            {
                Entity.resetGlobalCutscene();
                Greenfoot.setWorld(new UserInterface());
            }

            // Kamera-Objekt anweisen, die Position der Entities in der Welt in Bildschirm-Koordinaten umzurechnen
            // nicht sichtbare Entities deaktivieren
            List<Entity> allEntities = level.getEntities();
            camera.calculateCamera(allEntities, "slow");
            camera.calculateEntities(allEntities);
                        
            Entity.globalFrameCounter ++;

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

            // Kollisionsabfrage aller Objekte
            for (Entity entity : allEntities)
            {
                if (entity.isEnabled() && !level.isSimulationPaused() && delayTime<=0)
                {
                    entity.checkCollision(allEntities);                  
                }
            }
            
            level.checkEntities();

            // Objekte simulieren
            for (Entity entity : allEntities)
            {
                // Objekt aktualisieren und zeichnen, wenn es nicht deaktiviert ist
                if (entity.isEnabled() && !level.isSimulationPaused() && delayTime<=0)
                {
                    entity.simulate(allEntities);                    
                }
            } 
            
            // Objekte einzeichnen
            int numberOfEntities = 0;
            for (Entity entity : allEntities)
            {
                if (entity.isEnabled())
                {
                    numberOfEntities++;
                    graphics.setScale(camera.getScale());
                    graphics.setMode("ingame");
                    GreenfootImage image = graphics.getImage(entity.getName(), entity.getState(), entity.getActivity(), entity.getOrientation(), entity.getAnimationIndex());
                    if (entity.isVisible())
                    {
                        image.setTransparency(255);
                    }
                    else
                    {
                        image.setTransparency(0);
                    }
                    entity.setImage(image);
                    entity.calculateExactPos();                       
                    addObject(entity, (int)entity.getCameraX(), (int)entity.getCameraY());                
                }
            }
            
            entityCounter.setText(numberOfEntities+" Entities");    
            entityCounter.setLocation(entityCounter.getImage().getWidth()/2, 100);    
            
        } else if (mode.equals("editor"))   
        {
            if (level == null)
            {
                level = new Level(levelLoader.getLevelList().get(0), "editor");
            }

            // Alle Entities vom Bildschirm löschen
            List<Entity> currentEntities = getObjects(Entity.class);
            removeObjects(currentEntities);
            
            if (Greenfoot.isKeyDown("w"))
            {
                camera.moveY(6);
            }
            if (Greenfoot.isKeyDown("a"))
            {
                camera.moveX(-6);
            }
            if (Greenfoot.isKeyDown("s"))
            {
                camera.moveY(-6);
            }
            if (Greenfoot.isKeyDown("d"))
            {
                camera.moveX(6);
            }
            
            background = new GreenfootImage(getWidth(),getHeight());
            background.setColor(new Color(156,227,231));
            background.fill();
            background.setColor(new Color(255, 255, 255));
            //background.drawLine(camera.getPixelOffsetXBeginning(), 0, camera.getPixelOffsetXBeginning(), getHeight());
            //background.drawLine(camera.getPixelOffsetXEnd(), 0, camera.getPixelOffsetXEnd(), getHeight());
            //background.drawRect(camera.getPixelOffsetXBeginning(), 0, camera.getPixelOffsetXEnd() - camera.getPixelOffsetXBeginning(), getHeight());
            for (int i = 0; i < 100; i++)
            {
                //background.fillRect((int)(camera.getPixelOffsetXBeginning()+16*i*camera.getScale()), 0, (int)(camera.getPixelOffsetXEnd()+16*i*camera.getScale()) - (int)(camera.getPixelOffsetXBeginning()+16*i*camera.getScale()), getHeight());
            }
            
            //background.fillRect(0, getHeight() - camera.getPixelOffsetYEnd(), getWidth(), camera.getPixelOffsetYEnd() - camera.getPixelOffsetYBeginning());
            
            setBackground(background); 

            level.update();        

            // Kamera-Objekt anweisen, die Position der Entities in der Welt in Bildschirm-Koordinaten umzurechnen
            // nicht sichtbare Entities deaktivieren
            List<Entity> allEntities = level.getEntities();
            camera.calculateCameraPos("instant");
            camera.calculateEntities(allEntities);

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
                    graphics.setMode("editor");
                    entity.setImage(graphics.getImage(entity.getName(), entity.getState(), entity.getActivity(), entity.getOrientation(), entity.getAnimationIndex()));
                    entity.calculateExactPos();            
                    addObject(entity, (int)entity.getCameraX(), (int)entity.getCameraY());
                }
            }    
        }
        

        //System.out.println("UserInterface Act")
        if (switchClock<10)
        {
            switchClock+=1;
        }
        //Maus auf Linksklick prüfen
        MouseInfo Maus = Greenfoot.getMouseInfo();
        if (Maus !=null)
        {
            if (Maus.getButton()==1)
            {
                mouseButtonLeft=true;
            }
        }
        //Maus auf Links los gelassen prüfen
        if (Greenfoot.mouseClicked(null))
        {
            mouseButtonLeft=false;
        }
        //Maus aubfrage
        if (mouseButtonLeft)
        {
            checkMous();
        }
        else
        {
            //Fliegendes Object (für Verschiebung) entferenen und löschen
            if (floatingEntity != null)
            {
                if (isDragging)
                {
                    //floatingEntity.setLocation(camera.alignXatGrid(Maus.getX()+(floatingEntity.getImage().getWidth()/2)),camera.alignYatGrid(Maus.getY()-(floatingEntity.getImage().getHeight()/2)));
                    //System.out.println(floatingEntity.getName());
                    EntityData newEntity = new EntityData(floatingEntity.getType(), floatingEntity.getName(), camera.mapToWorldX(Maus.getX())/16*16, camera.mapToWorldY(Maus.getY())/16*16, floatingEntity.getState(), "");
                    level.addObject(newEntity);
                    isDragging= false;
                }
                if (!stampActiv)
                {
                    removeObject(floatingEntity);
                    //floatingEntity.setLocation(camera.alignXatGrid(Maus.getX()+(floatingEntity.getImage().getWidth()/2)),camera.alignYatGrid(Maus.getY()));
                    floatingEntity = null;
                }
            }
        }
        //Stempel um mehrer Objekte setzet zu können
        if (stampActiv)
        {
            
            if (floatingEntity !=null)
            {
                if (Maus !=null)
                {
                    floatingEntity.setLocation(Maus.getX()+50,Maus.getY()+35);
                    if (mouseButtonLeft)
                    {
                        EntityData newEntity = new EntityData(floatingEntity.getType(), floatingEntity.getName(), camera.mapToWorldX(Maus.getX())/16*16, camera.mapToWorldY(Maus.getY())/16*16, floatingEntity.getState(), "");
                        level.addObject(newEntity);
                    }
                }
            }
        }
        
        if (creditShown)
        {
            if (Greenfoot.isKeyDown("escape"))
            {
                removeObject(btcredits);
                btcredits = new Select("credits",0,"info.png",buttonScale/4*3);
                addObject(btcredits,getWidth()/8*2+(btcredits.getImage().getWidth()/2),getHeight()/16*15);
                addObject(newLevel,getWidth()/8*2+(newLevel.getImage().getWidth()/2),buttonYPos);
                creditShown = false;
            }
        }
    }
    
    
    
    /**
     * Eine Mausabfrage welche das händling mit den Actorn ermöglicht.
     */
    private void checkMous()
    {
        int x;
        int y;
        boolean aenderung = false;

        MouseInfo Maus = Greenfoot.getMouseInfo();

        //Prüfen das die Maus einen Actor anglickt
        if (Maus != null)
        {
            //Prüfen welche Taste gedrückt wurde
            //System.out.println("X:"+Maus.getX());
            //System.out.println("Y:"+Maus.getY());
            //System.out.println("Maus:"+Maus.getButton());

            if (mouseButtonLeft)
            {
                //Setz das Fliegende Object, fals dieses Exestiert, an die Position der Maus
                if (floatingEntity != null)
                {
                    //System.out.println(Maus.getX());
                    if (!stampActiv)
                    {
                        floatingEntity.setLocation(Maus.getX(),Maus.getY());
                    }
                    else 
                    {
                        //floatingEntity.setLocation(Maus.getX()+35,Maus.getY()+35);
                    }
                }
                if (Maus.getActor() != null)
                {
                    //Variablen dir für das Arbeiten/das Vergleichen mit Objecten von nöten sind
                    Actor object = Maus.getActor();
                    String name = object.toString();
                    name = name.intern();
                    //System.out.println(name);

                    //prüfen welcher Actor vorliegt
                    if(object instanceof Entity)
                    {
                        if (!trashcanActiv)
                        {
                            aenderung = true;
                        }
                        else
                        {
                            if (object.getClass() != Player.class)
                            {
                                EntityData deleteEntity = new EntityData((Entity)object);
                                level.removeObject(deleteEntity);
                                mouseButtonLeft=false;
                            }
                        }
                    }

                    //ändern der Postition je nach Actor
                    if (aenderung && floatingEntity == null && edit)
                    {
                        isDragging=true;
                        //((Entity)object).remove();
                        EntityData deleteEntity = new EntityData((Entity)object);
                        if (!Greenfoot.isKeyDown("control") || deleteEntity.getType().equals("player"))
                        {
                            level.removeObject(deleteEntity);
                        }                        
                        floatingEntity = new FloatingEntity((Entity)object);
                        addObject(floatingEntity,Maus.getX(),Maus.getY());
                        aenderung = false;
                    }

                    //auf Bewegung bzw. gecklickt werden auf die neuen Blöcke reagieren 
                    for(FloatingEntity f :placeableEntities)
                    {
                        if (object.equals(f))
                        {
                            floatingEntity = f;
                            removeObject(blockChosing);
                            //alle anderen neuen Blöcke löschen
                            for(FloatingEntity m :placeableEntities)
                            {
                                if (!object.equals(m))
                                {
                                    removeObject(m);
                                }
                            }
                            blockChosing = null;
                            isDragging=true;
                            //System.out.println("Test");
                        }
                    }                               

                    //prüfen ob object Levelmaker Angesteuert wird
                    if (!levelMakerdraw)
                    {
                        if (name.contains("LevelMaker"))
                        {
                            //zeichnet den levelmaker und löscht den Editor
                            levelMakerhaendler();
                            removeEditor();
                            removeObject(blockChosing);
                            removeObject(ingameToEditor);
                            addObject(newLevel,getWidth()/8*2+(newLevel.getImage().getWidth()/2),buttonYPos);
                            addObject(btcredits,getWidth()/8*2+(newLevel.getImage().getWidth()/2),getHeight()/16*15);
                        }
                    } 
                    
                    if (object.equals(btcredits))
                    {
                        removeOU();
                        showCredit();
                        creditShown = true;
                        removeObject(newLevel);
                    }
                    
                    if (object.equals(newLevel))
                    {
                        removeOU();
                        String levelName=Greenfoot.ask("Benenne deine Welt: ");
                        if (levelName.equals(""))
                        {

                        }
                        level = new Level("levels", levelName, "Beschreibung");
                        levelButton.clear();
                        buttonLevel(levelLoader.getLevelList());
                        removeLevelMaker();
                        removeObjects(getObjects(Select.class));
                        removeObjects(getObjects(LevelMaker.class));
                        levelMaker=null;
                        levelLoader = new LevelLoader(levelDir);
                        levelMaker = new LevelMaker(levelDir, buttonScale);
                        addObject(levelMaker,getWidth()/16*3,30);
                        levelMakerhaendler();
                        //Button neues Level erstellen und zeichenen
                        newLevel = new Select("newLevel",0,"newLevel.png",buttonScale);
                        addObject(newLevel,getWidth()/8*2+(newLevel.getImage().getWidth()/2),buttonYPos);
                        
                        addObject(newLevel,getWidth()/8*2+(newLevel.getImage().getWidth()/2),buttonYPos);
                        //Credits zeichen
                        addObject(btcredits,getWidth()/8*2+(btcredits.getImage().getWidth()/2),getHeight()/16*15);
                        mouseButtonLeft=false;

                    }

                    //Buttonabfrage für die jeweiligen Editor Buttons
                    for(Select s:editor)
                    {
                        if (object.equals(s))
                        {
                            if (s.getName() == "stamp")
                            {
                                trashcanOut();
                                if (!stampActiv)
                                {
                                    stampActiv = true;
                                    s.setImage(s.scaleImage(Tools.loadImage("images\\stamp_down.png")));;
                                }
                                else
                                {
                                    stampOut();
                                }

                                trashcanActiv =false;
                            }
                            if (s.getName() == "trashcan")
                            {
                                stampOut();
                                if (!trashcanActiv)
                                {
                                    trashcanActiv = true;
                                    s.setImage(s.scaleImage(Tools.loadImage("images\\delete_down.png")));;
                                }
                                else
                                {
                                    trashcanOut();
                                }
                            }
                            if (s.getName() == "zoomin")
                            {
                                camera.zoom(100);
                            }
                            if (s.getName() == "zoomout")
                            {
                                camera.zoom(-100);
                            }
                            if (s.getName() == "bloecke")
                            {
                                stampOut();
                                trashcanOut();
                                if (switchClock ==10)
                                {
                                    if (blockChosing == null )
                                    {
                                        //erstellen von Blockauswahl
                                        blockChosing = new Select("bloeckes",0,"missingImage.png",buttonScale);
                                        blockChosing.setImage(onBlockChoisClick());
                                        addObject(blockChosing,getWidth()/2,getHeight()/16*3);
                                        //erstellen der neuen Blöcke
                                        int i=0;
                                        for(FloatingEntity f :placeableEntities)
                                        {
                                            addObject(f,getWidth()/32*(4+i),getHeight()/32*6);
                                            i+=3;
                                        }
                                    }
                                    else
                                    {
                                        //Blockauswahl schließen
                                        removeObject(blockChosing);
                                        blockChosing = null;
                                        removeObjects(placeableEntities);
                                    }
                                    //dient als cooldown für den Wechsel
                                    switchClock=0;
                                }
                            }
                        }
                    } 
                    
                    if (object.equals(ok))
                    {
                        Tools.deleteFile(levelLoader.getLevelList().get(objectInter.getLevelNumber()));
                        objectInter = null;
                        Greenfoot.setWorld(new UserInterface());
                    }
                    if (object.equals(cancel))
                    {
                        removeOU();
                    }
                    
                    //Buttonabfrage für die Levelauswahl oder das Editiren der jeweiligen Level
                    if (!levelButton.isEmpty())
                    {
                        for (Select s: levelButton)
                        {

                            if (object.equals(s))
                            {
                                if (s.getName().contains("Play"))
                                {
                                    Tools.playBgm("Overworld.wav", 40);
                                    delayTime = 20;
                                    onPlayButtonClicked(s);
                                    removeOU();
                                    
                                }
                                if (s.getName().contains("Edit"))
                                {
                                    onEditButtonClicked(s);
                                    removeOU();

                                }
                                if (s.getName().contains("Delete"))
                                {
                                    deleteQU();
                                    objectInter = s;
                                }
                            }
                        }
                    }
                    if (switchClock == 10)
                    {
                        //Edit aus Play
                        if (object.equals(ingameToEditor))
                        {
                            onEditButtonClicked(ingameToEditor);
                            removeObject(ingameToEditor);
                            ingameToEditor=null;
                            removeObject(blockChosing);
                        }
                        //Play aus dem Editor heraus
                        if (object.equals(editorToIngame))
                        {
                            Tools.playSound("lets_go.wav", 75);
                            Tools.playBgm("Overworld_delay.wav", 40);
                            onPlayButtonClicked(editorToIngame);
                            delayTime = 40;
                            removeObject(editorToIngame);
                            editorToIngame=null;
                            removeObject(blockChosing);
                        }
                        switchClock=0;
                    }
                }
            } 
        }
    }

    /**
     * Initalisierung der Editor Buttons in das ArrayList editor
     */
    private void initializeSelect ()
    {
        editor.add(new Select("bloecke",0,"newBlock.png",buttonScale));
        editor.add(new Select("stamp",0,"stamp.png",buttonScale));
        editor.add(new Select("trashcan",0,"delete.png",buttonScale));
        editor.add(new Select("zoomin",0,"newLevel.png",buttonScale));
        editor.add(new Select("zoomout",0,"deleteLevel.png",buttonScale));
    }

    /**
     * Initialisierung der im Editor zu platzierenden Blöcke
     */
    private void initializePlaceable ()
    {
        placeableEntities = new ArrayList<FloatingEntity>();
        placeableEntities.add(new FloatingEntity("block", "Ground", "grass", Tools.loadImage("images\\Ground\\grass\\grass_single.png"),getHeight()/200));
        placeableEntities.add(new FloatingEntity("block", "Spinblock", "default", Tools.loadImage("images\\Spinblock\\default\\default.png"),getHeight()/200));
        placeableEntities.add(new FloatingEntity("block", "Cloud", "default", Tools.loadImage("images\\Cloud\\default\\default.png"),getHeight()/200));
        placeableEntities.add(new FloatingEntity("block", "Mystery_Block", "yellow", Tools.loadImage("images\\Mystery_Block\\yellow\\spinning\\0.png"),getHeight()/200));
        placeableEntities.add(new FloatingEntity("koopa", "Koopa", "red", Tools.loadImage("images\\Koopa\\red\\walking\\0.png"),getHeight()/200));
        //placeableEntities.add(new FloatingEntity("special", "Mushroom", "default", Tools.loadImage("images\\Mushroom\\default\\default.png"),getHeight()/200));
        placeableEntities.add(new FloatingEntity("special", "Coin", "default", Tools.loadImage("images\\Coin\\default\\default.png"),getHeight()/200));
        placeableEntities.add(new FloatingEntity("special", "Flagpole", "slim", Tools.loadImage("images\\Flagpole\\slim\\top.png"),getHeight()/200));
        
    }

    /**
     * Initalisirung der Levelauswahl Buttons in die Arrylist levelButton. Für jedes Level ein Play und Edit Button
     */
    private void buttonLevel (List<String> name)
    {
        int i = 0;
        for (String n :name)
        {
            levelButton.add(new Select(name+"Play",i,"play-button.png",28));
            levelButton.add(new Select(name+"Edit",i,"wrench.png",28));
            levelButton.add(new Select(name+"Delete",i,"deleteLevel.png",28));
            i++;
        }
    }

    /**
     * Erzeugen und setzen der Levelauswahl Buttons
     */
    private void createLevelButtons (List<String> name)
    {
        int width = getWidth()/4*3;
        int height = getHeight()/8;
        buttonLevel (name);

        int i=0;
        for(Select s:levelButton)
        {
            if (i == 0) 
            {
                addObject(s,width-80,height+20);
                i++;
            }
            else
            {
                if (i==1)
                {
                    addObject(s,width-50,height+20);
                    i++;
                }
                else 
                {
                    if (i==2)
                    {
                        addObject(s,width-20,height+20);
                        height +=30;
                        i=0;
                    }
                }
            }
        }
    }

    /**
     * Startet den Editor modus und erzeugt die Werkzeuge.
     */
    private void editMode ()
    {
        int position=getWidth()/2-(2*buttonScale+(buttonScale/2));
        for(Select e:editor)
        {
            addObject(e,position,buttonYPos);
            position +=buttonScale+10;
        }
    }

    /**
     * Setzt den LevelMaker zurück auf den Urzustand
     */
    private void removeLevelMaker ()
    {
        removeObjects(getObjects(LevelMaker.class));
        removeObject(btcredits);
        removeObject(newLevel);
        removeObjects(levelButton);
        levelMaker = new LevelMaker(levelDir,buttonScale);
        addObject(levelMaker,buttonYPos,buttonYPos);
        levelMakerdraw=false;
        levelButton = new ArrayList<Select>(); 
    }

    /**
     * setzt den Editor zurück / löscht die Buttons
     */
    private void removeEditor()
    {
        for (Select s:editor)
        {
            removeObject(s);
        }
        edit=false;
        removeObject(editorToIngame);
        //löscht alle neuen Blöcke
        for(FloatingEntity m :placeableEntities)
        {
            removeObject(m);
        }
    }

    /**
     * Erstellt die Levelauswahl und löscht die Verschiedenen andere Komponenten (Editor/Level)
     *
     */
    private void levelMakerhaendler()
    {
        //Levelasuwahl zeichen und anzeigen
        levelMaker.createLevelSelector(levelLoader.getLevelList());
        //zeigt an ob die Levelauswahl angezeigt wird
        levelMakerdraw = true;
        //Buttons für jedes Level erstellen (Play und Edit
        createLevelButtons(levelLoader.getLevelList());
        //setzt den Modus auf LevelSelector
        mode="init";
        //Löscht das aktuelle Level
        List<Entity> currentEntities = getObjects(Entity.class);
        removeObjects(currentEntities);
    }

    /**
     * Zeichnet den Ramen für die Blockauswahl
     *
     * @return GreenfootImage welche ein Rechteck welches auf die Groeße der Welt angepasst wird
     */
    private GreenfootImage onBlockChoisClick ()
    {
        int height = getHeight();
        int width = getWidth();
        GreenfootImage image = new GreenfootImage(width/8*7,height/8*1+20);
        image.setColor(new Color(0,0,80,25));
        image.fillRect(0,0,width,height);
        return image;
        //setImage(image);
        //setLocation(width/2,height/8+15);
    }

    /**
     * Wechselt in den Ingame modus und löschtdie alten Elemente und baut die neuen für Ingame
     *
     * @param Object der Klasse Select
     */
    private void onPlayButtonClicked(Select s)
    {
        //Level neu Initalisieren
        Entity.resetGlobalCutscene();
        level = new Level(levelLoader.getLevelList().get(s.getLevelNumber()), "ingame");
        //setzt den Modus auf Ingame
        mode = "ingame";
        //Löscht die Button vom Editor
        removeEditor();
        //Löscht die Levelauswahl
        removeLevelMaker();
        trashcanOut();
        stampOut();
        trashcanActiv=false;
        GreenfootImage image = Tools.loadImage("images/background.png");
        image.scale(getWidth(), getHeight());
        setBackground(image);
        
        //erstellt den Editorbutton im Ingame modus
        ingameToEditor = new Select(s.getName()+"Edit",s.getLevelNumber(),"wrench.png",buttonScale);
        addObject(ingameToEditor,getWidth()-buttonYPos,buttonYPos);
    }

    /**
     * Wechselt in den Editor modus und löscht die alten Elemente und baut die neuen für den Editor
     *
     * @param Object der Klasse Select
     */
    private void onEditButtonClicked(Select s)
    {
        removeEditor();
        //Level neu Initalisieren
        Entity.resetGlobalCutscene();
        level = new Level(levelLoader.getLevelList().get(s.getLevelNumber()), "editor");
        //Erstellt die Button für Editor
        editMode();
        camera.calculateCamera(level.getEntities(), "once");
        //Hintergrundbild des Editors
        background = new GreenfootImage(getWidth(),getHeight());
        background.setColor(new Color(156,227,231));
        background.fill();
        setBackground(background); 
        Tools.playBgm("Donut_Plains.wav", 40);
        //Setzt den Editirmodus
        edit = true;
        //Löscht die Levelauswahl
        removeLevelMaker();
        //Setzt den modus in Editor
        mode="editor";
        //erstellt den Playbutton im Editor
        editorToIngame = new Select(s.getName()+"Play",s.getLevelNumber(),"play-button.png",buttonScale);
        addObject(editorToIngame,getWidth()-buttonYPos,buttonYPos);
    }
    
    private void showCredit ()
    {
        imCredit = new GreenfootImage(getWidth()/2,getHeight()/4*3);
        imCredit.setColor(new Color(189,189,189,255));
        imCredit.fillRect(0,0,getWidth(),getHeight());
        //Schrift festlegen
        Font font = imCredit.getFont();
        font = font.deriveFont(20.0f);
        imCredit.setFont(font);
        imCredit.setColor(Color.BLACK);
        textCredit();
        btcredits.setLocation(getWidth()/4*2,getHeight()/2);
        btcredits.setImage(imCredit);
    }
    
    private void textCredit()
    {
        imCredit.drawString("Creators: ",10,30);
        imCredit.drawString("Simon Kemmesies und Phil Schneider",10,60);
        
        imCredit.drawString("Alle verwendeten Charactere gehören Nintendo",10,150);
        imCredit.drawString("Grafiken von www.spriters-resource.com",10,180);
        imCredit.drawString("Nutzer: MM102, SupaMit, DeeY,",10,210);
        imCredit.drawString("BMSantos, NO Body, Josiah",10,240);
        
        imCredit.drawString("Zum schließen ESCAPE drücken",10,getHeight()/4*3-10);
    }
    
    private void trashcanOut ()
    {
        trashcanActiv = false;
        for (Select d:editor)
        {
            if (d.getName() == "trashcan")
            {
                d.setImage(d.scaleImage(Tools.loadImage("images\\delete.png")));
            }
        }
    }
    
    private void stampOut ()
    {
        stampActiv= false;
        removeObject(floatingEntity);
        for (Select d:editor)
        {
            if (d.getName() == "stamp")
            {
                d.setImage(d.scaleImage(Tools.loadImage("images\\stamp.png")));
            }
        }
        floatingEntity = null;                        
    }
    
    private void deleteQU()
    {
        GreenfootImage deleteQU = new GreenfootImage(300,150);
        deleteQU.setColor(new Color(189,189,189,255));
        deleteQU.fillRect(0,0,getWidth(),getHeight());
        //Schrift festlegen
        Font font = deleteQU.getFont();
        font = font.deriveFont(20.0f);
        deleteQU.setFont(font);
        deleteQU.setColor(Color.BLACK);
        deleteQU.drawString("Level wirklich löschen?",55,30);
        
        addObject(field,getWidth()/2,getHeight()/2);
        field.setImage(deleteQU);
        addObject(ok,getWidth()/2-50,getHeight()/2+20);
        addObject(cancel,getWidth()/2+50,getHeight()/2+20);
        
    }
    
    private void removeOU()
    {
        removeObject(field);
        removeObject(ok);
        removeObject(cancel);
    }
    
}
