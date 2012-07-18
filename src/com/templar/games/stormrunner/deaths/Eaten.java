package com.templar.games.stormrunner.deaths;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.Shroud;
import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Eaten extends Death
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  int dyingFrame;
  int framesToWait;

  public Eaten()
  {
    this.dyingFrame = 0;
    this.framesToWait = 0;
  }

  public Eaten(int paramInt)
  {
    this.framesToWait = paramInt;
  }

  public void deathStep(Robot paramRobot) {
    this.dyingFrame += 1;
    if (this.dyingFrame >= this.framesToWait)
    {
      paramRobot.setDead(true);
      paramRobot.getEnvironment().getShroud().setVisible(
        paramRobot.getPosition().getMapPoint(), 2, false, false, true);
      GameApplet.audio.play("DeathAlarm");
      GameApplet.thisApplet.sendStatusMessage(
        "RCX: " + paramRobot.getName() + " - Unexpected external enclosure!\nContact with RCX lost!");
    }
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException {
    this.dyingFrame = paramObjectInput.readInt();
    this.framesToWait = paramObjectInput.readInt();
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeInt(this.dyingFrame);
    paramObjectOutput.writeInt(this.framesToWait);
  }
}