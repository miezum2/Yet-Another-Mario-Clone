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
        this.path = path;
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
    public Level(String path, String name, String desc)
    {
        levelInfo = new HashMap<String, String>();
        levelInfo.put("date", "Datum einfuegen");
        levelInfo.put("name", name);
        levelInfo.put("desc", desc);
        
        levelData = new ArrayList<EntityData>();
        EntityData mario = new EntityData("player", "Mario", 96, 32, "small", "");
        EntityData block1 = new EntityData("block", "Ground", 96, 16, "grass", "");
        EntityData block2 = new EntityData("block", "Ground", 96, 0, "grass", "");
        levelData.add(mario);
        levelData.add(block1);
        
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
        
        this.path = path+"\\level"+(highestIndex+1)+".json";
        save();
    }
    
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
                    Entity newEntity = new Block(entity.getName(), "0", entity.getX(), entity.getY(), graphics.getImage(), entity.getState());
                    entities.add(newEntity);
                    // Wenn ein Ground-Block gefunden, dann Welt bis zum Boden mit Ground-Blöcken füllen
                    for (double i = entity.getY(); i >= 0; i -= 16)
                    {
                        //Entity newEntity = new Block(entity.getName(), "0", entity.getX(), i, graphics.getImage(), entity.getState());
                        //entities.add(newEntity);
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
    
    public void addObject(EntityData entity)
    {
        levelData.add(entity);
        System.out.println(path);
        save();
        generateEntities();
    }
    
    public void removeObject(EntityData entity)
    {
        for (EntityData listEntity : levelData)
        {
            if (listEntity.equals(entity))
            {
                levelData.remove(listEntity);
            }
        }
    }
}
