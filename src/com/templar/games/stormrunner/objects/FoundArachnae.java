package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class FoundArachnae extends FoundRobot
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternal(paramObjectOutput);
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException
  {
    super.readExternal(paramObjectInput);
  }

  protected void initialize()
  {
    if (GameApplet.thisApplet != null)
    {
      super.initialize();
      this.initialOrientation = 180;
      this.newName = "Arachnae Prototype";
      this.chassis = "Arachnae";
      String[] arrayOfString = { "sensor.VidSensor", "assembly.Launcher" };
      this.parts = arrayOfString;
      this.clearAnimations = new AnimationComponent[2];
      for (int i = 0; i < 2; i++)
      {
        this.clearAnimations[i] = new AnimationComponent();
        Image[] localObject = new Image[6 + i];
        int[] arrayOfInt = new int[6 + i];
        for (int j = 0; j < 6 + i; j++)
        {
          localObject[j] = GameApplet.thisApplet.getImage(
            "com/templar/games/stormrunner/media/images/robot/buried/buried" + (i + 1) + "_0" + (j + 1) + ".gif");
          arrayOfInt[j] = j;
        }
        this.clearAnimations[i].setCells(localObject);
        this.clearAnimations[i].setSequence(arrayOfInt, null, false);
      }
      ImageComponent[] localObject = new ImageComponent[2];
      localObject[1] = new ImageComponent(GameApplet.thisApplet.getImage("com/templar/games/stormrunner/media/images/robot/chassis/Arachnae/walk0_225.gif"), true, false);
      localObject[0] = this.clearAnimations[0];

      setImages(localObject);
    }
  }

  public FoundArachnae()
  {
	  ////?
  }

  public FoundArachnae(Scene paramScene, Position paramPosition, boolean paramBoolean) {
    super(paramScene, paramPosition, paramBoolean);
  }

  public FoundArachnae(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
  }

  public FoundArachnae(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
  }
}