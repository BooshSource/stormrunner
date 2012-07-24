package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.RobotPart;
import com.templar.games.stormrunner.sensor.DirectionalSensor;
import java.awt.Component;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

public class TurnAway extends Instruction
  implements Contextualized, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  Object SpecifiedContext;
  DirectionalSensor where;
  public boolean animating;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    super.writeExternal(paramObjectOutput);

    paramObjectOutput.writeObject(this.SpecifiedContext);
    paramObjectOutput.writeObject(this.where);
    paramObjectOutput.writeBoolean(this.animating);
  }

  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);

    this.SpecifiedContext = paramObjectInput.readObject();
    this.where = ((DirectionalSensor)paramObjectInput.readObject());
    this.animating = paramObjectInput.readBoolean();
  }

  public boolean verifyRobot(Robot paramRobot)
  {
    Vector localVector = paramRobot.getSensors();
    boolean b = false;

    for (int j = 0; j < localVector.size(); j++) {
      if ((localVector.elementAt(j) instanceof DirectionalSensor))
        b = true;
    }
    return b;
  }

  public boolean checkContext(Object paramObject)
  {
    return ((paramObject instanceof Conditional)) && ((((Conditional)paramObject).getSensor() instanceof DirectionalSensor));
  }

  public void setContext(Object paramObject)
  {
    this.SpecifiedContext = paramObject;
    if (paramObject != null) {
      this.where = ((DirectionalSensor)((Conditional)paramObject).getSensor());

      return;
    }

    this.where = null;
  }

  public Object getContext()
  {
    return this.SpecifiedContext;
  }

  public boolean execute(Robot paramRobot)
  {
    if (!this.animating)
    {
      int i = this.where.getDirection(paramRobot);

      switch (i) {
      case 0:
        i = 180; break;
      case 180:
        i = 0; break;
      case 90:
        i = 270; break;
      case 270:
        i = 90; break;
      }
      paramRobot.setState(2, i);
    }
    this.animating = TurnInstruction.updateState(paramRobot);
    paramRobot.repaint();
    return !this.animating;
  }

  public void terminate(Robot paramRobot)
  {
    this.animating = false;
  }

  public boolean boundaryCheck(Robot paramRobot, int paramInt)
  {
    return false;
  }

  public boolean boundaryCheck(Robot paramRobot)
  {
    return false;
  }

  public static String getDescription()
  {
    return "Causes the RCX to rotate towards a triggered sensor.";
  }

  public String toString()
  {
    return "Turn Away from " + this.where.toString();
  }
}