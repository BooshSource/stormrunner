package com.templar.games.stormrunner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Vector;

public class World
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  Vector scenes = new Vector();
  int current = -1;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.scenes = ((Vector)paramObjectInput.readObject());
    this.current = paramObjectInput.readInt();
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeObject(this.scenes);
    paramObjectOutput.writeInt(this.current);
  }

  public void addScene(Scene paramScene, int paramInt)
  {
    this.scenes.insertElementAt(paramScene, paramInt);
  }

  public void addScene(Scene paramScene)
  {
    this.scenes.addElement(paramScene);
  }

  public Scene getScene(String paramString)
  {
    Enumeration localEnumeration = this.scenes.elements();
    while (localEnumeration.hasMoreElements())
    {
      Scene localScene = (Scene)localEnumeration.nextElement();
      if (localScene.mightBeDescribedAs(paramString))
        return localScene;
    }
    return null;
  }

  public Scene getCurrentScene()
  {
    if (this.current > -1)
    {
      try
      {
        return ((Scene)this.scenes.elementAt(this.current));
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
        return null;
      }

    }

    return null;
  }

  public Scene getNextScene()
  {
    if (this.scenes.size() > this.current)
    {
      this.current += 1;
      return getCurrentScene();
    }

    return null;
  }

  public String toString()
  {
    return "World[" + this.scenes + "]";
  }
}