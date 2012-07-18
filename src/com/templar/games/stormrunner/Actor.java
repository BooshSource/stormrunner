package com.templar.games.stormrunner;

import java.awt.Image;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class Actor extends PhysicalObject
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  String state;

  public Actor()
  {
  }

  public Actor(Scene paramScene, Position paramPosition, boolean paramBoolean)
  {
    super(paramScene, paramPosition, paramBoolean);
  }

  public Actor(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
  }

  public Actor(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    super(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
  }

  public void tick()
  {
  }

  public void setState(String paramString)
  {
    this.state = paramString;
  }

  public String getState()
  {
    return this.state;
  }

  public void readExternalWithoutImages(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    super.readExternalWithoutImages(paramObjectInput);
  }

  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    super.readExternal(paramObjectInput);
  }

  public void writeExternalWithoutImages(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternalWithoutImages(paramObjectOutput);
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    super.writeExternal(paramObjectOutput);
  }
}