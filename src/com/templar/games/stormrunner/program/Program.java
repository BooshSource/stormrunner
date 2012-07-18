package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import com.templar.games.stormrunner.objects.HairTrigger;
import com.templar.games.stormrunner.objects.Trigger;
import com.templar.games.stormrunner.sensor.DirectionalSensor;
import com.templar.games.stormrunner.templarutil.Debug;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

public class Program
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected Instruction FirstInstruction;
  protected Instruction pc;
  protected Conditional FirstConditional;
  protected Stack execution;
  protected boolean executing;
  static Class triggerClass;
  static Class hairTriggerClass;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.FirstInstruction = ((Instruction)paramObjectInput.readObject());
    this.pc = ((Instruction)paramObjectInput.readObject());
    this.FirstConditional = ((Conditional)paramObjectInput.readObject());
    this.execution = ((Stack)paramObjectInput.readObject());
    this.executing = paramObjectInput.readBoolean();
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeObject(this.FirstInstruction);
    paramObjectOutput.writeObject(this.pc);
    paramObjectOutput.writeObject(this.FirstConditional);
    paramObjectOutput.writeObject(this.execution);
    paramObjectOutput.writeBoolean(this.executing);
  }

  public Program()
  {
    this.executing = false;
    this.execution = new Stack();
    this.FirstInstruction = null;
    this.FirstConditional = null;
    this.pc = null;
    if (triggerClass == null)
      try
      {
        triggerClass = Class.forName("com.templar.games.stormrunner.objects.Trigger");
        hairTriggerClass = Class.forName("com.templar.games.stormrunner.objects.HairTrigger");

        return;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        localClassNotFoundException.printStackTrace();

        return;
      }
  }

  public Program copy()
  {
    if (this.executing) {
      return null;
    }

    Program localProgram = new Program();

    localProgram.setFirstInstruction(copyInstruction(this.FirstInstruction, null));
    localProgram.setFirstConditional(copyConditional(this.FirstConditional, null));

    return localProgram;
  }

  protected Instruction copyInstruction(Instruction paramInstruction, InstructionList paramInstructionList)
  {
    if (paramInstruction == null)
    {
      return null;
    }

    Instruction localInstruction = (Instruction)paramInstruction.copy();

    if (paramInstructionList instanceof Instruction)
      localInstruction.setPreviousInstruction((Instruction)paramInstructionList);
    else
      localInstruction.setPreviousInstruction(null);

    if ((paramInstruction instanceof Loop) && (((Loop)paramInstruction).getDestination() != null))
      ((Loop)localInstruction).setDestination((Instruction)((Loop)paramInstruction).getDestination().copy());

    localInstruction.setNextInstruction(copyInstruction(localInstruction.getNextInstruction(), localInstruction));
    return localInstruction;
  }

  protected Conditional copyConditional(Conditional paramConditional1, Conditional paramConditional2)
  {
    if (paramConditional1 == null)
    {
      return null;
    }

    Conditional localConditional = (Conditional)paramConditional1.copy();
    localConditional.setPreviousConditional(paramConditional2);
    localConditional.setNextInstruction(copyInstruction(localConditional.getNextInstruction(), localConditional));
    localConditional.setNextConditional(copyConditional(localConditional.getNextConditional(), localConditional));
    return localConditional;
  }

  public void setExecuting(boolean paramBoolean) {
    this.executing = paramBoolean;
  }

  public void executeNext(Robot paramRobot) {
    Object localObject3;
    if (!(this.executing)) {
      return;
    }

    do
    {
      if (this.execution.empty())
      {
        Debug.println("Robot: no program");
        restart(paramRobot);
        return;
      }

      localObject1 = (Context)this.execution.pop();
      this.pc = ((Context)localObject1).instruction;
    }
    while (this.pc == null);

    Object localObject1 = paramRobot.getEnvironment().getObjectOfTypeAt(
      paramRobot.getPosition(), 
      triggerClass);

    if (localObject1 != null)
    {
      Enumeration localEnumeration = ((Vector)localObject1).elements();
      while (localEnumeration.hasMoreElements())
      {
        localObject2 = (Trigger)localEnumeration.nextElement();
        if (localObject2 instanceof HairTrigger)
        {
          localObject3 = (HairTrigger)localObject2;
          if (this.pc.boundaryCheck(paramRobot, ((HairTrigger)localObject3).getThreshold()))
          {
            ((Trigger)localObject3).activate(paramRobot, 0);
          }
        }
      }

    }

    if (paramRobot.get_state() == 3) {
      return;
    }

    boolean bool = this.pc.boundaryCheck(paramRobot);

    if (bool)
    {
      if (paramRobot.isStopping())
      {
        restart(paramRobot);
        paramRobot.unsetStopping();
        return;
      }

      paramRobot.setCurrentTrigger(null);

      if ((paramRobot.getLastCell() != null) && (paramRobot.getEnvironment() != null))
      {
        localObject2 = paramRobot.getEnvironment().getObjectOfTypeAt(
          paramRobot.getLastCell(), triggerClass);
        if (localObject2 != null)
        {
          localObject3 = ((Vector)localObject2).elements();
          while (((Enumeration)localObject3).hasMoreElements())
          {
            Trigger localTrigger = (Trigger)((Enumeration)localObject3).nextElement();
            if ((localTrigger.activateOnEvent() & 0x8) > 0)
            {
              Debug.println("EXIT: activating " + localTrigger);
              localTrigger.activate(paramRobot, 8);
            }
          }
        }
        else {
          Debug.println("Nothing here (" + paramRobot.getLastCell() + ")");
        }
      } else {
        Debug.println("Robot had no last position.");
      }

      if (localObject1 != null)
      {
        localObject2 = ((Vector)localObject1).elements();
        while (((Enumeration)localObject2).hasMoreElements())
        {
          localObject3 = (Trigger)((Enumeration)localObject2).nextElement();
          if ((((Trigger)localObject3).activateOnEvent() & 0x4) > 0)
          {
            ((Trigger)localObject3).activate(paramRobot, 4);
          }
        }
      }
      else {
        Debug.println("Nothing here (" + paramRobot.getPosition() + ")");
      }

      if (paramRobot.isStopping())
      {
        restart(paramRobot);
        paramRobot.unsetStopping();
        return;
      }

      for (localObject2 = this.FirstConditional; localObject2 != null; localObject2 = ((Conditional)localObject2).getNextConditional())
      {
        if (((Conditional)localObject2).getNextInstruction() != null)
        {
          if (((Conditional)localObject2).getSensor() instanceof DirectionalSensor)
          {
            int i = 0;
            for (int j = this.execution.size() - 1; j >= 0; --j)
            {
              Context localContext = (Context)this.execution.elementAt(j);
              if ((localContext.conditional != null) && 
                (localContext.conditional.getSensor() == ((Conditional)localObject2).getSensor()))
              {
                i = 1;
                break;
              }

            }

            if (i != 0);
          }
          else if (((Conditional)localObject2).check(paramRobot))
          {
            this.pc.terminate(paramRobot);
            this.execution.push(new Context((Conditional)localObject2, this.pc));
            this.pc = ((Conditional)localObject2).getStart();

            return;
          }
        }
      }
    }

    Object localObject2 = this.pc.getNextInstruction();
    if (this.pc instanceof Loop)
    {
      Instruction localInstruction = ((Loop)this.pc).loop();
      if (localInstruction != null)
      {
        this.execution.push(new Context((Loop)this.pc, this.pc));
        this.pc = localInstruction;
        return;
      }

    }

    if (this.pc.execute(paramRobot))
    {
      if (paramRobot.isStopping())
      {
        if (paramRobot.isStopping())
        {
          restart(paramRobot);
          paramRobot.unsetStopping();
          return;
        }

        restart(paramRobot);
        paramRobot.unsetStopping();
        return;
      }
      if (this.executing)
      {
        this.pc = ((Instruction)localObject2);
      }
    }
  }

  public void restart(Robot paramRobot)
  {
    if (this.pc != null)
      this.pc.terminate(paramRobot);
    this.executing = false;
    this.pc = this.FirstInstruction;
    this.execution.removeAllElements();
    paramRobot.getDetailContainer();
  }

  public boolean isExecuting()
  {
    return this.executing;
  }

  public Instruction getFirstInstruction()
  {
    return this.FirstInstruction;
  }

  public void setFirstInstruction(Instruction paramInstruction)
  {
    this.FirstInstruction = paramInstruction;
    this.pc = paramInstruction;
    this.execution.removeAllElements();
  }

  public Conditional getFirstConditional()
  {
    return this.FirstConditional;
  }

  public void setFirstConditional(Conditional paramConditional)
  {
    this.FirstConditional = paramConditional;
  }

  public String toString()
  {
    if (this.pc == null)
      return "No Program\n";

    if (!(this.executing))
      return "Stopped\n";

    return "Executing " + this.pc.toString() + "\n";
  }
}