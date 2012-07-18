package com.templar.games.stormrunner.program.editor;

import com.templar.games.stormrunner.templarutil.gui.SimpleContainer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Hashtable;

public class ProgramContainer extends SimpleContainer
{
  protected Editor editor;
  protected StartBlock CurrentStart;
  protected int BoundX;
  protected int BoundY;
  protected int BoundW;
  protected int BoundH;
  protected Rectangle Bounds;

  public ProgramContainer(Editor paramEditor)
  {
    this.editor = paramEditor;

    this.BoundX = Editor.EditorInsets.left;
    this.BoundY = Editor.EditorInsets.top;
    this.BoundW = (this.editor.getSize().width - Editor.EditorInsets.left - Editor.EditorInsets.right);
    this.BoundH = (this.editor.getSize().height - Editor.EditorInsets.top - Editor.EditorInsets.bottom);
    this.Bounds = new Rectangle(this.BoundX, this.BoundY, this.BoundW, this.BoundH);

    this.CurrentStart = new StartBlock(this.editor.getProgram(), (Image)this.editor.getProgramPartImages().get("StartBlock"), this.editor.getPalette());
    this.CurrentStart.setLocation(120, 26);
    add(this.CurrentStart);
  }

  public Editor getEditor()
  {
    return this.editor;
  }

  public StartBlock getStartBlock()
  {
    return this.CurrentStart;
  }

  public synchronized void setLocation(int paramInt1, int paramInt2)
  {
    setLocation(paramInt1, paramInt2);

    this.BoundX = (Math.abs(paramInt1) + Editor.EditorInsets.left);
    this.BoundY = (Math.abs(paramInt2) + Editor.EditorInsets.top);
    this.Bounds.setLocation(this.BoundX, this.BoundY);
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }

  public synchronized void paint(Graphics paramGraphics)
  {
    Rectangle localRectangle = this.Bounds.intersection(paramGraphics.getClipBounds());

    if ((localRectangle.width > 0) && (localRectangle.height > 0))
    {
      paramGraphics.setClip(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);

      paramGraphics.setColor(Color.black);

      paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);

      super.paint(paramGraphics);
    }
  }
}