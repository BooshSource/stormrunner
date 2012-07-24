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

public class Salvage extends PhysicalObject
  implements Obstacle, PortableObject, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public static final int MIN = 17;
  public static final int MAX = 30;
  int value = -1;

  public Salvage()
  {
  }

  public void readExternal(ObjectInput paramObjectInput) throws ClassNotFoundException, IOException
  {
    super.readExternal(paramObjectInput);
    this.value = paramObjectInput.readInt();
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeInt(this.value);
  }

  public Salvage(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
    generate();
  }

  public Salvage(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    generate();
  }

  public Salvage(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
    generate();
  }

  private void generate() {
    if (this.value == -1)
    {
      Random localRandom = new Random(System.currentTimeMillis());
      this.value = (Math.abs(localRandom.nextInt() % 14) + 17);
    }
  }
  public int getPolymetals() {
    return this.value;
  }
  public String toString() {
    return "Salvage that will yield " + this.value + " PolyMetals";
  }

  public boolean isObstructing(Robot paramRobot) {
    return true;
  }
}