package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.Robot;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class RepeatForever extends Instruction
  implements Loop, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  Instruction destination;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);
    this.destination = ((Instruction)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeObject(this.destination);
  }

  public RepeatForever()
  {
    setDestination(null);
  }

  public boolean verifyRobot(Robot paramRobot)
  {
    return true;
  }

  public void setDestination(Instruction paramInstruction)
  {
    this.destination = paramInstruction;
  }

  public Instruction getDestination()
  {
    return this.destination;
  }

  public boolean execute(Robot paramRobot)
  {
    return true;
  }

  public static String getDescription()
  {
    return "Allows you to infinitely repeat a set of RCX instruction blocks.";
  }

  public boolean boundaryCheck(Robot paramRobot, int paramInt)
  {
    return false; }

  public boolean boundaryCheck(Robot paramRobot) { return false; }

  public void terminate(Robot paramRobot) { }

  public Instruction loopPeek() { return this.destination;
  }

  public Instruction loop() {
    return loopPeek();
  }

  public String toString() {
    return "Repeat Forever";
  }
}