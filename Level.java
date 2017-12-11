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
     * Neues Level anlegen
     */
    public Level(String name, String desc)
    {
        levelInfo = new HashMap<String, String>();
        levelInfo.put("date", "Datum einfuegen");
        levelInfo.put("name", name);
        levelInfo.put("desc", desc);
        
        levelData = new ArrayList<EntityData>();
        EntityData mario = new EntityData("player", "Mario", 100, 100, "small", "");
        levelData.add(mario);
        
        Map<String, Map<String, String>> levelInfoMap = new HashMap<String, Map<String, String>>();
        levelInfoMap.put("levelInfo", levelInfo);
        
        Map<String, List<EntityData>> levelDataMap = new HashMap<String, List<EntityData>>();
        levelDataMap.put("levelData", levelData);
        
        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
        
        String info = gsonBuilder.toJson(levelInfo);
        info = info.substring(0, info.length() - 1).trim()+",";
        System.out.println(info);
        
        
        //String test = gsonBuilder.toJson(testArray, EntityData[].class);
        //System.out.println(test);
        
        
        
        
        
    
    }
    
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
                Entity newEntity = new Player(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage(), entity.getState());
                entities.add(newEntity);
            }
            
            // Typ: Koopa
            if (entity.getType().equals("koopa"))
            {
                Entity newEntity = new Koopa(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage(), entity.getState());
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
                        Entity newEntity = new Block(entity.getName(), "0", entity.getX(), i, graphics.getImage(), entity.getState());
                        entities.add(newEntity);
                    }
                }
                else
                {
                    // Standardprozedur für Blöcke
                    Entity newEntity = new Block(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage(), entity.getState());
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
        // Objekte löschen, die im letzten Durchlauf entfernt wurden
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
    }
}
