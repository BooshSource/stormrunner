package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.Actor;
import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Renderer;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.assembly.Assembly;
import com.templar.games.stormrunner.chassis.Chassis;
import com.templar.games.stormrunner.sensor.Sensor;
import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

public class FoundRobot extends Actor
  implements Obstacle, Trigger, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected boolean clear;
  protected String newName;
  protected String chassis;
  protected String[] parts;
  protected int initialOrientation;
  protected AnimationComponent[] clearAnimations;
  protected transient GameState state;
  protected int whichAnimation;
  protected boolean active;

  public void readExternal(ObjectInput paramObjectInput)
    throws ClassNotFoundException, IOException
  {
    super.readExternalWithoutImages(paramObjectInput);
    this.whichAnimation = paramObjectInput.readInt();
    this.initialOrientation = paramObjectInput.readInt();
    this.clear = paramObjectInput.readBoolean();
    this.newName = ((String)paramObjectInput.readObject());
    this.chassis = ((String)paramObjectInput.readObject());
    this.parts = ((String[])paramObjectInput.readObject());
    Vector localVector = (Vector)paramObjectInput.readObject();
    ImageComponent[] arrayOfImageComponent = new ImageComponent[localVector.size()];
    for (int i = 0; i < localVector.size(); i++)
      if ((localVector.elementAt(i) instanceof AnimationComponent))
        arrayOfImageComponent[i] = ((ImageComponent)localVector.elementAt(i));
      else
        arrayOfImageComponent[i] = new ImageComponent(GameApplet.thisApplet, 
          (String)localVector.elementAt(i), true, false);
    this.clearAnimations = ((AnimationComponent[])paramObjectInput.readObject());
    this.active = paramObjectInput.readBoolean();
    setImages(arrayOfImageComponent);
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternalWithoutImages(paramObjectOutput);
    paramObjectOutput.writeInt(this.whichAnimation);
    paramObjectOutput.writeInt(this.initialOrientation);
    paramObjectOutput.writeBoolean(this.clear);
    paramObjectOutput.writeObject(this.newName);
    paramObjectOutput.writeObject(this.chassis);
    paramObjectOutput.writeObject(this.parts);
    Vector localVector = new Vector();
    ImageComponent[] arrayOfImageComponent = getImageComponents();
    for (int i = 0; i < arrayOfImageComponent.length; i++)
      if ((arrayOfImageComponent[i] instanceof AnimationComponent))
        localVector.addElement(arrayOfImageComponent[i]);
      else
        localVector.addElement(GameApplet.thisApplet.getImageFilename(arrayOfImageComponent[i].getImage()));
    paramObjectOutput.writeObject(localVector);
    paramObjectOutput.writeObject(this.clearAnimations);
    paramObjectOutput.writeBoolean(this.active);
  }

  public FoundRobot()
  {
    initialize();
  }

  public FoundRobot(Scene paramScene, Position paramPosition, boolean paramBoolean) {
    super(paramScene, paramPosition, paramBoolean);
    initialize();
  }

  public FoundRobot(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    initialize();
  }

  public FoundRobot(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
    initialize();
  }

  public boolean isObstructing(Robot paramRobot) {
    return true;
  }

  protected void initialize() {
    this.clear = false;
    this.newName = "Test Robot";
    this.chassis = "Hermes";
    this.clearAnimations = null;
    this.parts = new String[1];
    this.whichAnimation = -1;
    this.active = false;
  }
  public int activateOnEvent() { return 16; } 
  public void setGameState(GameState paramGameState) { this.state = paramGameState; }

  public void activate(Robot paramRobot, int paramInt) {
    if (this.clearAnimations != null)
    {
      if (!this.clear)
      {
        this.whichAnimation += 1;
        if (this.whichAnimation < this.clearAnimations.length)
        {
          ImageComponent[] arrayOfImageComponent = getImageComponents();

          arrayOfImageComponent[0] = this.clearAnimations[this.whichAnimation];

          setImages(arrayOfImageComponent);
          this.active = true;
          if (this.whichAnimation == this.clearAnimations.length - 1) {
            this.clear = true;

            return;
          }

        }

      }

    }
    else
    {
      this.clear = true;
    }
  }

  public void tick() {
    if (this.active)
    {
      if (!this.clearAnimations[this.whichAnimation].nextImage())
      {
        this.active = false;
        if (this.whichAnimation == this.clearAnimations.length - 1)
          this.clear = true;
      }
    }
  }

  protected void handleMouseClick(MouseEvent paramMouseEvent)
  {
    if (this.clear)
    {
      Position localPosition = getPosition();

      getEnvironment().removeObject(this);
      if (this.state != null)
      {
        Robot localRobot = new Robot(GameApplet.thisApplet);
        try
        {
          localRobot.setChassis((Chassis)Class.forName("com.templar.games.stormrunner.chassis." + this.chassis).newInstance());
          localRobot.setName(this.newName);
          Debug.println(this.parts);
          for (int i = 0; i < this.parts.length; i++) {
            if (this.parts[i] == null)
              continue;
            Class localClass = Class.forName("com.templar.games.stormrunner." + this.parts[i]);
            if (this.parts[i].startsWith("sensor."))
              localRobot.addSensor((Sensor)localClass.newInstance());
            else
              localRobot.addAssembly((Assembly)localClass.newInstance());
          }
          this.state.activateRobot(localRobot, localPosition.getMapPoint());
          localRobot.setOrientation(this.initialOrientation);
          localRobot.getEnvironment().getRenderer().setOffsetToCenter(localPosition.getMapPoint());
          localRobot.updateAppearance();
          localRobot.playSound("Robot-Alarm");
          GameApplet.audio.play("RobotStart");
          GameApplet.thisApplet.sendStatusMessage(
            "RCX:" + this.newName + " has been activated. Return it to the Cargo Bay for analysis of new parts.");

          return;
        }
        catch (ClassNotFoundException localClassNotFoundException)
        {
          localClassNotFoundException.printStackTrace();

          return;
        }
        catch (InstantiationException localInstantiationException)
        {
          localInstantiationException.printStackTrace();

          return;
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          localIllegalAccessException.printStackTrace();

          return;
        }

      }

      Debug.println(this + " has no GameState set.");

      return;
    }

    GameApplet.audio.play("ButtonError");
    GameApplet.thisApplet.sendStatusMessage(
      "RCX active but unable to initialize: RCX unable to move");
  }
}