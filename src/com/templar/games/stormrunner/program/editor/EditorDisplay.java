package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;

public class EditorDisplay extends SimpleContainer
{
  public Editor editor;
  protected int ClipperWidth;
  protected int ClipperHeight;
  protected Rectangle Clipper;

  public EditorDisplay(Editor paramEditor)
  {
    this.editor = paramEditor;

    this.ClipperWidth = 311;
    this.ClipperHeight = 228;
    this.Clipper = new Rectangle(0, 0, this.ClipperWidth, this.ClipperHeight);

    setLayout(null);
  }

  public Editor getEditor()
  {
    return this.editor;
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public void paint(Graphics paramGraphics)
  {
    Rectangle localRectangle = this.Clipper.intersection(paramGraphics.getClipBounds());

    if ((localRectangle.width > 0) && (localRectangle.height > 0))
    {
      paramGraphics.setClip(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
      super.paint(paramGraphics);
    }
  }
}