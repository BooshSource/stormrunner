package com.templar.games.stormrunner.program;

import com.templar.games.stormrunner.Robot;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;
import java.util.StringTokenizer;

public class Repeat extends Instruction
  implements Loop, Parameterized, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  Instruction destination;
  int times;
  int times_counter;
  boolean random = false;
  int[] randomvals = new int[2];

  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);
    this.destination = ((Instruction)paramObjectInput.readObject());
    this.times = paramObjectInput.readInt();
    this.times_counter = paramObjectInput.readInt();
    this.random = paramObjectInput.readBoolean();
    this.randomvals = ((int[])paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeObject(this.destination);
    paramObjectOutput.writeInt(this.times);
    paramObjectOutput.writeInt(this.times_counter);
    paramObjectOutput.writeBoolean(this.random);
    paramObjectOutput.writeObject(this.randomvals);
  }

  public Repeat()
  {
    setDestination(null);
    setTimes(1);
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

  public void setTimes(int paramInt)
  {
    this.times = (this.times_counter = paramInt);
  }

  public String getParameterString()
  {
    if (this.random) {
      return Integer.toString(this.randomvals[0]) + "-" + Integer.toString(this.randomvals[1]);
    }
    return Integer.toString(this.times);
  }

  public boolean setParameterString(String paramString)
  {
    try
    {
      int i = Integer.parseInt(paramString);
      if (i < 1) {
        return false;
      }

      setTimes(i);
      this.random = false;
      return true;
    }
    catch (NumberFormatException localNumberFormatException1)
    {
      try
      {
        StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "-", false);
        int j = 0;
        int[] arrayOfInt = new int[2];
        while (localStringTokenizer.hasMoreTokens())
        {
          arrayOfInt[j] = Integer.parseInt(localStringTokenizer.nextToken());

          if (arrayOfInt[j] < 1) {
            return false;
          }
          j++;
        }

        if (j != 2) {
          return false;
        }
        if (arrayOfInt[0] == arrayOfInt[1])
        {
          setTimes(arrayOfInt[0]);
          this.random = false;
          return true;
        }

        if (arrayOfInt[0] > arrayOfInt[1])
        {
          int k = arrayOfInt[0];
          arrayOfInt[0] = arrayOfInt[1];
          arrayOfInt[1] = k;
        }

        this.randomvals = arrayOfInt;
        this.random = true;
        genRandom();

        return true;
      }
      catch (NumberFormatException localNumberFormatException2)
      {
        return false;
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
      }
    }
    return false;
  }

  public int getMaxResponseLength()
  {
    return 7;
  }

  public String getAllowedCharacters()
  {
    return "1234567890-";
  }

  public String getPrompt()
  {
    return "Times to repeat (or #-# for random):";
  }

  protected void genRandom()
  {
    Random localRandom = new Random(System.currentTimeMillis());

    setTimes(Math.abs(localRandom.nextInt()) % (this.randomvals[1] - this.randomvals[0] + 1) + this.randomvals[0]);
  }

  public boolean execute(Robot paramRobot)
  {
    return true;
  }

  public static String getDescription()
  {
    return "Allows you to repeat a set of RCX instruction blocks.";
  }

  public boolean boundaryCheck(Robot paramRobot, int paramInt)
  {
    return false;
  }
  public boolean boundaryCheck(Robot paramRobot) { return false; } 
  public void terminate(Robot paramRobot) {
  }

  public Instruction loopPeek() {
    if (this.times_counter < 0)
    {
      if (this.random) {
        genRandom();
      }
      this.times_counter = this.times;
      return null;
    }
    return this.destination;
  }

  public Instruction loop() {
    this.times_counter -= 1;
    return loopPeek();
  }

  public String toString() {
    StringBuffer localStringBuffer = new StringBuffer("Repeat ");
    if (this.random)
    {
      localStringBuffer.append("random ");
      localStringBuffer.append(this.randomvals[0]);
      localStringBuffer.append("-");
      localStringBuffer.append(this.randomvals[1]);
    }
    else {
      localStringBuffer.append(this.times);
    }localStringBuffer.append(" times");
    return localStringBuffer.toString();
  }
}