package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.templarutil.Debug;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BayTrigger extends com.templar.games.stormrunner.PhysicalObject
  implements Ceiling, Trigger, Externalizable
{
  protected GameState state;
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

  public BayTrigger()
  {
  }

  public BayTrigger(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
  }

  public BayTrigger(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
  }

  public BayTrigger(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
  }

  public void setGameState(GameState paramGameState) {
    this.state = paramGameState;
  }

  public int activateOnEvent() {
    return 4;
  }

  public void activate(Robot paramRobot, int paramInt) {
    if (this.state != null)
    {
      if (!(this.state.isRobotActive(paramRobot))) return;

      this.state.deactivateRobot(paramRobot);

      return;
    }

    Debug.println("BayTrigger: no GameState!");
  }
}