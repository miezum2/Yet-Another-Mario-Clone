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
    private boolean invincible = false;
    private int recoveryTimer = -1;
    private int jumpCount;
    
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
        if (jumpCount<=8)
        {
            jumpCount++;
        }
    }   
    
    public void update(List<Entity> entities)
    {
        super.update(entities);
        movement.setEntities(entities);
        
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
                    setCurrentCutscene("deadWait");                    
                    setCutsceneFrameCounter(0);
                }                
            }
            
            if (getCurrentCutscene().equals("edge"))
            {
                if (getCutsceneFrameCounter() > 200)
                {
                    setCurrentCutscene("dead");
                    remove();
                }
            }
            
            if (getCurrentCutscene().equals("deadWait"))
            {
                if (getCutsceneFrameCounter() > 120)
                {
                    setCurrentCutscene("dead");
                    remove();
                }
            }  
           
            if (getCurrentCutscene().equals("victory"))
            {
                if (getCutsceneFrameCounter() > 450)
                {
                    setActivity("victory");
                }               
                else
                {
                    setActivity("walking");
                    setOrientation("right");
                    setPosX(getPosX()+0.5);
                    setAnimationIndex(getFrameCounter()/8);
                    setPosY(movement.gravity(getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
                }
                if (getCutsceneFrameCounter() > 550)
                {
                    setCurrentCutscene("victoryDone");                    
                }
            }
            
            if (getCurrentCutscene().equals("growing"))
            {
                int divided = getCutsceneFrameCounter()/4;
                if (divided % 2 == 1)
                {
                    setActivity("growing");
                }
                else if (divided < 5)
                {
                    setActivity("standing");
                }
                else
                {
                    setState("normal");
                    setActivity("standing");
                }
                if (divided > 12)
                {
                    setCurrentCutscene("");
                }
            }
            if (getCurrentCutscene().equals("shrinking"))
            {
                int divided = getCutsceneFrameCounter()/4;
                if (divided % 2 == 1)
                {
                    setActivity("growing");
                }
                else if (divided < 6)
                {
                    setState("normal");
                    setActivity("standing");
                }
                else
                {
                    setState("small");
                    setActivity("standing");
                }
                if (divided > 11)
                {
                    setCurrentCutscene("");
                    recoveryTimer = 127;                    
                    invincible = true;
                }
            }    
            
            if (getCurrentCutscene().equals("spawning"))
            {
                if (getCutsceneFrameCounter() < 40)
                {
                    Player otherPlayer = null;
                    for (Entity listEntity : entities)
                    {
                        if (getName().equals("Mario") && listEntity.getName().equals("Luigi"))
                        {
                            otherPlayer = (Player)listEntity;
                        }
                        if (getName().equals("Luigi") && listEntity.getName().equals("Mario"))
                        {
                            otherPlayer = (Player)listEntity;
                        }
                    }
                    setPosX(otherPlayer.getPosX());
                    setPosY(otherPlayer.getPosY()+64);
                    setVisibility(getFrameCounter()/14 % 2 == 0);
                }
                else
                {
                    setVisibility(true);
                    setCurrentCutscene("");
                }
            }
        }    
        else
        {
            if (getGlobalCutscene().equals("victory"))
            {
                setCurrentCutscene("victory");
                setCutsceneFrameCounter(0);                
            }
            
        }
        if (recoveryTimer >= 0)
        {
            int phase = recoveryTimer / 32;    
            int pow = (int)Math.pow(2, phase+1);
            int mod = recoveryTimer % pow;
                        
            if (mod == 0)
            {
                setVisibility(true);
            }
            else if (mod == pow / 2)
            {
                setVisibility(false);
            }
            
            if (recoveryTimer == 0)
            {
                invincible = false;
                setVisibility(true);
            }
            recoveryTimer--;
        }
    }
    
    public void checkCollision(List<Entity> entities)
    {
        movement.setEntities(entities);
          
        if (getCurrentCutscene().equals(""))
        {
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
                if (movement.isIntersecting(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), this, "Koopa", "red") && !invincible)
                {        
                    if (getState().equals("normal"))
                    {
                        Tools.playSound("smw_pipe.wav", 100);
                        setState("small");
                        setCurrentCutscene("shrinking");
                        setCutsceneFrameCounter(0);
                        invincible = true;
                    }
                    else if (getState().equals("small"))
                    {
                        Tools.playInterrupt("smw_lost_a_life.wav", 100);
                        setCurrentCutscene("dying");
                        setActivity("dying");
                        setCollisionEnabled(false);
                        setCutsceneFrameCounter(0);
                    }
                }
            }    
            
            if (movement.isIntersecting(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), this, "Mushroom"))
            {         
                Tools.playSound("smw_power-up.wav", 95);
                if (getState().equals("small"))
                {                    
                    setActivity("growing");
                    setCurrentCutscene("growing");
                    setCutsceneFrameCounter(0);
                }
                else
                {
                    Greenfoot.playSound("smw_reserve_item_store.wav");
                }
            }
            
            if (getPosY() + getHeightUnits() + 5 <= 0) 
            {
                Tools.playInterrupt("smw_lost_a_life.wav", 100);
                setCurrentCutscene("edge");   
                setCollisionEnabled(false);
                setCutsceneFrameCounter(0);                    
            }
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
        
        // Controls:
        // Hoch, Links, Unten, Rechts, Springen, Drehsprung, Sprinten
        // 0     1      2      3       4         5           6
        
        if (getCurrentCutscene().equals(""))
        {
            //sprinten
            if (Greenfoot.isKeyDown(controls[6]))
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
                    if ((!getActivity().equals("jumping") && !getActivity().equals("spinning")))
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
                    if ((getActivity()!="jumping" && !getActivity().equals("spinning")))
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
            if(Greenfoot.isKeyDown(controls[4]) || Greenfoot.isKeyDown(controls[5]))
            {
                //prüft ob Spiele Springen darf, also am Boden angekommen ist,
                if (jumpCount==8)
                {
                    setPosY(getPosY() + movement.jump(3)); 
                }
                if (jumpabel)
                {
                    setPosY(getPosY() + movement.jump(1));
                    if (Greenfoot.isKeyDown(controls[5]))
                    {
                        if (!getActivity().equals("spinning")) {
                            setFrameCounter(0);
                        }
                        {
                            setActivity("spinning");
                            Greenfoot.playSound("sounds/smw_spin_jump.wav");                               
                        }
                    }
                    else
                    {                            
                        setActivity("jumping");
                        Greenfoot.playSound("sounds/smw_jump.wav");
                    }
                    jumpabel = false;
                    jumpCount=0;
                }
            }
            
            if (movement.getYMove()==0 && (movement.getSpeed()==0 || getActivity().equals("spinning")) && movement.isTouchingObjectBelow(getPosX(), getPosY(), getWidthUnits(), getHeightUnits(), Block.class))
            {
                setActivity("standing");
            }
            
            if (movement.getYMove()<0 && !getActivity().equals(("spinning")))
            {
                setActivity("falling");
            }
            setPosY(movement.gravity(getPosX(), getPosY(), getWidthUnits(), getHeightUnits()));
            if (getActivity().equals("spinning"))
            {
                setAnimationIndex(getFrameCounter()/2);
            } 
            else if (Math.abs(movement.getSpeed()) <= 2)
            {
                setAnimationIndex(getFrameCounter()/5);
            }
            else
            {
                setAnimationIndex(getFrameCounter()/3);
            }
        }
        
        
    }   
    
}
