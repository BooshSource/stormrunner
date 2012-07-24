package com.templar.games.stormrunner.templarutil.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

public class DigitalGauge extends Component
{
  protected double Min;
  protected double Max;
  protected int Steps;
  protected Color[] SegmentColors;
  protected double Value;
  protected int StartAngle = 180;
  protected int EndAngle;
  protected Color Background;
  protected int ArcLength;
  protected int FinalSegment;
  protected Vector ImageListeners = new Vector();

  public DigitalGauge(double paramDouble1, double paramDouble2, int paramInt, Color[] paramArrayOfColor)
  {
    this.Min = paramDouble1;
    this.Max = paramDouble2;
    this.Steps = paramInt;
    this.SegmentColors = paramArrayOfColor;

    calculateArc();
  }

  public void setMinMax(double paramDouble1, double paramDouble2)
  {
    this.Min = paramDouble1;
    this.Max = paramDouble2;

    calculateArc();
  }

  public void setMax(double paramDouble)
  {
    this.Max = paramDouble;

    calculateArc();
  }

  public void setMin(double paramDouble)
  {
    this.Min = paramDouble;

    calculateArc();
  }

  public double getValue()
  {
    return this.Value;
  }

  public void setValue(double paramDouble)
  {
    if ((paramDouble >= this.Min) && (paramDouble <= this.Max))
    {
      appearanceChanged();

      this.Value = paramDouble;
      this.FinalSegment = (int)Math.round(this.Value * this.Steps / this.Max);
      repaint();
    }
  }

  public void setBackground(Color paramColor)
  {
    appearanceChanged();

    this.Background = paramColor;
  }

  public void setBoundingAngles(int paramInt1, int paramInt2)
  {
    this.StartAngle = paramInt1;
    this.EndAngle = paramInt2;

    calculateArc();
  }

  protected void calculateArc()
  {
    appearanceChanged();
    int i;
    if (this.EndAngle > this.StartAngle)
      i = this.StartAngle + (360 - this.EndAngle);
    else {
      i = this.StartAngle - this.EndAngle;
    }
    this.ArcLength = (int)Math.ceil(i / this.Steps);
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    Dimension localDimension = getSize();

    paramGraphics.setColor(this.Background);
    paramGraphics.fillRect(0, 0, localDimension.width, localDimension.height);

    int i = this.StartAngle;
    for (int j = 0; j < this.FinalSegment; j++)
    {
      paramGraphics.setColor(this.SegmentColors[j]);
      paramGraphics.fillArc(0, 0, localDimension.width, localDimension.height, i, -this.ArcLength);
      i -= this.ArcLength;
    }
  }

  public void addImageListener(ImageListener paramImageListener)
  {
    this.ImageListeners.addElement(paramImageListener);
  }

  public void removeImageListener(ImageListener paramImageListener)
  {
    this.ImageListeners.removeElement(paramImageListener);
  }

  public void appearanceChanged()
  {
    for (int i = 0; i < this.ImageListeners.size(); i++)
    {
      ImageListener localImageListener = (ImageListener)this.ImageListeners.elementAt(i);
      localImageListener.imageChanged(new ImageEvent(this, 5));
    }
  }
}