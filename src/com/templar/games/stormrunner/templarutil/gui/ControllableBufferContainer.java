package com.templar.games.stormrunner.templarutil.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ControllableBufferContainer extends SimpleContainer
  implements ComponentListener
{
  private Image BufferImage;
  private Graphics BufferGraphics;
  private boolean BufferTainted = true;
  private boolean TaintEntireScreen = true;
  private Rectangle TaintArea;

  public synchronized void taintBuffer()
  {
    this.BufferTainted = true;
    this.TaintEntireScreen = true;
  }

  public synchronized void taintBuffer(Rectangle paramRectangle)
  {
    this.BufferTainted = true;

    if (!(this.TaintEntireScreen))
    {
      if (this.TaintArea == null)
      {
        this.TaintArea = paramRectangle;

        return;
      }

      this.TaintArea.add(paramRectangle);
    }
  }

  public boolean isBufferTainted()
  {
    return this.BufferTainted;
  }

  public Rectangle getTaintArea()
  {
    return this.TaintArea;
  }

  public void addNotify()
  {
    addNotify();

    if (this.BufferImage == null)
    {
      initializeBuffer();
    }

    addComponentListener(this);
  }

  public void removeNotify()
  {
    removeNotify();

    removeComponentListener(this); }

  public void componentHidden(ComponentEvent paramComponentEvent) { }

  public void componentShown(ComponentEvent paramComponentEvent) { }

  public void componentMoved(ComponentEvent paramComponentEvent) { }

  public void componentResized(ComponentEvent paramComponentEvent) {
    initializeBuffer();
  }

  protected void initializeBuffer()
  {
    if (this.BufferGraphics != null)
      this.BufferGraphics.dispose();

    if (this.BufferImage != null) {
      this.BufferImage.flush();
    }

    this.BufferImage = createImage(getSize().width, getSize().height);
    this.BufferGraphics = this.BufferImage.getGraphics();
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    if (this.BufferTainted)
    {
      if (this.TaintEntireScreen) {
        this.BufferGraphics.setClip(getBounds());
      }
      else
      {
        this.BufferGraphics.setClip(this.TaintArea.intersection(getBounds()));
      }
      super.paint(this.BufferGraphics);

      this.BufferTainted = false;
      this.TaintEntireScreen = false;
      this.TaintArea = null;
    }

    paramGraphics.drawImage(this.BufferImage, 0, 0, this);
  }
}