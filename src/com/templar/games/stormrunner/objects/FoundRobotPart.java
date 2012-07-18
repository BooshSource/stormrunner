package com.templar.games.stormrunner.objects;

import com.templar.games.stormrunner.PhysicalObject;
import com.templar.games.stormrunner.Position;
import com.templar.games.stormrunner.Robot;
import com.templar.games.stormrunner.RobotPart;
import com.templar.games.stormrunner.Scene;
import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class FoundRobotPart extends PhysicalObject
  implements PortableObject, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected RobotPart thisPart;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(paramObjectInput);
    this.thisPart = ((RobotPart)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternal(paramObjectOutput);
    paramObjectOutput.writeObject(this.thisPart);
  }

  public FoundRobotPart()
  {
  }

  public FoundRobotPart(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
    initialize();
  }

  public FoundRobotPart(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    initialize();
  }

  public FoundRobotPart(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
    initialize();
  }

  public boolean isObstructing(Robot paramRobot) {
    return true; }

  public RobotPart getRobotPart() { return this.thisPart; }

  public void initialize() {
    this.thisPart = null;
  }

  public String toString() {
    if (this.thisPart != null)
      return this.thisPart.toString();
    return super.toString();
  }
}