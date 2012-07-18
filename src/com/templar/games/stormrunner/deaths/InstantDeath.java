package com.templar.games.stormrunner.deaths;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.Robot;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class InstantDeath extends Death
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
  }

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
  }

  public void deathStep(Robot paramRobot)
  {
    paramRobot.setDead(true);

    GameApplet.thisApplet.sendStatusMessage(
      "RCX: " + paramRobot.getName() + " has been destroyed.\n");
  }
}