package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.assembly.Piledriver;
import com.templar.games.stormrunner.objects.Trigger;
import com.templar.games.stormrunner.templarutil.audio.AudioManager;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Vector;

public class Hammer extends Instruction
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  int state;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternal(paramObjectOutput);

    paramObjectOutput.writeInt(this.state);
  }

  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);

    this.state = paramObjectInput.readInt();
  }

  public boolean verifyRobot(Robot paramRobot)
  {
    return (paramRobot.getAssembly("Piledriver") != null);
  }

  public boolean execute(Robot paramRobot)
  {
    Piledriver localPiledriver = (Piledriver)paramRobot.getAssembly("Piledriver");
    if (localPiledriver == null)
    {
      return true;
    }
    AnimationComponent localAnimationComponent = paramRobot.getAnimationComponent("Piledriver");
    switch (this.state)
    {
    case 0:
      localAnimationComponent.setSequence(localPiledriver.getAnimationSequence(1), null, false);
      localAnimationComponent.reset();
      this.state += 1;
      return false;
    case 1:
      if (!(localAnimationComponent.nextImage()))
      {
        GameApplet.audio.play("Piledriver-Strike");
        localAnimationComponent.setSequence(localPiledriver.getAnimationSequence(2), null, false);
        this.state += 1;
        Position localPosition = paramRobot.inFrontOf();
        Vector localVector = paramRobot.getEnvironment().getObjectAt(localPosition);
        if (localVector != null)
        {
          Enumeration localEnumeration = localVector.elements();
          while (localEnumeration.hasMoreElements())
          {
            PhysicalObject localPhysicalObject = (PhysicalObject)localEnumeration.nextElement();
            if ((localPhysicalObject instanceof Trigger) && 
              ((((Trigger)localPhysicalObject).activateOnEvent() & 0x10) > 0))
            {
              ((Trigger)localPhysicalObject).activate(paramRobot, 16);
            }
          }
        }
      }

      return false;
    case 2:
      if (localAnimationComponent.nextImage())
        break label222;
      this.state = 0;
      return true;
    }

    label222: return false; }

  public boolean boundaryCheck(Robot paramRobot) {
    return false;
  }

  public boolean boundaryCheck(Robot paramRobot, int paramInt) {
    return false;
  }

  public void terminate(Robot paramRobot)
  {
  }

  public String toString()
  {
    return "Bash";
  }
}