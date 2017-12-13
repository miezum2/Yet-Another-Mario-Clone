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
    private static final int width = 1500;
    private static final int height = 900;
    private static final String imageDir = "images";
    private static final String levelDir = "levels";
    
    private String mode;
    private GraphicsManager graphics;
    private LevelLoader levelLoader;
    private Level level;
    private Camera camera;

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
    //Zählvariable die für Cooldown verwendet wird
    private int switchClock =0;

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
        super(width, height, 1, false); 
        
        setPaintOrder(Text.class, LevelLoader.class,FloatingEntity.class, Select.class, LevelMaker.class,  Player.class, Koopa.class, Special.class, Block.class);

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
        camera = new Camera(width, height);
        //addObject(camera, 0,0);

        // Debug Informationen
        fpsCounter = new Text();
        addObject(fpsCounter, 20, 66);
        entityCounter = new Text();
        addObject(entityCounter, 20, 80);
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
        addObject(newLevel,getWidth()/8*2+(newLevel.getImage().getWidth()/2),buttonYPos);

        mode="levelSelector";
        initializeSelect();
        initializePlaceable();
    }

    private long lastNanoTime = 0;
    /**
     * Wird einmal pro Frame aufgerufen und weist enthaltene Objekte an, die Welt neu zu berechnen
     */
    public void act()
    {
        long newNanoTime = System.nanoTime();
        long diff = newNanoTime - lastNanoTime;
        String fps = (int)Math.floor(1/(diff/1000000000.0))+" FPS";
        lastNanoTime = newNanoTime;

        fpsCounter.setText(fps);
        fpsCounter.setLocation(fpsCounter.getImage().getWidth()/2, 66);
 
        if (mode.equals("levelSelector"))
        {
            GreenfootImage background = Tools.loadImage("images/levelselection20.png");
            background.scale(width, height);
            setBackground(background);  

        } else if (mode.equals("ingame"))
        {
            if (level == null)
            {
                level = new Level(levelLoader.getLevelList().get(0), "ingame");
            }

            // Alle Entities vom Bildschirm löschen
            List<Entity> currentEntities = getObjects(Entity.class);
            removeObjects(currentEntities);

            level.update();        
            if (level.mustReset())
            {
                Greenfoot.setWorld(new UserInterface());
            }

            // Kamera-Objekt anweisen, die Position der Entities in der Welt in Bildschirm-Koordinaten umzurechnen
            // nicht sichtbare Entities deaktivieren
            List<Entity> allEntities = level.getEntities();
            camera.calculateCamera(allEntities);
            camera.calculateEntities(allEntities);

            // Neuen Zustand aller Objekte ermitteln
            for (Entity entity : allEntities)
            {
                if (entity.isEnabled())
                {
                    entity.update(allEntities, level.getCurrentCutscene(), level.getCutsceneFrameCounter());
                    graphics.setScale(1);
                    GreenfootImage image = graphics.getImage(entity.getName(), entity.getState(), entity.getActivity(), entity.getOrientation(), entity.getAnimationIndex());
                    entity.setHeightUnits(image.getHeight());
                    entity.setWidthUnits(image.getWidth());                    
                }
            }   

            // Kollisionsabfrage aller Objekte
            for (Entity entity : allEntities)
            {
                if (entity.isEnabled() && !level.isSimulationPaused())
                {
                    entity.checkCollision(allEntities);                  
                }
            }
            
            level.checkEntities();

            // Objekte simulieren
            for (Entity entity : allEntities)
            {
                // Objekt aktualisieren und zeichnen, wenn es nicht deaktiviert ist
                if (entity.isEnabled() && !level.isSimulationPaused())
                {
                    entity.simulate(allEntities);                    
                }
            } 
            
            // Objekte einzeichnen
            for (Entity entity : allEntities)
            {
                if (entity.isEnabled())
                {
                    graphics.setScale(camera.getScale());
                    graphics.setMode("ingame");
                    entity.setImage(graphics.getImage(entity.getName(), entity.getState(), entity.getActivity(), entity.getOrientation(), entity.getAnimationIndex()));
                    entity.calculateExactPos();            
                    addObject(entity, (int)entity.getCameraX(), (int)entity.getCameraY());                
                }
            }
            

            entityCounter.setText(currentEntities.size()+" Entities");
        } else if (mode.equals("editor"))   
        {
            if (level == null)
            {
                level = new Level(levelLoader.getLevelList().get(0), "editor");
            }

            // Alle Entities vom Bildschirm löschen
            List<Entity> currentEntities = getObjects(Entity.class);
            removeObjects(currentEntities);

            level.update();        

            // Kamera-Objekt anweisen, die Position der Entities in der Welt in Bildschirm-Koordinaten umzurechnen
            // nicht sichtbare Entities deaktivieren
            List<Entity> allEntities = level.getEntities();
            camera.calculateEntities(allEntities);

            // Alle Objekte durchgehen
            for (Entity entity : allEntities)
            {
                // Objekt aktualisieren und zeichnen, wenn es nicht deaktiviert ist
                if (entity.isEnabled())
                {
                    entity.update(allEntities, "", 0);
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

        entityCounter.setLocation(entityCounter.getImage().getWidth()/2, 80);

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
            if (stampActiv)
            {
                mouseButtonLeft=true; 
            }
            else
            {
                mouseButtonLeft=false;
            }
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
                if (!isDragging && stampActiv)
                {

                }
                removeObject(floatingEntity);
                //floatingEntity.setLocation(camera.alignXatGrid(Maus.getX()+(floatingEntity.getImage().getWidth()/2)),camera.alignYatGrid(Maus.getY()));
                floatingEntity = null;

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
                //Setz das Flegende Object, fals dieses Exestiert, an die Position der Maus
                if (floatingEntity != null)
                {
                    //System.out.println(Maus.getX());
                    floatingEntity.setLocation(Maus.getX(),Maus.getY());
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
                        }
                    } 

                    if (object.equals(newLevel))
                    {
                        //String levelName=JOptionPane.showInputDialog("Benenne deine Welt: ");
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
                        mouseButtonLeft=false;

                    }

                    //Buttonabfrage für die jeweiligen Editor Buttons
                    for(Select s:editor)
                    {
                        if (object.equals(s))
                        {
                            if (s.getName() == "stamp")
                            {
                                if (!stampActiv)
                                {
                                    stampActiv = true;
                                }
                                else
                                {
                                    stampActiv= false;
                                }

                                trashcanActiv =false;
                            }
                            if (s.getName() == "trashcan")
                            {
                                stampActiv = false;
                                if (!trashcanActiv)
                                {
                                    trashcanActiv = true;
                                }
                                else
                                {
                                    trashcanActiv =false;
                                }
                            }
                            if (s.getName() == "worldleft")
                            {
                                camera.moveX(-96);
                            }
                            if (s.getName() == "worldright")
                            {
                                camera.moveX(96);
                            }
                            if (s.getName() == "bloecke")
                            {
                                if (switchClock ==10)
                                {
                                    if (blockChosing == null )
                                    {
                                        //erstellen von Blockauswahl
                                        blockChosing = new Select("bloeckes",0,"missingImage.png",buttonScale);
                                        blockChosing.setImage(onBlockChoisClick());
                                        addObject(blockChosing,width/2,height/16*3);
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

                    //Buttonabfrage für die Levelauswahl oder das Editiren der jeweiligen Level
                    if (!levelButton.isEmpty())
                    {
                        for (Select s: levelButton)
                        {

                            if (object.equals(s))
                            {
                                if (s.getName().contains("Play"))
                                {
                                    onPlayButtonClicked(s);
                                }
                                if (s.getName().contains("Edit"))
                                {
                                    onEditButtonClicked(s);

                                }
                                if (s.getName().contains("Delete"))
                                {
                                    Tools.deleteFile(levelLoader.getLevelList().get(s.getLevelNumber()));
                                    Greenfoot.setWorld(new UserInterface());
                                }
                            }
                        }
                    }
                    if (switchClock == 10)
                    {
                        //edit
                        if (object.equals(ingameToEditor))
                        {
                            onEditButtonClicked(ingameToEditor);
                            removeObject(ingameToEditor);
                            ingameToEditor=null;
                            removeObject(blockChosing);
                        }
                        //Play
                        if (object.equals(editorToIngame))
                        {
                            onPlayButtonClicked(editorToIngame);
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
        editor.add(new Select("worldleft",0,"arrowleft.png",buttonScale));
        editor.add(new Select("worldright",0,"arrowright.png",buttonScale));
    }

    /**
     * Initialisierung der im Editor zu platzierenden Blöcke
     */
    private void initializePlaceable ()
    {
        placeableEntities = new ArrayList<FloatingEntity>();
        placeableEntities.add(new FloatingEntity("block", "Ground", "grass", Tools.loadImage("images\\Ground\\grass\\grass_single.png"),getHeight()/200));
        placeableEntities.add(new FloatingEntity("block", "Mystery_Block", "yellow", Tools.loadImage("images\\Mystery_Block\\yellow\\spinning\\0.png"),getHeight()/200));
        placeableEntities.add(new FloatingEntity("koopa", "Koopa", "red", Tools.loadImage("images\\Koopa\\red\\walking\\0.png"),getHeight()/200));
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
        mode="levelSelector";
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
        level = new Level(levelLoader.getLevelList().get(s.getLevelNumber()), "ingame");
        //setzt den Modus auf Ingame
        mode = "ingame";
        //Löscht die Button vom Editor
        removeEditor();
        //Löscht die Levelauswahl
        removeLevelMaker();
        trashcanActiv=false;
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
        level = new Level(levelLoader.getLevelList().get(s.getLevelNumber()), "editor");
        //Erstellt die Button für Editor
        editMode();
        camera.calculateCamera(level.getEntities());
        //Hintergrundbild des Editors
        GreenfootImage image = new GreenfootImage(getWidth(),getHeight());
        image.setColor(new Color(156,227,231));
        image.fill();
        setBackground(image);
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

}
