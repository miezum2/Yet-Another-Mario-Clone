import greenfoot.*;
import java.io.File;
import com.google.gson.*;
import java.util.*;

/**
 * Write a description of class Level here.
 * 
 * @author Simon Kemmesies
 */
public class Level  
{
    private String path;
    private String mode;
    private GraphicsManager graphics;
    private Map<String, String> levelInfo;
    // Start- und Speicherzustand des Levels
    private List<EntityData> levelData;
    // Aktueller Zustand des Levels
    private List<Entity> entities;     
    private boolean levelCleared = false;
    private boolean levelLost = false;
    private boolean simulationPaused = false;
    private static Entity newEntity;
    
    private String[] controlsP1 = {"w","a","s","d","w","q","shift"};
    private String[] controlsP2 = {"up", "left", "down", "right", "up", "v", "control"};
    
    
    /**
     * neu erstelltes Level lädt Json-Datei mit angegebenem Pfad in Objekte 
     */
    public Level(String path, String mode)
    {        
        this.path = path;
        this.mode = mode;
        graphics = new GraphicsManager("images");
        
        String fileContent = Tools.getFileContent(path);
                
        //graphics = new GraphicsManager();        
        
        // Dateiinhalt verarbeiten
        // Gson-Objekte erstellen
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        
        // Levelinfo extrahieren
        levelInfo = new HashMap<String, String>();
        JsonObject levelInfoObject = parser.parse(fileContent)
                        .getAsJsonObject().getAsJsonObject("levelInfo");
        
        // Quelle: https://stackoverflow.com/questions/2779251/how-can-i-convert-json-to-a-hashmap-using-gson
        // Nutzer: Angel
        levelInfo = (Map<String, String>) 
                        gson.fromJson(levelInfoObject, levelInfo.getClass());
        //Tools.log(levelInfo.get("name"), "level");
        
        // Leveldata extrahieren
        String levelDataString = parser.parse(fileContent)
            .getAsJsonObject().getAsJsonArray("levelData").toString();
        //System.out.println(levelDataString.replace("},{", "\n"));
        
        // Quelle: https://stackoverflow.com/questions/5554217/google-gson-deserialize-listclass-object-generic-type
        // Nutzer: DevNG
        EntityData[] entityArray = gson.fromJson(levelDataString, EntityData[].class);
        levelData = new ArrayList<>(Arrays.asList(entityArray));         
        
        EntityData[] testArray = new EntityData[levelData.size()];
        testArray = levelData.toArray(testArray);        
        
        generateEntities();
        
        //entities = levelData;
        //System.out.println(entities == levelData);
        //System.out.println(levelData.equals(entities));
    }
    
    /**
     * Neues Level anlegen und einige Entities darin platzieren
     */
    public Level(String path, String name, String desc)
    {
        levelInfo = new HashMap<String, String>();
        levelInfo.put("date", "Datum einfuegen");
        levelInfo.put("name", name);
        levelInfo.put("desc", desc);
        
        levelData = new ArrayList<EntityData>();
        EntityData mario = new EntityData("player", "Mario", 80, 32, "small", "");
        levelData.add(mario);
        
        // Gras generieren
        for (int i = 0; i < 16*18; i += 16)
        {
            EntityData block1 = new EntityData("block", "Ground", i, 0, "grass", "");
            EntityData block2 = new EntityData("block", "Ground", i, 16, "grass", "");
            levelData.add(block1);
            levelData.add(block2);
        } 
        
        // nächsten freien Levelnamen suchen
        File[] levels = Tools.getDirContent(path, "file");
        int highestIndex = 0;
        for (File level : levels)
        {
            int fileIndex = Tools.parseToInt(level.getName().replace("level", "").replace(".json", ""));
            if (fileIndex > highestIndex)
            {
                highestIndex = fileIndex;
            }
        }
        
        this.path = path+"\\level"+String.format("%04d", (highestIndex+1))+".json";
        save();
    }
    
    /**
     * Level in Datei bei Pfad speichern
     * Datei erstellen, wenn sie noch nicht existiert
     */
    private void save()
    {
        // Json-kompatibles Objekt für levelInfo erstellen
        Map<String, Map<String, String>> levelInfoMap = new HashMap<String, Map<String, String>>();
        levelInfoMap.put("levelInfo", levelInfo);
        
        // Json-kompatibles Objekt für levelInfo erstellen
        Map<String, List<EntityData>> levelDataMap = new HashMap<String, List<EntityData>>();
        levelDataMap.put("levelData", levelData);
        
        // gsonBuilder zum Export erstellen
        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
        
        // Json-String für LevelInfo erstellen
        String info = gsonBuilder.toJson(levelInfoMap);
        info = info.trim().substring(0, info.length() - 1).trim()+",";
        //System.out.println(info);
        
        // Json-String für LevelData erstellen        
        String data = gsonBuilder.toJson(levelDataMap);
        data = data.substring(1, data.length());
        //System.out.println(data);        
        
        //String test = gsonBuilder.toJson(testArray, EntityData[].class);
        //System.out.println(info+data);
        // Json in Datei schreiben
        // und Datei anlegen, falls nicht existiert
        Tools.writeFile(path, info+data);
    }
        
