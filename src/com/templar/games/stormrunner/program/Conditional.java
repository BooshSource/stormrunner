package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.sensor.Sensor;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Conditional
  implements Linkable, ConditionalList, Cloneable, Copyable, InstructionList, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected Conditional next;
  protected Conditional prev;
  protected Instruction first;
  protected Sensor watch;

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    paramObjectOutput.writeObject(this.next);
    paramObjectOutput.writeObject(this.prev);
    paramObjectOutput.writeObject(this.first);
    paramObjectOutput.writeObject(this.watch);
  }

  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    this.next = ((Conditional)paramObjectInput.readObject());
    this.prev = ((Conditional)paramObjectInput.readObject());
    this.first = ((Instruction)paramObjectInput.readObject());
    this.watch = ((Sensor)paramObjectInput.readObject());
  }

  public Conditional()
  {
  }

  public Conditional(Sensor paramSensor)
  {
    this.watch = paramSensor;
  }

  public Conditional(Sensor paramSensor, Instruction paramInstruction)
  {
    this.first = paramInstruction;
    this.watch = paramSensor;
  }

  public Sensor getSensor()
  {
    return this.watch;
  }

  public boolean check(Robot paramRobot)
  {
    return this.watch.check(paramRobot);
  }

  public Instruction getStart() {
    return this.first;
  }

  public Object copy()
  {
    try
    {
      return clone();
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {
    }
    return null;
  }

  public Conditional getPreviousConditional()
  {
    return this.prev;
  }

  public Conditional getNextConditional()
  {
    return this.next;
  }

  public void setPreviousConditional(Conditional paramConditional)
  {
    this.prev = paramConditional;
  }

  public void setNextConditional(Conditional paramConditional)
  {
    this.next = paramConditional;
  }
  public void setPreviousInstruction(Instruction paramInstruction) {
  }
  public Instruction getPreviousInstruction() {
    return null;
  }

  public void setNextInstruction(Instruction paramInstruction) {
    this.first = paramInstruction;
  }

  public Instruction getNextInstruction()
  {
    return this.first;
  }

  public boolean detachNext(Linkable paramLinkable)
  {
    boolean b = false;

    if ((paramLinkable instanceof ConditionalList))
    {
      ConditionalList localConditionalList = (ConditionalList)paramLinkable;
      if ((localConditionalList.getPreviousConditional() == this) && (this.next == localConditionalList))
      {
        this.next.setPreviousConditional(null);
        this.next = null;

        b = true;
      }

    }

    return b;
  }

  public boolean attachNext(Linkable paramLinkable)
  {
   boolean b = false;

    if ((paramLinkable instanceof ConditionalList))
    {
      ConditionalList localConditionalList = (ConditionalList)paramLinkable;
      if (localConditionalList.getPreviousConditional() == null)
      {
        localConditionalList.setPreviousConditional(this);
        this.next = ((Conditional)localConditionalList);

        b = true;
      }

    }

    return b;
  }

  public boolean detachPrevious(Linkable paramLinkable)
  {
    boolean b=false;

    if ((paramLinkable instanceof ConditionalList))
    {
      ConditionalList localConditionalList = (ConditionalList)paramLinkable;
      if ((localConditionalList.getNextConditional() == this) && ((this.prev == localConditionalList) || (this.prev == null)))
      {
        if (this.prev != null)
          this.prev.setNextConditional(null);
        else {
          localConditionalList.setNextConditional(null);
        }
        this.prev = null;

        b = true;
      }

    }

    return b;
  }

  public boolean attachPrevious(Linkable paramLinkable)
  {
    boolean b=false;

    if ((paramLinkable instanceof ConditionalList))
    {
      ConditionalList localConditionalList = (ConditionalList)paramLinkable;
      if (localConditionalList.getNextConditional() == null)
      {
        localConditionalList.setNextConditional(this);
        if ((paramLinkable instanceof Conditional))
          this.prev = ((Conditional)paramLinkable);
        else {
          this.prev = null;
        }
        b=true;
      }

    }

    return b;
  }

  public String toString()
  {
    return "Conditional[first:" + this.first + ",watch:" + this.watch + "]";
  }
}