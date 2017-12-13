/**
 * Klasse zum speichern der Daten eines Entities
 * selbe Struktur wie die Json-Objekte in den Leveldateien ermöglicht einfaches Laden und speichern
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
    
    /**
     * Eigenschaften aus Entity auslesen
     */
    public EntityData(Entity entity)
    {
        if (entity.getClass() == Block.class)
        {
            type = "block";
        }
        else if (entity.getClass() == Player.class)
        {
            type = "player";
        }
        else if (entity.getClass() == Koopa.class)
        {
            type = "koopa";
        }
        else if (entity.getClass() == Special.class)
        {
            type = "special";
        }
        
        name = entity.getName();
        x = (int)entity.getPosX();
        y = (int)entity.getPosY();
        state = entity.getState();
        data = entity.getData();
    }
    
    /**
     * wichtige Eigenschaften übergeben
     */
    public EntityData(String type, String name, int x, int y, String state, String data)
    {
        this.type = type;
        this.name = name;
        this.x = x;
        this.y = y;
        this.state = state;
        this.data = data;
    }
    
    /**
     * auf Gleichheit mit anderem Entity prüfen
     */
    public boolean equals(EntityData entity)
    {
        return entity.getType().equals(type)
            && entity.getName().equals(name)
            && entity.getX() == x
            && entity.getY() == y
            && entity.getState().equals(state)
            && entity.getData().equals(data);
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
