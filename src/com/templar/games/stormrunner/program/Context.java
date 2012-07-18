package com.templar.games.stormrunner.program;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Context
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public Conditional conditional;
  public Loop loopblock;
  public Instruction instruction;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.conditional = ((Conditional)paramObjectInput.readObject());
    this.loopblock = ((Loop)paramObjectInput.readObject());
    this.instruction = ((Instruction)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeObject(this.conditional);
    paramObjectOutput.writeObject(this.loopblock);
    paramObjectOutput.writeObject(this.instruction);
  }

  public Context()
  {
  }

  public Context(Conditional paramConditional, Instruction paramInstruction)
  {
    this.conditional = paramConditional;
    this.instruction = paramInstruction;
  }

  public Context(Loop paramLoop, Instruction paramInstruction) {
    this.loopblock = paramLoop;
    this.instruction = paramInstruction;
  }
}