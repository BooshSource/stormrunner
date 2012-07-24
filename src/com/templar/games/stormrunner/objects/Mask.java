package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.Actor;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import java.awt.Component;
import java.awt.Image;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;

public class Mask extends Actor
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  boolean runOnTick;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);
    this.runOnTick = paramObjectInput.readBoolean();
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeBoolean(this.runOnTick);
  }

  public Mask()
  {
  }

  public Mask(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
    this.runOnTick = paramBoolean;
  }

  public Mask(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    this.runOnTick = paramBoolean;
  }

  public Mask(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    this.runOnTick = paramBoolean;
    for (int i = 0; i < paramArrayOfBoolean.length; i++)
      for (int j = 0; j < paramArrayOfBoolean[0].length; j++)
        paramArrayOfBoolean[i][j] = true;
    setShape(paramArrayOfBoolean);
  }

  public Mask(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean, URL paramURL, String paramString) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    for (int i = 0; i < paramArrayOfBoolean.length; i++)
      for (int j = 0; j < paramArrayOfBoolean[0].length; j++)
        paramArrayOfBoolean[i][j] = true;
    setShape(paramArrayOfBoolean);
    setClickTarget(paramURL, paramString);
  }

  public void tick() {
    if ((this.runOnTick) && (isVisible()))
    {
      ImageComponent[] arrayOfImageComponent = getImageComponents();
      int i = 0; int j = 0;
      for (int k = 0; k < arrayOfImageComponent.length; k++)
      {
        if (!(arrayOfImageComponent[k] instanceof AnimationComponent))
          continue;
        i++;
        if (!((AnimationComponent)arrayOfImageComponent[k]).nextImage()) {
          j++;
        }
      }
      if ((j == i) && (i != 0))
      {
        getEnvironment().removeObject(this);
      }
    }
  }
}