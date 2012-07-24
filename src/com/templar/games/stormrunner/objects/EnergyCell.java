package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.Scene;
import java.awt.Image;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;

public class EnergyCell extends PhysicalObject
  implements PortableObject, Externalizable, Obstacle
{
  static final long serialVersionUID = 4886718345L;
  public static final int MIN = 15;
  public static final int MAX = 25;
  int value = -1;

  public EnergyCell()
  {
  }

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);
    this.value = paramObjectInput.readInt();
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException
  {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeInt(this.value);
  }

  public EnergyCell(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
    generate();
  }

  public EnergyCell(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    generate();
  }

  public EnergyCell(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
    generate();
  }

  private void generate() {
    if (this.value == -1)
    {
      Random localRandom = new Random(System.currentTimeMillis());
      this.value = (Math.abs(localRandom.nextInt() % 11) + 15);
    }
  }
  public int getEnergyUnits() {
    return this.value; } 
  public boolean isObstructing(Robot paramRobot) { return true; }

  public String toString()
  {
    return "Energy Cell at " + (int)(this.value / 25.0D * 100.0D) + "% capacity";
  }
}