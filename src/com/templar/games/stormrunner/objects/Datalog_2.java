package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.program.Launch;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Vector;

public class Datalog_2 extends Datalog
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

  public Datalog_2()
  {
  }

  public Datalog_2(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
  }

  public Datalog_2(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
  }

  public Datalog_2(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
  }

  public void activate(Robot paramRobot, int paramInt) {
    super.activate(paramRobot, paramInt);
    if (this.state != null)
    {
      Vector localVector = this.state.getSpecialProgramParts();
      Enumeration localEnumeration = localVector.elements();
      while (localEnumeration.hasMoreElements())
        if ((localEnumeration.nextElement() instanceof Launch))
          return;
      localVector.addElement(new Launch());
    }
  }

  public void setGameState(GameState paramGameState) {
    this.state = paramGameState;
  }
  public String toString() { return "Datalog 2";
  }
}