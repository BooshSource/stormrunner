package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.util.ImageRetriever;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintStream;
import java.util.Vector;

public class Shroud extends Component
  implements Externalizable
{
  static final long serialVersionUID = 4886718345L;
  private int[][] TransientLightCount;
  private boolean[][] PermanentlyLit;
  private int[][] Tile;
  private transient Image[] TileImages;
  private Dimension gridsize;
  private Dimension gridcellsize;
  private Point Offset = new Point(0, 0);

  protected transient Vector ShroudListeners = new Vector();

  public void readExternal(ObjectInput paramObjectInput)
    throws ClassNotFoundException, IOException
  {
    this.TransientLightCount = ((int[][])paramObjectInput.readObject());
    this.PermanentlyLit = ((boolean[][])paramObjectInput.readObject());
    this.Tile = ((int[][])paramObjectInput.readObject());
    this.gridsize = ((Dimension)paramObjectInput.readObject());
    this.gridcellsize = ((Dimension)paramObjectInput.readObject());
    this.Offset = ((Point)paramObjectInput.readObject());
  }

  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeObject(this.TransientLightCount);
    paramObjectOutput.writeObject(this.PermanentlyLit);
    paramObjectOutput.writeObject(this.Tile);
    paramObjectOutput.writeObject(this.gridsize);
    paramObjectOutput.writeObject(this.gridcellsize);
    paramObjectOutput.writeObject(this.Offset);
  }

  public Shroud()
  {
  }

  public Shroud(Dimension paramDimension1, Dimension paramDimension2, Image[] paramArrayOfImage)
  {
    setTileImages(paramArrayOfImage);
    setVirtualSize(paramDimension1, paramDimension2);
  }

  public Shroud(Dimension paramDimension1, Dimension paramDimension2)
  {
    setVirtualSize(paramDimension1, paramDimension2);
  }

  public void setTileImages(Image[] paramArrayOfImage)
  {
    this.TileImages = paramArrayOfImage;

    MediaTracker localMediaTracker = new MediaTracker(this);
    for (int i = 0; i < paramArrayOfImage.length; i++)
      localMediaTracker.addImage(paramArrayOfImage[i], 0); try {
      localMediaTracker.waitForAll(); } catch (InterruptedException localInterruptedException) { localInterruptedException.printStackTrace();
    }
    if ((localMediaTracker.statusAll(false) & 0x4) != 0)
      System.out.println("Problems loading the Shroud images!");
  }

  public void setVirtualSize(Dimension paramDimension1, Dimension paramDimension2)
  {
    this.gridsize = paramDimension1;
    this.gridcellsize = paramDimension2;

    this.TransientLightCount = new int[paramDimension1.width][paramDimension1.height];
    this.PermanentlyLit = new boolean[paramDimension1.width][paramDimension1.height];
    this.Tile = new int[paramDimension1.width][paramDimension1.height];
  }

  public boolean isVisible(Point paramPoint)
  {
    return isVisible(paramPoint.x, paramPoint.y);
  }

  public boolean isVisible(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt1 >= this.gridsize.width) || (paramInt2 < 0) || (paramInt2 >= this.gridsize.height)) {
      return false;
    }
    return (this.PermanentlyLit[paramInt1][paramInt2] != false) || (this.TransientLightCount[paramInt1][paramInt2] > 0);
  }

  public int[][] getTileState()
  {
    return this.Tile;
  }

  public void setVisible(Point paramPoint, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    setVisible(paramPoint, paramInt, paramBoolean1, paramBoolean2, false);
  }

  public void setVisible(Point paramPoint, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    int i = paramPoint.x;
    int j = paramPoint.y;

    if ((i < 0) || (j < 0) || (i >= this.gridsize.width) || (j >= this.gridsize.height))
    {
      System.err.println("Shroud: setVisible on a point that does not exist. (" + i + "," + j + ")");
      return;
    }

    if (setState(paramPoint, paramInt, paramBoolean1, paramBoolean2))
    {
      if (paramBoolean1)
      {
        calculateEdgeTiles(paramPoint, paramInt + 1);

        return;
      }

      if (!paramBoolean3)
      {
        calculateEdgeTiles(paramPoint, paramInt + 1);
        calculateEdgeTiles(paramPoint, paramInt);

        return;
      }

      int k = 0; int m = this.gridsize.width - 1; int n = 0; int i1 = this.gridsize.height - 1;
      int i2 = Math.max(k, paramPoint.x - (paramInt + 1));
      int i3 = Math.min(m, paramPoint.x + (paramInt + 1));
      int i4 = Math.max(n, paramPoint.y - (paramInt + 1));
      int i5 = Math.min(i1, paramPoint.y + (paramInt + 1));
      for (int i6 = i2; i6 <= i3; i6++)
        for (int i7 = i4; i7 <= i5; i7++)
          calculateTile(i6, i7);
    }
  }

  public void setOffset(Point paramPoint)
  {
    this.Offset = paramPoint;
  }

  public Point getOffset()
  {
    return this.Offset;
  }

  public void addShroudListener(ShroudListener paramShroudListener)
  {
    this.ShroudListeners.addElement(paramShroudListener);
  }

  public void removeShroudListener(ShroudListener paramShroudListener)
  {
    this.ShroudListeners.removeElement(paramShroudListener);
  }

  protected void sendShroudEvent(Rectangle paramRectangle1, boolean paramBoolean, Rectangle paramRectangle2)
  {
    ShroudEvent localShroudEvent = new ShroudEvent(this, paramRectangle1, paramBoolean, paramRectangle2);

    for (int i = 0; i < this.ShroudListeners.size(); i++)
    {
      ((ShroudListener)this.ShroudListeners.elementAt(i)).shroudChanged(localShroudEvent);
    }
  }

  protected boolean setState(Point paramPoint, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 0;

    int j = 0; int k = this.gridsize.width - 1; int m = 0; int n = this.gridsize.height - 1;
    int i1 = Math.max(j, paramPoint.x - paramInt);
    int i2 = Math.min(k, paramPoint.x + paramInt);
    int i3 = Math.max(m, paramPoint.y - paramInt);
    int i4 = Math.min(n, paramPoint.y + paramInt);

    int i5 = Math.max(j, paramPoint.x - (paramInt + 1));
    int i6 = Math.min(k, paramPoint.x + (paramInt + 1));
    int i7 = Math.max(m, paramPoint.y - (paramInt + 1));
    int i8 = Math.min(n, paramPoint.y + (paramInt + 1));

    for (int i9 = i1; i9 <= i2; i9++)
    {
      for (int i10 = i3; i10 <= i4; i10++)
      {
        if (paramBoolean1)
        {
          if ((i == 0) && (!isVisible(i9, i10))) {
            i = 1;
          }
          if (paramBoolean2)
          {
            this.PermanentlyLit[i9][i10] = true;
          }

          this.TransientLightCount[i9][i10] += 1;

          this.Tile[i9][i10] = 15;
        }
        else
        {
          boolean bool1 = isVisible(i9, i10);

          if (paramBoolean2)
          {
            this.PermanentlyLit[i9][i10] = false;
          }

          if (this.TransientLightCount[i9][i10] > 0)
          {
            this.TransientLightCount[i9][i10] -= 1;
          }

          boolean bool2 = isVisible(i9, i10);
          if ((i == 0) && (bool1) && (!bool2)) {
            i = 1;
          }

          if ((i9 <= i1) || (i9 >= i2) || (i10 <= i3) || (i10 >= i4))
          {
            continue;
          }
          if (bool2)
            continue;
          this.Tile[i9][i10] = 0;
        }

      }

    }

    if (i != 0) {
      sendShroudEvent(new Rectangle(i1, i3, i2 - i1 + 1, i4 - i3 + 1), paramBoolean1, new Rectangle(i5, i7, i6 - i5 + 1, i8 - i7 + 1));
    }
    if(i==1) return true; else return false;
  }

  protected void calculateEdgeTiles(Point paramPoint, int paramInt)
  {
    int i = 0; int j = this.gridsize.width - 1; int k = 0; int m = this.gridsize.height - 1;
    int n = Math.max(i, paramPoint.x - paramInt);
    int i1 = Math.min(j, paramPoint.x + paramInt);
    int i2 = Math.max(k, paramPoint.y - paramInt);
    int i3 = Math.min(m, paramPoint.y + paramInt);

    int i6 = 0;
    for (int i4 = n; i6 == 0; i4 = i1)
    {
      for (int i5 = i2; i5 <= i3; i5++)
      {
        calculateTile(i4, i5);
      }
      if (i4 == i1) {
        i6 = 1;
      }
    }

    i6 = 0;
    for (int i5 = i2; i6 == 0; i5 = i3)
    {
      for (int i4 = n + 1; i4 < i1; i4++)
      {
        calculateTile(i4, i5);
      }
      if (i5 == i3)
        i6 = 1;
    }
  }

  protected void calculateTile(int paramInt1, int paramInt2)
  {
    if (isVisible(paramInt1, paramInt2)) {
      this.Tile[paramInt1][paramInt2] = 15;

      return;
    }

    boolean[][] arrayOfBoolean = new boolean[3][3];
    int i = 0;
    int j = 0;
    boolean[] arrayOfBoolean1 = new boolean[3]; boolean[] arrayOfBoolean2 = new boolean[3];
    int k = paramInt1 - 1; int m = paramInt1 + 1; int n = paramInt2 - 1; int i1 = paramInt2 + 1;
    int i2 = 0; int i3 = 0;
    for (int i4 = n; i4 <= i1; i3++)
    {
      for (int i5 = k; i5 <= m; i2++)
      {
        boolean bool = isVisible(i5, i4);
        i = (i == 0) && (!bool) ? 0 : 1;
        arrayOfBoolean1[i3] = ((arrayOfBoolean1[i3] == false) && (!bool) ? false : true);
        arrayOfBoolean2[i2] = ((arrayOfBoolean2[i2] == false) && (!bool) ? false : true);

        if (bool)
          j++;
        arrayOfBoolean[i2][i3] = bool;

        i5++;
      }

      i2 = 0;

      i4++;
    }

    if (i == 0) {
      this.Tile[paramInt1][paramInt2] = 0;

      return;
    }

    if ((j == 1) && (arrayOfBoolean[0][0] != false)) {
      this.Tile[paramInt1][paramInt2] = 12;

      return;
    }
    if ((j == 1) && (arrayOfBoolean[2][0] != false)) {
      this.Tile[paramInt1][paramInt2] = 11;

      return;
    }
    if ((j == 1) && (arrayOfBoolean[0][2] != false)) {
      this.Tile[paramInt1][paramInt2] = 10;

      return;
    }
    if ((j == 1) && (arrayOfBoolean[2][2] != false)) {
      this.Tile[paramInt1][paramInt2] = 9;

      return;
    }

    if ((j == 2) && (arrayOfBoolean[0][0] != false) && (arrayOfBoolean[2][2] != false)) {
      this.Tile[paramInt1][paramInt2] = 13;

      return;
    }
    if ((j == 2) && (arrayOfBoolean[0][2] != false) && (arrayOfBoolean[2][0] != false)) {
      this.Tile[paramInt1][paramInt2] = 14;

      return;
    }

    if ((arrayOfBoolean1[0] == false) && (arrayOfBoolean1[1] == false) && (arrayOfBoolean1[2] != false)) {
      this.Tile[paramInt1][paramInt2] = 7;

      return;
    }
    if ((arrayOfBoolean1[0] != false) && (arrayOfBoolean1[1] == false) && (arrayOfBoolean1[2] == false)) {
      this.Tile[paramInt1][paramInt2] = 2;

      return;
    }
    if ((arrayOfBoolean2[0] == false) && (arrayOfBoolean2[1] == false) && (arrayOfBoolean2[2] != false)) {
      this.Tile[paramInt1][paramInt2] = 5;

      return;
    }
    if ((arrayOfBoolean2[0] != false) && (arrayOfBoolean2[1] == false) && (arrayOfBoolean2[2] == false)) {
      this.Tile[paramInt1][paramInt2] = 4;

      return;
    }

    if ((arrayOfBoolean[1][2] == false) && (arrayOfBoolean[2][1] == false) && (arrayOfBoolean[2][2] == false)) {
      this.Tile[paramInt1][paramInt2] = 1;

      return;
    }
    if ((arrayOfBoolean[0][1] == false) && (arrayOfBoolean[0][2] == false) && (arrayOfBoolean[1][2] == false)) {
      this.Tile[paramInt1][paramInt2] = 3;

      return;
    }
    if ((arrayOfBoolean[1][0] == false) && (arrayOfBoolean[2][0] == false) && (arrayOfBoolean[2][1] == false)) {
      this.Tile[paramInt1][paramInt2] = 6;

      return;
    }
    if ((arrayOfBoolean[0][0] == false) && (arrayOfBoolean[1][0] == false) && (arrayOfBoolean[0][1] == false)) {
      this.Tile[paramInt1][paramInt2] = 8;

      return;
    }

    this.Tile[paramInt1][paramInt2] = 15;
  }

  public void paint(Graphics paramGraphics)
  {
    update(paramGraphics);
  }

  public void update(Graphics paramGraphics)
  {
    Rectangle localRectangle = paramGraphics.getClipBounds();

    int i = this.Offset.x + (int)Math.floor(localRectangle.x / this.gridcellsize.width);
    int j = i + (int)Math.ceil(localRectangle.width / this.gridcellsize.width);
    int k = this.Offset.y + (int)Math.floor(localRectangle.y / this.gridcellsize.height);
    int m = k + (int)Math.ceil(localRectangle.height / this.gridcellsize.height);

    j = Math.min(this.gridsize.width - 1, j);
    m = Math.min(this.gridsize.height - 1, m);

    int n = (i - this.Offset.x) * this.gridcellsize.width;
    int i1 = (k - this.Offset.y) * this.gridcellsize.height;
    for (int i2 = k; i2 <= m; i1 += this.gridcellsize.height)
    {
      for (int i3 = i; i3 <= j; n += this.gridcellsize.width)
      {
        Image localImage = this.TileImages[this.Tile[i3][i2]];
        GameApplet.thisApplet.hitCache(localImage);
        paramGraphics.drawImage(localImage, n, i1, this);

        i3++;
      }

      n = (i - this.Offset.x) * this.gridcellsize.width;

      i2++;
    }
  }
}