package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.sensor.GeolabSensor;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class FoundGeolabSensor extends FoundRobotPart
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

  public FoundGeolabSensor()
  {
    initialize();
  }

  public FoundGeolabSensor(Scene paramScene, Position paramPosition, boolean paramBoolean) {
    super(paramScene, paramPosition, paramBoolean);
    initialize();
  }

  public FoundGeolabSensor(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    initialize();
  }

  public FoundGeolabSensor(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
    initialize();
  }

  public void initialize() {
    this.thisPart = new GeolabSensor();
  }
}