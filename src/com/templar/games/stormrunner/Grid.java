package com.templar.games.stormrunner;

import com.templar.games.stormrunner.templarutil.util.UtilityThread;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

public class Grid extends Component
{
  protected int CellWidth;
  protected int CellHeight;
  protected Color GridColor;
  protected boolean GridOn = false;
  protected boolean BurnOn = false;
  protected boolean BurnOff = false;
  protected int BurnRate = 25;
  protected int BurnSteps = 3;
  protected int BurnWeight = 100;
  protected int BurnStepX;
  protected int BurnStepY;
  protected boolean BurnComplete = true;
  protected Dimension GridSize;

  public Grid(int paramInt1, int paramInt2, Color paramColor)
  {
    this.CellWidth = paramInt1;
    this.CellHeight = paramInt2;
    this.GridColor = paramColor;
  }

  public void setBurnCharacteristics(int paramInt1, int paramInt2, int paramInt3)
  {
    this.BurnRate = paramInt1;
    this.BurnSteps = paramInt2;
    this.BurnWeight = paramInt3;
  }

  public boolean isOn()
  {
    return this.GridOn;
  }

  public void setOn(boolean paramBoolean)
  {
    updateGridSize();

    this.GridOn = paramBoolean;

    super.repaint();
  }

  public void burnGridOn()
  {
    updateGridSize();

    this.BurnStepX = 0;
    this.BurnStepY = 0;
    this.BurnOn = true;
    this.GridOn = true;
    try
    {
      UtilityThread localUtilityThread = new UtilityThread(this.BurnRate, this, getClass().getMethod("burnOnStep", null), false);
      localUtilityThread.start();

      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
    }
  }

  public void burnGridOff()
  {
    updateGridSize();

    this.BurnOff = true;
    this.GridOn = false;
    this.BurnStepX = 0;
    this.BurnStepY = 0;
    try
    {
      UtilityThread localUtilityThread = new UtilityThread(this.BurnRate, this, getClass().getMethod("burnOffStep", null), false);
      localUtilityThread.start();

      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localNoSuchMethodException.printStackTrace();
    }
  }

  protected void updateGridSize()
  {
    int i = (int)Math.floor(super.getSize().width / this.CellWidth);
    int j = (int)Math.floor(super.getSize().height / this.CellHeight);
    this.GridSize = new Dimension(i, j);
  }

  public boolean burnOnStep()
  {
    this.BurnStepX += 1;
    this.BurnStepY += 1;

    if ((this.BurnStepX > this.GridSize.width + this.BurnSteps) && (this.BurnStepY > this.GridSize.height + this.BurnSteps))
    {
      this.BurnOn = false;
      super.repaint();
      return false;
    }

    super.repaint(this.BurnRate);
    return true;
  }

  public boolean burnOffStep()
  {
    this.BurnStepX += 1;
    this.BurnStepY += 1;

    if ((this.BurnStepX > this.GridSize.width + this.BurnSteps) && (this.BurnStepY > this.GridSize.height + this.BurnSteps))
    {
      this.BurnOff = false;
      super.repaint();
      return false;
    }

    super.repaint(this.BurnRate);
    return true;
  }

  protected Color brighten(Color paramColor, int paramInt)
  {
    int i = Math.min(paramColor.getRed() + paramInt, 255);
    int j = Math.min(paramColor.getGreen() + paramInt, 255);
    int k = Math.min(paramColor.getBlue() + paramInt, 255);
    Color localColor = new Color(i, j, k);

    return localColor;
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    int i;
    int j;
    int k;
    int l;
    int i1;
    int i2;
    int i3;
    if (this.BurnOff)
    {
      j = Math.max(this.BurnStepX - this.BurnSteps, 0);
      k = Math.max(this.BurnStepY - this.BurnSteps, 0);
      l = j; for (i1 = j * this.CellWidth; l <= this.GridSize.width; )
      {
        i = this.BurnStepX - l + 1;
        if (i > 0)
          paramGraphics.setColor(brighten(this.GridColor, this.BurnWeight * i));
        else
          paramGraphics.setColor(this.GridColor);

        paramGraphics.drawLine(i1, 0, i1, super.getSize().height);

        ++l; i1 += this.CellWidth;
      }

      i2 = k; for (i3 = k * this.CellHeight; i2 <= this.GridSize.height; )
      {
        i = this.BurnStepY - i2 + 1;
        if (i > 0)
          paramGraphics.setColor(brighten(this.GridColor, this.BurnWeight * i));
        else
          paramGraphics.setColor(this.GridColor);

        paramGraphics.drawLine(0, i3, super.getSize().width, i3);

        ++i2; i3 += this.CellHeight;
      }
      return;
    }

    if (this.BurnOn)
    {
      j = Math.min(this.BurnStepX, this.GridSize.width);
      k = Math.min(this.BurnStepY, this.GridSize.height);
      l = 0; for (i1 = 0; l <= j; )
      {
        i = l - this.BurnStepX + this.BurnSteps;
        if (i > 0)
          paramGraphics.setColor(brighten(this.GridColor, this.BurnWeight * i));
        else
          paramGraphics.setColor(this.GridColor);

        paramGraphics.drawLine(i1, 0, i1, super.getSize().height);

        ++l; i1 += this.CellWidth;
      }

      i2 = 0; for (i3 = 0; i2 <= k; )
      {
        i = i2 - this.BurnStepY + this.BurnSteps;
        if (i > 0)
          paramGraphics.setColor(brighten(this.GridColor, this.BurnWeight * i));
        else
          paramGraphics.setColor(this.GridColor);

        paramGraphics.drawLine(0, i3, super.getSize().width, i3);

        ++i2; i3 += this.CellHeight;
      }
      return;
    }

    if (this.GridOn)
    {
      paramGraphics.setColor(this.GridColor);
      i = 0; for (j = 0; i <= this.GridSize.width; )
      {
        paramGraphics.drawLine(j, 0, j, super.getSize().height);

        ++i; j += this.CellWidth;
      }

      k = 0; for (l = 0; k <= this.GridSize.height; )
      {
        paramGraphics.drawLine(0, l, super.getSize().width, l);

        ++k; l += this.CellHeight;
      }
    }
  }
}