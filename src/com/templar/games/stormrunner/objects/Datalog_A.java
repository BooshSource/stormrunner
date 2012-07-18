package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Datalog_A extends Datalog
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

  public Datalog_A()
  {
  }

  public Datalog_A(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
  }

  public Datalog_A(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
  }

  public Datalog_A(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
  }

  public void activate(Robot paramRobot, int paramInt)
  {
    if ((this.state != null) && 
      (this.state.getSecurityLevel() < 2))
      this.state.setSecurityLevel(2);

    super.activate(paramRobot, paramInt); }

  public int activateOnEvent() { return 1; } 
  public void setGameState(GameState paramGameState) { this.state = paramGameState; } 
  public String toString() { return "Datalog A";
  }
}