    /**
     * Entities aus geladenen Objekten erstellen
     */
    private void generateEntities()
    {
        entities = new ArrayList<Entity>();
        // entities aus levelData erzeugen
        
        for (EntityData entity: levelData)
        {
            // neuen Entity erstellen           
            //System.out.println(entity.getType()+" - "+entity.getName());
            
            // Typ: player
            if (entity.getType().equals("player"))
            {
                Entity newEntity;
                if (entity.getName().equals("Mario"))
                {
                    newEntity = new Player(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage(), entity.getState(), "", controlsP1);
                }
                else
                {
                    newEntity = new Player(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage(), entity.getState(), "", controlsP2);
                }
                entities.add(newEntity);
            }
            
            // Typ: Koopa
            if (entity.getType().equals("koopa"))
            {
                Entity newEntity = new Koopa(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage(), entity.getState(), "");
                entities.add(newEntity);
            }
            
            // Typ: block
            if (entity.getType().equals("block"))
            {
                Entity newEntity = new Block(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage(), entity.getState(), "default");
                entities.add(newEntity);                
            }
            
            // Typ: special
            if (entity.getType().equals("special"))
            {
                // Name: Flagpole
                if (entity.getName().equals("Flagpole"))
                {
                    if (mode.equals("editor"))
                    {
                        Entity newEntity = new Special(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage(), entity.getState(), "top");
                        entities.add(newEntity);
                    }
                    else
                    {
                        // komplette Zielflagge generieren
                        int i;
                        for (i = 0; i < 6; i++)
                        {
                            Entity newEntity = new Special(entity.getName(), "0", entity.getX(), entity.getY()+i*16, graphics.getImage(), "wide", "middle");
                            entities.add(newEntity);
                        }
                        Entity newEntity = new Special(entity.getName(), "0", entity.getX(), entity.getY()+i*16, graphics.getImage(), "wide", "top");
                        entities.add(newEntity); 
                        newEntity = new Special(entity.getName(), "0", entity.getX()+8, entity.getY(), graphics.getImage(), "wide", "bar");
                        entities.add(newEntity);
                        
                        // Gras unter der Zielflagge
                        for (i = 0; i < 18; i++)
                        {
                            for (int j = entity.getY()-16; j >= 0; j -= 16)
                            {
                                newEntity = new Block("Ground", "0", entity.getX()+16*i, j, graphics.getImage(), "grass", "");
                                entities.add(newEntity);
                            }
                        }
                    }
                }
                else
                {
                    // alle anderen Specials
                    Entity newEntity = new Special(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage(), entity.getState(), "default");
                    entities.add(newEntity);
                }
            }
        }        
        /*
        System.out.println(levelData.get(0).getType());
        System.out.println(entities.get(0).getType());
        System.out.println(levelData.get(0).equals(entities.get(0))); */
    }
    
    /**
     * aktuelle Entities der Welt zurückgeben
     */
    public List<Entity> getEntities()
    {
        return entities;
    }
    
    /**
     * Entities löschen, die im letzten Durchlauf entfernt wurden
     */
    public void update()
    {
        if (mode.equals("ingame"))
        {
            // Nutzereingaben prüfen  
            Player mario = null;
            Player luigi = null;
            for (Entity listEntity : entities)
            {
                if (listEntity.getName().equals("Luigi"))
                {
                    luigi = (Player)listEntity;
                }
                if (listEntity.getName().equals("Mario"))
                {
                    mario = (Player)listEntity;
                }
            }
        
            if (Greenfoot.isKeyDown(controlsP1[0]) ||
                Greenfoot.isKeyDown(controlsP1[1]) ||
                Greenfoot.isKeyDown(controlsP1[2]) ||
                Greenfoot.isKeyDown(controlsP1[3]) ||
                Greenfoot.isKeyDown(controlsP1[4]) ||
                Greenfoot.isKeyDown(controlsP1[5]) ||
                Greenfoot.isKeyDown(controlsP1[6]))
                
            {
                if (mario == null && luigi != null && luigi.getCurrentCutscene().equals(""))
                {
                    //String name, String id, double x, double y, GreenfootImage image, String state, String activity, String[] controls
                    Player newMario = new Player("Mario", "0", luigi.getPosX(), luigi.getPosY()+64, graphics.getImage(), "small", "standing", controlsP1);
                    newMario.setCurrentCutscene("spawning");
                    newMario.setCutsceneFrameCounter(0);
                    entities.add(newMario);
                    Tools.playSound("smw_reserve_item_release.wav", 100);
                }                
            }
            
            if (Greenfoot.isKeyDown(controlsP2[0]) ||
                Greenfoot.isKeyDown(controlsP2[1]) ||
                Greenfoot.isKeyDown(controlsP2[2]) ||
                Greenfoot.isKeyDown(controlsP2[3]) ||
                Greenfoot.isKeyDown(controlsP2[4]) ||
                Greenfoot.isKeyDown(controlsP2[5]) ||
                Greenfoot.isKeyDown(controlsP2[6]))
                
            {
                if (luigi == null && mario != null && mario.getCurrentCutscene().equals(""))
                {
                    //String name, String id, double x, double y, GreenfootImage image, String state, String activity, String[] controls
                    Player newLuigi = new Player("Luigi", "0", mario.getPosX(), mario.getPosY()+64, graphics.getImage(), "small", "standing", controlsP2);
                    newLuigi.setCurrentCutscene("spawning");
                    newLuigi.setCutsceneFrameCounter(0);
                    entities.add(newLuigi);
                    Tools.playSound("smw_reserve_item_release.wav", 100);
                }
            }
        }
        
        // Quelle: https://stackoverflow.com/questions/18448671/how-to-avoid-concurrentmodificationexception-while-removing-elements-from-arr
        // Nutzer: arshajii
        Iterator<Entity> iter = entities.iterator();
        while (iter.hasNext()) {
            Entity entity = iter.next();
        
            if (entity.isRemoved())
            {
                iter.remove();
            }          
        }      
        
        // neues Item einfügen
        if (newEntity != null)
        {
            entities.add(newEntity);
            newEntity = null;
        }
    }
    
