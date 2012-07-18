package com.templar.games.stormrunner.deaths;

//import [Z;
import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.Map;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.RobotPart;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.Shroud;
import com.templar.games.stormrunner.objects.CliffTrigger;
import com.templar.games.stormrunner.objects.Mask;
import com.templar.games.stormrunner.objects.Salvage;
import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import com.templar.games.stormrunner.templarutil.util.OrderedTable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Vector;

public class Falling extends Death
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  private static final int FALLACCEL = 2;
  public static final int[] EXPLOSION_SEQUENCE = { 0, 
    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 9, 10, 11, 12, 9, 10, 11, 12, 13, 14, 15, 16, 
    14, 15, 16, 17, 18, 19, 20, 18, 19, 20, 21, 22 };
  Position initialRobotPosition = new Position();
  int dyingFrame = 0;
  int velocity = 0;
  int fallTime = 0;
  int deltax = 0;
  int xstep = 0;
  AnimationComponent explode;
  Mask mask;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.dyingFrame = paramObjectInput.readInt();
    this.velocity = paramObjectInput.readInt();
    this.fallTime = paramObjectInput.readInt();
    this.deltax = paramObjectInput.readInt();
    this.xstep = paramObjectInput.readInt();
    this.mask = ((Mask)paramObjectInput.readObject());
    this.explode = ((AnimationComponent)paramObjectInput.readObject());
    this.initialRobotPosition = ((Position)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeInt(this.dyingFrame);
    paramObjectOutput.writeInt(this.velocity);
    paramObjectOutput.writeInt(this.fallTime);
    paramObjectOutput.writeInt(this.deltax);
    paramObjectOutput.writeInt(this.xstep);
    paramObjectOutput.writeObject(this.mask);
    paramObjectOutput.writeObject(this.explode);
    paramObjectOutput.writeObject(this.initialRobotPosition);
  }

  public void deathStep(Robot paramRobot)
  {
    Object localObject1;
    int i1;
    Object localObject2;
    Object localObject3;
    Position localPosition = paramRobot.getPosition();
    switch (this.dyingFrame)
    {
    case 0:
      paramRobot.playSound("CliffSlip");

      GameApplet.thisApplet.sendStatusMessage(
        "RCX: " + paramRobot.getName() + " - Freefall detected!\n");

      Point localPoint1 = localPosition.getMapPoint(); Point localPoint2 = null;
      this.initialRobotPosition = new Position(localPosition);
      int i = paramRobot.getOrientation();
      if (i == 270)
      {
        paramRobot.setOrientation(225);
        localPoint2 = new Point(localPoint1.x - 1, localPoint1.y + 1);
      }
      else if (i == 90)
      {
        localPoint2 = new Point(localPoint1.x + 1, localPoint1.y + 1);
        paramRobot.setOrientation(135);
      }
      else
      {
        paramRobot.setOrientation(180);
        int j = 0;
        for (int l = localPoint1.y; (l < paramRobot.getEnvironment().getMap().getSize().height) && (j == 0); ++l)
        {
          Vector localVector = paramRobot.getEnvironment().getObjectAt(localPoint1.x, l);

          if (localVector != null)
          {
            localObject2 = localVector.elements();
            j = 1;
            while (((Enumeration)localObject2).hasMoreElements())
            {
              localObject3 = (PhysicalObject)((Enumeration)localObject2).nextElement();

              if (localObject3 instanceof CliffTrigger)
                j = 0;
            }
            if (j != 0)
              localPoint2 = new Point(localPoint1.x, l);

          }
          else
          {
            j = 1;
            localPoint2 = new Point(localPoint1.x, l);
          }
        }
        if (localPoint2 == null)
        {
          paramRobot.setDead(true);
          return;
        }
      }
      paramRobot.setLayer("Robot Effects");

      localPosition.dy = ((localPosition.y - localPoint2.y) * 50);
      localPosition.x = localPoint2.x;
      localPosition.y = localPoint2.y;
      paramRobot.setPosition(localPosition);

      this.dyingFrame += 1;
      paramRobot.getAnimationComponent(paramRobot.getChassis().getID()).nextImage();
      if (localPosition.dx != 0)
      {
        double d = Math.sqrt(Math.abs(localPosition.dx) * 2 / 2);
        Debug.println((localPosition.dx * 2) + "," + (localPosition.dx * 2 / 2) + "," + d);
        i1 = (int)d;
        Debug.println("time to fall is " + i1);
        try
        {
          this.xstep = (Math.abs(localPosition.dx) / i1);
          Debug.println("xstep each frame is " + this.xstep);
        }
        catch (ArithmeticException localArithmeticException)
        {
          Debug.println("xstep is 0 because of div by zero");
          this.xstep = 0;
        }
        if (localPosition.dx > 0)
          this.deltax = (-this.xstep);
        else
          this.deltax = this.xstep;
      }
      else
      {
        this.xstep = 0;
        this.deltax = 0;
      }
      this.velocity = 2;
      this.fallTime = 0;
      return;
    case 1:
      this.velocity += 2;
      this.fallTime += 1;

      int k = localPosition.dx + this.deltax;

      Debug.println(String.valueOf(localPosition.dy + this.velocity));
      if (localPosition.dy + this.velocity >= 0)
      {
        Debug.println("final position: " + localPosition);
        paramRobot.setPosition(new Position(localPosition.x, localPosition.y, 0, 0));
        this.dyingFrame += 1;
        localObject1 = new Image[23];
        for (i1 = 1; i1 <= 23; ++i1)
          localObject1[(i1 - 1)] = GameApplet.thisApplet.getImage(
            "com/templar/games/stormrunner/media/images/robot/Explosion/r_explode_" + 
            ((java.lang.Integer.toString(i1).length() < 2) ? "0" + i1 : String.valueOf(i1)) + ".gif");
        paramRobot.getAnimationComponents().clear();
        paramRobot.setLayer("Robot");
        paramRobot.getEnvironment().removeObject(paramRobot);
        paramRobot.getEnvironment().addObject(paramRobot);
        localObject2 = { 
          GameApplet.thisApplet.getImage(
          "com/templar/games/stormrunner/media/images/robot/Wreckage/r_wreck_track.gif") };

        paramRobot.setImages(localObject2);
        localObject3 = paramRobot.getEnvironment().getObjectAt(localPosition.getMapPoint());
        if (localObject3 != null)
        {
          localObject4 = ((Vector)localObject3).elements();
          while (((Enumeration)localObject4).hasMoreElements())
          {
            PhysicalObject localPhysicalObject = (PhysicalObject)((Enumeration)localObject4).nextElement();
            if ((localPhysicalObject instanceof Robot) && (paramRobot != localPhysicalObject))
              ((Robot)localPhysicalObject).setDeath(new InstantDeath());
          }
        }
        Object localObject4 = new AnimationComponent[1];
        this.explode = new AnimationComponent(localObject1);
        localObject4[0] = this.explode;
        localObject4[0].setSequence(EXPLOSION_SEQUENCE, null, false);
        this.mask = new Mask(paramRobot.getEnvironment(), 
          new Position(localPosition.x - 1, 
          localPosition.y - 1, 0, 0), 
          null, new boolean[3][3], false);
        this.mask.setLayer("Robot Effects");
        this.mask.setImages(localObject4);
        paramRobot.getEnvironment().addObject(this.mask);
        paramRobot.getEnvironment().getRenderer().repaint();

        paramRobot.playSound("CannonHit");

        return;
      }

      paramRobot.setPosition(new Position(localPosition.x, localPosition.y, 
        k, localPosition.dy + this.velocity));
      paramRobot.getAnimationComponent(paramRobot.getChassis().getID()).nextImage();

      return;
    case 2:
      if (!(this.explode.nextImage()))
      {
        paramRobot.setDead(true);
        paramRobot.getEnvironment().getShroud().setVisible(
          this.initialRobotPosition.getMapPoint(), 2, false, false, true);
        paramRobot.getEnvironment().removeObject(this.mask);
        localObject1 = new Salvage(paramRobot.getEnvironment(), new Position(localPosition.getMapPoint()), paramRobot.getImages(), false);
        [Z[] arrayOf[Z = { { true } };
        ((PhysicalObject)localObject1).setShape(arrayOf[Z);
        ((PhysicalObject)localObject1).setLayer("Robot");
        paramRobot.getEnvironment().addObject((PhysicalObject)localObject1);
        paramRobot.getEnvironment().getRenderer().repaint();
        GameApplet.audio.play("DeathAlarm");
        GameApplet.thisApplet.sendStatusMessage(
          "RCX: " + paramRobot.getName() + " - Contact with RCX lost!\n");
      }
      return;
    }
  }
}