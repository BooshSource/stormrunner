package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.deaths.Falling;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class CliffTrigger extends PhysicalObject
  implements HairTrigger, Obstacle, Externalizable
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

  public CliffTrigger()
  {
  }

  public CliffTrigger(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
  }

  public CliffTrigger(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
  }

  public CliffTrigger(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean); }

  public int getThreshold() { return 25; }

  public void setGameState(GameState paramGameState) {
    this.state = paramGameState;
  }

  public void activate(Robot paramRobot, int paramInt) {
    if (paramRobot.getCurrentTrigger() == this)
      return;
    paramRobot.setCurrentTrigger(this);
    if (paramRobot.getElevated())
    {
      paramRobot.setDeath(new Falling());
      paramRobot.stop(); } }

  public int activateOnEvent() {
    return 0;
  }

  public boolean isObstructing(Robot paramRobot)
  {
    return (!(paramRobot.getElevated()));
  }
}