package com.templar.games.stormrunner.util;

import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.gui.ImageComponent;
import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SceneObject extends ImageComponent
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  public double VERSION = 0.2D;
  public String filename;
  public String name;
  public String classname;
  public String sound;
  public String urltarget;
  public String layer;
  public Image image;
  public boolean[][] shape;
  public Point coord;
  transient int mlcount;

  public SceneObject()
  {
    this.filename = "";
    this.name = "";
    this.classname = "";
    this.sound = "";
    this.urltarget = "";
    this.layer = "";
    this.shape = null;
    this.coord = new Point(0, 0);
    this.mlcount = 0;
  }

  public SceneObject(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, Image paramImage, Point paramPoint)
  {
    super(paramImage);
    this.filename = paramString1;
    if (this.filename != null)
      this.filename = this.filename.replace(File.separatorChar, '/');
    this.name = paramString2;
    this.classname = paramString3;
    this.shape = null;
    this.coord = paramPoint;
    this.sound = paramString4;
    this.layer = paramString6;
    this.urltarget = paramString5;
  }

  public SceneObject(SceneObject paramSceneObject)
  {
    this();
    if (paramSceneObject != null)
    {
      setImage(paramSceneObject.getImage());
      this.filename = new String(paramSceneObject.filename);
      if (this.filename != null)
        this.filename = this.filename.replace(File.separatorChar, '/');
      this.name = new String(paramSceneObject.name);
      this.classname = new String(paramSceneObject.classname);
      this.sound = new String(paramSceneObject.sound);
      this.urltarget = new String(paramSceneObject.urltarget);
      this.layer = new String(paramSceneObject.layer);
      if (paramSceneObject.shape == null) {
        this.shape = null;
      }
      else {
        this.shape = new boolean[paramSceneObject.shape.length][paramSceneObject.shape[0].length];
        for (int i = 0; i < this.shape.length; i++)
          for (int j = 0; j < this.shape[0].length; j++)
            this.shape[i][j] = paramSceneObject.shape[i][j];
      }
      this.coord = paramSceneObject.coord;
    }
  }

  public void addMouseListener(MouseListener paramMouseListener)
  {
    super.addMouseListener(paramMouseListener);
    this.mlcount += 1;
    Debug.println("# of mouse listeners on " + this + ": " + this.mlcount);
  }

  public void removeMouseListener(MouseListener paramMouseListener) {
    super.removeMouseListener(paramMouseListener);
    this.mlcount -= 1;
  }

  public void setShape(boolean[][] paramArrayOfBoolean)
  {
    this.shape = paramArrayOfBoolean;
    if (this.shape != null)
      setSize(this.shape.length, this.shape[0].length);
  }

  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException
  {
    String str = (String)paramObjectInput.readObject();
    if ((str.startsWith(getClass().getName())) && (str.endsWith(String.valueOf(this.VERSION))))
    {
      this.filename = ((String)paramObjectInput.readObject());
      if (this.filename != null)
        this.filename = this.filename.replace(File.separatorChar, '/');
      this.name = ((String)paramObjectInput.readObject());
      this.classname = ((String)paramObjectInput.readObject());
      this.shape = ((boolean[][])paramObjectInput.readObject());
      this.coord = ((Point)paramObjectInput.readObject());
      this.sound = ((String)paramObjectInput.readObject());
      this.urltarget = ((String)paramObjectInput.readObject());
      this.layer = ((String)paramObjectInput.readObject());

      return;
    }

    if (str.compareTo(getClass().getName()) == 0)
    {
      this.filename = ((String)paramObjectInput.readObject());
      if (this.filename != null)
        this.filename = this.filename.replace(File.separatorChar, '/');
      this.name = ((String)paramObjectInput.readObject());
      this.classname = ((String)paramObjectInput.readObject());
      this.shape = ((boolean[][])paramObjectInput.readObject());
      this.coord = ((Point)paramObjectInput.readObject());
      this.sound = ((String)paramObjectInput.readObject());
      this.urltarget = ((String)paramObjectInput.readObject());
      this.layer = "Unnamed";

      return;
    }

    throw new IOException("Could not load object, wrong version.");
  }

  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    paramObjectOutput.writeObject(getClass().getName() + this.VERSION);
    if (this.filename != null)
      this.filename = this.filename.replace(File.separatorChar, '/');
    paramObjectOutput.writeObject(this.filename);
    paramObjectOutput.writeObject(this.name);
    paramObjectOutput.writeObject(this.classname);
    paramObjectOutput.writeObject(this.shape);
    paramObjectOutput.writeObject(this.coord);
    paramObjectOutput.writeObject(this.sound);
    paramObjectOutput.writeObject(this.urltarget);
    paramObjectOutput.writeObject(this.layer);
  }

  public String toString() {
    return "SceneObject[filename:" + this.filename + ",name:" + this.name + ",classname:" + 
      this.classname + ",shapesize:(" + this.shape.length + "," + this.shape[0].length + 
      "),coord:" + this.coord + ",sound:" + this.sound + ",urltarget:" + this.urltarget + 
      ",layer:" + this.layer + "]";
  }
}