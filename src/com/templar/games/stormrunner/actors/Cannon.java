package com.templar.games.stormrunner.actors;

import [Z;
import com.templar.games.stormrunner.Actor;
import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.Shroud;
import com.templar.games.stormrunner.deaths.Shot;
import com.templar.games.stormrunner.objects.CannonTrigger;
import com.templar.games.stormrunner.objects.Mask;
import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Cannon extends Actor
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public static final int[] ANGLE_LOOKUP = { 
    270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 
    270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 
    270, 270, 270, 270, 270, 270, 270, 270, 270, 270, 
    240, 240, 240, 240, 240, 240, 240, 240, 240, 240, 
    240, 240, 240, 240, 240, 240, 240, 240, 240, 240, 
    240, 240, 240, 240, 240, 240, 240, 240, 240, 240, 
    210, 210, 210, 210, 210, 210, 210, 210, 210, 210, 
    210, 210, 210, 210, 210, 210, 210, 210, 210, 210, 
    210, 210, 210, 210, 210, 210, 210, 210, 210, 210, 
    180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 
    180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 
    180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 
    150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 
    150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 
    150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 
    120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 
    120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 
    120, 120, 120, 120, 120, 120, 120, 120, 120, 120 };
  protected static Hashtable cannonImages = new Hashtable();
  transient Image[] missImages;
  static final int[] missSequence = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
  static final int[] shotSequence = { 0, 1, 2, 3 };
  int orientation;
  int destOrientation;
  int distance;
  Point shootPos;
  AnimationComponent cannon;
  Hashtable misses = new Hashtable();
  boolean active;
  private Vector RobotsWereShootingAt = new Vector();
  boolean z;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternalWithoutImages(paramObjectOutput);
    paramObjectOutput.writeInt(this.orientation);
    paramObjectOutput.writeInt(this.destOrientation);
    paramObjectOutput.writeInt(this.distance);
    paramObjectOutput.writeObject(this.shootPos);
    paramObjectOutput.writeObject(this.cannon);
    paramObjectOutput.writeObject(this.misses);
    paramObjectOutput.writeBoolean(this.active);
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException
  {
    super.readExternalWithoutImages(paramObjectInput);
    this.orientation = paramObjectInput.readInt();
    this.destOrientation = paramObjectInput.readInt();
    this.distance = paramObjectInput.readInt();
    this.shootPos = ((Point)paramObjectInput.readObject());
    this.cannon = ((AnimationComponent)paramObjectInput.readObject());
    this.misses = ((Hashtable)paramObjectInput.readObject());
    this.active = paramObjectInput.readBoolean();
    AnimationComponent[] arrayOfAnimationComponent = new AnimationComponent[1];
    arrayOfAnimationComponent[0] = this.cannon;
    setImages(arrayOfAnimationComponent);
  }

  public Cannon()
  {
  }

  public Cannon(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
    initialize();
  }

  public Cannon(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    initialize();
  }

  public Cannon(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
    initialize();
  }

  public void initialize() {
    Debug.println("Cannon initialize()");
    if (GameApplet.thisApplet != null)
    {
      Enumeration localEnumeration = getEnvironment().getObjects().elements();

      while (localEnumeration.hasMoreElements())
      {
        PhysicalObject localPhysicalObject = (PhysicalObject)localEnumeration.nextElement();
        if (localPhysicalObject instanceof CannonTrigger)
        {
          Debug.println("Cannon connecting to " + localPhysicalObject);
          ((CannonTrigger)localPhysicalObject).setCannon(this);
        }
      }
      this.cannon = new AnimationComponent();
      this.orientation = 210;
      this.destOrientation = 210;
      updateAppearance(new Integer(this.orientation));
    }
  }

  protected void updateAppearance(Integer paramInteger) {
    Debug.println("Cannon.updateAppearance(" + paramInteger + ")");
    Image[] arrayOfImage = null;

    Object localObject = cannonImages.get(paramInteger);
    if (localObject != null) {
      arrayOfImage = (Image[])localObject;
    }
    else {
      arrayOfImage = new Image[4];
      for (int i = 0; i < 4; ++i)
      {
        arrayOfImage[i] = GameApplet.thisApplet.getImage(
          "com/templar/games/stormrunner/media/images/objects/cannon/g_cannon_" + 
          paramInteger.intValue() + "/g_cannon_" + paramInteger.intValue() + "_0" + i + ".gif");
      }
      cannonImages.put(paramInteger, arrayOfImage);
    }
    this.cannon.setCells(arrayOfImage);
    this.cannon.setSequence(shotSequence, null, false);
    this.cannon.reset();
    AnimationComponent[] arrayOfAnimationComponent = new AnimationComponent[1];
    arrayOfAnimationComponent[0] = this.cannon;
    setImages(arrayOfAnimationComponent);
  }

  public void shootAt(Robot paramRobot, Point paramPoint) {
    Debug.println("shootAt(" + paramPoint + ")");

    this.RobotsWereShootingAt.addElement(paramRobot);

    paramRobot.playSound("Robot-Angry");

    if (!(this.active))
    {
      Point localPoint1 = getPosition().getMapPoint();
      localPoint1.translate(1, 1);
      getEnvironment().getShroud().setVisible(localPoint1, 1, true, true);

      Point localPoint2 = new Point(paramPoint.x - getPosition().x + 1, paramPoint.y - getPosition().y + 1);
      Debug.println(localPoint2);
      int i = (int)Math.rint(Math.atan2(-localPoint2.y, localPoint2.x) * 57.295779513082323D);

      if (i < 0)
        i = 360 + i;

      Debug.println("angle: " + i + ", distance: " + this.distance);
      i %= 180;

      this.distance = (1 + this.distance / 3);

      i = ANGLE_LOOKUP[i];
      this.destOrientation = i;
      Debug.println("angle: " + i + ", distance: " + this.distance);
      if (this.missImages == null)
      {
        this.missImages = new Image[9];
        for (int j = 0; j < 9; ++j)
          this.missImages[j] = GameApplet.thisApplet.getImage(
            "com/templar/games/stormrunner/media/images/objects/cannon_miss/g_miss_0" + (j + 1) + ".gif");
      }
      AnimationComponent localAnimationComponent = new AnimationComponent();
      AnimationComponent[] arrayOfAnimationComponent = new AnimationComponent[1];
      localAnimationComponent.setCells(this.missImages);
      localAnimationComponent.setSequence(missSequence, null, false);
      boolean[][] arrayOfBoolean = new boolean[3][3];
      Mask localMask = new Mask(getEnvironment(), new Position(paramPoint.x - 1, paramPoint.y - 1, 0, 0), null, arrayOfBoolean, true);
      localMask.setLayer("miss anim");
      localMask.setLayer("Robot Effects");
      arrayOfAnimationComponent[0] = localAnimationComponent;
      localMask.setImages(arrayOfAnimationComponent);
      localMask.setVisible(false);
      getEnvironment().addObject(localMask);
      Debug.println("misses.put(" + paramPoint + "," + localMask + ")");
      this.misses.put(paramPoint, localMask);
      this.active = true;
      this.shootPos = paramPoint;
    }
  }

  public void tick()
  {
    if (this.active)
    {
      int i = this.destOrientation - this.orientation;
      if (i == 0)
      {
        Object localObject3;
        Debug.println("Lined up and shooting");

        stopSoundThroughRobots("CannonScan");

        if (!(this.z))
          playSoundThroughRobots("CannonFire");
        this.z = true;
        if (this.cannon.nextImage()) { return;
        }

        Debug.println("Done shooting");

        boolean bool = true;
        Class localClass = new Robot().getClass();
        Object localObject1 = null;
        Vector localVector = getEnvironment().getObjectOfTypeAt(this.shootPos, localClass);
        Debug.println(this.shootPos + ":");
        if (localVector != null)
        {
          Debug.println(String.valueOf(localVector.size()));
          localObject2 = (Robot)localVector.elementAt(0);
          Debug.println(((Robot)localObject2).getName() + ":" + ((PhysicalObject)localObject2).getPosition());
          if ((Math.abs(((PhysicalObject)localObject2).getPosition().dx) < 25) || 
            (Math.abs(((PhysicalObject)localObject2).getPosition().dy) < 25))
          {
            bool = false;
            localObject1 = localObject2;
          }
        }
        Object localObject2 = new Point(this.shootPos.x - 1, this.shootPos.y);
        Debug.println("---\n" + bool + "," + localObject2 + ":");
        localVector = getEnvironment().getObjectOfTypeAt((Point)localObject2, localClass);
        if ((localVector != null) && (bool))
        {
          Debug.println(String.valueOf(localVector.size()));
          localObject3 = (Robot)localVector.elementAt(0);
          Debug.println(((Robot)localObject3).getName() + ":" + ((PhysicalObject)localObject3).getPosition());
          if (((PhysicalObject)localObject3).getPosition().dx > 25)
          {
            bool = false;
            localObject1 = localObject3;
          }
        }
        localObject2 = new Point(this.shootPos.x + 1, this.shootPos.y);
        Debug.println("---\n" + bool + "," + localObject2 + ":");
        localVector = getEnvironment().getObjectOfTypeAt((Point)localObject2, localClass);
        if ((localVector != null) && (bool))
        {
          Debug.println(String.valueOf(localVector.size()));
          localObject3 = (Robot)localVector.elementAt(0);
          Debug.println(((Robot)localObject3).getName() + ":" + ((PhysicalObject)localObject3).getPosition());
          if (((PhysicalObject)localObject3).getPosition().dx > -25)
          {
            bool = false;
            localObject1 = localObject3;
          }
        }
        localObject2 = new Point(this.shootPos.x + 1, this.shootPos.y);
        Debug.println("---\n" + bool + "," + localObject2 + ":");
        localVector = getEnvironment().getObjectOfTypeAt(this.shootPos.x, this.shootPos.y - 1, localClass);
        if ((localVector != null) && (bool))
        {
          Debug.println(String.valueOf(localVector.size()));
          localObject3 = (Robot)localVector.elementAt(0);
          Debug.println(((Robot)localObject3).getName() + ":" + ((PhysicalObject)localObject3).getPosition());
          if (((PhysicalObject)localObject3).getPosition().dy > -25)
          {
            bool = false;
            localObject1 = localObject3;
          }
        }
        localObject2 = new Point(this.shootPos.x + 1, this.shootPos.y);
        Debug.println("---\n" + bool + "," + localObject2 + ":");
        localVector = getEnvironment().getObjectOfTypeAt(this.shootPos.x, this.shootPos.y + 1, localClass);
        if ((localVector != null) && (bool))
        {
          Debug.println(String.valueOf(localVector.size()));
          localObject3 = (Robot)localVector.elementAt(0);
          Debug.println(((Robot)localObject3).getName() + ":" + ((PhysicalObject)localObject3).getPosition());
          if (((PhysicalObject)localObject3).getPosition().dy > -25)
          {
            bool = false;
            localObject1 = localObject3;
          }
        }
        if (bool)
        {
          Object localObject4;
          Debug.println("Cannon missed.");

          if (this.misses.containsKey(this.shootPos))
          {
            ((Mask)this.misses.get(this.shootPos)).setVisible(true);
          }
          else
            Debug.println("hashtable does not contain " + this.shootPos);
          playSoundThroughRobots("CannonMiss");

          localObject3 = { 
            GameApplet.thisApplet.getImage(
            "com/templar/games/stormrunner/media/images/objects/g_crater.gif") };

          [Z[] arrayOf[Z = { { true } }; int j = 0;
          localVector = getEnvironment().getObjectOfTypeAt(this.shootPos, new Mask().getClass());
          if (localVector != null)
          {
            localObject4 = localVector.elements();
            while (((Enumeration)localObject4).hasMoreElements())
            {
              Mask localMask = (Mask)((Enumeration)localObject4).nextElement();
              Debug.println(localMask);
              if (localMask.getID().compareTo("crater") == 0)
              {
                j = 1;
                break;
              }
            }
          }
          if (j == 0)
          {
            Debug.println("placing crater");
            localObject4 = new Mask(getEnvironment(), 
              new Position(this.shootPos), 
              localObject3, arrayOf[Z, false);
            ((PhysicalObject)localObject4).setPosition(new Position(this.shootPos));
            ((PhysicalObject)localObject4).setID("crater");
            ((PhysicalObject)localObject4).setLayer("Ground Effects");
            getEnvironment().addObject((PhysicalObject)localObject4);
            getEnvironment().getRenderer().repaint();
          }
          this.misses.remove(this.shootPos);
        }
        else
        {
          Debug.println("Cannon hit " + localObject1.getName());
          playSoundThroughRobots("CannonHit");
          localObject1.setDeath(new Shot());
          if (this.misses.containsKey(this.shootPos))
          {
            localObject3 = (Mask)this.misses.get(this.shootPos);
            this.misses.remove(this.shootPos);
            ((PhysicalObject)localObject3).getEnvironment().removeObject((PhysicalObject)localObject3);
          }
        }
        this.active = false;
        this.z = false;
        this.cannon.reset();

        this.RobotsWereShootingAt.removeAllElements();

        return;
      }

      loopSoundThroughRobots("CannonScan");

      Debug.println("Cannon turning");
      if (i > 0)
        this.orientation += 30;
      else
        this.orientation -= 30;
      updateAppearance(new Integer(this.orientation));
    }
  }

  private void loopSoundThroughRobots(String paramString)
  {
    for (int i = 0; i < this.RobotsWereShootingAt.size(); ++i)
    {
      Robot localRobot = (Robot)this.RobotsWereShootingAt.elementAt(i);

      Vector localVector = localRobot.getLoopList();
      int j = 0;
      for (int k = 0; (k < localVector.size()) && (j == 0); ++k)
      {
        if (localVector.elementAt(k).equals(paramString))
          j = 1;
      }

      if (j == 0)
        localRobot.loopSound(paramString);
    }
  }

  private void playSoundThroughRobots(String paramString)
  {
    for (int i = 0; i < this.RobotsWereShootingAt.size(); ++i)
    {
      Robot localRobot = (Robot)this.RobotsWereShootingAt.elementAt(i);
      localRobot.playSound(paramString);
    }
  }

  private void stopSoundThroughRobots(String paramString)
  {
    for (int i = 0; i < this.RobotsWereShootingAt.size(); ++i)
    {
      Robot localRobot = (Robot)this.RobotsWereShootingAt.elementAt(i);
      localRobot.stopSound(paramString);
    }
  }
}