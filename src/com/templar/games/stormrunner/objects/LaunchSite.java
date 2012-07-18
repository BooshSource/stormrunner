package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class LaunchSite extends PhysicalObject
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    super.readExternalWithoutImages(paramObjectInput);
    ((String[])paramObjectInput.readObject());
    setImages(null);
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException
  {
    super.writeExternal(paramObjectOutput);
  }

  public LaunchSite()
  {
  }

  public LaunchSite(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
  }

  public LaunchSite(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, null, paramBoolean);
  }

  public LaunchSite(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, null, paramArrayOfBoolean, paramBoolean);
  }
}