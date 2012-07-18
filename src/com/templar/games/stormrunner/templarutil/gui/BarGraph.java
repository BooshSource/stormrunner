package com.templar.games.stormrunner.templarutil.gui;

import com.templar.games.stormrunner.GameApplet;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Window;
import java.util.Vector;

public class BarGraph extends Frame
{
  Vector queue;
  boolean active;

  public BarGraph()
  {
    super("Tick Graph");

    setSize(250, 300);
    addWindowListener(new 1(this));
    this.active = false;
    if (GameApplet.thisApplet != null)
      this.active = (GameApplet.thisApplet.getParameter("GRAPH") != null);
    else
      try
      {
        this.active = (System.getProperty("GRAPH") != null);
      }
      catch (Exception localException)
      {
        this.active = false;
      }
    if (this.active)
    {
      this.queue = new Vector();
      setVisible(true);
    }
  }

  public void update(Graphics paramGraphics) {
    paint(paramGraphics);
  }

  public synchronized void paint(Graphics paramGraphics) {
    while ((this.queue != null) && (this.queue.size() > 0))
    {
      int i = ((Integer)this.queue.elementAt(0)).intValue();
      paramGraphics.copyArea(0, 0, 200, 250, -1, 0);
      Color localColor = getBackground();
      paramGraphics.setColor(localColor);
      paramGraphics.drawLine(199, 0, 199, 250);
      if (i < 50)
        localColor = Color.green;
      if ((i < 100) && (i >= 50))
        localColor = Color.blue;
      if ((i >= 100) && (i < 150))
        localColor = Color.yellow;
      if (i >= 150)
        localColor = Color.red;
      paramGraphics.setColor(localColor);
      paramGraphics.drawLine(199, 250 - i + 1, 199, 250);
      this.queue.removeElementAt(0);
    }
  }

  public void addGraphValue(int paramInt) {
    if (this.active)
    {
      this.queue.addElement(new Integer(paramInt));
      repaint();
    }
  }

  public void setVisible(boolean paramBoolean) {
    if (this.active)
      setVisible(paramBoolean);
  }
}