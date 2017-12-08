import greenfoot.*;
import java.io.File;
import com.google.gson.*;
import java.util.*;

/**
 * Write a description of class Level here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Level  
{
    private String path;
    private GraphicsManager graphics;
    private Map<String, String> levelInfo;
    // Start- und Speicherzustand des Levels
    private List<EntityData> levelData;
    // Aktueller Zustand des Levels
    private List<Entity> entities; 
    
    public Level(String path)
    {        
        graphics = new GraphicsManager();
        
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
        //System.out.println(levelDataString);
        
        // Quelle: https://stackoverflow.com/questions/5554217/google-gson-deserialize-listclass-object-generic-type
        // Nutzer: DevNG
        EntityData[] entityArray = gson.fromJson(levelDataString, EntityData[].class);
        levelData = new ArrayList<>(Arrays.asList(entityArray));         
        
        generateEntities();
        
        //entities = levelData;
        //System.out.println(entities == levelData);
        //System.out.println(levelData.equals(entities));
    }
    
    private void generateEntities()
    {
        entities = new ArrayList<Entity>();
        // entities aus levelData erzeugen
        
        for (EntityData entity: levelData)
        {
            // neuen Entity erstellen           
            System.out.println(entity.getType()+" - "+entity.getName());
            
            // Typ: player
            if (entity.getType().equals("player"))
            {
                Entity newEntity = new Player(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage());
                entities.add(newEntity);
            }
            
            // Typ: block
            if (entity.getType().equals("block"))
            {
                // Spezialfälle bearbeiten
                // Name: Ground
                if (entity.getName().equals("Ground"))
                {
                    // Wenn ein Ground-Block gefunden, dann Welt bis zum Boden mit Ground-Blöcken füllen
                    for (double i = entity.getY(); i >= 0; i -= 16)
                    {
                        Entity newEntity = new Block(entity.getName(), "0", entity.getX(), i, graphics.getImage());
                        entities.add(newEntity);
                    }
                }
                else
                {
                    // Standardprozedur für Blöcke
                    Entity newEntity = new Block(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage());
                    entities.add(newEntity);
                }
            }
        }        
        /*
        System.out.println(levelData.get(0).getType());
        System.out.println(entities.get(0).getType());
        System.out.println(levelData.get(0).equals(entities.get(0))); */
    }
    
    public List<Entity> getEntities()
    {
        return entities;
    }
    
    public void update()
    {
        // in Entities nach Mario suchen
        Player mario = new Player();
        Player luigi = new Player();
        for (Entity entity : entities)
        {
            
            if (entity.getName().equals("Mario"))
            {
                mario = (Player)entity;
            }
            if (entity.getName().equals("Luigi"))
            {
                luigi = (Player)entity;
            }
            
        }
        
        int speed = 3;        
        if(Greenfoot.isKeyDown("w"))
        {
            mario.setPosY(mario.getPosY()+speed);
        }
        if(Greenfoot.isKeyDown("a"))
        {
            mario.setPosX(mario.getPosX()-speed);
        }
        if(Greenfoot.isKeyDown("s"))
        {
            mario.setPosY(mario.getPosY()-speed);
        }
        if(Greenfoot.isKeyDown("d"))
        {
            mario.setPosX(mario.getPosX()+speed);
        }
               
        if(Greenfoot.isKeyDown("up"))
        {
            luigi.setPosY(luigi.getPosY()+speed);
        }
        if(Greenfoot.isKeyDown("left"))
        {
            luigi.setPosX(luigi.getPosX()-speed);
        }
        if(Greenfoot.isKeyDown("down"))
        {
            luigi.setPosY(luigi.getPosY()-speed);
        }
        if(Greenfoot.isKeyDown("right"))
        {
            luigi.setPosX(luigi.getPosX()+speed);
        }
        
    }
}
