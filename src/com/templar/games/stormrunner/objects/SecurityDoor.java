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
import java.awt.Image;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SecurityDoor extends Actor
  implements Obstacle, Externalizable, Door
{
  static final long serialVersionUID = 4886718345L;
  public static final int SECURITY_LEVEL = 2;
  boolean open;
  boolean active;
  AnimationComponent door;
  transient GameState state;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.active = paramObjectInput.readBoolean();
    this.open = paramObjectInput.readBoolean();
    if ((this.open) || (this.active))
      super.readExternalWithoutImages(paramObjectInput);
    else
      super.readExternal(paramObjectInput);
    this.door = ((AnimationComponent)paramObjectInput.readObject());
    if ((this.open) || (this.active))
    {
      AnimationComponent[] arrayOfAnimationComponent = { this.door };
      setImages(arrayOfAnimationComponent);
    }
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeBoolean(this.active);
    paramObjectOutput.writeBoolean(this.open);
    if ((this.open) || (this.active))
      super.writeExternalWithoutImages(paramObjectOutput);
    else
      super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeObject(this.door);
  }

  public SecurityDoor()
  {
    initialize();
  }

  public SecurityDoor(Scene paramScene, Position paramPosition, boolean paramBoolean) {
    super(paramScene, paramPosition, paramBoolean);
    initialize();
  }

  public SecurityDoor(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    initialize();
  }

  public SecurityDoor(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
    initialize();
  }

  protected void initialize() {
    this.open = false;
    this.active = false;
  }

  public boolean isObstructing(Robot paramRobot) {
    if (GameApplet.thisApplet.getGameState().getSecurityLevel() >= 2)
    {
      if ((!this.open) && (!this.active))
      {
        GameApplet.thisApplet.sendStatusMessage(
          "RCX: " + paramRobot.getName() + " attempting to open a security door...\n\td1000]Succeeded, access granted.\n");

        this.door = new AnimationComponent();
        Image[] arrayOfImage = new Image[5];
        int[] arrayOfInt = new int[5];
        for (int i = 0; i < 5; i++)
        {
          arrayOfImage[i] = GameApplet.thisApplet.getImage(
            "com/templar/games/stormrunner/media/images/spaceships/tormod/tormud_door_0" + i + ".gif");
          arrayOfInt[i] = i;
        }
        this.door.setCells(arrayOfImage);
        this.door.setSequence(arrayOfInt, null, false);
        AnimationComponent[] arrayOfAnimationComponent = new AnimationComponent[1];
        arrayOfAnimationComponent[0] = this.door;
        setImages(arrayOfAnimationComponent);
        this.active = true;
      }
    }
    else {
      GameApplet.thisApplet.sendStatusMessage(
        "RCX: " + paramRobot.getName() + " attempting to open a security door...\n\td1000]Failed, insufficient access.\n");
    }
    return !this.open;
  }

  public void tick() {
    if ((this.active) && 
      (!this.door.nextImage()))
    {
      this.open = true;
      this.active = false;
    }
  }
}