package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.Robot;
import java.awt.Component;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TurnRightInstruction extends Instruction
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public boolean animating;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternal(paramObjectOutput);

    paramObjectOutput.writeBoolean(this.animating);
  }

  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);

    this.animating = paramObjectInput.readBoolean();
  }

  public boolean verifyRobot(Robot paramRobot)
  {
    return true;
  }

  public boolean execute(Robot paramRobot)
  {
    if (!this.animating)
    {
      int i = paramRobot.getOrientation();
      i += 90;
      if (i > 270) i = 0;

      paramRobot.setState(2, i);
    }
    this.animating = TurnInstruction.updateState(paramRobot);
    paramRobot.repaint();
    return !this.animating;
  }

  public static String getDescription() {
    return "Causes the RCX to rotate 90 degrees to the right.";
  }

  public boolean boundaryCheck(Robot paramRobot, int paramInt)
  {
    return false;
  }
  public boolean boundaryCheck(Robot paramRobot) {
    return false;
  }
  public void terminate(Robot paramRobot) {
    this.animating = false;
    paramRobot.setState(0, -1);
    paramRobot.setOrientation(paramRobot.getDestOrientation());
    paramRobot.repaint();
  }

  public String toString() {
    return "Turn Right";
  }
}