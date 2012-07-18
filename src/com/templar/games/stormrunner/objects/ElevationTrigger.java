package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ElevationTrigger extends PhysicalObject
  implements Trigger, Externalizable
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

  public ElevationTrigger()
  {
  }

  public ElevationTrigger(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
  }

  public ElevationTrigger(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, null, paramBoolean);
  }

  public ElevationTrigger(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, null, paramArrayOfBoolean, paramBoolean);
  }

  public void activate(Robot paramRobot, int paramInt) {
    if (paramInt == 4) {
      paramRobot.setElevated(true);

      return;
    }

    paramRobot.setElevated(false); }

  public int activateOnEvent() {
    return 12;
  }

  public void setGameState(GameState paramGameState)
  {
  }
}