package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.InventoryContainer;
import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.RobotPart;
import com.templar.games.stormrunner.assembly.Assembly;
import com.templar.games.stormrunner.assembly.GrabberArm;
import com.templar.games.stormrunner.objects.PortableObject;
import com.templar.games.stormrunner.templarutil.gui.AnimationComponent;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;

public class ArmRetrieve extends Instruction
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected transient AnimationComponent ArmAnimator;
  protected transient GrabberArm Arm;
  protected transient AnimationComponent RearAnimator;
  protected transient Assembly Rear;
  int state;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeInt(this.state);
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException {
    super.readExternal(paramObjectInput);
    this.state = paramObjectInput.readInt();
  }

  public boolean execute(Robot paramRobot)
  {
    Object localObject;
    this.ArmAnimator = paramRobot.getAnimationComponent("GrabberArm");
    this.Arm = ((GrabberArm)paramRobot.getAssembly("GrabberArm"));

    this.Rear = paramRobot.getAssembly(1);
    this.RearAnimator = paramRobot.getAnimationComponent(this.Rear.getID());

    if (this.Rear == null)
    {
      return true;
    }
    if (!(this.Rear instanceof InventoryContainer))
    {
      return true;
    }

    switch (this.state)
    {
    case 0:
      if (this.Arm.getCarrying() == null)
      {
        localObject = (InventoryContainer)this.Rear;
        if (((InventoryContainer)localObject).isEmpty())
        {
          paramRobot.playSound("Robot-Question");
          GameApplet.thisApplet.sendStatusMessage(
            "Grabber Arm on RCX: " + paramRobot.getName() + ": " + this.Rear.getID() + " is empty.\n");
          return true;
        }
        PhysicalObject localPhysicalObject = ((InventoryContainer)localObject).transferOut(null);
        this.Arm.setCarrying((PortableObject)localPhysicalObject);
        paramRobot.playSound("Arm-Retrieve");
        this.ArmAnimator.setSequence(this.Arm.getAnimationSequence(3), null, false);
        try
        {
          Field localField2 = this.Rear.getClass().getField("OPEN");
          this.RearAnimator.setSequence(this.Rear.getAnimationSequence(localField2.getInt(this.Rear)), null, false);
        }
        catch (Exception localException3)
        {
          localException3.printStackTrace();
          return true;
        }
        this.ArmAnimator.reset();
        this.RearAnimator.reset();
        this.state += 1;
        return false;
      }
      paramRobot.playSound("Robot-NotCompute");
      GameApplet.thisApplet.sendStatusMessage(
        "Grabber Arm on RCX: " + paramRobot.getName() + ": Already holding something.\n");
      return true;
    case 1:
      this.RearAnimator.nextImage();
      if (!(this.ArmAnimator.nextImage()))
      {
        this.ArmAnimator.setSequence(this.Arm.getAnimationSequence(4), null, false);
        try
        {
          localObject = this.Rear.getClass().getField("CLOSE");
          this.RearAnimator.setSequence(this.Rear.getAnimationSequence(((Field)localObject).getInt(this.Rear)), null, false);
        }
        catch (Exception localException1)
        {
          localException1.printStackTrace();
          return true;
        }
        this.ArmAnimator.reset();
        this.RearAnimator.reset();
        this.state += 1;
      }
      return false;
    case 2:
      this.RearAnimator.nextImage();
      if (!(this.ArmAnimator.nextImage()))
        this.state += 1;
      return false;
    case 3:
      this.ArmAnimator.setSequence(this.Arm.getAnimationSequence(0), null, false);
      try
      {
        Field localField1 = this.Rear.getClass().getField("STILL");
        this.RearAnimator.setSequence(this.Rear.getAnimationSequence(localField1.getInt(this.Rear)), null, false);
      }
      catch (Exception localException2)
      {
        localException2.printStackTrace();
        return true;
      }
      this.ArmAnimator.reset();
      this.RearAnimator.reset();
      this.state = 0;
      return true;
    }

    return true;
  }

  public boolean verifyRobot(Robot paramRobot)
  {
    return ((paramRobot.getAssembly("GrabberArm") != null) && (paramRobot.getAssembly(1) != null));
  }

  public boolean boundaryCheck(Robot paramRobot)
  {
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
    return "Retrieve";
  }
}