    public static void addEntity(Entity entity)
    {
        newEntity = entity;
    }
    
    /**
     * prüfen, ob bei der letzten Kollisionsabfrage eine Cutscene ausgelöst oder das Level beendet wurde
     */
    public void checkEntities()
    {
        simulationPaused = false;
        if (!Entity.getGlobalCutscene().equals(""))
        {
            simulationPaused = true;
        }       
        
        // Anzahl der Spieler zählen
        int playerCount = 0;
        for (Entity listEntity : entities)
        {
            if (listEntity.getClass() == Player.class)
            {
                playerCount ++;
            }
        }
        
        for (Entity listEntity : entities)
        {    
            if (playerCount == 1 && listEntity.isDead())
            {
                levelLost = true;
            }
            if (listEntity.getCurrentCutscene().equals("victoryDone"))
            {
                levelCleared = true;
            }
            if(playerCount == 1 && (listEntity.getCurrentCutscene().equals("edge") || listEntity.getCurrentCutscene().equals("dying") || listEntity.getCurrentCutscene().equals("deadWait")))
            {
                simulationPaused = true;
            }
            if (listEntity.getCurrentCutscene().equals("growing"))
            {
                simulationPaused = true;
            }
        }
    }
    
    
    /**
     * gibt aktuelle Cutscene zurück
     */
    /*
    private int counter;
    public String getCurrentCutscene()
    {
        counter = 0;
        for (Entity listEntity : entities)
        {
            if (!listEntity.getCurrentCutscene().equals(""))
            {
                counter = listEntity.getCutsceneFrameCounter();
                return listEntity.getCurrentCutscene();
            }
        }
        return "";
    }
    
    public int getCutsceneFrameCounter()
    {
        /*for (Entity listEntity : entities)
        {
            if (listEntity.getCutsceneFrameCounter() != 0)
            {
                return listEntity.getCutsceneFrameCounter();
            }
        }
        return counter;
        //return 0;
    }
    */
    /**
     * prüft, ob gerade eine Cutscene läuft
     */
    public boolean isSimulationPaused()
    {
        return simulationPaused;
    }
    
    /**
     * prüft, ob Level geschafft wurde
     */
    public boolean isLevelCleared()
    {
        return levelCleared;
    }
    
    /**
     * prüft, ob das Level verloren wurde
     */
    public boolean isLevelLost()
    {
        return levelLost;
    }    
    
    /**
     * neues Objekt in die Welt hinzufügen
     */
    public void addObject(EntityData entity)
    {
        // prüfen, ob bereits ein Entity an dieser Stelle ist
        EntityData entityAtThisPlace = null;
        for (EntityData listEntity : levelData)
        {
            if (listEntity.getX() == entity.getX() && listEntity.getY() == entity.getY())
            {
                entityAtThisPlace = listEntity;
            }
        }
                
        // Objekt löschen
        if (entityAtThisPlace == null)
        {
            // neues Objekt hinzufügen
            levelData.add(entity);
            save();
            generateEntities();   
        }
        else
        {
            if (!entityAtThisPlace.getType().equals("player"))
            {
                removeObject(entityAtThisPlace);
                
                // neues Objekt hinzufügen
                levelData.add(entity);
                save();
                generateEntities();
            }        
        }       
        
        
    }
    
    /**
     * Objekt aus der Welt löschen
     */
    public void removeObject(EntityData entity)
    {
        int removalIndex = -1;
        Iterator<EntityData> iter = levelData.iterator();
        while (iter.hasNext()) {
            EntityData listEntity = iter.next();
        
            if (listEntity.equals(entity))
            {
                removalIndex = levelData.indexOf(listEntity);
            }
        }   
        if (removalIndex != -1)
        {
            levelData.remove(removalIndex);
            save();
            generateEntities();
        }
    }
}
