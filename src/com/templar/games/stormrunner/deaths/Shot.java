package com.templar.games.stormrunner.deaths;

import [Z;
import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.Shroud;
import com.templar.games.stormrunner.objects.Mask;
import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Component;
import java.awt.Image;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Vector;

public class Shot extends Death
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  int dyingFrame;
  AnimationComponent explode;
  Mask fx;

  public void deathStep(Robot paramRobot)
  {
    if (this.dyingFrame == 0)
    {
      Object localObject;
      this.explode = new AnimationComponent();
      AnimationComponent[] arrayOfAnimationComponent = new AnimationComponent[1];
      arrayOfAnimationComponent[0] = this.explode;
      Image[] arrayOfImage1 = new Image[23];
      for (int i = 1; i <= 23; ++i)
        arrayOfImage1[(i - 1)] = GameApplet.thisApplet.getImage(
          "com/templar/games/stormrunner/media/images/robot/Explosion/r_explode_" + 
          ((java.lang.Integer.toString(i).length() < 2) ? "0" + i : String.valueOf(i)) + ".gif");
      arrayOfAnimationComponent[0].setCells(arrayOfImage1);
      arrayOfAnimationComponent[0].setSequence(Falling.EXPLOSION_SEQUENCE, null, false);
      arrayOfAnimationComponent[0].reset();
      this.fx = new Mask(paramRobot.getEnvironment(), 
        new Position(paramRobot.getPosition().x - 1, 
        paramRobot.getPosition().y - 1, 
        paramRobot.getPosition().dx, 
        paramRobot.getPosition().dy), 
        null, new boolean[3][3], false);
      this.fx.setImages(arrayOfAnimationComponent);
      this.fx.setLayer("Robot Effects");
      paramRobot.getEnvironment().addObject(this.fx);
      paramRobot.getEnvironment().getRenderer().repaint();
      paramRobot.setVisible(false);
      Image[] arrayOfImage2 = new Image[1];
      arrayOfImage2[0] = GameApplet.thisApplet.getImage(
        "com/templar/games/stormrunner/media/images/objects/g_crater.gif");
      [Z[] arrayOf[Z = { { true } };
      Vector localVector = paramRobot.getEnvironment().getObjectOfTypeAt(
        paramRobot.getPosition(), new Mask().getClass());
      int j = 0;
      if (localVector != null)
      {
        localObject = localVector.elements();
        while (((Enumeration)localObject).hasMoreElements())
        {
          Mask localMask = (Mask)((Enumeration)localObject).nextElement();

          if (localMask.getID().compareTo("crater") == 0)
          {
            localMask.setVisible(true);
            j = 1;
            break;
          }
        }
      }
      if (j == 0)
      {
        localObject = new Mask(paramRobot.getEnvironment(), 
          new Position(paramRobot.getPosition()), 
          arrayOfImage2, arrayOf[Z, false);
        ((PhysicalObject)localObject).setLayer("Ground Effects");
        ((PhysicalObject)localObject).setID("crater");
        paramRobot.getEnvironment().addObject((PhysicalObject)localObject);
        paramRobot.getEnvironment().getRenderer().repaint();
      }
      this.dyingFrame += 1;

      return;
    }

    if (!(this.explode.nextImage()))
    {
      paramRobot.setDead(true);
      paramRobot.getEnvironment().getShroud().setVisible(
        paramRobot.getPosition().getMapPoint(), 2, false, false, true);

      GameApplet.audio.play("DeathAlarm");
      GameApplet.thisApplet.sendStatusMessage(
        "RCX: " + paramRobot.getName() + " - Ballistic damage!\nContact with RCX lost!\n");
      paramRobot.getEnvironment().removeObject(this.fx);
    }
  }

  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    this.dyingFrame = paramObjectInput.readInt();
    this.explode = ((AnimationComponent)paramObjectInput.readObject());
    this.fx = ((Mask)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeInt(this.dyingFrame);
    paramObjectOutput.writeObject(this.explode);
    paramObjectOutput.writeObject(this.fx);
  }
}