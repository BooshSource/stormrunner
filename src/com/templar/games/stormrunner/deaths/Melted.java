package com.templar.games.stormrunner.deaths;

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
import com.templar.games.stormrunner.templarutil.util.OrderedTable;
import java.awt.Component;
import java.awt.Image;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Melted extends Death
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  int dyingFrame = 0;
  AnimationComponent[] comp;
  Mask fx;

  public void deathStep(Robot paramRobot)
  {
    Image[] arrayOfImage;
    int i;
    switch (this.dyingFrame)
    {
    case 0:
      GameApplet.thisApplet.sendStatusMessage(
        "RCX: " + paramRobot.getName() + " - External temperature exceeds design limits!\n");

      paramRobot.playSound("Robot-Scream");
      paramRobot.playSound("FieryDeath");

      paramRobot.getAnimationComponents().clear();
      this.comp = new AnimationComponent[1];
      this.comp[0] = new AnimationComponent();
      arrayOfImage = new Image[9];
      for (i = 0; i < 5; i++)
        arrayOfImage[i] = GameApplet.thisApplet.getImage(
          "com/templar/games/stormrunner/media/images/robot/melting/r_firedeath_a0" + (i + 1) + ".gif");
      for (int j = 0; j < 4; j++)
        arrayOfImage[(5 + j)] = GameApplet.thisApplet.getImage(
          "com/templar/games/stormrunner/media/images/robot/melting/r_firedeath_b0" + (j + 1) + ".gif");
      this.comp[0].setCells(arrayOfImage);
      int[] arrayOfInt = { 0, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 5, 6, 7, 8 };
      this.comp[0].setSequence(arrayOfInt, null, false);
      this.comp[0].reset();
      this.fx = new Mask(paramRobot.getEnvironment(), 
        new Position(paramRobot.getPosition().x, 
        paramRobot.getPosition().y - 1, 0, 0), 
        null, new boolean[1][2], false);
      this.fx.setImages(this.comp);
      this.fx.setLayer("Robot Effects");
      paramRobot.getEnvironment().addObject(this.fx);
      paramRobot.getEnvironment().getRenderer().repaint();

      this.dyingFrame += 1;
      return;
    case 1:
      if (this.comp[0].nextImage())
        break;
      paramRobot.getEnvironment().removeObject(this.fx);
      arrayOfImage = new Image[23];
      for (i = 1; i <= 23; i++)
        arrayOfImage[(i - 1)] = GameApplet.thisApplet.getImage(
          "com/templar/games/stormrunner/media/images/robot/Explosion/r_explode_" + (
          Integer.toString(i).length() < 2 ? "0" + i : String.valueOf(i)) + ".gif");
      this.comp[0].reset();
      this.comp[0].setCells(arrayOfImage);
      this.comp[0].setSequence(Falling.EXPLOSION_SEQUENCE, null, false);
      this.fx = new Mask(paramRobot.getEnvironment(), 
        new Position(paramRobot.getPosition().x - 1, 
        paramRobot.getPosition().y - 1, 0, 0), 
        null, new boolean[3][3], false);
      this.fx.setImages(this.comp);
      this.fx.setLayer("Robot Effects");
      paramRobot.getEnvironment().addObject(this.fx);
      paramRobot.getEnvironment().getRenderer().repaint();
      this.dyingFrame += 1;

      return;
    case 2:
    case 3:
    case 4:
      this.comp[0].nextImage();
      this.dyingFrame += 1;
      return;
    case 5:
      this.comp[0].nextImage();
      this.dyingFrame += 1;
      paramRobot.setVisible(false);
      return;
    case 6:
      if (this.comp[0].nextImage())
        break;
      paramRobot.setDead(true);
      paramRobot.getEnvironment().getShroud().setVisible(
        paramRobot.getPosition().getMapPoint(), 2, false, false, true);

      GameApplet.audio.play("DeathAlarm");
      GameApplet.thisApplet.sendStatusMessage(
        "RCX: " + paramRobot.getName() + " - Contact with RCX lost!\n");

      return;
    }
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException
  {
    this.dyingFrame = paramObjectInput.readInt();
    this.comp = ((AnimationComponent[])paramObjectInput.readObject());
    this.fx = ((Mask)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeInt(this.dyingFrame);
    paramObjectOutput.writeObject(this.comp);
    paramObjectOutput.writeObject(this.fx);
  }
}