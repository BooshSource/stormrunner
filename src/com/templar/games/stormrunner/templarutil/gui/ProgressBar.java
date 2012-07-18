package com.templar.games.stormrunner.templarutil.gui;

import com.templar.games.stormrunner.templarutil.Debug;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class ProgressBar extends SimpleContainer
{
  protected int Value;
  protected int Maximum;
  protected Image Background;
  protected Image BarTile;
  protected boolean Stretch;
  protected String MessageLeft;
  protected String MessageRight;
  protected Font MessageFont;
  protected Color BackgroundColour;
  protected Color ForegroundColour;
  protected Color XORColour;
  private FontMetrics MessageFM;

  public ProgressBar()
  {
    this(0, 100);
  }

  public ProgressBar(int paramInt1, int paramInt2) {
    this(paramInt1, paramInt2, "");
  }

  public ProgressBar(int paramInt1, int paramInt2, String paramString) {
    this(paramInt1, paramInt2, paramString, new Font("SansSerif", 0, 10));
  }

  public ProgressBar(int paramInt1, int paramInt2, String paramString, Font paramFont) {
    this(paramInt1, paramInt2, paramString, paramFont, Color.black, Color.yellow, false);
  }

  public ProgressBar(int paramInt1, int paramInt2, String paramString, Font paramFont, Color paramColor1, Color paramColor2, boolean paramBoolean) {
    setValue(paramInt1);
    setMaximum(paramInt2);
    setMessage(paramString);
    setFont(paramFont);
    setBackground(paramColor1);
    setForeground(paramColor2);
    setXOR(paramBoolean);
  }

  public ProgressBar(int paramInt1, int paramInt2, String paramString, Font paramFont, Image paramImage1, Image paramImage2, Color paramColor, boolean paramBoolean) {
    setValue(paramInt1);
    setMaximum(paramInt2);
    setMessage(paramString);
    setFont(paramFont);
    setBackground(paramImage1);
    setForeground(paramImage2);
    setXOR(paramColor);
    setTileForegroundImage(paramBoolean);
  }

  public void setFont(Font paramFont) {
    this.MessageFont = paramFont;
    this.MessageFM = Toolkit.getDefaultToolkit().getFontMetrics(paramFont);
  }

  public void setValue(int paramInt) {
    this.Value = paramInt;
  }

  public void setMaximum(int paramInt) {
    this.Maximum = paramInt;
  }

  public void setMessage(String paramString) {
    int i = paramString.indexOf("%");
    if ((i != -1) && (paramString.indexOf("%%") != i))
    {
      this.MessageLeft = paramString.substring(0, i);
      if (i >= paramString.length()) return;
      this.MessageRight = paramString.substring(i + 1);

      return;
    }

    this.MessageLeft = paramString;
    this.MessageRight = "";
  }

  public void setBackground(Color paramColor)
  {
    this.BackgroundColour = paramColor;
    this.Background = null;
  }

  public void setForeground(Color paramColor) {
    this.ForegroundColour = paramColor;
    this.BarTile = null;
  }

  public void setForeground(Image paramImage) {
    this.BarTile = paramImage;
    this.ForegroundColour = null;
  }

  public void setBackground(Image paramImage) {
    this.Background = paramImage;
    this.BackgroundColour = null;
  }

  public void setXOR(boolean paramBoolean) {
    if (paramBoolean) {
      this.XORColour = this.ForegroundColour;

      return;
    }

    this.XORColour = null;
  }

  public void setXOR(Color paramColor) {
    this.XORColour = paramColor;
  }

  public void setTileForegroundImage(boolean paramBoolean) {
    this.Stretch = (!(paramBoolean));
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics) {
    double d1 = this.Value;
    double d2 = this.Maximum;
    double d3 = d1 / d2;
    int i = getSize().width;
    int j = (int)(d3 * i);
    if (this.BackgroundColour != null)
    {
      paramGraphics.setColor(this.BackgroundColour);
      paramGraphics.fillRect(0, 0, getSize().width, getSize().height);
    }
    else if (this.Background != null) {
      int k = 0; for (l = this.Background.getWidth(null); k < getSize().width; k += l)
        paramGraphics.drawImage(this.Background, k, 0, l, getSize().height, null);
    } else {
      Debug.println("ProgressBar: Error, nothing to draw background with.");
    }
    if (this.ForegroundColour != null)
    {
      if (this.XORColour != null)
        paramGraphics.setXORMode(this.XORColour);
      else
        paramGraphics.setColor(this.ForegroundColour);
      paramGraphics.fillRect(0, 0, j, getSize().height);
      paramGraphics.setPaintMode();
    }
    else if (this.BarTile != null)
    {
      if (this.Stretch) {
        paramGraphics.drawImage(this.BarTile, 0, 0, j, getSize().height, null);
      }
      else {
        localObject = paramGraphics.create(0, 0, j, getSize().height);
        l = 0; for (int i1 = this.BarTile.getWidth(null); l < j; l += i1)
          ((Graphics)localObject).drawImage(this.BarTile, l, 0, i1, getSize().height, null);
        ((Graphics)localObject).dispose();
      }
    }
    else {
      Debug.println("ProgressBar: Error, nothing to draw foreground with.");
    }
    paramGraphics.setFont(this.MessageFont);
    Object localObject = new StringBuffer(this.MessageLeft);
    ((StringBuffer)localObject).append((int)(d3 * 100.0D));
    ((StringBuffer)localObject).append("%");
    ((StringBuffer)localObject).append(this.MessageRight);

    int l = this.MessageFM.stringWidth(((StringBuffer)localObject).toString());
    if (this.XORColour == null) {
      if (this.ForegroundColour == null)
      {
        paramGraphics.setXORMode(getBackground());
      }
      else
      {
        paramGraphics.setXORMode(this.BackgroundColour);
      }

    }
    else
      paramGraphics.setXORMode(this.XORColour);

    paramGraphics.drawString(((StringBuffer)localObject).toString(), getSize().width / 2 - l / 2, getSize().height / 2 + this.MessageFM.getAscent() / 2);

    paramGraphics.setPaintMode(); }

  public int getValue() { return this.Value; } 
  public int getMaximum() { return this.Maximum;
  }
}