package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.GameState;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.actors.Cannon;
import com.templar.games.stormrunner.templarutil.Debug;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Vector;

public class CannonTrigger extends PhysicalObject
  implements Trigger, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected transient GameState state;
  protected Cannon shooter;

  public void readExternal(ObjectInput paramObjectInput)
    throws ClassNotFoundException, IOException
  {
    super.readExternal(paramObjectInput);
    this.shooter = ((Cannon)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeObject(this.shooter);
  }

  public void initialize()
  {
    if (GameApplet.thisApplet != null)
    {
      Enumeration localEnumeration = super.getEnvironment().getObjects().elements();
      while (localEnumeration.hasMoreElements())
      {
        Object localObject = localEnumeration.nextElement();
        if (localObject instanceof Cannon)
        {
          this.shooter = ((Cannon)localObject);
          return;
        }
      }
    }
  }

  public CannonTrigger()
  {
  }

  public CannonTrigger(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
    initialize();
  }

  public CannonTrigger(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, null, paramBoolean);
    initialize();
  }

  public CannonTrigger(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, null, paramArrayOfBoolean, paramBoolean);
    initialize();
  }

  public void setGameState(GameState paramGameState) {
    this.state = paramGameState;
  }

  public void activate(Robot paramRobot, int paramInt) {
    if (this.shooter != null)
    {
      Debug.println("Robot " + paramRobot.getName() + " has activated CannonTrigger at " + paramRobot.getPosition());
      this.shooter.shootAt(paramRobot, paramRobot.getPosition().getMapPoint()); } }

  public int activateOnEvent() {
    return 4; }

  public void setCannon(Cannon paramCannon) {
    this.shooter = paramCannon;
  }
}