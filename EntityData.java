/**
 * Klasse zum speichern der Daten eines Entities
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EntityData  
{
    private String type;
    private String name;
    private int x;
    private int y;
    private String state;
    private String data;
    
    public EntityData(EntityData newEntity)
    {
        this.type = newEntity.getType();
        this.name = newEntity.getName();
        this.x = newEntity.getX();
        this.y = newEntity.getY();
        this.state = newEntity.getState();
        this.data = newEntity.getData();
    }
    
    public EntityData(String type, String name, int x, int y, String state, String data)
    {
        this.type = type;
        this.name = name;
        this.x = x;
        this.y = y;
        this.state = state;
        this.data = data;
    }
    
    public EntityData()
    {
        type = "";
        name = "";
        x = 0;
        y = 0;
        state = "";
        data = "";
    }
    
    public String getType() 
    { 
        return type; 
    }
    
    public String getName() 
    { 
        return name; 
    }
    
    public int getX() 
    { 
        return x; 
    }
    
    public int getY() 
    { 
        return y; 
    }
    
    public String getState() 
    {
        return state; 
    }
        
    public String getData() 
    { 
        return data; 
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public void setY(int Y)
    {
        this.y = Y;
    }
}
