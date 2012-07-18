package com.templar.games.stormrunner;

import com.templar.games.stormrunner.assembly.Assembly;
import com.templar.games.stormrunner.assembly.CargoPod;
import com.templar.games.stormrunner.assembly.GrabberArm;
import com.templar.games.stormrunner.assembly.Launcher;
import com.templar.games.stormrunner.assembly.Piledriver;
import com.templar.games.stormrunner.build.BuildPanel;
import com.templar.games.stormrunner.chassis.Achilles;
import com.templar.games.stormrunner.chassis.Arachnae;
import com.templar.games.stormrunner.chassis.Hermes;
import com.templar.games.stormrunner.objects.EnergyCell;
import com.templar.games.stormrunner.objects.FoundRobotPart;
import com.templar.games.stormrunner.objects.Salvage;
import com.templar.games.stormrunner.objects.Satellite;
import com.templar.games.stormrunner.objects.Trigger;
import com.templar.games.stormrunner.program.editor.Editor;
import com.templar.games.stormrunner.sensor.EnergySensor;
import com.templar.games.stormrunner.sensor.GeolabSensor;
import com.templar.games.stormrunner.sensor.HeatSensor;
import com.templar.games.stormrunner.sensor.ObstacleSensor;
import com.templar.games.stormrunner.sensor.VidSensor;
import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.gui.BarGraph;
import com.templar.games.stormrunner.templarutil.util.OrderedTable;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Frame;
import java.awt.Point;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

