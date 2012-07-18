package com.templar.games.stormrunner;

import [Z;
import com.templar.games.stormrunner.assembly.Assembly;
import com.templar.games.stormrunner.chassis.Chassis;
import com.templar.games.stormrunner.deaths.Death;
import com.templar.games.stormrunner.objects.Obstacle;
import com.templar.games.stormrunner.objects.Trigger;
import com.templar.games.stormrunner.program.Program;
import com.templar.games.stormrunner.sensor.ObstacleSensor;
import com.templar.games.stormrunner.sensor.Sensor;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import com.templar.games.stormrunner.templarutil.gui.TextContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import com.templar.games.stormrunner.templarutil.util.OrderedTable;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Robot extends Actor
  implements Obstacle, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public static final String DEFAULT_NAME = "[UNNAMED]";
  public static final int BIAS_CENTRE = 0;
  public static final int BIAS_LEFT = 1;
  public static final int BIAS_RIGHT = 2;
  public static final int N = 0;
  public static final int NE = 45;
  public static final int E = 90;
  public static final int SE = 135;
  public static final int S = 180;
  public static final int SW = 225;
  public static final int W = 270;
  public static final int NW = 315;
  public static final int STATIONARY = 0;
  public static final int WALKING = 1;
  public static final int TURNING = 2;
  public static final int DYING = 3;
  int orientation;
  int state;
  int dest_orientation;
  Chassis chassis;
  Vector sensors;
  Vector assemblies;
  OrderedTable animationComponents;
  Program program;
  transient ImageRetriever ir;
  String name = "[UNNAMED]";
  boolean soundPlaying = false;
  transient TextContainer details;
  transient Hashtable animationPositions;
  boolean stopping;
  boolean elevated;
  boolean dead;
  Death deathMethod;
  Trigger currentTrigger;

  public String convertConstant(int paramInt1, int paramInt2)
  {
    [Ljava.lang.String[] arrayOfString; = { 
      { "BIAS_CENTRE", "BIAS_LEFT", "BIAS_RIGHT" }, 
      { "STATIONARY", "WALKING", "TURNING", "DYING" } };

    return arrayOfString;[paramInt2][paramInt1];
  }

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    super.readExternalWithoutImages(paramObjectInput);
    this.name = ((String)paramObjectInput.readObject());
    this.chassis = ((Chassis)paramObjectInput.readObject());
    this.sensors = ((Vector)paramObjectInput.readObject());
    this.assemblies = ((Vector)paramObjectInput.readObject());
    this.program = ((Program)paramObjectInput.readObject());
    this.soundPlaying = paramObjectInput.readBoolean();
    this.stopping = paramObjectInput.readBoolean();
    this.orientation = paramObjectInput.readInt();
    this.state = paramObjectInput.readInt();
    this.dest_orientation = paramObjectInput.readInt();
    this.elevated = paramObjectInput.readBoolean();
    this.dead = paramObjectInput.readBoolean();
    this.animationComponents = ((OrderedTable)paramObjectInput.readObject());
    this.currentTrigger = ((Trigger)paramObjectInput.readObject());
    ImageComponent[] arrayOfImageComponent = new ImageComponent[this.animationComponents.size()];
    this.deathMethod = ((Death)paramObjectInput.readObject());
    Enumeration localEnumeration = this.animationComponents.elements();
    for (int i = 0; localEnumeration.hasMoreElements(); ++i)
      arrayOfImageComponent[i] = ((ImageComponent)localEnumeration.nextElement());
    setImages(arrayOfImageComponent);
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException
  {
    super.writeExternalWithoutImages(paramObjectOutput);
    paramObjectOutput.writeObject(this.name);
    paramObjectOutput.writeObject(this.chassis);
    paramObjectOutput.writeObject(this.sensors);
    paramObjectOutput.writeObject(this.assemblies);
    paramObjectOutput.writeObject(this.program);
    paramObjectOutput.writeBoolean(this.soundPlaying);
    paramObjectOutput.writeBoolean(this.stopping);
    paramObjectOutput.writeInt(this.orientation);
    paramObjectOutput.writeInt(this.state);
    paramObjectOutput.writeInt(this.dest_orientation);
    paramObjectOutput.writeBoolean(this.elevated);
    paramObjectOutput.writeBoolean(this.dead);
    paramObjectOutput.writeObject(this.animationComponents);
    paramObjectOutput.writeObject(this.currentTrigger);
    paramObjectOutput.writeObject(this.deathMethod);
  }

  public Robot()
  {
    initialize();
  }

  public Robot(ImageRetriever paramImageRetriever)
  {
    setAnimated(true);
    this.ir = paramImageRetriever;

    initialize();
  }

  public Robot(ImageRetriever paramImageRetriever, Scene paramScene, Position paramPosition)
  {
    super(paramScene, paramPosition, true);
    this.ir = paramImageRetriever;

    initialize();
  }

  public void setImageRetriever(ImageRetriever paramImageRetriever)
  {
    this.ir = paramImageRetriever;
  }

  protected void initialize()
  {
    this.orientation = 90;
    this.chassis = null;
    this.sensors = new Vector();
    this.assemblies = new Vector();
    this.program = new Program();
    this.dest_orientation = 90;
    this.state = 0;
    this.details = new TextContainer();
    [Z[] arrayOf[Z = { { true } };
    setShape(arrayOf[Z);
    this.layer = "Robot";
    this.stopping = false;
    this.elevated = false;
    this.deathMethod = null;
  }

  public void addNotify()
  {
    addNotify();
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public String getName()
  {
    return this.name; }

  public boolean getElevated() {
    return this.elevated;
  }

  public void setElevated(boolean paramBoolean) {
    this.elevated = paramBoolean;
  }

  public void setState(int paramInt1, int paramInt2)
  {
    if (this.state == 3)
    {
      return;
    }

    if (paramInt1 != -1)
    {
      if (((paramInt1 == 0) && (this.state == 1)) || (
        (paramInt1 == 2) && ((
        (this.state == 1) || (this.state == 0)))))
      {
        if (this.soundPlaying)
        {
          stopSound(getChassis().getID());
          this.soundPlaying = false;
        }

        playSound(getChassis().getID() + "Cover");
      }
      else if (paramInt1 == 1)
      {
        if (!(this.soundPlaying))
        {
          loopSound(getChassis().getID());
          this.soundPlaying = true;
        }

      }
      else if (paramInt1 == 3)
      {
        stopSound(getChassis().getID());
        this.soundPlaying = false;
      }
      this.state = paramInt1;
    }

    if (paramInt2 != -1)
      this.dest_orientation = paramInt2;  }

  public int get_state() {
    return this.state; } 
  public int getDestOrientation() { return this.dest_orientation;
  }

  public Assembly getAssembly(String paramString) {
    Enumeration localEnumeration = this.assemblies.elements();
    while (localEnumeration.hasMoreElements())
    {
      Assembly localAssembly = (Assembly)localEnumeration.nextElement();
      if (localAssembly.getID().compareTo(paramString) == 0)
        return localAssembly;
    }
    return null;
  }

  public Assembly getAssembly(int paramInt)
  {
    Enumeration localEnumeration = this.assemblies.elements();
    while (localEnumeration.hasMoreElements())
    {
      Assembly localAssembly = (Assembly)localEnumeration.nextElement();
      if (localAssembly.getPlacement() == paramInt)
        return localAssembly;
    }
    return null;
  }

  public Vector getAssemblies()
  {
    return this.assemblies;
  }

  public Sensor getSensor(String paramString)
  {
    Enumeration localEnumeration = this.sensors.elements();
    while (localEnumeration.hasMoreElements())
    {
      Sensor localSensor = (Sensor)localEnumeration.nextElement();
      if (localSensor.getID().compareTo(paramString) == 0)
        return localSensor;
    }
    return null;
  }

  public Vector getSensors()
  {
    return this.sensors;
  }

  public Chassis getChassis()
  {
    return this.chassis;
  }

  public void tick()
  {
    if (this.state == 3) {
      this.deathMethod.deathStep(this);

      return;
    }

    if (this.program != null)
    {
      this.program.executeNext(this);
    }
  }

  public void setDeath(Death paramDeath)
  {
    setState(3, -1);
    this.deathMethod = paramDeath;
  }

  public int getOrientation()
  {
    return this.orientation;
  }

  public void setOrientation(int paramInt)
  {
    setOrientation(paramInt, false);
  }

  public void setOrientation(int paramInt, boolean paramBoolean)
  {
    this.orientation = paramInt;

    if (paramBoolean)
    {
      this.dest_orientation = paramInt;
    }
    if ((this.animationComponents == null) || (this.animationComponents.size() == 0))
      updateAppearance();

    Enumeration localEnumeration = this.animationComponents.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();

      AnimationComponent localAnimationComponent = (AnimationComponent)this.animationComponents.get(str);

      RobotPart localRobotPart = getRobotPart(str);

      Image[] arrayOfImage = localRobotPart.getCells(this.ir, this.orientation, getBias());
      localAnimationComponent.setCells(arrayOfImage);
      if (str.compareTo(this.chassis.getID()) == 0)
      {
        int[] arrayOfInt = this.chassis.getAnimationSequence(0);
        localAnimationComponent.setSequence(arrayOfInt, null, true);
      }
    }
  }

  public boolean checkObstacles()
  {
    return new ObstacleSensor().check(this, false);
  }

  public Position inFrontOf()
  {
    Position localPosition = new Position(getPosition());
    switch (getOrientation())
    {
    case 0:
      localPosition.y -= 1;
      break;
    case 180:
      localPosition.y += 1;
      break;
    case 270:
      localPosition.x -= 1;
      break;
    case 90:
      localPosition.x += 1;
    }

    return localPosition;
  }

  public boolean checkBounds()
  {
    Position localPosition = inFrontOf();

    return ((localPosition.x < 0) || (localPosition.y < 0) || 
      (localPosition.x >= getEnvironment().getMap().getSize().width) || 
      (localPosition.y >= getEnvironment().getMap().getSize().height));
  }

  public boolean checkTerrain()
  {
    Position localPosition = inFrontOf();
    MapCell localMapCell = getEnvironment().getMap().getCell(localPosition.getMapPoint());

    return ((localMapCell == null) || (localMapCell.getImpassibility() > getChassis().getImpassibility()));
  }

  public Program getProgram()
  {
    return this.program;
  }

  public void setProgram(Program paramProgram)
  {
    this.program = paramProgram;
  }

  public void setChassis(Chassis paramChassis)
  {
    this.chassis = paramChassis;
    updateAppearance();
  }

  public void addSensor(Sensor paramSensor)
  {
    this.sensors.addElement(paramSensor);
  }

  public void addAssembly(Assembly paramAssembly)
  {
    this.assemblies.addElement(paramAssembly);
    updateAppearance();
  }

  public void stop()
  {
    if (getProgram().isExecuting())
    {
      this.stopping = true;
    }

    getDetailContainer(); }

  public boolean isStopping() {
    return this.stopping; } 
  public void unsetStopping() { this.stopping = false;
  }

  public void removeSensor(String paramString) {
    for (int i = 0; i < this.sensors.size(); ++i)
      if (((Sensor)this.sensors.elementAt(i)).getID().equals(paramString))
      {
        this.sensors.removeElementAt(i);
        return;
      }
  }

  public void removeAssembly(String paramString)
  {
    for (int i = 0; i < this.assemblies.size(); ++i)
      if (((Assembly)this.assemblies.elementAt(i)).getID().equals(paramString))
      {
        this.assemblies.removeElementAt(i);
        this.animationComponents.remove(paramString);
        updateAppearance();
        return;
      }
  }

  public RobotPart getRobotPart(String paramString)
  {
    if ((this.chassis != null) && (this.chassis.getID().compareTo(paramString) == 0)) {
      return this.chassis;
    }

    Object localObject = getAssembly(paramString);
    if (localObject != null)
      return localObject;
    localObject = getSensor(paramString);
    if (localObject != null)
      return localObject;
    return ((RobotPart)null);
  }

  public RobotPart[] getRobotParts()
  {
    int i = this.sensors.size() + this.assemblies.size();
    if (this.chassis != null)
      ++i;

    RobotPart[] arrayOfRobotPart = new RobotPart[i];
    int j = 0;

    if (this.chassis != null)
    {
      arrayOfRobotPart[0] = this.chassis;
      ++j;
    }

    for (int k = 0; k < this.sensors.size(); ++k)
      arrayOfRobotPart[(j++)] = ((RobotPart)this.sensors.elementAt(k));

    for (int l = 0; l < this.assemblies.size(); ++l)
      arrayOfRobotPart[(j++)] = ((RobotPart)this.assemblies.elementAt(l));

    return arrayOfRobotPart;
  }

  public TextContainer getDetailContainer()
  {
    this.details.setText(toString());
    return this.details;
  }

  public String toString()
  {
    Enumeration localEnumeration;
    if (this.dead)
      return "";
    StringBuffer localStringBuffer = new StringBuffer("RCX:");
    localStringBuffer.append(this.name);
    localStringBuffer.append("\n");
    if (this.chassis != null)
    {
      localStringBuffer.append(this.chassis.toString());
      localStringBuffer.append(" class.\n");
    }
    if (this.assemblies.size() > 0)
    {
      localEnumeration = this.assemblies.elements();
      while (localEnumeration.hasMoreElements())
      {
        localStringBuffer.append(localEnumeration.nextElement().toString());
        localStringBuffer.append("\n");
      }
    }
    if (this.sensors.size() > 0)
    {
      localEnumeration = this.sensors.elements();
      while (localEnumeration.hasMoreElements())
      {
        localStringBuffer.append(localEnumeration.nextElement().toString());
        localStringBuffer.append("\n");
      }
    }
    localStringBuffer.append(this.program);
    localStringBuffer.append("\n");
    return localStringBuffer.toString();
  }

  public int getBias()
  {
    int i = this.dest_orientation - this.orientation;
    if (i < 0)
      return 1;
    if (i > 0)
      return 2;
    return 0;
  }

  public static final String convertBias(int paramInt) {
    switch (paramInt)
    {
    case 0:
      return "C";
    case 2:
      return "R";
    case 1:
      return "L";
    }
    return "";
  }

  public void updateAppearance()
  {
    OrderedTable localOrderedTable = new OrderedTable();
    RobotPart[] arrayOfRobotPart = getRobotParts();
    for (int i = 0; i < arrayOfRobotPart.length; ++i)
      localOrderedTable.put(arrayOfRobotPart[i].getID(), arrayOfRobotPart[i]);
    localOrderedTable = RobotPartSorter.sortTable(localOrderedTable);
    Vector localVector = new Vector();
    Enumeration localEnumeration = localOrderedTable.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();

      if (localOrderedTable.get(str) instanceof Sensor) {
        localVector.addElement(str);
      }
      else {
        Image[] arrayOfImage = ((RobotPart)localOrderedTable.get(str)).getCells(this.ir, this.orientation, getBias());
        localOrderedTable.put(str, arrayOfImage);
      }
    }
    if (localVector.size() != 0)
      for (int j = 0; j < localVector.size(); ++j)
        localOrderedTable.remove((String)localVector.elementAt(j));

    this.animationComponents = setImages(localOrderedTable);

    getDetailContainer();
  }

  public AnimationComponent getAnimationComponent(String paramString)
  {
    if (this.animationComponents != null)
    {
      return ((AnimationComponent)this.animationComponents.get(paramString));
    }
    return null;
  }

  public OrderedTable getAnimationComponents()
  {
    return this.animationComponents;
  }

  public boolean isObstructing(Robot paramRobot)
  {
    return true;
  }

  public boolean isDead()
  {
    return this.dead;
  }

  public void setDead(boolean paramBoolean)
  {
    this.dead = paramBoolean;
  }

  public void setCurrentTrigger(Trigger paramTrigger)
  {
    this.currentTrigger = paramTrigger;
  }

  public Trigger getCurrentTrigger() {
    return this.currentTrigger;
  }

  protected void handleMouseClick(MouseEvent paramMouseEvent)
  {
    GameApplet.thisApplet.getGameState().setCurrentRobot(this);
  }
}