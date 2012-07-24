package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.assembly.GrabberArm;
import com.templar.games.stormrunner.objects.Datalog;
import com.templar.games.stormrunner.objects.FoundRobotPart;
import com.templar.games.stormrunner.objects.PortableObject;
import com.templar.games.stormrunner.objects.Trigger;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

public class ArmPickUp extends Instruction
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected transient AnimationComponent ArmAnimator;
  protected transient GrabberArm Arm;
  int state;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeInt(this.state);
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException
  {
    super.readExternal(paramObjectInput);
    this.state = paramObjectInput.readInt();
  }

  public boolean execute(Robot paramRobot)
  {
    this.ArmAnimator = paramRobot.getAnimationComponent("GrabberArm");
    this.Arm = ((GrabberArm)paramRobot.getAssembly("GrabberArm"));
    boolean bool;
    switch (this.state)
    {
    case 0:
      if (this.Arm.getCarrying() == null)
      {
        Position localPosition = paramRobot.inFrontOf();
        Vector localVector = paramRobot.getEnvironment().getObjectAt(localPosition.getMapPoint());
        PortableObject localPortableObject2 = null;

        if (localVector == null)
        {
          paramRobot.playSound("Robot-Question");

          return true;
        }
        for (int i = localVector.size() - 1; i >= 0; i--)
        {
          if (!(localVector.elementAt(i) instanceof PortableObject))
            continue;
          localPortableObject2 = (PortableObject)localVector.elementAt(i);

          this.Arm.setCarrying(localPortableObject2);
          break;
        }

        if (localPortableObject2 == null)
        {
          paramRobot.playSound("Robot-NotCompute");

          return true;
        }
        paramRobot.playSound("Arm-Up");
        this.ArmAnimator.setSequence(this.Arm.getAnimationSequence(1), null, false);
        this.ArmAnimator.reset();
        this.state += 1;
        return false;
      }

      paramRobot.playSound("Robot-NotCompute");
      GameApplet.thisApplet.sendStatusMessage(
        "Grabber Arm on RCX: " + paramRobot.getName() + ": Already holding something.\n");
      return true;
    case 1:
      bool = this.ArmAnimator.nextImage();

      if (!bool)
      {
        paramRobot.getEnvironment().removeObject((PhysicalObject)this.Arm.getCarrying());

        this.ArmAnimator.setSequence(this.Arm.getAnimationSequence(2), null, false);
        this.ArmAnimator.reset();
        this.state += 1;
      }
      return false;
    case 2:
      bool = this.ArmAnimator.nextImage();

      if (!bool)
        this.state += 1;
      return false;
    case 3:
      this.ArmAnimator.setSequence(this.Arm.getAnimationSequence(0), null, false);
      this.ArmAnimator.reset();

      PortableObject localPortableObject1 = this.Arm.getCarrying();

      if (((localPortableObject1 instanceof FoundRobotPart)) || ((localPortableObject1 instanceof Datalog)))
      {
        paramRobot.playSound("Robot-Alert");
      }

      if (((localPortableObject1 instanceof Trigger)) && 
        ((((Trigger)localPortableObject1).activateOnEvent() & 0x1) > 0)) {
        ((Trigger)localPortableObject1).activate(paramRobot, 1);
      }
      this.state = 0;
      return true;
    }

    return true;
  }

  public boolean verifyRobot(Robot paramRobot)
  {
    return paramRobot.getAssembly("GrabberArm") != null;
  }

  public boolean boundaryCheck(Robot paramRobot, int paramInt)
  {
    return false;
  }

  public boolean boundaryCheck(Robot paramRobot)
  {
    return false;
  }

  public void terminate(Robot paramRobot)
  {
  }

  public String toString()
  {
    return "Pick up";
  }
}