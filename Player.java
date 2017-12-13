import greenfoot.*;
import java.util.*;

/**
 * verwaltet alle Spieler und verarbeitet Tastatureingaben
 */
public class Player extends Entity
{
    private Movement movement;
    private String[] controls;  
    private boolean leftDown = false;
    private boolean rightDown = false;
    private boolean jumpabel = true;
    private boolean directionChange=false;   
       
    /**
     * erstellt neuen Player mit den wichtigsten Eigenschaften und nimmt zu prüfende Tasten entgegen
     */
    public Player(String name, String id, double x, double y, GreenfootImage image, String state, String activity, String[] controls)
    {
        super(name, id, x, y, image, state, activity);
        movement = new Movement(0, 0.1);
        setOrientation("right");
        setActivity("standing");
        this.controls=controls;
        setCurrentCutscene("");        
        setCutsceneFrameCounter(0);
    } 

    public void act()
    {
        
    }   
    
    public void update(List<Entity> entities, String currentCutscene, int cutsceneFrameCounter)
    {
        super.update(entities, currentCutscene, cutsceneFrameCounter);
        
        if (!getCurrentCutscene().equals(""))
        {
            if (getCurrentCutscene().equals("dying"))
            {
                if (getCutsceneFrameCounter() > 30 && getCutsceneFrameCounter()/4 % 2 == 0)
                {
                    setOrientation("right");
                }
                else
                {
                    setOrientation("left");
                }
                
                if (getCutsceneFrameCounter() > 30 && getCutsceneFrameCounter() < 50)
                {
                    setPosY(getPosY()+4);
                }
                
                if (getCutsceneFrameCounter() >= 50)
                {
                    setPosY(getPosY()-4);
                }
                
                if (getPosY() + getHeightUnits() < 0)
                {
                    setCurrentCutscene("wait");
                    setCutsceneFrameCounter(0);
                }                
            }
            
            if (getCurrentCutscene().equals("edge"))
            {
                if (getCutsceneFrameCounter() > 200)
                {
                    setCurrentCutscene("dead");
                }
            }
            
            if (getCurrentCutscene().equals("wait"))
            {
                if (getCutsceneFrameCounter() > 50)
                {
                    setCurrentCutscene("dead");
                }
            }  
           
            if (getCurrentCutscene().equals("victory"))
            {
                if (getCutsceneFrameCounter() > 300)
                {
                    setActivity("victory");
                }
                else
                {
                    setPosX(getPosX()+1);
                    setAnimationIndex(getFrameCounter()/5);
                }
            }
        }    
        else
        {
            if (currentCutscene.equals("victory"))
            {
                setCurrentCutscene("victory");
                setCutsceneFrameCounter(0);                
            }
            
        }
    }
    
    public void checkCollision(List<Entity> entities)
    {
        movement.setEntities(entities);
              
        
        //System.out.println("check Koopa");
        if (movement.isTouchingObjectBelow(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Koopa.class))
        {
            setActivity("jumping");
            movement.setY(2.5);
            jumpabel=true;            
        }    
        else
        {
            // Spieler wird von Koopa verletzt
            if (movement.isTouchedByObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Koopa.class))
            {                    
                setCurrentCutscene("dying");
                setActivity("dying");
                setCutsceneFrameCounter(0);
            }
        }
        
        if (getPosY() + getHeightUnits() <= 0) 
        {
            setCurrentCutscene("edge");            
            setCutsceneFrameCounter(0);    
            
        }
        
    }
    
    /**
     * Tasteneingaben auswerten und Spieler dementsprechend bewegen
     */
    public void simulate(List<Entity> entities)
    {
        // Aktuelle Welt an Movement übergeben, um Kollisionsprüfung zu erlauben
        movement.setEntities(entities);         
        //jumpabel = true fals er den Boden berührt
        jumpabel = movement.isTouchingObjectBelow(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class);
        
        //sprinten
        if (Greenfoot.isKeyDown("shift"))
        {
            movement.setMaxSpeed(2.5);
        }
        else
        {
            movement.setMaxSpeed(1.5);
        }
        
        //Bewegung nach links
        
        if(Greenfoot.isKeyDown(controls[1]))
        {
            //nach rechts nicht gedückt
            if (!rightDown)
            {
                //neue Position setzten 
                setPosX(movement.move(180, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                leftDown = true;
                setOrientation("left");
                if ((getActivity()!="jumping"))
                {
                    setActivity("walking");
                }
            }
            if (movement.getSpeed()>0)
            {
                directionChange=true;
            }
        }
        else
        {
            //prüfen fals Spieler gegen die Wand läuft
            if (movement.isTouchingLeftObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
                {
                    movement.setSpeed(0);
                    leftDown = false;
                }
                else
                {
                    if (leftDown)
                    {
                        //Richtungsänderung wärenden des Laufen
                        if (movement.getSpeed() < 0 )
                        {
                            setPosX(movement.move(0, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                            if ((movement.getSpeed()<1) && (movement.getSpeed()<0) && directionChange)
                            {
                                if (movement.isTouchingObjectBelow(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
                                {   
                                    setActivity("braking");
                                }
                                setOrientation("right");
                            }
                        }
                        else
                        {
                            movement.setSpeed(0);
                            directionChange=false;
                            leftDown = false;
                        }
                    }
                }
        }
        if(Greenfoot.isKeyDown(controls[2]))
        {
            
        }
        //Bewegung nach rechts
        if(Greenfoot.isKeyDown(controls[3]))
        {
            //nach links nicht gedückt
            if (!leftDown)
            {
                //neue Position setzten 
                setPosX(movement.move(0, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                rightDown = true;
                setOrientation("right");
                if ((getActivity()!="jumping"))
                {
                    setActivity("walking");
                }
            }
            if (movement.getSpeed()<0)
            {
                directionChange=true;
            }
        }
        else
        {
            //prüfen fals Spieler gegen die Wand läuft
            if (movement.isTouchingRightObject(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
            {
                movement.setSpeed(0);
                rightDown = false;
            }
            else
            {
                if (rightDown)
                {
                    //Richtungsänderung wärenden des Laufen
                    if (movement.getSpeed() > 0 )
                    {
                        setPosX(movement.move(180, getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                        if ((movement.getSpeed()<1) && (movement.getSpeed()>0) && directionChange)
                        {
                            if (movement.isTouchingObjectBelow(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
                            {   
                                setActivity("braking");
                            }
                            setOrientation("left");
                        }
                    }
                    else
                    {
                        movement.setSpeed(0);
                        rightDown = false;
                        directionChange=false;
                    }
                }
            }
        }
        //Sprung
        if(Greenfoot.isKeyDown(controls[4]))
        {
            //prüft ob Spiele Springen darf, also am Boden angekommen ist
            if (jumpabel)
            {
                {
                    /*
                    if (jumpCount==5)
                    {
                       setPosY(getPosY() + movement.jump(3)); 
                       jumpCount=0;
                    }
                    else
                    */
                    {
                        setPosY(getPosY() + movement.jump(1));
                        setActivity("jumping");
                    }
                    jumpabel = false;
                }
            }
        }
        
        if (movement.getYMove()==0 && movement.getSpeed()==0 && movement.isTouchingObjectBelow(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
        {
            setActivity("standing");
            
        }
        
        if (movement.getYMove()<0)
        {
            setActivity("falling");
        }
        setPosY(movement.gravity(getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
        if (Math.abs(movement.getSpeed()) <= 2)
        {
            setAnimationIndex(getFrameCounter()/5);
        }
        else
        {
            setAnimationIndex(getFrameCounter()/3);
        }
        
        
    }
}
