package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.Robot;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintStream;

public abstract class Instruction
  implements Linkable, Cloneable, Copyable, InstructionList, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  Instruction next;
  Instruction prev;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    paramObjectOutput.writeObject(this.next);
    paramObjectOutput.writeObject(this.prev);
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException {
    this.next = ((Instruction)paramObjectInput.readObject());
    this.prev = ((Instruction)paramObjectInput.readObject());
  }

  public Object copy()
  {
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {
    }
    return null;
  }

  public abstract boolean execute(Robot paramRobot);

  public abstract boolean verifyRobot(Robot paramRobot);

  public Instruction getNextInstruction() {
    return this.next; } 
  public Instruction getPreviousInstruction() { return this.prev;
  }

  public void setNextInstruction(Instruction paramInstruction) {
    this.next = paramInstruction;
  }

  public void setPreviousInstruction(Instruction paramInstruction)
  {
    this.prev = paramInstruction;
  }

  public boolean detachNext(Linkable paramLinkable)
  {
    int i = 0;

    if (paramLinkable instanceof InstructionList)
    {
      InstructionList localInstructionList = (InstructionList)paramLinkable;
      if ((localInstructionList.getPreviousInstruction() == this) && (this.next == localInstructionList))
      {
        this.next.setPreviousInstruction(null);
        this.next = null;

        i = 1;
      }
    }

    return i;
  }

  public boolean attachNext(Linkable paramLinkable)
  {
    int i = 0;

    if (paramLinkable instanceof InstructionList)
    {
      InstructionList localInstructionList = (InstructionList)paramLinkable;
      if (localInstructionList.getPreviousInstruction() == null)
      {
        localInstructionList.setPreviousInstruction(this);
        this.next = ((Instruction)localInstructionList);

        i = 1;
      }
    }

    return i;
  }

  public boolean detachPrevious(Linkable paramLinkable)
  {
    int i = 0;

    if (paramLinkable instanceof InstructionList)
    {
      InstructionList localInstructionList = (InstructionList)paramLinkable;

      if (localInstructionList.getNextInstruction() == this)
      {
        if ((this.prev == localInstructionList) || (this.prev == null))
        {
          if (this.prev != null)
            this.prev.setNextInstruction(null);
          else
            localInstructionList.setNextInstruction(null);

          this.prev = null;

          i = 1;
        }
        else {
          System.out.println("Instruction: Attempt to detach from an instruction we are not attached to.");
        }
      }
      else System.out.println("Instruction: Attempt to detach from an Instruction which not attached to us.");
    }
    else {
      System.out.println("Instruction: Attempt to detach from an incompatible Linkable.");
    }
    return i;
  }

  public boolean attachPrevious(Linkable paramLinkable)
  {
    int i = 0;

    if (paramLinkable instanceof InstructionList)
    {
      InstructionList localInstructionList = (InstructionList)paramLinkable;
      if (localInstructionList.getNextInstruction() == null)
      {
        localInstructionList.setNextInstruction(this);
        if (paramLinkable instanceof Instruction)
          this.prev = ((Instruction)localInstructionList);
        else
          this.prev = null;

        i = 1;
      }

    }
    else
    {
      System.out.println("Instruction: Attempt to attach to an incompatible Linkable.");
    }
    return i;
  }

  public static String getDescription()
  {
    return "An abstract instruction";
  }

  public abstract boolean boundaryCheck(Robot paramRobot, int paramInt);

  public abstract boolean boundaryCheck(Robot paramRobot);

  public abstract void terminate(Robot paramRobot);
}