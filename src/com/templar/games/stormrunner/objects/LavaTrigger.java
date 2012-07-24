package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.deaths.Melted;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class LavaTrigger extends PhysicalObject
  implements Trigger, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  GameState state;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternal(paramObjectOutput);
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException
  {
    super.readExternal(paramObjectInput);
  }

  public LavaTrigger()
  {
  }

  public LavaTrigger(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
  }

  public LavaTrigger(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, null, paramBoolean);
  }

  public LavaTrigger(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, null, paramArrayOfBoolean, paramBoolean);
  }

  public void setGameState(GameState paramGameState) {
    this.state = paramGameState;
  }

  public void activate(Robot paramRobot, int paramInt)
  {
    paramRobot.stop();
    paramRobot.setDeath(new Melted());
  }
  public int activateOnEvent() { return 4;
  }
}