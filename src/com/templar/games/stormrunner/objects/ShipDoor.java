package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.Actor;
import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Image;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ShipDoor extends Actor
  implements Trigger, Externalizable, Obstacle, Door
{
  static final long serialVersionUID = 4886718345L;
  transient GameState state;
  transient UtilityThread ut;
  AnimationComponent[] dest;
  boolean down = false;
  boolean falling = false;

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException
  {
    super.readExternal(paramObjectInput);
    this.dest = ((AnimationComponent[])paramObjectInput.readObject());
    this.down = paramObjectInput.readBoolean();
    this.falling = paramObjectInput.readBoolean();

    if ((this.down) || (this.falling))
      setImages(this.dest);
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeObject(this.dest);
    paramObjectOutput.writeBoolean(this.down);
    paramObjectOutput.writeBoolean(this.falling);
  }

  public ShipDoor()
  {
  }

  public ShipDoor(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
  }

  public ShipDoor(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
  }

  public ShipDoor(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
  }

  public int activateOnEvent() {
    return 16;
  }

  public void activate(Robot paramRobot, int paramInt) {
    if ((!this.down) && (!this.falling))
    {
      GameApplet localGameApplet = GameApplet.thisApplet;
      Image[] arrayOfImage = new Image[8];
      int[] arrayOfInt = new int[8];
      for (int i = 0; i < 8; i++)
      {
        arrayOfImage[i] = localGameApplet.getImage("com/templar/games/stormrunner/media/images/spaceships/decatur/decatur_door_0" + i + ".gif");
        arrayOfInt[i] = i;
      }
      this.dest = new AnimationComponent[1];
      this.dest[0] = new AnimationComponent(arrayOfImage);
      this.dest[0].setSequence(arrayOfInt, null, false);
      setImages(this.dest);
      this.falling = true;
    }
  }

  public void tick() {
    if (this.falling)
    {
      this.falling = this.dest[0].nextImage();
      if (!this.falling)
        this.down = true;
    }
  }

  public boolean isObstructing(Robot paramRobot) {
    return !this.down;
  }

  public void setGameState(GameState paramGameState)
  {
  }
}