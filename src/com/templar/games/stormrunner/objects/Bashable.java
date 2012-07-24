package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Bashable extends PhysicalObject
  implements Obstacle, Trigger, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  transient GameState state;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternal(paramObjectOutput);
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException
  {
    super.readExternal(paramObjectInput);
  }

  public Bashable()
  {
  }

  public Bashable(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
  }

  public Bashable(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
  }

  public Bashable(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
  }

  public int activateOnEvent() {
    return 16;
  }

  public void activate(Robot paramRobot, int paramInt)
  {
    try {
      UtilityThread localUtilityThread = new UtilityThread(100, this, getClass().getMethod("goAway", null), false);
      localUtilityThread.start();

      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public boolean goAway()
  {
    GameApplet localGameApplet = GameApplet.thisApplet;
    Image[] arrayOfImage = { 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/objects/g_salvage4.gif"), 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/objects/g_salvage3.gif"), 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/objects/g_salvage2.gif"), 
      localGameApplet.getImage("com/templar/games/stormrunner/media/images/objects/g_salvage1.gif") };

    int[] arrayOfInt = { 0, 1, 2, 3 };
    AnimationComponent[] arrayOfAnimationComponent = new AnimationComponent[1];
    arrayOfAnimationComponent[0] = new AnimationComponent(arrayOfImage);
    arrayOfAnimationComponent[0].setSequence(arrayOfInt, null, false);
    setImages(arrayOfAnimationComponent);
    for (int i = 0; i < 4; arrayOfAnimationComponent[0].nextImage())
    {
      try {
        Thread.currentThread(); Thread.sleep(100L);
      }
      catch (InterruptedException localInterruptedException)
      {
      }
      i++;
    }

    getEnvironment().removeObject(this);
    setVisible(false);
    return false;
  }

  public void setGameState(GameState paramGameState) {
    this.state = paramGameState;
  }

  public boolean isObstructing(Robot paramRobot) {
    return true;
  }
}