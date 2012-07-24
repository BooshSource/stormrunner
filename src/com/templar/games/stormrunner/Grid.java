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

    repaint();
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
    int i = (int)Math.floor(getSize().width / this.CellWidth);
    int j = (int)Math.floor(getSize().height / this.CellHeight);
    this.GridSize = new Dimension(i, j);
  }

  public boolean burnOnStep()
  {
    this.BurnStepX += 1;
    this.BurnStepY += 1;

    if ((this.BurnStepX > this.GridSize.width + this.BurnSteps) && (this.BurnStepY > this.GridSize.height + this.BurnSteps))
    {
      this.BurnOn = false;
      repaint();
      return false;
    }

    repaint(this.BurnRate);
    return true;
  }

  public boolean burnOffStep()
  {
    this.BurnStepX += 1;
    this.BurnStepY += 1;

    if ((this.BurnStepX > this.GridSize.width + this.BurnSteps) && (this.BurnStepY > this.GridSize.height + this.BurnSteps))
    {
      this.BurnOff = false;
      repaint();
      return false;
    }

    repaint(this.BurnRate);
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
    int j;
    int k;
    int m;
    int n;
    int i;
    int i1;
    int i2;
    if (this.BurnOff)
    {
      j = Math.max(this.BurnStepX - this.BurnSteps, 0);
      k = Math.max(this.BurnStepY - this.BurnSteps, 0);
      m = j; for (n = j * this.CellWidth; m <= this.GridSize.width; n += this.CellWidth)
      {
        i = this.BurnStepX - m + 1;
        if (i > 0)
          paramGraphics.setColor(brighten(this.GridColor, this.BurnWeight * i));
        else {
          paramGraphics.setColor(this.GridColor);
        }
        paramGraphics.drawLine(n, 0, n, getSize().height);

        m++;
      }

      i1 = k; for (i2 = k * this.CellHeight; i1 <= this.GridSize.height; i2 += this.CellHeight)
      {
        i = this.BurnStepY - i1 + 1;
        if (i > 0)
          paramGraphics.setColor(brighten(this.GridColor, this.BurnWeight * i));
        else {
          paramGraphics.setColor(this.GridColor);
        }
        paramGraphics.drawLine(0, i2, getSize().width, i2);

        i1++;
      }
      return;
    }

    if (this.BurnOn)
    {
      j = Math.min(this.BurnStepX, this.GridSize.width);
      k = Math.min(this.BurnStepY, this.GridSize.height);
      m = 0; for (n = 0; m <= j; n += this.CellWidth)
      {
        i = m - this.BurnStepX + this.BurnSteps;
        if (i > 0)
          paramGraphics.setColor(brighten(this.GridColor, this.BurnWeight * i));
        else {
          paramGraphics.setColor(this.GridColor);
        }
        paramGraphics.drawLine(n, 0, n, getSize().height);

        m++;
      }

      i1 = 0; for (i2 = 0; i1 <= k; i2 += this.CellHeight)
      {
        i = i1 - this.BurnStepY + this.BurnSteps;
        if (i > 0)
          paramGraphics.setColor(brighten(this.GridColor, this.BurnWeight * i));
        else {
          paramGraphics.setColor(this.GridColor);
        }
        paramGraphics.drawLine(0, i2, getSize().width, i2);

        i1++;
      }
      return;
    }

    if (this.GridOn)
    {
      paramGraphics.setColor(this.GridColor);
      i = 0; for (j = 0; i <= this.GridSize.width; j += this.CellWidth)
      {
        paramGraphics.drawLine(j, 0, j, getSize().height);

        i++;
      }

      k = 0; for (m = 0; k <= this.GridSize.height; m += this.CellHeight)
      {
        paramGraphics.drawLine(0, m, getSize().width, m);

        k++;
      }
    }
  }
}