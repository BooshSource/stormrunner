package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.assembly.GrabberArm;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import java.awt.Component;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ArmPutDown extends Instruction
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected transient AnimationComponent ArmAnimator;
  protected transient GrabberArm Arm;
  protected int state;

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

    switch (this.state)
    {
    case 0:
      if (this.Arm.getCarrying() != null)
      {
        Position localPosition = paramRobot.inFrontOf();
        if (paramRobot.getEnvironment().isObstructed(localPosition.getMapPoint()))
        {
          paramRobot.playSound("Robot-Deny");
          GameApplet.thisApplet.sendStatusMessage(
            "Grabber Arm on RCX: " + paramRobot.getName() + ": Cannot put down something there.\n");
          return true;
        }
        PhysicalObject localPhysicalObject = (PhysicalObject)this.Arm.getCarrying();
        localPhysicalObject.setPosition(localPosition);
        localPhysicalObject.setVisible(false);
        paramRobot.getEnvironment().addObject(localPhysicalObject);
        paramRobot.playSound("Arm-Down");
        this.ArmAnimator.setSequence(this.Arm.getAnimationSequence(1), null, false);
        this.ArmAnimator.reset();
        this.state += 1;
        return false;
      }
      paramRobot.playSound("Robot-Question");
      GameApplet.thisApplet.sendStatusMessage(
        "Grabber Arm on RCX: " + paramRobot.getName() + ": Not holding anything.\n");
      return true;
    case 1:
      if (!(this.ArmAnimator.nextImage()))
      {
        this.ArmAnimator.setSequence(this.Arm.getAnimationSequence(2), null, false);
        this.ArmAnimator.reset();
        ((PhysicalObject)this.Arm.getCarrying()).setVisible(true);
        ((PhysicalObject)this.Arm.getCarrying()).repaint();
        this.state += 1;
      }
      return false;
    case 2:
      if (!(this.ArmAnimator.nextImage()))
        this.state += 1;
      return false;
    case 3:
      this.Arm.setCarrying(null);
      this.ArmAnimator.setSequence(this.Arm.getAnimationSequence(0), null, false);
      this.ArmAnimator.reset();
      this.state = 0;
      return true;
    }

    return true;
  }

  public boolean verifyRobot(Robot paramRobot)
  {
    return (paramRobot.getAssembly("GrabberArm") != null);
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
    return "Put Down";
  }
}