public class GameState
  implements Runnable, InventoryContainer, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public static final int TICK_DELAY = 200;
  public static final int GC_TICK_COUNT = 500;
  public static final String GAMEOVER_URL = "gameover.html";
  public static final String VICTORY_URL = "victory.html";
  protected transient GameApplet appletRef;
  protected transient BarGraph bargraph;
  protected transient Scene currentScene;
  protected transient Renderer renderer;
  protected transient Robot currentRobot;
  protected World world;
  protected OrderedTable ProgramLibrary;
  protected String Username;
  protected String UserRank;
  protected int SecurityLevel;
  protected int Polymetals;
  protected int EnergyUnits;
  protected Vector Inventory;
  protected Vector RobotPartPatterns;
  protected Vector SpecialProgramParts;
  protected Vector ActiveRobots;
  protected Vector StoredRobots;
  protected long tickCount;
  protected transient Thread stateThread;
  protected transient boolean running;
  protected Point TemporaryOffset;
  long timestamp;

  public GameState()
  {
    this.bargraph = new BarGraph();

    this.world = new World();
    this.renderer = new Renderer(this);
    this.ProgramLibrary = new OrderedTable();
    this.ActiveRobots = new Vector();
    this.StoredRobots = new Vector();
    this.Inventory = new Vector();
    this.RobotPartPatterns = new Vector();
    this.SpecialProgramParts = new Vector();
  }

  public GameState(GameApplet paramGameApplet)
  {
    this.appletRef = paramGameApplet;

    this.RobotPartPatterns.addElement(new VidSensor());
    this.RobotPartPatterns.addElement(new ObstacleSensor());
    this.RobotPartPatterns.addElement(new CargoPod());
    this.RobotPartPatterns.addElement(new GrabberArm());
    this.RobotPartPatterns.addElement(new Achilles());
    if (this.appletRef.getParameter("cheats") != null)
    {
      this.RobotPartPatterns.addElement(new HeatSensor());
      this.RobotPartPatterns.addElement(new EnergySensor());
      this.RobotPartPatterns.addElement(new Hermes());
      this.RobotPartPatterns.addElement(new Arachnae());
      this.RobotPartPatterns.addElement(new Piledriver());
      this.RobotPartPatterns.addElement(new Launcher());
      this.RobotPartPatterns.addElement(new GeolabSensor()); }
  }

  public String getUsername() {
    return this.Username; }

  public void setUsername(String paramString) {
    this.Username = paramString;

    this.appletRef.getBuildPanel().setUsername(paramString); }

  public String getUserRank() { return this.UserRank; }

  public void setUserRank(String paramString) {
    this.UserRank = paramString;

    this.appletRef.getBuildPanel().setUserRank(paramString); }

  public int getSecurityLevel() { return this.SecurityLevel; }

  public void setSecurityLevel(int paramInt) {
    this.SecurityLevel = paramInt;

    this.appletRef.getBuildPanel().setSecurityLevel(paramInt); }

  public int getPolymetals() { return this.Polymetals; }

  public void setPolymetals(int paramInt) {
    this.Polymetals = paramInt;

    BuildPanel localBuildPanel = this.appletRef.getBuildPanel();
    if (localBuildPanel != null)
      localBuildPanel.setPolymetals(paramInt);  }

  public int getEnergyUnits() {
    return this.EnergyUnits; }

  public void setEnergyUnits(int paramInt) {
    this.EnergyUnits = paramInt;

    BuildPanel localBuildPanel = this.appletRef.getBuildPanel();
    if (localBuildPanel != null)
      localBuildPanel.setEnergyUnits(paramInt);  }

  public boolean isEmpty() {
    return (this.Inventory.size() == 0); } 
  public Vector getInventory() { return this.Inventory; }

  public PhysicalObject transferOut(String paramString) {
    for (int i = 0; i < this.Inventory.size(); ++i)
    {
      PhysicalObject localPhysicalObject = (PhysicalObject)this.Inventory.elementAt(i);
      if (paramString.compareTo(localPhysicalObject.getID()) == 0)
      {
        this.Inventory.removeElementAt(i);
        return localPhysicalObject;
      }
    }
    return null;
  }

  public boolean transferIn(PhysicalObject paramPhysicalObject) {
    this.Inventory.addElement(paramPhysicalObject);
    return true;
  }

  public OrderedTable getProgramLibrary()
  {
    return this.ProgramLibrary;
  }

  public Vector getRobotPartPatterns()
  {
    return this.RobotPartPatterns;
  }

  public Vector getSpecialProgramParts()
  {
    return this.SpecialProgramParts;
  }

  public Scene getCurrentScene()
  {
    if (this.currentScene == null)
    {
      this.currentScene = this.world.getCurrentScene();
      if (this.currentScene == null)
        this.currentScene = this.world.getNextScene();
      if (this.currentScene != null)
      {
        Vector localVector = this.currentScene.getObjects();
        Enumeration localEnumeration = localVector.elements();
        while (localEnumeration.hasMoreElements())
        {
          PhysicalObject localPhysicalObject = (PhysicalObject)localEnumeration.nextElement();

          if (localPhysicalObject instanceof Trigger)
          {
            ((Trigger)localPhysicalObject).setGameState(this);
          }
        }
      }
    }
    return this.currentScene;
  }

  public Point getViewport()
  {
    return this.renderer.getOffset();
  }

  public void setViewport(Point paramPoint)
  {
    this.renderer.setOffset(paramPoint);
  }

  public void setWorld(World paramWorld)
  {
    this.world = paramWorld;
  }

  public void setRenderer(Renderer paramRenderer)
  {
    this.renderer = paramRenderer;

    if (this.TemporaryOffset != null)
      setViewport(this.TemporaryOffset);
  }

  public Renderer getRenderer()
  {
    return this.renderer;
  }

  public GameApplet getAppletRef()
  {
    return this.appletRef;
  }

  public void setAppletRef(GameApplet paramGameApplet)
  {
    this.appletRef = paramGameApplet;
  }

  public void run()
  {
    while (this.running)
    {
      this.tickCount += 2497819989986246657L;
      if (this.tickCount % 500L == 2497820642821275648L)
      {
        System.gc();
        Debug.println("gc() called, freeMemory(): " + Runtime.getRuntime().freeMemory());
      }

      this.timestamp = System.currentTimeMillis();
      tick();
      long l1 = System.currentTimeMillis();
      try
      {
        long l2 = 200L - l1 - this.timestamp;
        if (l2 <= 2497820093065461760L) break label100;
        Thread.currentThread(); label100: Thread.sleep(l2);
      }
      catch (InterruptedException localInterruptedException)
      {
      }
    }
  }

  public void tick()
  {
    if (this.currentScene == null) return;

    synchronized (this.appletRef.getPaintLock())
    {
      Enumeration localEnumeration = this.currentScene.getActors().elements();
      while (localEnumeration.hasMoreElements())
      {
        Actor localActor = (Actor)localEnumeration.nextElement();

        if ((localActor instanceof Robot) && (((Robot)localActor).isDead()))
        {
          deactivateRobot((Robot)localActor);
        }
        else
        {
          localActor.tick();
        }

      }

      if (this.currentRobot != null)
        this.currentRobot.getDetailContainer();
      return;
    }
  }

  public void stop()
  {
    this.bargraph.setVisible(false);
    this.bargraph.dispose();
    this.running = false;
  }

  public void start()
  {
    this.running = true;
    this.stateThread = new Thread(this, "StateThread");
    this.stateThread.start();
    this.bargraph.setVisible(true);
  }

  public Robot getCurrentRobot()
  {
    return this.currentRobot;
  }

  public void setCurrentRobot(Robot paramRobot)
  {
    this.currentRobot = paramRobot;
    this.appletRef.getEditor().setRobot(paramRobot);
    this.appletRef.getStatusPanel().reportNewCurrentRobot(paramRobot);
  }

  public Robot getRobot(boolean paramBoolean)
  {
    Vector localVector = getAllRobots();
    if (this.currentRobot == null)
    {
      if (localVector.size() > 0)
      {
        this.currentRobot = ((Robot)localVector.elementAt((paramBoolean) ? localVector.size() - 1 : 0));
        return this.currentRobot;
      }
      return null;
    }
    int i = localVector.indexOf(this.currentRobot);
    if (i > -1)
    {
      i += ((paramBoolean) ? -1 : 1);
      if (i < 0)
        i = localVector.size() - 1;
      if (i == localVector.size())
        i = 0;
      return ((Robot)localVector.elementAt(i));
    }
    return null;
  }

  public Robot getRobot(boolean paramBoolean1, boolean paramBoolean2) {
    if (paramBoolean2)
    {
      this.currentRobot = getRobot(paramBoolean1);
      return this.currentRobot;
    }

    return getRobot(paramBoolean1);
  }

  public void addRobot(Robot paramRobot) {
    this.StoredRobots.addElement(paramRobot);
    if (this.appletRef.getStatusPanel() != null)
      this.appletRef.getStatusPanel().populateRosterPanel();
  }

  public void removeRobot(Robot paramRobot) {
    this.StoredRobots.removeElement(paramRobot);
    if (this.appletRef.getStatusPanel() != null)
      this.appletRef.getStatusPanel().populateRosterPanel();
  }

  public boolean activateRobot(Robot paramRobot)
  {
    return activateRobot(paramRobot, getCurrentScene().getRobotStart());
  }

  public boolean activateRobot(Robot paramRobot, Point paramPoint)
  {
    if (getCurrentScene().isObstructed(paramPoint))
    {
      return false;
    }

    this.StoredRobots.removeElement(paramRobot);
    this.ActiveRobots.addElement(paramRobot);

    Point localPoint = new Point(paramPoint);
    localPoint.translate(-4, -3);
    getCurrentScene().getRenderer().setOffset(localPoint);
    getCurrentScene().getShroud().setVisible(paramPoint, 2, true, false);

    paramRobot.setPosition(new Position(paramPoint.x, paramPoint.y, 0, 0));
    paramRobot.setEnvironment(getCurrentScene());
    paramRobot.setOrientation(90, true);

    getCurrentScene().addObject(paramRobot);

    if (this.appletRef.getStatusPanel() != null)
      this.appletRef.getStatusPanel().populateRosterPanel();

    setCurrentRobot(paramRobot);

    return true;
  }

  public void deactivateRobot(Robot paramRobot)
  {
    if (this.ActiveRobots.contains(paramRobot))
    {
      paramRobot.setEnvironment(null);

      paramRobot.stop();

      if (this.currentRobot == paramRobot)
      {
        setCurrentRobot(null);
        if (!(paramRobot.isDead()))
        {
          this.appletRef.setState(0);
          this.appletRef.getBuildPanel().setDescription("RCX: " + paramRobot.getName() + 
            " stored.");
        }
      }

      this.ActiveRobots.removeElement(paramRobot);
      paramRobot.getPosition();
      getCurrentScene().removeObject(paramRobot);
      if (!(paramRobot.isDead()))
      {
        Object localObject2;
        Object localObject3;
        this.StoredRobots.addElement(paramRobot);

        Vector localVector = new Vector();
        int i = 0;
        RobotPart[] arrayOfRobotPart = paramRobot.getRobotParts();
        for (int j = 0; j < arrayOfRobotPart.length; ++j)
        {
          if (arrayOfRobotPart[j] instanceof Assembly)
          {
            localObject1 = (Assembly)arrayOfRobotPart[j];

            if (localObject1 instanceof InventoryContainer)
            {
              InventoryContainer localInventoryContainer = (InventoryContainer)localObject1;

              setPolymetals(localInventoryContainer.getPolymetals() + getPolymetals());
              setEnergyUnits(localInventoryContainer.getEnergyUnits() + getEnergyUnits());
              localInventoryContainer.setPolymetals(0);
              localInventoryContainer.setEnergyUnits(0);

              while (!(localInventoryContainer.isEmpty()))
              {
                localObject3 = localInventoryContainer.transferOut(null);

                if ((localObject3 instanceof EnergyCell) || (localObject3 instanceof Salvage)) {
                  localObject3 = null;
                }
                else {
                  Object localObject4;
                  if (localObject3 instanceof FoundRobotPart)
                  {
                    localObject4 = (FoundRobotPart)localObject3;
                    Enumeration localEnumeration = this.RobotPartPatterns.elements();
                    while (localEnumeration.hasMoreElements())
                    {
                      RobotPart localRobotPart = (RobotPart)localEnumeration.nextElement();

                      if (localRobotPart.getID().compareTo(((FoundRobotPart)localObject4).getRobotPart().getID()) == 0)
                        localObject3 = null;
                    }
                    if (localObject3 != null)
                    {
                      this.RobotPartPatterns.addElement(((FoundRobotPart)localObject4).getRobotPart());
                      localVector.addElement(((FoundRobotPart)localObject4).getRobotPart().getID());
                    }

                    setEnergyUnits(((FoundRobotPart)localObject4).getRobotPart().getEnergyCost() + getEnergyUnits());
                    setPolymetals(((FoundRobotPart)localObject4).getRobotPart().getSalvageCost() + getPolymetals());
                  }
                  else if (localObject3 instanceof Satellite)
                  {
                    i = 1;

                    localObject4 = new Position(this.currentScene.getRobotStart());
                    localObject4.y += 1;
                    ((PhysicalObject)localObject3).setPosition((Position)localObject4);
                    this.currentScene.addObject((PhysicalObject)localObject3);
                  }
                  else {
                    transferIn((PhysicalObject)localObject3);
                  }
                }

              }

            }

          }

          localObject1 = this.RobotPartPatterns.elements();
          int k = 0;
          while (((Enumeration)localObject1).hasMoreElements())
          {
            localObject3 = ((Enumeration)localObject1).nextElement();
            if ((localObject3 instanceof RobotPart) && 
              (((RobotPart)localObject3).getID().compareTo(arrayOfRobotPart[j].getID()) == 0))
            {
              k = 1;
              break;
            }
          }

          if (k == 0)
            try
            {
              this.RobotPartPatterns.addElement(arrayOfRobotPart[j].getClass().newInstance());
              localVector.addElement(arrayOfRobotPart[j].getID());
            }
            catch (IllegalAccessException localIllegalAccessException)
            {
            }
            catch (InstantiationException localInstantiationException)
            {
            }
        }

        Object localObject1 = null;
        if (localVector.size() > 0)
        {
          this.appletRef.getBuildPanel().clearPartsList();

          if (localVector.size() == 1)
          {
            localObject1 = 
              "RCX: " + paramRobot.getName() + " stored.\nNew Pattern recognized:\n" + 
              ((String)localVector.elementAt(0)) + "\nIt is now available for building.";
          }
          else
          {
            localObject2 = new StringBuffer("RCX: ");
            ((StringBuffer)localObject2).append(paramRobot.getName());
            ((StringBuffer)localObject2).append(" stored.\nNew Patterns recognized:\n");
            localObject3 = localVector.elements();
            while (((Enumeration)localObject3).hasMoreElements())
              ((StringBuffer)localObject2).append((String)((Enumeration)localObject3).nextElement());
            ((StringBuffer)localObject2).append("Parts now available for building.\n");
            localObject1 = ((StringBuffer)localObject2).toString();
          }
        }

        if (i != 0)
        {
          localObject2 = "Foreign object (Satellite) could not be scanned and remains outside the bay.\n";
          if (localObject1 != null)
            localObject1 = localObject1 + ((String)localObject2);
          else
            localObject1 = localObject2;
        }

        if (localObject1 != null) {
          this.appletRef.getBuildPanel().setDescription((String)localObject1);
        }

      }
      else if (GameOverChecks.check(paramRobot))
      {
        this.appletRef.setMidGame(false);
        this.appletRef.setState(2);
        try
        {
          GameApplet.appletContext.showDocument(new URL(GameApplet.thisApplet.getDocumentBase(), "gameover.html"), "_self");
        }
        catch (MalformedURLException localMalformedURLException)
        {
        }
      }

      if (this.appletRef.getStatusPanel() != null)
        this.appletRef.getStatusPanel().populateRosterPanel();
    }
  }

  public boolean isRobotActive(Robot paramRobot)
  {
    return this.ActiveRobots.contains(paramRobot);
  }

  public boolean isRobotStored(Robot paramRobot)
  {
    return this.StoredRobots.contains(paramRobot);
  }

  public Vector getActiveRobots()
  {
    return this.ActiveRobots;
  }

  public Vector getStoredRobots()
  {
    return this.StoredRobots;
  }

  public Vector getAllRobots()
  {
    Vector localVector = new Vector(this.ActiveRobots.size() + this.StoredRobots.size());
    Enumeration localEnumeration = this.StoredRobots.elements();
    while (localEnumeration.hasMoreElements())
      localVector.addElement(localEnumeration.nextElement());
    localEnumeration = this.ActiveRobots.elements();
    while (localEnumeration.hasMoreElements())
      localVector.addElement(localEnumeration.nextElement());
    return localVector;
  }

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.world = ((World)paramObjectInput.readObject());
    this.currentScene = this.world.getCurrentScene();
    this.ProgramLibrary = ((OrderedTable)paramObjectInput.readObject());
    this.ActiveRobots = ((Vector)paramObjectInput.readObject());
    this.StoredRobots = ((Vector)paramObjectInput.readObject());
    Enumeration localEnumeration = getAllRobots().elements();
    while (localEnumeration.hasMoreElements())
      ((Robot)localEnumeration.nextElement()).setImageRetriever(GameApplet.thisApplet);
    localEnumeration = this.ActiveRobots.elements();
    while (localEnumeration.hasMoreElements())
    {
      Robot localRobot = (Robot)localEnumeration.nextElement();
      localRobot.setEnvironment(getCurrentScene());
    }
    int i = paramObjectInput.readInt();
    if (i != -1)
      this.currentRobot = ((Robot)this.ActiveRobots.elementAt(i));
    this.Username = ((String)paramObjectInput.readObject());
    this.UserRank = ((String)paramObjectInput.readObject());
    this.SecurityLevel = paramObjectInput.readInt();
    this.Polymetals = paramObjectInput.readInt();
    this.EnergyUnits = paramObjectInput.readInt();
    this.Inventory = ((Vector)paramObjectInput.readObject());
    this.RobotPartPatterns = ((Vector)paramObjectInput.readObject());
    this.TemporaryOffset = ((Point)paramObjectInput.readObject());
    this.SpecialProgramParts = ((Vector)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException
  {
    paramObjectOutput.writeObject(this.world);
    paramObjectOutput.writeObject(this.ProgramLibrary);
    paramObjectOutput.writeObject(this.ActiveRobots);
    paramObjectOutput.writeObject(this.StoredRobots);
    if (this.currentRobot == null)
      paramObjectOutput.writeInt(-1);
    else
      paramObjectOutput.writeInt(this.ActiveRobots.indexOf(this.currentRobot));
    paramObjectOutput.writeObject(this.Username);
    paramObjectOutput.writeObject(this.UserRank);
    paramObjectOutput.writeInt(this.SecurityLevel);
    paramObjectOutput.writeInt(this.Polymetals);
    paramObjectOutput.writeInt(this.EnergyUnits);
    paramObjectOutput.writeObject(this.Inventory);
    paramObjectOutput.writeObject(this.RobotPartPatterns);
    paramObjectOutput.writeObject(this.renderer.getOffset());
    paramObjectOutput.writeObject(this.SpecialProgramParts);
  }

  public void dispose()
  {
    this.renderer.setGameState(null);
    this.appletRef.getEditor().setGameState(null);
    this.appletRef.getBuildPanel().setGameState(null);
    this.appletRef.getStatusPanel().setGameState(null);
    Vector localVector = this.currentScene.getObjects();
    Enumeration localEnumeration = localVector.elements();
    while (localEnumeration.hasMoreElements())
    {
      PhysicalObject localPhysicalObject = (PhysicalObject)localEnumeration.nextElement();
      if (localPhysicalObject instanceof Trigger)
      {
        ((Trigger)localPhysicalObject).setGameState(null); }
    }
  }

  public long getTickCount() {
    return this.tickCount; } 
  public void setTickCount(long paramLong) { this.tickCount = paramLong; } 
  public void setFinished() { this.running = false;
  }
}