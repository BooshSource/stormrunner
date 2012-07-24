package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import java.awt.Image;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Vector;

public class DisappearingObject extends PhysicalObject
  implements Trigger, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  GameState state;
  private boolean Visible = true;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeBoolean(this.Visible);
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException
  {
    super.readExternal(paramObjectInput);
    this.Visible = paramObjectInput.readBoolean();

    setVisible(this.Visible);
  }

  public DisappearingObject()
  {
  }

  public int activateOnEvent()
  {
    return 12;
  }

  public void activate(Robot paramRobot, int paramInt) {
    Vector[][] arrayOfVector = getEnvironment().getObjectMap();
    boolean bool = true;
    int i = this.location.x; for (int j = 0; j < this.shape.length; j++)
    {
      int k = this.location.y; for (int m = 0; m < this.shape[0].length; m++)
      {
        if (this.shape[j][m] == this)
        {
          if (arrayOfVector[i][k] != null)
          {
            Enumeration localEnumeration = arrayOfVector[i][k].elements();
            while (localEnumeration.hasMoreElements())
            {
              if (!(localEnumeration.nextElement() instanceof Robot))
                continue;
              bool = false;
            }
          }
        }
        k++;
      }
      i++;
    }

    setVisible(bool);
  }

  public void setGameState(GameState paramGameState) {
    this.state = paramGameState;
  }

  public DisappearingObject(Scene paramScene, Position paramPosition, boolean paramBoolean) {
    super(paramScene, paramPosition, paramBoolean);
  }

  public DisappearingObject(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
  }

  public DisappearingObject(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
  }

  public void setVisible(boolean paramBoolean)
  {
    super.setVisible(paramBoolean);

    this.Visible = paramBoolean;
  }
}