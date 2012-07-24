package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.templarutil.Debug;
import java.applet.AppletContext;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Datalog extends PhysicalObject
  implements Trigger, PortableObject, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  transient GameState state;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternal(paramObjectOutput);
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException
  {
    super.readExternal(paramObjectInput);
  }

  public Datalog()
  {
  }

  public Datalog(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
  }

  public Datalog(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
  }

  public Datalog(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
  }

  public int activateOnEvent() {
    return 1;
  }

  public void activate(Robot paramRobot, int paramInt)
  {
    if ((this.state != null) && (paramRobot != null) && (this.url_target != null) && (this.frame != null))
    {
      if (GameApplet.appletContext != null)
      {
        GameApplet.appletContext.showDocument(this.url_target, this.frame);

        return;
      }

      Debug.println("No AppletContext!");

      return;
    }

    StringBuffer localStringBuffer = new StringBuffer("Datalog: null found - \n");
    localStringBuffer.append("State: ");
    localStringBuffer.append(this.state);
    localStringBuffer.append("\nRobot: ");
    localStringBuffer.append(paramRobot);
    localStringBuffer.append("\nurl_target: ");
    localStringBuffer.append(this.url_target);
    localStringBuffer.append("\nframe: ");
    localStringBuffer.append(this.frame);
    localStringBuffer.append("\n");
    Debug.println(localStringBuffer.toString());
  }

  public void setGameState(GameState paramGameState)
  {
    this.state = paramGameState;
  }
  public String toString() { return getID();
  }
}