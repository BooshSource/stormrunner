package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.Debug;
import com.templar.games.stormrunner.templarutil.audio.SoundListener;
import com.templar.games.stormrunner.templarutil.gui.ImageComposite;
import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.applet.AppletContext;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;
import java.util.Vector;

public class PhysicalObject extends ImageComposite
  implements Externalizable, SoundListener
{
  static final long serialVersionUID = 4886718345L;
  protected transient Scene environment;
  protected Position location;
  protected Position LastPosition;
  protected Position LastCell;
  protected PhysicalObject[][] shape;
  protected String ID;
  protected boolean animated;
  protected URL url_target;
  protected String frame;
  protected String layer;
  protected transient Renderer CurrentRenderer;
  protected transient Vector Playing = new Vector();
  protected transient Vector Looping = new Vector();
  transient boolean imListening;
  transient MouseHandler mouseHandler;

  public void readExternalWithoutImages(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.location = ((Position)paramObjectInput.readObject());
    this.LastPosition = ((Position)paramObjectInput.readObject());
    this.LastCell = ((Position)paramObjectInput.readObject());
    setShape((boolean[][])paramObjectInput.readObject());
    this.ID = ((String)paramObjectInput.readObject());
    this.animated = paramObjectInput.readBoolean();
    this.url_target = ((URL)paramObjectInput.readObject());
    this.frame = ((String)paramObjectInput.readObject());
    this.layer = ((String)paramObjectInput.readObject());
  }

  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    readExternalWithoutImages(paramObjectInput);
    String[] arrayOfString = (String[])paramObjectInput.readObject();
    if (arrayOfString != null)
    {
      Image[] arrayOfImage = new Image[arrayOfString.length];
      for (int i = 0; i < arrayOfString.length; i++)
        arrayOfImage[i] = GameApplet.thisApplet.getImage(arrayOfString[i]);
      setImages(arrayOfImage);
    }
  }

  public void writeExternalWithoutImages(ObjectOutput paramObjectOutput) throws IOException
  {
    paramObjectOutput.writeObject(this.location);
    paramObjectOutput.writeObject(this.LastPosition);
    paramObjectOutput.writeObject(this.LastCell);
    boolean[][] arrayOfBoolean = new boolean[this.shape.length][this.shape[0].length];
    for (int i = 0; i < this.shape.length; i++)
      for (int j = 0; j < this.shape[0].length; j++)
        if (this.shape[i][j] == null)
          arrayOfBoolean[i][j] = false;
        else
          arrayOfBoolean[i][j] = true;
    paramObjectOutput.writeObject(arrayOfBoolean);
    paramObjectOutput.writeObject(this.ID);
    paramObjectOutput.writeBoolean(this.animated);
    paramObjectOutput.writeObject(this.url_target);
    paramObjectOutput.writeObject(this.frame);
    paramObjectOutput.writeObject(this.layer);
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException
  {
    writeExternalWithoutImages(paramObjectOutput);
    Image[] arrayOfImage = getImages();
    if (arrayOfImage != null)
    {
      String[] arrayOfString = new String[arrayOfImage.length];
      for (int i = 0; i < arrayOfImage.length; i++)
        arrayOfString[i] = GameApplet.thisApplet.getImageFilename(arrayOfImage[i]);
      paramObjectOutput.writeObject(arrayOfString);

      return;
    }

    paramObjectOutput.writeObject(null);
  }

  public PhysicalObject()
  {
    this.ID = "";
    this.shape = new PhysicalObject[1][1];
    this.shape[0][0] = this;
    this.url_target = null;
    this.frame = "";
    this.layer = "";
    this.mouseHandler = new MouseHandler();
  }

  public PhysicalObject(Scene paramScene, Position paramPosition, boolean paramBoolean) {
    this();
    this.environment = paramScene;
    this.location = paramPosition;
    this.LastPosition = paramPosition;
    this.LastCell = paramPosition;
    this.animated = paramBoolean;
  }

  public PhysicalObject(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean paramBoolean) {
    this(paramScene, paramPosition, paramBoolean);
    setImages(paramArrayOfImage);
  }

  public PhysicalObject(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean) {
    this(paramScene, paramPosition, paramArrayOfImage, paramBoolean);
    setShape(paramArrayOfBoolean);
  }

  public PhysicalObject(Scene paramScene, Position paramPosition, Image[] paramArrayOfImage, boolean[][] paramArrayOfBoolean, boolean paramBoolean, URL paramURL, String paramString) {
    this(paramScene, paramPosition, paramArrayOfImage, paramArrayOfBoolean, paramBoolean);
    setClickTarget(paramURL, paramString);
  }

  public void setLayer(String paramString) {
    this.layer = paramString;
  }
  public String getLayer() { return this.layer; }

  public void setID(String paramString) {
    this.ID = paramString;
  }
  public String getID() { return this.ID; }

  public void setEnvironment(Scene paramScene) {
    this.environment = paramScene;
  }

  public Scene getEnvironment()
  {
    return this.environment;
  }

  public boolean getAnimated()
  {
    return this.animated;
  }

  public void setAnimated(boolean paramBoolean)
  {
    this.animated = paramBoolean;
  }

  public void setRenderer(Renderer paramRenderer)
  {
    this.CurrentRenderer = paramRenderer;
  }

  public void playSound(String paramString)
  {
    if (this.CurrentRenderer != null)
    {
      this.Playing.addElement(paramString);

      this.CurrentRenderer.playSound(this, paramString);
    }
  }

  public void loopSound(String paramString)
  {
    if (this.CurrentRenderer != null)
    {
      this.Looping.addElement(paramString);

      this.CurrentRenderer.loopSound(this, paramString);
    }
  }

  public void stopSound(String paramString)
  {
    if (this.CurrentRenderer != null)
    {
      this.Looping.removeElement(paramString);
      this.Playing.removeElement(paramString);

      this.CurrentRenderer.stopSound(this, paramString);
    }
  }

  public void soundStopped(String paramString, int paramInt)
  {
    this.Playing.removeElement(paramString);

    if (this.CurrentRenderer != null)
      this.CurrentRenderer.soundStopped(this, paramInt);
  }

  public Vector getPlayList()
  {
    return this.Playing;
  }

  public Vector getLoopList()
  {
    return this.Looping;
  }

  public void setClickTarget(URL paramURL, String paramString)
  {
    if (paramURL != null)
    {
      if (!this.imListening) {
        addMouseListener(this.mouseHandler);
      }
    }
    else if (this.imListening)
      removeMouseListener(this.mouseHandler);
    this.url_target = paramURL;
    this.frame = paramString;
  }

  public void setShape(PhysicalObject[][] paramArrayOfPhysicalObject)
  {
    this.shape = paramArrayOfPhysicalObject;
  }

  public void setShape(boolean[][] paramArrayOfBoolean)
  {
    this.shape = new PhysicalObject[paramArrayOfBoolean.length][paramArrayOfBoolean[0].length];
    for (int i = 0; i < paramArrayOfBoolean.length; i++)
      for (int j = 0; j < paramArrayOfBoolean[0].length; j++)
        if (paramArrayOfBoolean[i][j] != false)
          this.shape[i][j] = this;
        else
          this.shape[i][j] = null;
  }

  public Dimension getShapeSize()
  {
    return new Dimension(this.shape.length, this.shape[0].length);
  }

  public void setVisible(boolean paramBoolean)
  {
    if (this.animated) {
      if (paramBoolean)
        startDrawing();
      else
        stopDrawing();
    }
    super.setVisible(paramBoolean);
  }

  public Position getPosition()
  {
    return this.location;
  }

  public Position getLastPosition()
  {
    return this.LastPosition;
  }

  public Position getLastCell()
  {
    return this.LastCell;
  }

  public void setPosition(Position paramPosition)
  {
    if ((this.LastCell != null) && (this.LastPosition != null))
    {
      if ((this.LastPosition.x != paramPosition.x) || (this.LastPosition.y != paramPosition.y))
      {
        this.LastCell.x = this.LastPosition.x;
        this.LastCell.y = this.LastPosition.y;
      }
    }
    else {
      this.LastCell = new Position(paramPosition);
    }
    Position localPosition = this.location;

    this.location = paramPosition;
    setLocation(Position.mapToScreen(paramPosition));

    if (this.environment != null)
    {
      if (this.LastPosition != null)
        this.environment.moveObject(this, this.LastPosition.getMapPoint());
      else {
        place(this.environment.getObjectMap());
      }

      if (this.CurrentRenderer != null)
      {
        this.CurrentRenderer.reportNewPosition(this);
      }

    }

    this.LastPosition = localPosition;
  }

  public void place(Vector[][] paramArrayOfVector)
  {
    int i = this.location.x; for (int j = 0; j < this.shape.length; j++) {
      int k = this.location.y; for (int m = 0; m < this.shape[0].length; m++)
      {
        if (this.shape[j][m] != null)
        {
          if ((i >= 0) && (i < paramArrayOfVector.length) && (k >= 0) && (k < paramArrayOfVector[0].length))
          {
            if (paramArrayOfVector[i][k] == null)
              paramArrayOfVector[i][k] = new Vector(1, 1);
            if (!paramArrayOfVector[i][k].contains(this.shape[j][m]))
              paramArrayOfVector[i][k].addElement(this.shape[j][m]);
          }
          else {
            Debug.println("Tried to place an object outside the map: " + j + "," + m + " at " + i + "," + k);
          }
        }
        k++;
      }
      i++;
    }
  }

  protected void handleMouseClick(MouseEvent paramMouseEvent)
  {
    if ((GameApplet.appletContext != null) && (this.url_target != null) && (this.frame != null)) {
      GameApplet.appletContext.showDocument(this.url_target, this.frame);

      return;
    }

    Debug.println("Unable to use " + GameApplet.appletContext + " to showDocument(" + this.url_target + "," + this.frame + ")");
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics) {
    if (this.CurrentRenderer != null) {
      if (!this.CurrentRenderer.isInWindow(this))
      {
        stopDrawing();
        return;
      }

      startDrawing();
    }
    super.paint(paramGraphics);
  }

  public boolean[][] getShape() {
    boolean[][] arrayOfBoolean = new boolean[this.shape.length][this.shape[0].length];
    for (int i = 0; i < this.shape.length; i++)
      for (int j = 0; j < this.shape[0].length; j++)
        if (this.shape[i][j] != null)
          arrayOfBoolean[i][j] = true;
    return arrayOfBoolean;
  }

  public String toString() {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = getClass().getName();
    localStringBuffer.append(str.substring(str.lastIndexOf(".") + 1));
    localStringBuffer.append("[");
    localStringBuffer.append("Environment:" + (this.environment == null ? "null" : new StringBuffer("Scene@").append(this.environment.hashCode()).toString()));
    localStringBuffer.append(",");
    localStringBuffer.append(this.ID);
    localStringBuffer.append(",");
    localStringBuffer.append(this.location);
    localStringBuffer.append(",");
    localStringBuffer.append(this.url_target);
    localStringBuffer.append(",");
    localStringBuffer.append(this.frame);
    localStringBuffer.append(",");
    localStringBuffer.append(this.layer);
    localStringBuffer.append(",");
    localStringBuffer.append(this.mouseHandler);
    localStringBuffer.append(",\n");
    localStringBuffer.append(getShapeString());
    localStringBuffer.append("]");
    return localStringBuffer.toString();
  }

  public String getShapeString() {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.shape.length);
    localStringBuffer.append(",");
    localStringBuffer.append(this.shape[0].length);
    localStringBuffer.append("\n");
    for (int i = 0; i < this.shape[0].length; i++)
    {
      for (int j = 0; j < this.shape.length; j++)
        if (this.shape[j][i] == null)
          localStringBuffer.append(".");
        else
          localStringBuffer.append("#");
      localStringBuffer.append("\n");
    }
    return localStringBuffer.toString();
  }

  class MouseHandler extends MouseAdapter
  {
    public void mousePressed(MouseEvent paramMouseEvent)
    {
      PhysicalObject.this.handleMouseClick(paramMouseEvent);
    }

    MouseHandler()
    {
    }
  }
}