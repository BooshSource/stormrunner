package com.templar.games.stormrunner.templarutil.gui;

import com.templar.games.stormrunner.GameApplet;
import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Image;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintStream;

public class AnimationComponent extends ImageComponent
  implements Runnable, Externalizable
{
  static final long serialVersionUID = 4886718345L;
  protected Image[] Cells;
  protected int[] Indexes;
  protected int[] Delays;
  protected int CurrentIndex;
  protected boolean Loop;
  protected boolean Running;
  protected Thread Runner;

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    String[] arrayOfString = (String[])paramObjectInput.readObject();
    if (arrayOfString != null)
    {
      this.Cells = new Image[arrayOfString.length];
      for (int i = 0; i < arrayOfString.length; ++i)
        if (arrayOfString[i] != null)
          this.Cells[i] = GameApplet.thisApplet.getImage(arrayOfString[i]);
        else
          this.Cells[i] = null;
    }
    this.Indexes = ((int[])paramObjectInput.readObject());
    this.Delays = ((int[])paramObjectInput.readObject());
    this.CurrentIndex = paramObjectInput.readInt();
    this.Loop = paramObjectInput.readBoolean();
    this.Running = paramObjectInput.readBoolean();
    if (this.Cells != null)
      if (this.Indexes != null)
        jumpTo(this.CurrentIndex);
      else
        super.setImage(this.Cells[0]);
    if (paramObjectInput.readBoolean())
    {
      this.Runner = new Thread(this);
      this.Runner.start();
    }
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    if (this.Cells == null) {
      paramObjectOutput.writeObject(null);
    }
    else {
      String[] arrayOfString = new String[this.Cells.length];
      for (int i = 0; i < this.Cells.length; ++i)
        if (this.Cells[i] != null)
          arrayOfString[i] = GameApplet.thisApplet.getImageFilename(this.Cells[i]);
      paramObjectOutput.writeObject(arrayOfString);
    }
    paramObjectOutput.writeObject(this.Indexes);
    paramObjectOutput.writeObject(this.Delays);
    paramObjectOutput.writeInt(this.CurrentIndex);
    paramObjectOutput.writeBoolean(this.Loop);
    paramObjectOutput.writeBoolean(this.Running);
    paramObjectOutput.writeBoolean(this.Runner != null);
  }

  public AnimationComponent()
  {
    super(null, true, false);

    this.Running = false;
  }

  public AnimationComponent(Image[] paramArrayOfImage)
  {
    if (paramArrayOfImage.length < 1) {
      System.out.println("AnimationSequence: Cells must have at least one item.");

      return;
    }

    setCells(paramArrayOfImage);
  }

  public void setCells(Image[] paramArrayOfImage)
  {
    this.Cells = paramArrayOfImage;

    super.setImage(this.Cells[0]);
  }

  public void setSequence(int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean paramBoolean)
  {
    if ((paramArrayOfInt2 != null) && 
      (paramArrayOfInt1.length != paramArrayOfInt2.length))
      throw new IllegalArgumentException("AnimationSequence: Unacceptable sequence: Delays and Indexes not the same length.");

    this.Indexes = paramArrayOfInt1;
    this.Delays = paramArrayOfInt2;
    this.Loop = paramBoolean;
    this.CurrentIndex = -1;
  }

  public void reset()
  {
    this.CurrentIndex = 0;
    if ((this.Cells != null) && (this.Indexes != null))
      super.setImage(this.Cells[this.Indexes[0]]);
  }

  public void start()
  {
    this.Running = true;
    this.CurrentIndex = -1;

    this.Runner = new Thread(this);
    this.Runner.start();
  }

  public void stop()
  {
    this.Running = false;
  }

  public void jumpTo(int paramInt)
  {
    if (paramInt < 0)
      this.CurrentIndex = 0;
    if (paramInt >= this.Indexes.length)
      if (this.Loop)
        this.CurrentIndex = (paramInt - this.Indexes.length);
      else
        this.CurrentIndex = (this.Indexes.length - 1);
    super.setImage(this.Cells[this.Indexes[this.CurrentIndex]]);
  }

  public boolean nextImage()
  {
    int i = 0;
    this.CurrentIndex += 1;

    if ((this.Loop) && (this.CurrentIndex == this.Indexes.length)) {
      this.CurrentIndex = 0;
    }
    else if (this.CurrentIndex < this.Indexes.length)
      i = 1;
    if ((this.CurrentIndex < this.Indexes.length) && 
      (this.Indexes[this.CurrentIndex] < this.Cells.length))
    {
      super.setImage(this.Cells[this.Indexes[this.CurrentIndex]]);
    }

    return i;
  }

  public void run()
  {
    while ((this.Running) && (this.CurrentIndex < this.Indexes.length))
    {
      if (nextImage())
        this.Running = false;

      if (!(this.Running)) { return;
      }

      if (this.Delays != null)
        try
        {
          Thread.currentThread(); Thread.sleep(this.Delays[this.CurrentIndex]);
        }
        catch (InterruptedException localInterruptedException)
        {
          localInterruptedException.printStackTrace();
        }

      if (!(this.Running))
        return;  }
  }

  public int queryCurrentIndex() {
    return this.CurrentIndex; } 
  public Image[] getCells() { return this.Cells; }

  public String toString() {
    StringBuffer localStringBuffer = new StringBuffer("AnimationComponent[");
    localStringBuffer.append(super.getSize());
    localStringBuffer.append(",CurrentIndex:");
    localStringBuffer.append(this.CurrentIndex);
    localStringBuffer.append(",Indexes:");
    if (this.Indexes == null)
      localStringBuffer.append("null");
    else
      localStringBuffer.append(Debug.arrayPrint(this.Indexes));
    localStringBuffer.append(",Delays:");
    if (this.Delays == null)
      localStringBuffer.append("null");
    else
      localStringBuffer.append(Debug.arrayPrint(this.Delays));
    localStringBuffer.append(",Cells:");
    if (this.Cells == null)
      localStringBuffer.append("null");
    else
      localStringBuffer.append(Debug.arrayPrint(this.Cells));
    localStringBuffer.append("]\n");
    return localStringBuffer.toString();
  }
}